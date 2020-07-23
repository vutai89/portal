package com.mcredit.data.warehouse.repository;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.repository.IAddRepository;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.data.warehouse.entity.WhMapDocCode;

public class WhMapDocCodeRepository extends BaseRepository implements IUpsertRepository<WhMapDocCode>,IAddRepository<WhMapDocCode> {

	public WhMapDocCodeRepository(Session session) {
		super(session);
	}

	public void add(WhMapDocCode item) {
		this.session.save(item);
	}
	
	public void upsert(WhMapDocCode item) {
		this.session.saveOrUpdate("WhMapDocCode", item);
	}
}
