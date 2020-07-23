package com.mcredit.data.telesale.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.common.entity.Product;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.data.telesale.entity.Team;
import com.mcredit.data.telesale.entity.UplCustomer;
import com.mcredit.data.telesale.entity.UplCustomerHistory;
import com.mcredit.data.user.entity.Users;
import com.mcredit.model.dto.SendMarkApiDTO;

public class TelesaleRepository extends BaseRepository implements IUpsertRepository<Team> {

	public TelesaleRepository(Session session) {
		super(session);
	}

	public SendMarkApiDTO sendMarkToESB(String mobile, String identityNumber){
		List<?> lstResult = this.session.createNamedQuery("sendMarkToESB").setParameter("p_mobile",mobile).setParameter("p_identity_number",identityNumber).list();
		if(!lstResult.isEmpty())
			return (SendMarkApiDTO) transformList(lstResult, SendMarkApiDTO.class).get(0);
		else return null;
	}

	@Override
	public void upsert(Team item) {
		// TODO Auto-generated method stub
		
	}

	public Object getRoleCodeUserTls(String loginId) {
		List<?> lst = this.session.createNamedQuery("getRoleCodeUser").setParameter("p_login_id", loginId).list();
		return lst;
	}
	
	public Object getMemberNTB() {
		List<Users> lst = this.session.createNamedQuery("getMemberNTB").list();
		return lst; 
	}
	
	public Object getMemberByTeamLead(String loginId) {
		List<Users> lst = this.session.createNamedQuery("getMemberByTeamLead").setParameter("p_login_id", loginId).list();
		return lst;
	}
	
	@SuppressWarnings("unchecked")  
	public List<Long> getUplCustProspect(String mobile, String identityNumber) {
		List<BigDecimal> lstBigdecimal = this.session.createNamedQuery("findCustomerProspect").setParameter("p_identity_number", identityNumber).setParameter("p_mobile", mobile).list();
		List<Long> lstLongResult = new ArrayList<>();
		if(lstBigdecimal!=null && !lstBigdecimal.isEmpty()) {
			for(BigDecimal bigDecimalItem:lstBigdecimal) {
				lstLongResult.add(bigDecimalItem.longValue());
			}
		}
		/*return lstResult == null ? null : transformList(lstResult, UplCustomer.class);*/
		return lstLongResult;
	}
	
	public List<Long> getUplCustFromBPM(String mobile, String identityNumber) {
		List<BigDecimal> lstResult = this.session.createNamedQuery("findCustomerFromSrourceBPM").setParameter("p_identity_number", identityNumber).setParameter("p_mobile", mobile).list();
		List<Long> lstLongResult = new ArrayList<>();
		if(lstResult!=null && !lstResult.isEmpty()) {
			for(BigDecimal bigDecimalItem:lstResult) {
				lstLongResult.add(bigDecimalItem.longValue());
			}
		}
		/*return lstResult == null ? null : transformList(lstResult, UplCustomer.class);*/
		return lstLongResult;
	}
	
	public Object getClosestSuccessScore(String mobile, String identityNumber, String identityNumberOld) {
		List<UplCustomerHistory> lstResult = this.session.createNamedQuery("getClosestSuccessScore")
				.setParameter("p_identity_number", identityNumber.trim())
				.setParameter("p_identity_number_old", identityNumberOld == null ? "" : identityNumberOld.trim())
				.setParameter("p_mobile", mobile.trim()).list();
		return lstResult.isEmpty() ? null : transformList(lstResult, UplCustomerHistory.class).get(0);
	}
	
	public Object getClosestSuccessScoreSourceBPM(String mobile, String identityNumber, String identityNumberOld) {
		List<UplCustomerHistory> lstResult = this.session.createNamedQuery("getClosestSuccessScoreSourceBPM")
				.setParameter("p_identity_number", identityNumber.trim())
				.setParameter("p_identity_number_old", identityNumberOld == null ? "" : identityNumberOld.trim())
				.setParameter("p_mobile", mobile.trim()).list();
		return lstResult.isEmpty() ? null : transformList(lstResult, UplCustomerHistory.class).get(0);
	}
	
	public void updateUplCustWhenScoringFail(Long idUplCust) {
		this.session.createNamedQuery("updateUplCustWhenScoringFail").setParameter("p_id", idUplCust).executeUpdate();
	}
	
	public List<Long> getListUplCustBy(String mobile, String identityNumber) {
		List<BigDecimal> lstBigdecimal = this.session.createNamedQuery("findUcNotInUp").setParameter("p_identity_number", identityNumber).setParameter("p_mobile", mobile).list();
		List<Long> lstLongResult = new ArrayList<>();
		if(lstBigdecimal!=null && !lstBigdecimal.isEmpty()) {
			for(BigDecimal bigDecimalItem:lstBigdecimal) {
				lstLongResult.add(bigDecimalItem.longValue());
			}
		}
		/*return lstResult == null ? null : transformList(lstResult, UplCustomer.class);*/
		return lstLongResult;
		/*return lstResult.isEmpty() ? null : transformList(lstResult, UplCustomer.class);*/
		/*List<UplCustomer> lst = this.session.createQuery("from UplCustomer where mobile = :mobile and identityNumber=:identityNumber", UplCustomer.class)
				.setParameter("mobile", mobile != null ? mobile.trim() : "")
				.setParameter("identityNumber", identityNumber != null ? identityNumber.trim() : "").list();*/
		//return lstResult;
	}
	
	public boolean checkUserTsaHasDataXcell(Long userId){
		BigDecimal countDataXcell = (BigDecimal) this.session.createNamedQuery("checkTSAHasDataXcell").setParameter("id", userId).uniqueResult();
		return countDataXcell.compareTo(BigDecimal.ZERO) == 1 ? true : false;
	}
	
	public boolean checkUserTsaHasDataXcellExpired(Long userId){
		BigDecimal countDataXcellExpired = (BigDecimal) this.session.createNamedQuery("checkTSAHasDataXcellExpired").setParameter("id", userId).uniqueResult();
		return countDataXcellExpired.compareTo(BigDecimal.ZERO) == 1 ? true : false;
	}
	
	/**
	 * Check User TSA don't have data xcell
	 * 
	 * @param userId
	 * @return true (Prospect Status is F or W)
	 */
	public boolean checkUserTsaDontHaveDataXcellValidProspectStatus(Long userId){
		BigDecimal dataValid = (BigDecimal) this.session.createNamedQuery("checkTSADontHasDataXcellValidProspectStatus").setParameter("id", userId).uniqueResult();
		return dataValid.compareTo(BigDecimal.ZERO) == 1 ? true : false;
	}
	
	public void changeStatusUserTsa(Long userId, boolean isActive) {
		String status = isActive ? "A" : "C"; 
		String sqlString = "UPDATE users SET record_status = :status WHERE id = :userId";
		
		@SuppressWarnings("rawtypes")
		NativeQuery query = this.session.createNativeQuery(sqlString).setParameter("status", status).setParameter("userId", userId);
		
		query.executeUpdate();
		
	}
	
	public int changeStatusUserTsaInTeam(Long userId, boolean isActive) {
		String status = isActive ? "A" : "C"; 
		String sqlString = "UPDATE team_member SET status = :status WHERE user_id = :userId";
		
		@SuppressWarnings("rawtypes")
		NativeQuery query = this.session.createNativeQuery(sqlString).setParameter("status", status).setParameter("userId", userId);
		
		return query.executeUpdate();
		
	}
	
}
