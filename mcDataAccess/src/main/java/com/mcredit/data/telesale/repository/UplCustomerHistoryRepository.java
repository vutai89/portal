package com.mcredit.data.telesale.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.repository.IAddRepository;
import com.mcredit.data.telesale.entity.UplCustomer;
import com.mcredit.data.telesale.entity.UplCustomerHistory;
import com.mcredit.model.enums.Constant;
import com.mcredit.model.enums.ConstantTelesale;

public class UplCustomerHistoryRepository extends BaseRepository implements IAddRepository<UplCustomerHistory> {
  
	public UplCustomerHistoryRepository(Session session) {
		super(session);
	}

	@Override
	public void add(UplCustomerHistory item) {
		this.session.save("UplCustomerHistory", item);
	}
	
	public void merge(UplCustomerHistory item) {
        this.session.merge("UplCustomerHistory", item);
    }
	
	public void saveOrUpdate(UplCustomerHistory item) {
		this.session.saveOrUpdate("UplCustomerHistory", item);
	}
	 
	@SuppressWarnings("unchecked")
	public List<UplCustomerHistory> findBy(Long cusId, String refId) {
		Query query = this.session.createQuery("from UplCustomerHistory where uplCustomerId = :cusId and refId=:refId order by lastUpdatedDate desc");
		if(refId.equals(ConstantTelesale.CUS_HIS_REFID_MARK_TS)) query = this.session.createQuery("from UplCustomerHistory where uplCustomerId = :cusId and (refId=:refId or refId='MARK_ESB') order by lastUpdatedDate desc");
		List<UplCustomerHistory> lst  =  query.setParameter("cusId", cusId)
			.setParameter("refId", refId).list();
		return lst; 
	}
	
	public UplCustomerHistory getBy(Long cusId, String refId){
		List<UplCustomerHistory> lstResult = this.findBy(cusId, refId);
		return lstResult.isEmpty() ? null : lstResult.get(0);
	}
	
	public UplCustomerHistory getLastCustHistByUplCustId(String upl_customer_id) {
		Query query = this.session.createNativeQuery(" select ID, LAST_UPDATED_DATE, UPL_CUSTOMER_ID, UPL_MASTER_ID, REF_ID, RESPONSE_CODE, MESSAGE from upl_customer_hist " + 
				" where ref_id = 'OTP' " + 
				" and upl_customer_id = :upl_customer_id " + 
				" order by last_updated_date desc ");
		List<UplCustomerHistory> lst  =  query.setParameter("upl_customer_id", upl_customer_id).list();
		
		return lst.isEmpty() ? null : (UplCustomerHistory) transformList(lst, UplCustomerHistory.class).get(0);
	}
}
