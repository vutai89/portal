package com.mcredit.data.customer.repository;

import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.DataException;
import com.mcredit.data.customer.entity.CustomerAddlInfo;
import com.mcredit.data.repository.IUpsertRepository;

public class CustomerAddlInfoRepository extends BaseRepository implements
		IUpsertRepository<CustomerAddlInfo> {


	public CustomerAddlInfoRepository(Session session) {
		super(session);
	}

	public void upsert(CustomerAddlInfo item) throws DataException {
		if( item.getId() != null )
			updateEntity(item, "CustomerAddlInfo");
		else
			this.session.save("CustomerAddlInfo", item);
	}
	
	public void remove(CustomerAddlInfo item) throws DataException {
		this.session.delete("CustomerAddlInfo", item);
	}
	
	@SuppressWarnings("rawtypes")
	public List queryCustomerAddlInfoByCusID(String cusID) throws DataException {
		return this.session.getNamedNativeQuery("getCustomerAddlInfoByCusID")
							.setParameter("cusID", cusID)
							.list();
	}
}
