package com.mcredit.data.transactions;

import org.hibernate.Session;

import com.mcredit.data.BaseUnitOfWork;
import com.mcredit.data.HibernateBase;
import com.mcredit.data.repository.IRepository;
import com.mcredit.data.transaction.repository.TransactionsRepository;


public class UnitOfWorkTransactions extends BaseUnitOfWork {

	@SuppressWarnings("rawtypes")
	private IRepository _transactionsRepositoryRepository = null;


	public UnitOfWorkTransactions() {
		super();
	}

	public UnitOfWorkTransactions(HibernateBase hibernateBase, Session session) {
		super(hibernateBase, session);
	}
	
	
	public TransactionsRepository transactionsRepo() {

		if (_transactionsRepositoryRepository == null) {
			_transactionsRepositoryRepository = new TransactionsRepository(this.session);
		}

		return (TransactionsRepository) _transactionsRepositoryRepository;
	}


	@Override
	public void reset() {
		super.reset();
		_transactionsRepositoryRepository = null;
		_transactionsRepositoryRepository = new TransactionsRepository(this.session);
	}

}
