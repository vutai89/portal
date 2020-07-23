package com.mcredit.business.telesales.validation;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.dto.telesales.CallResultDTO;
import com.mcredit.model.dto.telesales.CustProspectDTO;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.DateUtil;
import com.mcredit.util.StringUtils;

public class ProspectManagerValidation extends AbstractValidation {

	private String dateFormat = "dd/MM/yyyy";
	
	public CodeTableDTO validateInsertProspectCall(CallResultDTO request) throws ValidationException {
		
		if(request == null)
			throw new ValidationException(Messages.getString("validation.field.invalidFormat", "request"));
		
		if (!StringUtils.isNullOrEmpty(request.getNextActionDate()) && !DateUtil.validateFormat(request.getNextActionDate(), this.dateFormat))
			getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.telesale.nextActionDate.inCorrect")));
				
		if(StringUtils.isNullOrEmpty(request.getCallResult()))
			getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.telesale.callResult.callResult")));
		
		if(StringUtils.isNullOrEmpty(request.getCallStatus()))
			getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.telesale.callResult.callStatus")));
		
		/*if(StringUtils.isNullOrEmpty(request.getNextAction()))
			getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.telesale.callResult.callAction")));*/
		
		CodeTableDTO callStatus = CacheManager.CodeTable().getCodeByCategoryCodeValue1("CALL_STAT", request.getCallStatus());
		
		if(callStatus == null)
			throw new ValidationException(Messages.getString("validation.field.invalidFormat", "Call Status"));
		
		CodeTableDTO callResult = CacheManager.CodeTable().getCodeByCategoryCodeValue1("CALL_RSLT", request.getCallResult());
		if(callResult == null)
			throw new ValidationException(Messages.getString("validation.field.invalidFormat", "Call Result"));
		
		/*CodeTableDTO nextAction = CacheManager.CodeTable().getCodeByCategoryCodeValue1("NEXT_ACT", request.getNextAction());
		if(nextAction == null)
			throw new ValidationException(Messages.getString("validation.field.invalidFormat", "Next Action"));*/
		
		request.setCallResult(callResult.getId().toString());
		request.setCallStatus(callStatus.getId().toString());
		//request.setNextAction(nextAction.getId().toString());
		
		if (!isValid())
			throw new ValidationException(this.buildValidationMessage());
		
		return null;
	}
	
	public void validateUpsertCustomerProspect(CustProspectDTO request) throws ValidationException {
		
		if(request != null) {
			
			if (request.getUplCustomerId() == null)
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.telesale.custProspect.uplCustomerId")));
		}
		
		if (!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
}
