package com.mcredit.data.common.repository;

import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.common.entity.Partner;
import com.mcredit.data.repository.IUpsertRepository;

public class PartnerRepository extends BaseRepository implements IUpsertRepository<Partner> {

	public PartnerRepository(Session session) {
		super(session);
	}

	public void upsert(Partner item) {
		this.session.saveOrUpdate("Partner", item);
	}

	public void remove(Partner item) {
		this.session.delete("Partner", item);
	}
	
	@SuppressWarnings("unchecked")
	public List<Partner> findPartner() {
		return this.session.getNamedQuery("findPartner").list();
	}	
}