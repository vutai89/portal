package com.mcredit.business.telesales.converter;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mcredit.business.telesales.validation.CustProspectValidation;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.mobile.UnitOfWorkMobile;
import com.mcredit.data.mobile.entity.ExternalUserMapping;
import com.mcredit.data.mobile.entity.UplCreditAppRequest;
import com.mcredit.data.telesale.entity.CallResult;
import com.mcredit.data.telesale.entity.CustProspect;
import com.mcredit.data.telesale.entity.UplCustomer;
import com.mcredit.data.user.entity.Employee;
import com.mcredit.data.user.repository.EmployeeRepository;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.dto.ProductDTO;
import com.mcredit.model.dto.telesales.CallResultDTO;
import com.mcredit.model.dto.telesales.CustProspectDTO;
import com.mcredit.model.dto.telesales.UplCustomerProductDTO;
import com.mcredit.model.dto.telesales.UploadCaseDTO;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.MessageLogStatus;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.UplCreAppRequestStatus;
import com.mcredit.model.object.mobile.dto.CreCaseBPMDTO;
import com.mcredit.model.object.mobile.dto.InfoCreCaseDTO;
import com.mcredit.model.object.mobile.enums.MfsMsgTransType;
import com.mcredit.model.object.mobile.enums.MfsServiceName;
import com.mcredit.model.object.telesales.CustProspectProduct;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.util.MessageTranslator;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.DateUtil;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

public class Converter {
	
	public static String dateFormat = "dd/MM/yyyy";
	private static String dateTimeFormat = "dd/MM/yyyy HH:mm";
	
	public static CustProspect convertFrom(CustProspectDTO cpdto, UserDTO user)
			throws ValidationException, ParseException {
		CustProspectValidation validation = new CustProspectValidation();

		CustProspect obj = validation.validateCustProspect(cpdto);

		obj.setGender(MessageTranslator.TransformCodeValue1(cpdto.getGender(),"GENDER"));
		obj.setProfessional(MessageTranslator.TransformCodeValue1(cpdto.getProfessional(), "PROFESSION"));
		obj.setAccommodationType(MessageTranslator.TransformCodeValue1(cpdto.getAccommodationType(), "ACCOM_TYPE"));
		obj.setPermanentProvince(MessageTranslator.TransformCodeValue1(cpdto.getPermanentProvince(), "PROVINCE"));
		obj.setPermanentDistrict(MessageTranslator.TransformCodeValue1(cpdto.getPermanentDistrict(), "DISTRICT"));
		obj.setPermanentWard(MessageTranslator.TransformCodeValue1(cpdto.getPermanentWard(), "WARD"));
		obj.setCompAddrProvince(MessageTranslator.TransformCodeValue1(cpdto.getCompAddrProvince(), "PROVINCE"));
		obj.setCompAddrDistrict(MessageTranslator.TransformCodeValue1(cpdto.getCompAddrDistrict(), "DISTRICT"));
		obj.setCompAddrWard(MessageTranslator.TransformCodeValue1(cpdto.getCompAddrWard(), "WARD"));
		obj.setPosition(MessageTranslator.TransformCodeValue1(cpdto.getPosition(), "POSITION"));
		obj.setNote(cpdto.getNote());
		
		obj.setLastUpdatedBy(user.getLoginId());
		obj.setPermanentAddress(cpdto.getPermanentAddress());

		return obj;
	}
	
	/*public static CustProspectDTO convertFrom(CustProspect cp) throws ValidationException, ParseException {
		CodeTableCacheManager cache = CodeTableCacheManager.getInstance();		
		CustProspectDTO obj = new CustProspectDTO();
  
		obj.setGender(cache.getCodeById1(cp.getGender()).getCodeValue1());
		obj.setProfessional(cp.getProfessional() == null ? StringUtils.Empty : cache.getCodeById1(cp.getProfessional()).getCodeValue1());
		obj.setAccommodationType(cp.getAccommodationType() == null ? StringUtils.Empty :cache.getCodeById1(cp.getAccommodationType()).getCodeValue1());
		obj.setPermanentProvince(cp.getPermanentProvince() == null ? StringUtils.Empty :cache.getCodeById1(cp.getPermanentProvince()).getCodeValue1());
		obj.setPermanentDistrict(cp.getPermanentDistrict() == null ? StringUtils.Empty :cache.getCodeById1(cp.getPermanentDistrict()).getCodeValue1());
		obj.setPermanentWard(cp.getPermanentWard() == null ? StringUtils.Empty :cache.getCodeById1(cp.getPermanentWard()).getCodeValue1());
		obj.setCompAddrProvince(cp.getCompAddrProvince() == null ? StringUtils.Empty :cache.getCodeById1(cp.getCompAddrProvince()).getCodeValue1());
		obj.setCompAddrDistrict(cp.getCompAddrDistrict() == null ? StringUtils.Empty :cache.getCodeById1(cp.getCompAddrDistrict()).getCodeValue1());
		obj.setCompAddrWard(cp.getCompAddrWard() == null ? StringUtils.Empty :cache.getCodeById1(cp.getCompAddrWard()).getCodeValue1());
		obj.setPosition(cp.getPosition() == null ? StringUtils.Empty :cache.getCodeById1(cp.getPosition()).getCodeValue1());
		
		obj.setRecordStatus(cp.getRecordStatus());
		obj.setAllocationDetailId(cp.getAllocationDetailId());
		obj.setBirthDate(cp.getBirthDate() != null ? DateUtil.toString(cp.getBirthDate(), "dd/MM/yyyy") : StringUtils.Empty);
		obj.setCompAddrStreet(cp.getCompAddrStreet());
		obj.setCreatedBy(cp.getCreatedBy());
		obj.setCustIncome(cp.getCustIncome());
		obj.setCustName(cp.getCustName());
		obj.setIdentityNumber(cp.getIdentityNumber());
		obj.setMobile(cp.getMobile());
		obj.setNote(cp.getNote());
		obj.setUplCustomerId(cp.getUplCustomerId());
		obj.setPermanentAddr(cp.getPermanentAddr());
		obj.setId(cp.getId());
	
		return obj;
	}*/
	
	public static CustProspectDTO convertFrom(CustProspectProduct cp) throws ValidationException, ParseException {
		CodeTableCacheManager cache = CodeTableCacheManager.getInstance();		
		CustProspectDTO obj = new CustProspectDTO();
  
		obj.setGender(cache.getCodeById1(cp.getGender()).getCodeValue1());
		obj.setProfessional(cp.getProfessional() == null ? StringUtils.Empty : cache.getCodeById1(cp.getProfessional()).getCodeValue1());
		obj.setAccommodationType(cp.getAccommodationType() == null ? StringUtils.Empty :cache.getCodeById1(cp.getAccommodationType()).getCodeValue1());
		obj.setPermanentProvince(cp.getPermanentProvince() == null ? StringUtils.Empty :cache.getCodeById1(cp.getPermanentProvince()).getCodeValue1());
		obj.setPermanentDistrict(cp.getPermanentDistrict() == null ? StringUtils.Empty :cache.getCodeById1(cp.getPermanentDistrict()).getCodeValue1());
		obj.setPermanentWard(cp.getPermanentWard() == null ? StringUtils.Empty :cache.getCodeById1(cp.getPermanentWard()).getCodeValue1());
		obj.setCompAddrProvince(cp.getCompAddrProvince() == null ? StringUtils.Empty :cache.getCodeById1(cp.getCompAddrProvince()).getCodeValue1());
		obj.setCompAddrDistrict(cp.getCompAddrDistrict() == null ? StringUtils.Empty :cache.getCodeById1(cp.getCompAddrDistrict()).getCodeValue1());
		obj.setCompAddrWard(cp.getCompAddrWard() == null ? StringUtils.Empty :cache.getCodeById1(cp.getCompAddrWard()).getCodeValue1());
		obj.setPosition(cp.getPosition() == null ? StringUtils.Empty :cache.getCodeById1(cp.getPosition()).getCodeValue1());
		
		obj.setRecordStatus(cp.getRecordStatus());
		obj.setAllocationDetailId(cp.getAllocationDetailId());
		obj.setBirthDate(cp.getBirthDate() != null ? DateUtil.toString(cp.getBirthDate(), "dd/MM/yyyy") : StringUtils.Empty);
		obj.setCompAddrStreet(cp.getCompAddrStreet());
		obj.setCreatedBy(cp.getCreatedBy());
		obj.setCustIncome(cp.getCustIncome());
		obj.setCustName(cp.getCustName());
		obj.setIdentityNumber(cp.getIdentityNumber());
		obj.setMobile(cp.getMobile());
		obj.setNote(cp.getNote());
		obj.setUplCustomerId(cp.getUplCustomerId());
		obj.setPermanentAddr(cp.getPermanentAddr());
		obj.setId(cp.getId());
		obj.setCreatedDate(cp.getCreatedDate());
		obj.setNote(cp.getNote());
		// Lead gen
		obj.setIncome(cp.getIncome());
		obj.setScoreRange(cp.getScoreRange());
		obj.setSourceSystem(cp.getSourceSystem());
		obj.setProductId(cp.getProduct_id());
		obj.setProductName(cp.getProductName());
		obj.setProductCode(cp.getProductCode());
		obj.setMinLoanAmount(cp.getMinLoanAmount());
		obj.setMaxLoanAmount(cp.getMaxLoanAmount());
		obj.setMinTenor(cp.getMinTenor());
		obj.setMaxTenor(cp.getMaxTenor());
		obj.setYearlyRate(cp.getYearlyRate());
		obj.setMonthlyRate(cp.getMonthlyRate());
		
		//xsell
		obj.setProvince(cp.getProvince());
		obj.setMinScore(cp.getMin_score());
		obj.setMaxScore(cp.getMax_score());
		obj.setRefId(cp.getRef_id());
		obj.setPreProductName(cp.getPre_product_name());
		obj.setPreProductCode(cp.getPre_product_code());
		obj.setPreMinLimit(cp.getPre_min_limit());
		obj.setPreMaxLimit(cp.getPre_max_limit());
		obj.setPreMinTenor(cp.getPre_min_tenor());
		obj.setPreMaxTenor(cp.getPre_max_tenor());
		obj.setPreMinEmi(cp.getPre_min_emi());
		obj.setAppProductName(cp.getApp_product_name());
		obj.setAppProductCode(cp.getApp_product_code());
		obj.setAppLoanApprovedAmt(cp.getApp_loan_approved_amt());
		obj.setAppIntRate(cp.getApp_int_rate());
		obj.setAppTermLoan(cp.getApp_term_loan());
		obj.setDisbursementDate(cp.getDisbursement_date());
		
		if (cp.getMat_date() != null) {
			obj.setMatDate(DateUtil.toString(cp.getMat_date(), "yyyy-MM-dd"));
		}
		
		obj.setCommoditiesCode(cp.getCommodities_code());
		obj.setDataSource(cp.getData_source());
		obj.setLeadSource(cp.getLead_source());
		obj.setCustId(cp.getCust_id());
		obj.setPreMaxEmi(cp.getPre_max_emi());
		obj.setAppEmi(cp.getApp_emi());
		obj.setCompanyName(cp.getCompany_name());
		obj.setPreMaxAmount(cp.getPreMaxAmount());
		obj.setPreMinAmount(cp.getPreMinAmount());
		obj.setPreTenor(cp.getPreTenor());
		obj.setPeriodMinTenor(cp.getPeriodMinTenor());
		obj.setPeriodMaxTenor(cp.getPeriodMaxTenor());
		
		obj.setPermanentAddress(cp.getPermanent_address());
		obj.setFromsource(cp.getFromsource());
		obj.setNewMobile(cp.getNewMobile());
		return obj;
	}

	public static List<CallResultDTO> convertFrom(List<CallResult> CRList) throws ParseException {
		CodeTableCacheManager CT = CodeTableCacheManager.getInstance();
		List<CallResultDTO> CRDTOList = new ArrayList<CallResultDTO>();
		for (CallResult cr : CRList) {
			if (cr == null)
				continue;

			CallResultDTO obj = new CallResultDTO();
			obj.setAllocationDetailId(cr.getAllocationDetailId());
			obj.setCallResult(CT.getCodeById(cr.getCallResult()).getCodeValue1());
			obj.setCallResultDes(CT.getCodeById(cr.getCallResult()).getDescription1());
			obj.setCallStatus(CT.getCodeById(cr.getCallStatus()).getCodeValue1());
			obj.setCallStatusDes(CT.getCodeById(cr.getCallStatus()).getDescription1());
			//obj.setNextAction(CT.getCodeById(cr.getNextAction()).getCodeValue1());
			//obj.setNextActionDes(CT.getCodeById(cr.getNextAction()).getDescription1());
			obj.setId(cr.getId());
			obj.setCustProspectId(cr.getCustProspectId());
			obj.setNote(cr.getNote());
			obj.setNextActionDate(cr.getNextActionDate() != null ? DateUtil.toString(cr.getNextActionDate(), dateFormat) : StringUtils.Empty);
			obj.setCallTimes(cr.getCallTimes());
			obj.setCallDate(DateUtil.toString(cr.getCallDate(), dateTimeFormat));
			CRDTOList.add(obj);
		}
		return CRDTOList;
	}
	
	public static CustProspectDTO convertFrom(UplCustomerProductDTO uplProductItem, UplCustomer CP) {

        if (uplProductItem == null) {
            return null;
        }

        CustProspectDTO result = new CustProspectDTO();
        result.setCustName(uplProductItem.getCustomerName());
        result.setPermanentAddr(uplProductItem.getAddress());
        result.setBirthDate(uplProductItem.getBirthDate());
        result.setIdentityNumber(uplProductItem.getIdentityNumber());
        result.setMobile(uplProductItem.getMobile());
        result.setNote(uplProductItem.getNote());
        result.setIncome(uplProductItem.getIncome());
        result.setCreatedDate(uplProductItem.getCreatedDate());
        result.setNote(uplProductItem.getNote());
        
        // Lead gen
        result.setScoreRange(uplProductItem.getScoreRange());
        result.setSourceSystem(uplProductItem.getSourceSystem());
        result.setProductId(uplProductItem.getProductId());
        result.setProductName(uplProductItem.getProductName());
        result.setProductCode(uplProductItem.getProductCode());
        result.setMinLoanAmount(uplProductItem.getMinLoanAmount());
        result.setMaxLoanAmount(uplProductItem.getMaxLoanAmount());
        result.setMinTenor(uplProductItem.getMinTenor());
        result.setMaxTenor(uplProductItem.getMaxTenor());
        result.setYearlyRate(uplProductItem.getYearlyRate());
        result.setMonthlyRate(uplProductItem.getMonthlyRate());

        //xsell
        result.setProvince(CP.getProvince());
        if (CP.getMinScore() != null) {
            result.setMinScore(CP.getMinScore().toString());
        }
        if (CP.getMaxScore() != null) {
            result.setMaxScore(CP.getMaxScore().toString());
        }
        result.setRefId(CP.getRefId());
        result.setIdentityNumberArmy(CP.getIdentityNumberArmy());
        result.setPreProductName(CP.getPreProductName());
        result.setPreProductCode(CP.getPreProductCode());
        if (CP.getPreMinLimit() != null) {
            result.setPreMinLimit(CP.getPreMinLimit().toString());
        }
        if (CP.getPreMaxLimit() != null) {
            result.setPreMaxLimit(CP.getPreMaxLimit().toString());
        }
        if (CP.getPreMinTenor() != null) {
            result.setPreMinTenor(CP.getPreMinTenor().toString());
        }
        if (CP.getPreMaxTenor() != null) {
            result.setPreMaxTenor(CP.getPreMaxTenor().toString());
        }
        if (CP.getPreMinEmi() != null) {
            result.setPreMinEmi(CP.getPreMinEmi().toString());
        }
        result.setAppProductName(CP.getAppProductName());
        result.setAppProductCode(CP.getAppProductCode());
        if (CP.getAppLoanApprovedAmt() != null) {
            result.setAppLoanApprovedAmt(CP.getAppLoanApprovedAmt().toString());
        }
        if (CP.getAppIntRate() != null) {
            result.setAppIntRate(CP.getAppIntRate().toString());
        }
        if (CP.getAppTermLoan() != null) {
            result.setAppTermLoan(CP.getAppTermLoan().toString());
        }
        if (CP.getDisbursementDate() != null) {
            result.setDisbursementDate(CP.getDisbursementDate().toString());
        }
        if (CP.getMatDate() != null) {
            result.setMatDate(CP.getMatDate().toString());
        }
        result.setCommoditiesCode(CP.getCommoditiesCode());
        result.setDataSource(CP.getDataSource());
        result.setLeadSource(CP.getLeadSource());
        if (CP.getCustId() != null) {
            result.setCustId(CP.getCustId().toString());
        }
        if (CP.getPreMaxEmi() != null) {
            result.setPreMaxEmi(CP.getPreMaxEmi().toString());
        }
        if (CP.getAppEmi() != null) {
            result.setAppEmi(CP.getAppEmi().toString());
        }
        
        // Trust Connect
        if("Nam".equals(CP.getGender()))
        	result.setGender("1");
        else if("Nu".equals(CP.getGender()))
        	result.setGender("2");
        
        // AMO
        result.setOriginIncome(uplProductItem.getOriginIncome());

        return result;
    }
	
	public static UplCreditAppRequest getNewCase(UploadCaseDTO newCase, Long productId, UserDTO _user) throws Exception {
		UplCreditAppRequest ucar = new UplCreditAppRequest();
		
		ucar.setCustomerName(newCase.getCustomerName());
		ucar.setProductId(productId);
		ucar.setCitizenId(newCase.getCitizenId());
		ucar.setIssueDateCitizen(DateUtil.toDate(newCase.getIssueDateCitizen(), "dd/MM/yyyy"));
		ucar.setTempResidence(newCase.getTempResidence());
		ucar.setLoanTenor(newCase.getLoanTenor());
		ucar.setLoanAmount(newCase.getLoanAmount());
		ucar.setHasInsurance(newCase.getHasInsurance());
		ucar.setShopCode(newCase.getShopCode());
		ucar.setMobileTSA(newCase.getMobileTSA());
		ucar.setUplCustomerId(newCase.getUplCustomerId());
		
		ucar.setCreatedBy(_user.getLoginId());
		ucar.setLastUpdatedBy(_user.getLoginId());
//		ucar.setRecordStatus("A");
		
		Date now = new Date();
		ucar.setCreatedDate(now);
		ucar.setLastUpdatedDate(now);
		ucar.setLastUpdateFromLOS(now);
		
		ucar.setStatus(UplCreAppRequestStatus.X.value());
		ucar.setSaleCode(_user.getEmpCode());
		ucar.setBirthDate(DateUtil.toDate(newCase.getCustBirthday(), "dd/MM/yyyy"));
		ucar.setGender(newCase.getCustGender());
		ucar.setCustomerIncome(newCase.getCustIncome());
		
		return ucar;
	}
	
	public static MessageLog getCreateCaseRequest(UploadCaseDTO upload, UplCreditAppRequest ucar, UserDTO _user, EmployeeRepository empRepo, String appNumBerOld, String newMobile, ProductDTO product) throws Exception {
		MessageLog message = new MessageLog();
//		ExternalUserMapping externalUserMapping = _unitOfWorkMobile.externalUserMappingRepo()
//				.getEUMappingByEmpId(_user.getLoginId());
//
//		message.setRelationId(String.valueOf(_user.getId()) + "-" + String.valueOf(externalUserMapping.getId()) + "-"
//				+ String.valueOf(id));
		message.setTransId(ucar.getId());
		message.setTransType(MfsMsgTransType.MSG_TRANS_TYPE_CREATE_CASE_BPM.value());
		message.setFromChannel(BusinessConstant.MCP);
		message.setToChannel(BusinessConstant.BPM);
		message.setRequestTime(new Timestamp(new Date().getTime()));
		message.setMsgStatus(MessageLogStatus.NEW.value()); // new case message log
		message.setServiceName(MfsServiceName.SERVICE_CREATE_CASE_BPM.value());

		// set message_req
		String proUid = CacheManager.Parameters().findParamValueAsString(ParametersName.MFS_CREATE_CASE_PRO_UID);
		String tasUid = CacheManager.Parameters().findParamValueAsString(ParametersName.MFS_CREATE_CASE_TAS_UID);
		Gson gson = new GsonBuilder().create();
		CreCaseBPMDTO creCaseBPMDTO = new CreCaseBPMDTO();
		creCaseBPMDTO.setPro_uid(proUid);
		creCaseBPMDTO.setTas_uid(tasUid);
		List<InfoCreCaseDTO> variables = new ArrayList<>();
		InfoCreCaseDTO info = new InfoCreCaseDTO();
		info.setCat_result_company_name("");
		info.setCompanyTaxNumber("");
		info.setCat_result_type("");
		info.setCitizenID_sale(upload.getCitizenId());
		info.setCustomerName_sale(upload.getCustomerName());
		info.setHasInsurrance_sale(upload.getHasInsurance());
		info.setIssueDateCitizenID_sale(new SimpleDateFormat("yyyy-MM-dd")
				.format(new SimpleDateFormat("dd/MM/yyyy").parse(upload.getIssueDateCitizen())));
		info.setLoanAmount_sale(upload.getLoanAmount());
		info.setLoanTenor_sale(upload.getLoanTenor());
		info.setProductSchemesFull("");
//		info.setSaleCode(_user.getEmpCode());
		info.setTSACode(_user.getEmpCode());
		info.setSaleCode_label(_user.getEmpCode());
//		info.setSaleID(externalUserMapping.getUserUid());

		Employee employee = empRepo.findById(Long.valueOf(_user.getEmpId()));
		String saleMobile = employee != null ? employee.getMobilePhone() : "";
		String saleName = employee != null ? employee.getFullName() : "";
//		info.setSaleMobile(ucar.getMobileTSA());	// lay sdt nhap tu web portal miniCRM
		info.setTSAMobilephone(ucar.getMobileTSA());	// lay sdt nhap tu web portal miniCRM
//		info.setSaleName(saleName);
		info.setTSAName(saleName);
		info.setSchemeProduct_sale(upload.getProductCode());
		info.setSchemeProduct_sale_label(product.getProductName());
		CodeTableDTO ctDTO = CodeTableCacheManager.getInstance().getIdByCategoryCodeValue(CTCat.TRAN_OFF.value(), upload.getShopCode());
		info.setSignContractAddress(ctDTO == null ? "" : ctDTO.getDescription2());
		info.setSignContractAt(upload.getShopCode());
		info.setStartUserName(ucar.getCreatedBy());
		info.setTemporaryResidence_sale("0".equals(upload.getTempResidence())?"1":"2");
		
		info.setCreateFromCRMTelesales("1");
		info.setTlsUserName(ucar.getCreatedBy());
		info.setAppNumberOld(appNumBerOld);
		if (StringUtils.isNullOrEmpty(newMobile))	// Khong co sdt moi thi lay so dien thoai cua ho so cu (xsMobile)
			info.setNewMobile(upload.getCustomerMobile());
		else
			info.setNewMobile(newMobile);
		info.setHasCourier("0");		// default no courier
		info.setGender_sale("M".equals(upload.getCustGender()) ? "1": "2");
		info.setCustomerIncome_sale(upload.getCustIncome());
		info.setDob_sale(new SimpleDateFormat("yyyy-MM-dd")
				.format(new SimpleDateFormat("dd/MM/yyyy").parse(upload.getCustBirthday())));
		
		variables.add(info);
		creCaseBPMDTO.setVariables(variables);
		message.setMsgRequest(JSONConverter.toJSON(creCaseBPMDTO));

		return message;
	}
	
}
