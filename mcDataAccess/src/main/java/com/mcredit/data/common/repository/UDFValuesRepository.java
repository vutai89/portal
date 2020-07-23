package com.mcredit.data.common.repository;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.common.entity.UDFValues;
import com.mcredit.data.repository.IUpdateRepository;
import com.mcredit.data.repository.IUpsertRepository;

public class UDFValuesRepository extends BaseRepository implements IUpdateRepository<UDFValues>, IUpsertRepository<UDFValues> {

	public UDFValuesRepository(Session session) {
		super(session);
	}

	public void upsert(UDFValues item) {
		this.session.saveOrUpdate("UDFValues", item);
	}

	public void update(UDFValues item) {
		this.session.update("UDFValues", item);
	}

	public void remove(UDFValues item) {
		this.session.delete("UDFValues", item);
	}

}
