package com.mcredit.business.checkcat.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.dto.check_cat.CompInsertDTO;
import com.mcredit.model.dto.check_cat.CompanyDTO;
import com.mcredit.model.dto.check_cat.SearchCompanyDTO;
import com.mcredit.model.enums.CTCat;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class CheckCatValidation {
	private static final String SPACE = " ";
	
	public void validateRemoveComp(String lstTaxNumber) throws ValidationException {
		if(StringUtils.isNullOrEmpty(lstTaxNumber))
			throw new ValidationException(Messages.getString("check.cat.list.remove.null"));
	}
	
	public void validateUpdateTopComp(List<String> lstRemove, List<CompInsertDTO> lstInsert, String ctcCat) throws ValidationException {
		if(StringUtils.isNullOrEmpty(ctcCat))
			throw new ValidationException(Messages.getString("check.cat.list.update"));
		if((lstRemove == null || lstRemove.size() == 0) && (lstInsert== null || lstInsert.size() == 0))
			throw new ValidationException(Messages.getString("check.cat.list.remove.null"));
	}
	
	public void validateCheckCompType(List<CompanyDTO> lstComp) throws ValidationException {
		if(lstComp == null || lstComp.size() == 0)
			throw new ValidationException(Messages.getString("check.cat.list.comp"));
	}
	
	public Boolean validateCheckCompType(CompanyDTO dto, List<String> lstErr, int index) {
		
		//Validate TaxNumber
		if(StringUtils.isNullOrEmpty(dto.getCompTaxNumber())) {
			lstErr.add(Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.taxnumber.null"));
			return false;
		} else {
			String taxNumber = dto.getCompTaxNumber();
			boolean isValidLength = (dto.getCompTaxNumber().length() == 10 || dto.getCompTaxNumber().length() == 14); 
			if (!isValidLength) {
				lstErr.add(Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.taxnumber.err"));
				return false;
			}
			taxNumber = dto.getCompTaxNumber().replaceAll("-", "");
			if((taxNumber.length()!=10 && taxNumber.length()!=13) || !StringUtils.isNumeric(taxNumber)) {
				lstErr.add(Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.taxnumber.err"));
				return false;
			}
		}
		
		if(StringUtils.isNullOrEmpty(dto.getCompName())) {
			lstErr.add(Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.comp.name.null"));
			return false;
		} else if(StringUtils.isNullOrEmpty(dto.getCompAddrStreet())) {
			lstErr.add(Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.comp.address.null"));
			return false;
		} else if(StringUtils.isNullOrEmpty(dto.getTaxChapter())) {
			lstErr.add(Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.comp.tax.chapter.null"));
			return false;
		} else if(StringUtils.isNullOrEmpty(dto.getCicInfo())) {
			lstErr.add(Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.comp.cic.null"));
			return false;
		} else if(StringUtils.isNullOrEmpty(dto.getEconomicType())) {
			lstErr.add(Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.comp.eco.type.null"));
			return false;
		} 
		
		//Validate Economic Type
		ArrayList<String> lstEcotype = new ArrayList<String>();
		List<CodeTableDTO> lstCodeTableEcoType = CodeTableCacheManager.getInstance().getCodeByCategory(CTCat.ECONOMIC_TYPE.value());
		for (CodeTableDTO eType : lstCodeTableEcoType) {
			lstEcotype.add(eType.getCodeValue1());
		}
		if (!lstEcotype.contains(dto.getEconomicType().trim())) {
			lstErr.add(Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.comp.eco.type.err"));
			return false;
		}

		// Validate CIC
		ArrayList<String> lstCicInfor = new ArrayList<String>();
		List<CodeTableDTO> lstCodeTable = CodeTableCacheManager.getInstance().getCodeByCategory(CTCat.CORP_CIC.value());
		for (CodeTableDTO ctItem : lstCodeTable) 
			lstCicInfor.add(ctItem.getCodeValue1());
		if (!lstCicInfor.contains(dto.getCicInfo())) {
			lstErr.add(Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.comp.cic.err"));
			return false;
		}
		
		//Validate RecordStatus
		if(StringUtils.isNullOrEmpty(dto.getRecordStatus())) {
			lstErr.add(Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.status.null"));
			return false;
		} else {
			if(!dto.getRecordStatus().equals("0") && !dto.getRecordStatus().equals("1")) {
				lstErr.add(Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.status.err"));
				return false;
			}
		}
		
		// Validate EstablishDate
		if (StringUtils.isNullOrEmpty(dto.getEstablishDate())) {
			lstErr.add(Labels.getString("lable.cat.tool.line") + index + SPACE
					+ Messages.getString("check.cat.comp.esb.null"));
			return false;
		} else {
			try {
				SimpleDateFormat _sdf = new SimpleDateFormat("dd/MM/yyyy");
				_sdf.parse(dto.getEstablishDate());
			} catch (ParseException e) {
				lstErr.add(Labels.getString("lable.cat.tool.line") + index + SPACE
						+ Messages.getString("check.cat.comp.esb.error"));
				return false;
			}
		}
		
		return true;
	}
	
	public void validateCheckCat(List<CompanyDTO> lstComp) throws ValidationException{
		if(lstComp == null || lstComp.size() == 0)
			throw new ValidationException(Messages.getString("check.cat.list.comp"));
		for (CompanyDTO compItem : lstComp) {
			if(compItem.getCompType().equals("MANUAL"))
				throw new ValidationException(Messages.getString("check.cat.comp.manual"));
		}
	}
	
	public Boolean validateTaxNumberFormat(CompInsertDTO item, List<String> lstInsertErrors, int index) {
		if(StringUtils.isNullOrEmpty(item.getTaxNumber())) {
			lstInsertErrors.add(Labels.getString("lable.cat.sheet.insert") + Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.taxnumber.null"));
			return false;
		} else {
			boolean isValidLength = (item.getTaxNumber().length() == 10 || item.getTaxNumber().length() == 14); 
			if (!isValidLength) {
				lstInsertErrors.add(Labels.getString("lable.cat.sheet.insert") + Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.taxnumber.err.insert"));
				return false;
			}
			String taxNumber = item.getTaxNumber().replaceAll("-", "");
			boolean isValid = (taxNumber.length() == 10 || taxNumber.length() == 13); 
			if(!StringUtils.isNumeric(taxNumber)) {
				lstInsertErrors.add(Labels.getString("lable.cat.sheet.insert") + Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.taxnumber.err.insert"));
				return false;
			}
			if(!isValid) {
				lstInsertErrors.add(Labels.getString("lable.cat.sheet.insert") + Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.taxnumber.err.insert"));
				return false;
			}
		}
		if(StringUtils.isNullOrEmpty(item.getCompName())) {
			lstInsertErrors.add(Labels.getString("lable.cat.sheet.insert") + Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.compname.null"));
			return false;
		}
		return true;
	}
	
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
		
		if (searchDTO.getTaxNumbers().size() > 50) {
			throw new ValidationException(Messages.getString("mfs.get.taxnumber.maxelementnumber"));
		}
		
		if (searchDTO.getPageNumber() == null || searchDTO.getPageNumber() < 1) {
			throw new ValidationException(Messages.getString("mfs.get.pagenumber.required"));
		}
		
		if (searchDTO.getPageSize() == null || searchDTO.getPageSize() < 1) {
			throw new ValidationException(Messages.getString("mfs.get.pagesize.required"));
		}
		
	}

}
