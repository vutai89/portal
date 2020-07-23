package com.mcredit.business.customer.validation;

import java.util.Arrays;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.telesale.ContractSearch;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.DateUtil;
import com.mcredit.util.StringUtils;

public class FindContractDuplicateValidation extends AbstractValidation {

	public void validate(ContractSearch contractSearch) throws ValidationException {
		
		if( contractSearch == null )
			getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.telesale.checkDuplicate.contractSearch.input")));
		
		if( StringUtils.isNumberic(contractSearch.getIdentityNumber())
				&& !Arrays.asList(new String[]{"9", "12"}).contains(contractSearch.getIdentityNumber().length() + "") )
			getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.customer.queryInfo.identityNumberInvalid")));
		
		/*if( StringUtils.isNullOrEmpty(contractSearch.getDsaTsaCode()) )
			getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.telesale.checkDuplicate.contractSearch.dsaTsaCode")));*/
		
		if( StringUtils.isNullOrEmpty(contractSearch.getFromDate()) || StringUtils.isNullOrEmpty(contractSearch.getToDate()) )
			getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.telesale.checkDuplicate.contractSearch.dateRange")));
		else {
			if( !DateUtil.validateFormat(contractSearch.getFromDate(), "dd/MM/yyyy") || !DateUtil.validateFormat(contractSearch.getToDate(), "dd/MM/yyyy") )
				getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.customer.queryInfo.dateInvalid")));
			else {
				
				long fromDate = DateUtil.dateToLong("dd/MM/yyyy", contractSearch.getFromDate());
				long toDate = DateUtil.dateToLong("dd/MM/yyyy", contractSearch.getToDate());
				
				//long sixtyDays = 60 * 24 * 60 * 60 * 1000;
				if( fromDate < toDate && (toDate - fromDate) > new Long("5184000000") )
					getMessageDes().add(Messages.getString("label.customer.queryInfo.dateRangeInvalid"));
			}
		}
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
	
	/*public static void main(String[] args) throws ParseException {

		//System.out.println( DateUtil.dateToLong("dd/MM/yyyy", "01/12/2018") );
		
		System.out.println( DateUtil.validateFormat("12/13/2018", "dd/MM/yyyy") );

	    // dd/MM/yyyy
	    // 86400000 1day
	    // 5184000000 60 days
		
    }*/
	
}
