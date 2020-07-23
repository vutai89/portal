package com.mcredit.data.assign;

import org.hibernate.Session;

import com.mcredit.data.BaseUnitOfWork;
import com.mcredit.data.HibernateBase;
import com.mcredit.data.assign.repository.ServiceRepository;
import com.mcredit.data.repository.IRepository;
import com.mcredit.model.dto.assign.ServiceDTO;


public class UnitOfWorkManagePermision extends BaseUnitOfWork {
	public UnitOfWorkManagePermision() {
		super();
	}
	
	public UnitOfWorkManagePermision(HibernateBase hibernateBase,Session session) {
		super(hibernateBase,session);
	}
	
	private IRepository<ServiceDTO> _serviceRepository = null;

	public ServiceRepository serviceRepository() {
		if (_serviceRepository == null)
			_serviceRepository = new ServiceRepository(this.session);
		return (ServiceRepository) _serviceRepository;
	}
}