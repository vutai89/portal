package com.mcredit.alfresco.api;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.exceptions.CmisContentAlreadyExistsException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.apache.log4j.Logger;

import com.mcredit.alfresco.utils.Constants;
import com.mcredit.data.ecm.UnitOfWorkECM;
import com.mcredit.model.enums.ECMUploadStatus;
import com.mcredit.model.object.ecm.DocumentToEcmDTO;
import com.mcredit.util.StringUtils;

public class AlfrescoECMUploadAPI {

	static Logger logger = Logger.getLogger(AlfrescoECMUploadAPI.class.getName());
	private String statMessage;
	private boolean isError = false;
	private static int MAX_RETRY_TIMES = 10;
	private UnitOfWorkECM uowECM = null;

	public List<Document> doMigrateItem(DocumentToEcmDTO docEcm, Session cmisSession) {
		statMessage = "";
		isError = false;
		List<Document> listOfDoc = null;
		try {
			// Find the root folder of our target site
			String path = Constants.loadDocumentPath;
			path = path + "/" + docEcm.getTypeOfLoan();
			CmisObject object = cmisSession.getObjectByPath(path);
			// Add to clear cache
			object.refresh();
			String typeOfLoanFolderId = object.getId();
			// Create a test document in the subFolder
			listOfDoc = createDocument(typeOfLoanFolderId, docEcm, cmisSession);

		} catch (IOException ioe) {
			statMessage = "IOException run doMigrateOnECM -  appID:= " + docEcm.getBpmAppId() + "= message:" + ioe.getMessage() + " modelItem:= " + docEcm.toString();
			logger.error(statMessage);
			isError = true;
		} catch (Exception e) {
			statMessage = "Exception run doMigrateOnECM -  appID:= " + docEcm.getBpmAppId() + " - message:" + e.getMessage()
			+ " -class:=" + e.getClass() + " modelItem:= " + docEcm.toString();
			logger.error(statMessage);
			isError = true;
		} finally {
			try {
				if(isError) {
					docEcm.setStatus(ECMUploadStatus.ERROR_UPLOAD.value());
					docEcm.setEcmObjectId(null);
					docEcm.setErrorMessage(statMessage.substring(0, 200));
				} else {
					docEcm.setStatus(ECMUploadStatus.UPLOADED.value());
					docEcm.setEcmObjectId((listOfDoc == null) ? null : listOfDoc.get(0).getId());
					docEcm.setErrorMessage(null);
					// delete local file
					try {
						// Delete file on local
						docEcm.getLocalFile().delete();
					} catch (Throwable th) {
						logger.error("delete file on local file="+ docEcm.getPath2File() + docEcm.getFilePath()+ ". error="+th.getMessage());
					}
				}
				uowECM.documentToECMRepo().updateDocumentToEcm(docEcm);
			} catch (Exception ex) {
				logger.error("Update status error="+ex.getMessage());
			}
		}

		return listOfDoc;
	}

	public Folder createFolder(String parentFolderId, String subFolderName, Session cmisSession) {
		Folder parentFolder = (Folder) cmisSession.getObject(parentFolderId);


		Folder subFolder = null;
		try {
			// Making an assumption here that you probably wouldn't normally do
			subFolder = (Folder) cmisSession.getObjectByPath(parentFolder.getPath() + "/" + subFolderName);
			logger.warn("Folder already existed!");
		} catch (CmisObjectNotFoundException onfe) {
			Map<String, Object> props = new HashMap<String, Object>();
			props.put("cmis:objectTypeId", "cmis:folder");
			props.put("cmis:name", subFolderName);
			subFolder = parentFolder.createFolder(props);
			String subFolderId = subFolder.getId();
			logger.info("Created new folder: " + subFolderId);
		} catch (Exception e) {
			statMessage = "Exception when createFolder message : " + e.getMessage() + " folderName: " + subFolderName;
			logger.error(statMessage);
			isError = true;
		}

		return subFolder;
	}

	/**
	 * Tao danh sach ho so tuong ung voi 1 model
	 * 
	 * @param parentFolder
	 * @param model
	 * @return
	 * @throws FileNotFoundException
	 */
	public List<Document> createDocument(String typeOfLoanFolderId,  DocumentToEcmDTO docEcm, Session cmisSession)
			throws FileNotFoundException {

		Document doc = null;
		Folder docName = null;
		int retry = 0;
		List<Document> docs = new ArrayList<Document>();
		Map<String, Object> props = null;
		try {
			if(Constants.TYPE_OF_LOAN_DATA_CENTRALIZE.equals(docEcm.getTypeOfLoan())) {
				// 1- kiem tra va tao folder document_type
				//Folder documentTypeFolder = createFolder(typeOfLoanFolderId, file.getDocumentType(), cmisSession);
				Folder yearFolder = createFolder(typeOfLoanFolderId, docEcm.getYear(), cmisSession);
				Folder monthFolder = createFolder(yearFolder.getId(), docEcm.getMonth(), cmisSession);
				Folder dayFolder = createFolder(monthFolder.getId(), docEcm.getDay(), cmisSession);
				// 2- kiem tra va tao folder appId
				Folder appIdFolder = createFolder(dayFolder.getId(), String.valueOf(docEcm.getBpmAppNumber().intValue()) + "-" + docEcm.getBpmAppId(), cmisSession);
				Folder folderName = createFolder(appIdFolder.getId(), docEcm.getFolderName(), cmisSession);
				docName = createFolder(folderName.getId(), docEcm.getDocumentType() + " - " + docEcm.getDocumentName(), cmisSession);
			} else {
				// 1- kiem tra va tao folder document_type
				Folder documentTypeFolder = createFolder(typeOfLoanFolderId, docEcm.getFolderName(), cmisSession);
				// 2- kiem tra va tao folder appId
				docName = createFolder(documentTypeFolder.getId(), docEcm.getBpmAppId(), cmisSession);
			}
			if (props == null) {
				props = new HashMap<String, Object>();
			}
			props.put("cmis:objectTypeId", Constants.D_MC_LOAN_DOCS);
			props.put("cmis:name", docEcm.getFileFullName());
			props.put("mc:refNumber", docEcm.getBpmAppId());
			props.put("mc:refName", "" + docEcm.getBpmAppNumber());
			props.put("mc:customerName", docEcm.getCustName());
			props.put("mc:idCardNumber", docEcm.getIdNumber());
			props.put("mc:loanTerm", docEcm.getLoanTerm());
			props.put("mc:contractNumber", (docEcm.getContractNumber() == null) ? docEcm.getLdNumber() : docEcm.getContractNumber());
			props.put("mc:productCode", docEcm.getProductCode());
			props.put("mc:productName", docEcm.getProductName());
			props.put("mc:loanAmount", docEcm.getLoanAmount());
			props.put("mc:businessStep", docEcm.getFolderName());
			props.put("mc:businessOwner", docEcm.getDocumentType() + " - " + docEcm.getDocumentName());
			retry = 0;
			doc = null;
			String lastMsg = statMessage;
			while((doc == null) && (retry < MAX_RETRY_TIMES)) {
				doc = createDocument(docName, docEcm, props, cmisSession);
				retry++;
				if(doc == null) {
					Thread.sleep(1000);
					logger.warn("Retry times for create document = "+retry);
				}
			}
		} catch (Exception e) {
			statMessage = "Exception run createDocument doMigrateOnECM -  appID:= " + docEcm.getBpmAppId() + " - message:" + e.getMessage()
			+ " -class:=" + e.getClass() + " modelItem:= " + docEcm.toString();
			logger.error(statMessage);
			isError = true;
		}
		if (doc != null) {
			docs.add(doc);
		} else {
			return null;
		}

		return docs;
	}

	/**
	 * Tao document tuong ung voi file
	 * 
	 * @param parentFolder
	 * @param file
	 * @param props
	 * @return
	 * @throws FileNotFoundException
	 */
	public Document createDocument(Folder parentFolder, DocumentToEcmDTO docEcm, Map<String, Object> props,
			Session cmisSession) throws FileNotFoundException {

		String fileName = docEcm.getFileName();
		if (docEcm.getMimeType() == null || docEcm.getMimeType().isEmpty()) {
			logger.error("mimeType is missing: " + docEcm.getFileName());
			statMessage = "mimeType is missing";
			return null;
		}
		File localFile = docEcm.getLocalFile();
		if (localFile == null || localFile.length() == 0) {
			logger.error("file local is empty: " + docEcm.getFileName());
			statMessage = "file local is empty";
			return null;
		}
		Document document = null;
		try {
			InputStream fis = new FileInputStream(localFile);
			DataInputStream dis = new DataInputStream(fis);
			byte[] bytes = new byte[(int) localFile.length()];
			dis.readFully(bytes);
			logger.info("Length of file: " + localFile.length());
			ContentStream contentStream = new ContentStreamImpl(localFile.getAbsolutePath(), BigInteger.valueOf(bytes.length), docEcm.getMimeType(),new ByteArrayInputStream(bytes));
			logger.info("Content stream: " + contentStream.getLength());
			document = parentFolder.createDocument(props, contentStream, null);
			logger.info("Created new document: " + document.getId());
			isError = false;
		} catch (CmisContentAlreadyExistsException ccaee) {
			statMessage = "Document already exists - fileName:= " + fileName + " - appId:=" + docEcm.getBpmAppId() + " -message:=" + ccaee.getMessage();
			logger.warn(statMessage);
			// Rename file
			long timeStamp = ZonedDateTime.now().toInstant().toEpochMilli();
			docEcm.setFileName(docEcm.getFileName() + "_" + Long.toString(timeStamp));
			props.put("cmis:name", docEcm.getFileFullName());
			isError = true;
		} catch (Throwable e) {
			long fileLocalLeng = localFile != null ? localFile.length() : 0;
			statMessage = "Exception when create document appId:=" + docEcm.getBpmAppId() + " fileName:=" + fileName
					+ " fileLocalLeng:=" + fileLocalLeng + " message:=" + e.getMessage();
			logger.error(statMessage);
			document = null;
			isError = true;
		}

		return document;
	}

	public UnitOfWorkECM getUowECM() {
		return uowECM;
	}

	public void setUowECM(UnitOfWorkECM uowECM) {
		this.uowECM = uowECM;
	}

}
