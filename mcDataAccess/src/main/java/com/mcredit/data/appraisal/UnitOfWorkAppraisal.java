package com.mcredit.data.appraisal;

import org.hibernate.Session;

import com.mcredit.data.BaseUnitOfWork;
import com.mcredit.data.HibernateBase;
import com.mcredit.data.appraisal.entity.CreditAppraisalData;
import com.mcredit.data.appraisal.repository.CreditAppraisalDataRepository;
import com.mcredit.data.repository.IRepository;

public class UnitOfWorkAppraisal extends BaseUnitOfWork {

	public UnitOfWorkAppraisal() {
		super();
	}
	
	public UnitOfWorkAppraisal(HibernateBase hibernateBase, Session session) {
		super(hibernateBase, session);
	}
	
	private IRepository<CreditAppraisalData> _creditAppraisalDataRepository = null;
	
	public CreditAppraisalDataRepository creditAppraisalDataRepository() {
        if (_creditAppraisalDataRepository == null) {
        	_creditAppraisalDataRepository = new CreditAppraisalDataRepository(this.session);
        }
        return (CreditAppraisalDataRepository) _creditAppraisalDataRepository;
    }
}
