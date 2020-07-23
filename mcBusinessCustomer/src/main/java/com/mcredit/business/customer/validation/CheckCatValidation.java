package com.mcredit.business.customer.validation;

import com.mcredit.common.Messages;
import com.mcredit.model.dto.check_cat.SearchCompanyDTO;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class CheckCatValidation extends AbstractValidation {

	public void validateTaxNumber(String taxNumber) throws ValidationException {
		if (StringUtils.isNullOrEmpty(taxNumber)) {
			throw new ValidationException(Messages.getString("check_cat.tax_number.required"));
		}
		
		this.validateFormatTaxNumber(taxNumber);
		
	}
	
	public void validateFormatTaxNumber(String taxNumber) throws ValidationException {
		boolean isValidLength = (taxNumber.length() == 10 || taxNumber.length() == 14); 
		if (!isValidLength) {
			throw new ValidationException(Messages.getString("check_cat.tax_number.invalid"));
		}
		
		taxNumber = taxNumber.replaceAll("-", "");
		try {
			Long cTaxNumber = Long.parseLong(taxNumber);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ValidationException(Messages.getString("check_cat.tax_number.invalid"));
		}
	}
	
	public void validateRiskLookup (SearchCompanyDTO searchDTO) throws ValidationException {
		
		if (searchDTO.getPageNumber() == null || searchDTO.getPageNumber() < 1) {
			throw new ValidationException(Messages.getString("mfs.get.pagenumber.required"));
		}
		
		if (searchDTO.getPageSize() == null || searchDTO.getPageSize() < 1) {
			throw new ValidationException(Messages.getString("mfs.get.pagesize.required"));
		}
		
	}
}
