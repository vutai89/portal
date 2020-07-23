package com.mcredit.data.mobile.repository;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.mobile.entity.ApprovalReport;
import com.mcredit.data.repository.IUpdateRepository;
import com.mcredit.data.repository.IUpsertRepository;

public class ApprovalReportRepository extends BaseRepository implements IUpsertRepository<ApprovalReport>,IUpdateRepository<ApprovalReport>  {


	public ApprovalReportRepository(Session session) {
		super(session);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(ApprovalReport item) {
		// TODO Auto-generated method stub
		this.session.saveOrUpdate("ApprovalReport", item);
	}

	@Override
	public void upsert(ApprovalReport item) {
		// TODO Auto-generated method stub
		this.session.saveOrUpdate("ApprovalReport", item);
	}
}
