package com.mcredit.data.debt_home.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.debt_home.entity.DebtHomeAssign;
import com.mcredit.data.repository.IAddRepository;
import com.mcredit.data.repository.IGetRepository;
import com.mcredit.data.repository.IRepository;
import com.mcredit.model.dto.debt_home.DebtContractInfo;
import com.mcredit.util.StringUtils;

public class DebtHomeAssignRepository extends BaseRepository implements IRepository<DebtHomeAssign>, IAddRepository<DebtHomeAssign>, IGetRepository<DebtHomeAssign> {

	public DebtHomeAssignRepository(Session session) {
		super(session);
	}

	@Override
	public DebtHomeAssign get(Long id) {
		return this.session.get(DebtHomeAssign.class, id);
	}

	@Override
	public void add(DebtHomeAssign item) {
		this.session.save(item);
	}
	
	public boolean checkPermision(String contractNumber, String user) {
		
		BigDecimal number = (BigDecimal) this.session.getNamedNativeQuery("checkAssignPermision")
				.setParameter("contract_number", contractNumber).setParameter("user", user).uniqueResult();
		return (number.longValue() > 0);
		
	}
	
	public String getAppNumberByContractNumber(String contractNumber) {
		Object appNumber = this.session.getNamedNativeQuery("findAppnumberByContractNumber").setParameter("contract_number", contractNumber).uniqueResult();
		return (appNumber != null) ? appNumber.toString() : null;
	}
	
	public String getContractNumberByAppNumber(Long appNumber) {
		Object contractNumber = this.session.getNamedNativeQuery("findContractNumberByAppNumber").setParameter("app_number", appNumber).uniqueResult();
		return (contractNumber != null) ? contractNumber.toString() : null;
	}
	
	public Integer disableExistData() {
		return this.session.getNamedNativeQuery("disableDebtHomeAssigns").executeUpdate();
	}
	
	public boolean checkExistContractNumber(String contractNumber) {
		BigDecimal number = (BigDecimal) this.session.getNamedNativeQuery("checkExistContractNumber").setParameter("contractNumber", contractNumber).uniqueResult();
		return (number.longValue() > 0);
	}
	
	/** 
	 * Get list contract info
	 * @param contractNumber
	 * @param appNumber
	 * @param mobilePhone
	 * @param identityNumber
	 * @return null or lstContractInfo
	 */
	@SuppressWarnings("unchecked")
	public List<DebtContractInfo> getListContractInfo(String contractNumber, String appNumber, String mobilePhone, String identityNumber) {
		List<DebtContractInfo> lstContractInfo = new ArrayList<DebtContractInfo>();
		System.out.println("===getListContractInfo==="+appNumber+"contractNumber==="+contractNumber+"mobilePhone==="+mobilePhone+"identityNumber==="+identityNumber);
		String sqlString = "SELECT cab.bpm_app_number, car.mc_contract_number, ci.IDENTITY_NUMBER, cci.CONTACT_VALUE, cpi.cust_name, cai.OLD_IDENTITY_NUMBER \r\n" + 
				"			FROM credit_app_bpm cab\r\n" + 
				"				inner JOIN CREDIT_APP_REQUEST car ON  car.id = cab.CREDIT_APP_ID\r\n" + 
				"				inner join CUST_IDENTITY ci on car.CUST_ID = ci.CUST_ID\r\n" + 
				"				inner join cust_contact_info cci on cci.CUST_ID = car.CUST_ID\r\n" + 
				"				inner join cust_personal_info cpi on cpi.id = ci.CUST_ID\r\n" + 
				"				inner join CUST_ADDL_INFO cai on cai.cust_id = car.cust_id " +
				"			WHERE cci.contact_type = (select id from code_table where category = 'CONTAC_TYP' and code_value1 = 'MOBILE')\r\n" + 
				"				and cci.contact_category = (select id from code_table where category = 'CONTAC_CAT' and code_value1 = 'CUSTOMER')\r\n";
		if(!StringUtils.isNullOrEmpty(appNumber)) {
			sqlString = sqlString + " and cab.bpm_app_number = :appNumber";
		}
		if(!StringUtils.isNullOrEmpty(contractNumber)) {
			sqlString = sqlString + " and car.mc_contract_number = :contractNumber";
		}
		if(!StringUtils.isNullOrEmpty(identityNumber)) {
			sqlString = sqlString + " and ci.identity_number = :identityNumber";
		}
		if(!StringUtils.isNullOrEmpty(mobilePhone)) {
			sqlString = sqlString + " and cci.CONTACT_VALUE = :mobilePhone";
		}
//		List lst = this.session.getNamedNativeQuery("findContractInfo").setParameter("appNumber", appNumber)
//				.setParameter("contractNumber", contractNumber).setParameter("identityNumber", identityNumber).setParameter("mobilePhone", mobilePhone)
//				.getResultList();
		NativeQuery query = this.session.createNativeQuery(sqlString);
		if(!StringUtils.isNullOrEmpty(appNumber)) {
			query.setParameter("appNumber", appNumber);
		}
		if(!StringUtils.isNullOrEmpty(contractNumber)) {
			query.setParameter("contractNumber", contractNumber);
		}
		if(!StringUtils.isNullOrEmpty(identityNumber)) {
			query.setParameter("identityNumber", identityNumber);
		}
		if(!StringUtils.isNullOrEmpty(mobilePhone)) {
			query.setParameter("mobilePhone", mobilePhone);
		}
		List<?> lst = query.getResultList();
		if (null != lst && !lst.isEmpty()) {
			lstContractInfo = transformList(lst, DebtContractInfo.class);
		}
		return lstContractInfo;
	}

}
