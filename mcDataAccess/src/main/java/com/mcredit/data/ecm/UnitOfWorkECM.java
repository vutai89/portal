package com.mcredit.data.ecm;

import java.util.concurrent.Callable;

import org.hibernate.Session;

import com.mcredit.data.BaseUnitOfWork;
import com.mcredit.data.HibernateBase;
import com.mcredit.data.ecm.repository.DocumentToECMRepository;
import com.mcredit.data.repository.IRepository;
import com.mcredit.data.warehouse.repository.WhExpectedDateRepository;

public class UnitOfWorkECM extends BaseUnitOfWork {
	
	@SuppressWarnings("rawtypes")
	private IRepository _documentToECMRepository = null;
	
	public UnitOfWorkECM() {
		super();
	}
	public UnitOfWorkECM(HibernateBase hibernateBase,Session session) {
		super(hibernateBase,session);
	}
	
	public DocumentToECMRepository documentToECMRepo() {
		if (_documentToECMRepository == null)
			_documentToECMRepository = new DocumentToECMRepository(this.session);
		return (DocumentToECMRepository) _documentToECMRepository;
	}
	
	@Override
	public void reset() {
		super.reset();
		_documentToECMRepository = new WhExpectedDateRepository(this.session);
	}
	
	
	public static <T> T tryCatch(UnitOfWorkECM uok, Callable<T> func) throws Exception {

		try {
			uok.start();
			T result = func.call();
			uok.commit();
			return result;
		} catch (Throwable e) {
			uok.rollback();
			throw e;
		} finally {
			uok.close();
		}
	}
}
