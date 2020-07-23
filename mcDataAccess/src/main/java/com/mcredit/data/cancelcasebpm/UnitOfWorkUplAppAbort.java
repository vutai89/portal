package com.mcredit.data.cancelcasebpm;

import org.hibernate.Session;

import com.mcredit.data.BaseUnitOfWork;
import com.mcredit.data.HibernateBase;
import com.mcredit.data.appraisal.entity.CreditAppraisalData;
import com.mcredit.data.appraisal.repository.CreditAppraisalDataRepository;
import com.mcredit.data.cancelcasebpm.entity.UplAppAutoAbort;
import com.mcredit.data.cancelcasebpm.repository.UplAppAutoAbortDataRepository;
import com.mcredit.data.repository.IRepository;

public class UnitOfWorkUplAppAbort extends BaseUnitOfWork {

	public UnitOfWorkUplAppAbort() {
		super();
	}
	
	public UnitOfWorkUplAppAbort(HibernateBase hibernateBase, Session session) {
		super(hibernateBase, session);
	}

	private IRepository<UplAppAutoAbort> _UplAppAutoAbortDataRepository = null;
	
	public UplAppAutoAbortDataRepository uplAppAutoAbortDataRepository() {
        if (_UplAppAutoAbortDataRepository == null) {
        	_UplAppAutoAbortDataRepository = new UplAppAutoAbortDataRepository(this.session);
        }
        return (UplAppAutoAbortDataRepository) _UplAppAutoAbortDataRepository;
    }

}
