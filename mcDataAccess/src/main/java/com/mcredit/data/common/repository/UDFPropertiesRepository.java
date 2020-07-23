package com.mcredit.data.common.repository;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.common.entity.UDFProperties;
import com.mcredit.data.repository.IUpdateRepository;
import com.mcredit.data.repository.IUpsertRepository;

public class UDFPropertiesRepository extends BaseRepository implements IUpdateRepository<UDFProperties>, IUpsertRepository<UDFProperties> {

	public UDFPropertiesRepository(Session session) {
		super(session);
	}

	public void upsert(UDFProperties item) {
		this.session.saveOrUpdate("UDFProperties", item);
	}

	public void update(UDFProperties item) {
		this.session.update("UDFProperties", item);
	}

	public void remove(UDFProperties item) {
		this.session.delete("UDFProperties", item);
	}

}
