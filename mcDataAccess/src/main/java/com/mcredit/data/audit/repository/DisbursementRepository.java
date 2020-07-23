package com.mcredit.data.audit.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.audit.entity.Disbursement;
import com.mcredit.data.repository.IRepository;
import com.mcredit.model.enums.disaudit.ThirdParty;
import com.mcredit.util.DateUtil;

public class DisbursementRepository extends BaseRepository implements IRepository<Disbursement> {
	public final static String dateFormat1 = "DDMMYYYY";
	public final static String dateFormat2 = "DD-MM-YYYY";

	public DisbursementRepository(Session session) {
		super(session);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Disbursement> getDisbursementByDate(String dateStr1, String thirdParty, List<String> tp)
			throws Exception {

		StringBuilder str = new StringBuilder(
				"select ID, PAYMENT_AMOUNT, CREATED_DATE, PAYMENT_CHANNEL_CODE, DISBURSEMENT_FEE, PARTNER_REF_ID, CONTRACT_NUMBER, PAYMENT_CODE, DEBIT_ACCOUNT, DISBURSEMENT_DATE from DISBURSEMENT where DISBURSEMENT_DATE = :dateStr1");
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
		return null == lst || lst.isEmpty() ? new ArrayList<>() : transformList(lst, Disbursement.class);
	}

	@SuppressWarnings("unchecked")
	public List<Disbursement> getListDisbursement(String fromDate, String toDate, String thirdParty) {
		StringBuilder str = new StringBuilder("select ID, PAYMENT_AMOUNT, CREATED_DATE, PAYMENT_CHANNEL_CODE, DISBURSEMENT_FEE, PARTNER_REF_ID, CONTRACT_NUMBER, PAYMENT_CODE, DEBIT_ACCOUNT, DISBURSEMENT_DATE "
				+ "from Disbursement where"
				+ " to_date(DISBURSEMENT_DATE,'DDMMYYYY') >= to_date(:fromDate, 'DD/MM/YYYY HH24:Mi:SS') and to_date(DISBURSEMENT_DATE,'DDMMYYYY') <= to_date(:toDate, 'DD/MM/YYYY HH24:Mi:SS')");

		if (!thirdParty.equals(ThirdParty.ALL.value())) {
			str.append(" and payment_channel_code = :thirdParty ");
		}
		List<?> lst = this.session.createNativeQuery(str.toString()).setParameter("fromDate", fromDate + " 00:00:00")
				.setParameter("toDate", toDate +" 23:59:59")
				.setParameter("thirdParty", thirdParty).getResultList();

		if (null != lst && !lst.isEmpty()) {
			return transformList(lst, Disbursement.class);
		}
		return new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	public List<Disbursement> getAllMBDisbursement(Date date) {
		List<?> lst =  this.session.getNamedNativeQuery("getAllMBDisbursement")
				.setParameter("month", DateUtil.getCurrentMonth(date))
				.setParameter("year", DateUtil.getYear(date)).getResultList();
		
		if (null != lst && !lst.isEmpty()) {
			return transformList(lst, Disbursement.class);
		}
		return new ArrayList<>();
	}
}
