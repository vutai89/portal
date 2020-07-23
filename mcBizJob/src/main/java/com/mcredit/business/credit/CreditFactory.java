package com.mcredit.business.credit;

import org.modelmapper.ModelMapper;

import com.mcredit.business.credit.dto.CreditDTO;
import com.mcredit.business.credit.validation.CreditApplicationValidation;
import com.mcredit.data.credit.UnitOfWorkCredit;
import com.mcredit.data.credit.entity.CreditApplicationAdditional;
import com.mcredit.data.credit.entity.CreditApplicationAppraisal;
import com.mcredit.data.credit.entity.CreditApplicationBPM;
import com.mcredit.data.credit.entity.CreditApplicationLoanManagement;
import com.mcredit.data.credit.entity.CreditApplicationRequest;

public class CreditFactory {
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public static CreditAggregate getInstance(CreditDTO input, UnitOfWorkCredit uok) {
		
		CreditAggregate item  = new CreditAggregate(uok);
		
		if( input.getAppRequest() != null )
			item.setApplicationRequest(modelMapper.map(input.getAppRequest(), CreditApplicationRequest.class));
		if( input.getAppAppraisal() != null )
			item.setApplicationAppraisal(modelMapper.map(input.getAppAppraisal(), CreditApplicationAppraisal.class));
		if( input.getAppBPM() != null )
			item.setApplicationBPM(modelMapper.map(input.getAppBPM(), CreditApplicationBPM.class));
		if( input.getAppLMN() != null )
			item.setApplicationLMN(modelMapper.map(input.getAppLMN(), CreditApplicationLoanManagement.class));
		if( input.getAppAdditional() != null )
			item.setApplicationAdditional(modelMapper.map(input.getAppAdditional(), CreditApplicationAdditional.class));
		return item;
	}
	
	public static CreditAggregate getInstance(Long id, UnitOfWorkCredit uok) {
		CreditAggregate item  = new CreditAggregate(uok);
		item.setApplicationRequest(uok.creditApplicationRequestRepo().get(id));
		item.setApplicationAdditional(uok.creditApplicationAdditionalRepo().getByRequestId(id));
		item.setApplicationAppraisal(uok.creditApplicationAppraisalRepo().getByRequestId(id));
		item.setApplicationBPM(uok.creditApplicationBPMRepo().getByRequestId(id));
		item.setApplicationLMN(uok.creditApplicationLoanManagementRepo().getByRequestId(id));
		
		return item;
	}
	
	public static CreditApplicationValidation createCreditApplicationValidation() {

		return new CreditApplicationValidation();
	}
}
