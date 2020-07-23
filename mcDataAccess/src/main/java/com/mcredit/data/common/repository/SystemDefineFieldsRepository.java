package com.mcredit.data.common.repository;

import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.common.entity.SystemDefineFields;
import com.mcredit.data.repository.IUpsertRepository;

public class SystemDefineFieldsRepository extends BaseRepository implements IUpsertRepository<SystemDefineFields> {

	public SystemDefineFieldsRepository(Session session) {
		super(session);
	}

	public void upsert(SystemDefineFields item) {
		this.session.saveOrUpdate("SystemDefineFields", item);
	}

	public void remove(SystemDefineFields item) {
		this.session.delete("SystemDefineFields", item);
	}
	
	public List<SystemDefineFields> findActiveSystemDefineFields() {
		return this.session.createQuery(" FROM " + SystemDefineFields.class.getName() + " where status = 'A' ORDER by category, codeTableValue ", SystemDefineFields.class).list();
	}

	public List<SystemDefineFields> findActiveSystemDefineFieldsOrderById() {
		return this.session.createQuery(" FROM " + SystemDefineFields.class.getName() + " where status = 'A' ORDER by id ", SystemDefineFields.class).list();
	}
}