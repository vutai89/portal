package com.mcredit.business.telesales.validation;

import java.text.ParseException;

import com.mcredit.common.Messages;
import com.mcredit.data.telesale.entity.CustProspect;
import com.mcredit.model.dto.telesales.CustProspectDTO;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.DateUtil;
import com.mcredit.util.StringUtils;

public class CustProspectValidation extends AbstractValidation {
	
	public CustProspect validateCustProspect(CustProspectDTO CPDTO) throws ValidationException, ParseException {
		
		if (CPDTO == null) 
			throw new ValidationException(Messages.getString("validation.field.invalidFormat", "Customer Prospect"));
		
		if (CPDTO.getRecordStatus() != null && CPDTO.getRecordStatus().length()!=1 ) 
			getMessageDes().add(Messages.getString("validation.field.invalidFormat", Messages.getString("label.customer.accountLink.recordStatus")));
		
		if (CPDTO.getRecordStatus() == null) 
			CPDTO.setRecordStatus("A");
		
		if(!StringUtils.isNullOrEmpty(CPDTO.getBirthDate()) && !DateUtil.validateFormat(CPDTO.getBirthDate(), "dd/MM/yyyy"))
			getMessageDes().add(Messages.getString("validation.field.mainMessage", Messages.getString("ts.invalid.format.birthDate.specify")));
		
		if (!isValid()) throw new ValidationException(this.buildValidationMessage());
		
		CustProspect CP = new CustProspect();
		CP.setRecordStatus(CPDTO.getRecordStatus());
		CP.setUplCustomerId(CPDTO.getUplCustomerId());
		CP.setId(CPDTO.getId());
		CP.setAllocationDetailId(CPDTO.getAllocationDetailId());
		CP.setBirthDate(StringUtils.isNullOrEmpty(CPDTO.getBirthDate()) ? null : DateUtil.toDate(CPDTO.getBirthDate(), "dd/MM/yyyy"));
		CP.setPermanentAddr(CPDTO.getPermanentAddr());
		CP.setCompAddrStreet(CPDTO.getCompAddrStreet());
		CP.setCustIncome(CPDTO.getCustIncome());
		CP.setNote(CPDTO.getNote());
		CP.setCustName(CPDTO.getCustName());
		CP.setIdentityNumber(CPDTO.getIdentityNumber());
		CP.setMobile(CPDTO.getMobile());
		CP.setNewMobile(CPDTO.getNewMobile());
		
		return CP;
	}
}
