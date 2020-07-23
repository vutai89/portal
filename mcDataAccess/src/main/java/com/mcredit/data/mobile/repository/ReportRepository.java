package com.mcredit.data.mobile.repository;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.mobile.entity.DataReport;
import com.mcredit.data.repository.IUpdateRepository;
import com.mcredit.data.repository.IUpsertRepository;

public class ReportRepository extends BaseRepository implements IUpsertRepository<DataReport>,IUpdateRepository<DataReport>  {


	public ReportRepository(Session session) {
		super(session);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(DataReport item) {
		// TODO Auto-generated method stub
		this.session.saveOrUpdate("DataReport", item);
	}

	@Override
	public void upsert(DataReport item) {
		// TODO Auto-generated method stub
		this.session.saveOrUpdate("DataReport", item);
	}
	
}
