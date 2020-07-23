package com.mcredit.data.credit.repository;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.credit.entity.CreditApplicationBPM;
import com.mcredit.data.repository.IUpsertRepository;

public class CreditApplicationBPMRepository extends BaseRepository implements
		IUpsertRepository<CreditApplicationBPM> {

	public CreditApplicationBPMRepository(Session session) {
		super(session);
	}

	public void upsert(CreditApplicationBPM item) {
		this.session.saveOrUpdate("CreditApplicationBPM", item);
	}

	public CreditApplicationBPM getByRequestId(Long id) {
		if (id == null) return null;
		Query<?> query = this.session.createQuery("from "+CreditApplicationBPM.class.getName()+ " where creditAppId =:id");
		query.setParameter("id", id);
		
		return (CreditApplicationBPM) query.uniqueResult();
	}

	
	public void remove(CreditApplicationBPM item) {
		this.session.delete("CreditApplicationBPM", item);
	}
	
    public Long findNextSeq() {
        @SuppressWarnings("rawtypes")
        Query query = this.session.getNamedNativeQuery("CreditAppBPM.nextSeq");
        BigDecimal result = (BigDecimal) query.uniqueResult();
        if(result == null)
            return null;
        return result.longValue();
    }

	public List<CreditApplicationBPM> getAppRequestBy(String bpmAppId, Long creditReqId) {
		if (bpmAppId == null)
			return (List<CreditApplicationBPM>) this.session.createQuery("from CreditApplicationBPM where creditAppId = :creditReqId", CreditApplicationBPM.class)
					.setParameter("creditReqId", creditReqId)
					.list();

		return (List<CreditApplicationBPM>) this.session.createQuery("from CreditApplicationBPM where bpmAppId = :bpmAppId or creditAppId = :creditReqId", CreditApplicationBPM.class)
								.setParameter("bpmAppId", bpmAppId!=null?bpmAppId.trim():"")
								.setParameter("creditReqId", creditReqId)
								.list();
	}
	
	public CreditApplicationBPM getBy(String bpmAppId) {
		return (CreditApplicationBPM) this.session.createQuery("from CreditApplicationBPM where bpmAppId = :bpmAppId", CreditApplicationBPM.class)
								.setParameter("bpmAppId", bpmAppId)
								.uniqueResult();
	}

}
