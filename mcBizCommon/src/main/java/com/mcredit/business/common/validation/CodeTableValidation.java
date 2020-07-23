package com.mcredit.business.common.validation;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.dto.CodeTableInput;
import com.mcredit.model.enums.DateFormatTag;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.DateUtil;
import com.mcredit.util.StringUtils;

public class CodeTableValidation extends AbstractValidation {

	public void validateQuerryCodetable(String codeGroup, String category)
			throws Exception {
		if (StringUtils.isNullOrEmpty(codeGroup) && StringUtils.isNullOrEmpty(category))
			getMessageDes().add(Messages.getString("validation.field.mainMessage", Labels.getString("label.query.codeTabel.queryParam")));
		if (!isValid())
			throw new ValidationException(this.buildValidationMessage());

	}
	
	public void validateQuerryCodetable(CodeTableInput category) throws Exception {
		
		if( !StringUtils.isNullOrEmpty(category.getDate()) && 
				!(DateUtil.validateFormat(category.getDate(), DateFormatTag.DATEFORMAT_yyyy_MM_dd_HH_mm_ss.value()) || DateUtil.validateFormat(category.getDate(), DateFormatTag.DATEFORMAT_CODE_TABLE.value())))
			getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.ct.effectDate")));
		
		if ( category==null || category.getCategory()==null || category.getCategory().isEmpty() )
			getMessageDes().add(Messages.getString("validation.field.mainMessage", Labels.getString("label.warehouse.invalid.category")));
		else {
			
			boolean valid = true;
			
			for( String cat : category.getCategory() ) {
				
				if( StringUtils.isNullOrEmpty(cat) ) {
					
					valid = false;
					
					break;
				}
			}
			if( !valid )
				getMessageDes().add(Messages.getString("validation.field.mainMessage", Labels.getString("label.warehouse.invalid.category")));
		}
		
		if (!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
	
	public void validateQuerryCodetable(String category) throws Exception {
		
		if (StringUtils.isNullOrEmpty(category))
			getMessageDes().add(Messages.getString("validation.field.mainMessage", Labels.getString("label.warehouse.invalid.category")));
		
		if (!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}

	public void validateCriteria(CodeTableDTO input) throws Exception {
		
		if(StringUtils.isNullOrEmpty(input.getCategory()))
			getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.common.queryParam.isCategory")));
		
		if (!isValid())
			throw new ValidationException(this.buildValidationMessage());

	}

}
