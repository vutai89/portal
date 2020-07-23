package com.mcredit.data.user;

import org.hibernate.Session;

import com.mcredit.data.BaseUnitOfWork;
import com.mcredit.data.HibernateBase;
import com.mcredit.data.assign.repository.RolesRepository;
import com.mcredit.data.mobile.repository.UsersProfilesRepository;
import com.mcredit.data.repository.IRepository;
import com.mcredit.data.user.repository.EmployeeRepository;
import com.mcredit.data.user.repository.UserSessionRepository;
import com.mcredit.data.user.repository.UsersRepository;

public class UnitOfWorkUser extends BaseUnitOfWork {

	@SuppressWarnings("rawtypes")
	private IRepository _userSessionRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _usersRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _employeeRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _usersProfilesRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _rolesRepository = null;

	public UnitOfWorkUser(HibernateBase hibernateBase,Session session) {
		super(hibernateBase,session);
	}
	
	public UnitOfWorkUser() {
		super();
	}
	
	public EmployeeRepository employeeRepo() {

		if (_employeeRepository == null)
			_employeeRepository = new EmployeeRepository(this.session);

		return (EmployeeRepository) _employeeRepository;
	}

	public UsersRepository usersRepo() {

		if (_usersRepository == null)
			_usersRepository = new UsersRepository(this.session);

		return (UsersRepository) _usersRepository;
	}

	public UserSessionRepository userSessionRepo() {

		if (_userSessionRepository == null)
			_userSessionRepository = new UserSessionRepository(this.session);

		return (UserSessionRepository) _userSessionRepository;
	}
	
	public UsersProfilesRepository usersProfilesRepository() {
		if (_usersProfilesRepository == null)
			_usersProfilesRepository = new UsersProfilesRepository(this.session);

		return (UsersProfilesRepository) _usersProfilesRepository;
	}
	public RolesRepository rolesRepo() {
		if (_rolesRepository == null)
			_rolesRepository = new RolesRepository(this.session);

		return (RolesRepository) _rolesRepository;
	}
}
