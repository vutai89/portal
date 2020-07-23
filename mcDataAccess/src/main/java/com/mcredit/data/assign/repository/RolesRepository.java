package com.mcredit.data.assign.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Session;
import org.hibernate.type.LongType;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.repository.IRepository;
import com.mcredit.model.dto.assign.MenuDTO;
import com.mcredit.model.dto.assign.RoleDTO;

public class RolesRepository extends BaseRepository implements IRepository<RoleDTO>{
	public RolesRepository(Session session) {
		super(session);
	}

	public List<RoleDTO> findRolesForUser(String loginId) {
		List<RoleDTO> results = new ArrayList<>();
		List lst = this.session.getNamedNativeQuery("findRolesForUser").setParameter("login_id", loginId).list();
		if (lst != null && !lst.isEmpty()) {
			results = transformList(lst, RoleDTO.class);
		}
		return results;
	}

	public List<RoleDTO> findAllRoles() {
		List<RoleDTO> results = new ArrayList<>();
		List lst = this.session.getNamedNativeQuery("findAllRoles").list();
		if (lst != null && !lst.isEmpty()) {
			results = transformList(lst, RoleDTO.class);
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	public Set<Long> findRolesById(Long userId) {
		List<Long> lst = this.session.getNamedNativeQuery("findRolesForUserId").setParameter("userId", userId).addScalar("object_id",LongType.INSTANCE).getResultList();
		if (lst != null && !lst.isEmpty()) {
			return new TreeSet<Long>(lst);
		}
		return null;
	}

	public int insertRolesId(Long userId, Long roleId) {
		return this.session.getNamedNativeQuery("insertRoleUserId")
				.setParameter("userId", userId)
				.setParameter("roleId", roleId)
				.executeUpdate();
	}

	public int deleteRolesId(Long userId, Long roleId) {
		return this.session.getNamedNativeQuery("deleteRoleUserId")
				.setParameter("userId", userId)
				.setParameter("roleId", roleId)
				.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<RoleDTO> findRolesByRoleId(Long[] roles) {
		List<RoleDTO> result = new ArrayList<>();
		List lst = this.session.getNamedNativeQuery("findRolesByRoleIds")
				.setParameterList("roleIds", roles)
				.list();
		if (null != lst && lst.size() > 0) {
			result = transformList(lst, RoleDTO.class);
		}
		return result;
	}
}
