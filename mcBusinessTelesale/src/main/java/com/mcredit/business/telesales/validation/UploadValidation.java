package com.mcredit.business.telesales.validation;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.dto.telesales.UploadFileDTO;
import com.mcredit.model.dto.telesales.UploadFileXsellDTO;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class UploadValidation extends AbstractValidation {

	public void validateUpload(UploadFileDTO request) throws ValidationException {
		
		if(request != null) {
			
			if ( StringUtils.isNullOrEmpty(request.getUserFileName()) )
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.telesale.upload.fileName")));
			
			if (request.getFileContent() == null)
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.telesale.upload.file")));
		}
		
		if (!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
	
	public void validateObjectUpload(Long uplDetailId) throws ValidationException {
		
		if ( uplDetailId==null )
			getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.telesale.upload.uplDetailId")));
			
		if (!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
	
	/*public void validateDeleteUpload(Long uplDetailId) throws ValidationException {
		
		if ( uplDetailId==null )
			getMessageDescCollection().add(Messages.getString("validation.field.madatory", Labels.getString("label.telesale.upload.uplDetailId")));
			
		if (!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}*/
 
}
