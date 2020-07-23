package com.mcredit.business.warehouse.aggregate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mcredit.data.warehouse.UnitOfWorkWareHouse;
import com.mcredit.data.warehouse.entity.WhCode;
import com.mcredit.data.warehouse.entity.WhDocument;
import com.mcredit.data.warehouse.entity.WhMapDocCode;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.object.Param;
import com.mcredit.model.object.warehouse.LodgeDocumentDTO;
import com.mcredit.model.object.warehouse.LodgeResponseDTO;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;

public class DocumentLodgeAbstractAggregate {

	protected UnitOfWorkWareHouse unitOfWorkWareHouse = null;
	protected CodeTableCacheManager ctCache = CacheManager.CodeTable();
	protected LodgeDocumentDTO lodgeDocument = null;
	protected String userLogin,ctmMaterial,ctChangeType,code,flag = null;
	protected boolean containCode = false;
	protected LodgeResponseDTO result = new LodgeResponseDTO();
	protected Long material,changeType,docType = null;
	protected String lodgeCode = null;
	
	public DocumentLodgeAbstractAggregate(UnitOfWorkWareHouse uok,LodgeDocumentDTO lodgeDocument, String userLogin) {
		this.unitOfWorkWareHouse = uok;
		this.lodgeDocument = lodgeDocument;
		this.userLogin = userLogin;
		
		/*CodeTableDTO ctmMaterial = ctCache.getBy(CTCodeValue1.WH_METAL,CTCat.WH_MATERIAL);
		material = ctmMaterial != null ? ctmMaterial.getId().longValue(): 0;*/

		CodeTableDTO ctChangeType = ctCache.getBy(CTCodeValue1.WH_STATUS_CHANGE, CTCat.WH_CHAN_TYPE);
		changeType = ctChangeType != null ? ctChangeType.getId().longValue() : 0;
		
		/*CodeTableDTO ctDocType = ctCache.getBy(CTCodeValue1.WH_CAVET, CTCat.WH_DOC_TYPE);
		docType = ctDocType != null ? ctDocType.getId().longValue() : 0;*/
	}

	protected LodgeResponseDTO processResult(){
		if (containCode) {
			result.setStatus("SUCCESS");
			if (lodgeDocument.getLstData().size() == 1)
				result.setLodgeCode(lodgeCode.split("###")[0]);
			else if (lodgeDocument.getLstData().size() > 1)
				result.setStatus("SUCCESS-LIST");
			else
				result.setStatus("UN-SUCCESS");
		} else
			result.setStatus("UN-SUCCESS");

		return result;
	}
	
	protected void createLodgeCode(String value){
		List<Param> params = new ArrayList<Param>();
		Param param = new Param();
		param.setTypeData("string"); 
        param.setValue(value);
        params.add(param);
        lodgeCode = StringUtils.nullToEmpty(unitOfWorkWareHouse.whCodeRepos().callFunctionReturnSingleRow("GET_CODE_SAVE_WH", params, "string"));
	}
	
	protected void processlodgeCode(){
		String[] lodgeCodeArr = lodgeCode.split("###");
		code = lodgeCodeArr[0].trim();
		flag = lodgeCodeArr[1];
	}
	
    protected void update(Long newStatusId, Long whDocId, int contractCavetType) {
        if (!StringUtils.isNullOrEmpty(lodgeCode)) {

            containCode = true;
            processlodgeCode();
            //lay list giay to update loi cua hs goc
            List<BigDecimal> lstAttributeRoot = unitOfWorkWareHouse.whDocumentRepo().getLstAttributeRootDoc(whDocId);

            if ("0".equals(flag)) { // Sinh ma luu kho moi

                WhCode whCode = new WhCode(newStatusId, code, docType, material);
                unitOfWorkWareHouse.whCodeRepos().add(whCode);
                unitOfWorkWareHouse.whMapDocCodeRepo().add(new WhMapDocCode(whDocId, whCode.getId(), userLogin, new Date()));               

                //SonHV update wh_code_id and wh_lodge_date in wh_document table
                WhDocument whDocument = unitOfWorkWareHouse.whDocumentRepo().getById(whDocId);
                whDocument.setWhCodeId(whCode.getId());
                whDocument.setWhLodgeDate(new Date());
                whDocument.setWhLodgeBy(userLogin);
                unitOfWorkWareHouse.whDocumentRepo().upsert(whDocument);
                
                if(lstAttributeRoot != null && !lstAttributeRoot.isEmpty()){
                     for (int i = 0; i < lstAttributeRoot.size(); i++) {
                        if (lstAttributeRoot.get(i).longValue() != whDocId){
                            unitOfWorkWareHouse.whMapDocCodeRepo().add(new WhMapDocCode(lstAttributeRoot.get(i).longValue(), whCode.getId(), userLogin, new Date()));
                            WhDocument wd = unitOfWorkWareHouse.whDocumentRepo().getById(lstAttributeRoot.get(i).longValue());
                            if (contractCavetType == 1){
                                if(wd.getDocType().equals(ctCache.getIdBy(CTCodeValue1.WH_LOAN_DOC, CTCat.WH_DOC_TYPE))
                                   || wd.getDocType().equals(ctCache.getIdBy(CTCodeValue1.WH_ERR_UPDATE, CTCat.WH_DOC_TYPE))){
                                    if (wd.getContractCavetType() ==  null || wd.getContractCavetType() == 1){
                                        wd.setStatus(newStatusId);
                                        wd.setWhCodeId(whCode.getId());
                                        wd.setWhLodgeDate(new Date());
                                        wd.setWhLodgeBy(userLogin);
                                    } else if (wd.getContractCavetType() == 2){
                                        wd.setWhCodeId(whCode.getId());
                                    }
                                    
                                }
                            } else if (contractCavetType == 2){
                               if (wd.getContractCavetType() == null || wd.getContractCavetType() == 2){
                                   if (wd.getDocType().equals(ctCache.getIdBy(CTCodeValue1.WH_CAVET, CTCat.WH_DOC_TYPE))){
                                        wd.setStatus(newStatusId);
                                        wd.setWhCodeId(whCode.getId());
                                        wd.setWhLodgeDate(new Date());
                                        wd.setWhLodgeBy(userLogin);
                                    } else if (wd.getDocType().equals(ctCache.getIdBy(CTCodeValue1.WH_ERR_UPDATE, CTCat.WH_DOC_TYPE))){
                                        wd.setStatus(newStatusId);                                    
                                        wd.setWhLodgeDate(new Date());
                                        wd.setWhLodgeBy(userLogin);
                                        //lay ma cua HD goc
                                        HashMap<String, WhDocument> maplstRoot = this.unitOfWorkWareHouse.whDocumentRepo().getRootUpdateErr(lstAttributeRoot.get(i).longValue());
                                        WhDocument whDocumentLoanDocRoot = maplstRoot.get("rootLoanDoc");
                                        if (whDocumentLoanDocRoot != null) {
                                            wd.setWhCodeId(whDocumentLoanDocRoot.getWhCodeId());                                        
                                        }
                                    }
                               }                                
                            }                            
                            unitOfWorkWareHouse.whDocumentRepo().upsert(wd);
                        }                        
                    }
                }               

            } else if ("1".equals(flag)) { // Update lai ma luu kho neu da ton tai ma
                Long whCodeId = unitOfWorkWareHouse.whCodeRepos().getIdByWHCode(code);
                unitOfWorkWareHouse.whMapDocCodeRepo().add(new WhMapDocCode(whDocId, whCodeId, userLogin, new Date()));
                
                WhDocument whDocument = unitOfWorkWareHouse.whDocumentRepo().getById(whDocId);
                whDocument.setWhCodeId(whCodeId);
                whDocument.setWhLodgeDate(new Date());
                whDocument.setWhLodgeBy(userLogin);
                unitOfWorkWareHouse.whDocumentRepo().upsert(whDocument);
                unitOfWorkWareHouse.whCodeRepos().updateStatusWhCode(newStatusId, code);
                
                if(lstAttributeRoot != null && !lstAttributeRoot.isEmpty()){
                     for (int i = 0; i < lstAttributeRoot.size(); i++) {
                        if (lstAttributeRoot.get(i).longValue() != whDocId){
                            unitOfWorkWareHouse.whMapDocCodeRepo().add(new WhMapDocCode(lstAttributeRoot.get(i).longValue(), whCodeId, userLogin, new Date()));
                            WhDocument wd = unitOfWorkWareHouse.whDocumentRepo().getById(lstAttributeRoot.get(i).longValue());
                           if (contractCavetType == 2){
                               if (wd.getContractCavetType() == null || wd.getContractCavetType() == 2){
                                   if (wd.getDocType().equals(ctCache.getIdBy(CTCodeValue1.WH_CAVET, CTCat.WH_DOC_TYPE))){
                                        wd.setStatus(newStatusId);
                                        wd.setWhCodeId(whCodeId);
                                        wd.setWhLodgeDate(new Date());
                                        wd.setWhLodgeBy(userLogin);
                                    } else if (wd.getDocType().equals(ctCache.getIdBy(CTCodeValue1.WH_ERR_UPDATE, CTCat.WH_DOC_TYPE))){
                                        wd.setStatus(newStatusId);                                    
                                        wd.setWhLodgeDate(new Date());
                                        wd.setWhLodgeBy(userLogin);
                                        //lay ma cua HD goc
                                        HashMap<String, WhDocument> maplstRoot = this.unitOfWorkWareHouse.whDocumentRepo().getRootUpdateErr(lstAttributeRoot.get(i).longValue());
                                        WhDocument whDocumentLoanDocRoot = maplstRoot.get("rootLoanDoc");
                                        if (whDocumentLoanDocRoot != null) {
                                            wd.setWhCodeId(whDocumentLoanDocRoot.getWhCodeId());                                        
                                        }
                                    }
                               }                                
                            }                            
                            unitOfWorkWareHouse.whDocumentRepo().upsert(wd);
                        }                        
                    }
                }
                
            } 
        }
    }
	
	/*protected void update(Long whDocType, Long newStatusId, Long whDocId){
		if (!StringUtils.isNullOrEmpty(lodgeCode)) { 
			
			containCode = true;
			processlodgeCode();

			if ("0".equals(flag)) { // Sinh ma luu kho moi
				
				WhCode whCode = new WhCode(newStatusId, code, whDocType, material);
				unitOfWorkWareHouse.whCodeRepos().add(whCode);
				unitOfWorkWareHouse.whMapDocCodeRepo().add(new WhMapDocCode(whDocId, whCode.getId(), userLogin, new Date()));
				
				//SonHV update wh_code_id and wh_lodge_date in wh_document table
				WhDocument whDocument = unitOfWorkWareHouse.whDocumentRepo().getById(whDocId);
				whDocument.setWhCodeId(whCode.getId());
				whDocument.setWhLodgeDate(new Date());
				whDocument.setWhLoggeBy(userLogin);
				unitOfWorkWareHouse.whDocumentRepo().upsert(whDocument);
				
			} else if ("1".equals(flag)) // Update lai ma luu kho neu da ton tai ma
				unitOfWorkWareHouse.whCodeRepos().updateStatusWhCode(newStatusId, code);
		}
	}*/
	
}
