package com.mcredit.data.customer.repository;

import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.DataException;
import com.mcredit.data.customer.entity.CustomerFinancialInfo;
import com.mcredit.data.repository.IUpsertRepository;

public class CustomerFinancialInfoRepository extends BaseRepository implements
		IUpsertRepository<CustomerFinancialInfo> {

	public CustomerFinancialInfoRepository(Session session) {
		super(session);
	}

	public void upsert(CustomerFinancialInfo item) throws DataException {
		if( item.getId() != null )
			updateEntity(item, "CustomerFinancialInfo");
		else
			this.session.save("CustomerFinancialInfo", item);
	}
	
	public void remove(CustomerFinancialInfo item) throws DataException {
		this.session.delete("CustomerFinancialInfo", item);
	}
	
	@SuppressWarnings("rawtypes")
	public List queryCustomerFinancialInfoByCusID(String cusID) throws DataException {
		return this.session.getNamedNativeQuery("getCustomerFinancialInfoByCusID")
							.setParameter("cusID", cusID).list();
	}
}
