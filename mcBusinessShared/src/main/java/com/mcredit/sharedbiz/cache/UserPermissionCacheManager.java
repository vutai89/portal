package com.mcredit.sharedbiz.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;

import com.mcredit.common.UnitOfWorkHelper;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.factory.UnitOfWorkFactory;
import com.mcredit.model.dto.MenuFunctionDTO;
import com.mcredit.model.dto.RoleDTO;
import com.mcredit.model.dto.ServicePermissionDTO;
import com.mcredit.model.enums.BooleanType;
import com.mcredit.model.enums.MenuObjectType;
import com.mcredit.model.enums.MenuType;
import com.mcredit.model.enums.SessionType;
import com.mcredit.model.object.MenuFunction;
import com.mcredit.model.object.ServicePermission;
import com.mcredit.model.object.TreeMenu;
import com.mcredit.model.object.UserRole;
import com.mcredit.sharedbiz.dto.TreeMenuDTO;
import com.mcredit.sharedbiz.util.MenuUtils;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class UserPermissionCacheManager implements IDataCache {

	private ModelMapper modelMapper = new ModelMapper();
	private List<TreeMenuDTO> menuCache;
	private Map<String, List<MenuFunctionDTO>> userMenu;
	private Map<String, List<TreeMenuDTO>> treeMenu;
	private Map<String, List<ServicePermissionDTO>> servicePerm;
	private Map<String, List<TreeMenuDTO>> treeMenuByUsers;
	private Map<String, List<RoleDTO>> roleByUsers;
	private UnitOfWork uok = null;
	
	private static UserPermissionCacheManager instance;

	private UserPermissionCacheManager() {
		initCache();
	}
	
	public static synchronized UserPermissionCacheManager getInstance() {
		if (instance == null) {
			synchronized (UserPermissionCacheManager.class) {
				if (null == instance) {
					instance = new UserPermissionCacheManager();
				}
			}
		}
		return instance;
	}

	public TreeMenuDTO getMenuById(Integer id) {
		if (menuCache == null) {
			initCache();
		}

		TreeMenuDTO out = new TreeMenuDTO();

		if (!StringUtils.isNullOrEmpty(id.toString())) {
			for (TreeMenuDTO item : menuCache) {
				if (id.equals(item.getMenuId()))
					out = item;
			}
		}

		return out;
	}
	
	
	public List<TreeMenuDTO> getUserTreeMenu(String loginId, boolean reLoadMenu) throws Exception {
		if( StringUtils.isNullOrEmpty(loginId) )
			return null;
		
		List<TreeMenuDTO> um = treeMenu.get(loginId);
		
		if( reLoadMenu ) {
			um = findUserTreeMenu(loginId, reLoadMenu);
			treeMenu.put(loginId, um);
		}else {
			if(um == null) {
				um = findUserTreeMenu(loginId, false);
				treeMenu.put(loginId, um);
			}
		}
		
		return um;
	}

	private List<TreeMenuDTO> findUserTreeMenu(String loginId, boolean reloadMenu) throws Exception {
		
		List<MenuFunctionDTO> userFunction = getUserFunction(loginId, reloadMenu);
		
		List<TreeMenuDTO> tm = new ArrayList<TreeMenuDTO>();
		tm.add(new TreeMenuDTO(null, "", "", "", "", 1, "ROOT", "Root Menu of Application", "ROOT", "R", 1, new ArrayList<TreeMenuDTO>()));
		
		boolean isFound = false;
		
		if (menuCache == null)
			initCache();
		
		for(MenuFunctionDTO m : userFunction) {
			isFound = false;
			for(TreeMenuDTO t : tm) {
				if(t.getMenuId().equals(m.getParentId())) {
					isFound = true;
					break;
				}
			}
			if(!isFound) {
				// Add to list
				addMenu(tm, m.getParentId());
			}
		}
		
		return tm;
	}
	
	private void addMenu(List<TreeMenuDTO> tmdList, int id) {
		boolean isFound = false;
		for(TreeMenu m : menuCache) {
			if(id == m.getMenuId()) {
				isFound = false;
				if(MenuType.INTERMEDIATE.value().equals(m.getMenuType())) {
					for(TreeMenuDTO tmd : tmdList) {
						if(tmd.getMenuId() == m.getParentId()) {
							isFound = true;
							break;
						}
					}
					if(!isFound) {
						addMenu(tmdList, m.getParentId());
					}
				}
				tmdList.add(modelMapper.map(m, TreeMenuDTO.class));
			}
		}
	}
	
	public List<MenuFunctionDTO> getUserFunction(String loginId, boolean reLoadMenu) throws Exception {
		List<MenuFunctionDTO> userFunction = userMenu.get(loginId);
		
		if( reLoadMenu ) {
			userFunction = findUserFunction(loginId);
			userMenu.put(loginId, userFunction);
		}else {
			if(userFunction == null) {
				userFunction = findUserFunction(loginId);
				userMenu.put(loginId, userFunction);
			}
		}
		
		return userFunction;
	}

	private List<MenuFunctionDTO> findUserFunction(String loginId) throws Exception {
		
		UnitOfWork _uok = new UnitOfWork();
		List<MenuFunction> functions = UnitOfWorkHelper.tryCatch(_uok, ()->{
			return _uok.user.usersRepo().findUserMenu(loginId);
		});
		
		if( functions==null || functions.size()==0 )
			return new ArrayList<MenuFunctionDTO>();
		
		List<MenuFunctionDTO> funcDTO = new ArrayList<MenuFunctionDTO>();
		int menuId = 0;
		int funcId = 0;
		int i = 0;
		String accessRight = "";
		String objType = "";
		for(MenuFunction mf : functions) {
			// Assign initial value
			if(menuId == 0) {
				menuId = mf.getMenuId();
			}
			if(funcId == 0) {
				funcId = mf.getFunctionId();
			}
			if("".equals(accessRight)) {
				accessRight = mf.getAccessRight();
			}
			if("".equals(objType)) {
				objType = mf.getObjectType();
			}
			// Compare to last step value
			if(funcId == mf.getFunctionId()) {
				// Last object type is Role and current object type is Role too -> union access right
				if(objType.equals(mf.getObjectType()) &&
						MenuObjectType.ROLE.value().equals(objType)) {
					accessRight = or2String(accessRight, mf.getAccessRight());
				} else {
					accessRight = mf.getAccessRight();
				}
			} else {
				// Get new function -> add last function to user menu
				if(MenuObjectType.FUNCTION.value().equals(objType)) {
					funcDTO.add(modelMapper.map(functions.get(i-1), MenuFunctionDTO.class));
				} else {
					funcDTO.add(modelMapper.map(functions.get(i-1), MenuFunctionDTO.class));
					funcDTO.get(funcDTO.size()-1).setAccessRight(accessRight);
				}
				accessRight = mf.getAccessRight();
			}
			// Assign latest value
			menuId = mf.getMenuId();
			funcId = mf.getFunctionId();
			objType = mf.getObjectType();
			i++;
		}
		// Add last function to user menu
		if(MenuObjectType.FUNCTION.value().equals(objType)) {
			funcDTO.add(modelMapper.map(functions.get(i-1), MenuFunctionDTO.class));
		} else {
			funcDTO.add(modelMapper.map(functions.get(i-1), MenuFunctionDTO.class));
			funcDTO.get(funcDTO.size()-1).setAccessRight(accessRight);
		}
		
		return funcDTO;
	}
	
	public List<TreeMenuDTO> findUserTreeMenuByUser(String loginId) throws Exception{
		if(!this.treeMenuByUsers.containsKey(loginId))
			this.treeMenuByUsers.put(loginId, this.buildUserTreeMenu(loginId, false));
		
		return this.treeMenuByUsers.get(loginId);
	}
	
	public List<RoleDTO> findRoleByUser(String loginId) throws Exception {
		return this.roleByUsers.get(loginId);
	}

	private String or2String(String str1, String str2) {
		String ret = "";
		int len = (str1.length() > str2.length()) ? str2.length() : str1.length();
		for(int i=0;i<len;i++) {
			if("Y".equals(str1.substring(i, i+1)) ||
					"Y".equals(str2.substring(i, i+1))) {
				ret = ret + "Y";
			} else {
				ret = ret + "N";
			}
		}
		return ret;
	}

	public List<ServicePermissionDTO> getUserService(String loginId) throws Exception {
		List<ServicePermissionDTO> userService = servicePerm.get(loginId);
		if(userService == null) {
			userService = findUserService(loginId);
			servicePerm.put(loginId, userService);
		}
		return userService;
	}

	private List<ServicePermissionDTO> findUserService(String loginId) throws Exception {
		
		UnitOfWork uok = new UnitOfWork();
		List<ServicePermission> services = UnitOfWorkHelper.tryCatch(uok, ()->{
			return uok.user.usersRepo().findUserService(loginId);
		});
		
		List<ServicePermissionDTO> servDTO = new ArrayList<ServicePermissionDTO>();
		int serviceId = 0;
		int i = 0;
		String accessRight = "";
		String objType = "";
		for(ServicePermission sp : services) {
			// Assign initial value
			if(serviceId == 0) {
				serviceId = sp.getServiceId();
			}
			if("".equals(accessRight)) {
				accessRight = sp.getAccessRight();
			}
			if("".equals(objType)) {
				objType = sp.getObjectType();
			}
			// Compare to last step value
			if(serviceId == sp.getServiceId()) {
				// Last object type is Role and current object type is Role too -> union access right
				if(objType.equals(sp.getObjectType()) &&
						MenuObjectType.ROLE.value().equals(objType)) {
					if(BooleanType.YES.value().equals(sp.getAccessRight())) {
						accessRight = BooleanType.YES.value();
					}
				} else {
					accessRight = sp.getAccessRight();
				}
			} else {
				// Get new function -> add last function to user menu
				if(MenuObjectType.SERVICE.value().equals(objType)) {
					servDTO.add(modelMapper.map(services.get(i-1), ServicePermissionDTO.class));
				} else {
					servDTO.add(modelMapper.map(services.get(i-1), ServicePermissionDTO.class));
					servDTO.get(servDTO.size()-1).setAccessRight(accessRight);
				}
				accessRight = sp.getAccessRight();
			}
			// Assign latest value
			serviceId = sp.getServiceId();
			objType = sp.getObjectType();
			i++;
		}
		// Add last function to user menu
		if(MenuObjectType.SERVICE.value().equals(objType)) {
			servDTO.add(modelMapper.map(services.get(i-1), ServicePermissionDTO.class));
		} else {
			if(i > 0){
				servDTO.add(modelMapper.map(services.get(i-1), ServicePermissionDTO.class));
				servDTO.get(servDTO.size()-1).setAccessRight(accessRight);
			}
		}

		return servDTO;
	}
	
	@Override
	public void initCache() {
		try {
			
			/* Xu ly moi, refresh cache cho phan quyen MENU */
			if( this.treeMenuByUsers==null || this.treeMenuByUsers.isEmpty() )
				this.treeMenuByUsers = new Hashtable<String, List<TreeMenuDTO>>();
			else {
				Iterator<?> it = this.treeMenuByUsers.entrySet().iterator();
			    while (it.hasNext()) {
			        @SuppressWarnings("rawtypes")
					Map.Entry pair = (Map.Entry) it.next();
			        this.treeMenuByUsers.put(pair.getKey().toString(), this.buildUserTreeMenu(pair.getKey().toString(), true));
			    }
			}
			/* -END- */
			
			uok = UnitOfWorkFactory.getInstance(SessionType.JTA);
			List<UserRole> roles = UnitOfWorkHelper.tryCatch(uok, ()->{
				return uok.user.usersRepo().getUserRoles();
			});			
			
			this.roleByUsers = new HashMap<String, List<RoleDTO>>();
			String login = "";
			List<RoleDTO> ur = null;
			for (UserRole r : roles) {
				if(!login.equals(r.getLoginId())) {
					if(!"".equals(login)) {
						this.roleByUsers.put(login, ur);
					}
					ur = new ArrayList<RoleDTO>();
					ur.add(new RoleDTO(r.getRoleCode()));
				} else {
					ur.add(new RoleDTO(r.getRoleCode()));
				}
				login = r.getLoginId();
			}
			// Add last item
			this.roleByUsers.put(login, ur);
			
			if(this.menuCache != null) {
				return;
			}

			uok = UnitOfWorkFactory.getInstance(SessionType.JTA);
			List<TreeMenu> menuCaches = UnitOfWorkHelper.tryCatch(uok, ()->{
				return uok.common.menuRepo().findTreeMenu();
			});			

			this.menuCache = new ArrayList<TreeMenuDTO>();
			for (TreeMenu item : menuCaches) {
				this.menuCache.add(modelMapper.map(item, TreeMenuDTO.class));
			}			

			this.userMenu = new HashMap<String, List<MenuFunctionDTO>>();
			this.treeMenu = new HashMap<String, List<TreeMenuDTO>>();
			this.servicePerm = new HashMap<String, List<ServicePermissionDTO>>();
			//this.treeMenuByUsers = new Hashtable<String, List<TreeMenuDTO>>();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public List<TreeMenuDTO> buildUserTreeMenu(String loginId, boolean reloadMenu) throws Exception {
		if( StringUtils.isNullOrEmpty(loginId) )
			throw new ValidationException("Login ID không được để trống.");
		
		List<TreeMenuDTO> treeLst = this.getUserTreeMenu(loginId, reloadMenu);
		
		if(treeLst == null || treeLst.size() == 0)
			throw new ValidationException("Không tìm thấy Menu cho tài khoản này.");
		
		List<MenuFunctionDTO> leafLst = this.getUserFunction(loginId, reloadMenu);
		
		if( leafLst!=null && leafLst.size()>0 ) {
			
			List<TreeMenuDTO> newTreeLst = new ArrayList<TreeMenuDTO>();
			
			for( TreeMenuDTO treeItem : treeLst ) {
				
				newTreeLst.add(treeItem);
				
				for( MenuFunctionDTO leafItem : leafLst ) {
					
					if( leafItem.getParentId().equals(treeItem.getMenuId())) {
						
						newTreeLst.add(new TreeMenuDTO(leafItem.getParentId(), treeItem.getParentCode(), treeItem.getParentTitle(), treeItem.getParentModule()
								, treeItem.getParentType(), leafItem.getMenuId(), treeItem.getMenuCode(), leafItem.getMenuTitle(), leafItem.getUrl()
								, "L", treeItem.getTreeLevel(), new ArrayList<TreeMenuDTO>()));
					}
				}
			}
			
			return MenuUtils.createRecursiveList(newTreeLst);
		}
		
		return MenuUtils.createRecursiveList(treeLst);
	}

	@Override
	public void refresh() {
		this.initCache();
	}
	
	public void refreshServicePermission() throws Exception {
		/* Refresh servicePermissionCache */
		if (null != this.servicePerm && !this.servicePerm.isEmpty()) {
			for (String key : this.servicePerm.keySet()) {
				this.servicePerm.put(key, this.findUserService(key));
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		
		UserPermissionCacheManager a =  new UserPermissionCacheManager();
		//List<ServicePermissionDTO> f = a.getUserService("dongtd.ho");
		a.initCache();

	}
}
