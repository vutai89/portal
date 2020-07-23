package com.mcredit.data.credit.repository;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.credit.entity.CreditApplicationAdditional;
import com.mcredit.data.repository.IUpsertRepository;

public class CreditApplicationAdditionalRepository extends BaseRepository
		implements IUpsertRepository<CreditApplicationAdditional> {

	public CreditApplicationAdditionalRepository(Session session) {
		super(session);
	}

	public void upsert(CreditApplicationAdditional item) {
		this.session.saveOrUpdate("CreditApplicationAdditional", item);
	}

	public CreditApplicationAdditional getByRequestId(Long id) {
		if (id == null) return null;
		@SuppressWarnings("rawtypes")
		Query query = this.session.createQuery("from "+CreditApplicationAdditional.class.getName()+ " where creditAppId =:id");
		query.setParameter("id", id);
		CreditApplicationAdditional result = (CreditApplicationAdditional) query.uniqueResult();
		return result;
	}

	public void remove(CreditApplicationAdditional item) {
		this.session.delete("CreditApplicationAdditional", item);
	}
	
    public Long findNextSeq() {
        @SuppressWarnings("rawtypes")
        Query query = this.session.getNamedNativeQuery("CreditAppAdditional.nextSeq");
        BigDecimal result = (BigDecimal) query.uniqueResult();
        if(result == null)
            return null;
        return result.longValue();
    }
    
	public List<CreditApplicationAdditional> getAppRequestBy(Long creditReqId) {
		return (List<CreditApplicationAdditional>) this.session.createQuery("from CreditApplicationAdditional where creditAppId = :creditReqId", CreditApplicationAdditional.class)
				.setParameter("creditReqId", creditReqId)
				.list();
	}

}
