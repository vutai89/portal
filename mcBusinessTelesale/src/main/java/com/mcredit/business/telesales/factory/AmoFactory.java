package com.mcredit.business.telesales.factory;

import com.mcredit.business.telesales.validation.AmoValidation;
import com.mcredit.data.customer.UnitOfWorkCustomer;
import com.mcredit.data.telesale.UnitOfWorkTelesale;
import com.mcredit.model.leadgen.LeadDTO;
import com.mcredit.sharedbiz.aggregate.AmoAggregate;

public class AmoFactory {

	public static AmoValidation getAmoValidation() {
		
		return new AmoValidation();
	}

	public static AmoAggregate getAmoAggregate(UnitOfWorkTelesale uokTelesale, UnitOfWorkCustomer uokCustomer, LeadDTO input) {
		
		return new AmoAggregate(uokTelesale, uokCustomer, input);
	}
}
