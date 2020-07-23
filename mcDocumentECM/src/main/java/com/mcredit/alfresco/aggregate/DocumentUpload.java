package com.mcredit.alfresco.aggregate;

import java.io.File;
import java.util.List;

import org.apache.tika.Tika;
import org.apache.tika.detect.TypeDetector;
import org.hibernate.Session;

import com.mcredit.alfresco.utils.Constants;
import com.mcredit.alfresco.utils.FTPFileUtils;
import com.mcredit.data.HibernateBase;
import com.mcredit.data.ecm.UnitOfWorkECM;
import com.mcredit.model.enums.DocumentSource;
import com.mcredit.model.enums.ECMUploadStatus;
import com.mcredit.model.enums.SessionType;
import com.mcredit.model.object.ecm.DocumentToEcmDTO;
import com.mcredit.util.StringUtils;

public class DocumentUpload {

	/** Instance of Tika facade class with Type detector. */
	private final static Tika typeTika = new Tika(new TypeDetector());

	public static UnitOfWorkECM getInstance(SessionType type) {
		UnitOfWorkECM uok = null;
		
		if(type == SessionType.THREAD)
			uok = new UnitOfWorkECM();//init Thread nhibernate session by using call getCurrentSession
		else {
			HibernateBase hibernateBase = new HibernateBase(type);//init nhibernate session depending on SessionType
			Session session = hibernateBase.getSession();
			uok = new UnitOfWorkECM(hibernateBase,session);
		}
		return uok;
	}

	public List<DocumentToEcmDTO> getDocumentsToUpload() {
		System.out.println("getDocumentsToUpload starting ...");
		UnitOfWorkECM uwe = DocumentUpload.getInstance(SessionType.JTA);
		String msg = "";
		List<DocumentToEcmDTO> upList = null;
		try {
			// Set value for document metadata
			uwe.start();
			upList = uwe.documentToECMRepo().getDocumentsToUpload();
			for(DocumentToEcmDTO dte : upList) {
				try {
					int indexOfFileSeparator = dte.getUserFileName().lastIndexOf(Constants.DOT);
					if (indexOfFileSeparator == -1) {
						msg = "Missing file extension filename="+ dte.getUserFileName();
						//isError = true;
						System.out.println("Ignore because of: file extention is missing appID:= " + dte.getBpmAppId()
								+ " - fileName:= " + dte.getUserFileName());
						dte.setStatus(ECMUploadStatus.ERROR_UPLOAD.value());
						dte.setErrorMessage(msg);
						uwe.documentToECMRepo().updateDocumentToEcm(dte);
						continue;
					}
					if(StringUtils.isNullOrEmpty(dte.getDocumentType())) {
						dte.setDocumentType(Constants.DATA_CENTRALIZE_OPERATION_DOCUMENT_TYPE);
					}
					if(!StringUtils.isNullOrEmpty(dte.getFolder())) {
						String[] split = dte.getFolder().split("/");
						dte.setYear(split[0]);
						dte.setMonth(String.format("%02d", Integer.parseInt(split[1])));
						dte.setDay(String.format("%02d", Integer.parseInt(split[2])));
					}
					if(StringUtils.isNullOrEmpty(dte.getFolderName())) {
						dte.setFolderName(Constants.DATA_CENTRALIZE_DEFAULT_FOLDER_NAME);
					}
					dte.setDocumentType(dte.getDocumentType().replaceAll("/", "-").trim());
					dte.setDocumentName(dte.getDocumentName().replaceAll("/", "-").trim());
					dte.setFileName(dte.getUserFileName().substring(0, indexOfFileSeparator));
					dte.setExtention(dte.getUserFileName().substring(indexOfFileSeparator+1));
		        	String localFilePath = dte.getUserFileName().replaceAll("/", "-");
					File tempFile = null;
					String strPath = "";
					if(DocumentSource.DATA_CENTRALIZATION.value().equals(dte.getDocumentSource())) {
						dte.setPath2File(Constants.prefixFileServerDE + dte.getServerFileName());
			        	dte.setBasePath(Constants.localDEBasePath + File.separator + dte.getBpmAppId());
			        	strPath = dte.getBpmAppId() + File.separator + dte.getFolderName() + File.separator + dte.getUserFileName();
						tempFile = FTPFileUtils.getRemoteFile(dte.getPath2File(), dte.getUserFileName(), dte.getBasePath(), localFilePath);
					} else if(DocumentSource.INSTALLMENT.value().equals(dte.getDocumentSource())
							|| DocumentSource.CASH.value().equals(dte.getDocumentSource())) {
		        		String appId = dte.getBpmAppId();
		        		int pos = 0;
		        		String part1 = appId.substring(pos, pos + 3);
		        		pos += 3;
		        		String part2 = appId.substring(pos, pos + 3);
		        		pos += 3;
		        		String part3 = appId.substring(pos, pos + 3);
		        		String part4 = appId.substring(pos + 3);
		        		String basePath = part1 + File.separator + part2 + File.separator + part3 + File.separator + part4;

						dte.setBasePath(basePath);
			        	strPath = dte.getBasePath() + File.separator + dte.getServerFileName();
						tempFile = FTPFileUtils.getRemoteFile(dte.getServerFileName(), dte.getBasePath(), 
								DocumentSource.CASH.value().equals(dte.getDocumentSource()));
					} else if(DocumentSource.FAS.value().equals(dte.getDocumentSource())) {
						if(StringUtils.isNullOrEmpty(dte.getDocumentType())) {
							dte.setDocumentType(Constants.fasDocumentType);
						}
						if(StringUtils.isNullOrEmpty(dte.getFolder())) {
							dte.setFolder(Constants.fasFolder);
						}
						tempFile = FTPFileUtils.getFasRemoteFile(dte.getServerFileName());
					} else if(DocumentSource.COLLECTION.value().equals(dte.getDocumentSource())) {
						tempFile = FTPFileUtils.getDcRemoteFile(dte.getServerFileName());
					}
					dte.setLocalFile(tempFile);
					//set mime type
					boolean exists = tempFile.exists();
					String mimeType = null;
					if (exists) {
						mimeType = typeTika.detect(strPath);
						if (StringUtils.isNullOrEmpty(mimeType)) {
							msg = "Mimetype is empty filename="+ dte.getFileName();
							dte.setStatus(ECMUploadStatus.ERROR_UPLOAD.value());
							dte.setErrorMessage(msg);
							uwe.documentToECMRepo().updateDocumentToEcm(dte);
							continue;
						}
					} else {
						msg = "File local doesn't exist filename="+ dte.getFileName() +" - localfile="+tempFile.getPath();
						dte.setStatus(ECMUploadStatus.ERROR_UPLOAD.value());
						dte.setErrorMessage(msg);
						uwe.documentToECMRepo().updateDocumentToEcm(dte);
						continue;
					}
					dte.setMimeType(mimeType);
					// update successful
					dte.setStatus(ECMUploadStatus.GOT_FILE.value());
					dte.setErrorMessage("");
					uwe.documentToECMRepo().updateDocumentToEcm(dte);
				} catch (Throwable forCat) {
					msg = "getDocumentsToUpload got exception. Message="+ forCat.getMessage();
					System.out.println(" ERROR getDocumentsToUpload. ID = " + dte.getId() + ". Message=" + forCat.getMessage());
					dte.setStatus(ECMUploadStatus.ERROR_UPLOAD.value());
					dte.setErrorMessage(msg);
					uwe.documentToECMRepo().updateDocumentToEcm(dte);
				}
			}
			
			return upList;
		} catch (Throwable th) {
			System.out.println(" ERROR getDocumentsToUpload got unknown exception. Message=" + th.getMessage());
			return upList;
		} finally {
			try {
				uwe.commit();
				uwe.clearCache();
				uwe.close();
			} catch (Throwable cth) {
				System.out.println(" ERROR getDocumentsToUpload when close connection. Message=" + cth.getMessage());
			}
		}
	}

	public void updateDocumentToProcess() {
		UnitOfWorkECM uwe = DocumentUpload.getInstance(SessionType.JTA);
		try {
			UnitOfWorkECM.tryCatch(uwe, ()->{
				return uwe.documentToECMRepo().updateDocumentToProcess();
			});
		} catch (Throwable th) {
			System.out.println(" ERROR updateDocumentToProcess. Message=" + th.getMessage());
		}
	}
}
