package com.mcredit.data.warehouse.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.repository.IAddRepository;
import com.mcredit.data.repository.IUpdateRepository;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.data.warehouse.entity.WhExpectedDate;

public class WhExpectedDateRepository extends BaseRepository implements IUpsertRepository<WhExpectedDate>, IUpdateRepository<WhExpectedDate>, IAddRepository<WhExpectedDate> {

	public WhExpectedDateRepository(Session session) {
		super(session);
	}

	@Override
	public void add(WhExpectedDate item) {
		this.session.saveOrUpdate("WhExpectedDate", item);
		this.session.flush();
		this.session.clear();

	}

	@Override
	public void update(WhExpectedDate item) {
		this.session.saveOrUpdate("WhExpectedDate", item);
		this.session.flush();
		this.session.clear();

	}

	@Override
	public void upsert(WhExpectedDate item) {
		if (item.getId() == null) {
			this.session.save("WhExpectedDate", item);
		} else {
			this.session.merge("WhExpectedDate", item);
		}

		this.session.flush();
		this.session.clear();

	}
	
	public HashMap<Long, Date> getSendThankLetterByCreditAppId(List<Long> creditAppIdLst) {
		if(creditAppIdLst == null || creditAppIdLst.isEmpty()){
			return null ;
		}
		HashMap<Long, Date> result = new HashMap<>() ; 
		List<?> lst = this.session.getNamedNativeQuery( "getSendThankLetterByCreditAppId") .setParameter("creditAppIdLst", creditAppIdLst).getResultList();
		if (lst != null && !lst.isEmpty()) {
			for (Object object : lst) {
				Object[] o = (Object[]) object;				
				result.put(Long.valueOf(o[0].toString()), (Date)o[1]);
			}
		}

		return result;
	}
	
	public HashMap<Long, Date> getDateErrCavetByCreditAppId(List<Long> creditAppIdLst) {
		if(creditAppIdLst == null || creditAppIdLst.isEmpty()){
			return null ;
		}
		HashMap<Long, Date> result = new HashMap<>() ; 
		List<?> lst = this.session.getNamedNativeQuery( "getDateErrCavetByCreditAppId") .setParameter("creditAppIdLst", creditAppIdLst).getResultList();
		if (lst != null && !lst.isEmpty()) {
			for (Object object : lst) {
				Object[] o = (Object[]) object;				
				result.put(Long.valueOf(o[0].toString()), (Date)o[1]);
			}
		}

		return result;
	}

	public HashMap<Long, Date> getDateErrLoanDocByCreditAppId(List<Long> creditAppIdLst) {
		if(creditAppIdLst == null || creditAppIdLst.isEmpty()){
			return null ;
		}
		HashMap<Long, Date> result = new HashMap<>() ; 
		List<?> lst = this.session.getNamedNativeQuery( "getDateErrLoanDocByCreditAppId") .setParameter("creditAppIdLst", creditAppIdLst).getResultList();
		if (lst != null && !lst.isEmpty()) {
			for (Object object : lst) {
				Object[] o = (Object[]) object;				
				result.put(Long.valueOf(o[0].toString()), (Date)o[1]);
			}
		}

		return result;
	}
        
	public void deleteExpectedDate(Long creditAppId, Long docType) {
		this.session.getNamedNativeQuery("deleteExpectedDate").setParameter("creditAppId", creditAppId).setParameter("docType", docType).executeUpdate();
	}
	public int checkExitExpectedDate(Long creditAppId, Long docType) {
            return  ((BigDecimal) this.session.getNamedNativeQuery("checkExitExpectedDate").setParameter("creditAppId", creditAppId).setParameter("docType", docType).getSingleResult()).intValue();
	}
}