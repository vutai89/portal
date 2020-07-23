package com.mcredit.data.audit;

import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseUnitOfWork;
import com.mcredit.data.HibernateBase;
import com.mcredit.data.audit.entity.AuditPaymentDebtCollection;
import com.mcredit.data.audit.entity.DebtCollection;
import com.mcredit.data.audit.entity.Disbursement;
import com.mcredit.data.audit.repository.AuditPDCRepository;
import com.mcredit.data.audit.repository.DebtCollectionRepository;
import com.mcredit.data.audit.repository.DisbursementRepository;
import com.mcredit.data.repository.IRepository;
import com.mcredit.model.dto.audit.OverviewResultDTO;

public class UnitOfWorkAudit extends BaseUnitOfWork {

	public UnitOfWorkAudit() {
		super();
	}

	public UnitOfWorkAudit(HibernateBase hibernateBase, Session session) {
		super(hibernateBase, session);
	}

	private IRepository<DebtCollection> _debtCollectionRepository = null;
	private IRepository<Disbursement> _disbursementRepository = null;
	private IRepository<AuditPaymentDebtCollection> _auditPDC = null;

	public DebtCollectionRepository debtCollectionRepository() {
		if (_debtCollectionRepository == null) {
			_debtCollectionRepository = new DebtCollectionRepository(this.session);
		}
		return (DebtCollectionRepository) _debtCollectionRepository;
	}

	public DisbursementRepository disbursementRepository() {
		if (_disbursementRepository == null) {
			_disbursementRepository = new DisbursementRepository(this.session);
		}
		return (DisbursementRepository) _disbursementRepository;
	}
	
	public AuditPDCRepository auditPDCRepository() {
		if (_auditPDC == null) {
			_auditPDC = new AuditPDCRepository(this.session);
		}
		return (AuditPDCRepository) _auditPDC;
	}
}
