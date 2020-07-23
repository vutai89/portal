package com.mcredit.data.mobile.repository;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.customer.entity.CustomerCompanyInfo;
import com.mcredit.data.mobile.entity.ExternalUserMapping;
import com.mcredit.data.repository.IRepository;

public class ExternalUserMappingRepository extends BaseRepository implements
		IRepository<ExternalUserMapping> {
	public ExternalUserMappingRepository(Session session) {
		super(session);
	}

	public void update(ExternalUserMapping item) {
		this.session.saveOrUpdate("ExternalUserMapping", item);
	}

	@SuppressWarnings("rawtypes")
	public ExternalUserMapping getEUMappingByEmpId(String empId) {
		NativeQuery query = this.session.getNamedNativeQuery("getEUMappingByEmpId").setParameter("empId", empId);
		Object eum = query.uniqueResult();

		ExternalUserMapping result = transformObject(eum, ExternalUserMapping.class);
		return result;
	}

	public List<ExternalUserMapping> getExternalUserMappingList() {
		return this.session.createQuery("from ExternalUserMapping",
				ExternalUserMapping.class).getResultList();
	}
	
	@SuppressWarnings("rawtypes")
	public ExternalUserMapping getEUMappingByUplId(Long uplId) {
		NativeQuery query = this.session.getNamedNativeQuery("getEUMappingByUplId").setParameter("uplId", uplId);
		Object eum = query.uniqueResult();

		ExternalUserMapping result = transformObject(eum, ExternalUserMapping.class);
		return result;
	}

	public ExternalUserMapping getById(long id) {
		return this.session.find(ExternalUserMapping.class, id);
	}
	
	public boolean checkExist(long id) {
//		return this.session.getna("getEUMappingByUplId").setParameter("uplId", id).;
//		BigDecimal count = (BigDecimal) this.session.getNamedNativeQuery("checkTopComp").setParameter("taxNumber", taxNumber).setParameter("ctCat", ctCat).uniqueResult();
//		return count.intValue() > 0;
		BigDecimal count = (BigDecimal) this.session.createNativeQuery(" select count(*) from external_user_mapping where id = :id ").setParameter("id", id).uniqueResult();
		return count.intValue() > 0;
	}
}
