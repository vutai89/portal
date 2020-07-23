package com.mcredit.data.pcb.repository;

import java.util.Date;
import java.util.List;

import org.hibernate.query.Query;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.pcb.entity.CreditBureauData;
import com.mcredit.data.repository.IAddRepository;
import com.mcredit.data.repository.IUpdateRepository;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.data.telesale.entity.UplCustomerHistory;
import com.mcredit.model.dto.cic.CICRequestClaim;

import net.bytebuddy.implementation.bytecode.ByteCodeAppender.Size;

public class CreditBureauDataRepository extends BaseRepository implements IUpsertRepository<CreditBureauData>,IUpdateRepository<CreditBureauData>,IAddRepository<CreditBureauData> {

	public CreditBureauDataRepository(Session session) {
		super(session);
	}

	@Override
	public void add(CreditBureauData item) {
		this.session.save(item);
	}

	@Override
	public void update(CreditBureauData item) {
		this.session.update(item);
	}

	@Override
	public void upsert(CreditBureauData item) {
		this.session.saveOrUpdate("CreditBureauData", item);
	}
	
	public CreditBureauData getById(Long id) {
		return this.session.find(CreditBureauData.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public CreditBureauData getPCBResponse(String custIdentityNumber) {
		List<CreditBureauData> result = this.session.getNamedQuery("getPCBResponse").setParameter("custIdentityNumber", custIdentityNumber).getResultList();
		return !result.isEmpty() ? result.get(0) : null;
	}
	
	@SuppressWarnings("unchecked")
	public Long getCusId(String appId) {
		List<Long> lst = this.session.getNamedNativeQuery("getCusId").setParameter("appId", appId).addScalar("cust_id",LongType.INSTANCE).getResultList();
		return lst.isEmpty() ? null : lst.get(0);
	}
	
	/**
	 * @author truongvd.ho
	 * @desc get list bureau data by cust_id
	 * @param custId
	 * @param custIdOld
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CreditBureauData> getLstBureauDataByBirthDayAndIdent(String idCard) {
//		Query<CreditBureauData> query = this.session.createQuery(
//				"from CreditBureauData where ((custIdentityNumber in (:id_card,:id_card1) and birthDate = :birthDay) or (birthDate = :birthDay and custIdentityNumberOld in (:id_card,:id_card1))) and cbSource='P' order by created_date desc",
//				CreditBureauData.class).setParameter("id_card", idCard).setParameter("id_card1", idCard1).setParameter("birthDay", birthDay);
		Query<CreditBureauData> query = this.session.createQuery(
		"from CreditBureauData where custIdentityNumber = :id_card and cbSource='P' order by created_date desc",
//		CreditBureauData.class).setParameter("id_card", idCard).setParameter("id_card1", idCard1).setParameter("birthDay", birthDay);
		CreditBureauData.class).setParameter("id_card", idCard);
		List<CreditBureauData> lstCreditBureauData = query.list();
		return lstCreditBureauData;
	}	
	
	/***** CIC *****/
	
	/**
	 * Get CIC result by identity
	 * @author catld.ho
	 * @param custIdentityNumber
	 * @return CreditBureauData
	 */
	@SuppressWarnings("unchecked")
	public CreditBureauData getCICResponse(String custIdentityNumber) {
		List<CreditBureauData> result = this.session.getNamedQuery("getCICResponse").setParameter("custIdentityNumber", custIdentityNumber).getResultList();
		return !result.isEmpty() ? result.get(0) : null;
	}
	
	/**
	 * get CIC result in 15 days (default) by identity
	 * @author catld.ho
	 * @param custIdentityNumber
	 * @param cicResultExpireDays : number of days valid cic result
	 * @return CreditBureauData
	 */
	@SuppressWarnings("unchecked")
	public CreditBureauData getCICResponseAvailable(String custIdentityNumber, Long cicResultExpireDays) {
		List<CreditBureauData> result = this.session.getNamedQuery("getCICResponseAvailable")
				.setParameter("custIdentityNumber", custIdentityNumber)
				.setParameter("cicResultExpireDays", cicResultExpireDays, LongType.INSTANCE)
				.getResultList();
		return !result.isEmpty() ? result.get(0) : null;
	}
	
	/**
	 * Get list CIC result
	 * @author catld.ho
	 * @param listIdentity
	 * @param cicResultExpireDays
	 * @return list CreditBureauData
	 */
	@SuppressWarnings("unchecked")
	public List<CreditBureauData> getListCICResponseAvailable(List<String> listIdentity, Long cicResultExpireDays) {
		List<CreditBureauData> result = this.session.getNamedQuery("getListCICResponseAvailable")
				.setParameter("listIdentity", listIdentity)
				.setParameter("cicResultExpireDays", cicResultExpireDays, LongType.INSTANCE)
				.getResultList();
		return result;
	}
	
	/**
	 * Get customer id from table cust_personal_info by identity in type (1, 2 or 3: cmnd/cccd/cmqd)
	 * @author catld.ho
	 * @param identity
	 * @return cust id
	 */
	@SuppressWarnings("unchecked")
	public Long getCustIdByIdentity(String identity) {
		List<Long> lst = this.session.getNamedNativeQuery("getCustIdByIdentity").setParameter("identity", identity).addScalar("id",LongType.INSTANCE).getResultList();
		return lst.isEmpty() ? null : lst.get(0);
	}

	/**
	 * Get cic request with transaction type default is checkCICManual
	 * @author catld.ho
	 * @param username : login id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CICRequestClaim> claimCICRequest() {
		List<Object> lst = this.session.getNamedNativeQuery("claimCICRequest")
						.list();
		List<CICRequestClaim> cicRequest = transformList(lst, CICRequestClaim.class);
		return cicRequest;
	}
	
	@SuppressWarnings("unchecked")
	public List<CICRequestClaim> claimCICRequestAutoJob(Integer limit) {
		List<Object> lst = this.session.getNamedNativeQuery("claimCICRequestAutoJob")
						.setParameter("limit", limit, IntegerType.INSTANCE)
						.list();
		List<CICRequestClaim> cicRequest = transformList(lst, CICRequestClaim.class);
		return cicRequest;
	}
	
	@SuppressWarnings("unchecked")
	public List<CreditBureauData> getCICInfoById(List<Long> listId) {
		return this.session.getNamedQuery("getListCICInfoById").setParameter("listId", listId).getResultList();
	}
	
	/**
	 * update status cic request to claimed
	 * @author catld.ho
	 * @param username : login id
	 * @param listId
	 */
	public void updateStatusRequestClaimed(String username, List<Long> listId) {
		this.session.getNamedNativeQuery("updateStateAssign")
						.setParameter("username", username)
						.setParameter("listId", listId)
						.executeUpdate();
	}
//	
//	public void updateStatusRequestResult(String response, Long id) {
//		this.session.getNamedNativeQuery("updateStateResult")
//						.setParameter("response", response)
//						.setParameter("id", id)
//						.executeUpdate();
//	}
	
	/**
	 * update cic result
	 * @author catld.ho
	 * @param dataDetail : json cic result
	 * @param identity
	 */
	public void updateCICResult(String dataDetail, String identity) {
		this.session.getNamedNativeQuery("updateCICResult")
						.setParameter("dataDetail", dataDetail)
						.setParameter("cust_identity_number", identity)
						.executeUpdate();
	}
	
	/**
	 * Reset cic request status to N (New)
	 * @param username
	 */
	@SuppressWarnings("unchecked")
	public void resetStatusCICRequest(String username) {
		this.session.getNamedNativeQuery("resetCICRequestStatus")
						.setParameter("username", username)
						.executeUpdate();
	}
	
	public CreditBureauData getScoreByKey(String cbKey, String identityNumber, String phoneNumber, String oldIdentityNumber, String cbType, String returnCode) {
		Query query = this.session.createQuery(" from CreditBureauData " +
				" where cb_source = 'T' " + 
				" and (cb_type = :cb_type or '-1' = :cb_type) " + 
				" and cb_key = :cb_key " + 
				" and ((cust_identity_number = :cust_identity_number and cust_mobile = :cust_mobile) " + 
				" or ((cust_identity_number = :old_cust_identity_number and cust_mobile = :cust_mobile))) " + 
				" and (cb_code = :code or '-1' = :code) " +
				" order by last_updated_date desc ");
		List<CreditBureauData> lst  =  query
				.setParameter("cb_type", cbType != null ? cbType : "-1")
				.setParameter("cb_key", cbKey)
				.setParameter("cust_identity_number", identityNumber)
				.setParameter("old_cust_identity_number", oldIdentityNumber != null ? oldIdentityNumber :  "-1")
				.setParameter("cust_mobile", phoneNumber)
				.setParameter("code", returnCode != null ? returnCode : "-1")
				.getResultList();
		
		return lst.isEmpty() ? null : lst.get(0);
	}
	
	/**
	 * Get result pcb by citizenId.
	 * @param citizenId
	 * @return
	 */
	public CreditBureauData getPcbByCitizenId(String citizenId) {
		List<CreditBureauData> result = this.session.getNamedQuery("getPcbByCitizenId").setParameter("custIdentityNumber", citizenId).getResultList();
		return !result.isEmpty() ? result.get(0) : null;
	}
	
}
