package com.mcredit.data.mobile.repository;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.mobile.entity.CreditAppTrail;
import com.mcredit.data.repository.IRepository;
import com.mcredit.model.object.mobile.dto.ReasonDTO;

import java.util.List;

public class CreditAppTrailRepository extends BaseRepository implements IRepository<CreditAppTrail> {
	
	public CreditAppTrailRepository(Session session) {
		super(session);
	}
	
	public List<ReasonDTO> getReasonsByCreditAppId(Long creditAppId) {
		NativeQuery query = this.session.getNamedNativeQuery("getReasonsByCreditAppId").setParameter("credit_app_id", creditAppId);
		List list =  query.list();
		List<ReasonDTO> reasons = transformList(list, ReasonDTO.class);
		return reasons;
	}
}
