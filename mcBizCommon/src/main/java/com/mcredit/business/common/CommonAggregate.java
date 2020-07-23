package com.mcredit.business.common;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.mcredit.business.common.utils.CaseUtils;
import com.mcredit.business.common.utils.FTPUtils;
import com.mcredit.data.common.UnitOfWorkCommon;
import com.mcredit.data.common.entity.Commodities;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.common.entity.Product;
import com.mcredit.model.enums.CommonValidationLength;
import com.mcredit.model.enums.MsgChannel;
import com.mcredit.model.enums.MsgStatus;
import com.mcredit.model.enums.MsgTransType;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.object.system.BPMDocument;
import com.mcredit.model.object.system.BPMDocumentFile;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.esb.BPMProxy;
import com.mcredit.util.JSONConverter;

import it.sauronsoftware.ftp4j.FTPClient;

public class CommonAggregate {
	static {
		System.setProperty("file.encoding", "UTF-8");
		System.setProperty("sun.jnu.encoding", "UTF-8");
	}

	Commodities commodities;
	MessageLog messageLog;
	Product product;

	private UnitOfWorkCommon unitOfWorkCommon = null;

	public Commodities getCommodities() {
		return commodities;
	}

	public void setCommodities(Commodities commodities) {
		this.commodities = commodities;
	}

	public MessageLog getMessageLog() {
		return messageLog;
	}

	public void setMessageLog(MessageLog messageLog) {
		this.messageLog = messageLog;
	}

	/**************** End Property ***************/

	public CommonAggregate(UnitOfWorkCommon uok) {

		this.unitOfWorkCommon = uok;
	}

	/****************
	 * Begin Behavior
	 * 
	 * @throws Exception
	 ***************/
	public void upsertCommom() throws Exception {

		if (this.commodities == null)
			throw new Exception("Object commodities is null!");
		unitOfWorkCommon.commoditiesRepo().upsert(this.commodities);

	}

	public Long getMessageLogTransId() throws Exception {
		if (this.messageLog == null)
			throw new Exception("Object messageTask is null!");
		return unitOfWorkCommon.messageLogRepo().getMessageLogTransId();
	}

	public void findMessageLog() throws Exception {
		if (this.messageLog == null)
			throw new Exception("Object messageLog is null!");
		unitOfWorkCommon.messageLogRepo().upsert(this.messageLog);
	}

	public void upsertMessageLog() throws Exception {
		if (this.messageLog == null)
			throw new Exception("Object messageLog is null!");

		if (this.messageLog.getTransId() == null || this.messageLog.getTransId() <= 0)
			this.messageLog.setTransId(getMessageLogTransId());
		if (this.messageLog.getMsgOrder() == null)
			this.messageLog.setMsgOrder(CommonValidationLength.VAL_MESSAGELOG_ORDER_1.value());
		if (this.messageLog.getFromChannel() == null)
			this.messageLog.setFromChannel(MsgChannel.BPM.value());
		if (this.messageLog.getToChannel() == null)
			this.messageLog.setToChannel(MsgChannel.T24_APPLICATION.value());
		if (this.messageLog.getTransType() == null)
			this.messageLog.setTransType(MsgTransType.ISSCARD.toString());
		if (this.messageLog.getMsgStatus() == null)
			this.messageLog.setMsgStatus(MsgStatus.NEW.value());

		unitOfWorkCommon.messageLogRepo().upsert(this.messageLog);
	}

	public ArrayList<BPMDocument> getFileFromBPM(String appId) throws Exception {
		try (BPMProxy manager = new BPMProxy()) {
			ApiResult apiResult = manager.getListFileUpload(appId);

			if (!apiResult.getStatus())
				return null;
			if (apiResult.getBodyContent().isEmpty() || apiResult.getBodyContent().contains("[]"))
				return null;

			ArrayList<BPMDocument> documents = new ArrayList<>();
			documents = JSONConverter.jsonToList(apiResult.getBodyContent(), BPMDocument.class);
			return documents;
		}
	}

	public File downloadFile(String remotePath) throws Exception {
		File fileout;
		FTPClient _client = null;
		try {
			_client = FTPUtils.createFTPConnection();
			String tempDirectory = CacheManager.Parameters()
					.findParamValueAsString(ParametersName.TMP_FOLDER_CAPTURE_DEBT_HOME);
			String fileName = CaseUtils.getFileNameByPath(remotePath);
			String tempFile = tempDirectory + fileName;
			fileout = new File(tempFile);
			_client.download(remotePath, fileout);
		} catch (Exception e) {
			throw e;
		} finally {
			if (_client != null)
				_client.disconnect(true);
		}
		return fileout;
	}

	public File downloadFileBPM(String appId, String docType) throws Exception {
		String tempFileSave = CacheManager.Parameters()
				.findParamValueAsString(ParametersName.TMP_FOLDER_BPM_DATA_FILE_FAS) + docType + "_" + appId;

		File checkFile = new File(tempFileSave);
		if (checkFile.exists())
			return checkFile;

		ArrayList<BPMDocument> listFile = this.getFileFromBPM(appId);

		for (int i = 0; i < listFile.size(); i++) {
			BPMDocument document = listFile.get(i);
			ArrayList<BPMDocumentFile> files = document.getFiles();
			for (int j = 0; j < files.size(); j++) {
				if (files.get(j).getDocType().toLowerCase().contains(docType.toLowerCase())) {
					String tempDirectory = CacheManager.Parameters()
							.findParamValueAsString(ParametersName.FOLDER_BPM_DATA_FILE);
					String fileName = CaseUtils.getFileNameByPath(files.get(j).getDocPageList().get(0));
					String tempFile = tempDirectory + document.getPath() + fileName;

					if (Files.exists(Paths.get(tempFile))) {
						Files.copy(Paths.get(tempFile), Paths.get(tempFileSave));
					}

					File fileout = new File(tempFileSave);
					System.out.print("Log Download File: " + tempFile);
					System.out.print("Log Download File exists: " + fileout.exists());
					if (fileout.exists())
						return fileout;
				}
			}
		}
		return null;
	}

	/****************
	 * End Behavior
	 * 
	 * @throws Exception
	 ***************/

}
