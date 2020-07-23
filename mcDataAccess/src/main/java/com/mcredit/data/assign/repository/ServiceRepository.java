package com.mcredit.data.assign.repository;

import java.util.ArrayList;
import java.util.List;


import org.hibernate.Session;
import org.hibernate.type.LongType;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.repository.IRepository;
import com.mcredit.model.dto.assign.RoleDTO;
import com.mcredit.model.dto.assign.ServiceDTO;

public class ServiceRepository extends BaseRepository implements IRepository<ServiceDTO> {

	public ServiceRepository(Session session) {
		super(session);
	}
	

	public RoleDTO getRoleById(Long roleId) {
		Object result = this.session.getNamedNativeQuery("getRoleById").setParameter("role_id", roleId).getSingleResult();
		if (result != null) {
			return transformObject(result, RoleDTO.class);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<ServiceDTO> getServices() {
		List<ServiceDTO> lst = this.session.getNamedNativeQuery("getServices").getResultList();
		if (lst != null && !lst.isEmpty()) {
			return transformList(lst, ServiceDTO.class);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<ServiceDTO> getServicesByRoleId(Long roleId) {
		List<Long> lst = this.session.getNamedNativeQuery("getServicesByRoleId").setParameter("role_id", roleId).getResultList();
		if (lst != null && !lst.isEmpty()) {
			return transformList(lst, ServiceDTO.class);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Long> getServiceIdsByRoleId(Long roleId) {
		List<Long> lst = this.session.getNamedNativeQuery("getServiceIdsByRoleId").setParameter("role_id", roleId).addScalar("OBJECT_ID",LongType.INSTANCE).getResultList();
		if (lst != null && !lst.isEmpty()) {
			return lst;
		}
		return new ArrayList<Long>();
	}
			
	public int insertRoleService(Long roleId, Long serviceId) {
		return this.session.getNamedNativeQuery("insertRoleService")
				.setParameter("role_id", roleId)
				.setParameter("service_id", serviceId)
				.executeUpdate();
	}
	
	public int removeRoleService(Long roleId, Long serviceId) {
		return this.session.getNamedNativeQuery("removeRoleService")
				.setParameter("role_id", roleId)
				.setParameter("service_id", serviceId)
				.executeUpdate();
	}
	
}
