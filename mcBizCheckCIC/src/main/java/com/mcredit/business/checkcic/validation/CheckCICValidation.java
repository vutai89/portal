package com.mcredit.business.checkcic.validation;

import java.io.InputStream;

import com.mcredit.common.Messages;
import com.mcredit.model.dto.cic.CICDetailDTO;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

public class CheckCICValidation extends AbstractValidation {
	
	public static final String REGEX_IDENTITY = "(?<!\\d)\\d{9}|\\d{12}(?!\\d)";		// regex cmnd/cccd
	public static final String REGEX_ALPHA_NUMBERIC = "^[a-zA-Z0-9]+$";
	
	public CheckCICValidation() {
	}
	
	/**
	 * validate request check cic
	 * @author catld.ho
	 * @param citizenID : cmnd
	 * @param oldCitizenID : cmnd cu
	 * @param militaryID : cmqd
	 * @throws ValidationException
	 */
	public void checkCIC(String citizenID, String oldCitizenID, String militaryID, String customerName) throws ValidationException {
		if (StringUtils.isNullOrEmpty(citizenID))
			getMessageDes().add(Messages.getString("checkcic.param.required", "citizenID"));
		else if (!citizenID.matches(REGEX_IDENTITY))
			getMessageDes().add(Messages.getString("checkcic.param.formatInvalid", "citizenID"));
		
		if (!StringUtils.isNullOrEmpty(oldCitizenID) && !oldCitizenID.matches(REGEX_IDENTITY))
			getMessageDes().add(Messages.getString("checkcic.param.formatInvalid", "oldCitizenID"));
		
		if (!StringUtils.isNullOrEmpty(militaryID) && (militaryID.length() > 15 || !militaryID.matches(REGEX_ALPHA_NUMBERIC)))
			getMessageDes().add(Messages.getString("checkcic.param.formatInvalid", "militaryID"));
		
		if (StringUtils.isNullOrEmpty(customerName))
			getMessageDes().add(Messages.getString("checkcic.param.required", "customerName"));
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
	
//	public void searchCIC(String citizenID) throws ValidationException {
//		if (StringUtils.isNullOrEmpty(citizenID))
//			getMessageDes().add(Messages.getString("checkcic.param.required", "citizenID"));
//		else if (!citizenID.matches(REGEX_IDENTITY))
//			getMessageDes().add(Messages.getString("checkcic.param.formatInvalid", "citizenID"));
//		
//		if(!isValid())
//			throw new ValidationException(this.buildValidationMessage());
//	}
	
	/**
	 * validate request update cic result
	 * @author catld.ho
	 * @param fileContent : file chup ket qua cic
	 * @param payload : noi dung ket qua cic
	 * @return CICDetailDTO
	 * @throws ValidationException
	 */
	public CICDetailDTO updateCIC(InputStream fileContent, String payload) throws ValidationException {
		CICDetailDTO dataDetail = null;
		if (StringUtils.isNullOrEmpty(payload) || null == fileContent) {
			getMessageDes().add(Messages.getString("checkcic.payload.required"));
		} else {
			dataDetail = JSONConverter.toObject(payload, CICDetailDTO.class);
			if (dataDetail == null) {
				getMessageDes().add(Messages.getString("checkcic.payload.formatInvalid"));
			} else {
				if (dataDetail.getRequestId() == null)
					getMessageDes().add(Messages.getString("checkcic.param.required", "requestId"));
				
//				if (StringUtils.isNullOrEmpty(dataDetail.getIdentifier()))
//					getMessageDes().add(Messages.getString("checkcic.param.required", "identifier"));
//				else if (!dataDetail.getIdentifier().matches(REGEX_IDENTITY))
//					getMessageDes().add(Messages.getString("checkcic.param.formatInvalid", "identifier"));
				
				if (StringUtils.isNullOrEmpty(dataDetail.getCicImageLink()))
					getMessageDes().add(Messages.getString("checkcic.param.required", "cicImageLink"));
				
				if (StringUtils.isNullOrEmpty(dataDetail.getCustomerName()))
					getMessageDes().add(Messages.getString("checkcic.param.required", "customerName"));
				
				if (StringUtils.isNullOrEmpty(dataDetail.getCicResult()))
					getMessageDes().add(Messages.getString("checkcic.param.required", "cicResult"));
			}
		}
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
		
		return dataDetail;
	}
	
	/**
	 * validate request download file cic result
	 * @author catld.ho
	 * @param identity : cmnd/cccd/cmqd
	 * @throws ValidationException
	 */
	public void downloadCICResult(String identity) throws ValidationException {
		if (StringUtils.isNullOrEmpty(identity))
			getMessageDes().add(Messages.getString("checkcic.param.required", "identity"));
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
	
	public void reportCICResult(String citizenID) throws ValidationException {
		if (StringUtils.isNullOrEmpty(citizenID))
			getMessageDes().add(Messages.getString("checkcic.param.required", "citizenID"));
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
	
}

