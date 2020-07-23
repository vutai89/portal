package com.mcredit.business.appraisal.convert;

import java.util.Date;

import com.mcredit.business.appraisal.aggregate.AppraisalAggregate;
import com.mcredit.common.Messages;
import com.mcredit.data.appraisal.entity.CreditAppraisalData;
import com.mcredit.model.dto.appraisal.AppraisalDataDetailDTO;
import com.mcredit.model.dto.appraisal.AppraisalObjectDTO;
import com.mcredit.model.object.RuleObject;
import com.mcredit.util.DateTimeFormat;
import com.mcredit.util.DateUtil;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

public class Converter {
	
	/**
	 * Convert from appraisal dto (sent by appraisal tool) to appraisal db model
	 * @author catld.ho
	 * @param appraisalObj : apraisal data in DTO model
	 * @return appraisal data in DB model
	 * @throws Exception
	 */
	public static CreditAppraisalData getCreditAppraisalData(AppraisalObjectDTO appraisalObj) throws Exception {
		CreditAppraisalData creditAppraisalData = new CreditAppraisalData();
		Date now = new Date();
		creditAppraisalData.setAction(appraisalObj.getAction());
		creditAppraisalData.setCreateBy(appraisalObj.getUpdateUser());
		creditAppraisalData.setLastUpdateBy(appraisalObj.getUpdateUser());
		creditAppraisalData.setCreatedDate(now);
		creditAppraisalData.setLastUpdatedDate(now);
		creditAppraisalData.setBpmAppId(appraisalObj.getBpmAppId());
		creditAppraisalData.setConclude(appraisalObj.getConclude());
		creditAppraisalData.setTransactionId(appraisalObj.getTransactionId());
		creditAppraisalData.setAppraisalDataDetail(JSONConverter.toJSON(appraisalObj.getAppraisalDataDetail()));
		
		return creditAppraisalData;
	}
	
	/**
	 * Convert from appraisal dto (sent by bpm) to appraisal db model
	 * @author catld.ho
	 * @param appraisalObj : appraisal data in DTO model
	 * @param appraisalData : appraisal data in DB model
	 * @return appraisal data in DB model
	 * @throws Exception
	 */
	public static CreditAppraisalData losGetCreditAppraisalData(AppraisalObjectDTO appraisalObj, CreditAppraisalData appraisalData) throws Exception {
		if (appraisalData == null)
			appraisalData = new CreditAppraisalData();
		
		Date now = new Date();
		appraisalData.setAction(AppraisalAggregate.ACTION_BPM + appraisalObj.getAction());
		appraisalData.setBpmAppId(appraisalObj.getBpmAppId());
		if (StringUtils.isNullOrEmpty(appraisalData.getCreateBy()))
			appraisalData.setCreateBy(appraisalObj.getUpdateUser());

		appraisalData.setLastUpdateBy(appraisalObj.getUpdateUser());
		if (appraisalData.getCreatedDate() == null)
			appraisalData.setCreatedDate(now);

		appraisalData.setLastUpdatedDate(now);
		appraisalObj.getAppraisalDataDetail().setCreatedDate(DateUtil.changeFormat(appraisalObj.getAppraisalDataDetail().getCreatedDate(), DateTimeFormat.yyyyMMddHHmmss.getDescription(), DateTimeFormat.yyyyMMdd.getDescription()));
//		appraisalObj.getAppraisalDataDetail().setBirthDate(DateUtil.changeFormat(appraisalObj.getAppraisalDataDetail().getBirthDate(), DateTimeFormat.yyyyMMdd_T_HHmmssSSSXXX.getDescription(), DateTimeFormat.yyyyMMdd.getDescription()));
//		appraisalObj.getAppraisalDataDetail().setCitizenIDDate(DateUtil.changeFormat(appraisalObj.getAppraisalDataDetail().getCitizenIDDate(), DateTimeFormat.yyyyMMdd_T_HHmmssSSSXXX.getDescription(), DateTimeFormat.yyyyMMdd.getDescription()));
//		if (!StringUtils.isNullOrEmpty(appraisalObj.getAppraisalDataDetail().getMilitaryIDDate()))
//			appraisalObj.getAppraisalDataDetail().setMilitaryIDDate(DateUtil.changeFormat(appraisalObj.getAppraisalDataDetail().getMilitaryIDDate(), DateTimeFormat.yyyyMMdd_T_HHmmssSSSXXX.getDescription(), DateTimeFormat.yyyyMMdd.getDescription()));
		
		appraisalData.setAppraisalDataDetail(JSONConverter.toJSON(appraisalObj.getAppraisalDataDetail()));
		
		return appraisalData;
	}
	
	/**
	 * Convert from appraisal db model to appraisal dto
	 * @author catld.ho
	 * @param appraisalData :  appraisal data in DB model update by appraisal tool
	 * @param appraisalDataFromLOS :  appraisal data in DB model update by bpm
	 * @return appraisal data in DTO model
	 * @throws Exception
	 */
	public static AppraisalObjectDTO getAppraisalData(CreditAppraisalData appraisalData, CreditAppraisalData appraisalDataFromLOS) throws Exception {
		AppraisalObjectDTO appraisalObjectDTO = null;
		AppraisalDataDetailDTO appraisalDataDetail = null;
		AppraisalDataDetailDTO appraisalDataDetailFromLOS = null;

		appraisalDataDetailFromLOS = JSONConverter.toObject(appraisalDataFromLOS.getAppraisalDataDetail(), AppraisalDataDetailDTO.class);
		if (appraisalDataDetailFromLOS == null)
			throw new Exception(Messages.getString("appraisal.dataDetail.notFound", appraisalDataFromLOS.getBpmAppId() + ":" + appraisalDataFromLOS.getAction()));

		if (appraisalData == null)
			appraisalData = new CreditAppraisalData();

		appraisalObjectDTO = new AppraisalObjectDTO();
		appraisalObjectDTO.setAction(appraisalData.getAction());
		appraisalObjectDTO.setUpdateUser(appraisalData.getLastUpdateBy());
		appraisalObjectDTO.setBpmAppId(appraisalDataFromLOS.getBpmAppId());
		appraisalObjectDTO.setConclude(appraisalData.getConclude());

		if (!StringUtils.isNullOrEmpty(appraisalData.getAppraisalDataDetail()))
			appraisalDataDetail = JSONConverter.toObject(appraisalData.getAppraisalDataDetail(), AppraisalDataDetailDTO.class);
		
		if (appraisalDataDetail == null)
			appraisalDataDetail = new AppraisalDataDetailDTO();

		appraisalObjectDTO.setAppraisalDataDetail(appraisalDataDetail);

		// get not editable variable from LOS (BPM)
		appraisalDataDetail.setAppNumber(appraisalDataDetailFromLOS.getAppNumber());
//		appraisalDataDetail.setProductId(appraisalDataDetailFromLOS.getProductId());
		appraisalDataDetail.setProductCode(appraisalDataDetailFromLOS.getProductCode());
		appraisalDataDetail.setProductName(appraisalDataDetailFromLOS.getProductName());
		appraisalDataDetail.setProductGroup(appraisalDataDetailFromLOS.getProductGroup());
		appraisalDataDetail.setCustomerName(appraisalDataDetailFromLOS.getCustomerName());
		appraisalDataDetail.setTypeOfDocPerson(appraisalDataDetailFromLOS.getTypeOfDocPerson());
		appraisalDataDetail.setCitizenID(appraisalDataDetailFromLOS.getCitizenID());
		appraisalDataDetail.setCitizenIDDate(appraisalDataDetailFromLOS.getCitizenIDDate());
		appraisalDataDetail.setMilitaryID(appraisalDataDetailFromLOS.getMilitaryID());
		appraisalDataDetail.setMilitaryIDDate(appraisalDataDetailFromLOS.getMilitaryIDDate());
		appraisalDataDetail.setCreatedDate(appraisalDataDetailFromLOS.getCreatedDate());
		appraisalDataDetail.setBirthDate(appraisalDataDetailFromLOS.getBirthDate());
		appraisalDataDetail.setSalaryPaymentType(appraisalDataDetailFromLOS.getSalaryPaymentType());
		appraisalDataDetail.setCustCompanyCat(appraisalDataDetailFromLOS.getCustCompanyCat());
		appraisalDataDetail.setGoodsInformation(appraisalDataDetailFromLOS.getGoodsInformation());
		appraisalDataDetail.setGoodsPrice(appraisalDataDetailFromLOS.getGoodsPrice());
		appraisalDataDetail.setHasInsurance(appraisalDataDetailFromLOS.getHasInsurance());
		appraisalDataDetail.setInsuranceTerm(appraisalDataDetailFromLOS.getInsuranceTerm());
		appraisalDataDetail.setInsuranceTermFee(appraisalDataDetailFromLOS.getInsuranceTermFee());
		appraisalDataDetail.setInsuranceTermOther(appraisalDataDetailFromLOS.getInsuranceTermOther());
		appraisalDataDetail.setInsuranceFee(appraisalDataDetailFromLOS.getInsuranceFee());
		appraisalDataDetail.setInterestRate(appraisalDataDetailFromLOS.getInterestRate());
		
		// get editable variable from LOS (BPM)
		appraisalDataDetail.setLoanAmount(appraisalDataDetailFromLOS.getLoanAmount());
		appraisalDataDetail.setLoanTenor(appraisalDataDetailFromLOS.getLoanTenor());
		appraisalDataDetail.setNumberOfRelationFinanceCompany(appraisalDataDetailFromLOS.getNumberOfRelationFinanceCompany());
		appraisalDataDetail.setNumberOfRelationBank(appraisalDataDetailFromLOS.getNumberOfRelationBank());
		appraisalDataDetail.setCustomerIncome(appraisalDataDetailFromLOS.getCustomerIncome());
		appraisalDataDetail.setLoanAmountAfterInsurance(appraisalDataDetailFromLOS.getLoanAmountAfterInsurance());
		
		return appraisalObjectDTO;
	}

	/**
	 * Get new rule object
	 * @author catld.ho
	 * @param ruleCode
	 * @param productCode
	 * @param productGroup
	 * @param createdDate
	 * @param birthDate
	 * @param loanTenor
	 * @param salaryPaymentType
	 * @param CustCompanyCat
	 * @param customerIncone
	 * @param goodsPrice
	 * @param typeOfGoods
	 * @param brand
	 * @param numberOfRelationOrganize
	 * @param numberOfRelationFinanceCompany
	 * @param totalOutstandingLoanAtCIC
	 * @param pti
	 * @param dti
	 * @param loanAmount
	 * @param averageElectricBill
	 * @param averageAccountBalance
	 * @param annualFeeLifeInsurance
	 * @return Rule Object
	 * @throws Exception
	 */
	public static RuleObject createRuleObject(String ruleCode, String productCode, String productGroup, String createdDate, String birthDate, Integer loanTenor, 
			String salaryPaymentType, String custCompanyCat, Double customerIncone, 
			Double goodsPrice, String typeOfGoods, String brand, Integer numberOfRelationOrganize, Integer numberOfRelationFinanceCompany, Double totalOutstandingLoanAtCIC,
			Double pti, Double dti, Integer loanAmount, Double averageElectricBill, Double averageAccountBalance, Double annualFeeLifeInsurance) throws Exception {
		RuleObject ro = new RuleObject();
		
		ro.setRuleCode(ruleCode);
		ro.setProductCode(productCode);
		ro.setProductGroup(productGroup);
		ro.setCreatedDate(createdDate);
		ro.setBirthDate(birthDate);
		if (loanTenor != null) {
			ro.setLoanTenor(loanTenor);
		}
		ro.setSalaryPaymentType(salaryPaymentType);
		ro.setCustCompanyCat(custCompanyCat);
		ro.setCustomerIncome(customerIncone);
		ro.setGoodsPrice(goodsPrice);
		ro.setTypeOfGoods(typeOfGoods);
		ro.setBrand(brand);
		ro.setNumberOfRelationOrganize(numberOfRelationOrganize);
		ro.setNumberOfRelationFinanceCompany(numberOfRelationFinanceCompany);
		ro.setTotalOutstandingLoanAtCIC(totalOutstandingLoanAtCIC);
		ro.setPti(pti);
		ro.setDti(dti);
		ro.setLoanAmount(loanAmount);
		ro.setAverageElectricBill(averageElectricBill);
		ro.setAverageAccountBalance(averageAccountBalance);
		ro.setAnnualFeeLifeInsurance(annualFeeLifeInsurance);
		
		return ro;
	}

}
