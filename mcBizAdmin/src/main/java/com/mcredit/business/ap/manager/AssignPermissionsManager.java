package com.mcredit.business.ap.manager;

import java.util.ArrayList;
import java.util.List;

import validation.AssignPermissionValidation;

import com.mcredit.business.ap.aggregate.AssignPermissionsAggregate;
import com.mcredit.model.dto.ResponseSuccess;
import com.mcredit.model.dto.assign.ExternalDTO;
import com.mcredit.model.dto.assign.FunctionDTO;
import com.mcredit.model.dto.assign.FunctionRolePermission;
import com.mcredit.model.dto.assign.ManageUsersDTO;
import com.mcredit.model.dto.assign.ManageUsersTsaDTO;
import com.mcredit.model.dto.assign.MenuDTO;
import com.mcredit.model.dto.assign.ResponseSetPermission;
import com.mcredit.model.dto.assign.ResultInsertUserList;
import com.mcredit.model.dto.assign.ResultPermissionFromFile;
import com.mcredit.model.dto.assign.RoleDTO;
import com.mcredit.model.dto.assign.RoleFunctionsDTO;
import com.mcredit.model.dto.assign.RoleMenusDTO;
import com.mcredit.model.dto.assign.RoleServiceIdsDTO;
import com.mcredit.model.dto.assign.RoleServicesDTO;
import com.mcredit.model.dto.assign.SearchUserDTO;
import com.mcredit.model.dto.assign.SearchUserTsaDTO;
import com.mcredit.model.dto.assign.ServiceDTO;
import com.mcredit.model.dto.assign.UserListDTO;
import com.mcredit.model.dto.assign.UserPermission;
import com.mcredit.model.dto.assign.UserPermissionFromFile;
import com.mcredit.model.dto.assign.UserRolesDTO;
import com.mcredit.model.dto.assign.UserSearchDTO;
import com.mcredit.model.dto.assign.UsersDTO;
import com.mcredit.model.enums.CacheTag;
import com.mcredit.model.object.ADUser;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.manager.BaseManager;

public class AssignPermissionsManager extends BaseManager {

	private UserDTO _user;
	private AssignPermissionsAggregate _agg;
	private AssignPermissionValidation _validate = new AssignPermissionValidation();
	
	public AssignPermissionsManager() {
		_agg = new AssignPermissionsAggregate(this.uok, this._user);
	}

	/**
	 * Service lay quyen (role) cua nguoi dung
	 * 
	 * @author linhtt2.ho
	 * @param loginId:
	 *            ten dang nhap cua nguoi dung
	 * @return return thong tin nguoi dung va danh sach quyen cua nguoi dung tuong
	 *         ung
	 * @throws Exception
	 */
	public List<UserRolesDTO> getUserRole(String loginId) throws Exception {

		return this.tryCatch(() -> {
			return _agg.getUserRole(loginId);
		});
	}

	/**
	 * Service lay tat ca quyen cua he thong
	 * 
	 * @author linhtt2.ho
	 * @param
	 * @return return tat ca nhung quyen cua he thong
	 * @throws Exception
	 */
	public List<RoleDTO> getAllRoles() throws Exception {
		return this.tryCatch(() -> {
			return _agg.getAllRoles();
		});
	}

	/**
	 * Service set lai quyen cua user
	 * 
	 * @author linhtt2.ho
	 * @param List<UserPermission>:
	 *            Danh sach thong tin va quyen moi cua tung user
	 * @return return danh sach user duoc thay doi quyen dung va danh sach user
	 *         khong duoc thay doi
	 * @throws Exception
	 */
	public ResponseSetPermission setPermission(List<UserPermission> userPermissions) throws Exception {
		ResponseSetPermission response = this.tryCatch(() -> {
			_validate.validateUserPermission(userPermissions);
			return _agg.setPermission(userPermissions);
		});

		return response;
	}
	
	public ResultPermissionFromFile setPermissionFromFile(List<UserPermissionFromFile> userPermissions) throws Exception {
		return this.tryCatch(() -> {
			return _agg.setPermissionFromList(userPermissions);
		});
	}

	/**
	 * Service get danh sach menu cua he thong
	 * 
	 * @author linhtt2.ho
	 * @param 
	 * 
	 * @return return danh sach menu hien tai cua he thong
	 * @throws Exception
	 */
	public List<MenuDTO> getAllMenus() throws Exception {
		return this.tryCatch(() -> {
			return _agg.getAllMenus();
		});
	}

	/**
	 * Service get danh sach menu cua role
	 * 
	 * @author linhtt2.ho
	 * @param roleId: role can lay thong tin menu
	 * 
	 * @return return danh sach menu hien tai cua role tuong ung
	 * @throws Exception
	 */
	public List<RoleMenusDTO> getRoleMenus(String roleId) throws Exception {
		return this.tryCatch(() -> {
			_validate.validateGetRoleMenus(roleId);
			return _agg.getRoleMenus(roleId);
		});
	}
	
	/**
	 */
	public List<FunctionDTO> getAllFunctions() throws Exception {
		return this.tryCatch(() -> {
			return _agg.getAllFunctions();
		});
	}
	
	/**
	 */
	public List<RoleFunctionsDTO> getRoleFunctions(String roleId) throws Exception {
		return this.tryCatch(() -> {
//			_validate.validateGetRoleMenus(roleId);
			return _agg.getRoleFunctions(roleId);
		});
	}
	
	/**
	 * Service get danh sach service cua role
	 * 
	 * @author hoanx.ho
	 * @param roleId: role can lay thong tin service
	 * 
	 * @return return danh sach service tuong ung cua role
	 * @throws Exception
	 */
	public List<RoleServicesDTO> getRoleServices(Long roleId) throws Exception {
		return this.tryCatch(() -> {
			return _agg.getRoleServices(roleId);
		});
	}
	
	/**
	 * Service get danh sach service hien tai cua he thong
	 * 
	 * @author hoanx.ho
	 * @param 
	 * 
	 * @return return danh sach service hien tai cua he thong
	 * @throws Exception
	 */
	public List<ServiceDTO> getServices() throws Exception {
		return this.tryCatch(() -> {
			return _agg.getServices();
		});
	}
	
	/**
	 * Service thay doi quyen cua role den service
	 * 
	 * @author hoanx.ho
	 * @param List<RoleServiceIdsDTO>: danh sach cau hinh role cung voi cac service moi tuong ung voi role
	 * 
	 * @return return danh sach role duoc thay doi thanh cong va that bai
	 * @throws Exception
	 */
	public ResponseSetPermission setRolePermissions(List<RoleServiceIdsDTO> rolesPermisions) throws Exception {
		ResponseSetPermission response = this.tryCatch(() -> {
			_validate.validateSetRolesServices(rolesPermisions);
			return _agg.setRolePermissions(rolesPermisions);
		});
		_agg.sendRefreshNoti(CacheTag.USER_PERMISSION.value());
		return response;
	}

	/**
	 * Service thay doi quyen cua role den menu. 
	 * 
	 * @author linhtt2.ho
	 * @param List<FunctionRolePermission>: danh sach cau hinh role cung voi cac menu (function) moi tuong ung voi role
	 * 
	 * @return return danh sach role duoc thay doi thanh cong va that bai
	 * @throws Exception
	 */
	public ResponseSetPermission setMenusRole(List<FunctionRolePermission> menuRole) throws Exception {
		ResponseSetPermission response = this.tryCatch(() -> {
			_validate.validateSetMenuRole(menuRole);
			return _agg.setMenusRole(menuRole);
		});
		
		_agg.sendRefreshNoti(CacheTag.USER_PERMISSION.value());

		return response;
	}
	
	/**
	 * Service lay cac lenh refresh cache hien tai 
	 * 
	 * @author linhtt2.ho
	 * @param 
	 * 
	 * @return return danh sach cac lenh refresh cache
	 * @throws Exception
	 */
	public List<String> getCacheTags() throws Exception {
		return this.tryCatch(() -> {
			return _agg.getCacheTags();
		});
	}

	/**
	 * Service thuc hien refresh cache tuong ung
	 * 
	 * @author linhtt2.ho
	 * @param cache: ten cache can duoc refresh
	 * 
	 * @return return ket qua refresh thanh cong hoac that bai
	 * @throws Exception
	 */
	public ResponseSuccess refreshCache(String cache) throws Exception {
		return this.tryCatch(() -> {
			return _agg.sendRefreshNoti(cache);
		});
	}
	
	/**
	 * Service tim kiem va hien thi users
	 * 
	 * @author hoanx.ho
	 * @param
	 * @return return users
	 * @throws Exception
	 */
	public ManageUsersDTO searchUsers(SearchUserDTO searchDTO) throws Exception {
		return this.tryCatch(() -> {
			_validate.validateSearchCase(searchDTO);
			return _agg.searchUsers(searchDTO);
		});
	}
	
	/**
	 * Service thay doi trang thai user
	 * 
	 * @author hoanx.ho
	 * @param
	 * @return return number 1 = success
	 * @throws Exception
	 */
	public int changeStatusUser(Long userId, boolean isActive) throws Exception {
		return this.tryCatch(() -> {
			_validate.validateChangeStatus(userId, isActive);
			return _agg.changeStatusUser(userId, isActive);
		});
	}
	
	/**
	 */
	public int updateUser(UserSearchDTO userSearchDTO, String loginId) throws Exception {
		return this.tryCatch(() -> {
//			_validate.validateChangeStatus(userId, isActive);
			return _agg.updateUser(userSearchDTO, loginId);
		});
	}
	
	/**
	 */
	public int insertUser(UserSearchDTO userSearchDTO, String loginId) throws Exception {
		return this.tryCatch(() -> {
//			_validate.validateInsertUser(userSearchDTO);
			return _agg.insertUser(userSearchDTO, loginId);
		});
	}
	
	/**
	 */
	public ResultInsertUserList insertUserList(List<UserListDTO> lstUser, String loginId) throws Exception {
		return this.tryCatch(() -> {
			_validate.validateInsertUserList(lstUser);
			return _agg.insertUserList(lstUser, loginId);
		});
	}
	
	public List<ExternalDTO> getExternalUserMappingList() throws Exception {
		return this.tryCatch(() -> {
			return _agg.getExternalUserMappingList();
		});
	}
	
	public ManageUsersTsaDTO searchUsersTsa(SearchUserTsaDTO searchUserTsaDTO) throws Exception {
		return this.tryCatch(()->{
			return _agg.searchUsersTsa(searchUserTsaDTO);
		});
	}
}




