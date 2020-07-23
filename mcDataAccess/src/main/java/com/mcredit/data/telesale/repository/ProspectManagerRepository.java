package com.mcredit.data.telesale.repository;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.DataException;
import com.mcredit.data.exception.NoRecordFoundException;
import com.mcredit.data.telesale.entity.CallResult;
import com.mcredit.data.telesale.entity.CustProspect;
import com.mcredit.data.telesale.entity.UplCustomer;
import com.mcredit.data.telesale.entity.UplMaster;
import com.mcredit.model.dto.telesales.ProspectReAssignDTO;
import com.mcredit.model.dto.telesales.UplCustomerProductDTO;
import com.mcredit.model.dto.warehouse.EmployeeDTO;
import com.mcredit.model.object.ProspectManagementData;
import com.mcredit.model.object.ProspectManager;
import com.mcredit.model.object.ProspectSearch;
import com.mcredit.model.object.telesales.CustProspectProduct;
import com.mcredit.model.object.telesales.CustProspectXsell;
import com.mcredit.util.StringUtils;

public class ProspectManagerRepository<T> extends BaseRepository {	

	public ProspectManagerRepository(Session session) {
		super(session);
	}
	
	public boolean prospectReAssign(List<ProspectReAssignDTO> lstProspectReAssign) throws DataException {
		
		boolean status = false;
		
		Query<?> query = null;
		
		String tmpSelectSql = "";
		
		for( ProspectReAssignDTO item : lstProspectReAssign ) {
			
			if( item.getAllocatedNumber() > 0 ) {
				
				long integerNumber = item.getAllocatedNumber() / item.getNewObjectId().size();
				long surplusNumber = item.getAllocatedNumber() % item.getNewObjectId().size();
				
				boolean isSurplusDiv = false;
				
				for( Long tsaId : item.getNewObjectId() ) { // So lan chia cho cac TSA
					
					if ( !isSurplusDiv && surplusNumber > 0 ) // Chia them so du cho 1 TSA nao do
						isSurplusDiv = true;
					else
						isSurplusDiv = false;
					
					if( !"".equals(item.getProspectStatus()) && item.getUplMasterId() > 0 ) { // Chon campaign & trang thai
						
						query = this.session.createQuery(" update AllocationDetail set objectId = :newObjectId "
														+ ("RN".equals(item.getProspectStatus())?" , status = (select id from CodeTable where category = 'ALCTYPE_TL' and codeValue1 = 'F') ":"")
														+ " where objectId = :oldObjectId "
														+ " and status = (select id from CodeTable where category = 'ALCTYPE_TL' and codeValue1 = :prospectStatus) "
														+ " and uplDetailId in (select id from UplDetail where uplMasterId = :uplMasterId) "
														+ " and rownum <= :allocatedNumber ");
						query.setParameter("newObjectId", tsaId);
						query.setParameter("oldObjectId", item.getOldObjectId());
						query.setParameter("prospectStatus", item.getProspectStatus());
						query.setParameter("uplMasterId", item.getUplMasterId());
						query.setParameter("allocatedNumber", isSurplusDiv ? (integerNumber + surplusNumber) : integerNumber);
						
						query.executeUpdate();
						
					}else if( !"".equals(item.getProspectStatus()) && item.getUplMasterId() == 0 ) { // Chi chon trang thai
						
						query = this.session.createQuery(" update AllocationDetail set objectId = :newObjectId "
														+ ("RN".equals(item.getProspectStatus())?" , status = (select id from CodeTable where category = 'ALCTYPE_TL' and codeValue1 = 'F') ":"")
														+ " where objectId = :oldObjectId "
														+ " and status = (select id from CodeTable where category = 'ALCTYPE_TL' and codeValue1 = :prospectStatus) "
														+ " and rownum <= :allocatedNumber ");
						query.setParameter("newObjectId", tsaId);
						query.setParameter("oldObjectId", item.getOldObjectId());
						query.setParameter("prospectStatus", item.getProspectStatus());
						query.setParameter("allocatedNumber", isSurplusDiv ? (integerNumber + surplusNumber) : integerNumber);
					
						query.executeUpdate();
						
					}else if( "".equals(item.getProspectStatus()) && item.getUplMasterId() > 0 ) { // Chi chon campaign
						
						query = this.session.createQuery(" update AllocationDetail set objectId = :newObjectId "
														+ " where objectId = :oldObjectId "
														+ " and uplDetailId in (select id from UplDetail where uplMasterId = :uplMasterId) "
														+ " and rownum <= :allocatedNumber ");
						query.setParameter("newObjectId", tsaId);
						query.setParameter("oldObjectId", item.getOldObjectId());
						query.setParameter("uplMasterId", item.getUplMasterId());
						query.setParameter("allocatedNumber", isSurplusDiv ? (integerNumber + surplusNumber) : integerNumber);
					
						query.executeUpdate();
						
						// --------- Xu ly chuyen data sang FRESH neu la RENEW
						tmpSelectSql = " select ID from ALLOCATION_DETAIL where ASSIGNEE_ID = :newObjectId "
										+ " and UPL_DETAIL_ID in (select ID from UPL_DETAIL where UPL_MASTER_ID = :uplMasterId) "
										+ " and STATUS = (select ID from CODE_TABLE where CATEGORY = 'ALCTYPE_TL' and CODE_VALUE1 = 'RN') ";
						
						@SuppressWarnings("unchecked")
						List<BigDecimal> reNewLst = this.session.createNativeQuery(tmpSelectSql)
															.setParameter("newObjectId", tsaId)
															.setParameter("uplMasterId", item.getUplMasterId())
															.list();
						if( reNewLst!=null && !reNewLst.isEmpty() ) {
							
							for( BigDecimal adId : reNewLst ) {
								query = this.session.createQuery(" update AllocationDetail set status = (select id from CodeTable where category = 'ALCTYPE_TL' and codeValue1 = 'F') "
																+ " where id = :adId ");
								query.setParameter("adId", adId.longValue());
								query.executeUpdate();
							}
						}
						// --------------------------------
						
					}else { // Khong chon campaign & trang thai
						
						query = this.session.createQuery(" update AllocationDetail set objectId = :newObjectId "													
														+ " where objectId = :oldObjectId "
														+ " and rownum <= :allocatedNumber ");
						query.setParameter("newObjectId", tsaId);
						query.setParameter("oldObjectId", item.getOldObjectId());
						query.setParameter("allocatedNumber", isSurplusDiv ? (integerNumber + surplusNumber) : integerNumber);
					
						query.executeUpdate();
						
						// --------- Xu ly chuyen data sang FRESH neu la RENEW
						tmpSelectSql = " select ID from ALLOCATION_DETAIL where ASSIGNEE_ID = :newObjectId "
									+ " and status = (select ID from CODE_TABLE where CATEGORY = 'ALCTYPE_TL' and CODE_VALUE1 = 'RN') ";
						
						@SuppressWarnings("unchecked")
						List<BigDecimal> reNewLst = this.session.createNativeQuery(tmpSelectSql)
															.setParameter("newObjectId", tsaId)
															.list();
						if( reNewLst!=null && !reNewLst.isEmpty() ) {
							
							for( BigDecimal adId : reNewLst ) {
								query = this.session.createQuery(" update AllocationDetail set status = (select id from CodeTable where category = 'ALCTYPE_TL' and codeValue1 = 'F') "
																+ " where id = :adId ");
								query.setParameter("adId", adId.longValue());
								query.executeUpdate();
							}
						}
						// --------------------------------
					}
				}
			}
		}
		
		status = true;
		
        return status;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public ProspectManagementData findProspect(ProspectSearch input, String campaignCodeLstOfSup) throws DataException {
		
		ProspectManagementData results = new ProspectManagementData();

		String condition = "";

		if (input.getUplMasterId() != null && input.getUplMasterId() > 0)
			condition += " and am.UPL_MASTER_ID = " + input.getUplMasterId();

		if (!"".equals(input.getProductName()))
			condition += " and (p.PRODUCT_NAME in " 
					+ StringUtils.putArrayStringIntoParameter(input.getProductName().trim())
					+ " or uc.PRE_PRODUCT_NAME in "
					+ StringUtils.putArrayStringIntoParameter(input.getProductName().trim()) 
					+ " ) ";
		
		if (!"".equals(input.getDataSource()))
			condition += " and upper(uc.DATA_SOURCE) = '" + input.getDataSource().toUpperCase() + "'";
		
		if (!"".equals(input.getLeadSource()))
			condition += " and uc.LEAD_SOURCE = '" + input.getLeadSource().trim() + "'";
		
		if ( input.getTsaReceiveId() != null && input.getTsaReceiveId() > 0 )
			condition += " and ad.OBJECT_TYPE = 'U' and ad.ASSIGNEE_ID = " + input.getTsaReceiveId();
		
		if (!"".equals(input.getMobile()))
			condition += " and (uc.MOBILE = '" + input.getMobile() 
//			+ "' OR cci.CONTACT_VALUE='" + input.getMobile()
			+ "' OR  cp.MOBILE = '"+input.getMobile()+"'  )";

		if (input.getProspectStatus() != null && input.getProspectStatus() > 0)
			condition += " and ad.STATUS = " + input.getProspectStatus();
		
		if (input.getNextAction() != null && input.getNextAction() > 0)
			condition += " and NEXT_ACTION = " + input.getNextAction();

		if (input.getCallStatus() != null && input.getCallStatus() > 0)
			condition += " and (CALL_STATUS = " + input.getCallStatus() + " and cr.STATUS <> 'C') ";
		
		if (input.getCallResult() != null && input.getCallResult() > 0)
			condition += " and CALL_RESULT in ( select ID from CODE_TABLE where CODE_GROUP = 'CALL' and CATEGORY = 'CALL_RSLT' and upper(DESCRIPTION1) = ( "
				       	    + " select upper(DESCRIPTION1) from CODE_TABLE where CODE_GROUP = 'CALL' and CATEGORY = 'CALL_RSLT' and ID = " + input.getCallResult()
				       	 + " ) "
				       + " ) ";

		if (!StringUtils.isNullOrEmpty(input.getCustName()))
			condition += " and (upper(cp.CUST_NAME) like upper('%" + input.getCustName().trim() + "%') or upper(uc.CUSTOMER_NAME) like upper('%" + input.getCustName().trim() + "%'))";

		if (!StringUtils.isNullOrEmpty(input.getFromDate()) && !StringUtils.isNullOrEmpty(input.getToDate()))
			condition += " and (cr.NEXT_ACTION_DATE >= to_date('" + input.getFromDate() + " 00:00:00', 'DD/MM/YYYY HH24:MI:SS') " + " and cr.NEXT_ACTION_DATE <= to_date('" + input.getToDate() + " 23:59:59', 'DD/MM/YYYY HH24:MI:SS')) ";
		else if (!StringUtils.isNullOrEmpty(input.getFromDate()))
			condition += " and (cr.NEXT_ACTION_DATE >= to_date('" + input.getFromDate() + " 00:00:00', 'DD/MM/YYYY HH24:MI:SS')) ";
		else if (!StringUtils.isNullOrEmpty(input.getToDate()))
			condition += " and (cr.NEXT_ACTION_DATE <= to_date('" + input.getToDate() + " 23:59:59', 'DD/MM/YYYY HH24:MI:SS')) ";

		if (!StringUtils.isNullOrEmpty(input.getIdentityNumber()))
			condition += " and (uc.IDENTITY_NUMBER = '" + input.getIdentityNumber() + "'  OR uc.identity_number_army = '" + input.getIdentityNumber() + "' OR cp.identity_number = '"+input.getIdentityNumber()+"' )";
		
		if (input.getProvinceId() != null && !"".equals(input.getProvinceId())) {
			condition += "and ( cp.PERMANENT_PROVINCE in (" + input.getProvinceId() + ")" 
					+ " OR cari.province in (" + input.getProvinceId() + ")"
					+ " OR uc.province in " + StringUtils.putArrayStringIntoParameter(input.getProvinceTextList())
					+ ")";
		}
		
		if (!StringUtils.isNullOrEmpty(input.getNewMobile()))
			condition += " and (cp.NEW_MOBILE = '"+input.getNewMobile()+"'  )";
		
		/*if( "ASM".equals(isAsm) ) //query data for ASM Leadgen
			condition += " and um.FROM_SOURCE = (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1='LEAD_GEN') and um.ID in (SELECT UPL_MASTER_ID FROM UPL_DETAIL WHERE STATUS in ('"+LeadGenEnums.UPL_STATUS.value()+"', 'P', 'A') GROUP BY UPL_MASTER_ID) and um.IMPORTED > 0 ";
		else if( input.isSuperVisor() ) //query data for TSP New to bank
			condition += " and um.ID in (SELECT UPL_MASTER_ID FROM UPL_DETAIL WHERE STATUS in ('C', 'P', 'A') GROUP BY UPL_MASTER_ID) AND um.FROM_SOURCE = (select ID from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1='TELESALE') AND um.IMPORTED > 0 ";*/
		if( input.isSuperVisor() && !"".equals(campaignCodeLstOfSup) ) //query data for Supervisor
			condition += " and um.UPL_CODE in " + StringUtils.putArrayStringIntoParameter(campaignCodeLstOfSup);
		else if( input.isTeamLead() ) //query data for teamLead
			condition += " and ad.OBJECT_TYPE='U' and ad.ASSIGNEE_ID in (SELECT tm.USER_ID FROM TEAM_MEMBER tm INNER JOIN TEAMS t on t.ID = tm.TEAM_ID WHERE tm.STATUS = 'A' and t.STATUS = 'A' and t.MANAGER_ID = " + input.getUserId() + ") ";
			//condition += " and (ad.OBJECT_TYPE='T' and ad.ASSIGNEE_ID in (select ID from TEAMS where MANAGER_ID = " + input.getUserId() + ")) ";
		else { //query data for TSA
			//condition += " and ad.STATUS <> (select ID from CODE_TABLE where CODE_GROUP='CALL' and CATEGORY='ALCTYPE_TL' and CODE_VALUE1='C') and ad.OBJECT_TYPE='U' and ad.ASSIGNEE_ID = " + input.getUserId();
			condition += " and ad.STATUS not in (select ID from CODE_TABLE where CATEGORY = 'ALCTYPE_TL' and CODE_VALUE1 in ('RN', 'C')) and ad.OBJECT_TYPE='U' and ad.ASSIGNEE_ID = " + input.getUserId();
		}
		
		if (!"".equals(condition))
			condition = " where um.FROM_SOURCE <> (select ID from CODE_TABLE where CATEGORY = 'UPL_SRC' and CODE_VALUE1 = 'WH') " + condition;
		String joinTable = " ALLOCATION_MASTER am "
						+ " inner join ALLOCATION_DETAIL ad on am.ID = ad.ALLOCATION_MASTER_ID "
						+ " inner join UPL_MASTER um on am.UPL_MASTER_ID = um.ID "
						+ " left join USERS u on ad.ASSIGNEE_ID = u.ID "
						+ " inner join UPL_CUSTOMER uc on ad.UPL_OBJECT_ID = uc.ID "
						+ " LEFT JOIN products p ON uc.product_id = p.id ";
		
			  //if( "ASM".equals(isAsm) || input.isSuperVisor() )
				  //joinTable += " inner join UPL_DETAIL ud on uc.UPL_DETAIL_ID = ud.ID inner join UPL_MASTER um on ud.UPL_MASTER_ID = um.ID ";
			  
			  joinTable += " left join CUST_PROSPECT cp on ad.UPL_OBJECT_ID = cp.UPL_CUSTOMER_ID "
						+ " left join (select * from CALL_RESULT a where exists (select 1 from "
						+ " (select CUST_PROSPECT_ID,max(CALL_DATE) CALL_DATE from CALL_RESULT group by CUST_PROSPECT_ID) b "
						+ " where b.CALL_DATE = a.CALL_DATE and b.CUST_PROSPECT_ID = a.CUST_PROSPECT_ID)) cr on cr.CUST_PROSPECT_ID = cp.ID"
//						+ " left join (select * from credit_bureau_data where id in ( " + 
//						"        		select max(id) from credit_bureau_data cbd_otp_sub " + 
//						"		        where cbd_otp_sub.cb_source = 'T' " + 
//						"		        and cbd_otp_sub.cb_key = 'OTP' " + 
//						"		        and cbd_otp_sub.cb_type = 'C' " + 
//						"		        group by trim(cbd_otp_sub.cust_identity_number), trim(cbd_otp_sub.cust_mobile))) cbd_otp " + 
//						"		        on (trim(cbd_otp.cust_identity_number) = trim(cp.identity_number) " + 
//						"		        and trim(cbd_otp.cust_mobile) = trim(cp.mobile)) " +
//						" left join (select * from credit_bureau_data where id in ( " + 
//						"		        select max(id) from credit_bureau_data cbd_score_sub " + 
//						"		        where cbd_score_sub.cb_source = 'T' " + 
//						"		        and cbd_score_sub.cb_key like 'MARK%' " + 
//						"		        and cbd_score_sub.cb_type = 'C' " + 
//						"		        group by trim(cbd_score_sub.cust_identity_number), trim(cbd_score_sub.cust_mobile))) cbd_score " + 
//						"		        on (trim(cbd_score.cust_identity_number) = trim(cp.identity_number) " + 
//						"		        and trim(cbd_score.cust_mobile) = trim(cp.mobile)) "
						;
						
			  
		  // Search mobile for xsell
		  joinTable += 
//				  "	LEFT JOIN CUST_CONTACT_INFO cci ON ( cci.CUST_ID = uc.CUST_ID	AND " + 
//				  		"		cci.CONTACT_TYPE = (SELECT id FROM CODE_TABLE WHERE code_group = 'CONT' AND  category = 'CONTAC_TYP' AND code_value1 = 'MOBILE') " + 
//				  		"		AND cci.CONTACT_CATEGORY= (SELECT id FROM CODE_TABLE WHERE code_group = 'CONT' AND  category = 'CONTAC_CAT' AND code_value1 = 'CUSTOMER') " + 
//				  		"		AND cci.CONTACT_PRIORITY = 1) " + 
				  		" 			LEFT JOIN CUST_ADDR_INFO cari ON (cari.CUST_ID = uc.CUST_ID AND cari.addr_type = (" + 
				  		"				SELECT" + 
				  		"					id" + 
				  		"				FROM" + 
				  		"					code_table ct" + 
				  		"				WHERE" + 
				  		"					ct.code_group = 'CONT'" + 
				  		"					AND ct.category = 'ADDR_TYPE'" + 
				  		"					AND ct.code_value1 = 'TEMPORARY' " + 
				  		"					" + 
				  		"			)) " +
				  		" LEFT JOIN CUST_PERSONAL_INFO cpi ON uc.cust_id = cpi.id	" + 
				  		" LEFT JOIN CODE_TABLE ct_xs_gender ON cpi.gender = ct_xs_gender.id" + 
				  		" LEFT JOIN CODE_TABLE ct_from_source ON um.from_source = ct_from_source.id";
		
		/*String sqlCount = "";
		if( input.isTeamLead() )
			sqlCount = " select sum(ad.ALLOCATED_NUMBER) from " + joinTable + condition; //For teamLead
		else*/
			String sqlCount = " select count(*) from " + joinTable + condition; //For Supervisor & TSA
			
		results.setTotalRows((BigDecimal) this.session.createNativeQuery(sqlCount).uniqueResult());
		
		/**/
		//condition = condition.replace(" and (ad.OBJECT_TYPE='T' and ad.ASSIGNEE_ID in (select ID from TEAMS where MANAGER_ID = " + input.getUserId() + ")) "
									//, " and ad.OBJECT_TYPE='U' and ad.ASSIGNEE_ID in (SELECT tm.USER_ID FROM TEAM_MEMBER tm INNER JOIN TEAMS t on t.ID = tm.TEAM_ID WHERE tm.STATUS = 'A' and t.STATUS = 'A' and t.MANAGER_ID = " + input.getUserId() + ") ");
			
		String sql = "select * from ( select cp.ID as custProspectId, nvl(cp.CUST_NAME, uc.CUSTOMER_NAME) as custName, " + 
					" (select DESCRIPTION1 from CODE_TABLE where ID = ad.STATUS) as prospectStatus, " + 
					" CASE ct_from_source.code_value1" + 
					"		WHEN 'MIS'" + 
					"		THEN " + 
					"			ct_xs_gender.description1 " + 
					"		ELSE" + 
					"			(SELECT DESCRIPTION1 FROM CODE_TABLE WHERE ID = cp.GENDER) " + 
					"	END  AS gender, " +
					"	CASE ct_from_source.code_value1 " + 
					"		WHEN 'MIS' " + 
					"		THEN " + 
					"			(SELECT description1 FROM CODE_TABLE WHERE id = cari.province) " + 
					"		ELSE " + 
					"			(SELECT DESCRIPTION1 FROM CODE_TABLE WHERE ID = cp.PERMANENT_PROVINCE) " + 
					"	END  AS province, "
					+ " uc.province as provinceText, "
					+ " to_char(cr.CALL_DATE, 'dd/MM/yyyy HH24:MI') as callDate, "
					+ " (select DESCRIPTION1 from CODE_TABLE where ID = cr.CALL_STATUS) as callStatus, "
					+ " (select DESCRIPTION1 from CODE_TABLE where ID = cr.CALL_RESULT) as callResult, "
					+ " (select DESCRIPTION1 from CODE_TABLE where ID = cr.NEXT_ACTION) as nextAction, uc.ID as uploadCustomerId, "
					+ " nvl(to_char(cr.NEXT_ACTION_DATE, 'dd/MM/yyyy HH24:MI'), '') as nextActionDate, u.login_id as loginId, "
					+ " cr.NOTE as note, p.PRODUCT_NAME as productName, "
					+ " uc.PRE_PRODUCT_NAME as preProductName, "
					+ " uc.UDF01 "
//					+ " JSON_VALUE(cbd_otp.cb_data_detail, '$.responseCode') mess_otp, "
//					+ " JSON_VALUE(cbd_score.cb_data_detail, '$.code') mess_ts, "
//					+ " JSON_VALUE(cbd_score.cb_data_detail, '$.score') score " 
					+ " from " + joinTable + condition + " order by am.ID desc)  tbl where 1=1 ";
		// 	Them cac dieu kien tim kiem 
				// 1. Khach hang co dong y tim kiem 
				if (-1 != input.getIsMark())
					sql += " and tbl.udf01 = '" + input.getIsMark().toString()+"'";
				// 2. OTP
				if (-1 !=input.getResultOTP()){
					String sMessCodeOtp = getMessCusHis(input.getResultOTP());
					sql += " and tbl.mess_otp " + sMessCodeOtp;
				}
				// 3. Mark TS
				if (-1 !=input.getResultTS()){
					if(input.getResultTS() == 1)
						sql += " and (tbl.mess_ts = 'success') ";
					else if(input.getResultTS() == 2)
						sql += " and (tbl.mess_ts = 'success' and (tbl.min_score is null or (tbl.min_score <550 or tbl.min_score >850))) ";
					else if(input.getResultTS() == 3)
						sql += " and tbl.mess_ts in ('incorrect_otp', 'Invalid OTP') ";
					else if(input.getResultTS() == 4)
						sql += " and tbl.mess_ts not in ('success', 'incorrect_otp', 'Invalid OTP') ";  
				}
		/*if( input.isTeamLead() )
			   sql += " left join TEAMS team on (ad.OBJECT_TYPE='T' and ad.ASSIGNEE_ID = team.ID) ";
		sql += condition;*/
		
		NativeQuery query = this.session.createNativeQuery(sql)
				.setFirstResult((input.getPage() - 1) * input.getRowPerPage())
				.setMaxResults(input.getRowPerPage());
		
		List lst = query.list();
		if (lst != null && lst.size() > 0)
			results.setDataRows(transformList(lst, ProspectManager.class));

		return results;
	} 
	
	public String getMessCusHis(Integer indexCombobox) {
		String sResult = "";
		switch (indexCombobox) {
		case 1:
			sResult = " = 'success' ";
			break;
		case 2:
			sResult = " in ('invalid_msisdn','msisdn_not_found','Invalid phone number') ";
			break;
		case 3:
			sResult = " in ('unsupported_telco','Not supported telecom') ";
			break;
		case 4:
			sResult = " not in ('success','invalid_msisdn','msisdn_not_found','Invalid phone number','unsupported_telco','Not supported telecom','reject') ";
			break;
		case 5:
			sResult = " = 'reject' ";
			break;
		default:
			break;
		}
		return sResult;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<CallResult> getCallResultByDate(Long custProspectId){
		Query query = this.session.createNamedQuery("findCallResultByCustProspectId").setParameter("custProspectId", custProspectId);
		return (List<CallResult>) query.list();
	}
	
	@SuppressWarnings({ "rawtypes" })
	public boolean checkCustProspectId(Long custProspectId){
		Query query = this.session.createNamedQuery("findCustProspectId").setParameter("custProspectId", custProspectId);
		List result = query.list();
		if (result == null || result.size()<=0) return false;
		return true;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public BigDecimal getAllocationDetailIdByCustProspectId(Long custProspectId) throws NoRecordFoundException{
		List queryResult = this.session.createNamedQuery("findAllocationDetailIdByCustProspectId").setParameter("custProspectId", custProspectId).list();
		if (queryResult != null && queryResult.size()>0) return (BigDecimal) queryResult.get(0);
		return new BigDecimal("1");
	}

	@SuppressWarnings("rawtypes")
	public Integer getNextCallTimesByCustProspectId(Long custProspectId){
		Integer result = 0;
		Query query = this.session.createNamedQuery("getMaxCallTimesByCustProspectId").setParameter("custProspectId", custProspectId);
		List queryResult = query.list();
		if (queryResult != null && queryResult.size()>0) result = (Integer) queryResult.get(0);
		result = result == null ? 1:++result;
		return result;
	}

	public void upsertCallResult(CallResult item) {
		this.session.saveOrUpdate("CallResult",item);
	}
	
	public void upsertCustProspect(CustProspect item) {
		this.session.saveOrUpdate("CustProspect", item);
	}
	
	public void merge(CustProspect item) {
		this.session.merge("CustProspect", item);
	}
	
	public CustProspect getCustProspectById(Long id) {
		return (CustProspect) this.session.getNamedQuery("getCustProspectById").setParameter("custProspectId", id).uniqueResult();
	}
	
	public CustProspect findCustProsptectByUPLCustId(Long uplCustId) {
		return (CustProspect) this.session.getNamedQuery("getCustProspectByUplCustomerId").setParameter("uplCustomerId", uplCustId).uniqueResult();
	}
	
	public CustProspectProduct findCustProsptectProductByUPLCustId(Long uplCustId) {
		try {
			Object obj = this.session.createNamedQuery("findCustomerProspectAndProductByUplId").setParameter("id", uplCustId).uniqueResult();
			if (obj != null) {
				return transformObject(obj, CustProspectProduct.class);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public UplCustomer findUplCustomerById(Long uplCustId) {
		try {
			
			return (UplCustomer) this.session.createNamedQuery("findUplCustomerById").setParameter("id", uplCustId).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public UplCustomerProductDTO findUplCustomerProductById(Long uplCustId) {
		try {
			
			Object obj = this.session.createNamedQuery("findUplCustomerAndProductByUplId").setParameter("id", uplCustId).uniqueResult();
			return transformObject(obj, UplCustomerProductDTO.class);
			   
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public CustProspectXsell findMoreInfoForXsell(Long uplCustId) {
		try {
			
			Object obj = this.session.getNamedNativeQuery("findMoreInfoForXsellByUplCustId").setParameter("uplCustId", uplCustId).uniqueResult();
			
			return transformObject(obj, CustProspectXsell.class);
			   
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getUplCodeByAllocationId(Long allocationId) {
		try {
			return (String) this.session.getNamedQuery("getUplCodeByAllocationId").setParameter("allocationDetailId", allocationId).setMaxResults(1).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public UplMaster getUplMasterByUPLCustId(Long uplCustId) {
		try {
			return this.session.createNativeQuery("select UPL_MASTER.ID,UPL_MASTER.RECORD_STATUS,UPL_MASTER.CREATED_DATE,UPL_MASTER.LAST_UPDATED_DATE,UPL_MASTER.CREATED_BY,UPL_MASTER.LAST_UPDATED_BY,"
					+ "UPL_MASTER.UPL_FORMAT,UPL_MASTER.FROM_SOURCE,UPL_MASTER.UPL_CODE,UPL_MASTER.UPL_TYPE,UPL_MASTER.OWNER_ID,UPL_MASTER.IMPORTED,UPL_MASTER.TOTAL_ALLOCATED "
					+ "from UPL_MASTER inner join UPL_DETAIL on UPL_MASTER.ID = UPL_DETAIL.UPL_MASTER_ID "
					+ "inner join UPL_CUSTOMER on  UPL_DETAIL.ID = UPL_CUSTOMER.UPL_DETAIL_ID "
					+ "where UPL_CUSTOMER.ID = :upl_cust_id",UplMaster.class).setParameter("upl_cust_id", uplCustId).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * @return Prospect List Search
	 * @author kienvt.ho
	 */
	public ProspectManagementData findProspectV2(ProspectSearch input, String campaignCodeLstOfSup) {
		ProspectManagementData results = new ProspectManagementData();
		
		String sql = " select cp.ID as custProspectId, "+
				" nvl(cp.CUST_NAME, uc.CUSTOMER_NAME) custName, " +
				" (select DESCRIPTION1 from CODE_TABLE where ID = ad.STATUS) prospectStatus, " + 
				" CASE ct_from_source.code_value1 " +
				" WHEN 'MIS'" + 
				"		THEN " + 
				"			ct_xs_gender.description1 " + 
				"		ELSE" + 
				"			(SELECT DESCRIPTION1 FROM CODE_TABLE WHERE ID = cp.GENDER) " + 
				"	END  AS gender, " +
				"	CASE ct_from_source.code_value1 " + 
				"		WHEN 'MIS' " + 
				"		THEN " + 
				"			(SELECT description1 FROM CODE_TABLE WHERE id = cari.province) " + 
				"		ELSE " + 
				"			(SELECT DESCRIPTION1 FROM CODE_TABLE WHERE ID = cp.PERMANENT_PROVINCE) " + 
				"	END  AS province, " +
				" uc.province as provinceText, " +
				" to_char(cr.CALL_DATE, 'dd/MM/yyyy HH24:MI') as callDate, " +
				" (select DESCRIPTION1 from CODE_TABLE where ID = cr.CALL_STATUS) as callStatus, " +
				" (select DESCRIPTION1 from CODE_TABLE where ID = cr.CALL_RESULT) as callResult, " +
				" (select DESCRIPTION1 from CODE_TABLE where ID = cr.NEXT_ACTION) as nextAction, uc.ID as uploadCustomerId, " +
				" nvl(to_char(cr.NEXT_ACTION_DATE, 'dd/MM/yyyy HH24:MI'), '') as nextActionDate, u.login_id as loginId, " +
				" cr.NOTE as note, p.PRODUCT_NAME as productName, " +
				" uc.PRE_PRODUCT_NAME as preProductName, " +
				" uc.UDF01 ";
		
				
				if ( !"".equals(input.getMobile()) 
						&& !((input.getUplMasterId() != null && input.getUplMasterId() > 0)
						|| (input.getProspectStatus() != null && input.getProspectStatus() > 0)
						|| !StringUtils.isNullOrEmpty(input.getIdentityNumber())
						|| !StringUtils.isNullOrEmpty(input.getNewMobile())
						|| !"".equals(input.getProductName())
						|| !"".equals(input.getLeadSource())
						|| (input.getCallStatus() != null && input.getCallStatus() > 0)
						|| (input.getCallResult() != null && input.getCallResult() > 0)
						|| (input.getTsaReceiveId() != null && input.getTsaReceiveId() > 0)
						|| (input.isSuperVisor() && !"".equals(campaignCodeLstOfSup)) )
						) {
					sql += "FROM   (SELECT * FROM   upl_customer WHERE  mobile = '"+ input.getMobile() +"' " + 
							"                UNION " + 
							"                SELECT uc2.* FROM   upl_customer uc2 " + 
							"                       INNER JOIN cust_contact_info cci " + 
							"                               ON uc2.cust_id = cci.cust_id " + 
							"                WHERE  cci.contact_type = (SELECT id " + 
							"                                           FROM   code_table " + 
							"                                           WHERE  category = 'CONTAC_TYP' AND code_value1 = 'MOBILE') " + 
							"                       AND cci.contact_category = (SELECT id " + 
							"                                                   FROM   code_table " + 
							"                                                   WHERE " + 
							"                           category = 'CONTAC_CAT' " + 
							"                           AND code_value1 = 'CUSTOMER') " + 
							"                       AND cci.contact_value = '"+ input.getMobile() +"') uc " + 
							"               FULL OUTER JOIN (SELECT * " + 
							"                                FROM   cust_prospect " + 
							"                                WHERE  1 = 1 " + 
							"                                       AND mobile = '"+ input.getMobile() +"') cp " + 
							"                            ON cp.upl_customer_id = uc.id " + 
							"               INNER JOIN (SELECT * " + 
							"                           FROM   allocation_detail " + 
							"                           WHERE 1=1 ";
								
							// This particular sql is just used for Teamlead and TSA	
							if( input.isTeamLead() ) { //query data for teamLead
								sql += " and OBJECT_TYPE='U' and ASSIGNEE_ID in " +
										"(SELECT tm.USER_ID FROM TEAM_MEMBER tm INNER JOIN TEAMS t on t.ID = tm.TEAM_ID WHERE tm.STATUS = 'A' and t.STATUS = 'A' and t.MANAGER_ID = " + input.getUserId() + ") ";
							} else { //query data for TSA
								sql += " and STATUS not in (select ID from CODE_TABLE where CATEGORY = 'ALCTYPE_TL' and CODE_VALUE1 in ('RN', 'C')) and OBJECT_TYPE='U' and ASSIGNEE_ID = " + input.getUserId();
							}
							
							sql += " ) ad " + 
							"                       ON ad.upl_object_id = Nvl(uc.id, cp.upl_customer_id) " + 
							"               INNER JOIN allocation_master am " + 
							"                       ON am.id = ad.allocation_master_id " + 
							"               INNER JOIN upl_master um " + 
							"                       ON am.upl_master_id = um.id " + 
							"               LEFT JOIN users u " + 
							"                      ON ad.assignee_id = u.id " + 
							"               LEFT JOIN products p " + 
							"                      ON uc.product_id = p.id " + 
							"               LEFT JOIN (SELECT * " + 
							"                          FROM   call_result a " + 
							"                          WHERE  EXISTS (SELECT 1 " + 
							"                                         FROM   (SELECT cust_prospect_id, " + 
							"                                                        Max(call_date) CALL_DATE " + 
							"                                                 FROM   call_result " + 
							"                                                 GROUP  BY cust_prospect_id) b " + 
							"                                         WHERE  b.call_date = a.call_date " + 
							"                                                AND b.cust_prospect_id = " + 
							"                                                    a.cust_prospect_id)) " + 
							"                                                        cr " + 
							"                      ON cr.cust_prospect_id = cp.id " + 
							"               LEFT JOIN cust_addr_info cari " + 
							"                      ON ( cari.cust_id = uc.cust_id " + 
							"                           AND cari.addr_type = (SELECT id " + 
							"                                                 FROM   code_table ct " + 
							"                                                 WHERE  ct.code_group = 'CONT' " + 
							"                                                        AND ct.category = " + 
							"                                                            'ADDR_TYPE' " + 
							"                                                        AND ct.code_value1 = " + 
							"                                                            'TEMPORARY') ) " + 
							"               LEFT JOIN cust_personal_info cpi " + 
							"                      ON uc.cust_id = cpi.id " + 
							"               LEFT JOIN code_table ct_xs_gender " + 
							"                      ON cpi.gender = ct_xs_gender.id " + 
							"               LEFT JOIN code_table ct_from_source " + 
							"                      ON um.from_source = ct_from_source.id " + 
							"        WHERE  um.from_source <> (SELECT id " + 
							"                                  FROM   code_table " + 
							"                                  WHERE  category = 'UPL_SRC' " + 
							"                                         AND code_value1 = 'WH') " + 
							"        ORDER  BY am.id DESC";
				} else {
					
					sql += " FROM   (SELECT ad_sub.*, " + 
							"                       um.id          UM_ID,  " + 
							"                       um.from_source um_from_source  " + 
							"                FROM   allocation_detail ad_sub  " + 
							"                       INNER JOIN allocation_master am  " + 
							"                               ON am.id = ad_sub.allocation_master_id  " + 
							"                       INNER JOIN upl_master um  " + 
							"                               ON am.upl_master_id = um.id  " + 
							"                WHERE  um.from_source <> (SELECT id  " + 
							"                                          FROM   code_table  " + 
							"                                          WHERE  category = 'UPL_SRC'  " + 
							"                                                 AND code_value1 = 'WH')  ";
					
							if (input.getUplMasterId() != null && input.getUplMasterId() > 0) { // Campaign code
								sql += " AND um.id = " + input.getUplMasterId();
							}
							
							if (input.getProspectStatus() != null && input.getProspectStatus() > 0) { // Status
								sql += " and ad_sub.STATUS = " + input.getProspectStatus();
							}
							
							if (input.getTsaReceiveId() != null && input.getTsaReceiveId() > 0) // TSA assignee
								sql += " and ad_sub.OBJECT_TYPE = 'U' and ad_sub.ASSIGNEE_ID = " + input.getTsaReceiveId();
							
							if( input.isSuperVisor() && !"".equals(campaignCodeLstOfSup) ) { //query data for Supervisor
								sql += " and um.UPL_CODE in " + StringUtils.putArrayStringIntoParameter(campaignCodeLstOfSup);
							} else if( input.isTeamLead() ) { //query data for teamLead
								sql += " and ad_sub.OBJECT_TYPE='U' and ad_sub.ASSIGNEE_ID in " +
										"(SELECT tm.USER_ID FROM TEAM_MEMBER tm INNER JOIN TEAMS t on t.ID = tm.TEAM_ID WHERE tm.STATUS = 'A' and t.STATUS = 'A' and t.MANAGER_ID = " + input.getUserId() + ") ";
							} else { //query data for TSA
								sql += " and ad_sub.STATUS not in (select ID from CODE_TABLE where CATEGORY = 'ALCTYPE_TL' and CODE_VALUE1 in ('RN', 'C')) and ad_sub.OBJECT_TYPE='U' and ad_sub.ASSIGNEE_ID = " + input.getUserId();
							}
							
							sql += " 		) ad  " + 
							"               LEFT JOIN upl_customer uc  " + 
							"                      ON (uc.id = ad.upl_object_id " + 
							"                        ) " + 
							"               LEFT JOIN cust_prospect cp  " + 
							"                      ON (cp.upl_customer_id = ad.upl_object_id " + 
							"                        ) " + 
							"               LEFT JOIN users u  " + 
							"                      ON ad.assignee_id = u.id  " + 
							"               LEFT JOIN products p  " + 
							"                      ON uc.product_id = p.id  " + 
							"                       " + 
							"               LEFT JOIN (SELECT *  " + 
							"                          FROM   call_result a  " + 
							"                          WHERE  EXISTS (SELECT 1  " + 
							"                                         FROM   (SELECT cust_prospect_id,  " + 
							"                                                        Max(call_date) CALL_DATE  " + 
							"                                                 FROM   call_result  " + 
							"                                                 GROUP  BY cust_prospect_id) b  " + 
							"                                         WHERE  b.call_date = a.call_date  " + 
							"                                                AND b.cust_prospect_id =  " + 
							"                                                    a.cust_prospect_id))  " + 
							"                                                        cr  " + 
							"                      ON cr.cust_prospect_id = cp.id  "; 

							if (!"".equals(input.getMobile())) {
								sql += " LEFT JOIN CUST_CONTACT_INFO cci ON ( cci.CUST_ID = uc.CUST_ID	AND " + 
										" cci.CONTACT_TYPE = (SELECT id FROM CODE_TABLE WHERE code_group = 'CONT' AND  category = 'CONTAC_TYP' AND code_value1 = 'MOBILE') " + 
										" AND cci.CONTACT_CATEGORY= (SELECT id FROM CODE_TABLE WHERE code_group = 'CONT' AND  category = 'CONTAC_CAT' AND code_value1 = 'CUSTOMER') " + 
										" AND cci.CONTACT_PRIORITY = 1 " +
										" and cci.contact_value = '"+ input.getMobile() +"') "; 
							}
							
							sql += "         LEFT JOIN cust_addr_info cari  " + 
							"                      ON ( cari.cust_id = uc.cust_id  " + 
							"                           AND cari.addr_type = (SELECT id  " + 
							"                                                 FROM   code_table ct  " + 
							"                                                 WHERE  " + 
							"                               ct.category = 'ADDR_TYPE'  " + 
							"                               AND ct.code_value1 =  " + 
							"                                   'TEMPORARY') )  " + 
							"               LEFT JOIN cust_personal_info cpi  " + 
							"                      ON uc.cust_id = cpi.id  " + 
							"               LEFT JOIN code_table ct_xs_gender  " + 
							"                      ON cpi.gender = ct_xs_gender.id  " + 
							"               LEFT JOIN code_table ct_from_source  " + 
							"                      ON ct_from_source.id = ad.um_from_source " + 
							"               where 1=1 ";
							
							if (!"".equals(input.getMobile()))
								sql += " and (uc.mobile = '"+ input.getMobile() +"' or cp.mobile = '"+ input.getMobile() +"' or cci.contact_value = '"+ input.getMobile() +"') ";
							
							if (!StringUtils.isNullOrEmpty(input.getIdentityNumber()))
								sql += " and (uc.identity_number = '" + input.getIdentityNumber() + "'  OR uc.identity_number_army = '" + input.getIdentityNumber() + "' OR cp.identity_number = '"+input.getIdentityNumber()+"' )";
							
							if (!StringUtils.isNullOrEmpty(input.getNewMobile())) {
								sql += " and cp.new_mobile = '"+ input.getNewMobile() +"' ";
							}
							
							if (!"".equals(input.getProductName()))
								sql += " and (p.PRODUCT_NAME in " 
										+ StringUtils.putArrayStringIntoParameter(input.getProductName().trim())
										+ " or uc.PRE_PRODUCT_NAME in "
										+ StringUtils.putArrayStringIntoParameter(input.getProductName().trim()) 
										+ " ) ";
							
							if (!"".equals(input.getLeadSource()))
								sql += " and uc.LEAD_SOURCE = '" + input.getLeadSource().trim() + "' ";
							
							if (input.getCallStatus() != null && input.getCallStatus() > 0)
								sql += " and (cr.CALL_STATUS = " + input.getCallStatus() + " and cr.STATUS <> 'C') ";
							
							if (input.getCallResult() != null && input.getCallResult() > 0)
								sql += " and cr.CALL_RESULT in ( select ID from CODE_TABLE where CODE_GROUP = 'CALL' and CATEGORY = 'CALL_RSLT' and upper(DESCRIPTION1) = ( "
									       	    + " select upper(DESCRIPTION1) from CODE_TABLE where CODE_GROUP = 'CALL' and CATEGORY = 'CALL_RSLT' and ID = " + input.getCallResult()
									       	 + " ) "
									       + " ) ";
					
				}
				
					
		String countingSql = "select count(*) from ("+ sql +")";		
		
		results.setTotalRows((BigDecimal) this.session.createNativeQuery(countingSql).uniqueResult());
				
		NativeQuery query = this.session.createNativeQuery(sql)
				.setFirstResult((input.getPage() - 1) * input.getRowPerPage())
				.setMaxResults(input.getRowPerPage());
		
		List lst = query.list();
		if (lst != null && lst.size() > 0)
			results.setDataRows(transformList(lst, ProspectManager.class));
		
		return results;
	}

	public EmployeeDTO getManagerByLoginId (String loginId) {
		StringBuilder sql = new StringBuilder("select ep.ID ,ep.RECORD_STATUS ,ep.CREATED_DATE ,ep.LAST_UPDATED_DATE ,")
				.append("ep.CREATED_BY ,ep.LAST_UPDATED_BY ,ep.FULL_NAME ,ep.EMAIL ,ep.HR_CODE ,ep.MOBILE_PHONE ,")
				.append("ep.EXT_PHONE ,ep.STATUS ,ep.START_EFF_DATE ,ep.END_EFF_DATE ,ep.STAFF_ID_IN_BPM ")
				.append("from teams te, team_member tm, users us, employees ep ")
				.append("where 1=1 ")
				.append("and tm.user_id in (select id from users where login_id=:loginId) ")
				.append("and te.id = tm.team_id ")
				.append("and te.manager_id = us.id ")
				.append("and ep.id = us.emp_id ")
				.append("and rownum=1 ")
				.append("order by ep.id desc ");
		Query<?> query = this.session.createNativeQuery(sql.toString());
		query.setParameter("loginId", loginId);
		Object obj =  query.uniqueResult();
		if (null != obj) {
			return transformObject(obj, EmployeeDTO.class);
		}

		return null;
	}

}
