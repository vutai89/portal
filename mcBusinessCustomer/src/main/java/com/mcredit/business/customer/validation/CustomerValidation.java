package com.mcredit.business.customer.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


import com.mcredit.business.customer.CustomerFactory;
import com.mcredit.business.customer.dto.CustomerDTO;
import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.dto.check_cat.CompInsertDTO;
import com.mcredit.model.dto.check_cat.CompanyDTO;
import com.mcredit.model.enums.CTCat;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class CustomerValidation extends AbstractValidation {
	
	private static final String SPACE = " ";
	
	public void validateCustomer(CustomerDTO item, String updateId) throws ValidationException {
		
		if( item!=null ) {
			
			if(updateId!=null && (!StringUtils.isNumberic(updateId) || Long.parseLong(updateId) <= 0)) {
				getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.common.queryParam.isNumber")));
				throw new ValidationException(this.buildValidationMessage());
			}
			
			CustomerFactory.customerValidation(item, updateId);
		}
	}
	
	public void validateCustomerAccountLink(CustomerDTO item, String updateId) throws Exception {
		
		if( item!=null )
			CustomerFactory.customerAccountLinkValidation(item, updateId);
	}
	
	/*public void validateCoreCustCode(CustomerPersonalInfoDTO item, Long updateId) throws Exception {
		
		if( item!=null )
			CustomerFactory.updateCoreCustCodeValidation(item, updateId);
	}*/
	
}
