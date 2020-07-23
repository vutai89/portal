package com.mcredit.data.telesale.repository;

import java.util.Date;
import java.util.Formatter;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.data.telesale.entity.Team;
import com.mcredit.data.telesale.entity.UplCustomer;
import com.mcredit.model.dto.telesales.UplCustomerScoring2DTO;
import com.mcredit.model.dto.telesales.UplCustomerScoringDTO;
import com.mcredit.util.DateUtil;
import com.mcredit.util.StringUtils;

public class TeamRepository extends BaseRepository implements IUpsertRepository<Team> {

	public TeamRepository(Session session) {
		super(session);
	}

	public void upsert(Team item) {
		this.session.saveOrUpdate("Team", item);
	}

	public void remove(Team item) {
		this.session.delete("Team", item);
	}
	
	@SuppressWarnings("unchecked")
	public List<Team> listTeam(String teamType , String teamGroup) {
		return  this.session.getNamedQuery("listTeamByTeamTypeTeamgroup")
				.setParameter("teamType", teamType)
				.setParameter("teamGroup", teamGroup)
				.getResultList();
		
	}
	
	public List<?> listTeams(String teamGroup, String isAsm) {
		
		String strQuery = " SELECT t1.ID, t1.RECORD_STATUS, t1.CREATED_DATE, t1.LAST_UPDATED_DATE, t1.CREATED_BY, t1.LAST_UPDATED_BY, t1.MANAGER_ID, t1.TEAM_TYPE, t1.TEAM_GROUP, t1.TEAM_CODE, t1.TEAM_NAME, t1.STATUS, "
						+ " us.USR_FULL_NAME as  TeamLeadNAME "
						+ " FROM  TEAMS t1 "
						+ " INNER JOIN Users us on t1.MANAGER_ID = us.ID "
						+ " where team_Type = '"+ ("ASM".equals(isAsm)?"3":"S") + "' and t1.team_Group = '" + teamGroup + "' and t1.status ='A' Order by t1.team_Name " ;
		
		return this.session.createNativeQuery(strQuery).list();
	}
	
	public List<?> listAllActiveTeams(String asm, String xsm, String ntb) {
		
		String filterGroup = "";
		if( !"".equals(asm) && !"".equals(xsm) && !"".equals(ntb) )
			filterGroup = " ('LG', 'XS', 'NTB') ";
		else if( !"".equals(asm) && !"".equals(xsm) )
			filterGroup = " ('LG', 'XS') ";
		else if( !"".equals(asm) && !"".equals(ntb) )
			filterGroup = " ('LG', 'NTB') ";
		else if( !"".equals(xsm) && !"".equals(ntb) )
			filterGroup = " ('XS', 'NTB') ";
		else if( !"".equals(asm) )
			filterGroup = " ('LG') ";
		else if( !"".equals(xsm) )
			filterGroup = " ('XS') ";
		else // Only NTB rights
			filterGroup = " ('NTB') ";
		
		String strQuery = " SELECT DISTINCT t.ID, t.RECORD_STATUS, t.CREATED_DATE, t.LAST_UPDATED_DATE, t.CREATED_BY, t.LAST_UPDATED_BY, t.MANAGER_ID, t.TEAM_TYPE, t.TEAM_GROUP, "
						 + " t.TEAM_CODE, t.TEAM_NAME, t.STATUS, u.USR_FULL_NAME as teamLeadName "
					     + " FROM TEAMS t "
					     + " INNER JOIN USERS u on t.MANAGER_ID = u.ID "
					     + " WHERE t.STATUS = 'A' and t.TEAM_GROUP in " + filterGroup
					     + " ORDER BY t.TEAM_GROUP, t.TEAM_NAME " ;
		
		return this.session.createNativeQuery(strQuery).list();
	}
	
	public List<?> listTeams(String empId, String teamGroup, String isAsm) {
		
		String strQuery = " SELECT DISTINCT t.ID, t.RECORD_STATUS, t.CREATED_DATE, t.LAST_UPDATED_DATE, t.CREATED_BY, t.LAST_UPDATED_BY, t.MANAGER_ID, t.TEAM_TYPE, t.TEAM_GROUP, t.TEAM_CODE, t.TEAM_NAME, t.STATUS, " 
						 + " u.USR_FULL_NAME as teamLeadName "
					     + " FROM TEAMS t "
					     + " INNER JOIN Users u on t.MANAGER_ID = u.ID "
					     //+ " INNER JOIN EMPLOYEES e on u.EMP_ID = e.ID "
					     + " INNER JOIN EMPLOYEE_LINK el on u.EMP_ID = el.EMP_ID "
					     + " WHERE el.MANAGER_ID = :empId and t.STATUS ='A' "
					     //+ " AND t.team_Type = 'S' and t.team_Group = 'T' "
					     + " ORDER BY t.TEAM_NAME " ;
		
		return this.session.createNativeQuery(strQuery)
				.setParameter("empId", empId)
				.list();
	}
	
	public List<?> listTeamsNew(String teamType , String teamGroup) {
		
		String strQuery = "SELECT t1.ID, t1.RECORD_STATUS, t1.CREATED_DATE, t1.LAST_UPDATED_DATE, t1.CREATED_BY, t1.LAST_UPDATED_BY, t1.MANAGER_ID, t1.TEAM_TYPE, t1.TEAM_GROUP, t1.TEAM_CODE, t1.TEAM_NAME, t1.STATUS,"
						+" us.USR_FULL_NAME as  TeamLeadNAME "
						+" FROM  TEAMS t1 "
						+" INNER JOIN Users us on t1.MANAGER_ID = us.ID "
						+" where team_Type='"+ teamType + "' and t1.team_Group='" + teamGroup + "' and t1.status ='A' Order by t1.team_Name  " ;
		
		return this.session.createNativeQuery(strQuery).list();
	}

	@SuppressWarnings("unchecked")
	public List<Team> getTeamByTeamleadLoginID(Long managerId) {	
		
		return this.session.getNamedQuery("getTeamByTeamleadLoginID").setParameter("managerId", managerId).list();
	}
  
	public List<UplCustomerScoringDTO> lstUplCusByTSACode(String tsaCode, String identityNumber, String mobile, String sendDateFrom, String sendDateTo, String loginId) {

		Query<?> query =  this.session.getNamedQuery("lstUplCusDetailsV2")
				.setParameter("tsaCode", !StringUtils.isNullOrEmpty(tsaCode) ? tsaCode : "-1")
				.setParameter("identityNumber", !StringUtils.isNullOrEmpty(identityNumber) ? identityNumber : "-1")
				.setParameter("mobile", !StringUtils.isNullOrEmpty(mobile) ? mobile : "-1")
				.setParameter("sendDateFrom", !StringUtils.isNullOrEmpty(sendDateFrom) ? sendDateFrom : "01/01/1900 00:00:00") 
				.setParameter("sendDateTo", !StringUtils.isNullOrEmpty(sendDateTo) ? sendDateTo : "01/01/9999 00:00:00")
				.setParameter("loginId", loginId);
		List<Object> lstObj = (List<Object>) query.list();
		List<UplCustomerScoringDTO> result = transformList(lstObj, UplCustomerScoringDTO.class);
		return result;
	}
	
	public List<UplCustomerScoringDTO> findCustomerSupervisor(String tsaCode, String identityNumber, String mobile, String sendDateFrom, String sendDateTo, String loginId) {
		
		Query<?> query =  this.session.getNamedQuery("lstUplCusDetails_SupV2")
				.setParameter("tsaCode", !StringUtils.isNullOrEmpty(tsaCode) ? tsaCode : "-1")
				.setParameter("identityNumber", !StringUtils.isNullOrEmpty(identityNumber) ? identityNumber : "-1")
				.setParameter("mobile", !StringUtils.isNullOrEmpty(mobile) ? mobile : "-1")
				.setParameter("sendDateFrom", !StringUtils.isNullOrEmpty(sendDateFrom) ? sendDateFrom : "01/01/1900 00:00:00") 
				.setParameter("sendDateTo", !StringUtils.isNullOrEmpty(sendDateTo) ? sendDateTo : "01/01/9999 00:00:00");
		List<Object> lstObj = (List<Object>) query.list();
		List<UplCustomerScoringDTO> result = transformList(lstObj, UplCustomerScoringDTO.class);
		return result;
	}
	
	public UplCustomerScoring2DTO getUplCustForScoring(Long upl_cust_id) {
		List<Object> lstObj = this.session.getNamedQuery("getUplCustForScoring")
				.setParameter("upl_cust_id", upl_cust_id)
				.list();
		
		List<UplCustomerScoring2DTO> result = transformList(lstObj, UplCustomerScoring2DTO.class);
		
		return result.size() == 0 ? null : result.get(0);
	}

}
