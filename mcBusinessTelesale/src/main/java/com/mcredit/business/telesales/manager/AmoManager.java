package com.mcredit.business.telesales.manager;

import com.mcredit.business.telesales.factory.AmoFactory;
import com.mcredit.model.leadgen.LeadDTO;
import com.mcredit.model.leadgen.LeadResultDTO;
import com.mcredit.sharedbiz.manager.BaseManager;
import com.mcredit.util.JSONConverter;

public class AmoManager extends BaseManager {

    public LeadResultDTO createLead(LeadDTO input) throws Exception {
    	
        return this.tryCatch(() -> {
        	
        	System.out.println(" ==== AMO.createLead.payLoad: " + JSONConverter.toJSON(input));
        	
        	AmoFactory.getAmoValidation().validateSendLead(input);
        	
        	LeadResultDTO result = AmoFactory.getAmoAggregate(uok.telesale, uok.customer, input).createLead();
        	
        	return result;
        });
    }
}
