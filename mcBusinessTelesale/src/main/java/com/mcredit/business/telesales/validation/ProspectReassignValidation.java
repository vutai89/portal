package com.mcredit.business.telesales.validation;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.dto.telesales.ProspectReAssignDTO;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;

public class ProspectReassignValidation extends AbstractValidation {
	
	public void validateProspectReassign(ProspectReAssignDTO dto) throws ValidationException {
		
		if( dto==null || dto.getLstProspectReAssign()==null || dto.getLstProspectReAssign().size()==0 )
			throw new ValidationException(Messages.getString("validation.field.invalidFormat", "ProspectReAssignDTO"));
			
		for( ProspectReAssignDTO input : dto.getLstProspectReAssign() ) {
			if (input.getOldObjectId() == null || input.getOldObjectId() == 0)
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.prospect.reassign.oldObjectId")));
			if (input.getNewObjectId() == null || input.getNewObjectId().isEmpty())
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.prospect.reassign.newObjectId")));
			if (input.getAllocatedNumber() == null || input.getAllocatedNumber() == 0)
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.prospect.reassign.allocatedNumber")));
		}
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
}
