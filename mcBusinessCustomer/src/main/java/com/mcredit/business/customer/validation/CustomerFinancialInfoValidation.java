package com.mcredit.business.customer.validation;

import java.math.BigDecimal;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.dto.card.CustomerFinancialInfoDTO;
import com.mcredit.model.enums.CustomerValidationLength;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class CustomerFinancialInfoValidation extends AbstractValidation {
	
	public void validateFinancialInfo(CustomerFinancialInfoDTO financialInfo) throws ValidationException {
		if( financialInfo!=null ) {
			if(financialInfo.getCustId() != null && financialInfo.getCustId() <= 0)
				getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.custId")));
			else if(String.valueOf(financialInfo.getCustId()).length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ID.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.custId"), CustomerValidationLength.MAX_LEN_CUSTOMER_ID.value()));
			
			if(StringUtils.isNullOrEmpty(financialInfo.getRecordStatus()))
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.customer.accountLink.recordStatus")));
			else if(financialInfo.getRecordStatus().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_TYPE.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.recordStatus"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_TYPE.value()));
			
			if(!StringUtils.validLength(financialInfo.getCreatedBy(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.createdBy"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()));
			
			if(!StringUtils.validLength(financialInfo.getLastUpdatedBy(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.lastUpdatedBy"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()));
			
			if(!StringUtils.validLength(financialInfo.getCreditInOtherBank(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_TYPE.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.financialInfo.creditInOtherBank"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_TYPE.value()));
			
			if(!StringUtils.validLength(financialInfo.getSpouseCreditInOtherBank(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_TYPE.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.financialInfo.spouseCreditInOtherBank"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_TYPE.value()));
			
			if(financialInfo.getS37Data()!=null && financialInfo.getS37Data() <= 0)
				getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.customer.financialInfo.s37Data")));
			
			if(financialInfo.getCustIncome()!=null && financialInfo.getCustIncome().compareTo(BigDecimal.ZERO) <= 0)
				getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.customer.financialInfo.custIncome")));
			else if(String.valueOf(financialInfo.getCustIncome()).length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ID.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.financialInfo.custIncome"), CustomerValidationLength.MAX_LEN_CUSTOMER_ID.value()));
			
			if(financialInfo.getLifeInsuCompanyId()!=null && financialInfo.getLifeInsuCompanyId() <= 0)
				getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.customer.financialInfo.lifeInsuCompanyId")));
			
			if(financialInfo.getInsuTerm()!=null && financialInfo.getInsuTerm() <= 0)
				getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.customer.financialInfo.insuTerm")));
			
			if(financialInfo.getInsuTermFee()!=null && financialInfo.getInsuTermFee().compareTo(BigDecimal.ZERO) <= 0)
				getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.customer.financialInfo.insuTermFee")));
			else if(String.valueOf(financialInfo.getInsuTermFee()).length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ID.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.financialInfo.insuTermFee"), CustomerValidationLength.MAX_LEN_CUSTOMER_ID.value()));
			
			if(!StringUtils.validLength(financialInfo.getInsuTermOther(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_PRODUCT.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.financialInfo.insuTermOther"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_PRODUCT.value()));
			
			if(financialInfo.getPaymentAmountAtBank()!=null && financialInfo.getPaymentAmountAtBank().compareTo(BigDecimal.ZERO) <= 0)
				getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.customer.financialInfo.paymentAmountAtBank")));
			else if(String.valueOf(financialInfo.getPaymentAmountAtBank()).length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ID.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.financialInfo.paymentAmountAtBank"), CustomerValidationLength.MAX_LEN_CUSTOMER_ID.value()));
			
			if(!StringUtils.validLength(financialInfo.getAccountNumberAtBank(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.financialInfo.accountNumberAtBank"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()));
			
			if(!StringUtils.validLength(financialInfo.getBankName(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.financialInfo.bankName"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()));
			
			if(!StringUtils.validLength(financialInfo.getBankBranch(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.financialInfo.bankBranch"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()));
			
			if(financialInfo.getAvgAccountBal()!=null && financialInfo.getAvgAccountBal().compareTo(BigDecimal.ZERO) <= 0)
				getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.customer.financialInfo.avgAccountBal")));
			else if(String.valueOf(financialInfo.getAvgAccountBal()).length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ID.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.financialInfo.avgAccountBal"), CustomerValidationLength.MAX_LEN_CUSTOMER_ID.value()));
			
			if(financialInfo.getAvgElectricBill()!=null && financialInfo.getAvgElectricBill().compareTo(BigDecimal.ZERO) <= 0)
				getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.customer.financialInfo.avgElectricBill")));
			else if(String.valueOf(financialInfo.getAvgElectricBill()).length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ID.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.financialInfo.avgElectricBill"), CustomerValidationLength.MAX_LEN_CUSTOMER_ID.value()));
		}
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
}
