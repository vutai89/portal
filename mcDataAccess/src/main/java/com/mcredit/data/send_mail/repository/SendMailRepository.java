package com.mcredit.data.send_mail.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.mcredit.data.BaseRepository;
import com.mcredit.model.dto.send_mail.DataMailDTO;
import com.mcredit.model.dto.send_mail.MailReasonDTO;

public class SendMailRepository extends BaseRepository {

	public SendMailRepository(Session session) {
		super(session);
	}
	
	@SuppressWarnings("unchecked")
	public List<DataMailDTO> getDataMail() {
		StringBuilder sql = new StringBuilder("select cb.appid appId ")
				.append(" ,cb.appnumber appNumber ")
				.append(" ,cb.customername custName ")
				.append(" ,cb.contractnumber contractNumber  ")
				.append(" ,to_char(cb.signContractDate, 'dd/MM/yyyy') contractDate  ")
				.append(" ,ct.description1 description ")
				.append(" ,ep.email emailCA ")
				.append(" ,epm.email emailLead ")
				.append(" from credit_app_request rq, credit_app_bpm bpm, code_table ct, cdb_data_entry cb, employees ep, employees epm ")
				.append(" where 1=1 ")
				.append(" and rq.id = bpm.credit_app_id ")
				.append(" and bpm.bpm_app_id = cb.appid ")
				.append(" and rq.status = ct.id   ")
				.append(" and ((ct.code_value1 ='POS_RETURN_OPEN' and ct.category ='STEP_STAT' ) or (ct.code_value1 ='POS_RETURN_PENDING' and ct.category ='STEP_STAT'))  ")
				.append(" and bpm.workflow ='InstallmentLoan'  ")
				.append(" and sysdate - bpm.created_date > 2  ")
				.append(" and ep.id = (select el.emp_id from employee_link el where el.emp_code = cb.salecode and rownum=1) ")
				.append(" and epm.id = (select els.manager_id from employee_link els where els.emp_id = ep.id and rownum=1) ");

		Query<?> query = this.session.createNativeQuery(sql.toString());
		List<?> lst = query.list();
		if (lst != null && !lst.isEmpty()) {
			return transformList(lst, DataMailDTO.class);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<MailReasonDTO> getReasonById(String appId) {
		StringBuilder sql = new StringBuilder("select distinct 1 id ")
				.append(",cd.reasondetailcode code ")
				.append(",cd.reasondetailcodelabel description ")
				.append(",cd.comment_column userComment ")
				.append("from cdb_operation_reason_for_return cd where 1=1 and cd.appid =:appId ");
		Query<?> query = this.session.createNativeQuery(sql.toString());
		query.setParameter("appId", appId);
		List<?> lst = query.list();
		if (lst != null && !lst.isEmpty()) {
			return transformList(lst, MailReasonDTO.class);
		}
		return null;
	}

}
