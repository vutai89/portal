package com.mcredit.data.common.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Session;
import org.hibernate.type.LongType;

import com.mcredit.common.DataUtils;
import com.mcredit.data.BaseRepository;
import com.mcredit.data.common.entity.Menu;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.model.dto.assign.FunctionDTO;
import com.mcredit.model.dto.assign.MenuDTO;
import com.mcredit.model.object.Sale;
import com.mcredit.model.object.TreeMenu;

public class MenuRepository extends BaseRepository implements IUpsertRepository<Menu> {

	public MenuRepository(Session session) {
		super(session);
	}

	public void upsert(Menu item) {
		this.session.saveOrUpdate("Menu", item);
	}

	public void remove(Menu item) {
		this.session.delete("Menu", item);
	}

	public List<TreeMenu> findTreeMenu() {
		List<Object[]> objList = this.session.getNamedNativeQuery("findTreeMenu").getResultList();
		List<TreeMenu> retList = new ArrayList<TreeMenu>();
		for (Object[] objects : objList) {
			TreeMenu tm = new TreeMenu();
			tm = (TreeMenu) DataUtils.bindingData(tm, objects);
			retList.add(tm);
		}
		return retList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<MenuDTO> getLeafMenus() {
		List<MenuDTO> result = new ArrayList<>();
		List lst = this.session.getNamedNativeQuery("getLeafMenus").list();
		if (null != lst && lst.size() > 0)
			result =  transformList(lst, MenuDTO.class);
		
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<MenuDTO> findMenuForRole(Long id) {
		List<MenuDTO> result = new ArrayList<>();
		List lst = this.session.getNamedNativeQuery("getMenuForRole")
				.setParameter("roleId", id)
				.list();
		if (null != lst && lst.size() > 0) {
			result = transformList(lst, MenuDTO.class);
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<FunctionDTO> findFunctionForRole(Long id) {
		List<FunctionDTO> result = new ArrayList<>();
		List lst = this.session.getNamedNativeQuery("getFunctionForRole")
				.setParameter("roleId", id)
				.list();
		if (null != lst && lst.size() > 0) {
			result = transformList(lst, FunctionDTO.class);
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<FunctionDTO> getAllFunction() {
		List<FunctionDTO> result = new ArrayList<>();
		List lst = this.session.getNamedNativeQuery("getAllFunction").list();
		if (null != lst && lst.size() > 0)
			result =  transformList(lst, FunctionDTO.class);
		
		return result;
	}

	@SuppressWarnings("unchecked")
	public Set<Long> getFunctionIdsForRole(Long roleId) {
		List<Long> lst = this.session.getNamedNativeQuery("getFunctionIdsForRole")
				.setParameter("roleId", roleId)
				.addScalar("object_id",LongType.INSTANCE)
				.getResultList();
		if (lst != null && !lst.isEmpty()) {
			return new TreeSet<Long>(lst);
		}
		return null;
	}

	public int insertFunctionId(Long roleId, Long functionId) {
		return this.session.getNamedNativeQuery("insertFunctionRoleId")
				.setParameter("roleId", roleId)
				.setParameter("functionId", functionId)
				.executeUpdate();
	}

	public int deleteFunctionId(Long roleId, Long functionId) {
		return this.session.getNamedNativeQuery("deleteFunctionRoleId")
				.setParameter("roleId", roleId)
				.setParameter("functionId", functionId)
				.executeUpdate();
		
	}

}
