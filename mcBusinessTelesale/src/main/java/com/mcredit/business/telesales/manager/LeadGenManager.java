package com.mcredit.business.telesales.manager;

import java.util.List;

import com.mcredit.business.telesales.factory.AmoFactory;
import com.mcredit.business.telesales.factory.LeadGenFactory;
import com.mcredit.model.enums.LeadGenEnums;
import com.mcredit.model.leadgen.LeadDTO;
import com.mcredit.model.leadgen.LeadResultDTO;
import com.mcredit.model.object.warehouse.LeadGenCustomerInfo;
import com.mcredit.sharedbiz.manager.BaseManager;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

public class LeadGenManager extends BaseManager {

	public LeadResultDTO checkLead(LeadDTO input) throws Exception {
		return this.tryCatch(() -> {
			System.out.println(" ==== checkLead-payLoad: " + JSONConverter.toJSON(input));
			LeadGenFactory.getLeadGenValidation().validateCheckLead(input);
			LeadResultDTO result;
			if ( !StringUtils.isNullOrEmpty(input.getPartner()) 
					&& (LeadGenEnums.PARTNER_TRUST_CONNECT.value().equals(input.getPartner())
							|| LeadGenEnums.PARTNER_AMO.value().equals(input.getPartner()))) {
				// danh cho nhung loai co db rieng nhu TrustConnect, AMO, ...
				result = LeadGenFactory.getLeadGenAggregate(uok.telesale, uok.customer, input).checkRemainingTypes();
			} else {
				// danh cho nhung loai dung chung voi db Leadgen
				result = LeadGenFactory.getLeadGenAggregate(uok.telesale, uok.customer, input).checkLead();
			}

			return result;
		});
	}

	public LeadResultDTO createLead(LeadDTO input) throws Exception {
		return this.tryCatch(() -> {
			System.out.println(" ==== createLead-payLoad: " + JSONConverter.toJSON(input));
			LeadResultDTO result;
			LeadGenFactory.getLeadGenValidation().validateCreateLead(input);
			if (LeadGenEnums.PARTNER_TRUST_CONNECT.value().equals(input.getPartner())
					|| LeadGenEnums.PARTNER_AMO.value().equals(input.getPartner())) {
				LeadGenFactory.getLeadGenValidation().validateSendRemainingTypes(input);
				result = LeadGenFactory.getLeadGenAggregate(uok.telesale, uok.customer, input).createRemainingTypes();
			} else {
				LeadGenFactory.getLeadGenValidation().validateSendLead(input);
				LeadGenFactory.getLeadGenValidation().validateRule(input);
				result = LeadGenFactory.getLeadGenAggregate(uok.telesale, uok.customer, input).createLead();
			}
			return result;
		});
	}

	public List<LeadGenCustomerInfo> findLeadGenCustomerInfo(String identityNumber, String mobileNumber)
			throws Exception {

		return this.tryCatch(() -> {
			LeadGenFactory.getLeadGenCustomerValidation().validate(StringUtils.nullToEmpty(identityNumber),
					StringUtils.nullToEmpty(mobileNumber));

			return LeadGenFactory.getLeadGenAggregate(uok.telesale, identityNumber, mobileNumber).findLead();
		});
	}
}
