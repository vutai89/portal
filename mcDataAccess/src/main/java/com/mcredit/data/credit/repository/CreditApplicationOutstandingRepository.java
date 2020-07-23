package com.mcredit.data.credit.repository;

import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.credit.entity.CreditApplicationOutstanding;
import com.mcredit.data.repository.IUpsertRepository;

public class CreditApplicationOutstandingRepository extends BaseRepository
implements IUpsertRepository<CreditApplicationOutstanding> {
	
	public CreditApplicationOutstandingRepository(Session session) {
		super(session);
	}
	public void add(CreditApplicationOutstanding item) {
	}

	public void update(CreditApplicationOutstanding item) {
	}

	public void upsert(CreditApplicationOutstanding item) {
		this.session.saveOrUpdate("CreditApplicationOutstanding", item);
	}
	public void remove(CreditApplicationOutstanding item) {
		this.session.delete("CreditApplicationOutstanding", item);
	}
	public List findNativeCardUnderMinPayment()
	{
		return this.session.getNamedNativeQuery("findNativeCardUnderMinPayment").list();
	}
	public List findNativeCardUnderDuePayment()
	{
		return this.session.getNamedNativeQuery("findNativeCardUnderDuePayment").list();
	}
	
	
	public CreditApplicationOutstanding findOutstandingByCard(String cardId)
	{
		//return (CreditApplicationOutstanding) this.session.getNamedNativeQuery("findOutstandingByCard").setParameter("linkValue", cardId).getSingleResult();
		
		List<CreditApplicationOutstanding> ojb = this.session.createNativeQuery("SELECT cao.ID, cao.CREDIT_APP_ID, cao.RECORD_STATUS, cao.LAST_UPDATED_DATE, cao.REMAIN_LIMIT, cao.CURRENT_BALANCE, cao.DUE_BALANCE, cao.NEXT_DUE_BALANCE, cao.MIN_PAYMENT_AMOUNT, cao.REMAIN_MIN_AMOUNT, "
				+ "cao.PRINCIPAL_AMOUNT, cao.DUE_INT_AMOUNT, cao.PENALTY_INT_AMOUNT, cao.FEE_AMOUNT, cao.OVERDUE_PRINCIPAL_AMOUNT, cao.DUE_FEE_AMOUNT, cao.LOYALTY_AMOUNT,"
				+ " cao.PAYMENT_AMOUNT FROM CREDIT_APP_OUTSTANDING cao  INNER JOIN CREDIT_APP_REQUEST car on car.ID = cao.CREDIT_APP_ID "
				+ "INNER JOIN CUST_ACCOUNT_LINK cal on  cal.CUST_ID = car.CUST_ID  WHERE cal.LINK_TYPE = 'I' and cal.LINK_VALUE =:cardId " ,CreditApplicationOutstanding.class).setParameter("cardId", cardId).getResultList();
		if(ojb != null && ojb.size() > 0){		
			return ojb.get(0) ;
		}
		
		return null;
		
	}
}
