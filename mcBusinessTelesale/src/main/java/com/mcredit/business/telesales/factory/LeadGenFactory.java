package com.mcredit.business.telesales.factory;

import com.mcredit.business.telesales.validation.FindLeadGenCustomerValidation;
import com.mcredit.business.telesales.validation.LeadGenValidation;
import com.mcredit.data.customer.UnitOfWorkCustomer;
import com.mcredit.data.telesale.UnitOfWorkTelesale;
import com.mcredit.model.leadgen.LeadDTO;
import com.mcredit.sharedbiz.aggregate.LeadGenAggregate;

public class LeadGenFactory {

	public static LeadGenValidation getLeadGenValidation() {
		
		return new LeadGenValidation();
	}

	public static LeadGenAggregate getLeadGenAggregate(UnitOfWorkTelesale uokTelesale, UnitOfWorkCustomer uokCustomer, LeadDTO input) {
		
		return new LeadGenAggregate(uokTelesale, uokCustomer, input);
	}
	
	public static LeadGenAggregate getLeadGenAggregate(UnitOfWorkTelesale uokTelesale, String identityNumber, String mobileNumber) {
		
		return new LeadGenAggregate(uokTelesale, identityNumber, mobileNumber);
	}
	
	public static FindLeadGenCustomerValidation getLeadGenCustomerValidation() {
		
		return new FindLeadGenCustomerValidation();
	}
}
