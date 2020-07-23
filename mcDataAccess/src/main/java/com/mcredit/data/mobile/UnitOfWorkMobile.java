package com.mcredit.data.mobile;

import org.hibernate.Session;

import com.mcredit.data.BaseUnitOfWork;
import com.mcredit.data.HibernateBase;
import com.mcredit.data.customer.entity.CustomerCompanyInfo;
import com.mcredit.data.customer.repository.CustomerCompanyInfoRepository;
import com.mcredit.data.mobile.entity.CreditAppTrail;
import com.mcredit.data.mobile.entity.ExternalUserMapping;
import com.mcredit.data.mobile.entity.UplCreditAppDocument;
import com.mcredit.data.mobile.entity.UplCreditAppFiles;
import com.mcredit.data.mobile.entity.UplCreditAppRequest;
import com.mcredit.data.mobile.entity.UsersProfiles;
import com.mcredit.data.mobile.repository.ApprovalReportRepository;
import com.mcredit.data.mobile.repository.CaseRepository;
import com.mcredit.data.mobile.repository.CreditAppDocumentRepository;
import com.mcredit.data.mobile.repository.CreditAppTrailRepository;
import com.mcredit.data.mobile.repository.ExternalUserMappingRepository;
import com.mcredit.data.mobile.repository.ReportRepository;
import com.mcredit.data.mobile.repository.UplCreditAppFilesRepository;
import com.mcredit.data.mobile.repository.UplCreditAppRequestRepository;
import com.mcredit.data.mobile.repository.UsersProfilesRepository;
import com.mcredit.data.repository.IRepository;
import com.mcredit.data.user.entity.Employee;
import com.mcredit.data.user.repository.EmployeeRepository;

public class UnitOfWorkMobile extends BaseUnitOfWork {

	public UnitOfWorkMobile() {
		super();
	}
	public UnitOfWorkMobile(HibernateBase hibernateBase,Session session) {
		super(hibernateBase,session);
	}

	@SuppressWarnings("rawtypes")
	private IRepository _ruleReportRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _ruleApprovalReportRepository = null;
	
	private IRepository<UplCreditAppRequest> _caseRepository = null;
	private IRepository<UplCreditAppDocument> _creditAppDocumentRepository = null;
	private IRepository<ExternalUserMapping> _externalUserMappingRepository = null;
	private IRepository<UplCreditAppRequest> _uplCreditAppRequestRepository = null;
	private IRepository<UplCreditAppFiles> _uplCreditAppFilesRepository = null;
	private IRepository<CreditAppTrail> _creditAppTrailRepository = null;
	private IRepository<Employee> _employeeRepository = null;
	private IRepository<UsersProfiles> _usersProfilesRepository = null;
	
	public CaseRepository caseRepo() {

        if (_caseRepository == null) {
        	_caseRepository = new CaseRepository(this.session);
        }
        return (CaseRepository) _caseRepository;
    }
	
	public CreditAppDocumentRepository creditAppDocumentRepo() {

        if (_creditAppDocumentRepository == null) {
        	_creditAppDocumentRepository = new CreditAppDocumentRepository(this.session);
        }
        return (CreditAppDocumentRepository) _creditAppDocumentRepository;
    }
	
	public ExternalUserMappingRepository externalUserMappingRepo() {
		
		 if (_externalUserMappingRepository == null) {
			 _externalUserMappingRepository = new ExternalUserMappingRepository(this.session);
	        }
	        return (ExternalUserMappingRepository) _externalUserMappingRepository;
	}
	
	public UplCreditAppRequestRepository uplCreditAppRequestRepo() {
		
		 if (_uplCreditAppRequestRepository == null) {
			 _uplCreditAppRequestRepository = new UplCreditAppRequestRepository(this.session);
	        }
	        return (UplCreditAppRequestRepository) _uplCreditAppRequestRepository;
	}
	
	public UplCreditAppFilesRepository uplCreditAppFilesRepo() {
		
		 if (_uplCreditAppFilesRepository == null) {
		 _uplCreditAppFilesRepository = new UplCreditAppFilesRepository(this.session);
        }
	        return (UplCreditAppFilesRepository) _uplCreditAppFilesRepository;
	}
	
	public CreditAppTrailRepository creditAppTrailRepo() {
		
		if (_creditAppTrailRepository == null) {
			_creditAppTrailRepository = new CreditAppTrailRepository(this.session);
        }
        return (CreditAppTrailRepository) _creditAppTrailRepository;
	}
	
	public EmployeeRepository employeeRepo() {

        if (_employeeRepository == null) {
        	_employeeRepository = new EmployeeRepository(this.session);
        }

        return (EmployeeRepository) _employeeRepository;
    }

	public UsersProfilesRepository usersProfilesRepo() {

        if (_usersProfilesRepository == null) {
        	_usersProfilesRepository = new UsersProfilesRepository(this.session);
        }

        return (UsersProfilesRepository) _usersProfilesRepository;
    }
	
	@Override
    public void reset() {
        super.reset();
        _ruleReportRepository = new ReportRepository(this.session);
        _ruleApprovalReportRepository = new ApprovalReportRepository(this.session);

    }
}
