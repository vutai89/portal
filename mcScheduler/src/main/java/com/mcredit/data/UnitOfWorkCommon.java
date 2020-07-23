package com.mcredit.data;

import org.hibernate.Session;

import com.mcredit.data.HibernateBase;
import com.mcredit.data.entity.SchedulerInstance;
import com.mcredit.data.repository.SchedulerRepository;

public class UnitOfWorkCommon {
	private HibernateBase hibernateBase;
	private Session session;
	
	private SchedulerRepository _schedulerRepository =  null;
	
	public SchedulerRepository schedulerRepository() {

		if (_schedulerRepository == null)
			_schedulerRepository = new SchedulerRepository(this.session);

		return _schedulerRepository;
	}

	public UnitOfWorkCommon() {
		if (this.hibernateBase == null) {
			hibernateBase = new HibernateBase();
		}
		if (this.session == null) {
			this.session = hibernateBase.getSession();
		}
	}

	public void start() {
		this.hibernateBase.startTransaction();
	}

	public void commit() {
		if (this.session == null) {
			return;
		} else {
			this.hibernateBase.endTransaction();
		}
	}

	public void rollback() {
		if (this.session == null) {
			return;
		} else {
			this.hibernateBase.rollbackTransaction();
		}
	}
	
	public final SchedulerInstance get(long id) {
		return (SchedulerInstance) this.hibernateBase.get(SchedulerInstance.class, id);
	}

}
