package com.mcredit.business.customer.dto;

import java.util.List;

import com.mcredit.model.dto.card.CustomerAccountLinkDTO;
import com.mcredit.model.dto.card.CustomerCompanyInfoDTO;
import com.mcredit.model.dto.card.CustomerContactInfoDTO;
import com.mcredit.model.dto.card.CustomerFinancialInfoDTO;

public class CustomerDTO {

	private static final Long serialVersionUID = 1L;

	private CustomerPersonalInfoDTO personalInfo;
	private CustomerAddlInfoDTO addlInfo;
	private CustomerFinancialInfoDTO financialInfo;
	private CustomerContactInfoDTO contactInfo;
	private CustomerCompanyInfoDTO companyInfo;
	private List<CustomerIdentityDTO> customerIdentity;
	private CustomerAccountLinkDTO accountLink;
	private List<CustomerAccountLinkDTO> lstAccountLink;
	
	public CustomerDTO() {}
	
	public CustomerDTO(CustomerPersonalInfoDTO personalInfo) {
		this.personalInfo = personalInfo;
	}
	
	public List<CustomerAccountLinkDTO> getLstAccountLink() {
		return lstAccountLink;
	}

	public void setLstAccountLink(List<CustomerAccountLinkDTO> lstAccountLink) {
		this.lstAccountLink = lstAccountLink;
	}
	
	public CustomerPersonalInfoDTO getPersonalInfo() {
		return personalInfo;
	}

	public void setPersonalInfo(CustomerPersonalInfoDTO personalInfo) {
		this.personalInfo = personalInfo;
	}

	public CustomerAddlInfoDTO getAddlInfo() {
		return addlInfo;
	}

	public void setAddlInfo(CustomerAddlInfoDTO addlInfo) {
		this.addlInfo = addlInfo;
	}

	public CustomerFinancialInfoDTO getFinancialInfo() {
		return financialInfo;
	}

	public void setFinancialInfo(CustomerFinancialInfoDTO financialInfo) {
		this.financialInfo = financialInfo;
	}

	public CustomerContactInfoDTO getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(CustomerContactInfoDTO contactInfo) {
		this.contactInfo = contactInfo;
	}

	public CustomerCompanyInfoDTO getCompanyInfo() {
		return companyInfo;
	}

	public void setCompanyInfo(CustomerCompanyInfoDTO companyInfo) {
		this.companyInfo = companyInfo;
	}

	public List<CustomerIdentityDTO> getCustomerIdentity() {
		return customerIdentity;
	}

	public void setCustomerIdentity(List<CustomerIdentityDTO> customerIdentity) {
		this.customerIdentity = customerIdentity;
	}

	public CustomerAccountLinkDTO getAccountLink() {
		return accountLink;
	}

	public void setAccountLink(CustomerAccountLinkDTO accountLink) {
		this.accountLink = accountLink;
	}

	public static Long getSerialversionuid() {
		return serialVersionUID;
	}
}