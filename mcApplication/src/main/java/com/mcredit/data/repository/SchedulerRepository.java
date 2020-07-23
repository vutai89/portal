package com.mcredit.data.repository;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.query.Query;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.DataException;
import com.mcredit.data.IRepository;
import com.mcredit.data.entity.Scheduler;
import com.mcredit.data.entity.SchedulerInstance;

public class SchedulerRepository extends BaseRepository implements IRepository<Scheduler> {

	public SchedulerRepository(Session session) {
			super(session);
	}

	public void add(Scheduler item) {
		this.session.save("Scheduler", item);
	}

	public void add(SchedulerInstance item) {
		if(item.getId() == null || item.getId() == 0) {
			item.setId(findNextSeq());
		}
		this.session.save("SchedulerInstance", item);
	}

	public void update(Scheduler item) {
		this.session.update("Scheduler", item);
	}

	public void update(SchedulerInstance item) {
		this.session.update("SchedulerInstance", item);
	}

	public void upsert(Scheduler item) {
		this.session.saveOrUpdate("Scheduler", item);
	}

	public void remove(Scheduler item) {
		this.session.delete("Scheduler", item);
	}

	public Long findNextSeq() {
		@SuppressWarnings("rawtypes")
		Query query = this.session.getNamedNativeQuery("SchedulerID.nextSeq");
		BigDecimal result = (BigDecimal) query.uniqueResult();
		if(result == null)
			return null;
		return result.longValue();
	}

	public final List<Scheduler> getAllActiveScheduler(Class<?> entityClass, String schGroup) {
		try {
			return session.createQuery("from " + entityClass.getName() + " where recordStatus = 'A' and scheduleGroup = '" + schGroup + "'").list();
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public final void executeProcedure(String procedureName) {
		try {
			ProcedureCall call = session.createStoredProcedureCall(procedureName);
			boolean ret = call.execute();
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}
}
