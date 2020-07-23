package com.mcredit.data.telesale.repository;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.data.telesale.entity.TeamMember;
import com.mcredit.data.user.entity.Users;
import com.mcredit.util.StringUtils;

public class TeamMemberRepository extends BaseRepository implements IUpsertRepository<TeamMember> {

	public TeamMemberRepository(Session session) {
		super(session);
	}

	public void upsert(TeamMember item) {
		this.session.saveOrUpdate("TeamMember", item);
	}

	public void remove(TeamMember item) {
		this.session.delete("TeamMember", item);
	}
	
	@SuppressWarnings("unchecked")
	public List<TeamMember> getListMemberByTeamId(String teamId){
		return this.session.getNamedQuery("getListMemberByTeamId").setParameter("teamId", teamId).getResultList();
	}

	public Object findTeamIdOwnerByUplMasterId(Long uplMasterId) {
		 List<?> lst =  this.session.getNamedNativeQuery("findTeamIdOwnerByUplMasterId").setParameter("uplMasterId", uplMasterId).list();
		 if( lst!=null && lst.size()>0 )
			 return  lst.get(0);
			return null;
	}
	
	public List<Users> findTeamMemberByOnwerId(Long onwerId) {
		return this.session.createNativeQuery("select ID,RECORD_STATUS,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,LOGIN_ID,USR_FULL_NAME,USR_TYPE,EMP_ID,STATUS,START_EFF_DATE,END_EFF_DATE from users where users.id in (select USER_ID from TEAM_MEMBER where TEAM_ID = (select TEAM_ID from TEAM_MEMBER where USER_ID = :USER_ID) and Status = 'A') and record_status = 'A' ",Users.class).setParameter("USER_ID", onwerId).getResultList();
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	//findType: reAssign || allcation
	public List<Users> findTeamMemberByTeamLeadId(Long teamLeadId, String findType, String campaignType) {
		
		List<Users> results = new ArrayList<>();
		String sql =  "";
		NativeQuery query = null;
		
		if( "reAssign".equals(findType) ) {
			
			sql = " select users.id, users.created_by, users.created_date, users.last_updated_by, users.last_updated_date, users.login_id, users.emp_id, users.usr_full_name, "
					+ " users.usr_type, users.status, users.start_eff_date, users.end_eff_date, users.record_status, allocatedfreshnumber, allocatedwipnumber "
					+ " from ( SELECT temptable.id, "
					+ " sum(nvl(ad_f.allocated_number,0)) AS allocatedfreshnumber, "
					+ " (select nvl(SUM(ad_w.allocated_number), 0) from allocation_detail ad_w where temptable.id = ad_w.ASSIGNEE_ID AND ad_w.status = ( SELECT id FROM code_table WHERE code_group = 'CALL' AND category = 'ALCTYPE_TL' AND code_value1 = 'W' )) as allocatedwipnumber "
					+ " FROM ( SELECT * FROM users WHERE users.id IN ( SELECT allocation_detail.ASSIGNEE_ID FROM allocation_detail WHERE  allocation_detail.ASSIGNEE_ID in ( SELECT tm.USER_ID FROM TEAM_MEMBER tm  INNER JOIN TEAMS t on tm.TEAM_ID = t.ID WHERE t.MANAGER_ID = :teamLeadId ) ) ) temptable  "
					+ " LEFT JOIN allocation_detail ad_f ON ( temptable.id = ad_f.ASSIGNEE_ID AND ad_f.status = ( "
					+ " SELECT id FROM code_table WHERE code_group = 'CALL' AND category = 'ALCTYPE_TL' AND code_value1 = 'F' ) ) "
					+ " group by temptable.id ) tempTable1 inner join users on users.id = tempTable1.id ";
			
			query = this.session.createNativeQuery(sql)
					.setParameter("teamLeadId", teamLeadId);

		}else if( "reAssignFullMember".equals(findType) ) {
			
			/*sql =  " select u.ID, '' as created_by, null as created_date, '' as last_updated_by, null as last_updated_date, u.login_id, 0 as emp_id, nvl(u.USR_FULL_NAME, e.full_name), '' as usrType, '' as status, null as startEffDate, null as endEffDate, '' as recordStatus "
					+ " , 0 as allocatedFreshNumber, 0 as allocatedWipNumber, 0 as allocationMasterId, '' as superVisor, 0 as bdsId, '' as bdsCode, nvl(u2.USR_FULL_NAME, e2.full_name) as teamLeadName "
					+ " from users u "
					+ " left join employees e on e.id = u.emp_id "
					+ " inner join TEAM_MEMBER tm on (u.id = tm.user_id and u.status = 'A' and tm.status = 'A') "
					+ " inner join TEAMS t on (t.id = tm.TEAM_ID and t.status = 'A') "
					+ " inner join users u2 on u2.ID = t.MANAGER_ID "
					+ " left join employees e2 on e2.id = u2.emp_id "
					+ " where " + ( !"".equals(campaignType) ? " t.TEAM_GROUP = :campaignType " : " t.TEAM_GROUP in ('XS', 'NTB', 'LG') " );
			
			query = this.session.createNativeQuery(sql);
			if( !"".equals(campaignType) )
				query.setParameter("campaignType", campaignType);*/
			
			sql =  " select u.ID, '' as created_by, null as created_date, '' as last_updated_by, null as last_updated_date, u.login_id, 0 as emp_id, nvl(u.USR_FULL_NAME, e.full_name), '' as usrType, '' as status, null as startEffDate, null as endEffDate, '' as recordStatus "
					+ " , 0 as allocatedFreshNumber, 0 as allocatedWipNumber, 0 as allocationMasterId, '' as superVisor, 0 as bdsId, '' as bdsCode, nvl(u2.USR_FULL_NAME, e2.full_name) as teamLeadName "
					+ " from users u "
					+ " left join employees e on e.id = u.emp_id "
					+ " inner join TEAM_MEMBER tm on (u.id = tm.user_id and u.status = 'A' and tm.status = 'A') "
					+ " inner join TEAMS t on (t.id = tm.TEAM_ID and t.status = 'A') "
					+ " inner join users u2 on u2.ID = t.MANAGER_ID "
					+ " left join employees e2 on e2.id = u2.emp_id "
					+ " where t.MANAGER_ID = :teamLeadId ";
			
			query = this.session.createNativeQuery(sql)
					.setParameter("teamLeadId", teamLeadId);
			
		}else if( "addMemberToList".equals(findType) ) {
			
			sql =  " select u.id, u.created_by, u.created_date, u.last_updated_by, u.last_updated_date, u.login_id, u.emp_id, u.usr_full_name, u.usr_type, u.status, u.start_eff_date, u.end_eff_date, u.record_status " +
						" , 0 as allocatedfreshnumber, 0 as allocatedwipnumber, 0 as allocationMasterId, '' as superVisor, 0 as bdsId, '' as bdsCode, '' as teamLead, '' as empCode, '' as email, '' as departmentName " +
						" , null as userSessionCreatedTime, '' as extNumber, '' as deviceName, count (u.id) as allocatedNumber " +
						" from USERS u " +
						" inner join ALLOCATION_DETAIL ad on (u.ID = ad.ASSIGNEE_ID and ad.OBJECT_TYPE = 'U') " +
						" inner join CODE_TABLE ct on (ct.ID = ad.STATUS) " +
						" inner join UPL_DETAIL ud on ud.ID = ad.UPL_DETAIL_ID " +
						" inner join UPL_MASTER um on um.ID = ud.UPL_MASTER_ID " +
						" where u.ID = 224745 and ct.CODE_VALUE1 = 'F' and um.ID = 2111 " +
						" group by u.id, u.created_by, u.created_date, u.last_updated_by, u.last_updated_date, u.login_id, u.emp_id, u.usr_full_name, u.usr_type, u.status, u.start_eff_date, u.end_eff_date, u.record_status ";
			
			query = this.session.createNativeQuery(sql)
					.setParameter("campaignType", campaignType);
			
		}else {
			
//			sql =  " select distinct "
//					+ " u.ID, u.CREATED_BY, u.CREATED_DATE, u.LAST_UPDATED_BY, u.LAST_UPDATED_DATE, u.LOGIN_ID,u.EMP_ID, u.USR_FULL_NAME, u.USR_TYPE, "
//					+ "  u.STATUS, u.START_EFF_DATE, u.END_EFF_DATE, u.RECORD_STATUS, "
//					+ " nvl(ad_f.ALLOCATED_NUMBER, 0) as allocatedFreshNumber, nvl(ad_w.ALLOCATED_NUMBER, 0) as allocatedWipNumber, "
//					+ " nvl(nvl(ad_f.ALLOCATION_MASTER_ID, ad_w.ALLOCATION_MASTER_ID), 0) as allocationMasterId , "
//					+ " t.MANAGER_ID as teamLeadId "
//					+ " from users u "
//					+ " left join TEAM_MEMBER tm on tm.id = u.id "
//					+ " left join TEAMS t on t.id = tm.TEAM_ID "
//					+ " left join ALLOCATION_DETAIL ad_f on (u.id = ad_f.ASSIGNEE_ID and ad_f.STATUS = (select id from CODE_TABLE where code_group='CALL' and category='ALCTYPE_TL' and code_value1='F')) "
//					+ " left join ALLOCATION_DETAIL ad_w on (u.id = ad_w.ASSIGNEE_ID and ad_w.STATUS = (select id from CODE_TABLE where code_group='CALL' and category='ALCTYPE_TL' and code_value1='W')) "
//					+ " where u.id in (select team_member.USER_ID "
//					+ " from teams inner join team_member on teams.ID = team_member.TEAM_ID where teams.manager_id = :teamLeadId and team_member.status = 'A') "
//					+ " and u.record_status = 'A' ";
			
			// lỗi query lâu: front-end chỉ dùng thông tin member, không cần lấy những thông tin khác
			sql =  " select distinct "
					+ " u.ID, u.CREATED_BY, u.CREATED_DATE, u.LAST_UPDATED_BY, u.LAST_UPDATED_DATE, u.LOGIN_ID,u.EMP_ID, u.USR_FULL_NAME, u.USR_TYPE, "
					+ "  u.STATUS, u.START_EFF_DATE, u.END_EFF_DATE, u.RECORD_STATUS, "
//					+ " nvl(ad_f.ALLOCATED_NUMBER, 0) as allocatedFreshNumber, nvl(ad_w.ALLOCATED_NUMBER, 0) as allocatedWipNumber, "
//					+ " nvl(nvl(ad_f.ALLOCATION_MASTER_ID, ad_w.ALLOCATION_MASTER_ID), 0) as allocationMasterId , "
					+ " 1 as allocatedFreshNumber, 1 as allocatedWipNumber, "
					+ " 0 as allocationMasterId , "
					+ " t.MANAGER_ID as teamLeadId "
					+ " from users u "
					+ " left join TEAM_MEMBER tm on tm.id = u.id "
					+ " left join TEAMS t on t.id = tm.TEAM_ID "
//					+ " left join ALLOCATION_DETAIL ad_f on (u.id = ad_f.ASSIGNEE_ID and ad_f.STATUS = (select id from CODE_TABLE where code_group='CALL' and category='ALCTYPE_TL' and code_value1='F')) "
//					+ " left join ALLOCATION_DETAIL ad_w on (u.id = ad_w.ASSIGNEE_ID and ad_w.STATUS = (select id from CODE_TABLE where code_group='CALL' and category='ALCTYPE_TL' and code_value1='W')) "
					+ " where u.id in (select team_member.USER_ID "
					+ " from teams inner join team_member on teams.ID = team_member.TEAM_ID where teams.manager_id = :teamLeadId and team_member.status = 'A') "
					+ " and u.record_status = 'A' ";
			
			query = this.session.createNativeQuery(sql)
					.setParameter("teamLeadId", teamLeadId);
		}
		
		List lst = query.list();
		
		if (lst != null && lst.size() > 0)
			results =  transformList(lst, Users.class);
		
		return results;
	}
	
	public Users findTeamMemberAllocatedNumber(Long memberId, String prospectStatus, Long campaignId, Long callStatus, Long callResult, String identityNumber, String receiveDate) {
		
		Users results = new Users();
			
		String condition = "";
		
		if( campaignId!= null && campaignId > 0 )
			condition += " and um.ID = :campaignId ";
		
		if( !StringUtils.isNullOrEmpty(prospectStatus) )
			condition += " and ct.CODE_VALUE1 = :prospectStatus ";
		
		if( callStatus!= null && callStatus > 0 )
			condition += " and cr.CALL_STATUS = :callStatus ";
		
		if( callResult!= null && callResult > 0 )
			condition += " and cr.CALL_RESULT in ( select ID from CODE_TABLE where CODE_GROUP = 'CALL' and CATEGORY = 'CALL_RSLT' and upper(DESCRIPTION1) = ( "
				       	    + " select upper(DESCRIPTION1) from CODE_TABLE where CODE_GROUP = 'CALL' and CATEGORY = 'CALL_RSLT' and ID = :callResult "
				       	 + " ) "
				       + " ) ";
		
		if( !"".equals(identityNumber) )
			condition += " and (cp.IDENTITY_NUMBER = :identityNumber or uc.IDENTITY_NUMBER = :identityNumber) ";
		
		if( !"".equals(receiveDate) )
			condition += " and to_char(ud.LAST_UPDATED_DATE, 'DD/MM/YYYY') = :receiveDate ";
		
		String sql =  " select u.id, u.created_by, u.created_date, u.last_updated_by, u.last_updated_date, u.login_id, u.emp_id, u.usr_full_name, u.usr_type, u.status, u.start_eff_date, u.end_eff_date, u.record_status " +
					" , 0 as allocatedfreshnumber, 0 as allocatedwipnumber, 0 as allocationMasterId, '' as superVisor, 0 as bdsId, '' as bdsCode, '' as teamLead, '' as empCode, '' as email, '' as departmentName " +
					" , null as userSessionCreatedTime, '' as extNumber, '' as deviceName, count (u.id) as allocatedNumber " +
					" from USERS u " +
					" inner join ALLOCATION_DETAIL ad on (u.ID = ad.ASSIGNEE_ID and ad.OBJECT_TYPE = 'U') " +
					" inner join CODE_TABLE ct on (ct.ID = ad.STATUS) " +
					" inner join UPL_DETAIL ud on ud.ID = ad.UPL_DETAIL_ID " +
					" inner join UPL_MASTER um on um.ID = ud.UPL_MASTER_ID " +
					" inner join UPL_CUSTOMER uc on ad.UPL_OBJECT_ID = uc.ID " +
					" left join CUST_PROSPECT cp on ad.UPL_OBJECT_ID = cp.UPL_CUSTOMER_ID " +
					" left join (select * from CALL_RESULT a where exists (select 1 from " +
					" (select CUST_PROSPECT_ID,max(CALL_DATE) CALL_DATE from CALL_RESULT group by CUST_PROSPECT_ID) b " +
					" where b.CALL_DATE = a.CALL_DATE and b.CUST_PROSPECT_ID = a.CUST_PROSPECT_ID)) cr on cr.CUST_PROSPECT_ID = cp.ID " +
					" where u.ID = :memberId " + condition +
					" group by u.id, u.created_by, u.created_date, u.last_updated_by, u.last_updated_date, u.login_id, u.emp_id, u.usr_full_name, u.usr_type, u.status, u.start_eff_date, u.end_eff_date, u.record_status ";
		
		@SuppressWarnings("rawtypes")
		NativeQuery query = this.session.createNativeQuery(sql)
				.setParameter("memberId", memberId);
		
		if( campaignId!= null && campaignId > 0 )
			query.setParameter("campaignId", campaignId);
		
		if( !StringUtils.isNullOrEmpty(prospectStatus) )
			query.setParameter("prospectStatus", prospectStatus);
		
		if( callStatus!= null && callStatus > 0 )
			query.setParameter("callStatus", callStatus);
		
		if( callResult!= null && callResult > 0 )
			query.setParameter("callResult", callResult);
		
		if( !"".equals(identityNumber) )
			query.setParameter("identityNumber", identityNumber.trim());
		
		if( !"".equals(receiveDate) )
			query.setParameter("receiveDate", receiveDate);
		
		Object obj = query.uniqueResult();
		
		if (obj != null)
			results =  transformObject(obj, Users.class);
		
		return results;
	}
	
	public Users findTeamLeader(Long memberId) {
		return this.session.createNativeQuery("select ID,RECORD_STATUS,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,LOGIN_ID,USR_FULL_NAME,USR_TYPE,EMP_ID,STATUS,START_EFF_DATE,END_EFF_DATE from USERS where ID =(select TEAMS.MANAGER_ID from team_member inner join TEAMS on team_member.TEAM_ID = TEAMS.ID where USER_ID = :USER_ID)",Users.class).setParameter("USER_ID", memberId).getSingleResult();
	}
	
	public List<Users> findTeamLeaders(Long memberId) {
		return this.session.createNativeQuery("select ID,RECORD_STATUS,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,LOGIN_ID,USR_FULL_NAME,USR_TYPE,EMP_ID,STATUS,START_EFF_DATE,END_EFF_DATE from USERS where ID In (select TEAMS.MANAGER_ID from team_member inner join TEAMS on team_member.TEAM_ID = TEAMS.ID where USER_ID = :USER_ID)",Users.class).setParameter("USER_ID", memberId).list();
	}

	public TeamMember findTeamMenberByTeamId(Long memberId) {
		
		@SuppressWarnings("unchecked")
		List<TeamMember> list =  this.session.createQuery(" from TeamMember where userId = :userId").setParameter("userId", memberId).getResultList();
		if(null != list &&  list.size() > 0){
			return list.get(0);
		}
		
		return null ;
	}
}
