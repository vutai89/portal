package com.mcredit.data.credit.repository;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.credit.entity.CreditApplicationAppraisal;
import com.mcredit.data.repository.IUpsertRepository;

public class CreditApplicationAppraisalRepository extends BaseRepository
		implements IUpsertRepository<CreditApplicationAppraisal> {

	public CreditApplicationAppraisalRepository(Session session) {
		super(session);
	}

	public void upsert(CreditApplicationAppraisal item) {
		this.session.saveOrUpdate("CreditApplicationAppraisal", item);
	}

	public CreditApplicationAppraisal getByRequestId(Long id) {
		if (id == null) return null;
		Query<?> query = this.session.createQuery("from "+CreditApplicationAppraisal.class.getName()+ " where creditAppId =:id");
		query.setParameter("id", id);
		
		return (CreditApplicationAppraisal) query.uniqueResult();
	}

	
	public void remove(CreditApplicationAppraisal item) {
		this.session.delete("CreditApplicationAppraisal", item);
	}
	
    public Long findNextSeq() {
        @SuppressWarnings("rawtypes")
        Query query = this.session.getNamedNativeQuery("CreditAppAppraisal.nextSeq");
        BigDecimal result = (BigDecimal) query.uniqueResult();
        if(result == null)
            return null;
        return result.longValue();
    }

	public List<CreditApplicationAppraisal> getAppRequestBy(Long creditReqId) {
		return (List<CreditApplicationAppraisal>) this.session.createQuery("from CreditApplicationAppraisal where creditAppId = :creditReqId", CreditApplicationAppraisal.class)
				.setParameter("creditReqId", creditReqId)
				.list();
	}

}
