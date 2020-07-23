package com.mcredit.business.warehouse.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mcredit.business.warehouse.convertor.Convertor;
import com.mcredit.data.warehouse.entity.WhBorrowedDocument;
import com.mcredit.data.warehouse.entity.WhDocumentChange;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.enums.CTGroup;
import com.mcredit.model.enums.ProductName;
import com.mcredit.model.enums.StatusBPM;
import com.mcredit.model.object.warehouse.WareHouseEnum;
import com.mcredit.model.object.warehouse.WareHouseSeachObject;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.manager.BaseManager;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class ManagerHelper extends BaseManager {
    
   private static int CAVET = 0;
		
	public static List<WareHouseSeachObject> followCase(List<WareHouseSeachObject> input) throws CloneNotSupportedException {
		CodeTableCacheManager ctCache = CacheManager.CodeTable();
		String whStatusName = "Gi\u1EA5y t\u1EDD c\u1EA7n v\u1EC1.";
		CodeTableDTO loanDoc = ctCache.getBy(CTCodeValue1.WH_LOAN_DOC, CTCat.WH_DOC_TYPE, CTGroup.WARE_HOUSE);
		CodeTableDTO cavetDoc = ctCache.getBy(CTCodeValue1.WH_CAVET, CTCat.WH_DOC_TYPE, CTGroup.WARE_HOUSE);
		CodeTableDTO upadeErro = ctCache.getBy(CTCodeValue1.WH_ERR_UPDATE, CTCat.WH_DOC_TYPE, CTGroup.WARE_HOUSE);
		List<CodeTableDTO> listMotorBike = ctCache.getIdByParent( new String[]{CTCodeValue1.MOTORBIKE.value(),CTCodeValue1.EMOTORBIKE.value()}, CTCat.COMM.value());
		
		List<WareHouseSeachObject> result = new ArrayList<>();
		HashMap<String, List<WareHouseSeachObject>> listInContact = new HashMap<>();

		if (input != null) {
			for (WareHouseSeachObject wareHouseSeachObject : input) {
				List<WareHouseSeachObject> listContract = listInContact.get(wareHouseSeachObject.getContractNum());
				if (listContract == null) {
					listContract = new ArrayList<>();
				}
				listContract.add(wareHouseSeachObject);
				listInContact.put(wareHouseSeachObject.getContractNum(), listContract);
			}
		}
		if (listInContact.size() > 0) {
			for (Map.Entry<String, List<WareHouseSeachObject>> entry : listInContact.entrySet()) {
				HashMap<String, WareHouseSeachObject> tmpSeach = new HashMap<>();
				int tmpCountCave = 0 , tmpCountErrorLoan = 0 , tmpCountLoan = 0  , tmpCountErrorCave = 0 ;
				
				if (entry.getValue().size() > 0) {
					List<WareHouseSeachObject> lstWhSeach = entry.getValue();
					if (lstWhSeach.size() == 1) {						
						WareHouseSeachObject tmp = lstWhSeach.get(0);
						StatusBPM statusBPMCode = StatusBPM.from(tmp.getStatusBPMCode());
						
						if (lstWhSeach.get(0).getWhId() == null) {
							if(!StringUtils.isNullOrEmpty(lstWhSeach.get(0).getProductName())
									&& ProductName.SUB_TW.value().equals(lstWhSeach.get(0).getProductName().substring(0, 2))
									&& lstWhSeach.get(0).getGoodsId() != null
									&& listMotorBike.parallelStream().filter(o -> o.getId().equals(tmp.getGoodsId().intValue())).findFirst().isPresent()){
							
								WareHouseSeachObject loan = Convertor.convertFrom(loanDoc, whStatusName, tmp, true);
								if (StatusBPM.DONE == statusBPMCode) {								
									loan.setExpectedReceiptDate(caculateExp(tmp.getApprovedDateBPM(), loanDoc.getId()));
								}
								result.add(loan);
								
								WareHouseSeachObject cavet = Convertor.convertFrom(cavetDoc, whStatusName, tmp, true);
								
								if (StatusBPM.DONE == statusBPMCode) {
									cavet.setExpectedReceiptDate(caculateExp(tmp.getApprovedDateBPM(),cavetDoc.getId()));
								}
								result.add(cavet);
								
							} else {
								WareHouseSeachObject loan = Convertor.convertFrom(loanDoc, whStatusName, tmp, true);
								if (StatusBPM.DONE == statusBPMCode) {
									loan.setExpectedReceiptDate(caculateExp(tmp.getApprovedDateBPM(), tmp.getDocTypeId()!= null ? tmp.getDocTypeId().intValue() : 0));
								}
								result.add(loan);
							}
						} else {
							if(!StringUtils.isNullOrEmpty(lstWhSeach.get(0).getProductName())
									&& ProductName.SUB_TW.value().equals(lstWhSeach.get(0).getProductName().substring(0, 2))
									&& lstWhSeach.get(0).getGoodsId() != null
									&& listMotorBike.parallelStream().filter(o -> o.getId().equals(tmp.getGoodsId().intValue())).findFirst().isPresent()){
								if (tmp.getDocTypeCode().equals(CTCodeValue1.WH_CAVET.value())){
									WareHouseSeachObject loan = Convertor.convertFrom(loanDoc, whStatusName, tmp, false);
									
									if (StatusBPM.DONE == statusBPMCode) {
										loan.setExpectedReceiptDate(caculateExp(tmp.getApprovedDateBPM(), loanDoc.getId()));
									}
									result.add(loan);
								}
								
								if (CTCodeValue1.WH_LOAN_DOC.value().equals(tmp.getDocTypeCode())){
									WareHouseSeachObject cavet = Convertor.convertFrom(cavetDoc, whStatusName, tmp, false);
									
									if (StatusBPM.DONE == statusBPMCode) {						
										cavet.setExpectedReceiptDate(caculateExp(tmp.getApprovedDateBPM(),cavetDoc.getId()));
									}
									result.add(cavet);
								}
							}else {
								if (tmp.getDocTypeId() != null){
									/* Phase 2 check doccument need recieve
									if (WareHouseEnum.CONTRACTCAVETTYPE_CONTRACT.longValue().equals(tmp.getContractCavetType())
											|| WareHouseEnum.CONTRACTCAVETTYPE_CAVET.longValue().equals(tmp.getContractCavetType())
											|| ( tmp.getStatusWHCode().equals(CTCodeValue1.WH_LODGED_ERR_UPDATE.value()) || tmp.getStatusWHCode().equals(CTCodeValue1.WH_LODGED_ERR_UPDATE_BORROW.value()))) {
									result.add(Convertor.convertFrom(upadeErro, whStatusName, tmp));										
									}*/					
									
								}	
							}
							result.add(tmp);
						}
					} else {
						
						for (WareHouseSeachObject wareHouseSeachObject : entry.getValue()) {
							tmpSeach.put(wareHouseSeachObject.getContractNum() + wareHouseSeachObject.getDocTypeCode(), wareHouseSeachObject);
						}
						for (WareHouseSeachObject whSearchObj : entry.getValue()) {
							if (!StatusBPM.DONE.value().equals(whSearchObj.getStatusBPMCode())) {
								whSearchObj.setExpectedReceiptDate(null);
							}
							
							result.add(whSearchObj);
							
							StatusBPM statusBPMCode = StatusBPM.from(whSearchObj.getStatusBPMCode());
							CTCodeValue1 statusWHCode = CTCodeValue1.from(whSearchObj.getStatusWHCode());
							
							if (CTCodeValue1.WH_CAVET.value().equals(whSearchObj.getDocTypeCode())
									
								&& (!StringUtils.isNullOrEmpty(whSearchObj.getProductName())
										&& ProductName.SUB_TW.value().equals(whSearchObj.getProductName().substring(0, 2))
										&& lstWhSeach.get(0).getGoodsId() != null
										&& listMotorBike.parallelStream().filter(o -> o.getId().equals(whSearchObj.getGoodsId().intValue())).findFirst().isPresent())){
								 if(tmpSeach.get(whSearchObj.getContractNum() + CTCodeValue1.WH_LOAN_DOC.value()) == null && tmpCountLoan == 0 ){
									WareHouseSeachObject loan = Convertor.convertFrom(loanDoc, whStatusName, whSearchObj, false);
									
									if (StatusBPM.DONE == statusBPMCode) {
										loan.setExpectedReceiptDate(caculateExp(whSearchObj.getApprovedDateBPM(), loanDoc.getId()));
									}
									result.add(loan);
									tmpCountLoan++;
								 } else if(tmpCountErrorLoan == 0){
									 if(!StringUtils.isNullOrEmpty(whSearchObj.getProductName())
												&& ProductName.SUB_TW.value().equals(whSearchObj.getProductName().substring(0, 2))
												&& lstWhSeach.get(0).getGoodsId() != null
												&& listMotorBike.parallelStream().filter(o -> o.getId().equals(whSearchObj.getGoodsId().intValue())).findFirst().isPresent()){
										 if( CTCodeValue1.WH_WAIT_ERR_UPDATE == statusWHCode
												|| CTCodeValue1.WH_LODGED_ERR_UPDATE == statusWHCode
												|| CTCodeValue1.WH_LODGED_ERR_UPDATE_BORROW == statusWHCode){
											 
											WareHouseSeachObject tmpError = Convertor.convertFrom(upadeErro, whStatusName, whSearchObj, false);
											result.add(tmpError);
											tmpCountErrorLoan ++ ;
										}
									 }
								 }
							}
							
							if (CTCodeValue1.WH_LOAN_DOC.value().equals(whSearchObj.getDocTypeCode())){
								 if(!StringUtils.isNullOrEmpty(whSearchObj.getProductName())
											&& ProductName.SUB_TW.value().equals(whSearchObj.getProductName().substring(0, 2))
											&& lstWhSeach.get(0).getGoodsId() != null
											&& listMotorBike.parallelStream().filter(o -> o.getId().equals(whSearchObj.getGoodsId().intValue())).findFirst().isPresent()){
								if(tmpSeach.get(whSearchObj.getContractNum() + CTCodeValue1.WH_CAVET.value()) == null && tmpCountCave == 0){
									
									WareHouseSeachObject cavet = Convertor.convertFrom(cavetDoc, whStatusName, whSearchObj,true);
									
									if (StatusBPM.DONE == statusBPMCode) {
										cavet.setExpectedReceiptDate(caculateExp(whSearchObj.getApprovedDateBPM(),cavetDoc.getId()));
								   }
								   result.add(cavet);
								   tmpCountCave ++ ;
								}else if(tmpCountErrorCave == 0){									
									if( CTCodeValue1.WH_WAIT_ERR_UPDATE == statusWHCode 
											|| CTCodeValue1.WH_LODGED_ERR_UPDATE == statusWHCode
											|| CTCodeValue1.WH_LODGED_ERR_UPDATE_BORROW == statusWHCode){
										
										WareHouseSeachObject tmpError = Convertor.convertFrom(upadeErro, whStatusName, whSearchObj, false);
										result.add(tmpError);
										tmpCountErrorCave++;
										}
									}
								}
							}
						}
					}

				}
			}
		}
		return result.parallelStream().distinct().collect(Collectors.toList());
	}
	
	public static WhDocumentChange createWHDocumentChange(long idWHDoc, long id_code_table, Long type, UserDTO user) {
		return new WhDocumentChange(idWHDoc, type, "", user.getLoginId(), new Date(), id_code_table);
	}

	public static long getRenewalAppointStatus(CodeTableDTO getCodeValue, List<CodeTableDTO> arrStatusAppoint) {
		CodeTableDTO ct = new CodeTableDTO();

		if (CTCodeValue1.from(getCodeValue.getCodeValue1()) == CTCodeValue1.WH_LODGED_COMPLETE) {
			ct = arrStatusAppoint.parallelStream()
					.filter(cus -> CTCodeValue1.WH_LODGE_COMPLETE_BORROW.value().equalsIgnoreCase(cus.getCodeValue1()))
					.findAny() // If 'findAny' then return found
					.orElse(null);
		} else {
			ct = arrStatusAppoint.parallelStream()
					.filter(cus -> CTCodeValue1.WH_LODGED_ERR_UPDATE_BORROW.value()
							.equalsIgnoreCase(cus.getCodeValue1()))
					.findAny() // If 'findAny' then return found
					.orElse(null);
		}

		return ct.getId() != null ? ct.getId().longValue() : new Long(0);
	}
	
	public static WhBorrowedDocument addAttributeBorrowedDoc(WhBorrowedDocument whBD, long approveStatus, long idType,int documentType) {
		whBD.setApproveStatus(approveStatus);
		whBD.setType(idType);
		whBD.setCreatedDate(new Date());

		if(documentType == CAVET){
			whBD.setApproveDate(new Date());
		} else {
                        whBD.setBorrowedDate(new Date());                    
                }
		return whBD;
	}
	
	private static Date caculateExp(Date approcedDate, Integer doctypeId) {
		Calendar c = Calendar.getInstance();
		
		if(doctypeId == null ||approcedDate == null  ){
			return null;
		}
		if (approcedDate != null) {
			c.setTime(approcedDate);
			
			try {
				c.add(Calendar.DATE,
						(ParametersCacheManager.getInstance()
								.findParamValueAsInteger(WareHouseEnum.SUB_DAYS_.stringValue()
										+ CodeTableCacheManager.getInstance().getbyID(doctypeId.intValue()).getCodeValue1())));
				return (c.getTime());
			} catch (ValidationException e) {
				e.printStackTrace();
			}

		}
		return null;
	}

}
