package com.mcredit.data.telesale;

import org.hibernate.Session;

import com.mcredit.data.BaseUnitOfWork;
import com.mcredit.data.HibernateBase;
import com.mcredit.data.common.repository.CodeTableRepository;
import com.mcredit.data.common.repository.MessageLogRepository;
import com.mcredit.data.common.repository.ProductRepository;
import com.mcredit.data.mobile.repository.UplCreditAppRequestRepository;
import com.mcredit.data.repository.IRepository;
import com.mcredit.data.telesale.repository.ProspectManagerRepository;
import com.mcredit.data.telesale.repository.TeamMemberRepository;
import com.mcredit.data.telesale.repository.TeamRepository;
import com.mcredit.data.telesale.repository.TelesaleRepository;
import com.mcredit.data.telesale.repository.UplCustomerHistoryRepository;
import com.mcredit.data.telesale.repository.UplCustomerRepository;
import com.mcredit.data.telesale.repository.UplDetailRepository;
import com.mcredit.data.telesale.repository.UplMasterRepository;
import com.mcredit.data.user.repository.EmployeeRepository;

public class UnitOfWorkTelesale extends BaseUnitOfWork {

	public UnitOfWorkTelesale(HibernateBase hibernateBase,Session session) {
		super(hibernateBase,session);
	}
	
	public UnitOfWorkTelesale() {
		super();
	}
	
	@SuppressWarnings("rawtypes")
	private IRepository _uplMasterRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _uplDetailRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _uplCustomerRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _uplCustomerHistoryRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _teamRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _teamMemberRepository = null;

	@SuppressWarnings("rawtypes")
	private ProspectManagerRepository _prospectManagerRepository = null;
	
	@SuppressWarnings("rawtypes")
	private TelesaleRepository _telesaleRepository = null;
	
	@SuppressWarnings("rawtypes")
	private ProductRepository _productRepository = null;
	
	@SuppressWarnings("rawtypes")
	private CodeTableRepository _codeTableRepository = null;
	
	@SuppressWarnings("rawtypes")
	private UplCreditAppRequestRepository _uplCreditAppRequestRepository = null;
	
	@SuppressWarnings("rawtypes")
	private EmployeeRepository _employeeRepository = null;
	
	@SuppressWarnings("rawtypes")
	private MessageLogRepository _messageLogRepository = null;
	
	@SuppressWarnings("rawtypes")
	public ProspectManagerRepository prospectManagerRepo() {

		if (_prospectManagerRepository == null)
			_prospectManagerRepository = new ProspectManagerRepository(this.session);

		return (ProspectManagerRepository) _prospectManagerRepository;
	}

	public UplMasterRepository uplMasterRepo() {

		if (_uplMasterRepository == null)
			_uplMasterRepository = new UplMasterRepository(this.session);

		return (UplMasterRepository) _uplMasterRepository;
	}

	public TeamRepository teamRepo() {

		if (_teamRepository == null)
			_teamRepository = new TeamRepository(this.session);

		return (TeamRepository) _teamRepository;
	}

	public TeamMemberRepository teamMemberRepo() {

		if (_teamMemberRepository == null)
			_teamMemberRepository = new TeamMemberRepository(this.session);

		return (TeamMemberRepository) _teamMemberRepository;
	}

	public UplDetailRepository uplDetailRepo() {

		if (_uplDetailRepository == null)
			_uplDetailRepository = new UplDetailRepository(this.session);

		return (UplDetailRepository) _uplDetailRepository;
	}

	public UplCustomerRepository uplCustomerRepo() {

		if (_uplCustomerRepository == null)
			_uplCustomerRepository = new UplCustomerRepository(this.session);

		return (UplCustomerRepository) _uplCustomerRepository;
	}

	public UplCustomerHistoryRepository uplCustomerHistoryRepo() {

		if (_uplCustomerHistoryRepository == null)
			_uplCustomerHistoryRepository = new UplCustomerHistoryRepository(this.session);

		return (UplCustomerHistoryRepository) _uplCustomerHistoryRepository;
	}
	
	public TelesaleRepository telesaleRepository() {

		if (_telesaleRepository == null)
			_telesaleRepository = new TelesaleRepository(this.session);

		return (TelesaleRepository) _telesaleRepository;
	}
	
	public ProductRepository productRepository() {

		if (_productRepository == null)
			_productRepository = new ProductRepository(this.session);

		return (ProductRepository) _productRepository;
	}
	
	public CodeTableRepository codeTableRepository() {

		if (_codeTableRepository == null)
			_codeTableRepository = new CodeTableRepository(this.session);

		return (CodeTableRepository) _codeTableRepository;
	}
	
	public UplCreditAppRequestRepository uplCreditAppRequestRepository() {

		if (_uplCreditAppRequestRepository == null)
			_uplCreditAppRequestRepository = new UplCreditAppRequestRepository(this.session);

		return (UplCreditAppRequestRepository) _uplCreditAppRequestRepository;
	}
	
	public EmployeeRepository employeeRepository() {

		if (_employeeRepository == null)
			_employeeRepository = new EmployeeRepository(this.session);

		return (EmployeeRepository) _employeeRepository;
	}
	
	public MessageLogRepository messageLogRepository() {

		if (_messageLogRepository == null)
			_messageLogRepository = new MessageLogRepository(this.session);

		return (MessageLogRepository) _messageLogRepository;
	}

	@Override
	public void reset() {
		super.reset();
		_uplMasterRepository = null;
		_uplDetailRepository = null;
		_teamMemberRepository = null;
		_teamMemberRepository = null;
		_uplCustomerRepository = null;
		_uplCustomerHistoryRepository = null;
		_prospectManagerRepository = null;
		_productRepository = null;
		_codeTableRepository = null;

		_uplMasterRepository = new UplMasterRepository(this.session);
		_uplDetailRepository = new UplDetailRepository(this.session);
		_teamMemberRepository = new TeamRepository(this.session);
		_teamMemberRepository = new TeamMemberRepository(this.session);
		_uplCustomerRepository = new UplCustomerRepository(this.session);
		_uplCustomerHistoryRepository = new UplCustomerHistoryRepository(this.session);
		_prospectManagerRepository = new ProspectManagerRepository(this.session);
		_telesaleRepository = new TelesaleRepository(this.session);
		_productRepository = new ProductRepository(this.session);
		_codeTableRepository= new CodeTableRepository(this.session);
		_uplCreditAppRequestRepository = new UplCreditAppRequestRepository(this.session);
		_employeeRepository = new EmployeeRepository(this.session);
		_messageLogRepository = new MessageLogRepository(this.session);
	}
}
