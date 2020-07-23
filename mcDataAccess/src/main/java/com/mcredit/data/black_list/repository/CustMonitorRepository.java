package com.mcredit.data.black_list.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

import com.google.gson.Gson;
import com.mcredit.data.BaseRepository;
import com.mcredit.data.black_list.entity.CustMonitor;
import com.mcredit.data.repository.IRepository;
import com.mcredit.model.dto.black_list.BlackListResponseDTO;
import com.mcredit.model.dto.black_list.CustMonitorDTO;


public class CustMonitorRepository extends BaseRepository implements IRepository<CustMonitor> {
	
	public CustMonitorRepository(Session session) {
		super(session);
	}
	
	public void update(List<String> idNumber, String userName, String status) {
		System.out.println("List update status: " + status + new Gson().toJson(idNumber));
		StringBuilder sql = new StringBuilder("update cust_monitor mo set mo.record_status= :status, mo.last_updated_by =:userName, mo.last_updated_date=sysdate where mo.id_number in (:idNumber) ");
		this.session.createNativeQuery(sql.toString()).setParameterList("idNumber", idNumber)
		.setParameter("userName", userName)
		.setParameter("status", status)
		.executeUpdate();
	}
	
	public void update(String idNumber, String userName, String status, CustMonitorDTO obj) {
		System.out.println("Update idNumber: " + idNumber + " request: " + new Gson().toJson(obj));
		StringBuilder sql = new StringBuilder("update cust_monitor mo set mo.record_status= :status, mo.last_updated_by =:userName, mo.last_updated_date=sysdate ")
				.append(",mo.id_type =:idType ")
				.append(",mo.cust_name =:custName ")
				.append("where mo.id_number in (:idNumber) ");
		this.session.createNativeQuery(sql.toString()).setParameter("idNumber", idNumber)
		.setParameter("userName", userName)
		.setParameter("status", status)
		.setParameter("idType", obj.getIdType())
		.setParameter("custName", obj.getCustName())
		.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<Long> findIdNumberByCode(List<String> code) {
		List<Long> lst = this.session.getNamedNativeQuery("findIdNumberByCode").setParameterList("codeValue", code).getResultList();
		return (CollectionUtils.isEmpty(lst)) ? new ArrayList<>() : lst;
	}
	
	@SuppressWarnings("unchecked")
	public List<Long> findIdNumberByStatus() {
		List<Long> lst = this.session.getNamedNativeQuery("findIdNumberByStatus").getResultList();
		return (CollectionUtils.isEmpty(lst)) ? new ArrayList<>() : lst;
	}
	
	@SuppressWarnings("unchecked")
	public List<CustMonitorDTO> findIdByCodeValue() {
		StringBuilder sql = new StringBuilder("select ct.id, ct.code_value1 from code_table ct where ct.record_status = 'A' ")
				.append("and ct.category = 'IDTYP' and ct.code_value1 in (1,3,5) ");
		Query<?> query = this.session.createNativeQuery(sql.toString());
		List<?> lst = query.list();

		if (lst != null && !lst.isEmpty()) {
			return transformList(lst, CustMonitorDTO.class);
		}

		return null;
	}
	
	public void insert(CustMonitorDTO obj, String userName) {
		this.session.getNamedNativeQuery("insertCustMonitor")
				.setParameter("createdBy", userName)
				.setParameter("custName", obj.getCustName())
				.setParameter("idNumber", obj.getIdNumber())
				.setParameter("idType", obj.getIdType())
				.setParameter("lastUpdateBy", userName)
				.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<BlackListResponseDTO> findBlackList(String idNumber, List<String> lstCode) {
		StringBuilder sql = new StringBuilder("select distinct cm.cust_name, cm.id_number, ct.description1 from cust_monitor cm, code_table ct ")
				.append("where 1=1 and cm.id_number=:idNumber and cm.record_status = 'A' ")
				.append("and cm.id_type in (select tb.id from code_table tb where tb.category='IDTYP' and tb.code_value1 in (:lstCode)) ")
				.append("and cm.id_type = ct.id");
		Query<?> query = this.session.createNativeQuery(sql.toString());
		query.setParameter("idNumber", idNumber).setParameterList("lstCode", lstCode);
		List<?> lst = query.list();
		if (lst != null && !lst.isEmpty()) {
			return transformList(lst, BlackListResponseDTO.class);
		}

		return null;
		
	}
}
