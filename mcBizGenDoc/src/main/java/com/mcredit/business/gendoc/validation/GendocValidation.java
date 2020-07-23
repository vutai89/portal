/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcredit.business.gendoc.validation;

import com.mcredit.common.Messages;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;
import java.text.ParseException;


public class GendocValidation extends AbstractValidation {
    
    public void gendocFile(String appID, String typeOfDocument, String typeOfLoans, String userName, String appNum) throws ValidationException, ParseException {
		if(StringUtils.isNullOrEmpty(appID)){
                    getMessageDes().add(Messages.getString("validation.field.madatory", "appID"));	
                }
		if(StringUtils.isNullOrEmpty(typeOfDocument)){
                    getMessageDes().add(Messages.getString("validation.field.madatory", "typeOfDocument"));
                }
		if(StringUtils.isNullOrEmpty(typeOfLoans)){
                    getMessageDes().add(Messages.getString("validation.field.madatory", "typeOfLoans"));
                }
		if(StringUtils.isNullOrEmpty(userName)){
                    getMessageDes().add(Messages.getString("validation.field.madatory", "userName"));
                }
		if(StringUtils.isNullOrEmpty(appNum)){
                    getMessageDes().add(Messages.getString("validation.field.madatory", "appNum"));
                }		
		if (!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
    
}
