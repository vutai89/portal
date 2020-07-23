package com.mcredit.data.telesale.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.LongType;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.pcb.entity.CreditBureauData;
import com.mcredit.data.repository.IMergeRepository;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.data.telesale.entity.UplCustomer;
import com.mcredit.model.dto.telesales.ScoreLGResult;
import com.mcredit.model.enums.AmoEnums;
import com.mcredit.model.enums.LeadGenEnums;
import com.mcredit.model.leadgen.UploadCustomerDTO;
import com.mcredit.model.leadgen.UploadDataId;
import com.mcredit.model.object.warehouse.LeadGenCustomerInfo;
import com.mcredit.util.StringUtils;

public class UplCustomerRepository extends BaseRepository
		implements IUpsertRepository<UplCustomer>, IMergeRepository<UplCustomer> {

	public UplCustomerRepository(Session session) {
		super(session);
	}

	public void add(UplCustomer item) {
		this.session.save("UplCustomer", item);
	}

	public void upsert(UplCustomer item) {
		this.session.saveOrUpdate("UplCustomer", item);
	}

	public void upsertImportXsell(UplCustomer item, int count) {
		this.session.save("UplCustomer", item);
		if (count % 1000 == 0) {
			session.flush();
			session.clear();
		}
	}

	public void merge(UplCustomer item) {
		this.session.merge("UplCustomer", item);
	}

	public void remove(UplCustomer item) {
		this.session.delete("UplCustomer", item);
	}

	public int removeWithDetailsID(long idUpl) {
		return this.session.getNamedQuery("deleteUplCustomer").setParameter("idFile", idUpl).executeUpdate();
	}

	@SuppressWarnings({ "unchecked" })
	public List<LeadGenCustomerInfo> findLeadGenCustomerInfo(String identityNumber, String mobileNumber) {

		List<LeadGenCustomerInfo> results = new ArrayList<>();

		String sql = " select uc.CUSTOMER_NAME as custName, uc.IDENTITY_NUMBER as identityNumber, uc.MOBILE as mobilePhone, uc.PROVINCE as province, uc.SCORE_RANGE as scoreRange "
                + " , CASE uc.INCOME "
                + " WHEN '0' THEN '< 10.000.000 vnd' "
                + " WHEN '1' THEN '>= 10.000.000 vnd' "
                + " ELSE 'N/A' "
                + " END as income "
                + " , uc.SOURCE_SYSTEM as sourceInfo, p.PRODUCT_NAME as productName, p.PRODUCT_CODE as productCode "
                + " , trim(to_char(p.MIN_LOAN_AMOUNT,'999,999,999')) || ' vnd' as levelMin, trim(to_char(p.MAX_LOAN_AMOUNT,'999,999,999')) || ' vnd' as levelMax, p.MIN_TENOR as periodMin, p.MAX_TENOR as periodMax "
                + " , it.YEARLY_RATE || '%' as interestRateYear, it.MONTHLY_RATE || '%' as interestRateMonth, cm.CUST_NAME as watchListResult "
                + " from UPL_CUSTOMER uc "
                + " inner join UPL_DETAIL ud on ud.ID  = uc.UPL_DETAIL_ID " 
                + " inner join UPL_MASTER um on um.ID = ud.UPL_MASTER_ID " 
                + " inner join CODE_TABLE ct on ct.ID = um.FROM_SOURCE " 
                + " inner join PRODUCTS p on uc.PRODUCT_ID = p.ID "
                + " left join INTEREST_TABLE it on p.RATE_INDEX = it.ID "
                + " left join CUST_MONITOR cm on (cm.MONITOR_TYPE = 'W' and cm.ID_NUMBER = uc.MOBILE) "
                + " where uc.SCORE_RANGE is not null and uc.PRODUCT_ID is not null and uc.RESPONSE_CODE = '" + LeadGenEnums.OK.value() + "' "
                + " and  ct.CATEGORY = 'UPL_SRC' and ct.CODE_VALUE1 = 'LEAD_GEN' "
                + " and (uc.MOBILE = :mobileNumber " + (!"".equals(identityNumber) ? " and uc.IDENTITY_NUMBER = :identityNumber " : "") + ") ";

		Query<?> query = this.session.createNativeQuery(sql).setParameter("mobileNumber", mobileNumber.trim());

		if (!"".equals(identityNumber)) {
			query.setParameter("identityNumber", identityNumber.trim());
		}

		List<?> lst = query.list();

		if (lst != null && !lst.isEmpty()) {
			results = transformList(lst, LeadGenCustomerInfo.class);
		}

		return results;
	}

	@SuppressWarnings("unchecked")
	public List<UplCustomer> findBy(Long uplDetailId) {
		return this.session.getNamedQuery("findUplDetailId").setParameter("uplDetailId", uplDetailId).list();
	}

	@SuppressWarnings("unchecked")
	public UplCustomer findById(Long id) {
		return this.session.get(UplCustomer.class, id);
	}

	@SuppressWarnings("unchecked")
	public UplCustomer findByCondition(String mobile, String identityNumber) {
		Query query = this.session.createQuery("from UplCustomer where mobile=:p_mobile").setParameter("p_mobile",
				mobile);
		if (!StringUtils.isNullOrEmpty(identityNumber))
			query = this.session
					.createQuery("from UplCustomer where mobile=:p_mobile and identityNumber=:p_identityNumber")
					.setParameter("p_mobile", mobile).setParameter("p_identityNumber", identityNumber);
		return (UplCustomer) query.uniqueResult();
	}
	
	/* tuannn1: check for dupplicate phoneNumber and identityNumber in UplCustomer */
//	@SuppressWarnings("unchecked")
//	public UplCustomer findUplCustomerByMobileAndIdentNumber(String mobile, String identityNumber) {
//		Query query = this.session.createQuery("from UplCustomer where mobile=:p_mobile and (trunc(SYSDATE) - trunc(last_updated_date)) <= 90").setParameter("p_mobile",
//				mobile);
//		if (!StringUtils.isNullOrEmpty(identityNumber))
//			query = this.session
//					.createQuery("from UplCustomer where mobile=:p_mobile and identityNumber=:p_identityNumber and (trunc(SYSDATE) - trunc(last_updated_date)) <= 90")
//					.setParameter("p_mobile", mobile).setParameter("p_identityNumber", identityNumber);
//		return (UplCustomer) query.uniqueResult();
//	}
	
	/* tuannn1: end*/
	
	@SuppressWarnings("unchecked")
	public List<UplCustomer> findUplCustomerbyID(Long id) {
		return this.session.getNamedQuery("findUplCustomerbyID").setParameter("id", id).list();
	}

	@SuppressWarnings("unchecked")
	public UplCustomer getUplCustomerbyID(Long id) {
		return this.session.get(UplCustomer.class, id);
	}

	public int deleteBy(Long uplDetailId) {
		return this.session.getNamedNativeQuery("deleteUplCustomerByUplDetailId1")
				.setParameter("UPL_DETAIL_ID", uplDetailId).executeUpdate();
	}

	public void checkUplCustomer(Long id) {
		StoredProcedureQuery query = this.session.createStoredProcedureCall("validate_Upload_Customer")
				.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN).setParameter(1, id);
		query.execute();
	}

	public Integer checkDupOldData(Long UplDetailId) {
		return ((BigDecimal) this.session.getNamedNativeQuery("checkDupOldData")
				.setParameter("uplDetailId", UplDetailId).getSingleResult()).intValue();
	}

	public Integer checkDupDataLeadGenInput(String identityNumber, String mobile) {
		return ((BigDecimal) this.session.getNamedNativeQuery("checkDupDataLeadGenInput")
				.setParameter("identityNumber", identityNumber.trim()).setParameter("mobile", mobile.trim())
				.getSingleResult()).intValue();
	}

	public Integer checkBlackListWatchListLeadGenInput(String idNumber, String monitorType) {
		return ((BigDecimal) this.session.getNamedNativeQuery("checkBlackListWatchListLeadGenInput")
				.setParameter("monitorType", monitorType).setParameter("idNumber", idNumber.trim()).getSingleResult())
						.intValue();
	}
	
	public Integer checkBlackListCitizenId(String idNumber) {
		return ((BigDecimal) this.session.getNamedNativeQuery("checkBlackListCitizenId")
				.setParameter("idNumber", idNumber.trim()).getSingleResult())
						.intValue();
	}

	public Long findProductIdByProductCode(String productCode) {

		@SuppressWarnings("rawtypes")
		Query query = this.session.getNamedNativeQuery("findProductIdByProductCode").setParameter("productCode",
				productCode);
		BigDecimal result = (BigDecimal) query.uniqueResult();

		return result != null ? result.longValue() : 0L;
	}

	public UploadDataId findUploadMasterIdByUploadCode(String uploadCode) {

		@SuppressWarnings("rawtypes")
		Query query = this.session.getNamedNativeQuery("findUploadDataIdByUploadCode").setParameter("uploadCode",
				uploadCode);

		Object result = query.uniqueResult();

		return result != null ? transformObject(result, UploadDataId.class) : null;
	}

	public int increaseImportedUplMaster(Long numOfIncre, Long uplMasterId) {
		return this.session.getNamedQuery("increaseImportedUplMaster").setParameter("numOfIncre", numOfIncre, LongType.INSTANCE).setParameter("uplMasterId", uplMasterId)
				.executeUpdate();
	}

	public int increaseImportedUplDetail(Long numOfIncre, Long uplDetailId) {
		return this.session.getNamedQuery("increaseImportedUplDetail").setParameter("numOfIncre", numOfIncre, LongType.INSTANCE).setParameter("uplDetailId", uplDetailId)
				.executeUpdate();
	}

	public Integer checkDupCurrentData(Long UplDetailId) {
		return ((BigDecimal) this.session.getNamedNativeQuery("checkDupCurrentData")
				.setParameter("uplDetailId", UplDetailId).getSingleResult()).intValue();
	}

	public Integer checkInvalidData(Long UplDetailId) {
		return ((BigDecimal) this.session.getNamedNativeQuery("checkInvalidData")
				.setParameter("uplDetailId", UplDetailId).getSingleResult()).intValue();
	}

	public void deleteUplCustomerByUplDetail(Long UplDetailId) {
		this.session.getNamedQuery("deleteUplCustomerByUplDetailId1").setParameter("uplDetailId", UplDetailId)
				.executeUpdate();
	}

	public void deleteInvalidCustomerByUplDetailId(Long UplDetailId) {
		this.session.getNamedQuery("deleteInvalidCustomerByUplDetailId").setParameter("uplDetailId", UplDetailId)
				.executeUpdate();
	}

	public void updateImportedAndAllocatedUplMaster(Long uplDetailId) {

		this.session.getNamedNativeQuery("updateImportedAndAllocatedUplDetail").setParameter("uplDetailId", uplDetailId)
				.executeUpdate();

		this.session.getNamedNativeQuery("updateImportedAndAllocatedUplMaster").setParameter("uplDetailId", uplDetailId)
				.executeUpdate();
	}

	public List<UplCustomer> getListCustumerTeamLeadId(Long relatedAllocation, Long userId) {
		String stquery = "SELECT ID, UPL_DETAIL_ID, CUSTOMER_NAME, BIRTH_DATE, MOBILE, IDENTITY_NUMBER, ADDRESS, INCOME, NOTE, RESPONSE_CODE, MESSAGE "
				+ "FROM UPL_CUSTOMER um WHERE um.UPL_DETAIL_ID = (SELECT ad.UPL_DETAIL_ID FROM ALLOCATION_DETAIL ad  WHERE ad.ALLOCATION_MASTER_ID =:relatedAllocation and  "
				+ "ad.ASSIGNEE_ID = ( SELECT tm.TEAM_ID FROM TEAM_MEMBER tm WHERE tm.USER_ID =:userId )) "
				+ "And um.ID not in(SELECT ad1.UPL_CUSTOMER_ID FROM ALLOCATION_DETAIL ad1 WHERE ad1.OBJECT_TYPE = 'U')  and um.RESPONSE_CODE = 'OK' ";

		return this.session.createNativeQuery(stquery, UplCustomer.class)
				.setParameter("relatedAllocation", relatedAllocation).setParameter("userId", userId).getResultList();
	}

	public UploadCustomerDTO findResponseCodeUplCustomer(String fromSource, String phoneNumber) {
		// with AMO, check uplCustomer existed within 90 days
		String sql_Exist_Within_90_days = "";
		if(AmoEnums.AMO.value().equals(fromSource) || LeadGenEnums.PARTNER_TRUST_CONNECT.value().equals(fromSource)) {
			sql_Exist_Within_90_days = " and (trunc(SYSDATE) - trunc(uc.last_updated_date)) <= 90 ";
		} 
				
		String sql = " (select uc.ID as id, uc.UPL_DETAIL_ID as uplDetailId, uc.RESPONSE_CODE as responseCode from UPL_MASTER um "
				+ " inner join UPL_DETAIL   ud on um.ID = ud.UPL_MASTER_ID "
				+ " inner join UPL_CUSTOMER uc on ud.ID = uc.UPL_DETAIL_ID " + " where um.UPL_FORMAT = '.json' "
				+ " and um.FROM_SOURCE = (SELECT ID FROM CODE_TABLE WHERE CATEGORY = 'UPL_SRC' AND CODE_VALUE1 = '"
				+ fromSource + "')" + " and uc.RESPONSE_CODE in ('" + LeadGenEnums.OK.value() + "', '"
				+ LeadGenEnums.DEDUP.value() + "', '" + LeadGenEnums.DEDUP_1.value() + "', '" + LeadGenEnums.DEDUP_2.value() + "') and uc.MOBILE = :phoneNumber "
				+ sql_Exist_Within_90_days + ") "
				+ " union all "
				+ " (select uc.ID as id, uc.UPL_DETAIL_ID as uplDetailId, uc.RESPONSE_CODE as responseCode from UPL_MASTER um "
				+ " inner join UPL_DETAIL   ud on um.ID = ud.UPL_MASTER_ID "
				+ " inner join UPL_CUSTOMER uc on ud.ID = uc.UDF08 " + " where um.UPL_FORMAT = '.json' "
				+ " and um.FROM_SOURCE = (SELECT ID FROM CODE_TABLE WHERE CATEGORY = 'UPL_SRC' AND CODE_VALUE1 = '"
				+ fromSource + "')" + " and uc.RESPONSE_CODE in ('" + LeadGenEnums.APPROVING.value() + "') and uc.MOBILE = :phoneNumber "
				+ sql_Exist_Within_90_days + ") ";

		Query<?> query = this.session.createNativeQuery(sql).setParameter("phoneNumber", phoneNumber.trim());

		List<?> lst = query.list();

		if (lst != null && !lst.isEmpty()) {
			return transformObject(lst.get(0), UploadCustomerDTO.class);
		}

		return null;
	}

	public UploadCustomerDTO findResponseCodeUplCustomerByRefId(String fromSource, String refId) {
		String sql = " (select uc.ID as id, uc.UPL_DETAIL_ID as uplDetailId, uc.RESPONSE_CODE as responseCode from UPL_MASTER um "
				+ " inner join UPL_DETAIL   ud on um.ID = ud.UPL_MASTER_ID "
				+ " inner join UPL_CUSTOMER uc on ud.ID = uc.UPL_DETAIL_ID " + " where um.UPL_FORMAT = '.json' "
				+ " and um.FROM_SOURCE = (SELECT ID FROM CODE_TABLE WHERE CATEGORY = 'UPL_SRC' AND CODE_VALUE1 = '"
				+ fromSource + "')" + " and uc.RESPONSE_CODE in ('" + LeadGenEnums.OK.value() + "', '"
				+ LeadGenEnums.DEDUP.value() + "', '" + LeadGenEnums.DEDUP_1.value() + "', '" + LeadGenEnums.DEDUP_2.value() + "') and uc.REF_ID = :refId) "
				+ " union all "
				+ " (select uc.ID as id, uc.UPL_DETAIL_ID as uplDetailId, uc.RESPONSE_CODE as responseCode from UPL_MASTER um "
				+ " inner join UPL_DETAIL   ud on um.ID = ud.UPL_MASTER_ID "
				+ " inner join UPL_CUSTOMER uc on ud.ID = uc.UDF08 " + " where um.UPL_FORMAT = '.json' "
				+ " and um.FROM_SOURCE = (SELECT ID FROM CODE_TABLE WHERE CATEGORY = 'UPL_SRC' AND CODE_VALUE1 = '"
				+ fromSource + "')" + " and uc.RESPONSE_CODE in ('" + LeadGenEnums.APPROVING.value() + "') and uc.REF_ID = :refId) ";

		Query<?> query = this.session.createNativeQuery(sql).setParameter("refId", refId.trim());

		List<?> lst = query.list();

		if (lst != null && !lst.isEmpty()) {
			return transformObject(lst.get(0), UploadCustomerDTO.class);
		}

		return null;
	}

	public boolean checkExistsDataVersusNTBSystem(String phoneNumber) {

		String sql = " select uc.ID " + " from UPL_MASTER um "
				+ " inner join UPL_DETAIL   ud on um.ID = ud.UPL_MASTER_ID "
				+ " inner join UPL_CUSTOMER uc on ud.ID = uc.UPL_DETAIL_ID "
				+ " where um.FROM_SOURCE = (select ID from CODE_TABLE where CATEGORY = 'UPL_SRC' and CODE_VALUE1 = 'TELESALE') and uc.MOBILE = :phoneNumber "
				+ " and (trunc(SYSDATE) - trunc(uc.last_updated_date)) <= 90 ";
		Query<?> query = this.session.createNativeQuery(sql).setParameter("phoneNumber", phoneNumber.trim());

		List<?> lst = query.list();

		return (lst != null && !lst.isEmpty());
	}
	
	public boolean checkExistsDataInNTBSystem(String phoneNumber) {
		// Kiem tra du lieu cua nhom cac doi tac thuoc NewToBank toi tai trong vong 90 ngay gan nhat
		String sql = " select uc.ID " + " from UPL_MASTER um "
				+ " inner join UPL_DETAIL   ud on um.ID = ud.UPL_MASTER_ID "
				+ " inner join UPL_CUSTOMER uc on ud.ID = uc.UPL_DETAIL_ID "
				+ " where um.FROM_SOURCE in (select map_id1 from mapping_hierarchy where map_type = 'MAP_TELESALE_TS') and  uc.MOBILE = :phoneNumber "
				+ " and (trunc(SYSDATE) - trunc(uc.last_updated_date)) <= 90 ";
		Query<?> query = this.session.createNativeQuery(sql).setParameter("phoneNumber", phoneNumber.trim());

		List<?> lst = query.list();

		return (lst != null && !lst.isEmpty());
	}

	public Object checkCustomerNTB(Long idUplCus) {
		Integer iResult = 0;
		List<?> lstResult = this.session.getNamedQuery("checkCustomerNTB").setParameter("p_id_upl_cus", idUplCus)
				.list();
		return lstResult.get(0);
	}
	
	public UplCustomer mergeObject(UplCustomer item) {
        return (UplCustomer) this.session.merge("UplCustomer", item);
    }
	
	public List<UplCustomer> getListDataWaitForApprove() {
		return this.session.getNamedQuery("getListDataWaitForApprove").setParameter("responseCode", LeadGenEnums.APPROVING.value()).getResultList();
	}
	
	public int changeUplDetailStatusToPartialAllocated(Long uplDetailId) {
		return this.session.getNamedQuery("changeUplDetailStatus").setParameter("status", "P").setParameter("uplDetailId", uplDetailId)
				.executeUpdate();
	}
	
	public ScoreLGResult getScoreLG(String mobilePhone, String productCode) {
		String sql = " select uc.score_range, uc.last_updated_date " + 
				" from UPL_CUSTOMER uc " + 
				" inner join UPL_DETAIL ud on uc.UPL_DETAIL_ID = ud.ID " + 
				" inner join UPL_MASTER um on ud.UPL_MASTER_ID = um.ID " + 
				" inner join code_table ct on um.from_source = ct.ID " + 
				" inner join products p on uc.product_id = p.id " + 
				" where ct.code_value1 = 'LEAD_GEN' " + 
				" and ct.category = 'UPL_SRC' " + 
				" and uc.response_code = 'OK' " + 
				" and uc.mobile = :mobilePhone " + 
				" and p.product_code = :productCode " + 
				" order by uc.id desc ";

		Query<?> query = this.session.createNativeQuery(sql)
				.setParameter("mobilePhone", mobilePhone)
				.setParameter("productCode", productCode);

		List<?> lst = query.list();

		if (lst != null && !lst.isEmpty()) {
			return transformObject(lst.get(0), ScoreLGResult.class);
		}

		return null;
	}
	
	public Object checkRenew(Long idUplCus) {
		Integer iResult = 0;
		List<?> lstResult = this.session.getNamedQuery("checkRenew").setParameter("p_id_upl_cus", idUplCus)
				.list();
		return lstResult.get(0);
	}
	
	public void deleteInvalidAllocationDetail(Long UplDetailId) {
		this.session.getNamedQuery("deleteInvalidAllocationDetail").setParameter("uplDetailId", UplDetailId)
				.executeUpdate();
	}
	
	public void deleteAllocationMaster(List<Long> lstId) {
		this.session.getNamedQuery("deleteAllocationMaster").setParameterList("lstId", lstId)
				.executeUpdate();
	}

	public void deleteInvalidAllocationMaster(Long UplDetailId) {
		this.session.getNamedQuery("deleteInvalidAllocationMaster").setParameter("uplDetailId", UplDetailId)
				.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<Long> findListByDetailId(Long uplDetailId) {
		List<Long> lst = this.session.getNamedNativeQuery("findListByDetailId").setParameter("uplDetailId", uplDetailId).getResultList();
		return (CollectionUtils.isEmpty(lst)) ? new ArrayList<>() : lst;
	}

}
