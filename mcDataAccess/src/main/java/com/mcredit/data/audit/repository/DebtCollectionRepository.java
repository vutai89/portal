package com.mcredit.data.audit.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.audit.entity.DebtCollection;
import com.mcredit.data.repository.IRepository;
import com.mcredit.model.enums.disaudit.ThirdParty;
import com.mcredit.util.DateUtil;

public class DebtCollectionRepository extends BaseRepository implements IRepository<DebtCollection> {
	
	public final static String dateFormat1 = "DDMMYYYY";
	public final static String dateFormat2 = "DD-MM-YYYY";

	public DebtCollectionRepository(Session session) {
		super(session);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<DebtCollection> getDebtCollectionByDate(String dateStr1, String thirdParty, List<String> tp) throws Exception{

		StringBuilder str = new StringBuilder("select ID, PAYMENT_AMOUNT, CREATED_DATE, PAYMENT_CHANNEL_CODE, COLLECTION_FEE, PARTNER_REF_ID, CONTRACT_NUMBER, CANCEL from debt_collection where to_char(CREATED_DATE,'DDMMYYYY') = :dateStr1");
		if (!thirdParty.equals(ThirdParty.ALL.value())) {
			str.append(" and payment_channel_code = :channel");
		} else { 
			str.append(" and payment_channel_code not in (:channels)");
		}
		NativeQuery query = this.session.createNativeQuery(str.toString());
		query.setParameter("dateStr1", dateStr1);
		if (!thirdParty.equals(ThirdParty.ALL.value())) {
			query.setParameter("channel", thirdParty);			
		} else {
			query.setParameterList("channels", tp);
		}
		List lst = query.getResultList();
		return null == lst || lst.isEmpty() ? new ArrayList<>(): transformList(lst, DebtCollection.class);
	}

	@SuppressWarnings("unchecked")
	public List<DebtCollection> getListDC(String fromDate, String toDate, String thirdParty) {
		StringBuilder str = new StringBuilder("select ID, PAYMENT_AMOUNT, CREATED_DATE, PAYMENT_CHANNEL_CODE, COLLECTION_FEE, PARTNER_REF_ID, CONTRACT_NUMBER, CANCEL from DEBT_COLLECTION where"
				+ " CREATED_DATE >= to_date('"+ fromDate +" 00:00:00', 'dd/mm/yyyy HH24:Mi:SS') and CREATED_DATE <= to_date('"+ toDate +" 23:59:59', 'dd/mm/yyyy HH24:Mi:SS')");
		if (!thirdParty.equals(ThirdParty.ALL.value())) {
			str.append(" and PAYMENT_CHANNEL_CODE = '"+ thirdParty +"'");
		}
		List<?> lst = this.session.createNativeQuery(str.toString())
				.getResultList();
		
		if (null != lst && !lst.isEmpty()) {
			return transformList(lst, DebtCollection.class);
		}
		return new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	public List<DebtCollection> getDebtCollection17ByDate(String date) {
		List<?> lst = this.session.getNamedNativeQuery("getVNPOST17h")
				.setParameter("fromDate", date + " 00:00:00")
				.setParameter("toDate", date + " 17:00:00")
				.getResultList();
		
		if (null != lst && !lst.isEmpty()) {
			return transformList(lst, DebtCollection.class);
		}
		return new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	public List<DebtCollection> getAllMBDebtCollection(Date day) {
		List<?> lst = this.session.getNamedNativeQuery("getAllMBDebtCollection")
				.setParameter("month", DateUtil.getCurrentMonth(day))
				.setParameter("year", DateUtil.getYear(day)).getResultList();
		
		if (null != lst && !lst.isEmpty()) {
			return transformList(lst, DebtCollection.class);
		}
		return new ArrayList<>();
	}
	
}

