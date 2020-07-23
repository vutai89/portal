package com.mcredit.data.common.repository;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.data.common.entity.AllocateHistory;

public class AllocateHistoryRepository extends BaseRepository implements IUpsertRepository<AllocateHistory> {

	public AllocateHistoryRepository(Session session) {
		super(session);
	}

	public void upsert(AllocateHistory item) {		
		this.session.saveOrUpdate("AllocateHistory", item);
		this.session.flush();
		this.session.clear();
	}

}
