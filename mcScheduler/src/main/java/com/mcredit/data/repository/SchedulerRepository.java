package com.mcredit.data.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.query.Query;
import org.hibernate.type.StandardBasicTypes;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.DataException;
import com.mcredit.data.DataUtils;
import com.mcredit.data.IRepository;
import com.mcredit.data.entity.Scheduler;
import com.mcredit.data.entity.SchedulerInstance;
import com.mcredit.scheduler.SchedulerDTO;

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

	public final List<SchedulerDTO> getAllActiveScheduler(String schGroup) {
		try {
			List<Object[]> lstObj = session.createNativeQuery("select ID,RECORD_STATUS,to_char(CREATED_DATE,'DD/MM/YYYY HH24:MI:SS') CREATED_DATE, " + 
					"to_char(LAST_UPDATED_DATE,'DD/MM/YYYY HH24:MI:SS') LAST_UPDATED_DATE,CREATED_BY, " + 
					"LAST_UPDATED_BY,SCHEDULE_GROUP,SCH_NAME,FREQUENCY,INTERVAL,to_char(START_TIME,'DD/MM/YYYY HH24:MI:SS') START_TIME, " + 
					"to_char(nvl(NEXT_SCHEDULE_TIME,START_TIME),'DD/MM/YYYY HH24:MI:SS') NEXT_SCHEDULE_TIME,STATUS,NUM_OF_RUN,REQUEST,ALLOW_OVERLAP,EXECUTE_TARGET " + 
					"from SCHEDULER where RECORD_STATUS = 'A' and SCHEDULE_GROUP = :schGroup ")
					.setParameter("schGroup", schGroup).list();
			List<SchedulerDTO> list = new ArrayList<SchedulerDTO>();
			for(Object[] objs : lstObj) {
				SchedulerDTO schedule = new SchedulerDTO();
				list.add((SchedulerDTO) DataUtils.bindingData(schedule, objs));
			}
			return list;
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public final List<SchedulerDTO> getAllDueScheduler(String schGroup) {
		try {
			List<Object[]> lstObj = session.createNativeQuery("select ID,RECORD_STATUS,to_char(CREATED_DATE,'DD/MM/YYYY HH24:MI:SS') CREATED_DATE, " + 
					"to_char(LAST_UPDATED_DATE,'DD/MM/YYYY HH24:MI:SS') LAST_UPDATED_DATE,CREATED_BY, " + 
					"LAST_UPDATED_BY,SCHEDULE_GROUP,SCH_NAME,FREQUENCY,INTERVAL,to_char(START_TIME,'DD/MM/YYYY HH24:MI:SS') START_TIME, " + 
					"to_char(NEXT_SCHEDULE_TIME,'DD/MM/YYYY HH24:MI:SS') NEXT_SCHEDULE_TIME,STATUS,NUM_OF_RUN,REQUEST,ALLOW_OVERLAP,EXECUTE_TARGET,TIME_RANGE " + 
					"from SCHEDULER where RECORD_STATUS = 'A' and STATUS = 'S' " + 
					"and SCHEDULE_GROUP = :schGroup and nvl(NEXT_SCHEDULE_TIME,START_TIME) <= sysdate ")
					.setParameter("schGroup", schGroup, StandardBasicTypes.STRING).list();
			List<SchedulerDTO> list = new ArrayList<SchedulerDTO>();
			for(Object[] objs : lstObj) {
				SchedulerDTO schedule = new SchedulerDTO();
				list.add((SchedulerDTO) DataUtils.bindingData(schedule, objs));
			}
			return list;
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public final void updateScheduler(SchedulerDTO scheduler) {
		try {
			int count = session.createNativeQuery("update SCHEDULER set LAST_UPDATED_DATE = sysdate,"
					+ " NEXT_SCHEDULE_TIME = to_date(:schNextTime,'DD/MM/YYYY HH24:MI:SS'),"
					+ " STATUS = :schStatus,"
					+ " NUM_OF_RUN = :schRun "
					+ " where ID = :schId")
					.setParameter("schNextTime", scheduler.getNextScheduleTime(), StandardBasicTypes.STRING)
					.setParameter("schStatus", scheduler.getStatus(), StandardBasicTypes.STRING)
					.setParameter("schRun", scheduler.getNumOfRun(), StandardBasicTypes.INTEGER)
					.setParameter("schId", scheduler.getId(), StandardBasicTypes.INTEGER)
					.executeUpdate();
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
