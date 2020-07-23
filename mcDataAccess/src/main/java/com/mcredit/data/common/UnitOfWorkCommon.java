package com.mcredit.data.common;

import org.hibernate.Session;

import com.mcredit.data.BaseUnitOfWork;
import com.mcredit.data.HibernateBase;
import com.mcredit.data.common.entity.SendEmail;
import com.mcredit.data.common.repository.AllocateHistoryRepository;
import com.mcredit.data.common.repository.AllocationDetailRepository;
import com.mcredit.data.common.repository.AllocationMasterRepository;
import com.mcredit.data.common.repository.ArchMessageLogRepository;
import com.mcredit.data.common.repository.AuditDataChangeRepository;
import com.mcredit.data.common.repository.CalendarRepository;
import com.mcredit.data.common.repository.CodeTableRepository;
import com.mcredit.data.common.repository.CommoditiesRepository;
import com.mcredit.data.common.repository.HealthCheckRepository;
import com.mcredit.data.common.repository.MappingHierarchyRepository;
import com.mcredit.data.common.repository.MenuRepository;
import com.mcredit.data.common.repository.MessageLogRepository;
import com.mcredit.data.common.repository.MessageTaskRepository;
import com.mcredit.data.common.repository.NotificationTemplateRepository;
import com.mcredit.data.common.repository.ParametersRepository;
import com.mcredit.data.common.repository.PartnerRepository;
import com.mcredit.data.common.repository.ProductRepository;
import com.mcredit.data.common.repository.SendEmailRepository;
import com.mcredit.data.common.repository.SystemDefineFieldsRepository;
import com.mcredit.data.common.repository.UDFDefinitionRepository;
import com.mcredit.data.common.repository.UDFPropertiesRepository;
import com.mcredit.data.common.repository.UDFValuesRepository;
import com.mcredit.data.common.repository.UserCommonRepository;
import com.mcredit.data.customer.repository.CustomerCompanyInfoRepository;
import com.mcredit.data.mobile.repository.DocumentRepository;
import com.mcredit.data.product.repository.CommodityRepository;
import com.mcredit.data.repository.IRepository;
import com.mcredit.data.telesale.repository.UplDetailRepository;
import com.mcredit.data.telesale.repository.UplMasterRepository;

public class UnitOfWorkCommon extends BaseUnitOfWork {

	public UnitOfWorkCommon() {
		super();
	}

	public UnitOfWorkCommon(HibernateBase hibernateBase, Session session) {
		super(hibernateBase, session);
	}

	@SuppressWarnings("rawtypes")
	private IRepository _commoditiesRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _messageLogRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _archMessageLogRepository = null;	
	@SuppressWarnings("rawtypes")
	private IRepository _messageTaskRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _codeTableRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _partnerRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _parametersRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _productRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _notificationTemplateRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _calendarRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _systemDefineFieldsRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _menuRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _allocationDetailRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _allocationMasterRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _allocateHistoryRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _udfDefinitionRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _udfPropertiesRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _udfValuesRepository = null;
	
	@SuppressWarnings("rawtypes")
	private IRepository _customerCompanyInfoRepository = null;
	
	@SuppressWarnings("rawtypes")
	private IRepository _userCommonRepository = null;
	
	@SuppressWarnings("rawtypes")
	private IRepository _notificationRepository = null;
	
	private IRepository<SendEmail> _sendEmailRepository = null;
	
	@SuppressWarnings("rawtypes")
	private IRepository _healthRepository = null;
	
	@SuppressWarnings("rawtypes")
	private IRepository _documentRepository = null;
	
	@SuppressWarnings("rawtypes")
	private IRepository _commondityRepository = null;
	
	@SuppressWarnings("rawtypes")
	private IRepository _mappingHierarchy = null;
	
	@SuppressWarnings("rawtypes")
	private IRepository _auditDataChangeRepository = null;
	
	@SuppressWarnings("rawtypes")
	private IRepository _uplMasterRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _uplDetailRepository = null;

	public CodeTableRepository codeTableRepo() {

		if (_codeTableRepository == null)
			_codeTableRepository = new CodeTableRepository(this.session);

		return (CodeTableRepository) _codeTableRepository;
	}

	public AuditDataChangeRepository auditDataChangeRepo() {

		if (_auditDataChangeRepository == null)
			_auditDataChangeRepository = new AuditDataChangeRepository(this.session);
		return (AuditDataChangeRepository) _auditDataChangeRepository;
	}

	public PartnerRepository partnerRepo() {

		if (_partnerRepository == null)
			_partnerRepository = new PartnerRepository(this.session);

		return (PartnerRepository) _partnerRepository;
	}

	public ParametersRepository parametersRepo() {

		if (_parametersRepository == null)
			_parametersRepository = new ParametersRepository(this.session);

		return (ParametersRepository) _parametersRepository;
	}

	public CommoditiesRepository commoditiesRepo() {

		if (_commoditiesRepository == null)
			_commoditiesRepository = new CommoditiesRepository(this.session);

		return (CommoditiesRepository) _commoditiesRepository;
	}

	public MessageLogRepository messageLogRepo() {

		if (_messageLogRepository == null)
			_messageLogRepository = new MessageLogRepository(this.session);

		return (MessageLogRepository) _messageLogRepository;
	}
	
	public ArchMessageLogRepository archMessageLogRepo() {

		if (_archMessageLogRepository == null)
			_archMessageLogRepository = new ArchMessageLogRepository(this.session);

		return (ArchMessageLogRepository) _archMessageLogRepository;
	}

	public MessageTaskRepository messageTaskRepo() {

		if (_messageTaskRepository == null)
			_messageTaskRepository = new MessageTaskRepository(this.session);

		return (MessageTaskRepository) _messageTaskRepository;
	}

	public ProductRepository productTaskRepo() {

		if (_productRepository == null)
			_productRepository = new ProductRepository(this.session);

		return (ProductRepository) _productRepository;
	}

	public NotificationTemplateRepository notificationTemplateRepo() {

		if (_notificationTemplateRepository == null)
			_notificationTemplateRepository = new NotificationTemplateRepository(this.session);

		return (NotificationTemplateRepository) _notificationTemplateRepository;
	}

	public CalendarRepository calendarRepo() {

		if (_calendarRepository == null)
			_calendarRepository = new CalendarRepository(this.session);

		return (CalendarRepository) _calendarRepository;
	}

	public SystemDefineFieldsRepository systemDefineFieldsRepo() {

		if (_systemDefineFieldsRepository == null)
			_systemDefineFieldsRepository = new SystemDefineFieldsRepository(this.session);

		return (SystemDefineFieldsRepository) _systemDefineFieldsRepository;
	}

	public MenuRepository menuRepo() {

		if (_menuRepository == null)
			_menuRepository = new MenuRepository(this.session);

		return (MenuRepository) _menuRepository;
	}
	
	public AllocationMasterRepository allocationMasterRepo() {

		if (_allocationMasterRepository == null)
			_allocationMasterRepository = new AllocationMasterRepository(this.session);

		return (AllocationMasterRepository) _allocationMasterRepository;
	}

	public AllocationDetailRepository allocationDetailRepo() {

		if (_allocationDetailRepository == null)
			_allocationDetailRepository = new AllocationDetailRepository(this.session);

		return (AllocationDetailRepository) _allocationDetailRepository;
	}
	
	public AllocateHistoryRepository allocateHistoryRepo() {

		if (_allocateHistoryRepository == null)
			_allocateHistoryRepository = new AllocateHistoryRepository(this.session);

		return (AllocateHistoryRepository) _allocateHistoryRepository;
	}
	
	public UDFDefinitionRepository udfDefinitionRepo() {

		if (_udfDefinitionRepository == null)
			_udfDefinitionRepository = new UDFDefinitionRepository(this.session);

		return (UDFDefinitionRepository) _udfDefinitionRepository;
	}
	
	public UDFPropertiesRepository udfPropertiesRepo() {

		if (_udfPropertiesRepository == null)
			_udfPropertiesRepository = new UDFPropertiesRepository(this.session);

		return (UDFPropertiesRepository) _udfPropertiesRepository;
	}
	
	public UDFValuesRepository udfValuesRepo() {

		if (_udfValuesRepository == null)
			_udfValuesRepository = new UDFValuesRepository(this.session);

		return (UDFValuesRepository) _udfValuesRepository;
	}
	
	public UserCommonRepository getUserCommonRepository() {

		if (_userCommonRepository == null)
			_userCommonRepository = new UserCommonRepository(this.session);

		return (UserCommonRepository) _userCommonRepository;
	}
	
	public NotificationTemplateRepository getNotificationTemplateRepository() {

		if (_notificationRepository == null)
			_notificationRepository = new NotificationTemplateRepository(this.session);

		return (NotificationTemplateRepository) _notificationRepository;
	}
	
	public SendEmailRepository getSendEmailRepository() {
		
		if (_sendEmailRepository == null)
			_sendEmailRepository = new SendEmailRepository(this.session);

		return (SendEmailRepository) _sendEmailRepository;
	}
	
	public HealthCheckRepository getHealtCheckRepository() {
		if(_healthRepository == null)
			_healthRepository = new HealthCheckRepository(this.session);
		
		return (HealthCheckRepository) _healthRepository;
	}
	
	public CustomerCompanyInfoRepository getCompanyInfoRepository() {
		if(_customerCompanyInfoRepository == null)
			_customerCompanyInfoRepository = new CustomerCompanyInfoRepository(this.session);
		
		return (CustomerCompanyInfoRepository) _customerCompanyInfoRepository;
	}
	
	public DocumentRepository getDocumentRepository() {
		if(_documentRepository == null)
			_documentRepository = new DocumentRepository(this.session);
		
		return (DocumentRepository) _documentRepository;
	}
	
	public MappingHierarchyRepository mappingHierarchyRepository() {
		if(_mappingHierarchy == null)
			_mappingHierarchy = new MappingHierarchyRepository(this.session);
		
		return (MappingHierarchyRepository) _mappingHierarchy;
	}
	
	public CommodityRepository commondityRepository() {
		if(_commondityRepository == null)
			_commondityRepository = new CommodityRepository(this.session);
		
		return (CommodityRepository) _commondityRepository;
	}
	
	public UplMasterRepository uplMasterRepo() {

		if (_uplMasterRepository == null)
			_uplMasterRepository = new UplMasterRepository(this.session);

		return (UplMasterRepository) _uplMasterRepository;
	}
	
	public UplDetailRepository uplDetailRepo() {

		if (_uplDetailRepository == null)
			_uplDetailRepository = new UplDetailRepository(this.session);

		return (UplDetailRepository) _uplDetailRepository;
	}
	@Override
	public void reset() {
		super.reset();
		_messageLogRepository = null;
		_commoditiesRepository = null;
		_messageTaskRepository = null;
		_codeTableRepository = null;
		_auditDataChangeRepository = null;
		_partnerRepository = null;
		_parametersRepository = null;
		_productRepository = null;
		_systemDefineFieldsRepository = null;
		_menuRepository = null;
		_notificationTemplateRepository = null;
		_allocationDetailRepository = null;
		_allocationMasterRepository = null;
		_allocateHistoryRepository = null ;
		_udfDefinitionRepository = null;
		_udfPropertiesRepository = null;
		_udfValuesRepository = null;
		_mappingHierarchy =null;
		_commondityRepository = null;
		_uplMasterRepository = null;
		_uplDetailRepository = null;
		_messageTaskRepository = new MessageTaskRepository(this.session);
		_commoditiesRepository = new CommoditiesRepository(this.session);
		_messageLogRepository = new MessageLogRepository(this.session);
		_codeTableRepository = new CodeTableRepository(this.session);
		_auditDataChangeRepository = new AuditDataChangeRepository(this.session);
		_partnerRepository = new PartnerRepository(this.session);
		_parametersRepository = new ParametersRepository(this.session);
		_productRepository = new ProductRepository(this.session);
		_notificationTemplateRepository = new NotificationTemplateRepository(this.session);
		_systemDefineFieldsRepository = new SystemDefineFieldsRepository(this.session);
		_menuRepository = new MenuRepository(session);
		_allocationDetailRepository = new AllocationDetailRepository(this.session);
		_allocationMasterRepository = new AllocationMasterRepository(this.session);
		_allocateHistoryRepository = new AllocateHistoryRepository(this.session);
		_udfDefinitionRepository = new UDFDefinitionRepository(this.session);
		_udfPropertiesRepository = new UDFPropertiesRepository(this.session);
		_udfValuesRepository = new UDFValuesRepository(this.session);
		_healthRepository = new HealthCheckRepository(this.session);
		_customerCompanyInfoRepository = new CustomerCompanyInfoRepository(this.session);
		_documentRepository = new DocumentRepository(this.session);
		_mappingHierarchy = new MappingHierarchyRepository(this.session);
		_commondityRepository = new CommodityRepository(this.session);
		_uplMasterRepository = new UplMasterRepository(this.session);
		_uplDetailRepository = new UplDetailRepository(this.session);
	}

}
