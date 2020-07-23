package com.mcredit.data.pcb;

import org.hibernate.Session;

import com.mcredit.data.BaseUnitOfWork;
import com.mcredit.data.HibernateBase;
import com.mcredit.data.pcb.entity.CreditBureauData;
import com.mcredit.data.pcb.repository.CreditBureauDataRepository;
import com.mcredit.data.repository.IRepository;

public class UnitOfWorkPCB extends BaseUnitOfWork {

	public UnitOfWorkPCB() {
		super();
	}
	
	public UnitOfWorkPCB(HibernateBase hibernateBase, Session session) {
		super(hibernateBase, session);
	}
	
	private IRepository<CreditBureauData> _creditBureauDataRepository = null;
	
	public CreditBureauDataRepository creditBureauDataRepository() {
        if (_creditBureauDataRepository == null) {
        	_creditBureauDataRepository = new CreditBureauDataRepository(this.session);
        }
        return (CreditBureauDataRepository) _creditBureauDataRepository;
    }
}
