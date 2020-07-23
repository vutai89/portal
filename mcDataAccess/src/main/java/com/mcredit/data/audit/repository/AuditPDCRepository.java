package com.mcredit.data.audit.repository;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.FlushMode;
import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.audit.entity.AuditDifferent;
import com.mcredit.data.audit.entity.AuditPaymentDebtCollection;
import com.mcredit.data.audit.entity.PartnerMapping;
import com.mcredit.data.audit.entity.ReportOverviewDTO;
import com.mcredit.data.repository.IRepository;
import com.mcredit.model.dto.audit.AuditCommandDTO;
import com.mcredit.model.dto.audit.AuditDuplicateDTO;
import com.mcredit.model.dto.audit.ConsolidatePaymentDTO;
import com.mcredit.model.dto.audit.OverviewResultDTO;
import com.mcredit.model.enums.disaudit.AuditEnum;
import com.mcredit.model.enums.disaudit.ThirdParty;
import com.mcredit.util.DateUtil;
import com.mcredit.util.StringUtils;

public class AuditPDCRepository extends BaseRepository implements IRepository<AuditPaymentDebtCollection> {

	private SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
	private SimpleDateFormat dfs = new SimpleDateFormat("dd/MM/yyyy");

	public AuditPDCRepository(Session session) {
		super(session);
	}

	@SuppressWarnings("unchecked")
	public List<String> getTpDownloaded(String date, String thirdparty) throws ParseException {
		Date day = sdf.parse(date);
		day = new Date(day.getTime() - 1);
		StringBuilder str = new StringBuilder(
				"select distinct partner_name from partner p inner join consolidate_payment cp on cp.partner_id = p.id where to_char(cp.CONTRACT_DATE,'DDMMYYYY') = '")
						.append(sdf.format(day)).append("'");
		if (!thirdparty.equals(ThirdParty.ALL.value())) {
			str.append(" and p.partner_name = '").append(thirdparty).append("'");
		}
		List lst = this.session.createNativeQuery(str.toString()).getResultList();
		if (null != lst) {
			return (List<String>) lst;
		}
		return null;
	}

	public void deleteRecords(String date, String thirdParty) throws ParseException {
		Date day = sdf.parse(date);
		StringBuilder str = new StringBuilder(
				"delete from consolidate_payment where time_control != '17h' and to_char(contract_date,'DDMMYYYY') = '").append(sdf.format(day))
						.append("'");
		if (!thirdParty.equals(ThirdParty.ALL.value())) {
			str = str.append(" and partner_id = (select id from partner where partner_name = '").append(thirdParty)
					.append("')");
		}
		this.session.createNativeQuery(str.toString()).executeUpdate();
	}

	public void deleteRecords(String fromDate, String toDate, String thirdParty, String type) throws ParseException {
		StringBuilder str = new StringBuilder("delete from consolidate_payment where type = " + type
				+ " and contract_date <= TO_DATE('" + toDate + " 23:59:59', 'DD/MM/YYYY HH24:Mi:SS') "
				+ "and contract_date >= TO_DATE('" + fromDate + " 00:00:00','DD/MM/YYYY HH24:Mi:SS')");
		if (!thirdParty.equals(ThirdParty.ALL.value())) {
			str = str.append(" and partner_id = (select id from partner where partner_name = '").append(thirdParty)
					.append("')");
		}
		this.session.createNativeQuery(str.toString()).executeUpdate();
	}

	public void add(ConsolidatePaymentDTO item) throws ParseException {
		AuditPaymentDebtCollection audit = new AuditPaymentDebtCollection();
		if (!StringUtils.isNullOrEmpty(item.getContractDate())) {
			audit.setContractDate(dfs.parse(item.getContractDate()));
		}
		audit.setContractId(item.getContractId());
		audit.setMcContractFee(item.getMcContractFee());
		audit.setMcPartnerRefId(item.getMcPartnerRefId());
		audit.setMcStatus(item.getMcStatus());
		audit.setPartnerId(Integer.valueOf(item.getThirdParty()));
		audit.setResult(item.getResult());
		audit.setTpContractFee(item.getTpContractFee());
		audit.setTpPartnerRefId(item.getTpPartnerRefId());
		audit.setTpStatus(item.getTpStatus());
		audit.setType(item.getType());
		audit.setWorkFlow(item.getWorkFlow());
		audit.setTimeControl(item.getTimeControl());
		audit.setTpContractId(item.getTpContractId());
		this.session.save("AuditPaymentDebtCollection", audit);
	}

	public void add(AuditPaymentDebtCollection audit) {
		this.session.save("AuditPaymentDebtCollection", audit);
	}

	@SuppressWarnings("unchecked")
	public List<ReportOverviewDTO> getReport(String thirdParty, String fromDate, String toDate) {

		List<?> lst = this.session.getNamedNativeQuery("getReport").setParameter("thirdParty", thirdParty)
				.setParameter("fromDate", fromDate + " 00:00:00").setParameter("toDate", toDate + " 23:59:59")
				.getResultList();
		if (null != lst) {
			return (List<ReportOverviewDTO>) transformList(lst, ReportOverviewDTO.class);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<AuditDifferent> getDetailDifferent(String thirdParty, String fromDate, String toDate, String reportType,
			Long identity_type_id) {
		List<?> lst = this.session.getNamedNativeQuery("getDetailDifferent").setParameter("thirdParty", thirdParty)
				.setParameter("fromDate", fromDate + " 00:00:00").setParameter("toDate", toDate + " 23:59:59")
				.setParameter("type", reportType).setParameter("identity_type_id", identity_type_id).getResultList();

		if (null != lst && !lst.isEmpty()) {
			return (List<AuditDifferent>) transformList(lst, AuditDifferent.class);
		}

		return new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Integer> getMapThirdParty() {
		HashMap<String, Integer> lst = new HashMap<>();

		List<?> lstObj = this.session.getNamedNativeQuery("getThirdPartyMapping").getResultList();
		if (null != lstObj && !lstObj.isEmpty()) {
			List<PartnerMapping> lstPM = transformList(lstObj, PartnerMapping.class);
			for (PartnerMapping pm : lstPM) {
				lst.put(pm.getPartner_name(), pm.getId().intValue());
			}
		}

		return lst;
	}

	@SuppressWarnings("unchecked")
	public HashMap<Integer, String> getMapReturnThirdParty() {
		HashMap<Integer, String> lst = new HashMap<>();

		List<?> lstObj = this.session.getNamedNativeQuery("getThirdPartyMapping").getResultList();
		if (null != lstObj && !lstObj.isEmpty()) {
			List<PartnerMapping> lstPM = transformList(lstObj, PartnerMapping.class);
			for (PartnerMapping pm : lstPM) {
				lst.put(pm.getId().intValue(), pm.getPartner_name());
			}
		}

		return lst;
	}

	public int checkIfExist17h(String date) throws ParseException {

		Object obj = this.session.getNamedNativeQuery("checkIfExist17h").setParameter("from_date", date + " 00:00:00")
				.setParameter("to_date", date + " 23:59:59").setParameter("thirdParty", "VNPOST").uniqueResult();
		if (null == obj) {
			return AuditEnum.EXIST.intValue();
		} else {
			return (int) obj;
		}
	}

	public void removeVNPOST17h(String date) throws ParseException {

		this.session.getNamedNativeQuery("removeVNPOST17h").setParameter("contract_date", date)
				.setParameter("third_party", "VNPOST").executeUpdate();
	}

	public void deleteRecord17(String date) {
		this.session.getNamedNativeQuery("deleteRecord17").setParameter("date", date).executeUpdate();

	}

	public void deleteMBRecords(Date day) {
		this.session.getNamedNativeQuery("deleteMBRecords").setParameter("month", DateUtil.getCurrentMonth(day))
				.setParameter("year", DateUtil.getYear(day)).executeUpdate();
	}

	public void flush() {
		this.session.flush();
	}

	@SuppressWarnings("unchecked")
	public List<OverviewResultDTO> getGeneralReport(AuditCommandDTO auditCD) {
		StringBuilder sql = new StringBuilder(
				"select to_date(to_char(contract_date, 'DD/MM/YYYY'),'DD/MM/YYYY') as contract_date ,result, count(TP_PARTNER_REFID) as countTp,\r\n"
						+ "count(MC_PARTNER_REFID) as countMc, sum(TP_CONTRACT_FEE) as sumTp, sum(MC_CONTRACT_FEE) as sumMc\r\n"
						+ "from consolidate_payment where contract_date between to_date(:fromDate,'DD/MM/YYYY HH24:Mi:SS') and to_date(:toDate,'DD/MM/YYYY HH24:Mi:SS') \r\n"
						+ "and type = :type and result != :result ");
		if (!auditCD.getThirdParty().equals(ThirdParty.ALL.value())) {
			sql.append(" and partner_id = (select id from partner where partner_name = '" + auditCD.getThirdParty()
					+ "') ");
		}
		if (null != auditCD.getTime() && auditCD.getTime().equals(AuditEnum.TIME_CONTROL_17h.value())
				&& auditCD.getThirdParty().equals(ThirdParty.VNPOST.value())) {
			sql.append(" and time_control = '17h' ");
		} else {
			sql.append(" and time_control != '17h' ");
		}
		sql.append(
				"group by to_date(to_char(contract_date, 'DD/MM/YYYY'),'DD/MM/YYYY'), result order by to_date(to_char(contract_date, 'DD/MM/YYYY'),'DD/MM/YYYY') asc");

		Query query = this.session.createNativeQuery(sql.toString())
				.setParameter("fromDate", auditCD.getFromDate() + " 00:00:00")
				.setParameter("toDate", auditCD.getToDate() + " 23:59:59").setParameter("type", auditCD.getType())
				.setParameter("result", AuditEnum.DUPLICATE.value());

		List<?> lst = query.getResultList();
		if (null != lst && !lst.isEmpty()) {
			return transformList(lst, OverviewResultDTO.class);
		}
		return new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	public List<ConsolidatePaymentDTO> getDetailReport(String thirdParty, String fromDate, String toDate,
			String reportType, String result, String workflow, String time, int pageSize, int pageNum) {
		StringBuilder str = new StringBuilder(
				"select cp.id, cp.tp_partner_refid, cp.mc_partner_refid, cp.contract_id, cp.tp_contract_id, cp.type, cp.tp_contract_fee, cp.mc_contract_fee, to_char(cp.contract_date, 'DD/MM/YYYY'), cp.tp_status, cp.mc_status, "
						+ "cp.result, p.partner_name, cp.workflow, cp.time_control"
						+ " from consolidate_payment cp inner join partner p on p.id = cp.partner_id WHERE cp.type = :type and cp.contract_date <= TO_DATE(:toDate, 'DD/MM/YYYY HH24:Mi:SS') \r\n"
						+ "	and cp.contract_date >= TO_DATE(:fromDate,'DD/MM/YYYY HH24:Mi:SS') ");

		if (!thirdParty.equals(ThirdParty.ALL.value())) {
			str.append(" and p.partner_name = '" + thirdParty + "' ");
		}
		if (result.equals(AuditEnum.EQUAL.value())) {
			str.append(" and cp.result = '0' ");
		} else {
			str.append(" and cp.result != '0' and cp.result != '8'");
		}
		if (time.equals(AuditEnum.TIME_CONTROL_17h.value()) && thirdParty.equals(ThirdParty.VNPOST.value())) {
			str.append(" and cp.time_control = '17h' ");
		} else {
			str.append(" and cp.time_control != '17h' ");
		}
		str.append(" order by cp.contract_date, id asc");

		Query query = this.session.createNativeQuery(str.toString()).setParameter("toDate", toDate + " 23:59:59")
				.setParameter("fromDate", fromDate + " 00:00:00").setParameter("type", reportType)
				.setHibernateFlushMode(FlushMode.ALWAYS);
		;
		if (pageNum != 0 && pageSize != 0) {
			query.setFirstResult((pageNum - 1) * pageSize).setMaxResults(pageSize);
		}

		List<?> lst = query.getResultList();
		if (null != lst && !lst.isEmpty()) {
			return transformList(lst, ConsolidatePaymentDTO.class);
		}
		return new ArrayList<>();
	}

	public BigDecimal getNumDetailReport(String thirdParty, String fromDate, String toDate, String reportType,
			String result, String workflow, String time) {
		StringBuilder str = new StringBuilder(
				"select count(*) from consolidate_payment cp inner join partner p on p.id = cp.partner_id "
						+ "WHERE cp.type = :type and cp.contract_date <= TO_DATE(:toDate, 'DD/MM/YYYY HH24:Mi:SS') "
						+ "and cp.contract_date >= TO_DATE(:fromDate,'DD/MM/YYYY HH24:Mi:SS') ");
		if (!thirdParty.equals(ThirdParty.ALL.value())) {
			str.append(" and p.partner_name = '" + thirdParty + "' ");
		}
		if (result.equals(AuditEnum.EQUAL.value())) {
			str.append(" and cp.result = '0' ");
		} else {
			str.append(" and cp.result != '0' and cp.result != '8'");
		}
		if (time.equals(AuditEnum.TIME_CONTROL_17h.value()) && thirdParty.equals(ThirdParty.VNPOST.value())) {
			str.append(" and cp.time_control = '17h' ");
		} else {
			str.append(" and cp.time_control != '17h' ");
		}

		return (BigDecimal) this.session.createNativeQuery(str.toString()).setParameter("toDate", toDate + " 23:59:59")
				.setParameter("fromDate", fromDate + " 00:00:00").setParameter("type", reportType).uniqueResult();
	}

	public BigDecimal getDayCountReport(AuditCommandDTO auditCD) {
		StringBuilder str = new StringBuilder(
				"select count(distinct to_char(contract_date, 'DD/MM/YYYY')) from consolidate_payment where "
						+ "contract_date <= to_date(:toDate, 'DD/MM/YYYY HH24:Mi:SS') and contract_date >= to_date(:fromDate, 'DD/MM/YYYY HH24:Mi:SS') and type = :type and result != :result ");
		if (!auditCD.getThirdParty().equals(ThirdParty.ALL.value())) {
			str.append(" and partner_id = (select id from partner where partner_name = '" + auditCD.getThirdParty()
					+ "')");
		}
		if (null != auditCD.getTime() && auditCD.getTime().equals(AuditEnum.TIME_CONTROL_17h.value())
				&& auditCD.getThirdParty().equals(ThirdParty.VNPOST.value())) {
			str.append(" and time_control = '17h' ");
		} else {
			str.append(" and time_control != '17h' ");
		}
		return (BigDecimal) this.session.createNativeQuery(str.toString())
				.setParameter("toDate", auditCD.getToDate() + " 23:59:59")
				.setParameter("fromDate", auditCD.getFromDate() + " 00:00:00").setParameter("type", auditCD.getType())
				.setParameter("result", AuditEnum.DUPLICATE.value())
				.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public void getIntervalDate(AuditCommandDTO auditCD) {
		StringBuilder str = new StringBuilder(
				"SELECT MAX(DAY), MIN(DAY) FROM (select DAY, rownum r__ from (select distinct to_date(to_char(contract_date, 'DD/MM/YYYY'),'DD/MM/YYYY') as DAY "
						+ "from consolidate_payment where contract_date <= to_date(:toDate, 'DD/MM/YYYY HH24:Mi:SS') and contract_date >= to_date(:fromDate, 'DD/MM/YYYY HH24:Mi:SS') and type = :type "
						+ "and result != :result");
		if (!auditCD.getThirdParty().equals(ThirdParty.ALL.value())) {
			str.append(" and partner_id = (select id from partner where partner_name = '" + auditCD.getThirdParty()
					+ "')");
		}
		if (null != auditCD.getTime() && auditCD.getTime().equals(AuditEnum.TIME_CONTROL_17h.value())
				&& auditCD.getThirdParty().equals(ThirdParty.VNPOST.value())) {
			str.append(" and time_control = '17h' ");
		} else {
			str.append(" and time_control != '17h' ");
		}
		str.append(
				" order by DAY asc) where rownum < (:pageNumber * :pageSize) + 1) WHERE r__ >= (((:pageNumber - 1) * :pageSize) + 1)");
		List<Object[]> days = this.session.createNativeQuery(str.toString())
				.setParameter("toDate", auditCD.getToDate() + " 23:59:59")
				.setParameter("fromDate", auditCD.getFromDate() + " 00:00:00").setParameter("type", auditCD.getType())
				.setParameter("result", AuditEnum.DUPLICATE.value())
				.setParameter("pageNumber", auditCD.getPageNum()).setParameter("pageSize", auditCD.getPageSize())
				.getResultList();

		for (Object[] day : days) {
			if (null != day[0] && null != day[1]) {
				Date fromDate = (Date) day[1];
				Date toDate = (Date) day[0];
				auditCD.setFromDate(dfs.format(fromDate));
				auditCD.setToDate(dfs.format(toDate));
				break;
			}
		}
	}

	public BigDecimal getTotalMatchDeal(AuditCommandDTO auditCD) {
		StringBuilder str = new StringBuilder("select count(*) from consolidate_payment where result = '0' "
				+ "and contract_date <= to_date(:toDate, 'DD/MM/YYYY HH24:Mi:SS') and contract_date >= to_date(:fromDate, 'DD/MM/YYYY HH24:Mi:SS') and type = :type");
		if (!auditCD.getThirdParty().equals(ThirdParty.ALL.value())) {
			str.append(" and partner_id = (select id from partner where partner_name = '" + auditCD.getThirdParty()
					+ "')");
		}
		if (null != auditCD.getTime() && auditCD.getTime().equals(AuditEnum.TIME_CONTROL_17h.value())
				&& auditCD.getThirdParty().equals(ThirdParty.VNPOST.value())) {
			str.append(" and time_control = '17h' ");
		} else {
			str.append(" and time_control != '17h' ");
		}
		return (BigDecimal) this.session.createNativeQuery(str.toString())
				.setParameter("toDate", auditCD.getToDate() + " 23:59:59")
				.setParameter("fromDate", auditCD.getFromDate() + " 00:00:00").setParameter("type", auditCD.getType())
				.uniqueResult();
	}

	public BigDecimal getTotalMatchMoney(AuditCommandDTO auditCD) {
		StringBuilder str = new StringBuilder("select sum(mc_contract_fee) from consolidate_payment where result = '0' "
				+ "and contract_date <= to_date(:toDate, 'DD/MM/YYYY HH24:Mi:SS') and contract_date >= to_date(:fromDate, 'DD/MM/YYYY HH24:Mi:SS') and type = :type");
		if (!auditCD.getThirdParty().equals(ThirdParty.ALL.value())) {
			str.append(" and partner_id = (select id from partner where partner_name = '" + auditCD.getThirdParty()
					+ "')");
		}
		if (null != auditCD.getTime() && auditCD.getTime().equals(AuditEnum.TIME_CONTROL_17h.value())
				&& auditCD.getThirdParty().equals(ThirdParty.VNPOST.value())) {
			str.append(" and time_control = '17h' ");
		} else {
			str.append(" and time_control != '17h' ");
		}
		return (BigDecimal) this.session.createNativeQuery(str.toString())
				.setParameter("toDate", auditCD.getToDate() + " 23:59:59")
				.setParameter("fromDate", auditCD.getFromDate() + " 00:00:00").setParameter("type", auditCD.getType())
				.uniqueResult();
	}

	public BigDecimal getTotalUnMatchMcreditDeal(AuditCommandDTO auditCD) {
		StringBuilder str = new StringBuilder(
				"select count(*) from consolidate_payment where result != '0' and result not like '%1%' and result != '8' "
						+ "and contract_date <= to_date(:toDate, 'DD/MM/YYYY HH24:Mi:SS') and contract_date >= to_date(:fromDate, 'DD/MM/YYYY HH24:Mi:SS') and type = :type");
		if (!auditCD.getThirdParty().equals(ThirdParty.ALL.value())) {
			str.append(" and partner_id = (select id from partner where partner_name = '" + auditCD.getThirdParty()
					+ "')");
		}
		if (null != auditCD.getTime() && auditCD.getTime().equals(AuditEnum.TIME_CONTROL_17h.value())
				&& auditCD.getThirdParty().equals(ThirdParty.VNPOST.value())) {
			str.append(" and time_control = '17h' ");
		} else {
			str.append(" and time_control != '17h' ");
		}
		return (BigDecimal) this.session.createNativeQuery(str.toString())
				.setParameter("toDate", auditCD.getToDate() + " 23:59:59")
				.setParameter("fromDate", auditCD.getFromDate() + " 00:00:00").setParameter("type", auditCD.getType())
				.uniqueResult();
	}

	public BigDecimal getTotalUnMatchMcreditMoney(AuditCommandDTO auditCD) {
		StringBuilder str = new StringBuilder(
				"select sum(mc_contract_fee) from consolidate_payment where result != '0' and result not like '%1%' and result != '8' "
						+ "and contract_date <= to_date(:toDate, 'DD/MM/YYYY HH24:Mi:SS') and contract_date >= to_date(:fromDate, 'DD/MM/YYYY HH24:Mi:SS') and type = :type");
		if (!auditCD.getThirdParty().equals(ThirdParty.ALL.value())) {
			str.append(" and partner_id = (select id from partner where partner_name = '" + auditCD.getThirdParty()
					+ "')");
		}
		if (null != auditCD.getTime() && auditCD.getTime().equals(AuditEnum.TIME_CONTROL_17h.value())
				&& auditCD.getThirdParty().equals(ThirdParty.VNPOST.value())) {
			str.append(" and time_control = '17h' ");
		} else {
			str.append(" and time_control != '17h' ");
		}
		return (BigDecimal) this.session.createNativeQuery(str.toString())
				.setParameter("toDate", auditCD.getToDate() + " 23:59:59")
				.setParameter("fromDate", auditCD.getFromDate() + " 00:00:00").setParameter("type", auditCD.getType())
				.uniqueResult();
	}

	public BigDecimal getTotalUnMatchTPDeal(AuditCommandDTO auditCD) {
		StringBuilder str = new StringBuilder(
				"select count(*) from consolidate_payment where result != '0' and result not like '%2%' and result != '8' "
						+ "and contract_date <= to_date(:toDate, 'DD/MM/YYYY HH24:Mi:SS') and contract_date >= to_date(:fromDate, 'DD/MM/YYYY HH24:Mi:SS') and type = :type");
		if (!auditCD.getThirdParty().equals(ThirdParty.ALL.value())) {
			str.append(" and partner_id = (select id from partner where partner_name = '" + auditCD.getThirdParty()
					+ "')");
		}
		if (null != auditCD.getTime() && auditCD.getTime().equals(AuditEnum.TIME_CONTROL_17h.value())
				&& auditCD.getThirdParty().equals(ThirdParty.VNPOST.value())) {
			str.append(" and time_control = '17h' ");
		} else {
			str.append(" and time_control != '17h' ");
		}
		return (BigDecimal) this.session.createNativeQuery(str.toString())
				.setParameter("toDate", auditCD.getToDate() + " 23:59:59")
				.setParameter("fromDate", auditCD.getFromDate() + " 00:00:00").setParameter("type", auditCD.getType())
				.uniqueResult();
	}

	public BigDecimal getTotalUnMatchTPMoney(AuditCommandDTO auditCD) {
		StringBuilder str = new StringBuilder(
				"select sum(tp_contract_fee) from consolidate_payment where result != '0' and result not like '%2%' and result != '8' "
						+ "and contract_date <= to_date(:toDate, 'DD/MM/YYYY HH24:Mi:SS') and contract_date >= to_date(:fromDate, 'DD/MM/YYYY HH24:Mi:SS') and type = :type");
		if (!auditCD.getThirdParty().equals(ThirdParty.ALL.value())) {
			str.append(" and partner_id = (select id from partner where partner_name = '" + auditCD.getThirdParty()
					+ "')");
		}
		if (null != auditCD.getTime() && auditCD.getTime().equals(AuditEnum.TIME_CONTROL_17h.value())
				&& auditCD.getThirdParty().equals(ThirdParty.VNPOST.value())) {
			str.append(" and time_control = '17h' ");
		} else {
			str.append(" and time_control != '17h' ");
		}
		return (BigDecimal) this.session.createNativeQuery(str.toString())
				.setParameter("toDate", auditCD.getToDate() + " 23:59:59")
				.setParameter("fromDate", auditCD.getFromDate() + " 00:00:00").setParameter("type", auditCD.getType())
				.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<AuditDuplicateDTO> getDuplicate(AuditCommandDTO auditCD) {
		StringBuilder str = new StringBuilder(
				"select CP.ID, CP.TP_PARTNER_REFID, CP.TP_CONTRACT_ID, CP.TYPE, CP.TP_CONTRACT_FEE, TO_CHAR(CP.CONTRACT_DATE, 'DD/MM/YYYY'), "
						+ "CP.TP_STATUS, CP.RESULT, P.PARTNER_NAME, CP.WORKFLOW, CP.TIME_CONTROL FROM CONSOLIDATE_PAYMENT CP INNER JOIN PARTNER P ON P.ID = CP.PARTNER_ID "
						+ "WHERE CP.CONTRACT_DATE <= TO_DATE(:toDate, 'DD/MM/YYYY HH24:Mi:SS') AND CP.CONTRACT_DATE >= TO_DATE(:fromDate, 'DD/MM/YYYY HH24:Mi:SS') AND CP.RESULT = :result "
						+ "AND CP.TYPE = :type");
		if (!auditCD.getThirdParty().equals(ThirdParty.ALL.value())) {
			str.append(" AND P.PARTNER_NAME = '" + auditCD.getThirdParty() + "'");
		}
		if (null != auditCD.getTime() && auditCD.getTime().equals(AuditEnum.TIME_CONTROL_17h.value())
				&& auditCD.getThirdParty().equals(ThirdParty.VNPOST.value())) {
			str.append(" and CP.time_control = '17h' ");
		} else {
			str.append(" and CP.time_control != '17h' ");
		}
		List<?> lst = this.session.createNativeQuery(str.toString())
				.setParameter("toDate", auditCD.getToDate() + " 23:59:59")
				.setParameter("fromDate", auditCD.getFromDate() + " 00:00:00").setParameter("type", auditCD.getType())
				.setParameter("result", AuditEnum.DUPLICATE.value()).getResultList();

		if (null != lst && !lst.isEmpty()) {
			return transformList(lst, AuditDuplicateDTO.class);
		}
		return new ArrayList<>();
	}

}
