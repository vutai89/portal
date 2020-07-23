package com.mcredit.data.mobile;

import org.hibernate.Session;

import com.mcredit.data.BaseUnitOfWork;
import com.mcredit.data.HibernateBase;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.common.entity.Product;
import com.mcredit.data.common.repository.MessageLogRepository;
import com.mcredit.data.customer.entity.CustomerCompanyInfo;
import com.mcredit.data.customer.repository.CustomerCompanyInfoRepository;
import com.mcredit.data.mobile.entity.CreditAppTrail;
import com.mcredit.data.mobile.entity.ExternalUserMapping;
import com.mcredit.data.mobile.entity.UplCreditAppDocument;
import com.mcredit.data.mobile.entity.UplCreditAppFiles;
import com.mcredit.data.mobile.entity.UplCreditAppRequest;
import com.mcredit.data.mobile.entity.UsersProfiles;
import com.mcredit.data.mobile.reporsitorytest.MessageLogRepositoryTest;
import com.mcredit.data.mobile.reporsitorytest.ProductRepositoryTest;
import com.mcredit.data.mobile.reporsitorytest.UplCreditAppFilesRepositoryTest;
import com.mcredit.data.mobile.reporsitorytest.UplCreditAppRequestRepositoryTest;
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
import com.mcredit.data.user.entity.Users;
import com.mcredit.data.user.repository.EmployeeRepository;
import com.mcredit.data.user.repository.UsersRepository;

public class UnitOfWorkMobileTest extends BaseUnitOfWork  {

	public UnitOfWorkMobileTest() {
		super();
	}
	
	public UnitOfWorkMobileTest(HibernateBase hibernateBase,Session session) {
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
	private IRepository<CustomerCompanyInfo> _custCompanyInfroRepository = null;
	private IRepository<UsersProfiles> _usersProfilesRepository = null;
	private IRepository<MessageLog> _messageLogRepository = null;
	private IRepository<UplCreditAppRequest> _uplCreditAppRequestRepoTest = null;
	private IRepository<Users> _usersRepository = null;
	private IRepository<UplCreditAppFiles> _uplCreditAppFileRepoTest = null;
	private IRepository<MessageLog> _messageLogTest = null;
	private IRepository<Product> _productTest = null;
	
	public UsersRepository usersRepository() {

        if (_usersRepository == null) {
        	_usersRepository = new UsersRepository(this.session);
        }
        return (UsersRepository) _usersRepository;
    }
	
	public ReportRepository reportRepository() {

        if (_ruleReportRepository == null) {
        	_ruleReportRepository = new ReportRepository(this.session);
        }

        return (ReportRepository) _ruleReportRepository;
    }
	
	public ApprovalReportRepository approvalReportRepository() {

        if (_ruleApprovalReportRepository == null) {
        	_ruleApprovalReportRepository = new ApprovalReportRepository(this.session);
        }

        return (ApprovalReportRepository) _ruleApprovalReportRepository;
    }
	
	public CaseRepository caseRepository() {

        if (_caseRepository == null) {
        	_caseRepository = new CaseRepository(this.session);
        }
        return (CaseRepository) _caseRepository;
    }
	
	public CreditAppDocumentRepository creditAppDocumentRepository() {

        if (_creditAppDocumentRepository == null) {
        	_creditAppDocumentRepository = new CreditAppDocumentRepository(this.session);
        }
        return (CreditAppDocumentRepository) _creditAppDocumentRepository;
    }
	
	public ExternalUserMappingRepository externalUserMappingRepository() {
		
		 if (_externalUserMappingRepository == null) {
			 _externalUserMappingRepository = new ExternalUserMappingRepository(this.session);
	        }
	        return (ExternalUserMappingRepository) _externalUserMappingRepository;
	}
	
	public UplCreditAppRequestRepository uplCreditAppRequestRepository() {
		
		 if (_uplCreditAppRequestRepository == null) {
			 _uplCreditAppRequestRepository = new UplCreditAppRequestRepository(this.session);
	        }
	        return (UplCreditAppRequestRepository) _uplCreditAppRequestRepository;
	}
	
	public UplCreditAppRequestRepositoryTest uplCreditAppRequestRepositoryTest() {
		
		 if (_uplCreditAppRequestRepoTest == null) {
			 _uplCreditAppRequestRepoTest = new UplCreditAppRequestRepositoryTest(this.session);
	        }
	        return (UplCreditAppRequestRepositoryTest) _uplCreditAppRequestRepoTest;
	}
	
	public MessageLogRepositoryTest messageLogRepositoryTest() {
		
		 if (_messageLogTest == null) {
			 _messageLogTest = new MessageLogRepositoryTest(this.session);
	        }
	        return (MessageLogRepositoryTest) _messageLogTest;
	}
	
	public UplCreditAppFilesRepositoryTest uplCreditAppFilesRepositoryTest() {
		
		 if (_uplCreditAppFileRepoTest == null) {
			 _uplCreditAppFileRepoTest = new UplCreditAppFilesRepositoryTest(this.session);
	        }
	        return (UplCreditAppFilesRepositoryTest) _uplCreditAppFileRepoTest;
	}
	
	public UplCreditAppFilesRepository uplCreditAppFilesRepository() {
		
		 if (_uplCreditAppFilesRepository == null) {
		 _uplCreditAppFilesRepository = new UplCreditAppFilesRepository(this.session);
        }
	        return (UplCreditAppFilesRepository) _uplCreditAppFilesRepository;
	}
	
	public CreditAppTrailRepository creditAppTrailRepository() {
		
		if (_creditAppTrailRepository == null) {
			_creditAppTrailRepository = new CreditAppTrailRepository(this.session);
        }
        return (CreditAppTrailRepository) _creditAppTrailRepository;
	}
	
	public EmployeeRepository employeeRepository() {

        if (_employeeRepository == null) {
        	_employeeRepository = new EmployeeRepository(this.session);
        }

        return (EmployeeRepository) _employeeRepository;
    }
	
	public CustomerCompanyInfoRepository customerCompanyInforRepository() {

		if (_custCompanyInfroRepository == null) {
			_custCompanyInfroRepository = new CustomerCompanyInfoRepository(this.session);
		}
		return (CustomerCompanyInfoRepository) _custCompanyInfroRepository;
	}

	public UsersProfilesRepository usersProfilesRepository() {

        if (_usersProfilesRepository == null) {
        	_usersProfilesRepository = new UsersProfilesRepository(this.session);
        }

        return (UsersProfilesRepository) _usersProfilesRepository;
    }
	
	public MessageLogRepository messageLogRepository() {

		if (_messageLogRepository == null) {
			_messageLogRepository = new MessageLogRepository(this.session);
		}
		return (MessageLogRepository) _messageLogRepository;
	}
	
	public ProductRepositoryTest productRepository() {

		if (_productTest == null) {
			_productTest = new ProductRepositoryTest(this.session);
		}
		return (ProductRepositoryTest) _productTest;
	}
	
	@Override
    public void reset() {
        super.reset();
        _ruleReportRepository = new ReportRepository(this.session);
        _ruleApprovalReportRepository = new ApprovalReportRepository(this.session);

    }
}
