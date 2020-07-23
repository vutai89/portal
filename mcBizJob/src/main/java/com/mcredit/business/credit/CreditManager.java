package com.mcredit.business.credit;

import java.text.DecimalFormat;

import com.mcredit.business.common.ProductAggregate;
import com.mcredit.business.credit.dto.CreditDTO;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.dto.ErrorResultDTO;
import com.mcredit.model.dto.IdDTO;
import com.mcredit.model.enums.CTCat;
import com.mcredit.sharedbiz.aggregate.UserAggregate;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.sharedbiz.manager.BaseManager;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class CreditManager extends BaseManager {

	public IdDTO upsertCredit(CreditDTO request, String updateId) throws Exception {
		
		return this.tryCatch(()->{
			IdDTO result = new IdDTO();
			codeTableFieldProcess(request);
			ProductCodeProcess(request);
			employeeProcess(request);

			if (updateId != null)
				request.getAppRequest().setId(Long.parseLong(updateId));

			CreditAggregate item = CreditFactory.getInstance(request, this.uok.credit);
			item.upsertCredit();
			result.setId(item.getApplicationRequest().getId());
			
			return result;
		});
		
	}

	public ErrorResultDTO deleteCredit(String ids) throws Exception {
		
		return this.tryCatch(()->{
			Long id = Long.parseLong(new DecimalFormat("###.#").format(Double.parseDouble(ids)));
			
			CreditAggregate item = CreditFactory.getInstance(id, this.uok.credit);
			item.removeCredit();
			ErrorResultDTO returnResult = new ErrorResultDTO("200","Successful!");
			return returnResult;
		});
		
	}

	private Integer findIdCodeTable(String category, String value1) throws Exception {
		CodeTableDTO codeTable = CodeTableCacheManager.getInstance().getIdByCategoryCodeValue(category, value1.trim());

		if (codeTable == null)
			throw new ValidationException("category[" + category + "] - value1[" + value1 + "] in CodeTable not found");

		return codeTable.getId();
	}

	private void codeTableFieldProcess(CreditDTO request) throws Exception {
		if (!StringUtils.isNullOrEmpty(request.getAppRequest().getLnTenorValue()))
			request.getAppRequest().setLnTenor(
					findIdCodeTable(CTCat.CA_TENOR.value(), request.getAppRequest().getLnTenorValue()));

		if (!StringUtils.isNullOrEmpty(request.getAppRequest().getTransOfficeIdValue()))
			request.getAppRequest().setTransOfficeId(findIdCodeTable(CTCat.TRAN_OFF.value(),
					request.getAppRequest().getTransOfficeIdValue()));

		if (!StringUtils.isNullOrEmpty(request.getAppRequest().getApplyToObjectValue()))
			request.getAppRequest().setApplyToObject(findIdCodeTable(CTCat.APPLY_OBJ.value(),
					request.getAppRequest().getApplyToObjectValue()));

		if (!StringUtils.isNullOrEmpty(request.getAppRequest().getHasInsuranceValue()))
			request.getAppRequest().setHasInsurance(
					findIdCodeTable(CTCat.BOOLEAN.value(), request.getAppRequest().getHasInsuranceValue()));

		if (!StringUtils.isNullOrEmpty(request.getAppRequest().getInsuCompanyValue()))
			request.getAppRequest().setInsuCompany(findIdCodeTable(CTCat.INSU_COMP.value(),
					request.getAppRequest().getInsuCompanyValue()));

		if (!StringUtils.isNullOrEmpty(request.getAppRequest().getInsuStaffIdValue()))
			request.getAppRequest().setInsuStaffid(findIdCodeTable(CTCat.INSU_STFID.value(),
					request.getAppRequest().getInsuStaffIdValue()));

		if (!StringUtils.isNullOrEmpty(request.getAppRequest().getInsuTypeValue()))
			request.getAppRequest().setInsuType(
					findIdCodeTable(CTCat.INSU_TYPE.value(), request.getAppRequest().getInsuTypeValue()));

		if (!StringUtils.isNullOrEmpty(request.getAppRequest().getDisbursementChannelValue()))
			request.getAppRequest().setDisbursementChannel(findIdCodeTable(CTCat.DIS_CHANN.value(),
					request.getAppRequest().getDisbursementChannelValue()));

		if (!StringUtils.isNullOrEmpty(request.getAppRequest().getDisbursementMethodValue()))
			request.getAppRequest().setDisbursementMethod(findIdCodeTable(CTCat.DIS_METHOD.value(),
					request.getAppRequest().getDisbursementMethodValue()));

		if (!StringUtils.isNullOrEmpty(request.getAppRequest().getIsDuplicatedValue()))
			request.getAppRequest().setIsDuplicated(findIdCodeTable(CTCat.DUPLICATE.value(),
					request.getAppRequest().getIsDuplicatedValue()));

		if (!StringUtils.isNullOrEmpty(request.getAppRequest().getLnPurposeValue()))
			request.getAppRequest().setLnPurpose(
					findIdCodeTable(CTCat.CA_PURPOSE.value(), request.getAppRequest().getLnPurposeValue()));

		if (!StringUtils.isNullOrEmpty(request.getAppLMN().getDisbursementStatusValue()))
			request.getAppLMN().setDisbursementStatus(findIdCodeTable(CTCat.DIS_STATUS.value(),
					request.getAppLMN().getDisbursementStatusValue()));

		if (!StringUtils.isNullOrEmpty(request.getAppAdditional().getIsIbAllowedValue()))
			request.getAppAdditional().setIsIbAllowed(findIdCodeTable(CTCat.BOOLEAN.value(),
					request.getAppAdditional().getIsIbAllowedValue()));

		if (!StringUtils.isNullOrEmpty(request.getAppAdditional().getReceiveCardAddrValue()))
			request.getAppAdditional().setReceiveCardAddr(findIdCodeTable(CTCat.CARD_RADD.value(),
					request.getAppAdditional().getReceiveCardAddrValue()));

		if (!StringUtils.isNullOrEmpty(request.getAppAdditional().getIssueFeeCollValue()))
			request.getAppAdditional().setIssueFeeColl(findIdCodeTable(CTCat.BOOLEAN.value(),
					request.getAppAdditional().getIssueFeeCollValue()));

		if (!StringUtils.isNullOrEmpty(request.getAppAdditional().getAnnualFeeCollValue()))
			request.getAppAdditional().setAnnualFeeColl(findIdCodeTable(CTCat.BOOLEAN.value(),
					request.getAppAdditional().getAnnualFeeCollValue()));

		if (!StringUtils.isNullOrEmpty(request.getAppAdditional().getAnnualFeeFrequencyValue()))
			request.getAppAdditional().setAnnualFeeFrequency(findIdCodeTable(CTCat.FRE_COLANN.value(),
					request.getAppAdditional().getAnnualFeeFrequencyValue()));

		if (!StringUtils.isNullOrEmpty(request.getAppAdditional().getInsuFeeCollValue()))
			request.getAppAdditional().setInsuFeeColl(findIdCodeTable(CTCat.BOOLEAN.value(),
					request.getAppAdditional().getInsuFeeCollValue()));

		if (!StringUtils.isNullOrEmpty(request.getAppAdditional().getInsuFeeFrequencyValue()))
			request.getAppAdditional().setInsuFeeFrequency(findIdCodeTable(CTCat.FRE_COLINS.value(),
					request.getAppAdditional().getInsuFeeFrequencyValue()));

		if (!StringUtils.isNullOrEmpty(request.getAppAppraisal().getIndustryValue()))
			request.getAppAppraisal().setIndustry(
					findIdCodeTable(CTCat.INDUSTRY.value(), request.getAppAppraisal().getIndustryValue()));

	}
	
	private void ProductCodeProcess(CreditDTO request) throws Exception {
		String productCode = request.getAppRequest().getProductCode();
		ProductAggregate productAggregate = new ProductAggregate(this.uok.common, productCode);
		
		if (productAggregate.getProduct() == null) throw new ValidationException("No Such ProductCode: "+productCode);
		Long productId = productAggregate.getProduct().getId();
		
		request.getAppRequest().setProductId(productId != null ? productId.intValue() : null);
	}
	
	private void employeeProcess(CreditDTO request) throws Exception {
		
		String empCode = request.getAppRequest().getSaleCode();
		
		//change into from UsersAggregate to UserAggregate
		UserAggregate usersAggregate = new UserAggregate(this.uok);
		
		if (usersAggregate.getEmpBySaleCode(empCode) == null) {
			System.out.println("No Such saleCode: " + empCode);
			request.getAppRequest().setSaleId(null);
		}else {
			Long userId = usersAggregate.getEmployeeLink().getEmpId();
			
			request.getAppRequest().setSaleId(userId != null ? userId : null);
		}
	
	}

	
	/*public static void main(String[] args) throws Exception {
		CreditDTO request = new CreditDTO();
		request.setAppRequest(new CreditApplicationRequestDTO());
		request.getAppRequest().setSaleCode("DS011706002");
		
		new CreditManager().saleCodeProcess(request);
	}*/
	
	public void close() {
	}
}
