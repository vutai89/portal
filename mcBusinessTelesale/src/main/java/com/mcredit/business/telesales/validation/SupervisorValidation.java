package com.mcredit.business.telesales.validation;

import java.util.List;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.dto.telesales.AllocationCustomerDTO;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;

public class SupervisorValidation extends AbstractValidation {

	public void validateSupervisor(List<AllocationCustomerDTO> lstAllocationCustomerDTO) throws ValidationException {

		if ( lstAllocationCustomerDTO != null ) {
			if ( lstAllocationCustomerDTO==null || lstAllocationCustomerDTO.size()==0 )
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.telesale.supervisor.lstAllocationCustomer")));
		}

		if (!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
}
