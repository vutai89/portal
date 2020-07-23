package com.mcredit.business.telesales.validation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mcredit.business.telesales.util.EsbApi;
import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.dto.CodeTableEliteDTO;
import com.mcredit.model.enums.CustomerValidationLength;
import com.mcredit.model.enums.LeadGenEnums;
import com.mcredit.model.leadgen.LeadDTO;
import com.mcredit.model.object.RuleResult;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

public class LeadGenValidation extends AbstractValidation {
	
	public static final String DEFAULT_CAMPAIGN_CODE = "LeadGen";
	
	private void validateBaseInfo(String partner, String phoneNumber, String nationalId) throws ValidationException {
		if (!StringUtils.isNullOrEmpty(partner)
				&& LeadGenEnums.PARTNER_CICLG.value().toLowerCase().contains(partner.toLowerCase().trim())
				&& (StringUtils.isNullOrEmpty(phoneNumber) || StringUtils.isNullOrEmpty(nationalId))) {
			getMessageDes()
					.add(Messages.getString("validation.field.madatory", Labels.getString("label.leadgen.phoneNumber")
							+ "," + Labels.getString("label.leadgen.nationalId")));
		} else if (StringUtils.isNullOrEmpty(phoneNumber))
			getMessageDes().add(
					Messages.getString("validation.field.madatory", Labels.getString("label.leadgen.phoneNumber")));

		if (!StringUtils.checkMobilePhoneNumberNew(phoneNumber))
			getMessageDes().add(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.leadgen.phoneNumber")));

		if (!StringUtils.isNullOrEmpty(nationalId)) {
			if (!StringUtils.isNumberic((nationalId)))
				getMessageDes().add(Messages.getString("validation.field.invalidFormat",
						Labels.getString("label.leadgen.nationalId")));
			else if (!Arrays.asList(new String[] { "9", "12" }).contains(nationalId.length() + ""))
				getMessageDes().add(Messages.getString("label.leadgen.nationalId.inRange",
						Labels.getString("label.leadgen.nationalId")));
		}

		if (!isValid())
			throw new ValidationException(BusinessConstant.JSON_NAME_ERROR, this.buildValidationMessage(), "");
	}

	public void validateCheckLead(LeadDTO input) throws ValidationException {
		if (input == null) {
			getMessageDes().add(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.telesale.checkDuplicate.contractSearch.input")));
			throw new ValidationException(BusinessConstant.JSON_NAME_ERROR, this.buildValidationMessage(), "");
		}

		if (StringUtils.isNullOrEmpty(input.getRequestId()))
			getMessageDes()
					.add(Messages.getString("validation.field.madatory", Labels.getString("label.leadgen.requestId")));

		if (!StringUtils.isNullOrEmpty(input.getPartner()) && !checkPartnerExist(input.getPartner())) {
			getMessageDes()
			.add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.leadgen.partner")));
		}

		if (!isValid())
			throw new ValidationException(BusinessConstant.JSON_NAME_ERROR, this.buildValidationMessage(), "");

		validateBaseInfo(input.getPartner(), input.getPhoneNumber(), input.getNationalId());
	}

	public void validateSendLead(LeadDTO input) throws ValidationException {
//		if (input == null) {
//			getMessageDes().add(Messages.getString("validation.field.invalidFormat",
//					Labels.getString("label.telesale.checkDuplicate.contractSearch.input")));
//			throw new ValidationException(BusinessConstant.JSON_NAME_ERROR, this.buildValidationMessage(), "");
//		}

		if (StringUtils.isNullOrEmpty(input.getRequestId()))
			getMessageDes()
					.add(Messages.getString("validation.field.madatory", Labels.getString("label.leadgen.requestId")));

//		if (StringUtils.isNullOrEmpty(input.getPartner()))
//			getMessageDes()
//					.add(Messages.getString("validation.field.madatory", Labels.getString("label.leadgen.partner")));
		
		if (StringUtils.isNullOrEmpty(input.getRefId()))
			getMessageDes()
					.add(Messages.getString("validation.field.madatory", Labels.getString("label.leadgen.refId")));
		else if (input.getRefId().length() > 20)
			getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.leadgen.refId"),
					CustomerValidationLength.MAX_LEN_CUSTOMER_CORE_CODE.value()));

		List<String> msgLst = new ArrayList<>();

		if (StringUtils.isNullOrEmpty(input.getPhoneNumber()))
			msgLst.add(Messages.getString("validation.field.madatory", Labels.getString("label.leadgen.phoneNumber")));
		else if (!StringUtils.checkMobilePhoneNumberNew(input.getPhoneNumber()))
			msgLst.add(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.leadgen.phoneNumber")));

		if (!StringUtils.isNullOrEmpty(input.getNationalId())) {
			if (!StringUtils.isNumberic((input.getNationalId())))
				msgLst.add(Messages.getString("validation.field.invalidFormat",
						Labels.getString("label.leadgen.nationalId")));
			else if (!Arrays.asList(new String[] { "9", "12" }).contains(input.getNationalId().length() + "")) {
				msgLst.add(Messages.getString("label.leadgen.nationalId.inRange",
						Labels.getString("label.leadgen.nationalId")));

				if (input.getNationalId().length() > CustomerValidationLength.MAX_LEN_PROVINCE.value())
					input.setNationalId(
							input.getNationalId().substring(0, CustomerValidationLength.MAX_LEN_PROVINCE.value()));
			}
		}

		if (StringUtils.isNullOrEmpty(input.getTelcoCode()))
			msgLst.add(Messages.getString("validation.field.madatory", Labels.getString("label.leadgen.telcoCode")));
		else if (input.getTelcoCode().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_CORE_CODE.value()) {

			msgLst.add(Messages.getString("validation.field.length", Labels.getString("label.leadgen.telcoCode"),
					CustomerValidationLength.MAX_LEN_CUSTOMER_CORE_CODE.value()));

			input.setTelcoCode(
					input.getTelcoCode().substring(0, CustomerValidationLength.MAX_LEN_CUSTOMER_CORE_CODE.value()));
		}

		if (!StringUtils.isNullOrEmpty(input.getSource())
				&& input.getSource().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_CORE_CODE.value()) {

			msgLst.add(Messages.getString("validation.field.length", Labels.getString("label.leadgen.source"),
					CustomerValidationLength.MAX_LEN_CUSTOMER_CORE_CODE.value()));

			input.setSource(
					input.getSource().substring(0, CustomerValidationLength.MAX_LEN_CUSTOMER_CORE_CODE.value()));
		}

		if (input.getFullName().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()) {
			msgLst.add(Messages.getString("validation.field.length", Labels.getString("label.leadgen.fullName"),
					CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()));

			input.setFullName(input.getFullName().substring(0,
					CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()));
		}

		if (!StringUtils.isNullOrEmpty(input.getProvince()) && input.getProvince()
				.length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()) {

			msgLst.add(Messages.getString("validation.field.length", Labels.getString("label.leadgen.province"),
					CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()));

			input.setProvince(input.getProvince().substring(0,
					CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()));
		}

		if (!StringUtils.isNullOrEmpty(input.getIncomeLevel())
				&& input.getIncomeLevel().length() > CustomerValidationLength.MAX_LEN_PROVINCE.value()) {

			msgLst.add(Messages.getString("validation.field.length", Labels.getString("label.leadgen.incomeLevel"),
					CustomerValidationLength.MAX_LEN_PROVINCE.value()));

			input.setIncomeLevel(
					input.getIncomeLevel().substring(0, CustomerValidationLength.MAX_LEN_PROVINCE.value()));
		}

		if (!StringUtils.isNullOrEmpty(input.getOther())
				&& input.getOther().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()) {

			msgLst.add(Messages.getString("validation.field.length", Labels.getString("label.leadgen.note"),
					CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()));

			input.setOther(
					input.getOther().substring(0, CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()));
		}
		
		if (StringUtils.isNullOrEmpty(input.getCampaignCode()) && input.getPartner().equals(LeadGenEnums.PARTNER_CICLG.value())){
			msgLst.add(Messages.getString("validation.field.madatory", Labels.getString("label.leadgen.campaignCode")));
		}

		// Check campaign code. If campaignCode equal null and partner isn't CICLG then campaignCode = "LeadGen"
		if(null == input.getCampaignCode() && !input.getPartner().equals(LeadGenEnums.PARTNER_CICLG)){
			input.setCampaignCode(DEFAULT_CAMPAIGN_CODE);
		}
		
		if (msgLst.size() > 0) {
			StringBuilder sb = new StringBuilder();
			int count = 0;
			for (Iterator<String> iterator = msgLst.iterator(); iterator.hasNext();) {
				String msg = (String) iterator.next();
				sb.append(msg);
				sb.append("\r\n");
				count++;
			}
			String msgStr = sb.toString();
			if (count == 1)
				msgStr = msgStr.replace("\r\n", "");

			getMessageDes().add(msgStr);
			throw new ValidationException(BusinessConstant.JSON_NAME_ERROR, this.buildValidationMessage(), "");
		}

		validateBaseInfo(input.getPartner(), input.getPhoneNumber(), input.getNationalId());

		if (!isValid())
			throw new ValidationException(BusinessConstant.JSON_NAME_ERROR, this.buildValidationMessage(), "");
	}

	public void validateRule(LeadDTO input) throws ValidationException, Exception {
		EsbApi esbApi = new EsbApi();
		ApiResult apiResult = null;
		RuleResult ruleResult = null;
		
		String sMin = "0";
		String sMax = "0";

		// NoScore case
		if (input.getScoreRange().trim().isEmpty() || input.getScoreRange() == null) {
			input.setMinScore(0);
			input.setMaxScore(0);
		// Normal case
		} else {
			// Score range
			String[] arr = input.getScoreRange().split("-");
			if (arr == null || arr.length == 0) {
				getMessageDes().add(Messages.getString("validation.field.invalidFormat",
						Labels.getString("label.leadgen.scoreRange")));
				throw new ValidationException(BusinessConstant.JSON_NAME_ERROR, this.buildValidationMessage(), "");
			}

			sMin = arr[0].trim();
			sMax = arr.length > 1 ? (arr[1] != null ? arr[1].trim() : "") : "";
		}
		
		if (!StringUtils.isNumberic(sMin)) {
			getMessageDes().add(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.leadgen.scoreRange.min")));
			throw new ValidationException(BusinessConstant.JSON_NAME_ERROR, this.buildValidationMessage(), "");
		}

		if (sMin.length() >= 5) {
			getMessageDes().add(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.leadgen.scoreRange.min")));
			throw new ValidationException(BusinessConstant.JSON_NAME_ERROR, this.buildValidationMessage(), "");
		}

		if (LeadGenEnums.PREFIX_UPL_CODE.value().toLowerCase().contains(input.getPartner().toLowerCase().trim())
				&& !StringUtils.isNumberic(sMax)) {
			getMessageDes().add(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.leadgen.scoreRange.max")));
			throw new ValidationException(BusinessConstant.JSON_NAME_ERROR, this.buildValidationMessage(), "");
		}

		if (!StringUtils.isNumberic(sMin)) {
			getMessageDes().add(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.leadgen.scoreRange.min")));

			throw new ValidationException(BusinessConstant.JSON_NAME_ERROR, this.buildValidationMessage(), "");
		}

		int min = Integer.parseInt(sMin);
		int max = min;

		if (StringUtils.isNumberic(sMax)) {
			if (sMax.length() >= 5) {
				getMessageDes().add(Messages.getString("validation.field.invalidFormat",
						Labels.getString("label.leadgen.scoreRange.max")));
				throw new ValidationException(BusinessConstant.JSON_NAME_ERROR, this.buildValidationMessage(), "");
			}
			max = Integer.parseInt(sMax);
		}

		input.setMinScore(min);
		input.setMaxScore(max);

		/** CHECK RULES **/
		Map<String, String> inputCheckRule = new HashMap<String, String>();
		inputCheckRule.put("ruleCode", "LEADGEN_SCORE");
		inputCheckRule.put("partner", input.getPartner());
		inputCheckRule.put("telcoCode", input.getTelcoCode());
		inputCheckRule.put("minScore", String.valueOf(input.getMinScore()));
		inputCheckRule.put("typeScore", "1");
		
		apiResult = esbApi.checkRule(inputCheckRule);

		if (apiResult == null || !apiResult.getStatus())
			getMessageDes().add(Messages.getString("validation.field.invalidFormat",
					Labels.getString("esbService.checkRules.error1")));

		ruleResult = JSONConverter.toObject(apiResult.getBodyContent(), RuleResult.class);
		if (ruleResult == null)
			getMessageDes()
					.add(Messages.getString("esbService.checkRules.error3", Labels.getString("LEADGEN_SCORE")));

		if (!ruleResult.getReturnCode().contains("200"))
			getMessageDes().add(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.leadgen.scoreRange")));
		
		// NoScore case: 
		if ("0".equals(ruleResult.getMultiValue())) {
			input.setMinScore(0);
			input.setMaxScore(0);
		}

		input.setRuleResult(ruleResult);

		if (!isValid())
			throw new ValidationException(BusinessConstant.JSON_NAME_ERROR, this.buildValidationMessage(), "");
	}
	
	public void validateSendRemainingTypes(LeadDTO input) throws ValidationException {
//		if ( input == null ) {
//			getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.telesale.checkDuplicate.contractSearch.input")));
//			throw new ValidationException(BusinessConstant.JSON_NAME_ERROR, this.buildValidationMessage(), "");
//		}
		
		if ( StringUtils.isNullOrEmpty(input.getRefId()) )
			getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.leadgen.refId")));
		else if( input.getRefId().length() > 20 )
			getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.leadgen.refId"), CustomerValidationLength.MAX_LEN_CUSTOMER_CORE_CODE.value()));
		
		if ( StringUtils.isNullOrEmpty(input.getPhoneNumber()) )
			getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.leadgen.phoneNumber")));
		else if ( !StringUtils.checkMobilePhoneNumberNew(input.getPhoneNumber()) )
			getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.leadgen.phoneNumber")));
		
		if ( !StringUtils.isNullOrEmpty(input.getNationalId()) ) {
			if ( !StringUtils.isNumberic((input.getNationalId())) )
				getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.leadgen.nationalId")));
			else if ( !Arrays.asList(new String[]{"9", "12"}).contains(input.getNationalId().length() + "") )
				getMessageDes().add(Messages.getString("label.leadgen.nationalId.inRange", Labels.getString("label.leadgen.nationalId")));
		}
		
		if (!StringUtils.isNullOrEmpty(input.getTelcoCode())
			 && input.getTelcoCode().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_CORE_CODE.value()) {
			getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.leadgen.telcoCode"),
					CustomerValidationLength.MAX_LEN_CUSTOMER_CORE_CODE.value()));
		}
		
		if (!StringUtils.isNullOrEmpty(input.getIncomeLevel())
				&& input.getIncomeLevel().length() > CustomerValidationLength.MAX_LEN_PROVINCE.value()) {
			getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.leadgen.incomeLevel"),
					CustomerValidationLength.MAX_LEN_PROVINCE.value()));
		}
		
		if (!StringUtils.isNullOrEmpty(input.getOther())
				&& input.getOther().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()) {
			getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.leadgen.note"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()));
		}
		
		if (!StringUtils.isNullOrEmpty(input.getSource())
				&& input.getSource().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_CORE_CODE.value()) {
			getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.leadgen.source"),
					CustomerValidationLength.MAX_LEN_CUSTOMER_CORE_CODE.value()));
		}
		
		if ( !StringUtils.isNullOrEmpty(input.getDob()) && input.getDob().length() > 15 )
			getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.personal.birthDate"), CustomerValidationLength.MAX_LEN_PROVINCE.value()));
		
		if ( !StringUtils.isNullOrEmpty(input.getProvince()) && StringUtils.getUTFSize(input.getProvince()) > 30 )
			getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.leadgen.province"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()));
		
		if ( !StringUtils.isNullOrEmpty(input.getFullName()) && StringUtils.getUTFSize(input.getFullName()) > 100 )
			getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.leadgen.fullName"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()));
		
		if ( !StringUtils.isNullOrEmpty(input.getAddress()) && StringUtils.getUTFSize(input.getAddress()) > 50 )
			getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.leadgen.address"), CustomerValidationLength.MAX_LEN_ADDRESS.value()));
		
		if ( !isValid() )
			throw new ValidationException(BusinessConstant.JSON_NAME_ERROR, this.buildValidationMessage(), "");
	}
	
	public void validateCreateLead(LeadDTO input) throws ValidationException {
		if ( input == null ) {
			getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.telesale.checkDuplicate.contractSearch.input")));
			throw new ValidationException(BusinessConstant.JSON_NAME_ERROR, this.buildValidationMessage(), "");
		}
		
		if (!StringUtils.isNullOrEmpty(input.getPartner()) && !checkPartnerExist(input.getPartner())) {
			getMessageDes()
			.add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.leadgen.partner")));
		}
		
		/* Bo sung validate dob:
		 * 1. cho phep truyen rong
		 * 2. Neu co gia tri phai thoa man format dd-MM-yyyy hoac yyyy
		 */
		if(!StringUtils.isNullOrEmpty(input.getDob())) {
			try {
				if(input.getDob().contains("-")) {
					DateFormat dob = new SimpleDateFormat("dd-MM-yyyy");
					dob.setLenient(false);
					dob.parse(input.getDob());
				} else if(Integer.parseInt(input.getDob()) > 0 && input.getDob().length() == 4){
					Date dob = new SimpleDateFormat("yyyy").parse(input.getDob());
				} else {
					getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.leadgen.dob")));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.leadgen.dob")));
			}
		}
		
		if ( !isValid() )
			throw new ValidationException(BusinessConstant.JSON_NAME_ERROR, this.buildValidationMessage(), "");
	}
	
	public boolean checkPartnerExist(String partner) {
		List<CodeTableEliteDTO> lstPartner = CodeTableCacheManager.getInstance().getByCategory(LeadGenEnums.UPL_SRC.toString());
		for (CodeTableEliteDTO codeTableEliteDTO : lstPartner) {
			if(codeTableEliteDTO.getCodeValue1().equals(partner)) {
				// Neu partner nam trong danh sach cac doi tac duoc import thi tra ve true
				System.out.println(codeTableEliteDTO.getCodeValue1());
				return true;
			}
		}
		return false;
	}
}
