package com.mcredit.data.debt_home;

import org.hibernate.Session;

import com.mcredit.data.BaseUnitOfWork;
import com.mcredit.data.HibernateBase;
import com.mcredit.data.debt_home.entity.DebtHomeAssign;
import com.mcredit.data.debt_home.repository.DebtHomeAssignRepository;
import com.mcredit.data.repository.IRepository;

public class UnitOfWorkDebtHome extends BaseUnitOfWork {
	public UnitOfWorkDebtHome() {
		super();
	}
	
	public UnitOfWorkDebtHome(HibernateBase hibernateBase,Session session) {
		super(hibernateBase,session);
	}
	
	private IRepository<DebtHomeAssign> _debtHomeAssignRepository = null;

	public DebtHomeAssignRepository debtHomeAssignRepository() {

		if (_debtHomeAssignRepository == null)
			_debtHomeAssignRepository = new DebtHomeAssignRepository(this.session);

		return (DebtHomeAssignRepository) _debtHomeAssignRepository;
	}
}
