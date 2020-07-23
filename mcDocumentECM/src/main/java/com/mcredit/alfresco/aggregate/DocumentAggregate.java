package com.mcredit.alfresco.aggregate;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

import com.mcredit.alfresco.api.AlfrescoECMRestAPI;
import com.mcredit.alfresco.threads.ThreadManager;
import com.mcredit.alfresco.utils.Constants;
import com.mcredit.alfresco.utils.FTPFileUtils;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.ecm.entity.DocumentToECM;
import com.mcredit.model.enums.Commons;
import com.mcredit.model.enums.DataSizeEnum;
import com.mcredit.model.enums.ECMEnum;
import com.mcredit.model.enums.ECMSourceEnum;
import com.mcredit.model.object.ecm.DocumentToEcmDTO;
import com.mcredit.model.object.ecm.InputUploadECM;
import com.mcredit.model.object.ecm.LoanDocRequest;
import com.mcredit.model.object.ecm.LoanDocRespone;
import com.mcredit.util.PartitionListHelper;

public class DocumentAggregate {
	private UnitOfWork unitOfWork = null;
	static Logger logger = Logger.getLogger(DocumentAggregate.class.getName());
	protected String contracNumber =  null ;
	protected String basicToken =  null ;
	protected String pathParent =  null ;
	protected String mediaType =  "image/png" ;
	protected int numberParallel =  10 ;
	byte[] buffer = new byte[DataSizeEnum.BYTE_SIZE.intValue()];

	public DocumentAggregate(UnitOfWork uok) {
		this.unitOfWork = uok;
	}
	
	public DocumentAggregate() {
		super();
	}

	public DocumentAggregate(String contracNumber, String basicToken, String pathParent, String mediaType, int numberParallel) {
		this.contracNumber = contracNumber ;
		this.basicToken = basicToken ;
		this.pathParent = pathParent ;
		this.mediaType = mediaType ;
		this.numberParallel = numberParallel ;
	}

	public List<LoanDocRespone> queryDoc(String appId) {
		try {
			logger.info("************starting queryDoc on ECM *******");
			logger.info("appId = " + appId);
			AlfrescoECMRestAPI api = new AlfrescoECMRestAPI();
			LoanDocRequest docRequest = new LoanDocRequest();
			docRequest.setRefNumber(appId);
			List<LoanDocRespone> lstDocs = api.doQuery(docRequest);
			logger.info("************finish queryDoc on ECM *******");
			return lstDocs;
		} catch (Exception e) {
			logger.error("***** queryDoc, message= " + e.getMessage());
			e.printStackTrace();
			return null;
		}

	}

	public List<LoanDocRespone> queryDocByObjectId(String objectId) {
		try {
			logger.info("************starting queryDoc on ECM *******");
			logger.info("objectId = " + objectId);
			AlfrescoECMRestAPI api = new AlfrescoECMRestAPI();
			List<LoanDocRespone> lstDocs = api.doQueryByObjectId(objectId);
			logger.info("************finish queryDoc on ECM *******");
			return lstDocs;
		} catch (Exception e) {
			logger.error("***** queryDoc, message= " + e.getMessage());
			e.printStackTrace();
			return null;
		}

	}

	public List<LoanDocRespone> queryDoc(int appNumber) {
		try {
			logger.info("************starting queryDoc on ECM *******");
			logger.info("appNumber = " + appNumber);
			AlfrescoECMRestAPI api = new AlfrescoECMRestAPI();
			LoanDocRequest docRequest = new LoanDocRequest(String.valueOf(appNumber));
			List<LoanDocRespone> lstDocs = api.doQuery(docRequest);
			logger.info("************finish queryDoc on ECM *******");
			return lstDocs;
		} catch (Exception e) {
			logger.error("***** queryDoc, message= " + e.getMessage());
			e.printStackTrace();
			return null;
		}

	}

	public boolean deleteDoc(String appId) {
		try {
			logger.info("************starting deleteDoc on ECM *******");
			logger.info("appId = " + appId);
			List<LoanDocRespone> lstRes = queryDoc(appId);
			AlfrescoECMRestAPI api = new AlfrescoECMRestAPI();
			for(LoanDocRespone resDoc : lstRes) {
				api.deleteDoc(resDoc);
			}
			logger.info("************finish deleteDoc on ECM *******");
			return true;
		} catch (Exception e) {
			logger.error("***** deleteDoc, message= " + e.getMessage());
			e.printStackTrace();
			return false;
		}

	}
	
	public void uploadDocToECM() {
		DocumentUpload docUp = new DocumentUpload();
		docUp.updateDocumentToProcess();
		List<DocumentToEcmDTO> list = docUp.getDocumentsToUpload();
		FTPFileUtils.closeAllSession();
		ThreadManager tm = new ThreadManager();
		tm.initialize(list);
		tm.execute();
		System.out.println(" uploadDocToECM finished ");
	}

/*	public List<LoanDocRespone> queryDocList(String docName) {
		try {
			logger.info("************starting queryDoc on ECM *******");
			logger.info("appId = " + docName);
			AlfrescoECMRestAPI api = new AlfrescoECMRestAPI();
			List<LoanDocRespone> lstDocs = api.doQueryList(docName);
			logger.info("************finish queryDoc on ECM *******");
			return lstDocs;
		} catch (Exception e) {
			logger.error("***** queryDoc, message= " + e.getMessage());
			e.printStackTrace();
			return null;
		}

	}*/
	
	public boolean captureParallel(List<LoanDocRespone> lstDoc ) throws IOException, InterruptedException, ExecutionException {
		boolean allDone = true;
		 ArrayList<List<LoanDocRespone>> tmp = new ArrayList<List<LoanDocRespone>>(PartitionListHelper.partition(lstDoc, numberParallel));
		for (int i = 0; i < tmp.size(); i++) {
				ExecutorService executor = Executors.newSingleThreadExecutor();
				for (LoanDocRespone doc : tmp.get(i)) {
				Callable<Boolean> callable = () -> {
					return captureFile(doc);
				};
				allDone &= executor.submit(callable).get() ;
			}
			executor.shutdown();
		}
		
		return allDone ;
	}
	
	public boolean captureFile(LoanDocRespone doc ) throws IOException {
		boolean result = true ;
		OutputStream outStream = null;
		URLConnection uCon = null;
		InputStream is = null;
		
		URL Url;
	
		int ByteRead;
		Url = new URL(doc.getObjectUrl());
					
		uCon = Url.openConnection();					
		uCon.setRequestProperty("Authorization","Basic " +  basicToken);					
		is = uCon.getInputStream();
		outStream = new BufferedOutputStream(new FileOutputStream(pathParent + contracNumber + File.separator +  doc.getObjectName()));
		try {	
			while ((ByteRead = is.read(buffer)) != -1) {
				System.out.print((char) ByteRead);
				outStream.write(buffer, 0, ByteRead);
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			try {
				outStream.close();
				is.close();	
				
			} catch (IOException e) {
				e.printStackTrace();
				result = false;
			}
		}
		return result;
	}


	public boolean zipFile() {
		boolean result = true ;
		String fileFath = pathParent + contracNumber ;
        String source = new File(fileFath).getName();
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        
        File folder = new File(fileFath);		        
        String[] files = folder.list();
        
        try {
            fos = new FileOutputStream(fileFath +Commons.SLASH.value() + contracNumber +".zip");
            zos = new ZipOutputStream(fos);
            
            FileInputStream in = null;

            for (String file: files) {
                System.out.println("File Added : " + file);
                ZipEntry ze = new ZipEntry(source + Commons.SLASH.value() + file);
                zos.putNextEntry(ze);
                try {
                    in = new FileInputStream(fileFath +Commons.SLASH.value() + file);
                    int len;
                    while ((len = in .read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                } finally {
                    in.close();
                }
            }
            
            zos.closeEntry();
            System.out.println("Folder successfully compressed");
            zos.close();

        } catch (IOException ex) {
            ex.printStackTrace();
            result = false ;	
        }
		return result; 
		
	}
	
	public String uploadMetaDataECM(InputUploadECM inputUploadECM) {
		try {
			
			DocumentToECM newDoc = new DocumentToECM(
					inputUploadECM.getDocumentId(), 
					inputUploadECM.getDocumentSource(), null, 
					inputUploadECM.getAppId(), 
					inputUploadECM.getAppNumber(), 
					inputUploadECM.getLdNumber(), 
					inputUploadECM.getProductId(), 
					inputUploadECM.getCitizenID(), 
					inputUploadECM.getCustName(), 
					inputUploadECM.getUserFileName(), 
					inputUploadECM.getServerFileName(), null,
					inputUploadECM.getFolder());

			this.unitOfWork.ecm.documentToECMRepo().insert(newDoc);
			return ECMEnum.INSERT.value();
		}
		catch(Exception e){
			e.printStackTrace();
			return ECMEnum.ERROR_MSG.value();
		}
	}
	
	public List<InputUploadECM> getListInforUpdate(List<InputUploadECM> inputUploadECMLst,
			ECMSourceEnum srcEnum, List<String> listID) {
		List<InputUploadECM> list = null;
		
		if(listID != null  && listID.size() > 0){
			if(ECMSourceEnum.FAS == srcEnum) {
				list = this.unitOfWork.ecm.documentToECMRepo().getListInforECMByAppId(listID);
			} else if(ECMSourceEnum.COLLECTION == srcEnum) {
				list = this.unitOfWork.ecm.documentToECMRepo().getListInforECMByContractLD(listID);
			}
		}
		
		if(list != null) {
			for (InputUploadECM input : inputUploadECMLst) {
				for (InputUploadECM tmp : list) {
					if(((ECMSourceEnum.FAS == srcEnum) && (input.getAppId().equals(tmp.getAppId())))
						|| ((ECMSourceEnum.COLLECTION == srcEnum) && (input.getLdNumber().equals(tmp.getContractNumber())))) {
						input.setCitizenID(tmp.getCitizenID());
						input.setCustName(tmp.getCustName());
						input.setFolder(tmp.getFolder());
						input.setAppNumber(tmp.getAppNumber());
						input.setLdNumber(tmp.getContractNumber());
						input.setAppId(tmp.getAppId());
						input.setProductId(tmp.getProductId());
					}
				}
			}
		}

		return inputUploadECMLst ;
	}

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
	
	/*		
			Scanner input = new Scanner(new File("appid.txt"));
			input.useDelimiter("-|\n");
	
			List<Integer> appids = new ArrayList<Integer>();
	
			while (input.hasNext()) {
				Integer appid = input.nextInt();
				appids.add(appid);
			}
			
			for (Integer integer : appids) {
				DocumentAggregate doc = new DocumentAggregate();		
				List<LoanDocRespone> docList = doc.queryDoc(integer);
				PrintWriter writer = new PrintWriter( integer +".txt", "UTF-8");
				writer.println( JSONConverter.toJSON(docList));
				writer.close();
			}
	 	*/
			String serverFile = Constants.prefixFileServerBPM;
			DocumentAggregate doc = new DocumentAggregate();
			//List<LoanDocRespone> list = doc.queryDocByObjectId("05689b97-e50a-453e-98a8-c45cb597b481");
			//System.out.println("objectId="+list.get(0).getObjectId());
			doc.uploadDocToECM();
	/*		String delDocs = "8097355515d1962d6cd15d4059493716;9054242655d15bd735825c8017206175;5028317365d15a060477a17017119447;7788263135d159414365ad8076502657;3813668975d197d8fa1fee5008351571;1636281005d18192c686436055679357;4635402225d156a22c8ebb1079839544";
			String[] lstDel = delDocs.split(";");
			for(String appid : lstDel) {
				if(!StringUtils.isNullOrEmpty(appid)) {
					doc.deleteDoc(appid);
				}
			}
	*/
			System.exit(0);
	}

}
