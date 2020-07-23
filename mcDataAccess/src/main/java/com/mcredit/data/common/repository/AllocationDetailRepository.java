package com.mcredit.data.common.repository;

import java.util.List;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.common.entity.AllocationDetail;
import com.mcredit.data.common.entity.AllocationMaster;
import com.mcredit.data.repository.IRepository;

public class AllocationDetailRepository extends BaseRepository implements IRepository<AllocationDetail> {

	public AllocationDetailRepository(Session session) {
		super(session);
	}
	
	public void add(AllocationDetail item) {
	}

	public void update(AllocationDetail item) {
	}

	public void upsert(AllocationDetail item) {		
		this.session.saveOrUpdate("AllocationDetail", item);
		this.session.flush();
		this.session.clear();
	}
	
	public void merge(AllocationDetail item) {		
		this.session.merge("AllocationDetail", item);
                this.session.flush();
		this.session.clear();
	}

	public void remove(AllocationDetail item) {
		this.session.delete("AllocationDetail", item);
	}
	
	@SuppressWarnings("unchecked")
	public List<AllocationDetail> findAllocationDetail() {
		return this.session.getNamedQuery("findAllocationDetail").list();
	}	
	
	public int insertAllocationDetailAllTeamLead( Integer allocationMasterId,Integer allocatedNumber, Long uplDetailId){
		return this.session.getNamedQuery("insertAllocationDetailAllTeanLead")
				.setParameter("allocatedNumber", allocatedNumber)
				.setParameter("allocationMasterId",allocationMasterId)
				.setParameter("uplDetailId", uplDetailId)
				.executeUpdate();
	}

	public int insertAllocationDetailAllTeamAxcept(Long allocatedNumber, Long uplDetailId, List<Integer> objectId) {
		return this.session.getNamedQuery("insertAllocationDetailAllTeanAxcept")
				.setParameter("allocatedNumber", allocatedNumber)
				.setParameter("uplDetailId", uplDetailId)
				.setParameter("listTeamId",objectId)
				.executeUpdate();
		
	}

	public int insertAllocationDetailAllTeamOwned(Long allocatedNumber, Long uplDetailId,Long allocationMasterId) {
		return this.session.getNamedQuery("insertAllocationDetailAllTeamOwned")
				.setParameter("allocatedNumber", allocatedNumber)
				.setParameter("uplDetailId", uplDetailId)
				.setParameter("allocationMasterId",allocationMasterId)
				.executeUpdate();
	}

	public void updateAllocationDetaiByStore(Long uplMasterId,Integer allocNumber ,Integer allocTo, Long teamId, String listMember) {
		StoredProcedureQuery query = this.session.createStoredProcedureQuery("allocate_To_TSA")
				.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN)
				.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter(4, Long.class, ParameterMode.IN)
				.registerStoredProcedureParameter(5, String.class, ParameterMode.IN)
				.setParameter(1, uplMasterId)
				.setParameter(2, allocNumber)
				.setParameter(3, allocTo)
				.setParameter(4, teamId)
				.setParameter(5, listMember);
		query.execute();
	}

	public AllocationDetail getAllocationDetailByUPLCustomerIdAndObjectId(Long uplCustomerId) {
		Object ojb = this.session.getNamedQuery("getAllocationDetailByUPLCustomerIdAndObjectId").setParameter("uplCustomerId", uplCustomerId).getSingleResult();
		return ojb != null ? (AllocationDetail) ojb : null;
	}
	
	public AllocationDetail getAllocationDetailByUplMsterIdTeamId(Long allocationMasterId, Long teamId) {
		Object ojb = this.session.getNamedQuery("getAllocationDetailByallocationMasterIdTeamId").setParameter("allocationMasterId", allocationMasterId).setParameter("teamId", teamId).getSingleResult();
		return ojb != null ? (AllocationDetail) ojb : null;
	}

	public AllocationMaster getAllocationDetailByUplMsterIdTeamId(Long uplMasterId, String allocatedType) {
		
		Object ojb = this.session.getNamedQuery("findAllocationMasterByuplMasterIdAllocatedType").setParameter("uplMasterId", uplMasterId).setParameter("allocatedType", allocatedType).getSingleResult();
		return ojb != null ? (AllocationMaster) ojb : null;
	}

	@SuppressWarnings("unchecked")
	public List<AllocationDetail> findAllocationDetailByUserFresh(Long oldObjectId) {
		return this.session.getNamedQuery("findAllocationDetailByUserFresh").setParameter("oldObjectId", oldObjectId).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<AllocationDetail> findAllocationDetailByUserWip(Long oldObjectId) {
		return this.session.getNamedQuery("findAllocationDetailByUserWip").setParameter("oldObjectId", oldObjectId).getResultList();
	}
	
	public AllocationDetail getAllocationDetailTelesale(Long uplCustomerId) {
		Object obj = this.session.getNamedNativeQuery("getAllocationDetailTelesale").setParameter("uplCustomerId", uplCustomerId).uniqueResult();
		return obj != null ? transformObject(obj, AllocationDetail.class) : null;
	}


	public AllocationDetail findByuplCustomerId(Long documentId ) {
		AllocationDetail ad = null ;
		try {
			//ad = this.session.createQuery("from AllocationDetail where uplDetailId = 0 and uplCustomerId= :documentId  ", AllocationDetail.class).setParameter("documentId", documentId).getSingleResult();
			ad = transformObject( this.session.getNamedNativeQuery("findWhId").setParameter("documentId", documentId).getSingleResult(), AllocationDetail.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ad ;
	}
	
	@SuppressWarnings("unchecked")
	public List<AllocationDetail> getListWhId(List<Long> lstwhDocId) {
		List<AllocationDetail> ad = null;
		try {
			ad = transformList(this.session.getNamedNativeQuery("getListWhId").setParameter("lstwhDocId", lstwhDocId) .getResultList(), AllocationDetail.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ad;
	}

	public AllocationDetail checkPemisionData(Long objectId, Long userId) {
		try {
			return (AllocationDetail) this.session.getNamedQuery("checkPemisionData").setParameter("objectId", objectId).setParameter("userId", userId).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public Integer getWhUplDetailId() {
		Object  obj =  this.session.getNamedNativeQuery("getWareHouseUplDetailId").uniqueResult();
		if(obj != null){
			return Integer.valueOf(obj.toString());
		}
		return null;
	}
}
