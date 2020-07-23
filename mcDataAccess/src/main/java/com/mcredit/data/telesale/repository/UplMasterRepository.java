package com.mcredit.data.telesale.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.repository.IAddRepository;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.data.telesale.entity.UplMaster;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.enums.LeadGenEnums;
import com.mcredit.model.object.telesales.CheckDupIdentity;

public class UplMasterRepository extends BaseRepository implements IUpsertRepository<UplMaster>, IAddRepository<UplMaster> {

	public UplMasterRepository(Session session) {
		super(session);
	}

	@Override
	public void add(UplMaster item) {
		this.session.save("UplMaster", item);
	}
	
	public void upsert(UplMaster item) {
		this.session.saveOrUpdate("UplMaster", item);
	}

	public void merge(UplMaster item) {
		this.session.merge("UplMaster", item);
	}

	public void remove(UplMaster item) {
		this.session.delete("UplMaster", item);
	}
	public int updateUpl(String status, Long idUpl) {
		return this.session.getNamedQuery("updateUplMaster").setParameter("status", status).setParameter("idFile", idUpl)
				.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<UplMaster> findActiveCampaigns(String step, Long userId, String isAsm, String isXsm, String isNtb, boolean queryForProspectManagement, String tsaLst) {

		if ( "".equals(step) ) { //Get list campaign man hinh import (Supervisor New To Bank (TSP))
			
			String strQuery = " select distinct UPL_MASTER.id,UPL_MASTER.RECORD_STATUS,UPL_MASTER.CREATED_DATE,UPL_MASTER.LAST_UPDATED_DATE, UPL_MASTER.CREATED_BY,UPL_MASTER.LAST_UPDATED_BY, "
							+ " UPL_MASTER.UPL_FORMAT,UPL_MASTER.FROM_SOURCE,UPL_MASTER.UPL_CODE, UPL_MASTER.UPL_TYPE,UPL_MASTER.OWNER_ID,UPL_MASTER.IMPORTED,UPL_MASTER.TOTAL_ALLOCATED, "
							+ " UPL_MASTER.OWNER_ID as owner_login_id, case when UPL_MASTER.OWNER_ID > 0 then (select USERS.USR_FULL_NAME "
							+ " from USERS where USERS.ID = UPL_MASTER.OWNER_ID) END as OWNER_NAME,'' as team_id,'' as team_name,case when NVL(USERS.ID,-1) = -1 "
							+ " then (select distinct t.MANAGER_ID from  TEAMS t inner join UPL_MASTER u on t.MANAGER_ID = u.OWNER_ID "
							+ " where u.UPL_CODE = UPL_MASTER.UPL_CODE) else USERS.ID END as TEAM_LEADER_ID,'' as team_lead_login_id,case when NVL(USERS.ID,-1) = -1 "
							+ " then (select distinct u.USR_FULL_NAME from TEAMS t inner join UPL_MASTER upl on t.MANAGER_ID = upl.OWNER_ID inner join USERS u on u.ID = upl.OWNER_ID "
							+ " where upl.UPL_CODE = UPL_MASTER.UPL_CODE) else USERS.USR_FULL_NAME END as TEAM_LEADER_NAME from UPL_MASTER "
							+ " left join TEAM_MEMBER on UPL_MASTER.OWNER_ID = TEAM_MEMBER.USER_ID left join TEAMS on TEAMS.ID = TEAM_MEMBER.TEAM_ID "
							+ " left join USERS on USERS.ID = TEAMS.MANAGER_ID "
							+ " where UPL_MASTER.RECORD_STATUS = 'A' AND UPL_MASTER.FROM_SOURCE in (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1 in ('TELESALE', 'MB')) order by UPL_MASTER.UPL_CODE ";
			NativeQuery<?> query = this.session.createNativeQuery(strQuery);
			List<?> obj = query.getResultList();
			if (obj != null && obj.size() > 0) {
				List<UplMaster> result = new ArrayList<UplMaster>();
				for (Object o : obj) {
					result.add(transformObject(o, UplMaster.class));
				}
				return result; 
			}
		}

		if ( "supervisor".equals(step) ) { //Man hinh chon danh sach campaign de SUP phan bo xuong cho TeamLead
			
			boolean ntb = false;
			
			String strSql = " select distinct um.ID, um.RECORD_STATUS, um.CREATED_DATE, um.LAST_UPDATED_DATE, um.CREATED_BY, um.LAST_UPDATED_BY, um.UPL_FORMAT, um.FROM_SOURCE, um.UPL_CODE, um.UPL_TYPE, um.OWNER_ID, um.IMPORTED, um.TOTAL_ALLOCATED ";
			
			String columnSelect = strSql;
			
			String unionSqlDataMB = " union "
								+ columnSelect + " from UPL_MASTER um "
								+ " inner join code_table ct on (ct.code_value1 = (select login_id from users where id = :userId) and ct.code_value2 = '" + CTCodeValue1.UPL_SRC_MB_HN.value() + "' "
								+ " and um.UPL_CODE = '" + CTCodeValue1.UPL_CODE_MB_HN.value() + "' and um.IMPORTED > 0 AND um.ID in (SELECT UPL_MASTER_ID FROM UPL_DETAIL WHERE STATUS in ('C', 'P', 'A') GROUP BY UPL_MASTER_ID)) "
									+ " union "
								+ columnSelect + " from UPL_MASTER um "
								+ " inner join code_table ct on (ct.code_value1 = (select login_id from users where id = :userId) and ct.code_value2 = '" + CTCodeValue1.UPL_SRC_MB_HCM.value() + "' "
								+ " and um.UPL_CODE = '" + CTCodeValue1.UPL_CODE_MB_HCM.value() + "' and um.IMPORTED > 0 AND um.ID in (SELECT UPL_MASTER_ID FROM UPL_DETAIL WHERE STATUS in ('C', 'P', 'A') GROUP BY UPL_MASTER_ID)) ";
			
			
			if( "ASM".equals(isAsm) && ("XSM").equals(isXsm) && ("NTB").equals(isNtb) ) { // LG, Xsell, NTB
				if( queryForProspectManagement )
					strSql += " from UPL_MASTER um inner join UPL_DETAIL ud on ud.UPL_MASTER_ID = um.id inner join ALLOCATION_MASTER am on (um.ID = am.UPL_MASTER_ID and am.ALLOCATED_TYPE = 'T') " +
							  " where ( " +
							  "  um.FROM_SOURCE in (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1 in ('LEAD_GEN', 'AMO', 'TC')) or (um.CREATED_BY = (select LOGIN_ID from USERS where id = :userId) and um.FROM_SOURCE in (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1 = 'TELESALE')) " +
							  " or (um.CREATED_BY is null and um.FROM_SOURCE in (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1 = 'TELESALE')) " + //campaign renew
							  " or (um.created_by in (select us.login_id from teams te, team_member tm, users us where te.id = tm.team_id and te.manager_id = :userId and us.id = tm.user_id)) " +
							  "  or ( " +
							  " 	 um.FROM_SOURCE = (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1 = 'MIS') " +
							  "  ) " +
							  " ) " +
							  " and um.IMPORTED > 0 ";
				else
					strSql += " from UPL_MASTER um inner join UPL_DETAIL ud on ud.UPL_MASTER_ID = um.id " +
							  " where ( " +
							  "  um.FROM_SOURCE in (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1 in ('LEAD_GEN', 'AMO', 'TC')) or (um.CREATED_BY = (select LOGIN_ID from USERS where id = :userId) and um.FROM_SOURCE in (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1 = 'TELESALE')) " +
							  " or (um.CREATED_BY is null and um.FROM_SOURCE in (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1 = 'TELESALE')) " + //campaign renew
							  "  or ( " +
							  " 	 um.FROM_SOURCE = (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1 = 'MIS') and ud.STATUS_APP = (select ID from CODE_TABLE where CODE_VALUE1 = '" + CTCodeValue1.XSELL_APP_ACTIVE.value() + "') " +
							  "  ) " +
							  " ) " +
							  " and um.IMPORTED > 0 " + unionSqlDataMB;
				ntb = true;
			}
			else if( "ASM".equals(isAsm) && ("XSM").equals(isXsm) ) { // LG & Xsell
				if( queryForProspectManagement )
					strSql += " from UPL_MASTER um inner join UPL_DETAIL ud on ud.UPL_MASTER_ID = um.id inner join ALLOCATION_MASTER am on (um.ID = am.UPL_MASTER_ID and am.ALLOCATED_TYPE = 'T') " +
							  " where (um.FROM_SOURCE in (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1 = 'LEAD_GEN') " +
							  " or (um.FROM_SOURCE = (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1 = 'MIS'))) " +
							  " and um.IMPORTED > 0 ";
				else
					strSql += " from UPL_MASTER um inner join UPL_DETAIL ud on ud.UPL_MASTER_ID = um.id " +
							  " where (um.FROM_SOURCE in (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1 = 'LEAD_GEN') " +
							  " or (um.FROM_SOURCE = (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1 = 'MIS') and ud.STATUS_APP = (select ID from CODE_TABLE where CODE_VALUE1 = '" + CTCodeValue1.XSELL_APP_ACTIVE.value() + "'))) " +
							  " and um.IMPORTED > 0 ";
			}
			
			else if( "ASM".equals(isAsm) && ("NTB").equals(isNtb) ) { // LG & NTB
				
				strSql += " from UPL_MASTER um where (um.FROM_SOURCE in (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1 in ('LEAD_GEN', 'AMO' , 'TC')) "
						+ " or ( (um.FROM_SOURCE in (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1 = 'TELESALE') and um.CREATED_BY is null ) ) " //campaign renew
						+ " or ( (um.FROM_SOURCE in (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1 = 'TELESALE') and um.CREATED_BY = (select LOGIN_ID from USERS where id = :userId)) )) and um.IMPORTED > 0 "
						+ unionSqlDataMB;
				ntb = true;
			}
			
			else if( "XSM".equals(isXsm) && ("NTB").equals(isNtb) ) { // Xsell & NTB
				if( queryForProspectManagement )
					strSql += " from UPL_MASTER um inner join UPL_DETAIL ud on ud.UPL_MASTER_ID = um.id inner join ALLOCATION_MASTER am on (um.ID = am.UPL_MASTER_ID and am.ALLOCATED_TYPE = 'T') " +
							  " where ((um.FROM_SOURCE in (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1 in ('TELESALE', 'AMO' , 'TC')) and um.CREATED_BY = (select LOGIN_ID from USERS where id = :userId)) " +
							  " or (um.FROM_SOURCE = (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1 = 'MIS'))) " +
							  " and um.IMPORTED > 0 ";
				else
					strSql += " from UPL_MASTER um inner join UPL_DETAIL ud on ud.UPL_MASTER_ID = um.id " +
							  " where ((um.FROM_SOURCE in (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1 in ('TELESALE', 'AMO', 'TC')) and um.CREATED_BY = (select LOGIN_ID from USERS where id = :userId)) " +
							  " or (um.FROM_SOURCE = (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1 = 'MIS') and ud.STATUS_APP = (select ID from CODE_TABLE where CODE_VALUE1 = '" + CTCodeValue1.XSELL_APP_ACTIVE.value() + "'))) " +
							  " and um.IMPORTED > 0 " + unionSqlDataMB;
				ntb = true;
			}
			
			else if( "ASM".equals(isAsm) ) // LG only
				strSql += " from UPL_MASTER um where um.FROM_SOURCE = (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1 = 'LEAD_GEN') " +
						  " and um.ID in (SELECT UPL_MASTER_ID FROM UPL_DETAIL WHERE STATUS in ('"+LeadGenEnums.UPL_STATUS.value()+"', 'P', 'A') GROUP BY UPL_MASTER_ID) and um.IMPORTED > 0 ";
			
			else if( "XSM".equals(isXsm) ) { // Xsell only
				if( queryForProspectManagement )
					strSql += " from UPL_MASTER um INNER JOIN UPL_DETAIL ud on um.ID = ud.UPL_MASTER_ID" +
							  " inner join ALLOCATION_MASTER am on (um.ID = am.UPL_MASTER_ID and am.ALLOCATED_TYPE = 'T') " +
							  " where um.FROM_SOURCE = (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1 = 'MIS') and um.IMPORTED > 0 ";
				else
					strSql += " from UPL_MASTER um INNER JOIN UPL_DETAIL ud on ud.UPL_MASTER_ID = um.id INNER JOIN CODE_TABLE ct_status_app on ct_status_app.ID = ud.STATUS_APP " +
							  " where um.FROM_SOURCE = (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1 = 'MIS') AND ct_status_app.CODE_GROUP = 'MIS' " +
							  " and ct_status_app.CODE_VALUE1= '" + CTCodeValue1.XSELL_APP_ACTIVE.value() + "' and um.IMPORTED > 0 ";
			}
			else { // NTB only

				strSql += " from UPL_MASTER um where ((um.FROM_SOURCE = (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1 = 'AMO')) or (um.FROM_SOURCE = (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1 = 'TC')) or"
						+ " (um.FROM_SOURCE = (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1 = 'TELESALE') "
						+ " and um.IMPORTED > 0 AND (um.CREATED_BY = (select LOGIN_ID from USERS where id = :userId) or um.CREATED_BY is null) AND ID in (SELECT UPL_MASTER_ID FROM UPL_DETAIL WHERE STATUS in ('C', 'P', 'A') GROUP BY UPL_MASTER_ID))) "
						+ unionSqlDataMB;
				ntb = true;
			}
			
			strSql += " order by UPL_CODE ";
			
			if( ntb )
				return this.session.createNativeQuery(strSql, UplMaster.class).setParameter("userId", userId).list();
			else
				return this.session.createNativeQuery(strSql, UplMaster.class).list();
			
			/*if ( "ASM".equals(isAsm) ) { //Get list campaign for Supervisor LeadGen (ASM)
				
				strSql = " select distinct um.ID, um.RECORD_STATUS, um.CREATED_DATE, um.LAST_UPDATED_DATE, um.CREATED_BY, um.LAST_UPDATED_BY, um.UPL_FORMAT, um.FROM_SOURCE, um.UPL_CODE, um.UPL_TYPE, um.OWNER_ID, um.IMPORTED, um.TOTAL_ALLOCATED " + 
						 " from   UPL_MASTER um " +
						 " where um.FROM_SOURCE = (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1='LEAD_GEN') and um.ID in (SELECT UPL_MASTER_ID FROM UPL_DETAIL " +
						 " WHERE STATUS in ('"+LeadGenEnums.UPL_STATUS.value()+"', 'P', 'A') GROUP BY UPL_MASTER_ID) and um.IMPORTED > 0 ";
				return this.session.createNativeQuery(strSql, UplMaster.class).list();
				
			}else if ( ("XSM").equals(isXsm) ) { //Get list campaign for Supervisor Xsell (XSM)
				
				strSql = " SELECT distinct um.id, um.record_status, um.created_date, um.last_updated_date, um.created_by, um.last_updated_by, um.upl_format, um.from_source, um.upl_code, um.upl_type, um.owner_id, um.imported, um.total_allocated "
						+ " FROM upl_master um INNER JOIN UPL_DETAIL ud on ud.UPL_MASTER_ID = um.id INNER JOIN CODE_TABLE ct_status_app on ct_status_app.ID = ud.STATUS_APP "
						+ " WHERE um.FROM_SOURCE = (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1='MIS') AND ct_status_app.CODE_GROUP = 'MIS' "
						+ " and ct_status_app.CODE_VALUE1= '" + CTCodeValue1.XSELL_APP_ACTIVE.value() + "' AND um.imported > 0 ";
				return this.session.createNativeQuery(strSql, UplMaster.class).list();
				
			}else { //Get list campaign for Supervisor New To Bank (TSP)
				
				strSql = " SELECT distinct ID, RECORD_STATUS, CREATED_DATE, LAST_UPDATED_DATE, CREATED_BY, LAST_UPDATED_BY, UPL_FORMAT, FROM_SOURCE, UPL_CODE, UPL_TYPE, OWNER_ID, IMPORTED, TOTAL_ALLOCATED " +
					    " FROM   UPL_MASTER um " +
					    " WHERE um.CREATED_BY = (select LOGIN_ID from USERS where id = :userId) AND ID in (SELECT UPL_MASTER_ID FROM UPL_DETAIL WHERE STATUS in ('C', 'P', 'A') GROUP BY UPL_MASTER_ID) " +
					    " AND um.FROM_SOURCE = (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1='TELESALE') AND IMPORTED > 0 ";
				return this.session
						.createNativeQuery(strSql, UplMaster.class).setParameter("userId", userId).list();
			}*/
		}
		
		else if ( "supervisorTeam".equals(step) ) { //Man hinh chon danh sach campaign de TeamLead phan bo xuong cho TSA
			
			List<BigDecimal> managerXsell = this.session.createNativeQuery(" select ID from TEAMS where MANAGER_ID = :userId and TEAM_GROUP = 'XS' ")
										.setParameter("userId", userId)
										.list();
			List<BigDecimal> managerNonXsell = this.session.createNativeQuery(" select ID from TEAMS where MANAGER_ID = :userId and TEAM_TYPE = 'S' and TEAM_GROUP != 'XS' ")
					.setParameter("userId", userId)
					.list();
			
			/*return this.session
					.createNativeQuery(" SELECT ulm.ID,ulm.RECORD_STATUS,ulm.CREATED_DATE,ulm.LAST_UPDATED_DATE,ulm.CREATED_BY,ulm.LAST_UPDATED_BY,ulm.UPL_FORMAT,ulm.FROM_SOURCE,ulm.UPL_CODE,UPL_TYPE,ulm.OWNER_ID,ulm.IMPORTED,ulm.TOTAL_ALLOCATED " +
										" FROM UPL_MASTER ulm "
							
										+ ( (managerXsell!=null && !managerXsell.isEmpty()) ? " INNER JOIN UPL_DETAIL ud on ulm.id = ud.UPL_MASTER_ID INNER JOIN CODE_TABLE ct_status_app on (ct_status_app.ID = ud.STATUS_APP and ct_status_app.CODE_VALUE1= '" + CTCodeValue1.XSELL_APP_ACTIVE.value() + "') " : "" )
										
										+ " WHERE ulm.id in ( SELECT ad.UPL_MASTER_ID FROM ALLOCATION_MASTER ad WHERE id in ( SELECT ad.ALLOCATION_MASTER_ID  FROM allocation_detail ad WHERE  ad.object_type = 'T'  AND ad.ASSIGNEE_ID in " +
										" (SELECT t.id FROM teams t WHERE t.MANAGER_ID = :userId ) GROUP BY ad.ALLOCATION_MASTER_ID ) GROUP BY ad.UPL_MASTER_ID ) order by ulm.id desc ",
										UplMaster.class).setParameter("userId", userId).list();*/
			
			if( managerXsell!=null && !managerXsell.isEmpty() && (managerNonXsell == null || managerNonXsell.isEmpty())) { //Xu ly voi truong hop la teamLead cua XSELL va khong la teamlead cua team nao khac XSELL
				
				//Man hinh phan bo tu TeamLead xuong TSA
				String strSql = " SELECT distinct ulm.ID,ulm.RECORD_STATUS,ulm.CREATED_DATE,ulm.LAST_UPDATED_DATE,ulm.CREATED_BY,ulm.LAST_UPDATED_BY,ulm.UPL_FORMAT,ulm.FROM_SOURCE,ulm.UPL_CODE,UPL_TYPE,ulm.OWNER_ID,ulm.IMPORTED,ulm.TOTAL_ALLOCATED " +
								" FROM UPL_MASTER ulm INNER JOIN UPL_DETAIL ud on ulm.id = ud.UPL_MASTER_ID " +
								" WHERE ( " +
								"     ulm.FROM_SOURCE in ( select ID from CODE_TABLE WHERE CATEGORY = 'UPL_SRC' and CODE_VALUE1 in ('TELESALE', 'LEAD_GEN') ) " +
								"     OR ( " +
								"         ulm.FROM_SOURCE = ( select ID from CODE_TABLE WHERE CATEGORY = 'UPL_SRC' and CODE_VALUE1 = 'MIS' ) " +
								"         AND " +
								"         ud.status_app = ( select ID from CODE_TABLE WHERE CODE_VALUE1 = '" + CTCodeValue1.XSELL_APP_ACTIVE.value() + "' ) " +
								"     ) " +
								" ) " +
							    " AND ulm.id in ( SELECT ad.UPL_MASTER_ID FROM ALLOCATION_MASTER ad WHERE id in ( SELECT ad.ALLOCATION_MASTER_ID  FROM allocation_detail ad WHERE  ad.object_type = 'T'  AND ad.ASSIGNEE_ID in " +
								" (SELECT t.id FROM teams t WHERE t.MANAGER_ID = :userId ) GROUP BY ad.ALLOCATION_MASTER_ID ) GROUP BY ad.UPL_MASTER_ID ) " +
							    " order by ulm.id desc ";
				
				if( !"".equals(tsaLst) ) //Man hinh ReAssign
					strSql = " SELECT distinct ulm.ID, ulm.RECORD_STATUS, ulm.CREATED_DATE, ulm.LAST_UPDATED_DATE, ulm.CREATED_BY, ulm.LAST_UPDATED_BY, ulm.UPL_FORMAT, ulm.FROM_SOURCE, ulm.UPL_CODE, UPL_TYPE, ulm.OWNER_ID, ulm.IMPORTED, ulm.TOTAL_ALLOCATED " 
							+ " FROM UPL_MASTER ulm "
							+ " inner join UPL_DETAIL ud on ulm.id = ud.upl_master_id "
							+ " inner join ALLOCATION_DETAIL ad on ud.id = ad.upl_detail_id "
							+ " WHERE ( "
							+ "     ulm.FROM_SOURCE in ( select ID from CODE_TABLE WHERE CATEGORY = 'UPL_SRC' and CODE_VALUE1 in ('TELESALE', 'LEAD_GEN') ) "
							+ "     OR ( "
							+ "         ulm.FROM_SOURCE = ( select ID from CODE_TABLE WHERE CATEGORY = 'UPL_SRC' and CODE_VALUE1 = 'MIS' ) "
							//+ "         AND ud.status_app = ( select ID from CODE_TABLE WHERE CODE_VALUE1 = '" + CTCodeValue1.XSELL_APP_ACTIVE.value() + "' ) "
							+ "     ) "
							+ " ) "
							+ " and ad.object_type = 'U' and ad.ASSIGNEE_ID in (:tsaLst) order by ulm.id desc ";
				
				@SuppressWarnings("rawtypes")
				NativeQuery query = this.session.createNativeQuery(strSql, UplMaster.class);
				
				if( !"".equals(tsaLst) ) { //Man hinh ReAssign
					List<String> lstParam = Arrays.asList(tsaLst.substring(0, tsaLst.length()-1).split("\\s*,\\s*"));
					query.setParameterList("tsaLst", lstParam);
				}
				else //Man hinh phan bo tu TeamLead xuong TSA
					query.setParameter("userId", userId);
				
				return query.list();
				
			}else { // LG & NTB
				
				//Man hinh phan bo tu TeamLead xuong TSA
				String strSql = " SELECT ulm.ID, ulm.RECORD_STATUS, ulm.CREATED_DATE, ulm.LAST_UPDATED_DATE, ulm.CREATED_BY, ulm.LAST_UPDATED_BY, ulm.UPL_FORMAT, ulm.FROM_SOURCE, ulm.UPL_CODE, UPL_TYPE, ulm.OWNER_ID, ulm.IMPORTED, ulm.TOTAL_ALLOCATED " 
								+ " FROM UPL_MASTER ulm "
								+ " WHERE "
								+ " ulm.id in ( SELECT ad.UPL_MASTER_ID FROM ALLOCATION_MASTER ad WHERE id in ( SELECT ad.ALLOCATION_MASTER_ID  FROM   allocation_detail ad WHERE  ad.object_type = 'T'  AND ad.ASSIGNEE_ID in " 
								+ " (SELECT t.id FROM teams t WHERE t.MANAGER_ID = :userId ) GROUP BY ad.ALLOCATION_MASTER_ID ) GROUP BY ad.UPL_MASTER_ID ) order by ulm.id desc ";
				
				if( !"".equals(tsaLst) ) //Man hinh ReAssign
					strSql = " SELECT distinct ulm.ID, ulm.RECORD_STATUS, ulm.CREATED_DATE, ulm.LAST_UPDATED_DATE, ulm.CREATED_BY, ulm.LAST_UPDATED_BY, ulm.UPL_FORMAT, ulm.FROM_SOURCE, ulm.UPL_CODE, UPL_TYPE, ulm.OWNER_ID, ulm.IMPORTED, ulm.TOTAL_ALLOCATED " 
							+ " FROM UPL_MASTER ulm "
							+ " inner join UPL_DETAIL ud on ulm.id = ud.upl_master_id "
							+ " inner join ALLOCATION_DETAIL ad on ud.id = ad.upl_detail_id "
							+ " WHERE ulm.from_source <> (select id from code_table where category='UPL_SRC' and code_value1='WH') "
							+ " and ad.object_type = 'U' and ad.ASSIGNEE_ID in (:tsaLst) order by ulm.id desc ";
				
				@SuppressWarnings("rawtypes")
				NativeQuery query = this.session.createNativeQuery(strSql, UplMaster.class);
				
				if( !"".equals(tsaLst) ) { //Man hinh ReAssign
					List<String> lstParam = Arrays.asList(tsaLst.substring(0, tsaLst.length()-1).split("\\s*,\\s*"));
					query.setParameterList("tsaLst", lstParam);
				}
				else //Man hinh phan bo tu TeamLead xuong TSA
					query.setParameter("userId", userId);
				
				return query.list();
			}
		}
		
		else if ( "TSA".equals(step) ) { //Man hinh chon danh sach campaign cua TSA
			
			return this.session
					.createNativeQuery(" SELECT distinct ulm.ID,ulm.RECORD_STATUS,ulm.CREATED_DATE,ulm.LAST_UPDATED_DATE,ulm.CREATED_BY,ulm.LAST_UPDATED_BY,ulm.UPL_FORMAT,ulm.FROM_SOURCE,ulm.UPL_CODE,UPL_TYPE"
									+ " ,ulm.OWNER_ID,ulm.IMPORTED,ulm.TOTAL_ALLOCATED "
									+ " FROM ALLOCATION_DETAIL ad "
									+ " INNER JOIN UPL_DETAIL ud on ad.UPL_DETAIL_ID = ud.ID "
									+ " INNER JOIN UPL_MASTER ulm on ud.UPL_MASTER_ID = ulm.ID "
									+ " WHERE ASSIGNEE_ID = :userId ",
									UplMaster.class).setParameter("userId", userId).list();
		}
		
		return null;
	}
	
	public UplMaster getByUPLCode(String uplCode) {
		try {
			return (UplMaster)this.session.getNamedQuery("findUplMasterbyUplCode").setParameter("uplCode", uplCode).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public UplMaster findUplMasterbyUplCode(String uplCode) {
		UplMaster uplMaster = null;
		@SuppressWarnings("unchecked")
		List<UplMaster> results = this.session
				.getNamedQuery("findUplMasterbyUplCode")
				.setParameter("uplCode", uplCode).getResultList();
		if (!results.isEmpty())
			uplMaster = results.get(0);
		return uplMaster;
	}

	public UplMaster findUplMasterbyID(Long id) {
		UplMaster uplMaster = new UplMaster();
		List<?> lst = this.session.getNamedQuery("findUplMasterbyID")
				.setParameter("id", id).list();

		if (lst != null && lst.size() > 0) {
			for (Object object : lst) {

				uplMaster = (UplMaster) object;
			}
		}

		return uplMaster;

	}

	public UplMaster queryUploadMaster(String uplCode, String uplType, String uplSequence, String uplOwner, String isAsm, String xsm) {
		List<?> obj  = null ;

		String strQuery = "select um.*,  u.login_id  OWNER_LOGIN_ID,u.usr_full_name  OWNER_NAME,t.id  TEAM_ID,t.team_name TEAM_NAME,ul.id  TEAM_LEAD_ID,ul.login_id  TEAM_LEAD_LOGIN_ID,ul.usr_full_name  TEAM_LEAD_NAME ,um.imported - um.total_allocated as total_unallocated ,ct1.DESCRIPTION1 "
				+ "from UPL_MASTER um left join USERS u on um.OWNER_ID = u.ID "
				+ "left join TEAM_MEMBER tm on tm.USER_ID = u.ID "
				+ "left join TEAMS t on t.ID = tm.TEAM_ID "
				+ "left join USERS ul on ul.ID = t.MANAGER_ID "
				+ "left join CODE_TABLE ct on ct.ID = um.FROM_SOURCE "
				+ "left join CODE_TABLE ct1 on ct1.ID = um.UPL_TYPE "
				//+ " where ct.CODE_VALUE1 = :fromSource and um.UPL_CODE = :uplCode";
				+ " where um.UPL_CODE = :uplCode";

		if (uplType != null && !"".equals(uplType.trim()))
			strQuery = strQuery + "  AND um.uplType = " + uplType;

		if (null != uplSequence && "".equals(uplSequence.trim()))
			strQuery = strQuery + " AND um.uplSequence = " + uplSequence;

		if (uplOwner != null && !"".equals(uplOwner.trim()))
			strQuery = strQuery + "AND um.ownerId = " + uplOwner;
		
		/*if("ASM".equals(isAsm)){
			 NativeQuery<?> query = this.session.createNativeQuery(strQuery).setParameter("fromSource",LeadGenEnums.LEAD_GEN.value()).setParameter("uplCode", uplCode);
			 obj = query.getResultList();
		}else if("XSM".equals(xsm)){
			 NativeQuery<?> query = this.session.createNativeQuery(strQuery).setParameter("fromSource",ExcellEnums.EXCEL_FROM_SOURCE.stringValue()).setParameter("uplCode", uplCode);
			 obj = query.getResultList();
		}else{
			NativeQuery<?> query = this.session.createNativeQuery(strQuery).setParameter("fromSource",Commons.TS_UPL_SOURCE.value()).setParameter("uplCode", uplCode);
			 obj = query.getResultList();
		}*/
		NativeQuery<?> query = this.session.createNativeQuery(strQuery).setParameter("uplCode", uplCode);
		obj = query.getResultList();

		if (obj != null && obj.size() > 0) {
			for (Object o : obj) {
				return transformObject(o, UplMaster.class);
			}
		}
		return null;
	}
	
	public UplMaster queryAllocationDetail(String uplCode, Long teamId , String step ,Long uplMaterId) {
		NativeQuery<?> query = null ;
		String strQuery = "select um.*,  u.login_id  OWNER_LOGIN_ID,u.usr_full_name  OWNER_NAME,t.id  TEAM_ID,t.team_name TEAM_NAME,ul.id  TEAM_LEAD_ID,"
				+ "ul.login_id  TEAM_LEAD_LOGIN_ID,ul.usr_full_name  TEAM_LEAD_NAME ,"
				+ "recv.case_received  -  alloc.total_allocated as total_unallocated ,"
				+ "ct1.DESCRIPTION1 ,"
				+ "recv.upl_master_id  as allocation_MasterId ,"
				+ "recv.case_received as case_received ,"
				+ "alloc.total_allocated as TOTAL_ALLOCATEDS  "
				+ "from UPL_MASTER um left join  "
				+ "(select am.UPL_MASTER_ID, sum(ad.ALLOCATED_NUMBER) CASE_RECEIVED  "
				+ "from ALLOCATION_MASTER am inner join ALLOCATION_DETAIL ad  "
				+ "on am.ID = ad.ALLOCATION_MASTER_ID "
				+ "where ad.OBJECT_TYPE = 'T' and ad.ASSIGNEE_ID =:teamId and am.ALLOCATED_TYPE = 'S' "
				+ "group by am.UPL_MASTER_ID) recv on um.ID = recv.UPL_MASTER_ID "
				+ "left join (select ateam.UPL_MASTER_ID, sum(ateam.ALLOCATED_NUMBER) TOTAL_ALLOCATED "
				+ "from ALLOCATION_MASTER am inner join ALLOCATION_DETAIL ad  "
				+ "on am.ID = ad.ALLOCATION_MASTER_ID "
				+ "inner join ALLOCATION_MASTER ateam on ateam.RELATED_ALLOCATION = am.ID "
				+ "where am.ALLOCATED_TYPE = 'S' and ateam.ALLOCATED_TYPE = 'T' "
				+ "and ad.OBJECT_TYPE = 'T' and ad.ASSIGNEE_ID =:teamId1  "
				+ "group by ateam.UPL_MASTER_ID) alloc on um.ID = alloc.UPL_MASTER_ID  "
				+ "left join CODE_TABLE ut on ut.ID = um.UPL_TYPE "
				+ "left join USERS u on u.ID = um.OWNER_ID "
				+ "left join TEAM_MEMBER tm on tm.USER_ID = u.ID "
				+ "left join TEAMS t on t.ID = tm.TEAM_ID "
				+ "left join USERS ul on ul.ID = t.MANAGER_ID "
				+ "left join CODE_TABLE ct1 on ct1.ID = um.UPL_TYPE " ;
		
				if(step == null || "".equals(step.trim())){
					strQuery = strQuery + "where um.UPL_CODE = :uplCode "  ;
					 query = this.session.createNativeQuery(strQuery).setParameter("teamId", teamId).setParameter("teamId1", teamId).setParameter("uplCode", uplCode);
				}
				
				if(null != step && !"".equals(step.trim()) && step .equals("allocateTeam")){
					strQuery = strQuery + "where um.id = :uplMaterId "  ;
					 query = this.session.createNativeQuery(strQuery).setParameter("teamId", teamId).setParameter("teamId1", teamId).setParameter("uplMaterId", uplMaterId);
				}
		
		
			Object obj = query.getSingleResult();
	
			return transformObject(obj, UplMaster.class);
		
	}
	
	public String getUplCode() {
        return (String) this.session.getNamedNativeQuery("getUplCode").getSingleResult();
    }

	/*public String getTotalImportedAndTotalAllocated(Long uplMasterId) {
		Object rs = this.session.getNamedNativeQuery("getTotalImportedAndTotalAllocated")
								.setParameter("uplMasterId", uplMasterId)
								.uniqueResult();
		return rs!=null ? (String ) rs : "";
	}*/
	
	/*public Integer getTotalImportedFromUplDetail(Long uplDetailId) {
		Object rs = this.session.getNamedNativeQuery("getTotalImportedFromUplDetail")
								.setParameter("uplDetailId", uplDetailId)
								.uniqueResult();
		return rs!=null ? Integer.parseInt((String)rs) : 0;
	}*/

    @SuppressWarnings("unchecked")
	public List<CheckDupIdentity> checkDupIdentity(Date dateFrom) {
        List<CheckDupIdentity> results = new ArrayList<>();
        @SuppressWarnings("rawtypes")
		List lstIdentityDB = this.session.getNamedNativeQuery("checkDupIdentity").setParameter("dateFrom", dateFrom).list();
        if (lstIdentityDB != null && !lstIdentityDB.isEmpty()){
			results = transformList(lstIdentityDB, CheckDupIdentity.class);
		}
        return results;
    }
    @SuppressWarnings("unchecked")
	public List<CheckDupIdentity> checkDupIdentityArmy(Date dateFrom) {
        List<CheckDupIdentity> results = new ArrayList<>();
        @SuppressWarnings("rawtypes")
		List lstIdentityArmyDB = this.session.getNamedNativeQuery("checkDupIdentityArmy").setParameter("dateFrom", dateFrom).list();
        if (lstIdentityArmyDB != null && !lstIdentityArmyDB.isEmpty()){
			results = transformList(lstIdentityArmyDB, CheckDupIdentity.class);
		}
        return results;
    }

    public Long getCustId(String birthDate,String identityNumber) {
        @SuppressWarnings("rawtypes")
		List lstCustId = this.session.getNamedNativeQuery("getCustId")
									.setParameter("birthDate", birthDate)
									.setParameter("identityNumber", identityNumber).list();
        if (lstCustId != null && !lstCustId.isEmpty()) {
            BigDecimal bigId = (BigDecimal) lstCustId.get(0);
            return bigId.longValue();
        } else {
            return null;
        }

    }

    public List<String> getLstProductCode() {
        @SuppressWarnings("unchecked")
		List<String> lstProductCode = this.session.getNamedNativeQuery("lstProductCode").list();
        return lstProductCode;
    }
    
	public int removeWithId(Long idUpl) {
    	 return this.session.getNamedQuery("deleteUplMaster").setParameter("idFile", idUpl)
                 .executeUpdate();
	}
}