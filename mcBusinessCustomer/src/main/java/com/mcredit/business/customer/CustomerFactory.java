package com.mcredit.business.customer;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.mcredit.business.customer.dto.CustomerDTO;
import com.mcredit.business.customer.dto.CustomerIdentityDTO;
import com.mcredit.business.customer.validation.CheckCatValidation;
import com.mcredit.business.customer.validation.CheckDuplicateValidation;
import com.mcredit.business.customer.validation.CustomerAccountLinkValidation;
import com.mcredit.business.customer.validation.CustomerAddlInfoValidation;
import com.mcredit.business.customer.validation.CustomerCompanyInfoValidation;
import com.mcredit.business.customer.validation.CustomerContactInfoValidation;
import com.mcredit.business.customer.validation.CustomerFinancialInfoValidation;
import com.mcredit.business.customer.validation.CustomerIdentityValidation;
import com.mcredit.business.customer.validation.CustomerPersonalInfoValidation;
import com.mcredit.business.customer.validation.CustomerValidation;
import com.mcredit.business.customer.validation.FindContractDuplicateValidation;
import com.mcredit.business.customer.validation.QueryInfoValidation;
import com.mcredit.data.customer.UnitOfWorkCustomer;
import com.mcredit.data.customer.entity.CustomerAccountLink;
import com.mcredit.data.customer.entity.CustomerAddlInfo;
import com.mcredit.data.customer.entity.CustomerAddressInfo;
import com.mcredit.data.customer.entity.CustomerCompanyInfo;
import com.mcredit.data.customer.entity.CustomerContactInfo;
import com.mcredit.data.customer.entity.CustomerFinancialInfo;
import com.mcredit.data.customer.entity.CustomerIdentity;
import com.mcredit.data.customer.entity.CustomerPersonalInfo;
import com.mcredit.model.dto.card.CustomerAccountLinkUpsertResultDTO;
import com.mcredit.model.dto.card.CustomerAddressInfoDTO;
import com.mcredit.model.dto.card.CustomerCommunicationInfoDTO;
import com.mcredit.model.dto.card.CustomerPersonalInfoUpsertResultDTO;
import com.mcredit.sharedbiz.validation.ValidationException;

public class CustomerFactory {

	private static ModelMapper modelMapper = new ModelMapper();

	public static CustomerAggregate getInstance(CustomerDTO input, UnitOfWorkCustomer uok) {

		CustomerAggregate item = null;

		if (input != null && uok != null) {

			item = new CustomerAggregate(uok);

			if (input.getPersonalInfo() != null)
				item.setPersonalInfo(modelMapper.map(input.getPersonalInfo(), CustomerPersonalInfo.class));

			if (input.getAddlInfo() != null)
				item.setAddlInfo(modelMapper.map(input.getAddlInfo(), CustomerAddlInfo.class));

			if (input.getFinancialInfo() != null)
				item.setFinancialInfo(modelMapper.map(input.getFinancialInfo(), CustomerFinancialInfo.class));

			if (input.getContactInfo() != null && input.getContactInfo().getCommunicationInfo() != null
					&& input.getContactInfo().getCommunicationInfo().size() > 0) {
				List<CustomerContactInfo> lst = new ArrayList<CustomerContactInfo>();

				for (CustomerCommunicationInfoDTO communication : input.getContactInfo().getCommunicationInfo()) {
					lst.add((modelMapper.map(communication, CustomerContactInfo.class)));
				}
				item.setContactInfo(lst);
			}

			if (input.getContactInfo() != null && input.getContactInfo().getAddressInfo() != null && input.getContactInfo().getAddressInfo().size() > 0) {

				List<CustomerAddressInfo> lst = new ArrayList<CustomerAddressInfo>();

				for (CustomerAddressInfoDTO address : input.getContactInfo().getAddressInfo()) {
					lst.add((modelMapper.map(address, CustomerAddressInfo.class)));
				}
				item.setAddressInfo(lst);
			}

			if (input.getCompanyInfo() != null)
				item.setCompanyInfo(modelMapper.map(input.getCompanyInfo(), CustomerCompanyInfo.class));

			if (input.getCustomerIdentity() != null && input.getCustomerIdentity().size() > 0) {
				List<CustomerIdentity> lstIdentity = new ArrayList<CustomerIdentity>();
				for (CustomerIdentityDTO objIdentity : input.getCustomerIdentity()) {
					lstIdentity.add(modelMapper.map(objIdentity, CustomerIdentity.class));
				}
				item.setLstIdentity(lstIdentity);
			}

			if (input.getAccountLink() != null)
				item.setAccountLink(modelMapper.map(input.getAccountLink(), CustomerAccountLink.class));
		}
		return item;
	}

	public static CustomerAggregate getInstance(UnitOfWorkCustomer uok) {
		return new CustomerAggregate(uok);
	}

	public static CustomerPersonalInfoUpsertResultDTO getInstance(Long mcCustId, String mcCustCode) {
		CustomerPersonalInfoUpsertResultDTO result = new CustomerPersonalInfoUpsertResultDTO();

		result.setMcCustCode(mcCustCode);
		result.setMcCustId(mcCustId);

		return result;
	}

	public static CustomerAccountLinkUpsertResultDTO getInstance(Long id) {
		CustomerAccountLinkUpsertResultDTO result = new CustomerAccountLinkUpsertResultDTO();

		result.setId(id);

		return result;
	}

	public static CustomerValidation createCustomerValidation() {
		return new CustomerValidation();
	}

	public static void customerValidation(CustomerDTO item, String updateId) throws ValidationException {
		validatePersonalInfo(item, updateId);
		validateCompanyInfo(item);
		validateAddlInfo(item);
		validateFinancialInfo(item);
		validateContactInfo(item);
		validateIdentity(item);
	}

	public static QueryInfoValidation getQueryInfoValidation() {
		return new QueryInfoValidation();
	}

	public static CheckDuplicateValidation getCheckDuplicateValidation() {
		return new CheckDuplicateValidation();
	}

	public static FindContractDuplicateValidation getContractDuplicateValidation() {
		return new FindContractDuplicateValidation();
	}

	private static void validatePersonalInfo(CustomerDTO item, String updateId) throws ValidationException {
		new CustomerPersonalInfoValidation().validatePersonalInfo(item.getPersonalInfo(), updateId);
	}

	public static void validateCompanyInfo(CustomerDTO item) throws ValidationException {
		new CustomerCompanyInfoValidation().validateCompanyInfo(item.getCompanyInfo());
	}

	public static void validateAddlInfo(CustomerDTO item) throws ValidationException {
		new CustomerAddlInfoValidation().validateAddlInfo(item.getAddlInfo());
	}

	public static void validateFinancialInfo(CustomerDTO item) throws ValidationException {
		new CustomerFinancialInfoValidation().validateFinancialInfo(item.getFinancialInfo());
	}

	public static void validateContactInfo(CustomerDTO item) throws ValidationException {
		new CustomerContactInfoValidation().validateContactInfo(item.getContactInfo());
	}

	public static void validateIdentity(CustomerDTO item) throws ValidationException {
		new CustomerIdentityValidation().validateIdentity(item.getCustomerIdentity());
	}

	/*
	 * public static void updateCoreCustCodeValidation(CustomerPersonalInfoDTO
	 * personalInfo, Long updateId) throws Exception { new
	 * CustomerPersonalInfoValidation().validateCoreCustCode(personalInfo,
	 * updateId); }
	 */

	public static void customerAccountLinkValidation(CustomerDTO item, String updateId) throws Exception {
		new CustomerAccountLinkValidation().validate(item.getAccountLink(), updateId);
	}
	
	/* ============ Check cat  ============  */
	public static CheckCatValidation checkCatValidation() {
		return new CheckCatValidation();
	}
}
