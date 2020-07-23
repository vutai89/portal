package com.mcredit.data.customer;

import org.hibernate.Session;

import com.mcredit.data.BaseUnitOfWork;
import com.mcredit.data.HibernateBase;
import com.mcredit.data.customer.repository.CustomerAccountLinkRepository;
import com.mcredit.data.customer.repository.CustomerAddlInfoRepository;
import com.mcredit.data.customer.repository.CustomerAddressInfoRepository;
import com.mcredit.data.customer.repository.CustomerCompanyInfoRepository;
import com.mcredit.data.customer.repository.CustomerContactInfoRepository;
import com.mcredit.data.customer.repository.CustomerFinancialInfoRepository;
import com.mcredit.data.customer.repository.CustomerIdentityRepository;
import com.mcredit.data.customer.repository.CustomerPersonalInfoRepository;
import com.mcredit.data.repository.IRepository;

public class UnitOfWorkCustomer extends BaseUnitOfWork {
	
	public UnitOfWorkCustomer() {
		super();
	}
	
	public UnitOfWorkCustomer(HibernateBase hibernateBase,Session session) {
		super(hibernateBase,session);
	}
	
	@SuppressWarnings("rawtypes")
	private IRepository _customerAccountLinkRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _customerAddlInfoRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _customerCompanyInfoRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _customerContactInfoRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _customerAddressInfoRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _customerFinancialInfoRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _customerIdentityRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _customerPersonalInfoRepository = null;

	public CustomerAccountLinkRepository customerAccountLinkRepo() {

		if (_customerAccountLinkRepository == null)
			_customerAccountLinkRepository = new CustomerAccountLinkRepository(
					this.session);

		return (CustomerAccountLinkRepository) _customerAccountLinkRepository;
	}

	public CustomerAddlInfoRepository customerAddlInfoRepo() {

		if (_customerAddlInfoRepository == null)
			_customerAddlInfoRepository = new CustomerAddlInfoRepository(
					this.session);

		return (CustomerAddlInfoRepository) _customerAddlInfoRepository;
	}

	public CustomerCompanyInfoRepository customerCompanyInfoRepo() {

		if (_customerCompanyInfoRepository == null)
			_customerCompanyInfoRepository = new CustomerCompanyInfoRepository(
					this.session);

		return (CustomerCompanyInfoRepository) _customerCompanyInfoRepository;
	}

	public CustomerContactInfoRepository customerContactInfoRepo() {

		if (_customerContactInfoRepository == null)
			_customerContactInfoRepository = new CustomerContactInfoRepository(
					this.session);

		return (CustomerContactInfoRepository) _customerContactInfoRepository;
	}
	
	public CustomerAddressInfoRepository customerAddressInfoRepo() {

		if (_customerAddressInfoRepository == null)
			_customerAddressInfoRepository = new CustomerAddressInfoRepository(
					this.session);

		return (CustomerAddressInfoRepository) _customerAddressInfoRepository;
	}

	public CustomerFinancialInfoRepository customerFinancialInfoRepo() {

		if (_customerFinancialInfoRepository == null)
			_customerFinancialInfoRepository = new CustomerFinancialInfoRepository(
					this.session);

		return (CustomerFinancialInfoRepository) _customerFinancialInfoRepository;
	}

	public CustomerIdentityRepository customerIdentityRepo() {

		if (_customerIdentityRepository == null)
			_customerIdentityRepository = new CustomerIdentityRepository(
					this.session);

		return (CustomerIdentityRepository) _customerIdentityRepository;
	}

	public CustomerPersonalInfoRepository customerPersonalInfoRepo() {

		if (_customerPersonalInfoRepository == null)
			_customerPersonalInfoRepository = new CustomerPersonalInfoRepository(
					this.session);

		return (CustomerPersonalInfoRepository) _customerPersonalInfoRepository;
	}
}
