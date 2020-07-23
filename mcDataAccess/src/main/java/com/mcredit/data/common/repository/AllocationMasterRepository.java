package com.mcredit.data.common.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.common.entity.AllocationMaster;
import com.mcredit.data.repository.IRepository;
import com.mcredit.data.telesale.entity.UplMaster;

public class AllocationMasterRepository extends BaseRepository implements IRepository<AllocationMaster> {

	public AllocationMasterRepository(Session session) {
		super(session);
	}
	
	public void add(AllocationMaster item) {
	}

	public void update(AllocationMaster item) {
	}

	public void upsert(AllocationMaster item) {		
		this.session.saveOrUpdate("AllocationMaster", item);
		this.session.flush();
		this.session.clear();
	}

	public void remove(AllocationMaster item) {
		this.session.delete("AllocationMaster", item);
	}
	
	@SuppressWarnings("unchecked")
	public List<AllocationMaster> findAllocationMasterByuplMasterIdAllocatedType(Long uplMasterId ,String allocatedType) {
		List<AllocationMaster> lAllocationMasters = new ArrayList<>();
		 List<?> lst =  this.session.getNamedQuery("findAllocationMasterByuplMasterIdAllocatedType").setParameter("uplMasterId", uplMasterId).setParameter("allocatedType", allocatedType).getResultList();
		 if(lst != null && lst.size() > 0 ){
			 for (Object object : lst) {
				 AllocationMaster aMaster = new AllocationMaster();
				 aMaster = (AllocationMaster) object;
				 lAllocationMasters.add(aMaster);
			}
		 }
		 
		 return lAllocationMasters;
	}
	public BigDecimal buidAllocatedSeq(Long uplMasterId ,String allocatedType) {		
		List<Object> result = this.session.getNamedNativeQuery("buidAllocatedSeq").setParameter("uplMasterId", uplMasterId).setParameter("allocatedType", allocatedType).list();
		if(result != null ){			
			return  (BigDecimal) result.get(0);
		}
		return null;
	}

	public List<?> queryUploadMaster(String uplCode, String fromSource, String uplType, String uplSequence, String uplOwner) {

		String strQuery = "SELECT ulm.UPL_CODE as Campaign_Code, ct.DESCRIPTION1 as Campaign_Type ,  us.USR_FULL_NAME, us1.USR_FULL_NAME as TEAMLEAD_NAME, uld.ALLOCATED_NUMBER,"
				+ " (SELECT count(id) from ALLOCATION_DETAIL  where OBJECT_TYPE = 'U' and ASSIGNEE_ID = us.MANAGER_ID ) as Total_Unallocated , ulm.OWNER_ID , tm.TEAM_ID ,alm.ID as ALLOCATION_MASTER_ID "
				+ "FROM UPL_MASTER  ulm "
				+ "inner join CODE_TABLE ct on ct.ID = ulm.UPL_TYPE "
				+ "inner join USERS us on us.ID = ulm.OWNER_ID " 
				+ "inner join USERS us1 on us1.ID = us.MANAGER_ID "
				+ "inner join TEAM_MEMBER tm on tm.USER_ID = ulm.OWNER_ID "
				+ "inner join ALLOCATION_MASTER alm on alm.UPL_MASTER_ID = ulm.ID "
				+ "inner join ALLOCATION_DETAIL uld on uld.ALLOCATION_MASTER_ID = alm.ID "
				+ "WHERE ulm.UPL_CODE = '" + uplCode + "' and ulm.FROM_SOURCE = ( SELECT ID FROM CODE_TABLE Where CODE_VALUE1 = '" + fromSource + "') " 
				+ "and uld.OBJECT_TYPE = 'T' ";

		if (uplType!= null && !"".equals(uplType.trim()))
			strQuery = strQuery + "  AND ulm.uplType = " + uplType;

		/*
		  if (!StringUtils.isNullOrEmpty(uplSequence))
		   strQuery = strQuery + " AND uplSequence = " + uplSequence;
		 */

		if (uplOwner!= null && !"".equals(uplOwner.trim()))
			strQuery = strQuery + "AND ulm.ownerId = " + uplOwner;

		List<?> ojb =  this.session.createNativeQuery(strQuery).list();

		return ojb;
	}
	
	public UplMaster queryAllocationDetail(String uplCode, Long teamId , String step ,Long uplMaterId) {
		NativeQuery<?> query = null ;
		String strQuery = " select um.*, u.login_id  OWNER_LOGIN_ID,u.usr_full_name  OWNER_NAME,t.id  TEAM_ID,t.team_name TEAM_NAME, "
				+ "  ul.id TEAM_LEAD_ID,ul.login_id  TEAM_LEAD_LOGIN_ID,ul.usr_full_name  TEAM_LEAD_NAME ,recv.case_received  -  nvl(alloc.total_allocated,0) as total_unallocated , "
				+ "  ct1.DESCRIPTION1 ,recv.upl_master_id  as allocation_MasterId ,recv.case_received as case_received ,nvl(alloc.total_allocated,0) as TOTAL_ALLOCATEDS "
				+ "  from UPL_MASTER um left join  (select am.UPL_MASTER_ID, sum(ad.ALLOCATED_NUMBER) CASE_RECEIVED "
				+ "  from ALLOCATION_MASTER am inner join ALLOCATION_DETAIL ad  on am.ID = ad.ALLOCATION_MASTER_ID where ad.OBJECT_TYPE = 'T' "
				+ "  and ad.ASSIGNEE_ID = :teamId "
				+ "  and am.ALLOCATED_TYPE = 'S' group by am.UPL_MASTER_ID) recv on um.ID = recv.UPL_MASTER_ID "
				+ "  left join (select ateam.UPL_MASTER_ID, sum(ateam.ALLOCATED_NUMBER) TOTAL_ALLOCATED from ALLOCATION_MASTER am "
				+ "  inner join ALLOCATION_MASTER ateam on ateam.RELATED_ALLOCATION = am.ID "
				+ "  where am.ALLOCATED_TYPE = 'S' and ateam.ALLOCATED_TYPE = 'T' "
				+ "  and ateam.ASSIGNER_ID = :teamId1 "
				+ "  group by ateam.UPL_MASTER_ID) alloc on um.ID = alloc.UPL_MASTER_ID "
				//+ " left join CODE_TABLE ut on ut.ID = um.UPL_TYPE "
				+ "  left join USERS u on u.ID = um.OWNER_ID left join TEAM_MEMBER tm on (tm.USER_ID = u.ID and tm.STATUS = 'A') "
				+ "  left join TEAMS t on t.ID = tm.TEAM_ID left join USERS ul on ul.ID = t.MANAGER_ID "
				+ "  left join CODE_TABLE ct1 on ct1.ID = um.UPL_TYPE " ;
		
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

	public Integer countAlocatedByTeamUPLCode(String uplCode, Long teamId ){
		String strQuery = " SELECT  SUM (ad.ALLOCATED_NUMBER) as total  FROM  ALLOCATION_DETAIL ad WHERE ad.ASSIGNEE_ID in (SELECT t.USER_id FROM TEAM_MEMBER t WHERE t.TEAM_ID =:teamId ) "
				+ "and ad.OBJECT_TYPE = 'U' and UPL_DETAIL_ID in (SELECT ud.ID FROM UPL_DETAIL ud  INNER JOIN UPL_MASTER um on ud.UPL_MASTER_ID = um.ID  WHERE um.UPL_CODE =:uplCode ) " ;
		
		NativeQuery<?> query = this.session.createNativeQuery(strQuery).setParameter("teamId", teamId).setParameter("uplCode", uplCode);
		
		Object result  = query.getSingleResult();
			
		return result != null ? ((BigDecimal) result).intValue() :  0 ;
		
	}

	public void deleteForWh(Long allocationMasterId) {
		this.session.createNativeQuery("UPDATE ALLOCATION_MASTER SET RECORD_STATUS = 'C' WHERE ID=:allocationMasterId").setParameter("allocationMasterId", allocationMasterId).executeUpdate();
		
	}

	@SuppressWarnings("unchecked")
	public List<Object> getuploadConfig() {
		List<Object> output = new ArrayList<>();
		List<Object> result = this.session.createNativeQuery(" select ID from UPL_MASTER where UPL_CODE = 'WH_ALLOCATION' UNION select ID from UPL_DETAIL where UPL_MASTER_ID = (select ID from UPL_MASTER where UPL_CODE = 'WH_ALLOCATION') ").getResultList();
		
		if(result != null && result.size() > 0 ){
			 for (Object object : result) {
				 output.add(object != null ?((BigDecimal)object).longValue()  : -1l);
			}
		 }
		 
		 return output;
	}
	
}
