package com.mcredit.business.debt_home.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.modelmapper.ValidationException;

import com.mcredit.business.debt_home.factory.MainFactory;
import com.mcredit.common.Messages;
import com.mcredit.data.pcb.entity.CreditBureauData;
import com.mcredit.model.dto.DowloadZipDTO;
import com.mcredit.model.dto.debt_home.DebtContractInfo;
import com.mcredit.model.dto.debt_home.DebtContractPcbDTO;
import com.mcredit.model.dto.debt_home.DebtHomeAllFile;
import com.mcredit.model.dto.debt_home.DebtHomeAssignDTO;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.object.StateResponse;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.manager.BaseManager;
import com.mcredit.util.DateUtil;
import com.mcredit.util.StringUtils;
import com.mcredit.model.dto.debt_home.DebtContractPcbDTO;
import com.mcredit.data.pcb.entity.CreditBureauData;

public class DebtHomeManager extends BaseManager {

	public Integer assignDebtHome(List<DebtHomeAssignDTO> debtHomeAssigns, String currentUser) throws Exception {

		return this.tryCatch(() -> {
			MainFactory.validation(this.uok).validateAssign(debtHomeAssigns);
			return MainFactory.assignAggreate(this.uok).assignDebtHome(debtHomeAssigns, currentUser);
		});
	}

	public List<DebtHomeAllFile> lookup(String contractNumber, String currentUser, boolean isRemoveFilePath,
			boolean isCheckPermission) throws Exception {
		return this.tryCatch(() -> {
			MainFactory.validation(this.uok).validateLookup(contractNumber, currentUser, isCheckPermission);
			return MainFactory.searchFilesAggreate(this.uok).lookup(contractNumber, currentUser, isRemoveFilePath);
		});
	}

	public String getFilePath(Long fileId, String loginId) throws Exception {
		return this.tryCatch(() -> {
			MainFactory.validation(this.uok).validateGetFilePath(fileId, loginId);
			return MainFactory.searchFilesAggreate(this.uok).getFilePath(fileId);

		});
	}

	public File zipDocument(String contractNumber, String currentUser, boolean isCheckPermission) throws Exception {
		return this.tryCatch(() -> {
			MainFactory.validation(this.uok).validateLookup(contractNumber, currentUser, isCheckPermission);

			List<DebtHomeAllFile> documentLst = MainFactory.searchFilesAggreate(this.uok).lookup(contractNumber,
					currentUser, false);

			if (documentLst != null && !documentLst.isEmpty()) {

				String folderSave = CacheManager.Parameters()
						.findParamValueAsString(ParametersName.TMP_FOLDER_UPLOAD_DEBT_HOME);

				File folderDir = new File(folderSave);
				if (folderDir.exists() && folderDir.isDirectory()) {
					folderDir.mkdir();
				}

				String fileName = CacheManager.Parameters()
						.findParamValueAsString(ParametersName.TMP_FOLDER_UPLOAD_DEBT_HOME) + contractNumber + ".zip";

				FileOutputStream fos = new FileOutputStream(fileName);
				ZipOutputStream zipOut = new ZipOutputStream(fos);

				int i = 1;
				for (DebtHomeAllFile item : documentLst) {
					File fileOrigin = new File(item.getFilePath());

					if (!fileOrigin.exists()) {
						System.out.println(Messages.getString("validation.not.exists",
								"File: \"'\"" + item.getFilePath() + "\"'\""));
						continue;
					}

					String tmpFile = "";
					try {
						tmpFile = CacheManager.Parameters()
								.findParamValueAsString(ParametersName.TMP_FOLDER_UPLOAD_DEBT_HOME)
								+ DateUtil.toString(new Date(), "yyyyMMdd") + i + "_" + item.getDocName();
					} catch (ParseException ex) {
						System.out.println("DebtHomeManager.zipDocument.ex.105: " + ex.toString());
					}

					File fileToZip = new File(tmpFile);
					FileUtils.copyFile(fileOrigin, fileToZip);
					FileInputStream fis = new FileInputStream(fileToZip);

					ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
					zipOut.putNextEntry(zipEntry);

					byte[] bytes = new byte[1024];
					int length;
					while ((length = fis.read(bytes)) >= 0) {
						zipOut.write(bytes, 0, length);
					}

					fis.close();
					fileToZip.delete();
					i++;
				}
				zipOut.close();
				fos.close();

				return new File(fileName);
			}
			return null;
		});
	}

	public DowloadZipDTO zipDocumentV2(String contractNumber, String currentUser, boolean isCheckPermission)
			throws Exception {
		return this.tryCatch(() -> {
			MainFactory.validation(this.uok).validateLookup(contractNumber, currentUser, isCheckPermission);

			List<DebtHomeAllFile> documentLst = MainFactory.searchFilesAggreate(this.uok).lookup(contractNumber,
					currentUser, false);

			if (documentLst != null && !documentLst.isEmpty()) {

				String fileName = CacheManager.Parameters()
						.findParamValueAsString(ParametersName.TMP_FOLDER_UPLOAD_DEBT_HOME) + contractNumber + ".zip";

				FileOutputStream fos = new FileOutputStream(fileName);

				ZipOutputStream zipOut = new ZipOutputStream(fos);

				int i = 1;
				for (DebtHomeAllFile item : documentLst) {
					File fileOrigin = new File(item.getFilePath());

					if (!fileOrigin.exists()) {
						System.out.println(Messages.getString("validation.not.exists",
								"File: \"'\"" + item.getFilePath() + "\"'\""));
						continue;
					}

					String tmpFile = "";
					try {
						tmpFile = CacheManager.Parameters()
								.findParamValueAsString(ParametersName.TMP_FOLDER_UPLOAD_DEBT_HOME)
								+ DateUtil.toString(new Date(), "yyyyMMdd") + i + "_" + item.getDocName();
					} catch (ParseException ex) {
						System.out.println("DebtHomeManager.zipDocument.ex.105: " + ex.toString());
					}

					File fileToZip = new File(tmpFile);
					FileUtils.copyFile(fileOrigin, fileToZip);
					FileInputStream fis = new FileInputStream(fileToZip);

					ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
					zipOut.putNextEntry(zipEntry);

					byte[] bytes = new byte[1024];
					int length;
					while ((length = fis.read(bytes)) >= 0) {
						zipOut.write(bytes, 0, length);
					}

					fis.close();
					fileToZip.delete();
					i++;
				}
				zipOut.close();
				fos.close();

				return new DowloadZipDTO(fileName, contractNumber + ".zip");
			}
			return null;
		});
	}

	public StateResponse dropDocument(String contractNumber, String currentUser) throws Exception {

		return this.tryCatch(() -> {

			MainFactory.validation(this.uok).validateLookup(contractNumber, currentUser, true);

			String fileName = CacheManager.Parameters()
					.findParamValueAsString(ParametersName.TMP_FOLDER_UPLOAD_DEBT_HOME) + contractNumber + ".zip";

			return new StateResponse(Files.deleteIfExists(new File(fileName).toPath()));
		});
	}

	/**
	 * Get list contract information
	 * @param contractNumber
	 * @param appNumber
	 * @param custMobilePhone
	 * @param identityNumber
	 * @return
	 * @throws Exception
	 */
	public List<DebtContractInfo> lookupContract(String contractNumber, String appNumber, String custMobilePhone,
			String identityNumber) throws Exception {
		return this.tryCatch(() -> {
			MainFactory.validation(this.uok).validateInputLookupContract(contractNumber, appNumber, custMobilePhone,
					identityNumber);
			return MainFactory.searchFilesAggreate(this.uok).lookupContractInfo(contractNumber.trim(), appNumber.trim(),
					custMobilePhone.trim(), identityNumber.trim());
		});
	}

	public List<DebtHomeAllFile> lookupFile(String appNumber) throws Exception {
		return this.tryCatch(() -> {
			MainFactory.validation(this.uok).validateFindFile(appNumber);
			return MainFactory.searchFilesAggreate(this.uok).lookupFile(appNumber);
		});
	}
	
	public String downloadFileContract(Long fileId) throws Exception {
		return this.tryCatch(() -> {
			MainFactory.validation(this.uok).validateViewFileContract(fileId);
			return MainFactory.searchFilesAggreate(this.uok).getFilePath(fileId);

		});
	}

	public File downloadAllFileContract(String appNumber) throws Exception {
		return this.tryCatch(() -> {
//			List<DebtHomeAllFile> documentLst = this.lookupFile(appNumber);

			List<DebtHomeAllFile> documentLst = MainFactory.searchFilesAggreate(this.uok).lookupFile(appNumber);
			if (documentLst != null && !documentLst.isEmpty()) {

				String folderSave = CacheManager.Parameters()
						.findParamValueAsString(ParametersName.TMP_FOLDER_UPLOAD_DEBT_HOME);

				File folderDir = new File(folderSave);
				if (folderDir.exists() && folderDir.isDirectory()) {
					folderDir.mkdir();
				}

				String fileName = CacheManager.Parameters()
						.findParamValueAsString(ParametersName.TMP_FOLDER_UPLOAD_DEBT_HOME) + appNumber + ".zip";

				FileOutputStream fos = new FileOutputStream(fileName);
				ZipOutputStream zipOut = new ZipOutputStream(fos);

				int i = 1;
				for (DebtHomeAllFile item : documentLst) {
					File fileOrigin = new File(item.getFilePath());

					if (!fileOrigin.exists()) {
						System.out.println(Messages.getString("validation.not.exists",
								"File: \"'\"" + item.getFilePath() + "\"'\""));
						continue;
					}

					String tmpFile = "";
					try {
						tmpFile = CacheManager.Parameters()
								.findParamValueAsString(ParametersName.TMP_FOLDER_UPLOAD_DEBT_HOME)
								+ DateUtil.toString(new Date(), "yyyyMMdd") + i + "_" + item.getDocName();
					} catch (ParseException ex) {
						System.out.println("DebtHomeManager.downloadAllFileContract.ex.105: " + ex.toString());
					}

					File fileToZip = new File(tmpFile);
					FileUtils.copyFile(fileOrigin, fileToZip);
					FileInputStream fis = new FileInputStream(fileToZip);

					ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
					zipOut.putNextEntry(zipEntry);

					byte[] bytes = new byte[1024];
					int length;
					while ((length = fis.read(bytes)) >= 0) {
						zipOut.write(bytes, 0, length);
					}

					fis.close();
					fileToZip.delete();
					i++;
				}
				zipOut.close();
				fos.close();

				return new File(fileName);
			}
			return null;
		});
	}

	public StateResponse dropZipFileContract(String appNumber) throws Exception {
		return this.tryCatch(() -> {

			MainFactory.validation(this.uok).validateFindFile(appNumber);

			String fileName = CacheManager.Parameters()
					.findParamValueAsString(ParametersName.TMP_FOLDER_UPLOAD_DEBT_HOME) + appNumber + ".zip";

			return new StateResponse(Files.deleteIfExists(new File(fileName).toPath()));
		});
	}

	/**
	 * Get report pcb by citizenId
	 * @param nullToEmpty
	 * @param citizenId
	 * @return
	 */
	public List<DebtContractPcbDTO> getPcbByCitizenId(String appNumber, String citizenId, String oldCitizenID) {
		CreditBureauData pcbCitizenID = this.uok.pcb.creditBureauDataRepository().getPcbByCitizenId(citizenId);
		CreditBureauData pcbOldCitizenID = this.uok.pcb.creditBureauDataRepository().getPcbByCitizenId(oldCitizenID);
		List<DebtContractPcbDTO> lstPcb = new ArrayList<DebtContractPcbDTO>();
		DebtContractPcbDTO debtPcb = null;
		debtPcb = (pcbCitizenID == null) ? new DebtContractPcbDTO(false, null, null) : new DebtContractPcbDTO(true, pcbCitizenID.getId(), null);
		lstPcb.add(debtPcb);
		debtPcb = (pcbOldCitizenID == null) ? new DebtContractPcbDTO(false, null, null) : new DebtContractPcbDTO(true, pcbOldCitizenID.getId(), null);
		lstPcb.add(debtPcb);
		return lstPcb;
	}
}
