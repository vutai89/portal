package com.mcredit.alfresco.validate;

import java.util.List;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.data.UnitOfWork;
import com.mcredit.model.enums.ECMSourceEnum;
import com.mcredit.model.object.ecm.InputUploadECM;
import com.mcredit.model.object.mobile.enums.DocumentCodeEnum ;
import com.mcredit.sharedbiz.cache.DocumentsCachManager;
import com.mcredit.sharedbiz.cache.ProductCacheManager;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class DocumentValidation extends AbstractValidation {

		UnitOfWork uok = null;
		private DocumentsCachManager documentCache =  DocumentsCachManager.getInstance();
		private ProductCacheManager productCache = ProductCacheManager.getInstance();
		
	public DocumentValidation(UnitOfWork uok)  {
		this.uok = uok;
	}

	public void validateContractNumForDebtColection(String contractNumber) throws ValidationException { 
		
		if(null == contractNumber || contractNumber.trim().isEmpty() || contractNumber.trim().length() > 20){
			throw new ValidationException(Messages.getString("validation.invalidContractNum"));
		}
	}

	public void validateUploadMetaDataECM(List<InputUploadECM> inputUploadECMLst) throws ValidationException {

		if(inputUploadECMLst  == null || inputUploadECMLst.size() < 1){
			throw new ValidationException(Messages.getString("validation.UploadMetaDataECM"));		
		}
		
	}

	@SuppressWarnings("unused")
	public String validateUploadMetaDataECMElement(InputUploadECM inputUploadECM) {
		DocumentCodeEnum docCode = DocumentCodeEnum.from(inputUploadECM.getDocumentCode());
		ECMSourceEnum ecmEnum = ECMSourceEnum.from(inputUploadECM.getDocumentSource());
		
		if(inputUploadECM == null) {
			getMessageDes().add(Messages.getString("validation.field.madatory"));
		} else {
			// Document code is mandatory
			if(StringUtils.isNullOrEmpty(inputUploadECM.getDocumentCode())){
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.mcredit.common.bpm.documentCode")));
			}
			// Document code is valid
			if(documentCache.getDocumentIdByCode(inputUploadECM.getDocumentCode()) == null) {
				getMessageDes().add(Messages.getString("validation.field.notcorect",Labels.getString("label.mcredit.common.bpm.documentCode")));
			}
			// Document source is mandatory
			if(StringUtils.isNullOrEmpty(inputUploadECM.getDocumentSource())){
				getMessageDes().add(Messages.getString("validation.field.madatory",Labels.getString("label.mcredit.common.bpm.documentSource")));
			}
			// Document source is valid
			if(ecmEnum == null){
				getMessageDes().add(Messages.getString("validation.field.notcorect",Labels.getString("label.mcredit.common.bpm.documentSource")));
			}
			// Conditional mandatory
			if(ECMSourceEnum.CASH_FROM_BPM == ecmEnum 
					|| ECMSourceEnum.INSTALLMENT_FROM_BPM == ecmEnum 
					||ECMSourceEnum.DATA_CENTRALIZE_FROM_BPM == ecmEnum) {
				// for bpm app number
				if(inputUploadECM.getAppNumber() == null) {
					getMessageDes().add(Messages.getString("validation.field.madatory",Labels.getString("label.mcredit.common.bpm.appNumber")));
				}
				// for product code
				if(StringUtils.isNullOrEmpty(inputUploadECM.getProductCode())) {
					getMessageDes().add(Messages.getString("validation.field.madatory",Labels.getString("label.mcredit.common.bpm.productCode")));
				}
				// product code is valid
				if(productCache.findProductByCode(inputUploadECM.getProductCode()) == null) {
					getMessageDes().add(Messages.getString("validation.field.notcorect",Labels.getString("label.mcredit.common.bpm.productCode")));
				}
				// for identity
				if(StringUtils.isNullOrEmpty(inputUploadECM.getCitizenID())) {
					getMessageDes().add(Messages.getString("validation.field.madatory",Labels.getString("label.mcredit.common.bpm.identityID")));
				}
				// for customer name
				if(StringUtils.isNullOrEmpty(inputUploadECM.getCustName())) {
					getMessageDes().add(Messages.getString("validation.field.madatory",Labels.getString("label.mcredit.common.bpm.customerName")));
				}
			}
			// Conditional mandatory for bpm app id
			if((ECMSourceEnum.CASH_FROM_BPM == ecmEnum 
					|| ECMSourceEnum.INSTALLMENT_FROM_BPM == ecmEnum 
					|| ECMSourceEnum.FAS == ecmEnum 
					||ECMSourceEnum.DATA_CENTRALIZE_FROM_BPM == ecmEnum)
				&& StringUtils.isNullOrEmpty(inputUploadECM.getAppId())) {
				getMessageDes().add(Messages.getString("validation.field.madatory",Labels.getString("label.mcredit.common.bpm.appId")));
			}
			// Conditional mandatory for contract/ld number
			if((ECMSourceEnum.COLLECTION == ecmEnum) && StringUtils.isNullOrEmpty(inputUploadECM.getLdNumber())){
				getMessageDes().add(Messages.getString("validation.field.madatory",Labels.getString("label.mcredit.common.bpm.ldNumber")));
			}
			// server file name
			if(StringUtils.isNullOrEmpty(inputUploadECM.getServerFileName())){
				getMessageDes().add(Messages.getString("validation.field.madatory",Labels.getString("label.mcredit.common.bpm.serverFileName")));
			}
			// client file name
			if(StringUtils.isNullOrEmpty(inputUploadECM.getUserFileName())){
				getMessageDes().add(Messages.getString("validation.field.madatory",Labels.getString("label.mcredit.common.bpm.userFileName")));
			}
		
		}
		
		if(!isValid())
			return this.buildValidationMessage() ;
		
		return null;
	}
}
