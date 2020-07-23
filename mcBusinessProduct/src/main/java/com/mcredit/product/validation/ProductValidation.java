package com.mcredit.product.validation;

import java.io.File;
import java.util.List;

import com.mcredit.common.Messages;
import com.mcredit.model.dto.product.CommodityDTO;
import com.mcredit.model.enums.product.ProductCategory;
import com.mcredit.product.dto.CommonDTO;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class ProductValidation extends AbstractValidation {
	public void validateString(String payload) throws ValidationException {
		if (StringUtils.isNullOrEmpty(payload)) {
			throw new ValidationException(Messages.getString("String is not null or empty"));
		}
	}
	
	public void validateUpdateSchemeGroup(CommonDTO item) throws ValidationException {
		if (StringUtils.isNullOrEmpty(item.getsValue1())) {
			throw new ValidationException(Messages.getString("Channel Sale is null or empty"));
		}
		if (item.getiValue1()==null) {
			throw new ValidationException(Messages.getString("Product Id is null or empty"));
		}
		if (item.getiValue2()==null) {
			throw new ValidationException(Messages.getString("Product group id is null or empty"));
		}
	}
	
	public void validateInsertCommodity(CommodityDTO commodityObj) throws ValidationException {
		if (StringUtils.isNullOrEmpty(commodityObj.getObjectType())) {
			throw new ValidationException(Messages.getString("No new categories have been selected"));
		} else {
			if(commodityObj.getObjectType().equals(ProductCategory.Commodity.value())) {
				if (StringUtils.isNullOrEmpty(commodityObj.getCommodityCode())) 
					throw new ValidationException(Messages.getString("Good code is null or empty"));
				if (StringUtils.isNullOrEmpty(commodityObj.getCommodityName())) 
					throw new ValidationException(Messages.getString("Good name is null or empty"));
			} else if(commodityObj.getObjectType().equals(ProductCategory.Brand.value())) {
				if (StringUtils.isNullOrEmpty(commodityObj.getBrandCode())) 
					throw new ValidationException(Messages.getString("Brand code is null or empty"));
				if (StringUtils.isNullOrEmpty(commodityObj.getBrandName())) 
					throw new ValidationException(Messages.getString("Brand name is null or empty"));
			} else if(commodityObj.getObjectType().equals(ProductCategory.Model.value())) {
				if (StringUtils.isNullOrEmpty(commodityObj.getModelCode())) 
					throw new ValidationException(Messages.getString("Model code is null or empty"));
				if (StringUtils.isNullOrEmpty(commodityObj.getModelName())) 
					throw new ValidationException(Messages.getString("Model name is null or empty"));
			}
		}
	}
	
	public void validateInsertProductConfig(List<CommodityDTO> lstCommodityObj) throws ValidationException {
		for(CommodityDTO item : lstCommodityObj) {
			if (item.getProductId() == null || item.getCommodityId() == null || item.getBrandId() == null) {
				throw new ValidationException(Messages.getString("Input is null or empty"));
			}
		}
	}
	
	public void validateInsertLstCommodity(File fileUpload, String payload) throws ValidationException {
		if (fileUpload == null || StringUtils.isNullOrEmpty(payload)) {
			throw new ValidationException(Messages.getString("Input is null or empty"));
		}
	}
	
	public void validateDeleteProductGroup(CommonDTO item) throws ValidationException {
		if (item.getiValue1() == null) {
			throw new ValidationException(Messages.getString("Input is null or empty"));
		}
	}
}
