package com.mcredit.business.appraisal.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mcredit.business.appraisal.aggregate.AppraisalAggregate;
import com.mcredit.common.Messages;
import com.mcredit.model.dto.appraisal.AppraisalDataDetailDTO;
import com.mcredit.model.dto.appraisal.AppraisalObjectDTO;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class AppraisalValidation extends AbstractValidation {
	
	private static ParametersCacheManager parameter = CacheManager.Parameters();;
	private static final String COMMA = ",";
	private static List<String> PRD_GRP_MILITARY;
	private static List<String> PRD_GRP_CS_SY;
	private static List<String> PRD_GRP_CS_EVN;
	private static List<String> PRD_GRP_CS_INSURANCE;
	private static List<String> PRD_GRP_CS_BANK_ACCT;
	private static final String TYPE_OF_DOCS_PERSON_CITIZEN_ID = "1";
	private static final String TYPE_OF_DOCS_PERSON_MILITARY_ID = "2";	
	
	public AppraisalValidation() {
		PRD_GRP_MILITARY = new ArrayList<>();
		PRD_GRP_CS_SY = new ArrayList<>();
		PRD_GRP_CS_EVN = new ArrayList<>();
		PRD_GRP_CS_INSURANCE = new ArrayList<>();
		PRD_GRP_CS_BANK_ACCT = new ArrayList<>();
		String cs_military = parameter.findParamValueAsString(ParametersName.PRD_GRP_MILITARY);
		String cs_sy = parameter.findParamValueAsString(ParametersName.PRD_GRP_CS_SY);
		String cs_evn = parameter.findParamValueAsString(ParametersName.PRD_GRP_CS_EVN);
		String cs_insurance = parameter.findParamValueAsString(ParametersName.PRD_GRP_CS_INSURANCE);
		String cs_bank_acct = parameter.findParamValueAsString(ParametersName.PRD_GRP_CS_BANK_ACCT);
		
		if (!StringUtils.isNullOrEmpty(cs_military))
			PRD_GRP_MILITARY.addAll(Arrays.asList(cs_military.split(COMMA)));
		
		if (!StringUtils.isNullOrEmpty(cs_sy))
			PRD_GRP_CS_SY.addAll(Arrays.asList(cs_sy.split(COMMA)));
		
		if (!StringUtils.isNullOrEmpty(cs_evn))
			PRD_GRP_CS_EVN.addAll(Arrays.asList(cs_evn.split(COMMA)));

		if (!StringUtils.isNullOrEmpty(cs_insurance))
			PRD_GRP_CS_INSURANCE.addAll(Arrays.asList(cs_insurance.split(COMMA)));

		if (!StringUtils.isNullOrEmpty(cs_bank_acct))
			PRD_GRP_CS_BANK_ACCT.addAll(Arrays.asList(cs_bank_acct.split(COMMA)));
	}
	
	/**
	 * Validate parameteres when get appraisal data
	 * @author catld.ho
	 * @param bpmAppId : appId correspond customer profile
	 * @throws ValidationException
	 */
	public void getAppraisalData(String bpmAppId) throws ValidationException {
		if (StringUtils.isNullOrEmpty(bpmAppId))
			getMessageDes().add(Messages.getString("appraisal.param.required", "bpmAppId"));
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}

	/**
	 * Validate parameteres when update appraisal data
	 * @author catld.ho
	 * @param appraisalObj : appraisal object sent by appraisal tool (portal)
	 * @throws ValidationException
	 */
	public void saveAppraisalData(AppraisalObjectDTO appraisalObj) throws ValidationException {
		if (appraisalObj == null) {
			getMessageDes().add(Messages.getString("appraisal.payload.required"));
		} else {
			if (StringUtils.isNullOrEmpty(appraisalObj.getAction()))
				getMessageDes().add(Messages.getString("appraisal.param.required", "action"));

			if (StringUtils.isNullOrEmpty(appraisalObj.getBpmAppId()))
				getMessageDes().add(Messages.getString("appraisal.param.required", "bpmAppId"));

			if (StringUtils.isNullOrEmpty(appraisalObj.getTransactionId()))
				getMessageDes().add(Messages.getString("appraisal.param.required", "transactionId"));
		}
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
	
	/**
	 * Validate parameteres when update appraisal data from bpm
	 * @author catld.ho
	 * @param appraisalObj : appraisal object sent by bpm
	 * @throws ValidationException
	 */
	public void losSaveAppraisalData(AppraisalObjectDTO appraisalObj) throws ValidationException {
		if (appraisalObj == null || appraisalObj.getAppraisalDataDetail() == null) {
			getMessageDes().add(Messages.getString("appraisal.payload.required"));
		} else {
			if (StringUtils.isNullOrEmpty(appraisalObj.getBpmAppId()))
				getMessageDes().add(Messages.getString("appraisal.param.required", "bpmAppId"));
			
			if (StringUtils.isNullOrEmpty(appraisalObj.getAction()))
				getMessageDes().add(Messages.getString("appraisal.param.required", "action"));

			if (appraisalObj.getAppraisalDataDetail().getAppNumber() == null)
				getMessageDes().add(Messages.getString("appraisal.param.required", "appNumber"));

			if (StringUtils.isNullOrEmpty(appraisalObj.getAppraisalDataDetail().getProductCode()))
				getMessageDes().add(Messages.getString("appraisal.param.required", "productCode"));

			if (StringUtils.isNullOrEmpty(appraisalObj.getAppraisalDataDetail().getProductName()))
				getMessageDes().add(Messages.getString("appraisal.param.required", "productName"));

			if (StringUtils.isNullOrEmpty(appraisalObj.getAppraisalDataDetail().getProductGroup()))
				getMessageDes().add(Messages.getString("appraisal.param.required", "productGroup"));

			if (StringUtils.isNullOrEmpty(appraisalObj.getAppraisalDataDetail().getCustomerName()))
				getMessageDes().add(Messages.getString("appraisal.param.required", "customerName"));

			if (appraisalObj.getAppraisalDataDetail().getLoanAmount() == null)
				getMessageDes().add(Messages.getString("appraisal.param.required", "loanAmount"));
			
			if (appraisalObj.getAppraisalDataDetail().getLoanAmountAfterInsurance() == null)
				getMessageDes().add(Messages.getString("appraisal.param.required", "loanAmountAfterInsurance"));
			
			if (appraisalObj.getAppraisalDataDetail().getLoanTenor() == null)
				getMessageDes().add(Messages.getString("appraisal.param.required", "loanTenor"));
			
			if (StringUtils.isNullOrEmpty(appraisalObj.getAppraisalDataDetail().getCreatedDate()))
				getMessageDes().add(Messages.getString("appraisal.param.required", "createdDate"));

			if (StringUtils.isNullOrEmpty(appraisalObj.getAppraisalDataDetail().getBirthDate()))
				getMessageDes().add(Messages.getString("appraisal.param.required", "birthDate"));

			if (StringUtils.isNullOrEmpty(appraisalObj.getAppraisalDataDetail().getHasInsurance()))
				getMessageDes().add(Messages.getString("appraisal.param.required", "hasInsurance"));

			if (appraisalObj.getAppraisalDataDetail().getInterestRate() == null)
				getMessageDes().add(Messages.getString("appraisal.param.required", "interestRate"));
			
			if (AppraisalAggregate.INSTALLMENT_LOAN.equals(appraisalObj.getAppraisalDataDetail().getProductGroup())) {
				if (appraisalObj.getAppraisalDataDetail().getGoodsInformation() == null || appraisalObj.getAppraisalDataDetail().getGoodsInformation().size() < 1)
					getMessageDes().add(Messages.getString("appraisal.param.required", "goodsInformation"));
				
				if (appraisalObj.getAppraisalDataDetail().getGoodsPrice() == null)
					getMessageDes().add(Messages.getString("appraisal.param.required", "goodsPrice"));
			}
			
//			if (appraisalObj.getAppraisalDataDetail().getNumberOfRelationFinanceCompany() == null)
//				getMessageDes().add(Messages.getString("appraisal.param.required", "numberOfRelationFinanceCompany"));
//			
//			if (appraisalObj.getAppraisalDataDetail().getNumberOfRelationBank() == null)
//				getMessageDes().add(Messages.getString("appraisal.param.required", "numberOfRelationBank"));
			
			if ("1".equals(appraisalObj.getAppraisalDataDetail().getHasInsurance())) {
				if (appraisalObj.getAppraisalDataDetail().getInsuranceFee() == null)
					getMessageDes().add(Messages.getString("appraisal.param.required", "insuranceFee"));
			}
			
			if (!StringUtils.isNullOrEmpty(appraisalObj.getAppraisalDataDetail().getProductCode())) {
				if (((AppraisalAggregate.CASH_LOAN.equals(appraisalObj.getAppraisalDataDetail().getProductGroup()) || AppraisalAggregate.CONCENTRATING_DATA_ENTRY.equals(appraisalObj.getAppraisalDataDetail().getProductGroup())) && (StringUtils.isNullOrEmpty(appraisalObj.getAppraisalDataDetail().getTypeOfDocPerson()) || TYPE_OF_DOCS_PERSON_CITIZEN_ID.equals(appraisalObj.getAppraisalDataDetail().getTypeOfDocPerson()))) ||
						(AppraisalAggregate.INSTALLMENT_LOAN.equals(appraisalObj.getAppraisalDataDetail().getProductGroup()) && !PRD_GRP_MILITARY.contains(appraisalObj.getAppraisalDataDetail().getProductCode()))) {
					if (StringUtils.isNullOrEmpty(appraisalObj.getAppraisalDataDetail().getCitizenID()))
						getMessageDes().add(Messages.getString("appraisal.param.required", "citizenID"));
					
					if (StringUtils.isNullOrEmpty(appraisalObj.getAppraisalDataDetail().getCitizenIDDate()))
						getMessageDes().add(Messages.getString("appraisal.param.required", "citizenIDDate"));
				}
				
				if (((AppraisalAggregate.CASH_LOAN.equals(appraisalObj.getAppraisalDataDetail().getProductGroup()) || AppraisalAggregate.CONCENTRATING_DATA_ENTRY.equals(appraisalObj.getAppraisalDataDetail().getProductGroup())) && TYPE_OF_DOCS_PERSON_MILITARY_ID.equals(appraisalObj.getAppraisalDataDetail().getTypeOfDocPerson())) ||
						(AppraisalAggregate.INSTALLMENT_LOAN.equals(appraisalObj.getAppraisalDataDetail().getProductGroup()) && PRD_GRP_MILITARY.contains(appraisalObj.getAppraisalDataDetail().getProductCode()))) {
					if (StringUtils.isNullOrEmpty(appraisalObj.getAppraisalDataDetail().getMilitaryID()))
						getMessageDes().add(Messages.getString("appraisal.param.required", "militaryID"));
					
					if (StringUtils.isNullOrEmpty(appraisalObj.getAppraisalDataDetail().getMilitaryIDDate()))
						getMessageDes().add(Messages.getString("appraisal.param.required", "militaryIDDate"));
				}
	
				if (PRD_GRP_CS_SY.contains(appraisalObj.getAppraisalDataDetail().getProductCode())) {
					if (StringUtils.isNullOrEmpty(appraisalObj.getAppraisalDataDetail().getSalaryPaymentType()))
						getMessageDes().add(Messages.getString("appraisal.param.required", "salaryPaymentType"));
	
					if (StringUtils.isNullOrEmpty(appraisalObj.getAppraisalDataDetail().getCustCompanyCat()))
						getMessageDes().add(Messages.getString("appraisal.param.required", "custCompanyCat"));
				}
				
				if (PRD_GRP_CS_INSURANCE.contains(appraisalObj.getAppraisalDataDetail().getProductCode())) {
					if (StringUtils.isNullOrEmpty(appraisalObj.getAppraisalDataDetail().getInsuranceTerm()))
						getMessageDes().add(Messages.getString("appraisal.param.required", "insuranceTerm"));
	
//					if (appraisalObj.getAppraisalDataDetail().getInsuranceTermFee() == null)
//						getMessageDes().add(Messages.getString("appraisal.param.required", "insuranceTermFee"));
				}
			}
		}
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
	
	/**
	 * Validate parameteres when get appraisal result
	 * @author catld.ho
	 * @param appraisalObj : appraisal object sent by bpm
	 * @throws ValidationException
	 */
	public void getAppraisalResult(AppraisalObjectDTO appraisalObj) throws ValidationException {
		if (appraisalObj == null) {
			getMessageDes().add(Messages.getString("appraisal.payload.required"));
		} else {
			if (StringUtils.isNullOrEmpty(appraisalObj.getTransactionId()))
				getMessageDes().add(Messages.getString("appraisal.param.required", "transactionId"));
		}
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
	
	/**
	 * Validate parameteres when request check customer profile
	 * @author catld.ho
	 * @param dataDetail : appraisal object sent by appraisal tool (portal)
	 * @throws ValidationException
	 */
	public void checkCustomer(AppraisalDataDetailDTO dataDetail) throws ValidationException {
		if (dataDetail == null) {
			getMessageDes().add(Messages.getString("appraisal.payload.required"));
		} else {
			if (StringUtils.isNullOrEmpty(dataDetail.getProductCode()))
				getMessageDes().add(Messages.getString("appraisal.param.required", "productCode"));

			if (StringUtils.isNullOrEmpty(dataDetail.getProductGroup()))
				getMessageDes().add(Messages.getString("appraisal.param.required", "productGroup"));

			// age
			if (dataDetail.getLoanTenor() == null)
				getMessageDes().add(Messages.getString("appraisal.param.required", "loanTenor"));

			if (StringUtils.isNullOrEmpty(dataDetail.getBirthDate()))
				getMessageDes().add(Messages.getString("appraisal.param.required", "birthDate"));

			if (StringUtils.isNullOrEmpty(dataDetail.getCreatedDate()))
				getMessageDes().add(Messages.getString("appraisal.param.required", "createdDate"));
			
			// income
			if (dataDetail.getCustomerIncomeAppraisal() == null)
				getMessageDes().add(Messages.getString("appraisal.param.required", "customerIncomeAppraisal"));

			if (dataDetail.getCustomerIncomeInterpolate() == null)
				getMessageDes().add(Messages.getString("appraisal.param.required", "customerIncomeInterpolate"));

			// Hồ sơ thuộc các nhóm SP: CS SY
			if (PRD_GRP_CS_SY.contains(dataDetail.getProductCode())) {
				if (StringUtils.isNullOrEmpty(dataDetail.getSalaryPaymentType()))
					getMessageDes().add(Messages.getString("appraisal.param.required", "salaryPaymentType"));

				if (StringUtils.isNullOrEmpty(dataDetail.getCustCompanyCat()))
					getMessageDes().add(Messages.getString("appraisal.param.required", "custCompanyCat"));
			}
			
			// loan amount suggest
			if (dataDetail.getLoanAmountAfterInsurance() == null)
				getMessageDes().add(Messages.getString("appraisal.param.required", "loanAmountAfterInsurance"));
			
			// loan tenor
//			if (dataDetail.getLoanTenor() == null) {
//				getMessageDes().add(Messages.getString("appraisal.param.required", "loanTenor"));
//			}
			
			// prepay rate
			if (AppraisalAggregate.INSTALLMENT_LOAN.equals(dataDetail.getProductGroup())) {
				if (dataDetail.getDownPaymentRate() == null)
					getMessageDes().add(Messages.getString("appraisal.param.required", "downPaymentRate"));

				if (dataDetail.getGoodsPrice() == null)
					getMessageDes().add(Messages.getString("appraisal.param.required", "goodsPrice"));
			}
			
			// appraisal
			if (dataDetail.getNumberOfRelationOrganize() == null)
				getMessageDes().add(Messages.getString("appraisal.param.required", "numberOfRelationOrganize"));

			if (dataDetail.getNumberOfRelationFinanceCompany() == null)
				getMessageDes().add(Messages.getString("appraisal.param.required", "numberOfRelationFinanceCompany"));

			if (dataDetail.getTotalOutstandingLoanCIC() == null)
				getMessageDes().add(Messages.getString("appraisal.param.required", "totalOutstandingLoanCIC"));

			if (dataDetail.getPti() == null)
				getMessageDes().add(Messages.getString("appraisal.param.required", "pti"));
			
			if (dataDetail.getDti() == null)
				getMessageDes().add(Messages.getString("appraisal.param.required", "dti"));

			if (dataDetail.getLoanAmount() == null)
				getMessageDes().add(Messages.getString("appraisal.param.required", "loanAmount"));

//			if (dataDetail.getCustomerIncomeInterpolate() == null) {
//				getMessageDes().add(Messages.getString("appraisal.param.required", "customerIncomeInterpolate"));
//			}
			
			// Hồ sơ thuộc nhóm SP: CS EVN: electricBillM1
			if (PRD_GRP_CS_EVN.contains(dataDetail.getProductCode())) {
				if (dataDetail.getElectricBillM1() == null)
					getMessageDes().add(Messages.getString("appraisal.param.required", "electricBillM1"));

				if (dataDetail.getElectricBillM2() == null)
					getMessageDes().add(Messages.getString("appraisal.param.required", "electricBillM2"));

				if (dataDetail.getElectricBillM3() == null)
					getMessageDes().add(Messages.getString("appraisal.param.required", "electricBillM3"));

				if (dataDetail.getElectricBillM1() != null && dataDetail.getElectricBillM2() != null && dataDetail.getElectricBillM3() != null) {
					Double avg = (dataDetail.getElectricBillM1() + dataDetail.getElectricBillM1() + dataDetail.getElectricBillM1()) /3;
					dataDetail.setAverageElectricBill(avg);
				}
			}
			
			// Hồ sơ thuộc nhóm SP: CS Bank Acct: accountBalanceMinM1
			if (PRD_GRP_CS_BANK_ACCT.contains(dataDetail.getProductCode())) {
				if (dataDetail.getAccountBalanceMinM1() == null)
					getMessageDes().add(Messages.getString("appraisal.param.required", "accountBalanceMinM1"));

				if (dataDetail.getAccountBalanceMaxM1() == null)
					getMessageDes().add(Messages.getString("appraisal.param.required", "accountBalanceMaxM1"));

				if (dataDetail.getAccountBalanceMinM2() == null)
					getMessageDes().add(Messages.getString("appraisal.param.required", "accountBalanceMinM2"));

				if (dataDetail.getAccountBalanceMaxM2() == null)
					getMessageDes().add(Messages.getString("appraisal.param.required", "accountBalanceMaxM2"));

				if (dataDetail.getAccountBalanceMinM3() == null)
					getMessageDes().add(Messages.getString("appraisal.param.required", "accountBalanceMinM3"));

				if (dataDetail.getAccountBalanceMaxM3() == null)
					getMessageDes().add(Messages.getString("appraisal.param.required", "accountBalanceMaxM3"));

				if (dataDetail.getAccountBalanceMinM4() == null)
					getMessageDes().add(Messages.getString("appraisal.param.required", "accountBalanceMinM4"));

				if (dataDetail.getAccountBalanceMaxM4() == null)
					getMessageDes().add(Messages.getString("appraisal.param.required", "accountBalanceMaxM4"));

				if (dataDetail.getAccountBalanceMinM5() == null)
					getMessageDes().add(Messages.getString("appraisal.param.required", "accountBalanceMinM5"));

				if (dataDetail.getAccountBalanceMaxM5() == null)
					getMessageDes().add(Messages.getString("appraisal.param.required", "accountBalanceMaxM5"));

				if (dataDetail.getAccountBalanceMinM6() == null)
					getMessageDes().add(Messages.getString("appraisal.param.required", "accountBalanceMinM6"));

				if (dataDetail.getAccountBalanceMaxM6() == null)
					getMessageDes().add(Messages.getString("appraisal.param.required", "accountBalanceMaxM6"));

				if (dataDetail.getAccountBalanceMinM1() != null && dataDetail.getAccountBalanceMaxM1() != null && dataDetail.getAccountBalanceMinM2() != null && dataDetail.getAccountBalanceMaxM2() != null && 
						dataDetail.getAccountBalanceMinM3() != null && dataDetail.getAccountBalanceMaxM3() != null && dataDetail.getAccountBalanceMinM4() != null && dataDetail.getAccountBalanceMaxM4() != null && 
						dataDetail.getAccountBalanceMinM5() != null && dataDetail.getAccountBalanceMaxM5() != null && dataDetail.getAccountBalanceMinM6() != null && dataDetail.getAccountBalanceMaxM6() != null) {
					Double avg = (dataDetail.getAccountBalanceMinM1() + dataDetail.getAccountBalanceMaxM1() + dataDetail.getAccountBalanceMinM2() + dataDetail.getAccountBalanceMaxM2() + 
							dataDetail.getAccountBalanceMinM3() + dataDetail.getAccountBalanceMaxM3() + dataDetail.getAccountBalanceMinM4() + dataDetail.getAccountBalanceMaxM4() + 
							dataDetail.getAccountBalanceMinM5() + dataDetail.getAccountBalanceMaxM5() + dataDetail.getAccountBalanceMinM6() + dataDetail.getAccountBalanceMaxM6() - 6000000) / 12;
					dataDetail.setAverageAccountBalance(avg);
				}
			}
			
			// Hồ sơ thuộc nhóm SP: CS Insurance: annualFeeLifeInsurance
			if (PRD_GRP_CS_INSURANCE.contains(dataDetail.getProductCode())) {
				if (dataDetail.getAnnualFeeLifeInsurance() == null)
					getMessageDes().add(Messages.getString("appraisal.param.required", "getAnnualFeeLifeInsurance"));
			}
			
			if (AppraisalAggregate.INSTALLMENT_LOAN.equals(dataDetail.getProductGroup())) {
				if (dataDetail.getGoodsInformation() == null || dataDetail.getGoodsInformation().size() < 1)
					getMessageDes().add(Messages.getString("appraisal.param.required", "goodsInformation"));
			}
		}
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
	
}

