package com.mcredit.data.customer.repository;

import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.DataException;
import com.mcredit.data.customer.entity.CustomerIdentity;
import com.mcredit.data.repository.IUpsertRepository;

public class CustomerIdentityRepository extends BaseRepository implements
		IUpsertRepository<CustomerIdentity> {

	public CustomerIdentityRepository(Session session) {
		super(session);
	}

	public void upsert(CustomerIdentity item) throws DataException {
		if( item.getId() != null )
			updateEntity(item, "CustomerIdentity");
		else
			this.session.save("CustomerIdentity", item);
	}
	
	public void remove(CustomerIdentity custIdentity) throws DataException {
		this.session.delete("CustomerIdentity", custIdentity);
	}
	
	@SuppressWarnings("rawtypes")
	public List findAllCustomerIdentity(int page, int rowsPerPage) throws DataException {
		return this.session.getNamedQuery("findAllCustomerIdentity")
				.setFirstResult((page - 1) * rowsPerPage)
				.setMaxResults(rowsPerPage).list();
	}

	public CustomerIdentity findCustomerIdentityById(long customerIdentityId) throws DataException {
		return (CustomerIdentity) this.session.getNamedQuery("findCustomerIdentityById")
				.setParameter("custIdentityId", customerIdentityId)
				.uniqueResult();
	}
}
