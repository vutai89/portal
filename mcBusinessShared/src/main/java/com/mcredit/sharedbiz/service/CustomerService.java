package com.mcredit.sharedbiz.service;

import java.util.Date;

import com.mcredit.data.customer.UnitOfWorkCustomer;
import com.mcredit.model.dto.CustomerDTO;

public class CustomerService {
	
	private UnitOfWorkCustomer uokCustomer = null;
	
	public CustomerService(UnitOfWorkCustomer uokCustomer) {
		this.uokCustomer = uokCustomer;
	}
	
	public CustomerDTO findCustIdByCardId(String cardId) {
		
		Object obj = uokCustomer.customerAccountLinkRepo().findCardInformationAllByCardId(cardId.trim());
		if( obj!=null ) {
			Object[] o = (Object[]) obj;
			CustomerDTO csDto = new CustomerDTO() ;
			csDto.setLinkValue((String)o[0]);
			csDto.setId((Long)o[1]);
			csDto.setCustName((String)o[2]);
			csDto.setIdentityNumber((String) o[3]);
			csDto.setIdentityIssueDate((Date) o[4]);
			csDto.setCodeValue1((String) o[5]);
			csDto.setMobile((String) o[6]);
			csDto.setCoreCustCode((String) o[7]);	
			return csDto;
		}
		return null;
	}
	
	
}
