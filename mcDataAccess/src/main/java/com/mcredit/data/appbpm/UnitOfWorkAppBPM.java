package com.mcredit.data.appbpm;

import org.hibernate.Session;

import com.mcredit.data.BaseUnitOfWork;
import com.mcredit.data.HibernateBase;
import com.mcredit.data.appbpm.repository.AppReasonBPMRepository;
import com.mcredit.data.appbpm.repository.AppStatusBPMRepository;
import com.mcredit.data.repository.IRepository;

public class UnitOfWorkAppBPM extends BaseUnitOfWork {

	@SuppressWarnings("rawtypes")
	private IRepository _appStatusBPMRepo = null;
	
	@SuppressWarnings("rawtypes")
	private IRepository _appReasonBPMRepo = null;

	public UnitOfWorkAppBPM() {
		super();
	}

	public UnitOfWorkAppBPM(HibernateBase hibernateBase, Session session) {
		super(hibernateBase, session);
	}

	public AppStatusBPMRepository createAppStatusBPMRepo() {
		if (_appStatusBPMRepo == null)
			_appStatusBPMRepo = new AppStatusBPMRepository(this.session);
		return (AppStatusBPMRepository) _appStatusBPMRepo;
	}
	
	public AppReasonBPMRepository createAppReasonBPMRepo() {
		if (_appReasonBPMRepo == null)
			_appReasonBPMRepo = new AppReasonBPMRepository(this.session);
		return (AppReasonBPMRepository) _appReasonBPMRepo;
	}

}
