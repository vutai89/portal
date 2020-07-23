package com.mcredit.sharedbiz.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mcredit.common.UnitOfWorkHelper;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.factory.UnitOfWorkFactory;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.enums.CTGroup;
import com.mcredit.model.enums.SessionType;
import com.mcredit.model.object.warehouse.WareHouseCodeTableCacheDTO;
import com.mcredit.sharedbiz.factory.AllocationFactory;

public class WHCodeTableCacheManager implements IDataCache {
	
	private UnitOfWork uok = null;
	
	private static WHCodeTableCacheManager instance;
	private WareHouseCodeTableCacheDTO wareHouseCodeTableID;
	
	public static synchronized WHCodeTableCacheManager getInstance() {
		if (instance == null) {
			synchronized (WHCodeTableCacheManager.class) {
				if (null == instance) {
					instance = new WHCodeTableCacheManager();
				}
			}
		}
		return instance;
	}
	
	public void initCache() {
		
		 CodeTableCacheManager _ctCache = CacheManager.CodeTable();
		 ProductCacheManager _prdCache = CacheManager.Product();
		 
		 if (null == _ctCache ) {
			  CodeTableCacheManager.getInstance().initCache();
			}
		 
		 if (null == _prdCache ) {
			 ProductCacheManager.getInstance().initCache();
			}
		 
		wareHouseCodeTableID = new WareHouseCodeTableCacheDTO();
					
		wareHouseCodeTableID.setLstIdCodetabeWH_LOGDE(_ctCache.getIdByListCodeValue(CTCat.WH_LOGDE.value(),new ArrayList<String>(Arrays.asList(CTCodeValue1.WH_NEW_WAIT.value(),CTCodeValue1.WH_WAIT_COMPLETE.value(),CTCodeValue1.WH_WAIT_ERR_UPDATE.value(),CTCodeValue1.WH_LODGED_COMPLETE.value(),CTCodeValue1.WH_LODGED_ERR_UPDATE.value(),CTCodeValue1.WH_LODGED_ERR_UPDATE_BORROW.value(),CTCodeValue1.WH_LODGE_COMPLETE_BORROW.value())))); 
		wareHouseCodeTableID.setLstEM_POS_TS(_ctCache.getIdByListCodeValue(CTCat.EM_POS_TS.value(),new ArrayList<String>(Arrays.asList(CTCodeValue1.TSA.value(),CTCodeValue1.DSA.value())))); 
		wareHouseCodeTableID.setCarStatus(_ctCache.getIdByListCodeValue(CTCat.STEP_STAT.value(),new ArrayList<String>(Arrays.asList(CTCodeValue1.POS_ABORT.value(),CTCodeValue1.POS_OPEN.value()))));
		
		wareHouseCodeTableID.setWhChanTypeSaveAndMove(_ctCache.getBy(CTCodeValue1.WH_SAVE_AND_MOVE, CTCat.WH_CHAN_TYPE).getId());
		wareHouseCodeTableID.setChangeToLodgeId(_ctCache.getIdByListCodeValue(CTCat.WH_LOGDE.value(),new ArrayList<String>(Arrays.asList(CTCodeValue1.WH_WAIT_COMPLETE.value(),CTCodeValue1.WH_WAIT_ERR_UPDATE.value()))));
		wareHouseCodeTableID.setWhCaveTypeInCavet(_ctCache.getBy(CTCodeValue1.WH_IN_CAVET, CTCat.WH_CAVET_TYPE, CTGroup.WARE_HOUSE).getId());
		wareHouseCodeTableID.setWhCaveTypeInAppendix(_ctCache.getBy(CTCodeValue1.WH_IN_APPENDIX, CTCat.WH_CAVET_TYPE, CTGroup.WARE_HOUSE).getId());
		wareHouseCodeTableID.setWhAppCavetBrType(_ctCache.getBy(CTCodeValue1.WH_APP_CAVET_BR, CTCat.WH_CHAN_TYPE, CTGroup.WARE_HOUSE).getId());
		wareHouseCodeTableID.setWhAppCavetBrIdCodeTable(_ctCache.getBy(CTCodeValue1.WH_APP_BORROW_CV, CTCat.WH_CHAN_TYPE, CTGroup.WARE_HOUSE).getId());
		wareHouseCodeTableID.setWhStatusAllocation(_ctCache.getIdByListCodeValue(CTCat.WH_LOGDE.value(),new ArrayList<String>(Arrays.asList(CTCodeValue1.WH_NEW_WAIT.value(),CTCodeValue1.WH_LODGED_ERR_UPDATE_BORROW.value(),CTCodeValue1.WH_LODGE_COMPLETE_BORROW.value()))));
		wareHouseCodeTableID.setStatusAllocation(_ctCache.getIdByListCodeValue(CTCat.WH_ASS_TYPE.value(),new ArrayList<String>(Arrays.asList(CTCodeValue1.WH_ONE.value(),CTCodeValue1.WH_MANY.value()))));
		wareHouseCodeTableID.setWhdocTypeForLodgeCavet(_ctCache.getIdByListCodeValue(CTCat.WH_DOC_TYPE.value(),new ArrayList<String>(Arrays.asList(CTCodeValue1.WH_CAVET.value(),CTCodeValue1.WH_THANKS_LETTER.value()))));
		wareHouseCodeTableID.setWhStatusLodgeContract(_ctCache.getIdByListCodeValue(CTCat.WH_LOGDE.value(),new ArrayList<String>(Arrays.asList(CTCodeValue1.WH_LODGE_COMPLETE_BORROW.value(),CTCodeValue1.WH_LODGED_COMPLETE.value(),CTCodeValue1.WH_LODGED_ERR_UPDATE.value(),CTCodeValue1.WH_LODGED_ERR_UPDATE_BORROW.value(),CTCodeValue1.WH_WAIT_COMPLETE.value(),CTCodeValue1.WH_WAIT_ERR_UPDATE.value()))));
		wareHouseCodeTableID.setWhStatusLodgeCavet(_ctCache.getIdByListCodeValue(CTCat.WH_LOGDE.value(),new ArrayList<String>(Arrays.asList(CTCodeValue1.WH_LODGED_COMPLETE.value(),CTCodeValue1.WH_LODGED_COMPLETE_RETURN.value(),CTCodeValue1.WH_LODGED_ERR_UPDATE.value(),CTCodeValue1.WH_LODGED_ERR_UPDATE_BORROW.value(),CTCodeValue1.WH_LODGED_ERR_UPDATE_RETURN.value(),CTCodeValue1.WH_LODGED_RETURN.value(),CTCodeValue1.WH_LODGE_COMPLETE_BORROW.value(),CTCodeValue1.WH_WAIT_COMPLETE.value(),CTCodeValue1.WH_WAIT_ERR_UPDATE.value(),CTCodeValue1.WH_WAIT_RETURN.value()))));
		
		wareHouseCodeTableID.setDateOperation2ASC(_ctCache.getIdByListCodeValue(CTCat.WH_LOGDE.value(),new ArrayList<String>(Arrays.asList(CTCodeValue1.WH_WAIT_COMPLETE.value(),CTCodeValue1.WH_WAIT_ERR_UPDATE.value()))));
		wareHouseCodeTableID.setDateStorageDESC(_ctCache.getIdByListCodeValue(CTCat.WH_LOGDE.value(),new ArrayList<String>(Arrays.asList(CTCodeValue1.WH_LODGED_COMPLETE.value(),CTCodeValue1.WH_LODGED_ERR_UPDATE.value(),CTCodeValue1.WH_LODGED_RETURN.value()))));
		wareHouseCodeTableID.setDateBorrowASC(_ctCache.getIdByListCodeValue(CTCat.WH_LOGDE.value(),new ArrayList<String>(Arrays.asList(CTCodeValue1.WH_LODGE_COMPLETE_BORROW.value(),CTCodeValue1.WH_LODGED_ERR_UPDATE_BORROW.value()))));
		wareHouseCodeTableID.setDateReturnDESC(_ctCache.getIdByListCodeValue(CTCat.WH_LOGDE.value(),new ArrayList<String>(Arrays.asList(CTCodeValue1.WH_LODGED_COMPLETE_RETURN.value(),CTCodeValue1.WH_LODGED_ERR_UPDATE_RETURN.value()))));
		wareHouseCodeTableID.setDateReceiveASC(_ctCache.getIdByListCodeValue(CTCat.WH_LOGDE.value(),new ArrayList<String>(Arrays.asList(CTCodeValue1.WH_WAIT_RETURN.value()))));
		
		wareHouseCodeTableID.setWhStatusBorrowReturnWait(_ctCache.getBy(CTCodeValue1.WH_WAIT, CTCat.WH_APR_STATUS).getId());
		wareHouseCodeTableID.setWhStatusBorrowedAndRerected(_ctCache.getIdByListCodeValue(CTCodeValue1.WH_APR_STATUS.value(),new ArrayList<String>(Arrays.asList(CTCodeValue1.WH_OK.value(),CTCodeValue1.WH_REJECT.value()))));
		
		wareHouseCodeTableID.setTypeReturn(_ctCache.getBy(CTCodeValue1.WH_APP_RETURN,CTCat.WH_CHAN_TYPE).getId());

		wareHouseCodeTableID.setTypeBorrow(_ctCache.getBy(CTCodeValue1.WH_APP_BORROW,CTCat.WH_CHAN_TYPE).getId());		

		wareHouseCodeTableID.setContactType(_ctCache.getBy(CTCodeValue1.MOBILE,CTCat.CONTAC_TYP).getId());
		wareHouseCodeTableID.setContactCategory(_ctCache.getBy(CTCodeValue1.CUSTOMER,CTCat.CONTAC_CAT).getId());
		
		wareHouseCodeTableID.setAddType(_ctCache.getBy(CTCodeValue1.PERMANENT,CTCat.ADDR_TYPE).getId());		

		wareHouseCodeTableID.setWhDocTypeLoanDoc(_ctCache.getBy(CTCodeValue1.WH_LOAN_DOC, CTCat.WH_DOC_TYPE, CTGroup.WARE_HOUSE).getId());
		wareHouseCodeTableID.setWhStatusBorrowContract(_ctCache.getIdByListCodeValue(CTCat.WH_LOGDE.value(),new ArrayList<String>(Arrays.asList(CTCodeValue1.WH_LODGE_COMPLETE_BORROW.value(),CTCodeValue1.WH_LODGED_COMPLETE.value(),CTCodeValue1.WH_LODGED_ERR_UPDATE.value(),CTCodeValue1.WH_LODGED_ERR_UPDATE_BORROW.value()))));
		wareHouseCodeTableID.setWhDocTypeCavet(_ctCache.getBy(CTCodeValue1.WH_CAVET, CTCat.WH_DOC_TYPE, CTGroup.WARE_HOUSE).getId());
		wareHouseCodeTableID.setWhcStatusBorrowCavet(_ctCache.getIdByListCodeValue(CTCat.WH_LOGDE.value(),new ArrayList<String>(Arrays.asList(CTCodeValue1.WH_LODGED_COMPLETE.value(),CTCodeValue1.WH_LODGED_ERR_UPDATE.value(),CTCodeValue1.WH_LODGED_ERR_UPDATE_BORROW.value(),CTCodeValue1.WH_LODGE_COMPLETE_BORROW.value()))));
		wareHouseCodeTableID.setWhStatusNotBorrowCavet(_ctCache.getIdByListCodeValue(CTCat.WH_RE_STATUS.value(),new ArrayList<String>(Arrays.asList(CTCodeValue1.WH_WAIT_RETURN.value(),CTCodeValue1.WH_OK_RETURN.value(),CTCodeValue1.WH_REJECT_RETURN.value()))));
		wareHouseCodeTableID.setWhcStatusReturnCavet(_ctCache.getIdByListCodeValue(CTCat.WH_LOGDE.value(),new ArrayList<String>(Arrays.asList(CTCodeValue1.WH_LODGED_COMPLETE.value(),CTCodeValue1.WH_LODGED_ERR_UPDATE.value()))));
		wareHouseCodeTableID.setWhcStatusLodge(_ctCache.getIdByListCodeValue(CTCat.WH_LOGDE.value(),new ArrayList<String>(Arrays.asList(CTCodeValue1.WH_LODGE_COMPLETE_BORROW.value(),CTCodeValue1.WH_LODGED_ERR_UPDATE_BORROW.value()))));
		
		wareHouseCodeTableID.setWhStatusBorrowWait(_ctCache.getBy(CTCodeValue1.WH_WAIT,CTCat.WH_APR_STATUS).getId());		
		wareHouseCodeTableID.setWhStatusReturnWait(_ctCache.getBy(CTCodeValue1.WH_WAIT_RETURN,CTCat.WH_RE_STATUS).getId());	
		
		wareHouseCodeTableID.setHashIndentityIssuePlace(_ctCache.hashCodeTablebyCodeGroupCategory(new ArrayList<String>(Arrays.asList(CTGroup.CUST.value())),Arrays.asList(CTCat.IDPLACE.value())));
		
		wareHouseCodeTableID.setWhStatusBorrowedAndRerected(_ctCache.getIdByListCodeValue(CTCat.WH_APR_STATUS.value(),new ArrayList<String>(Arrays.asList(CTCodeValue1.WH_OK.value(),CTCodeValue1.WH_REJECT.value()))));
		wareHouseCodeTableID.setWhStatusReturnAndRerected(_ctCache.getIdByListCodeValue(CTCat.WH_RE_STATUS.value(),new ArrayList<String>(Arrays.asList(CTCodeValue1.WH_OK_RETURN.value(),CTCodeValue1.WH_REJECT_RETURN.value()))));
		
		wareHouseCodeTableID.setWhApprovalStatusBorrowCavet(_ctCache.getIdByListCodeValue(CTCat.WH_APR_STATUS.value(),new ArrayList<String>(Arrays.asList(CTCodeValue1.WH_WAIT.value(),CTCodeValue1.WH_OK.value(),CTCodeValue1.WH_REJECT.value()))));
		wareHouseCodeTableID.setWhApprovalStatusReturnCavet(_ctCache.getIdByListCodeValue(CTCat.WH_RE_STATUS.value(),new ArrayList<String>(Arrays.asList(CTCodeValue1.WH_WAIT_RETURN.value(),CTCodeValue1.WH_OK_RETURN.value(),CTCodeValue1.WH_REJECT_RETURN.value()))));
		
		/*workFlowCodeTable by Code_value2*/
		wareHouseCodeTableID.setWorkFlowCodeTable(_ctCache.findCodeTableByLstGroupCateGory( new ArrayList<String>(Arrays.asList(CTGroup.WARE_HOUSE.value())), new ArrayList<String>(Arrays.asList(CTCat.WH_FLOW.value()))));
		
		/*add code WareHouse*/
		List<String> listCodeGroupWHSeach = new ArrayList<String>(Arrays.asList(CTGroup.WARE_HOUSE.value()));
		List<String> listCategoryWHSeach = new ArrayList<String>(Arrays.asList(CTCat.WH_ASS_TYPE.value(),CTCat.WH_MATERIAL.value(),CTCat.WH_CAVET_TYPE.value(),CTCat.WH_CHANGE_STATUS.value(),CTCat.WH_DOC_TYPE.value(),CTCat.WH_C_LODGE_CV.value(),CTCat.WH_BRW_TYPE.value(),CTCat.WH_RE_STATUS.value(),
				CTCat.WH_CHAN_TYPE.value(),CTCat.WH_SAVE_TRANSFER.value(),CTCat.WH_APR_STATUS.value(),CTCat.WH_UPDATE_ERR.value(),CTCat.WH_FLOW.value(),CTCat.WH_STATUS_DOC.value(),CTCat.BUS_SEGM.value(),CTCat.WH_ASS_STATUS.value(),CTCat.WH_LOGDE.value(),CTCat.WH_PRG_STATUS.value(),
				CTCat.WH_FLOW.value(),CTCat.PRD_GROUP.value(),CTCat.WH_DOC_TYPE.value(),CTCat.WH_ASS_TYPE.value(),CTCat.WH_LOGDE.value()));
		
		/*add productGroupId*/
		listCodeGroupWHSeach.addAll(Arrays.asList(CTGroup.PROD.value()));	
		listCategoryWHSeach.addAll(Arrays.asList(CTCat.PRD_GROUP.value()));	
		
		/*add IDENTITY TYPE ID*/
		listCodeGroupWHSeach.addAll(Arrays.asList(CTGroup.CUST.value()));	
		listCategoryWHSeach.addAll(Arrays.asList(CTCat.IDTYP.value()));	
		
		/*add Local*/
		listCodeGroupWHSeach.addAll(Arrays.asList(CTGroup.LOCA.value()));	
		listCategoryWHSeach.addAll(Arrays.asList(CTCat.WARD.value()));	
		listCategoryWHSeach.addAll(Arrays.asList(CTCat.DISTRICT.value()));	
		listCategoryWHSeach.addAll(Arrays.asList(CTCat.PROVINCE.value()));
		
		/*add postCodeId*/
		listCodeGroupWHSeach.addAll(Arrays.asList(CTGroup.SALE.value()));	
		listCategoryWHSeach.addAll(Arrays.asList(CTCat.POS_SIP.value()));
		
		/*add StatusBPM*/
		listCodeGroupWHSeach.addAll(Arrays.asList(CTGroup.STEP.value()));	
		listCategoryWHSeach.addAll(Arrays.asList(CTCat.STEP_STAT.value()));	
		
		/*add PostHub*/
		listCodeGroupWHSeach.addAll(Arrays.asList(CTGroup.KIOSK.value()));	
		listCategoryWHSeach.addAll(Arrays.asList(CTCat.TRAN_OFF.value()));		
		
		/*add Comminity*/
		listCodeGroupWHSeach.addAll(Arrays.asList(CTGroup.INST.value()));	
		listCategoryWHSeach.addAll(Arrays.asList(CTCat.COMM.value()));
		
		wareHouseCodeTableID.setHashCodeSeach( _ctCache.hashCodeTablebyCodeGroupCategory(listCodeGroupWHSeach,listCategoryWHSeach));
		/*add Prodctcache*/
		wareHouseCodeTableID.setHashProduct( _prdCache.getProductHashCache());
		
		/*add UplDetailId*/
		try {
			wareHouseCodeTableID.setWhUplDetailId(getWhUplDetailId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void refresh() {
		initCache();
	}

	public WareHouseCodeTableCacheDTO getWareHouseCodeTableID() {
		return wareHouseCodeTableID;
	}

	public void setWareHouseCodeTableID(WareHouseCodeTableCacheDTO wareHouseCodeTableID) {
		this.wareHouseCodeTableID = wareHouseCodeTableID;
	}
	
	private Integer getWhUplDetailId() throws Exception {
		uok = UnitOfWorkFactory.getInstance(SessionType.JTA);
		return  UnitOfWorkHelper.tryCatch(uok, ()->{
		return AllocationFactory.getSupervisorAgg(null, uok.common).getWhUplDetailId();
		});
	}
}
