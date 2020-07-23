package com.mcredit.data.common.repository;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.common.entity.UDFDefinition;
import com.mcredit.data.repository.IUpdateRepository;
import com.mcredit.data.repository.IUpsertRepository;

public class UDFDefinitionRepository extends BaseRepository implements IUpdateRepository<UDFDefinition>, IUpsertRepository<UDFDefinition> {

	public UDFDefinitionRepository(Session session) {
		super(session);
	}

	public void upsert(UDFDefinition item) {
		this.session.saveOrUpdate("UDFDefinition", item);
	}

	public void update(UDFDefinition item) {
		this.session.update("UDFDefinition", item);
	}

	public void remove(UDFDefinition item) {
		this.session.delete("UDFDefinition", item);
	}

}
