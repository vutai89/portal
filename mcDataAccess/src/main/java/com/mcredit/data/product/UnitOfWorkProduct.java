package com.mcredit.data.product;

import org.hibernate.Session;

import com.mcredit.data.BaseUnitOfWork;
import com.mcredit.data.HibernateBase;
import com.mcredit.data.common.repository.ProductRepository;
import com.mcredit.data.customer.repository.CustomerAccountLinkRepository;
import com.mcredit.data.customer.repository.CustomerAddlInfoRepository;
import com.mcredit.data.customer.repository.CustomerAddressInfoRepository;
import com.mcredit.data.customer.repository.CustomerCompanyInfoRepository;
import com.mcredit.data.customer.repository.CustomerContactInfoRepository;
import com.mcredit.data.customer.repository.CustomerFinancialInfoRepository;
import com.mcredit.data.customer.repository.CustomerIdentityRepository;
import com.mcredit.data.customer.repository.CustomerPersonalInfoRepository;
import com.mcredit.data.repository.IRepository;

public class UnitOfWorkProduct extends BaseUnitOfWork {
	
	public UnitOfWorkProduct() {
		super();
	}
	
	public UnitOfWorkProduct(HibernateBase hibernateBase,Session session) {
		super(hibernateBase,session);
	}
	
	@SuppressWarnings("rawtypes")
	private IRepository _productRepository = null;

	public ProductRepository productRepo() {

		if (_productRepository == null)
			_productRepository = new ProductRepository(
					this.session);

		return (ProductRepository) _productRepository;
	}
}
