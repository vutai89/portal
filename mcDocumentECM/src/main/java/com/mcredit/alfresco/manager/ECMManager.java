package com.mcredit.alfresco.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.ValidationException;

import com.mcredit.alfresco.aggregate.DocumentAggregate;
import com.mcredit.alfresco.factory.DocumentFactory;
import com.mcredit.common.Messages;
import com.mcredit.model.dto.DowloadZipDTO;
import com.mcredit.model.dto.ProductDTO;
import com.mcredit.model.dto.ResponseSuccess;
import com.mcredit.model.enums.Commons;
import com.mcredit.model.enums.ECMEnum;
import com.mcredit.model.enums.ECMSourceEnum;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.object.ecm.InputUploadECM;
import com.mcredit.model.object.ecm.LoanDocRespone;
import com.mcredit.model.object.ecm.OutputUploadECM;
import com.mcredit.model.object.mobile.dto.DocumentTypeDTO;
import com.mcredit.sharedbiz.cache.DocumentsCachManager;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;
import com.mcredit.sharedbiz.cache.ProductCacheManager;
import com.mcredit.sharedbiz.manager.BaseManager;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

public class ECMManager extends BaseManager {
	
	//private String pathParent = "E:\\TMP\\capture\\" ;
	//private int numberParallel = 10;
	
	private String pathParent = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.TMP_FOLDER_CAPTURE_DOCUMENT_ECM)  ;
	private int numberParallel = ParametersCacheManager.getInstance() .findParamValueAsInteger(ParametersName.NUMBER_PARALLEL_CAPTURE_ECM);
	
	private String token = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_TOKENT);
	private String mediaType = "ALL" ;
	
	private DocumentsCachManager documentCache =  DocumentsCachManager.getInstance();
	private ProductCacheManager product =  ProductCacheManager.getInstance();
	
	private DocumentAggregate  docAgg = null;
	
	private final String VALIDATION_OK = "T";
	private final String VALIDATION_FAILED = "F";

	public ECMManager() {
		this.docAgg = new DocumentAggregate(uok);
	}

	public DowloadZipDTO dowloadFile(String contractNumber , String type) throws Exception {
		return  this.tryCatch(()->{
			if( !StringUtils.isNullOrEmpty(type)){
				mediaType = type ;
			}
			
			DocumentFactory.validation(this.uok).validateContractNumForDebtColection(contractNumber);
			
			Long appNumber = this.uok.credit.creditApplicationRequestRepo().checkContractNumberDebt(contractNumber);
			if ( null == appNumber) {
				throw new ValidationException(Messages.getString("validation.notExistContractNum"));
			}
			
			DocumentAggregate docAgg = new DocumentAggregate(contractNumber, token , pathParent , mediaType , numberParallel) ;
			List<LoanDocRespone> listDoc = docAgg.queryDoc(appNumber.intValue());
			
			if(null == listDoc || listDoc.size() == 0){
				throw new ValidationException(Messages.getString("validation.notExistDocumentContractNum"));
			}
					
			List<LoanDocRespone> listRemote = new ArrayList<>();
			for (LoanDocRespone docTMP : listDoc) {
				if(!"ALL".equals(mediaType.toUpperCase()) && ! mediaType.equals(docTMP.getMimeType().split("/")[0])){
					listRemote.add(docTMP);					
				}
			}			
			listDoc.removeAll(listRemote);
			
			if(null == listDoc || listDoc.size() == 0){
				throw new ValidationException(Messages.getString("validation.notExistDocumentTypeContractNum"));
			}
			
			 new File(pathParent  + Commons.SLASH.value() +contractNumber).mkdir();
			 
			  docAgg.captureParallel(listDoc) ;
			  
				 if(docAgg.zipFile()){
					 return new DowloadZipDTO(pathParent  + Commons.SLASH.value() +contractNumber  + Commons.SLASH.value() + contractNumber +".zip", contractNumber +".zip"); 
				 };
			 
			return null;
		
		});
	}
	
	public Object uploadMetaDataECM(List<InputUploadECM> inputUploadECMLst) throws Exception {
		System.out.println("uploadMetaDataECM input data: "+ JSONConverter.toJSON(inputUploadECMLst));
		return  this.tryCatch(()->{
			DocumentFactory.validation(this.uok).validateUploadMetaDataECM(inputUploadECMLst);
			ECMSourceEnum ecmEnum = ECMSourceEnum.DATA_CENTRALIZE_FROM_BPM;
			
			ArrayList<OutputUploadECM> success = new ArrayList<>() ;
			ArrayList<OutputUploadECM> error = new ArrayList<>() ;
			//List<InputUploadECM> listRemove =  new ArrayList<>();
			//List<String> appIdLst = new ArrayList<>();
			List<String> docCode= new ArrayList<>();
			List<String> listParam = new ArrayList<String>();
			int countInvalid = 0;
			
			for (InputUploadECM inputUploadECM : inputUploadECMLst) {
				String valdate = DocumentFactory.validation(this.uok).validateUploadMetaDataECMElement(inputUploadECM) ;
				if(null != valdate){
					error.add(new OutputUploadECM(inputUploadECM.getUserFileName(), valdate));
					//listRemove.add(inputUploadECM);
					inputUploadECM.setValidateStatus(this.VALIDATION_FAILED);
					countInvalid++;
				}else {
					DocumentTypeDTO doc = documentCache.getInstance().getDocumentByCode(inputUploadECM.getDocumentCode());
					ProductDTO prod = ProductCacheManager.getInstance().findProductByCode(inputUploadECM.getProductCode());
					inputUploadECM.setValidateStatus(this.VALIDATION_OK);
					inputUploadECM.setDocumentId(((doc == null) ? null : doc.getId().intValue()));
					inputUploadECM.setProductId(((prod == null) ? null : prod.getId().intValue()));
					ecmEnum = ECMSourceEnum.from(inputUploadECM.getDocumentSource());
					if(ECMSourceEnum.FAS == ecmEnum) {
						listParam.add(inputUploadECM.getAppId());
					} else if(ECMSourceEnum.COLLECTION == ecmEnum) {
						listParam.add(inputUploadECM.getLdNumber());
					} else if(ECMSourceEnum.DATA_CENTRALIZE_FROM_BPM == ecmEnum) {
						int index = inputUploadECM.getServerFileName().indexOf("/", 31);
						inputUploadECM.setFolder(inputUploadECM.getServerFileName().substring(index + 1, index + 11));
					}
					//appIdLst.add(inputUploadECM.getAppId());
					//docCode.add(inputUploadECM.getDocumentCode());
				}
			}
			
			//inputUploadECMLst.removeAll(listRemove);
			
			if(countInvalid > 0 ) {
				throw new ValidationException(Messages.getString("validation.UploadMetaDataECM"));
			}
			
			List<InputUploadECM> InPutlst = docAgg.getListInforUpdate(inputUploadECMLst, ecmEnum, listParam);

			//HashMap<String, Long>  hashDocumentId = documentCache.hashDocumentIdByLstCode(docCode);
			//List<InputUploadECM> InPutlst = docAgg.getListInforUpdate(inputUploadECMLst);
			
			if(InPutlst.size() == 0){
				throw new ValidationException(Messages.getString("validation.UploadMetaDataECM"));
			}
			
			for (InputUploadECM inputUploadECM : InPutlst) {
				
				ECMEnum ecmRes = ECMEnum.from(docAgg.uploadMetaDataECM(inputUploadECM));
				
				if(ECMEnum.ERROR_MSG == ecmRes){
					error.add(new OutputUploadECM(inputUploadECM.getUserFileName(), ecmEnum.value()));
				}else{
					success.add(new OutputUploadECM(inputUploadECM.getUserFileName(), ecmEnum.value()));			
				}
			}

			if(error.size() > 0){
				System.out.println("file = "+ error.get(0).getFileName() + "; error="+error.get(0).getMessage());
				throw new ValidationException(Messages.getString("validation.UploadMetaDataECM"));
			}
			else {
				System.out.println("uploadMetaDataECMNew success");
				return new ResponseSuccess();
			}
		});
	}
	
	public static void main(String[] args) throws IOException {
		ECMManager ecm = new ECMManager();
		
		try {
			//System.out.println( " OUT " + JSONConverter.toJSON(ecm.dowloadFile("1000319040000231","ALL")));
			 System.out.println(ecm.uploadMetaDataECM(ecm.genetricData()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ecm.close();
	}
	
	public List<InputUploadECM> genetricData(){
		
		List<InputUploadECM> input = new ArrayList<>() ;
		ArrayList<String> listAppId = new ArrayList<String>(Arrays.asList(
				"2993199115bb46d3d50efa9069341068","2286843645bb46e2a6edd10023514835","6196521375bb46f453e8169016220172","8702195065bb46fd628e231043689932"
				,"5748976205bb4714a63ab63069277338","8356833715bb471f656eba0096962398","7581185855bb4723fedd1a6082458171","2782605345bb4724ec34303060148757",
				"3861428375bb4734a38e1a2006892604","2357111795bb473e8e0b027002961159","4017658465bb475192ac136007907702","6268714415b8622549f9908020420209"
				));
		
		for ( int i = 0 ; i < listAppId.size() ; i ++){
			InputUploadECM in = new InputUploadECM("IdentityCard", "I", 1000 + i, listAppId.get(i),"C0000008", "citizenID_" + i,"ldNumber" + i , "custName_" + i, "\\upload\\document\\" + i + ".jpg", ""+ i + ".jpg");
			input.add(in);
		}
		System.out.println( " -- > "  + JSONConverter.toJSON(input));
		return input;
	}
}
