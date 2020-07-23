package com.mcredit.business.credit.validation;

import java.math.BigDecimal;

import com.mcredit.business.credit.dto.CreditApplicationAppraisalDTO;
import com.mcredit.business.credit.dto.CreditApplicationBPMDTO;
import com.mcredit.business.credit.dto.CreditApplicationLoanManagementDTO;
import com.mcredit.business.credit.dto.CreditApplicationRequestDTO;
import com.mcredit.business.credit.dto.CreditDTO;
import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.enums.CommonValidationLength;
import com.mcredit.model.enums.CreditValidationLength;
import com.mcredit.model.enums.CustomerValidationLength;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class CreditApplicationValidation extends AbstractValidation {

	public void validateCreditApplication(CreditDTO item, String updateId) throws Exception {
		if (item != null) {

			if (updateId != null && (!StringUtils.isNumberic(updateId) || Long.parseLong(updateId) <= 0)) {
				getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.common.queryParam.isNumber")));
				throw new ValidationException(this.buildValidationMessage());
			}

			CreditApplicationAppraisalDTO appAppraisal = item.getAppAppraisal();
			CreditApplicationBPMDTO appBPM = item.getAppBPM();
			CreditApplicationLoanManagementDTO appLMN = item.getAppLMN();
			CreditApplicationRequestDTO appRequest = item.getAppRequest();

			if (appRequest != null) {
				if(appRequest.getCustId() == null)
					getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.custId")));
				if(appRequest.getCustId() != null && appRequest.getCustId() <= 0)
					getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.custId")));
				else if(String.valueOf(appRequest.getCustId()).length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ID.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.custId"), CustomerValidationLength.MAX_LEN_CUSTOMER_ID.value()));
			
				if(StringUtils.isNullOrEmpty(appRequest.getRecordStatus()))
					getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.customer.accountLink.recordStatus")));
				else if(appRequest.getRecordStatus().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_TYPE.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.recordStatus"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_TYPE.value()));
				
				if(StringUtils.isNullOrEmpty(appRequest.getCreatedBy()))
					getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.customer.accountLink.createdBy")));
				if(StringUtils.isNullOrEmpty(appRequest.getCreatedBy()) && appRequest.getCreatedBy().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.createdBy"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()));
			
				if(!StringUtils.isNullOrEmpty(appRequest.getLastUpdatedBy()) && appRequest.getLastUpdatedBy().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.lastUpdatedBy"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()));
				
				if(appRequest.getRateIndex() != null && String.valueOf(appRequest.getCustId()).length() > CommonValidationLength.MAX_LEN_RATEINDEX.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appRequest.rateIndex"), CommonValidationLength.MAX_LEN_RATEINDEX.value()));
				
				if(appRequest.getIntRate() == null)
					getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.credit.appRequest.IntRate")));
				
				if(appRequest.getIntRate() != null && appRequest.getIntRate() <= 0)
					getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.credit.appRequest.IntRate")));
				else if(String.valueOf(appRequest.getIntRate()).length() > CommonValidationLength.MAX_LEN_INTRATE.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appRequest.IntRate"), CommonValidationLength.MAX_LEN_INTRATE.value()));
				
				if(appRequest.getLnAmount() == null)
					getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.credit.appRequest.LnAmount")));
				
				if(appRequest.getLnAmount() != null && appRequest.getLnAmount() <= 0)
					getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.credit.appRequest.LnAmount")));
				else if(String.valueOf(appRequest.getLnAmount()).length() > CommonValidationLength.MAX_LEN_LNAMOUNT.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appRequest.LnAmount"), CommonValidationLength.MAX_LEN_LNAMOUNT.value()));

				if(appRequest.getLnPurpose() != null && String.valueOf(appRequest.getLnPurpose()).length() > CommonValidationLength.MAX_LEN_RLNPURPOSE.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appRequest.LnPurpose"), CommonValidationLength.MAX_LEN_RLNPURPOSE.value()));
				
				if(!StringUtils.isNullOrEmpty(appRequest.getLnOtherPurpose()) && appRequest.getLnOtherPurpose().length() > CommonValidationLength.MAX_LEN_LNOTHERPURPOSE.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appRequest.LnOtherPurpose"), CommonValidationLength.MAX_LEN_LNOTHERPURPOSE.value()));
				
				if(appRequest.getLnTenor() == null)
					getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.credit.appRequest.LnTenor")));
				
				if(appRequest.getLnTenor() != null && appRequest.getLnTenor() <= 0)
					getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.credit.appRequest.LnTenor")));
				else if(String.valueOf(appRequest.getLnTenor()).length() > CommonValidationLength.MAX_LEN_LNTENOR.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appRequest.LnTenor"), CommonValidationLength.MAX_LEN_LNTENOR.value()));
				
				if(appRequest.getSaleId() != null && appRequest.getSaleId() <= 0)
					getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.credit.appRequest.SaleId")));
				else if(appRequest.getSaleId() != null && String.valueOf(appRequest.getSaleId()).length() > CommonValidationLength.MAX_LEN_GETSALEID.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appRequest.SaleId"), CommonValidationLength.MAX_LEN_GETSALEID.value()));
				
				if(appRequest.getProductId() == null)
					getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.credit.appRequest.ProductId")));
				
				if(appRequest.getProductId() != null && appRequest.getProductId() <= 0)
					getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.credit.appRequest.ProductId")));
				else if(String.valueOf(appRequest.getProductId()).length() > CommonValidationLength.MAX_LEN_PRODUCTID.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appRequest.ProductId"), CommonValidationLength.MAX_LEN_PRODUCTID.value()));
				
				if(appRequest.getTransOfficeId() != null && appRequest.getTransOfficeId() <= 0)
					getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.credit.appRequest.TransOfficeId")));
				else if(String.valueOf(appRequest.getTransOfficeId()).length() > CommonValidationLength.MAX_LEN_TRANSOFFICEID.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appRequest.TransOfficeId"), CommonValidationLength.MAX_LEN_TRANSOFFICEID.value()));

				if(appRequest.getApplyToObject() != null && appRequest.getApplyToObject() <= 0)
					getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.credit.appRequest.ApplyToObject")));
				else if(String.valueOf(appRequest.getApplyToObject()).length() > CommonValidationLength.MAX_LEN_APPLYTOOBJECT.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appRequest.ApplyToObject"), CommonValidationLength.MAX_LEN_APPLYTOOBJECT.value()));
				
				if(appRequest.getInsuAmount() != null && appRequest.getInsuAmount() <= 0)
					getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.credit.appRequest.InsuAmount")));
				else if(String.valueOf(appRequest.getInsuAmount()).length() > CommonValidationLength.MAX_LEN_INSUAMOUNT.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appRequest.InsuAmount"), CommonValidationLength.MAX_LEN_INSUAMOUNT.value()));

				if(appRequest.getInsuFee() != null && appRequest.getInsuFee() <= 0)
					getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.credit.appRequest.InsuFee")));
				else if(String.valueOf(appRequest.getInsuFee()).length() > CommonValidationLength.MAX_LEN_INSUFEE.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appRequest.InsuFee"), CommonValidationLength.MAX_LEN_INSUFEE.value()));

				if(appRequest.getInsuType() != null && appRequest.getInsuType() <= 0)
					getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.credit.appRequest.InsuType")));
				else if(String.valueOf(appRequest.getInsuType()).length() > CommonValidationLength.MAX_LEN_INSUTYPE.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appRequest.InsuType"), CommonValidationLength.MAX_LEN_INSUTYPE.value()));

				if(appRequest.getInsuRate() != null && appRequest.getInsuRate() <= 0)
					getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.credit.appRequest.InsuRate")));
				else if(String.valueOf(appRequest.getInsuType()).length() > CommonValidationLength.MAX_LEN_INSURATE.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appRequest.InsuRate"), CommonValidationLength.MAX_LEN_INSURATE.value()));
				
				if(!StringUtils.isNullOrEmpty(appRequest.getMcContractNumber()) && appRequest.getMcContractNumber().length() > CommonValidationLength.MAX_LEN_MCCONTRACTNUMBER.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appRequest.McContractNumber"), CommonValidationLength.MAX_LEN_MCCONTRACTNUMBER.value()));
				
				if(!StringUtils.isNullOrEmpty(appRequest.getStatus()) && appRequest.getStatus().length() > CommonValidationLength.MAX_LEN_GETSTATUS.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appRequest.Status"), CommonValidationLength.MAX_LEN_GETSTATUS.value()));
				
				if(appRequest.getDisbursementChannel() != null && appRequest.getDisbursementChannel() <= 0)
					getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.credit.appRequest.DisbursementChannel")));
				else if(String.valueOf(appRequest.getDisbursementChannel()).length() > CommonValidationLength.MAX_LEN_DISBURSEMENTCHANNEL.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appRequest.DisbursementChannel"), CommonValidationLength.MAX_LEN_DISBURSEMENTCHANNEL.value()));
				
				if(appRequest.getDisbursementMethod() != null && appRequest.getDisbursementMethod() <= 0)
					getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.credit.appRequest.DisbursementMethod")));
				else if(String.valueOf(appRequest.getDisbursementMethod()).length() > CommonValidationLength.MAX_LEN_DISBURSEMENTMETHOD.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appRequest.DisbursementMethod"), CommonValidationLength.MAX_LEN_DISBURSEMENTMETHOD.value()));
				
				if(appRequest.getScope() != null && appRequest.getScope() <= 0)
					getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.credit.appRequest.Scope")));
				else if(String.valueOf(appRequest.getScope()).length() > CommonValidationLength.MAX_LEN_SCOPE.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appRequest.Scope"), CommonValidationLength.MAX_LEN_SCOPE.value()));
			}
			
			if (appAppraisal != null) {
				if(StringUtils.isNullOrEmpty(appAppraisal.getCreatedBy()) && appAppraisal.getCreatedBy().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.createdBy"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()));
				
				if(StringUtils.isNullOrEmpty(appAppraisal.getRecordStatus()) && appAppraisal.getRecordStatus().length() > CreditValidationLength.MAX_LEN_MESSAGELOG_MSGTYPE.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appAppraisal.RecordStatus"), CreditValidationLength.MAX_LEN_MESSAGELOG_MSGTYPE.value()));

				if(appAppraisal.getApprovedAmount() != null && (BigDecimal.ZERO.compareTo(appAppraisal.getApprovedAmount()) > 0))
					getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.credit.appAppraisal.ApprovedAmount")));
				else if(String.valueOf(appAppraisal.getApprovedAmount()).length() > CommonValidationLength.MAX_LEN_LNAMOUNT.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appAppraisal.ApprovedAmount"), CommonValidationLength.MAX_LEN_LNAMOUNT.value()));
				
				if(appAppraisal.getApprovedDate()==null)
					getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.credit.appAppraisal.ApprovedDate")));
				
				if(appAppraisal.getApprovedUser()==null)
					getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.credit.appAppraisal.ApprovedUser")));
				
			}

			if (appBPM != null) {
				if(StringUtils.isNullOrEmpty(appBPM.getCreatedBy()) && appBPM.getCreatedBy().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.createdBy"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()));
				
				if(StringUtils.isNullOrEmpty(appBPM.getRecordStatus()) && appBPM.getRecordStatus().length() > CreditValidationLength.MAX_LEN_MESSAGELOG_MSGTYPE.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appAppraisal.RecordStatus"), CreditValidationLength.MAX_LEN_MESSAGELOG_MSGTYPE.value()));

				if(StringUtils.isNullOrEmpty(appBPM.getBpmAppId()) || appBPM.getBpmAppId().length() > CommonValidationLength.LEN_BPM_APP_ID.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appBPM.bpmAppId"), CommonValidationLength.LEN_BPM_APP_ID.value()));
				
			}
			

		
		if(appLMN!= null){
			
			if(appLMN.getRecordStatus() == null)
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.appLMS.RecordStatus")));
			
			if(!StringUtils.isNullOrEmpty(appLMN.getRecordStatus()) && appLMN.getRecordStatus().length() > CommonValidationLength.MAX_LEN_RECORDSTATUS.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appLMS.RecordStatus"), CommonValidationLength.MAX_LEN_RECORDSTATUS.value()));
			
			
			if(StringUtils.isNullOrEmpty(appLMN.getCreatedBy()) && appLMN.getCreatedBy().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.createdBy"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()));
			
			if(!StringUtils.isNullOrEmpty(appLMN.getLastUpdatedBy()) && appLMN.getLastUpdatedBy().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.lastUpdatedBy"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()));
			
			if(!StringUtils.isNullOrEmpty(appLMN.getCoreLnAppId()) && appLMN.getCoreLnAppId().length() > CommonValidationLength.MAX_LEN_APPLMS_CORELNAPPID.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appLMS.CoreLnAppId"), CommonValidationLength.MAX_LEN_APPLMS_CORELNAPPID.value()));
			
			if(appLMN.getDisbursementStatus() != null && appLMN.getDisbursementStatus() <= 0)
				getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.credit.appRequest.DisbursementStatus")));
			else if(String.valueOf(appLMN.getDisbursementStatus()).length() > CommonValidationLength.MAX_LEN_DISBURSEMENTSTATUS.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appRequest.DisbursementStatus"), CommonValidationLength.MAX_LEN_DISBURSEMENTSTATUS.value()));
			
			if(appLMN.getDisbursementAmount() != null && appLMN.getDisbursementAmount() <= 0)
				getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.credit.appRequest.DisbursementAmount")));
			else if(String.valueOf(appLMN.getDisbursementAmount()).length() > CommonValidationLength.MAX_LEN_DISBURSEMENTAMOUNT.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.credit.appRequest.DisbursementAmount"), CommonValidationLength.MAX_LEN_DISBURSEMENTAMOUNT.value()));
		}

		if (!isValid())
			throw new ValidationException(this.buildValidationMessage());

		}
	}
}