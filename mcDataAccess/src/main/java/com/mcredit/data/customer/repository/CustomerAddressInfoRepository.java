package com.mcredit.data.customer.repository;

import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.DataException;
import com.mcredit.data.customer.entity.CustomerAddressInfo;
import com.mcredit.data.repository.IUpsertRepository;

public class CustomerAddressInfoRepository extends BaseRepository implements
		IUpsertRepository<CustomerAddressInfo> {

	public CustomerAddressInfoRepository(Session session) {
		super(session);
	}

	public void add(CustomerAddressInfo item) throws DataException {
	}

	public void update(CustomerAddressInfo item) throws DataException {
	}

	public void upsert(CustomerAddressInfo item) throws DataException {
		if( item.getId() != null )
			updateEntity(item, "CustomerAddressInfo");
		else
			this.session.save("CustomerAddressInfo", item);
	}
	
	public Integer findNextAddressOrder(Long custId, Integer addrType) {
		Integer result = null;
		List<?> lst = this.session.getNamedQuery("CustomerAddressInfo.nextAddrOrder")
				.setParameter("custId", custId)
				.setParameter("addrType", addrType).list();
		if( lst!=null && lst.size()>0 )
			result = (Integer) lst.get(0);
		return result;
	}
	
	public void remove(CustomerAddressInfo item) throws DataException {
		this.session.delete("CustomerAddressInfo", item);
	}
}
