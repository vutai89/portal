package com.mcredit.business.telesales.aggregate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.mcredit.business.telesales.converter.Converter;
import com.mcredit.business.telesales.object.ProspectSearch;
import com.mcredit.business.telesales.util.JSONFactory;
import com.mcredit.common.Messages;
import com.mcredit.data.common.UnitOfWorkCommon;
import com.mcredit.data.common.entity.AllocationDetail;
import com.mcredit.data.exception.NoRecordFoundException;
import com.mcredit.data.telesale.UnitOfWorkTelesale;
import com.mcredit.data.telesale.entity.CallResult;
import com.mcredit.data.telesale.entity.CustProspect;
import com.mcredit.data.telesale.entity.UplCustomer;
import com.mcredit.data.telesale.entity.UplMaster;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.dto.IdDTO;
import com.mcredit.model.dto.telesales.CallResultDTO;
import com.mcredit.model.dto.telesales.CustProspectDTO;
import com.mcredit.model.dto.telesales.ProspectReAssignDTO;
import com.mcredit.model.dto.telesales.UplCustomerProductDTO;
import com.mcredit.model.dto.warehouse.EmployeeDTO;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.object.ProspectManagementData;
import com.mcredit.model.object.telesales.CustProspectProduct;
import com.mcredit.model.object.telesales.CustProspectXsell;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.DateUtil;
import com.mcredit.util.StringUtils;

public class ProspectManagerAggregate {

	/**************** Begin Property ***************/
	private UnitOfWorkTelesale unitOfWork = null;
	private UnitOfWorkCommon unitOfCommon = null;
	
	private ModelMapper modelMapper = new ModelMapper();

	/**************** End Property ***************/
	
	public ProspectManagerAggregate(UnitOfWorkTelesale uok,UnitOfWorkCommon unitOfCommon) {
		this.unitOfWork = uok;
		this.unitOfCommon = unitOfCommon;
	}

	public Object insertProspectCall(CallResultDTO request,CodeTableDTO nextAction) throws Exception{

		CallResult cr = this.convertFrom(request);
		unitOfWork.prospectManagerRepo().upsertCallResult(cr);
		
		AllocationDetail allocationDetail = (AllocationDetail) unitOfCommon.allocationDetailRepo().get(AllocationDetail.class, cr.getAllocationDetailId());
		if(allocationDetail != null){
			/*if("REMOVE_CUST".equals(nextAction.getCodeValue1())){
				CodeTableDTO item = CacheManager.CodeTable().getIdByCategoryCodeValue("ALCTYPE_TL", "C");
				allocationDetail.setStatus(item.getId().longValue());
			}else{*/
				CodeTableDTO item = CacheManager.CodeTable().getIdByCategoryCodeValue("ALCTYPE_TL", "W");
				allocationDetail.setStatus(item.getId().longValue());
			//}
			unitOfCommon.allocationDetailRepo().merge(allocationDetail);
		}
		
		return new IdDTO(cr.getId());
	}
	
	public Object upsertCustomerProspect(CustProspectDTO obj,UserDTO user) throws Exception {
		CustProspect oldCP = null;
		
		if(obj != null  && obj.getId() != null && obj.getId().longValue() > 0) {
			
			oldCP = unitOfWork.prospectManagerRepo().getCustProspectById(obj.getId());
			
			if(oldCP == null)
				throw new ValidationException(Messages.getString("validation.not.exists", "Cust Prospect Id") + ": " + obj.getId().toString());
		} 
		CustProspect cp = Converter.convertFrom(obj, user);
		cp.setCreatedDate(oldCP != null ? oldCP.getCreatedDate() : new Date());
		cp.setCreatedBy(oldCP != null ? oldCP.getCreatedBy() : user.getLoginId());
		cp.setUplCustomerId(obj.getUplCustomerId());
		
		// Xoa province cu
		if (cp.getPermanentProvince() != null) {
			Long uplCustomerId = obj.getUplCustomerId();
			UplCustomer uplCustomer = unitOfWork.uplCustomerRepo().getUplCustomerbyID(uplCustomerId);
			uplCustomer.setProvince(null);
			unitOfWork.uplCustomerRepo().merge(uplCustomer);
		} 
		
		if(oldCP == null){
			AllocationDetail ad = unitOfCommon.allocationDetailRepo().getAllocationDetailTelesale(obj.getUplCustomerId());
			cp.setAllocationDetailId(ad == null ? 0 : ad.getId());
		}else
			cp.setAllocationDetailId(oldCP.getAllocationDetailId());
		
		if(oldCP == null)
			unitOfWork.prospectManagerRepo().upsertCustProspect(cp);
		else
			unitOfWork.prospectManagerRepo().merge(cp);
		// Luu lai UPL Upload neu co cham diem
		if(!StringUtils.isNullOrEmpty(obj.getIsMark())) {
			Long uplCustomerId = obj.getUplCustomerId();
			UplCustomer uplCustomer = unitOfWork.uplCustomerRepo().getUplCustomerbyID(uplCustomerId);
			// Luu co cham diem hay khong 
			uplCustomer.setUdf01(obj.getIsMark());
			// Luu ngay yeu cau cham diem 
			Date date = Calendar.getInstance().getTime();  
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
			String dateMark = dateFormat.format(date);  
			
			uplCustomer.setUdf04(dateMark);
			unitOfWork.uplCustomerRepo().merge(uplCustomer);
		}
		return new IdDTO(cp.getId());
	}
	
	public CustProspectDTO getCustProspectByUplCusId(Long uplCusId) throws Exception {
		CustProspectDTO result = null;
		
		CustProspectProduct obj = unitOfWork.prospectManagerRepo().findCustProsptectProductByUPLCustId(uplCusId);
		
		if(obj == null) {
			UplCustomerProductDTO uplProductItem = unitOfWork.prospectManagerRepo().findUplCustomerProductById(uplCusId);
			
			if(uplProductItem != null) {
				
				// for XSell
				UplCustomer uplCus  = unitOfWork.prospectManagerRepo().findUplCustomerById(uplCusId);
				if(uplCus == null)
					uplCus = new UplCustomer();
					
				result = Converter.convertFrom(uplProductItem, uplCus);
				UplMaster uplMaster = this.getUplMasterByUPLCustId(uplCusId);
				
				
				if( uplMaster.getFromSource()!=null && !uplMaster.getFromSource().equals(0L) ) {
					CodeTableDTO ct = CodeTableCacheManager.getInstance().getbyID(uplMaster.getFromSource().intValue());
					if( ct!=null )
						result.setFromsource(ct.getCodeValue1());
				}
				
				result.setUplCode(uplMaster != null ? uplMaster.getUplCode() : StringUtils.Empty);
				result.setUplCustomerId(uplProductItem.getId());
				
			}
			
		} else {
			result = Converter.convertFrom(obj);
			if (result.getAllocationDetailId() != null)
				result.setUplCode(unitOfWork.prospectManagerRepo().getUplCodeByAllocationId(result.getAllocationDetailId()));
		}
		
		boolean isXsellProduct = result.getFromsource().equals(CTCodeValue1.MIS.toString());
		if (isXsellProduct) {
			findMoreInfoForXsell(result, uplCusId);
		}
		
		System.out.println(JSONFactory.toJson(result));
		return result; 
	}
	
	private void findMoreInfoForXsell(CustProspectDTO custProspectDTO, Long uplCusId) {
		
		CustProspectXsell result = unitOfWork.prospectManagerRepo().findMoreInfoForXsell(uplCusId);
		
		if( result!=null ) {
			custProspectDTO.setXsMobile(result.getXsMobile());
			custProspectDTO.setIdentityNumberArmy(result.getIdentity_number_army());
			custProspectDTO.setCmndIssueDate(result.getCmndIssueDate());
			custProspectDTO.setCmndIssuePlace(result.getCmndIssuePlace());
			custProspectDTO.setCmqdIssueDate(result.getCmqdIssueDate());
			custProspectDTO.setCmqdIssuePlace(result.getCmqdIssuePlace());
	        
			
			custProspectDTO.setXsellValidatedFromDate(result.getXsellValidatedFromDate());
			custProspectDTO.setXsellValidatedToDate(result.getXsellValidatedToDate());
			custProspectDTO.setRefFullName1(result.getRefFullName1());
			custProspectDTO.setRefFullName2(result.getRefFullName2());
			custProspectDTO.setRelationRefPerson1(result.getRelationRefPerson1());
			custProspectDTO.setRelationRefPerson2(result.getRelationRefPerson2());
			custProspectDTO.setRefPerson1Mobile(result.getRefPerson1Mobile());
			custProspectDTO.setRefPerson2Mobile(result.getRefPerson2Mobile());
			
			
			custProspectDTO.setXsGender(result.getXsGender());
			custProspectDTO.setXsProfessionnal(result.getXsProfessionnal());
			custProspectDTO.setXsHouseNumber(result.getXsHouseNumber());
			custProspectDTO.setXsWard(result.getXsWard());
			custProspectDTO.setXsDistrict(result.getXsDistrict());
			custProspectDTO.setXsProvince(result.getXsProvince());
			custProspectDTO.setXsAccommodation(result.getXsAccommodation());
			
			custProspectDTO.setXsPerHouseNumber(result.getXsPerHouseNumber());
			custProspectDTO.setXsPerWard(result.getXsPerWard());
			custProspectDTO.setXsPerDistrict(result.getXsPerDistrict());
			custProspectDTO.setXsPerProvince(result.getXsPerProvince());
			
			custProspectDTO.setXsCompanyName(result.getXsCompanyName());
			custProspectDTO.setXsCompanyAddress(result.getXsCompanyAddress());
			custProspectDTO.setXsPositionInComp(result.getXsPositionInComp());
			custProspectDTO.setXsCustIncome(result.getXsCustIncome());
			custProspectDTO.setXsCompWard(result.getXsCompWard());
			custProspectDTO.setXsCompDistrict(result.getXsCompDistrict());
			custProspectDTO.setXsCompProvince(result.getXsProvince());

		}
	}
	
	/*public Object getCustProspectById(Long custProspectId) throws Exception {
		
		CustProspect obj = unitOfWork.prospectManagerRepo().getCustProspectById(custProspectId);
		if(obj == null)
			throw new ValidationException("Invalid  ust Prospect Id: " + custProspectId.toString());
		
		CustProspectDTO CP = Converter.convertFrom(obj);
		CP.setUplCode(unitOfWork.prospectManagerRepo().getUplCodeByAllocationId(CP.getAllocationDetailId()));
		
		return CP;
	}*/

	public ProspectManagementData findProspect(ProspectSearch input, String campaignCodeLstOfSup) throws Exception {
		return unitOfWork.prospectManagerRepo().findProspectV2(modelMapper.map(input, com.mcredit.model.object.ProspectSearch.class), campaignCodeLstOfSup);
	}

	@SuppressWarnings("unchecked")
	public boolean prospectReAssign(ProspectReAssignDTO input) throws Exception {
		return unitOfWork.prospectManagerRepo().prospectReAssign(input.getLstProspectReAssign());
	}

	@SuppressWarnings("unchecked")
	public List<CallResult> findCallResult(Long custProspectId) throws Exception {
		return unitOfWork.prospectManagerRepo().getCallResultByDate(custProspectId);
	}

	private Long getAllocationDetailIdByCustProspectId(Long custProspectId) throws ValidationException {
		try {
			return unitOfWork.prospectManagerRepo().getAllocationDetailIdByCustProspectId(custProspectId).longValue();
		} catch (NoRecordFoundException e) {
			throw new ValidationException(e.getMessage());
		}
	}

	private UplMaster getUplMasterByUPLCustId(Long allocationId) {
		return unitOfWork.prospectManagerRepo().getUplMasterByUPLCustId(allocationId);
	}
	
	public CustProspect findCustProsptectByUPLCustId(Long custProspectId) {
		return unitOfWork.prospectManagerRepo().findCustProsptectByUPLCustId(custProspectId);
	}
	
	public AllocationDetail getAllocationDetailByUPLCustomerIdAndObjectId(Long uplCustomerId) {
		return unitOfCommon.allocationDetailRepo().getAllocationDetailTelesale(uplCustomerId);
	}	

	private CallResult convertFrom(CallResultDTO request) throws Exception {
		CallResult cr = new CallResult();
		
		Date nextActionDate = null;
		if(!StringUtils.isNullOrEmpty(request.getNextActionDate()))
			nextActionDate = DateUtil.toDate(request.getNextActionDate(), Converter.dateFormat);
		
		cr.setAllocationDetailId(this.getAllocationDetailIdByCustProspectId(request.getCustProspectId()));
		cr.setCallTimes(unitOfWork.prospectManagerRepo().getNextCallTimesByCustProspectId(request.getCustProspectId()));
		cr.setCallResult(StringUtils.toInt(request.getCallResult()));
		cr.setCallStatus(StringUtils.toInt(request.getCallStatus()));
		//cr.setNextAction(StringUtils.toInt(request.getNextAction()));
		cr.setNextActionDate(nextActionDate);
		cr.setNote(request.getNote());
		cr.setCustProspectId(request.getCustProspectId());
		cr.setCallDate(new Date());
		if(request.getId() != null)
			cr.setId(request.getId().longValue());

		return cr;
	}
	
	public EmployeeDTO getManagerByLoginId (String loginId) {
		return unitOfWork.prospectManagerRepo().getManagerByLoginId(loginId);
	}
}
