package com.mcredit.data.cancelcasebpm.repository;

import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.cancelcasebpm.entity.UplAppAutoAbort;
import com.mcredit.data.repository.IAddRepository;
import com.mcredit.data.repository.IUpdateRepository;
import com.mcredit.data.repository.IUpsertRepository;

public class UplAppAutoAbortDataRepository extends BaseRepository implements IUpsertRepository<UplAppAutoAbort>,IUpdateRepository<UplAppAutoAbort>,IAddRepository<UplAppAutoAbort>{

	public UplAppAutoAbortDataRepository(Session session) {
		super(session);
	}

	@Override
	public void add(UplAppAutoAbort item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(UplAppAutoAbort item) {
		session.update(item);
	}

	@Override
	public void upsert(UplAppAutoAbort item) {
		session.saveOrUpdate(item);
	}
	
	public List<UplAppAutoAbort> getUplAppAutoAbortData () {
		List<UplAppAutoAbort> result = this.session.createNamedQuery("getUplAppAutoAbortList", UplAppAutoAbort.class)
				.getResultList();
		return result;
	};
	
	public UplAppAutoAbort getUplAppAutoAbortByAppuid (String appuid) {
		UplAppAutoAbort result = this.session.createNamedQuery("getUplAppAutoAbortByAppuid", UplAppAutoAbort.class)
				.setParameter("appuid", appuid)
				.getSingleResult();
		return result;
	};

}
