package com.mcredit.data.credit.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.DataException;
import com.mcredit.data.credit.entity.CreditApplicationLoanManagement;
import com.mcredit.data.repository.IUpsertRepository;

public class CreditApplicationLoanManagementRepository extends BaseRepository
		implements IUpsertRepository<CreditApplicationLoanManagement> {

	public CreditApplicationLoanManagementRepository(Session session) {
		super(session);
	}

	/****************
	 * End Implement lazy load
	 ***************/


	public void upsert(CreditApplicationLoanManagement item) {
		this.session.saveOrUpdate("CreditApplicationLoanManagement", item);
	}

	public void updateCreditAppLmsWithCardId(String cardId, Long creditAppId) throws DataException {
		
		CriteriaBuilder builder = this.session.getCriteriaBuilder();
		CriteriaUpdate<CreditApplicationLoanManagement> criteria = builder.createCriteriaUpdate(CreditApplicationLoanManagement.class);
		Root<CreditApplicationLoanManagement> root = criteria.from(CreditApplicationLoanManagement.class);
		criteria.set(root.get("coreLnAppId"), cardId.trim());
		criteria.where(builder.equal(root.get("creditAppId"), creditAppId));
		this.session.createQuery(criteria).executeUpdate();
	}
	
	public CreditApplicationLoanManagement getByRequestId(Long id) {
		if (id == null) return null;
		@SuppressWarnings("rawtypes")
		Query query = this.session.createQuery("from "+CreditApplicationLoanManagement.class.getName()+ " where creditAppId =:id");
		query.setParameter("id", id);
		CreditApplicationLoanManagement result = (CreditApplicationLoanManagement) query.uniqueResult();
		return result;
	}

	public void remove(CreditApplicationLoanManagement item) {
		this.session.delete("CreditApplicationLoanManagement", item);
	}
	
    public Long findNextSeq() {
        @SuppressWarnings("rawtypes")
        Query query = this.session.getNamedNativeQuery("CreditAppLMS.nextSeq");
        BigDecimal result = (BigDecimal) query.uniqueResult();
        if(result == null)
            return null;
        return result.longValue();
    }
    
	public List<CreditApplicationLoanManagement> getAppRequestBy(String coreLnAppId, Long creditReqId) {
		if (coreLnAppId == null)
			return (List<CreditApplicationLoanManagement>) this.session.createQuery("from CreditApplicationLoanManagement where creditAppId = :creditReqId", CreditApplicationLoanManagement.class)
					.setParameter("creditReqId", creditReqId)
									.list();

		return (List<CreditApplicationLoanManagement>) this.session.createQuery("from CreditApplicationLoanManagement where coreLnAppId = :coreLnAppId or creditAppId = :creditReqId", CreditApplicationLoanManagement.class)
				.setParameter("mcContractNumber", coreLnAppId!=null?coreLnAppId.trim():"")
				.setParameter("creditReqId", creditReqId)
								.list();
		
	}


}
