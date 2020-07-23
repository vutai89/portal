package com.mcredit.data.credit;

import org.hibernate.Session;

import com.mcredit.data.HibernateBase;
import com.mcredit.data.BaseUnitOfWork;
import com.mcredit.data.credit.repository.CreditApplicationAdditionalRepository;
import com.mcredit.data.credit.repository.CreditApplicationAppraisalRepository;
import com.mcredit.data.credit.repository.CreditApplicationBPMRepository;
import com.mcredit.data.credit.repository.CreditApplicationLoanManagementRepository;
import com.mcredit.data.credit.repository.CreditApplicationOutstandingRepository;
import com.mcredit.data.credit.repository.CreditApplicationRequestRepository;
import com.mcredit.data.credit.repository.CreditApplicationSignatureRepository;
import com.mcredit.data.repository.IRepository;

public class UnitOfWorkCredit extends BaseUnitOfWork {
	public UnitOfWorkCredit() {
		super();
	}
	public UnitOfWorkCredit(HibernateBase hibernateBase,Session session) {
		super(hibernateBase,session);
	}
	
	public void flush() {
		this.session.flush();
		this.session.clear();
	}
	
	@SuppressWarnings("rawtypes")
	private IRepository _creditApplicationAppraisalRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _creditApplicationBPMRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _creditApplicationLoanManagementRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _creditApplicationRequestRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _creditApplicationAdditionalRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _creditApplicationOutstandingRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _creditApplicationSignatureRepository = null;

	public CreditApplicationAppraisalRepository creditApplicationAppraisalRepo() {
		if (_creditApplicationAppraisalRepository == null)
			_creditApplicationAppraisalRepository = new CreditApplicationAppraisalRepository(this.session);
		return (CreditApplicationAppraisalRepository) _creditApplicationAppraisalRepository;
	}

	public CreditApplicationBPMRepository creditApplicationBPMRepo() {

		if (_creditApplicationBPMRepository == null)
			_creditApplicationBPMRepository = new CreditApplicationBPMRepository(this.session);

		return (CreditApplicationBPMRepository) _creditApplicationBPMRepository;
	}

	public CreditApplicationLoanManagementRepository creditApplicationLoanManagementRepo() {

		if (_creditApplicationLoanManagementRepository == null)
			_creditApplicationLoanManagementRepository = new CreditApplicationLoanManagementRepository(this.session);

		return (CreditApplicationLoanManagementRepository) _creditApplicationLoanManagementRepository;
	}

	public CreditApplicationRequestRepository creditApplicationRequestRepo() {

		if (_creditApplicationRequestRepository == null)
			_creditApplicationRequestRepository = new CreditApplicationRequestRepository(this.session);

		return (CreditApplicationRequestRepository) _creditApplicationRequestRepository;
	}

	public CreditApplicationAdditionalRepository creditApplicationAdditionalRepo() {

		if (_creditApplicationAdditionalRepository == null)
			_creditApplicationAdditionalRepository = new CreditApplicationAdditionalRepository(this.session);

		return (CreditApplicationAdditionalRepository) _creditApplicationAdditionalRepository;
	}
	public CreditApplicationOutstandingRepository creditApplicationOutstandingRepo() {

		if (_creditApplicationOutstandingRepository == null)
			_creditApplicationOutstandingRepository = new CreditApplicationOutstandingRepository(this.session);

		return (CreditApplicationOutstandingRepository) _creditApplicationOutstandingRepository;
	}

	public CreditApplicationSignatureRepository creditApplicationSignatureRepo() {

		if (_creditApplicationSignatureRepository == null)
			_creditApplicationSignatureRepository = new CreditApplicationSignatureRepository(this.session);

		return (CreditApplicationSignatureRepository) _creditApplicationSignatureRepository;
	}
}
