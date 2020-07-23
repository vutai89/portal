package com.mcredit.business.telesales.validation;

import java.util.Arrays;

import com.mcredit.business.telesales.payload.ScoringDTO;
import com.mcredit.business.telesales.payload.ScoringCRM;
import com.mcredit.business.telesales.payload.ScoringPayload;
import com.mcredit.business.telesales.payload.SendOTP;
import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.dto.telesales.UploadCaseDTO;
import com.mcredit.model.enums.CustomerValidationLength;
import com.mcredit.model.enums.VendorCodeEnum;
import com.mcredit.model.leadgen.LeadDTO;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class TelesaleValidation extends AbstractValidation {
	
	public static final String REGEX_IDENTITY = "(?<!\\d)\\d{9}|\\d{12}(?!\\d)";		// regex cmnd/cccd
	
	public void validateSendOTP(SendOTP object) throws ValidationException {
		if(StringUtils.isNullOrEmpty(object.getPrimaryPhone())) {
			throw new ValidationException("Thieu primaryPhone");
		}
		if(StringUtils.isNullOrEmpty(object.getNationalId())) {
			throw new ValidationException("Thieu nationalId");
		}
		
		// Trim
		object.setPrimaryPhone(object.getPrimaryPhone().trim());
		object.setNationalId(object.getNationalId().trim());
		
		if(StringUtils.isNullOrEmpty(object.getAppNumber())) {
			throw new ValidationException("Thieu appNumber");
		}
	}
	
	public void validateSendESB(ScoringPayload object) throws ValidationException {
		if(StringUtils.isNullOrEmpty(object.getVerificationCode())){
			throw new ValidationException(Messages.getString("Chua nhap so OTP"));
		}
	}
	
	public void validateSendTs(ScoringPayload object) throws ValidationException {
		if(StringUtils.isNullOrEmpty(object.getApiRequestId())){
			throw new ValidationException(Messages.getString("Khong ton tai api request"));
		}
	}
	
	public void validateScoringTsApi(String mobile, String identityNumber, String appNumber) throws ValidationException {
		if(StringUtils.isNullOrEmpty(mobile) || StringUtils.isNullOrEmpty(identityNumber) || StringUtils.isNullOrEmpty(appNumber)){
			throw new ValidationException(Messages.getString("S\u1ED1 \u0111i\u1EC7n tho\u1EA1i, CMT, appNumber kh\u00F4ng \u0111\u01B0\u1EE3c \u0111\u1EC3 tr\u1ED1ng !"));
		}
	}
	
	public void createCaseFromCRM(UploadCaseDTO newCase, UserDTO _user) throws ValidationException {
		if (null == newCase) {
			getMessageDes().add("Payload can't null");
			throw new ValidationException(this.buildValidationMessage());
		}
		
		if (StringUtils.isNullOrEmpty(newCase.getCustomerName()))
			getMessageDes().add(Messages.getString("checkcic.param.required", "customerName"));
		
		if (StringUtils.isNullOrEmpty(newCase.getProductCode()))
			getMessageDes().add(Messages.getString("checkcic.param.required", "productCode"));
		
		if (StringUtils.isNullOrEmpty(newCase.getCitizenId()))
			getMessageDes().add(Messages.getString("checkcic.param.required", "citizenID"));
		else if (!newCase.getCitizenId().matches(REGEX_IDENTITY))
			getMessageDes().add(Messages.getString("checkcic.param.formatInvalid", "citizenID"));
		
		if (StringUtils.isNullOrEmpty(newCase.getIssueDateCitizen()))
			getMessageDes().add(Messages.getString("checkcic.param.required", "issueDateCitizen"));
		
		if (StringUtils.isNullOrEmpty(newCase.getTempResidence()))
			getMessageDes().add(Messages.getString("checkcic.param.required", "tempResidence"));
		
		if (null == newCase.getLoanTenor())
			getMessageDes().add(Messages.getString("checkcic.param.required", "loanTenor"));
		
		if (null == newCase.getLoanAmount())
			getMessageDes().add(Messages.getString("checkcic.param.required", "loanAmount"));
		
		if (null == newCase.getHasInsurance())
			getMessageDes().add(Messages.getString("checkcic.param.required", "hasInsurance"));
		
		if (StringUtils.isNullOrEmpty(newCase.getShopCode()))
			getMessageDes().add(Messages.getString("checkcic.param.required", "shopCode"));
		
		if (StringUtils.isNullOrEmpty(newCase.getMobileTSA()))
			getMessageDes().add(Messages.getString("checkcic.param.required", "mobileTSA"));
		else if (newCase.getMobileTSA().length() > 10 || !newCase.getMobileTSA().startsWith("0"))
			getMessageDes().add(Messages.getString("checkcic.param.formatInvalid", "mobileTSA"));
		
		if (StringUtils.isNullOrEmpty(newCase.getCustomerMobile()))
			getMessageDes().add(Messages.getString("checkcic.param.required", "customerMobile"));
		
		if (null == newCase.getUplCustomerId())
			getMessageDes().add(Messages.getString("checkcic.param.required", "uplCustomerId"));
		
		if (StringUtils.isNullOrEmpty(_user.getEmpCode()))
			getMessageDes().add("Sale code not found " +  _user.getLoginId());
		
		if (StringUtils.isNullOrEmpty(_user.getEmpId()))
			getMessageDes().add("Sale id not found " +  _user.getLoginId());
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
	
	public void getProductInfo(String productCode) throws ValidationException {
		if (StringUtils.isNullOrEmpty(productCode))
			getMessageDes().add(Messages.getString("checkcic.param.required", "productCode"));
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
	
	public void validateScoringCRM(ScoringDTO object) throws ValidationException {
		if(StringUtils.isNullOrEmpty(object.getAppNumber())){
			throw new ValidationException("Thieu appNumber");
		}
		if(StringUtils.isNullOrEmpty(object.getPrimaryPhone())) {
			throw new ValidationException("Thieu primaryPhone");
		}
		if(StringUtils.isNullOrEmpty(object.getNationalId())) {
			throw new ValidationException("Thieu nationalId");
		}
		if(StringUtils.isNullOrEmpty(object.getVerificationCode())) {
			throw new ValidationException("Thieu verificationCode");
		}
		
		// Trim
		object.setPrimaryPhone(object.getPrimaryPhone().trim());
		object.setNationalId(object.getNationalId().trim());
		
	}
	
	public void validateScoringBPM(ScoringDTO object) throws ValidationException {
		
		if(StringUtils.isNullOrEmpty(object.getPrimaryPhone())) {
			throw new ValidationException("Thieu primaryPhone");
		}
		
		if(StringUtils.isNullOrEmpty(object.getNationalId())) {
			throw new ValidationException("Thieu nationalId");
		}
		
		if(StringUtils.isNullOrEmpty(object.getAppNumber())) {
			throw new ValidationException("Thieu appNumber");
		}
		
		// Trim string identity + phone number
		object.setPrimaryPhone(object.getPrimaryPhone().trim());
		object.setNationalId(object.getNationalId().trim());
		if(!StringUtils.isNullOrEmpty(object.getNationalIdOld())) {
			object.setNationalIdOld(object.getNationalIdOld().trim());
		}
		
		// OTP not null
		if(!StringUtils.isNullOrEmpty(object.getVerificationCode())) {
			if(StringUtils.isNullOrEmpty(object.getVendorCode())) {
				throw new ValidationException("Thieu vendorCode");
			}
			
			if(StringUtils.isNullOrEmpty(object.getNationalId())) {
				throw new ValidationException("Thieu nationalId");
			}
			
			if(!VendorCodeEnum.TS.value().equals(object.getVendorCode()) 
					&& !VendorCodeEnum.CICDATA.value().equals(object.getVendorCode()) ) {
				throw new ValidationException("unsupport_vendor_code", "Khong ho tro nha cung cap tren");
			}
			
			if(VendorCodeEnum.TS.value().equals(object.getVendorCode())) {
				if(StringUtils.isNullOrEmpty(object.getRequestId())) {
					throw new ValidationException("Thieu requestId cho Trusting Social");
				}
			}
			
		}
	}
	
	public void getScoreLG(String mobilePhone1, String mobilePhone2, String productCode) throws ValidationException {
		if(StringUtils.isNullOrEmpty(mobilePhone1)) {
			throw new ValidationException("Truong mobilePhone1 la bat buoc");
		}
		
		if(StringUtils.isNullOrEmpty(productCode)) {
			throw new ValidationException("Truong productCode la bat buoc");
		}
		
	}
}
