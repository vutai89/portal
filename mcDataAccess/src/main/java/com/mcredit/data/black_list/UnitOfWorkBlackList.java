package com.mcredit.data.black_list;

import org.hibernate.Session;

import com.mcredit.data.BaseUnitOfWork;
import com.mcredit.data.HibernateBase;
import com.mcredit.data.black_list.entity.CustMonitor;
import com.mcredit.data.black_list.repository.CustMonitorRepository;
import com.mcredit.data.repository.IRepository;

public class UnitOfWorkBlackList extends BaseUnitOfWork {
	public UnitOfWorkBlackList() {
		super();
	}
	
	public UnitOfWorkBlackList(HibernateBase hibernateBase, Session session) {
		super(hibernateBase, session);
	}
	
	private IRepository<CustMonitor> _custMonitorRepository = null;
	
	public CustMonitorRepository createCustMonitorRepository() {
		if (_custMonitorRepository == null) {
			_custMonitorRepository = (IRepository<CustMonitor>) new CustMonitorRepository(this.session);
		}
		return (CustMonitorRepository) _custMonitorRepository;
	}
	
}
