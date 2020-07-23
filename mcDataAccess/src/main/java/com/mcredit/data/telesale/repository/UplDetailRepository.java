package com.mcredit.data.telesale.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.repository.IAddRepository;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.data.telesale.entity.UplDetail;
import com.mcredit.mobile.object.xsell.XSellSearchInfo;
import com.mcredit.model.dto.xsell.XSellFileDTO;
import com.mcredit.model.enums.Commons;
import com.mcredit.model.enums.ExcellEnums;
import com.mcredit.model.enums.LeadGenEnums;
import com.mcredit.util.DateUtil;
import com.mcredit.util.StringUtils;


public class UplDetailRepository extends BaseRepository
		implements IUpsertRepository<UplDetail>, IAddRepository<UplDetail> {


	public UplDetailRepository(Session session) {
		super(session);
	}

	@Override
	public void add(UplDetail item) {
		this.session.save("UplDetail", item);
	}

	public void upsert(UplDetail item) {
		if (item.getId() == null)
			this.session.save("UplDetail", item);
		else
			this.session.merge("UplDetail", item);
	}

	public void merge(UplDetail item) {
		this.session.merge("UplDetail", item);
	}

	public void remove(UplDetail item) {
		this.session.delete("UplDetail", item);
	}

	@SuppressWarnings("rawtypes")
	public Integer getNextCallTimesByCustProspectId(Long uplMasterId) {
		Integer result = null;
		Query query = this.session.createNamedQuery("getSeqTimesByUplMasterId").setParameter("uplMasterId",
				uplMasterId);
		List queryResult = query.list();
		if (queryResult != null && queryResult.size() > 0)
			result = (Integer) queryResult.get(0);
		if (result == null)
			result = 0;
		result++;
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<UplDetail> findUPLDetailByUplMasterIdStatusVP(Long uplMasterId, String fromSource, String isAsm, String xsm) {
		
		List<UplDetail> result = null;
		
		if( (LeadGenEnums.LEAD_GEN.value()).equals(fromSource) || (LeadGenEnums.SCRIPT.value()).equals(fromSource)) {
			result = this.session.getNamedQuery("findUPLDetailByUplMasterIdStatusLeadGen")
					   .setParameter("uplMasterId", uplMasterId)
					   .list();
		}else if ( (ExcellEnums.EXCEL_FROM_SOURCE.stringValue()).equals(fromSource) ) {
			List<?> resultSql = this.session.getNamedNativeQuery("findUPLDetailByUplMasterIdStatusVP_EXCEL")
					   .setParameter("uplMasterId", uplMasterId)
					   .setParameter("statusLst", Arrays.asList("C", "P"))					   
					   .list();
			return transformList(resultSql, UplDetail.class);
		}else if ( (Commons.TS_UPL_SOURCE.value()).equals(fromSource)
				|| (Commons.MIS_MB_UPL_SOURCE.value()).equals(fromSource)
				|| (Commons.TC_UPL_SOURCE.value()).equals(fromSource)
				|| (Commons.AMO_UPL_SOURCE.value()).equals(fromSource) ) {

			result =  this.session.getNamedQuery("findUPLDetailByUplMasterIdStatusVP")
					   .setParameter("uplMasterId", uplMasterId)
					   .setParameter("statusLst", Arrays.asList("C", "P", "L"))
					   .list();
		}else
			System.out.println("UplDetailRepository.findUPLDetailByUplMasterIdStatusVP().error: missing case specify UplSource.");
				
		return result;
	}

	public int updateuplDetailSupervisor(Long allocatedNumber, Long uplDetailId) {
		return this.session.getNamedQuery("updateuplDetailSupervisor").setParameter("allocatedNumber", allocatedNumber)
				.setParameter("uplDetailId", uplDetailId).executeUpdate();
	}
	
	public Long getIdUplDetailSourceBPM() {
		List<BigDecimal> lstResult = this.session.getNamedQuery("getUplDetailFromSourceBPM").list();
		return lstResult == null ? null : lstResult.get(0).longValue();
	}

	public UplDetail findUplDetailbyID(Long uplMaterid) {
		UplDetail uplDetail = null;
		@SuppressWarnings("unchecked")
		List<UplDetail> results = this.session.getNamedQuery("findUplDetailbyID").setParameter("id", uplMaterid)
				.getResultList();
		if (!results.isEmpty())
			uplDetail = results.get(0);
		return uplDetail;
	}

	public UplDetail findUplDetailbyMaterID(Long uplMaterid) throws Exception{
		UplDetail uplDetail = null;
		@SuppressWarnings({"deprecation", "unchecked" })
		List<UplDetail> results = this.session.createCriteria(UplDetail.class).add(Restrictions.eq("uplMasterId", uplMaterid)).list();
		if (!results.isEmpty())
			uplDetail = results.get(0);
		return uplDetail;
	}
	
	public Long findAllocatedNumberInUplDetail(Integer allocatedNumber, Long id) {
		return (Long) this.session.getNamedQuery("findAllocatedNumberInUplDetail")
				.setParameter("allocatedNumber", allocatedNumber).setParameter("uplDetailId", id).getSingleResult();
	}

	public UplDetail getUplDetailbyId(Long id) {
		return (UplDetail) this.session.getNamedQuery("findUplDetailbyID").setParameter("id", id).getSingleResult();
	}

	public UplDetail findCurrentPendingUplDetail(String userLogin, List<String> status) {
		return (UplDetail) this.session.getNamedQuery("findPendingUplDetail").setParameter("userLogin", userLogin)
				.setParameter("status", status).setMaxResults(1).getSingleResult();
	}
	

	// XSell
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<XSellFileDTO> searchXSellFiles(Long statusId, String fromDateUpload, String toDateUpload) {

		String condition = "";

		String checkStatusApp = " AND UD.STATUS_APP = :statusAppId";
		String checkDateFrom = " AND UD.CREATED_DATE >= TO_DATE(:fromDateUpload, 'DD/MM/YYYY HH24:MI:SS')";
		String checkDateTo = " AND UD.CREATED_DATE <= TO_DATE(:toDateUpload, 'DD/MM/YYYY HH24:MI:SS')";

		if (statusId != null) {
			condition += checkStatusApp;
		}

		if (!StringUtils.isNullOrEmpty(fromDateUpload) && !StringUtils.isNullOrEmpty(toDateUpload)) {
			condition += (checkDateFrom + checkDateTo);

		} else if (!StringUtils.isNullOrEmpty(fromDateUpload)) {
			condition += checkDateFrom;

		} else if (!StringUtils.isNullOrEmpty(toDateUpload)) {
			condition += checkDateTo;
		}

		
		String strQuery = " SELECT " + 
							"	UD.ID, " +
							"   to_char(UD.CREATED_DATE, 'dd/mm/yyyy')  AS upload_date, " +
							"   ud.CREATED_BY AS user_upload, " +
							"   ud.approver," +
							"	UD.UPL_FILE_NAME, " +
							"	UM.UPL_CODE, " +
							"	UD.DATE_FROM, " +
							"	UD.DATE_TO, " +
							"	UD.SERVER_FILE_NAME, " +
							"	CT.DESCRIPTION1, " +
							"	UD.REJECTION_REASON, " +
							"	to_char(UD.DATE_APP, 'dd/mm/yyyy'), " +
							"	UD.STATUS_APP " +
							" FROM " +
							"	UPL_DETAIL UD " +
							" JOIN UPL_MASTER UM ON UD.UPL_MASTER_ID = UM.ID AND UM.RECORD_STATUS='A' " +
							"  JOIN CODE_TABLE CT ON CT.ID = UD.STATUS_APP" +
							" WHERE UD.RECORD_STATUS='A' AND UM.FROM_SOURCE = (SELECT ID FROM CODE_TABLE WHERE CODE_GROUP = 'MISC' AND CATEGORY='UPL_SRC' AND CODE_VALUE1='MIS')" + condition 
							+ " ORDER BY trunc(UD.CREATED_DATE) DESC, trunc(UD.DATE_FROM) desc, trunc(ud.date_to) desc";

		NativeQuery query = this.session.createNativeQuery(strQuery).setHibernateFlushMode(FlushMode.ALWAYS); 

		if (statusId != null) {
			query.setParameter("statusAppId", statusId);
		}
		
		if (!StringUtils.isNullOrEmpty(fromDateUpload)) {
			query.setParameter("fromDateUpload", fromDateUpload + " 00:00:00");
		}

		if (!StringUtils.isNullOrEmpty(toDateUpload)) {
			query.setParameter("toDateUpload", toDateUpload + " 23:59:59");
		}


		List files = query.list();
		List<XSellFileDTO> xSellFiles = transformList(files, XSellFileDTO.class);
		return xSellFiles;
	}

        
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<XSellFileDTO> searchXSellFiles(Date date) throws ParseException {

		String strQuery = " SELECT " + 
							"	UD.ID, " +
							"	UD.CREATED_DATE, " +
							"   ud.CREATED_BY AS user_upload, " +
							"   ud.approver," +
							"	UD.UPL_FILE_NAME, " +
							"	UM.UPL_CODE, " +
							"	UD.DATE_FROM, " +
							"	UD.DATE_TO, " +
							"	UD.SERVER_FILE_NAME, " +
							"	CT.DESCRIPTION1, " +
							"	UD.REJECTION_REASON, " +
							"	UD.DATE_APP, " +
							"	UD.STATUS_APP " +
							" FROM " +
							"	UPL_DETAIL UD " +
							" INNER JOIN UPL_MASTER UM ON UD.UPL_MASTER_ID = UM.ID AND UM.RECORD_STATUS='A' " +
							" LEFT JOIN CODE_TABLE CT ON CT.ID = UD.STATUS_APP" +
							" WHERE UD.RECORD_STATUS='A' AND UM.FROM_SOURCE = (SELECT ID FROM CODE_TABLE WHERE CODE_GROUP = 'MISC' AND CATEGORY='UPL_SRC' AND CODE_VALUE1='MIS') AND UD.DATE_FROM <= TO_DATE(:date, 'DD/MM/YYYY HH24:MI:SS') AND UD.DATE_TO >= TO_DATE(:date, 'DD/MM/YYYY HH24:MI:SS')" ; 

		NativeQuery query = this.session.createNativeQuery(strQuery).setHibernateFlushMode(FlushMode.ALWAYS); 
		query.setParameter("date", DateUtil.toString(date, "dd/MM/yyyy"));

		List files = query.list();
		List<XSellFileDTO> xSellFiles = transformList(files, XSellFileDTO.class);
		return xSellFiles;
	}
	
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<XSellSearchInfo> searchXSellInfoBorrow(String identityNumber) {
        List<XSellSearchInfo> results;
        if (identityNumber != null && !identityNumber.trim().equals("")) {
        	 String sqlQuery = "select distinct cpi.ID as doc_id, cpi.CUST_NAME, cmt.IDENTITY_NUMBER cmt_identity_number, cmt.IDENTITY_ISSUE_DATE cmt_identity_issue_date, "
                    + "    nvl(cmt.IDENTITY_ISSUE_PLACE_TEXT,(select DESCRIPTION1 from CODE_TABLE where ID = cmt.IDENTITY_ISSUE_PLACE)) cmt_issue_place,"
                    + "    cmqd.IDENTITY_NUMBER cmqd_identity_number, cmqd.IDENTITY_ISSUE_DATE cmqd_itentity_issue_date,"
                    + "    nvl(cmqd.IDENTITY_ISSUE_PLACE_TEXT,(select DESCRIPTION1 from CODE_TABLE where ID = cmqd.IDENTITY_ISSUE_PLACE)) cmqd_issue_place,"
                    
                    
                    + "   cpi.BIRTH_DATE,cci.CONTACT_VALUE mobile,addr.ADDRESS permanent_addr,"
					+ "    nvl (" + 
					"	    	( SELECT DESCRIPTION1 FROM CODE_TABLE WHERE id = addr.ward) ," + 
					"	    	addr.ward_addr" + 
					"	    ) AS per_ward,"
					+ "   nvl (" + 
					"	     	(SELECT DESCRIPTION1 FROM CODE_TABLE WHERE addr.district = id)," + 
					"	     	addr.district_addr" + 
					"	    )" + 
					"	    AS per_district,"
                	+ "   ( SELECT DESCRIPTION1 FROM CODE_TABLE WHERE addr.province = id ) AS per_province,"
                    
                    + " addr1.ADDRESS temporary_addr,"
                    + "   nvl (" + 
                    "       	(SELECT DESCRIPTION1 FROM CODE_TABLE WHERE addr1.ward = id)," + 
                    "       	addr1.ward_addr" + 
                    "       )" + 
                    "        AS temp_ward,"
                    + "   nvl (" + 
                    "        	(SELECT DESCRIPTION1 FROM CODE_TABLE WHERE addr1.district = id)," + 
                    "        	addr1.district_addr" + 
                    "        )" + 
                    "        AS temp_district,"
            	    + "  ( SELECT DESCRIPTION1 FROM CODE_TABLE WHERE addr1.province = id ) AS temp_province,"
                    
                    + "    cai.COMPANY_NAME, cai.COMPANY_ADDR,"
                    + "    nvl (" + 
                    "     	(SELECT DESCRIPTION1 FROM CODE_TABLE WHERE cai.ward_id = id)," + 
                    "     	cai.company_ward" + 
                    "     )" + 
                    "   " + 
                    "         AS com_ward,"
                    + "      	nvl (" + 
                    "     	(SELECT DESCRIPTION1 FROM CODE_TABLE WHERE cai.district_id = id)," + 
                    "     	cai.company_district" + 
                    "     )" + 
                    "         AS com_district,"
                    + "   ( SELECT DESCRIPTION1 FROM CODE_TABLE WHERE cai.province_id = id ) AS com_province,"
                	
                    + "(select DESCRIPTION1 from CODE_TABLE where ID = cpi.GENDER) gender,"
                    + "    uc.APP_PRODUCT_NAME,uc.APP_PRODUCT_CODE,uc.APP_LOAN_APPROVED_AMT,uc.APP_INT_RATE,"
                    + "    uc.APP_TERM_LOAN,uc.DISBURSEMENT_DATE,uc.MAT_DATE,uc.APP_EMI,uc.COMMODITIES_CODE,"
                    + "    uc.PRE_PRODUCT_NAME,uc.PRE_PRODUCT_CODE,uc.PRE_MIN_LIMIT,uc.PRE_MAX_LIMIT,"
                    + "    uc.PRE_MIN_TENOR,uc.PRE_MAX_TENOR,uc.PRE_MIN_EMI,uc.PRE_MAX_EMI,"
                    + "    cai.REF_FULL_NAME1, (select DESCRIPTION1 from CODE_TABLE where ID = cai.RELATION_REF_PERSON1) relation1, cai.REF_PERSON1_MOBILE,"
                    + "    cai.REF_FULL_NAME2, (select DESCRIPTION1 from CODE_TABLE where ID = cai.RELATION_REF_PERSON2) relation2, cai.REF_PERSON2_MOBILE,"
                    + " uc.DATA_SOURCE ,uc.LEAD_SOURCE, ud.DATE_FROM , ud.DATE_TO "
                    + " from UPL_CUSTOMER uc "
                    + " inner join CUST_PERSONAL_INFO cpi on uc.CUST_ID = cpi.ID"
                    + " inner join CUST_IDENTITY cmt on cmt.ID = cpi.IDENTITY_ID"
                    + " left join CUST_IDENTITY cmqd on cpi.ID = cmqd.CUST_ID "
                    + "    and cmqd.IDENTITY_NUMBER = :identityNumber "
                    + " left join CUST_ADDL_INFO cai on cpi.ID = cai.CUST_ID"
                    + " left join CUST_CONTACT_INFO cci on cpi.ID = cci.CUST_ID and cci.CONTACT_TYPE = (select ID from CODE_TABLE where category = 'CONTAC_TYP' and CODE_VALUE1 = 'MOBILE')"
                    + "    and cci.CONTACT_CATEGORY = (select ID from CODE_TABLE where category = 'CONTAC_CAT' and CODE_VALUE1 = 'CUSTOMER')"
                    + "    and cci.CONTACT_PRIORITY = 1"
                    + " left join CUST_ADDR_INFO addr on cpi.ID = addr.CUST_ID "
                    + "    and addr.ADDR_TYPE = (select ID from CODE_TABLE where category = 'ADDR_TYPE' and CODE_VALUE1 = 'PERMANENT')"
                    + "    and addr.ADDR_ORDER = 1"
                    + " left join CUST_ADDR_INFO addr1 on cpi.ID = addr1.CUST_ID "
                    + "    and addr1.ADDR_TYPE = (select ID from CODE_TABLE where category = 'ADDR_TYPE' and CODE_VALUE1 = 'TEMPORARY')"
                    + "    and addr1.ADDR_ORDER = 1"
                    + " join upl_detail ud on ud.id = uc.UPL_DETAIL_ID and ud.RECORD_STATUS='A' "
                    + " join code_table ctb on ctb.id = ud.status_app and ctb.code_group='MIS' and  ctb.category='STATUS_APP_XSELL' and ctb.code_value1='XSELL_APP_ACTIVE' "
                    + " join upl_master um on um.id = ud.upl_master_id and um.RECORD_STATUS='A' "
                    + " join code_table ctbm on ctbm.id = um.FROM_SOURCE and  ctbm.category='UPL_SRC' and ctbm.code_value1='MIS'  "
                    + " where uc.IDENTITY_NUMBER = :identityNumber or uc.IDENTITY_NUMBER_ARMY = :identityNumber";

            NativeQuery query = this.session.createNativeQuery(sqlQuery);
            query.setParameter("identityNumber", identityNumber.trim());

            List lst = query.list();

            results = transformList(lst, XSellSearchInfo.class);
        } else {
            results = null;
        }

        return results;
    }

	public int approvalUpl(String loginId, Long idCodetable, Long idUpl) {
		return this.session.getNamedQuery("approveUplDetail").setParameter("status", idCodetable)
				.setParameter("idFile", idUpl).setParameter("loginId", loginId).executeUpdate();
	}

	public int refuseUpl(String loginId, Long idCodetable, String rejectReason, Long idUpl) {
		return this.session.getNamedQuery("rejectUplDetail").setParameter("status", idCodetable)
				.setParameter("rejectReason", rejectReason).setParameter("idFile", idUpl).setParameter("loginId", loginId).executeUpdate();
	}

	public int updateUpl(String status, Long idUpl) {
		return this.session.getNamedQuery("updateUplDetail").setParameter("status", status).setParameter("idFile", idUpl)
				.executeUpdate();
	}
	
	public int deleteUpl(Long idUpl) {
		return this.session.getNamedQuery("deleteUplDetail").setParameter("idFile", idUpl)
				.executeUpdate();
	}
	
	public int changeStatus(String status, Long uplDetailId) {
		return this.session.getNamedQuery("changeStatus").setParameter("status", status).setParameter("UPL_DETAIL_ID", uplDetailId)
				.executeUpdate();

	}
	
	public void disableByDate(Date date, Long activeStatus,Long inactiveStatus) throws ParseException {

		String condition = "update UPL_DETAIL set STATUS_APP = :inactiveStatus where DATE_TO < TO_DATE(:date, 'DD/MM/YYYY HH24:MI:SS') and STATUS_APP = :activeStatus";
		
		this.session.createNativeQuery(condition).setHibernateFlushMode(FlushMode.ALWAYS).setParameter("date", DateUtil.toString(date, "dd/MM/yyyy") + " 00:00:00").setParameter("activeStatus", activeStatus).setParameter("inactiveStatus", inactiveStatus).executeUpdate(); 
	}
	
	public void confirmNextImportTSA(Long uplDetailId) {
		 this.session.getNamedQuery("updateStatusByImportTSA").setParameter("uplDetailId", uplDetailId).executeUpdate();
		 this.session.getNamedQuery("removeAlloMasterByImportTSA").setParameter("uplDetailId", uplDetailId).executeUpdate();
		 this.session.getNamedQuery("removeAlloDetailByImportTSA").setParameter("uplDetailId", uplDetailId).executeUpdate();
		 this.session.getNamedQuery("removeCustomerByImportTSA").setParameter("uplDetailId", uplDetailId).executeUpdate();
	}
	
	public String getUplCodeByDetailId(Long uplDetailId) {
	 return (String) this.session.getNamedQuery("getUplCodeByDetailId").setParameter("uplDetailId", uplDetailId).uniqueResult();
	}
}
