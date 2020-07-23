package com.mcredit.service.assign;

import java.net.MalformedURLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.business.ap.manager.AssignPermissionsManager;
import com.mcredit.model.dto.assign.ChangeUserStatusDTO;
import com.mcredit.model.dto.assign.FunctionRolePermission;
import com.mcredit.model.dto.assign.ResponseSetPermission;
import com.mcredit.model.dto.assign.ResultPermissionFromFile;
import com.mcredit.model.dto.assign.RoleServiceIdsDTO;
import com.mcredit.model.dto.assign.SearchUserDTO;
import com.mcredit.model.dto.assign.SearchUserTsaDTO;
import com.mcredit.model.dto.assign.UserListDTO;
import com.mcredit.model.dto.assign.UserPermission;
import com.mcredit.model.dto.assign.UserPermissionFromFile;
import com.mcredit.model.dto.assign.UserSearchDTO;
import com.mcredit.model.enums.ServiceName;
import com.mcredit.service.BasedService;

@Path("/v1.0/assign")
public class AssignPermissionService extends BasedService {

	public AssignPermissionService(@Context HttpHeaders headers) {
		super(headers);
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
	@GET
	@Path("/user-roles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserRoles(@QueryParam("loginIds") String loginId) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_AP_GET_USER_ROLES, () -> {
			try (AssignPermissionsManager manager = new AssignPermissionsManager()) {
				return ok(manager.getUserRole(loginId));
			}
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
	@GET
	@Path("/all-roles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllRoles() throws Exception {
		return this.authorize(ServiceName.GET_V1_0_AP_GET_ALL_ROLES, () -> {
			try (AssignPermissionsManager manager = new AssignPermissionsManager()) {
				return ok(manager.getAllRoles());
			}
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
	@POST
	@Path("/permissions")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setPermission(List<UserPermission> userPermissions) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_AP_POST_SET_PERMISSIONS, () -> {
			try (AssignPermissionsManager manager = new AssignPermissionsManager()) {
				ResponseSetPermission response = manager.setPermission(userPermissions);
				return ok(response);
			}
		});
	}
	
	
	@POST
	@Path("/permissions-from-file")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response setPermissionFromFile(List<UserPermissionFromFile> userPermissions) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_AP_POST_SET_PERMISSIONS, () -> {
			try (AssignPermissionsManager manager = new AssignPermissionsManager()) {
				ResultPermissionFromFile response = manager.setPermissionFromFile(userPermissions);
				return ok(response);
			}
		});
	}

	/**
	 * Service lay tat ca menu cua he thong
	 * 
	 * @author linhtt2.ho
	 * @param
	 * @return return tat ca nhung menu cua he thong
	 * @throws Exception
	 */
	@GET
	@Path("/all-menus")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllMenus() throws Exception {
		return this.authorize(ServiceName.GET_V1_0_AP_GET_ALL_MENUS, () -> {
			try (AssignPermissionsManager manager = new AssignPermissionsManager()) {
				return ok(manager.getAllMenus());
			}
		});
	}

	/**
	 * Service lay tat ca menu cua role
	 * 
	 * @author linhtt2.ho
	 * @param
	 * @return return tat ca nhung menu va role tuong ung
	 * @throws Exception
	 */
	@GET
	@Path("/role-menus")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserMenu(@QueryParam("roleIds") String roleId) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_AP_GET_ROLE_MENUS, () -> {
			try (AssignPermissionsManager manager = new AssignPermissionsManager()) {
				return ok(manager.getRoleMenus(roleId));
			}
		});
	}
	
	/**
	 * Service lay tat ca function cua he thong
	 * 
	 * @author kienvt.ho
	 * @return tat ca Function trong he thong
	 */
	@GET
	@Path("/all-functions")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllFunction() throws Exception {
		return this.authorize(ServiceName.GET_V1_0_AP_GET_ALL_FUNCTIONS, () -> {
			try (AssignPermissionsManager manager = new AssignPermissionsManager()) {
				return ok(manager.getAllFunctions());
			}
		});
	}
	
	/**
	 * Service lay function theo RoleID
	 * 
	 * @author kienvt.ho
	 * @return List cac function tuong ung voi Role
	 */
	@GET
	@Path("/role-functions")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserFunctions(@QueryParam("roleIds") String roleId) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_AP_GET_ROLE_FUNCTIONS, () -> {
			try (AssignPermissionsManager manager = new AssignPermissionsManager()) {
				return ok(manager.getRoleFunctions(roleId));
			}
		});
	}

	/**
	 * Service thay doi menu cua role
	 * 
	 * @author linhtt2.ho
	 * @param
	 * @return return danh sach role thanh cong va that bai
	 * @throws Exception
	 */
	@POST
	@Path("/menus-role")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setMenusRole(List<FunctionRolePermission> menuRole) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_AP_POST_SET_MENUS_ROLE, () -> {
			try (AssignPermissionsManager manager = new AssignPermissionsManager()) {
				ResponseSetPermission response = manager.setMenusRole(menuRole);
				return ok(response);
			}
		});
	}

	/**
	 * Service get danh sach service cua role
	 * 
	 * @author hoanx.ho
	 * @param roleId:
	 *            role can lay thong tin service
	 * 
	 * @return return danh sach service tuong ung cua role
	 * @throws Exception
	 */
	@GET
	@Path("/roles-services")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRolesServices(@QueryParam("roleId") Long roleId) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_MANAGE_PERMISION_ROLES_SERVICES, () -> {
			try (AssignPermissionsManager manager = new AssignPermissionsManager()) {
				return ok(manager.getRoleServices(roleId));
			}
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
	@GET
	@Path("/services")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getServices() throws Exception {
		return this.authorize(ServiceName.GET_V1_0_MANAGE_PERMISION_SERVICES, () -> {
			try (AssignPermissionsManager manager = new AssignPermissionsManager()) {
				return ok(manager.getServices());
			}
		});
	}

	/**
	 * Service thay doi quyen cua role den service
	 * 
	 * @author hoanx.ho
	 * @param List<RoleServiceIdsDTO>:
	 *            danh sach cau hinh role cung voi cac service moi tuong ung voi
	 *            role
	 * 
	 * @return return danh sach role duoc thay doi thanh cong va that bai
	 * @throws Exception
	 */
	@PUT
	@Path("roles-permisions")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setRolePermision(List<RoleServiceIdsDTO> rolesPermisions) throws Exception {
		return this.authorize(ServiceName.SET_V1_0_MANAGE_PERMISION_ROLES_SERVICES, () -> {
			try (AssignPermissionsManager manager = new AssignPermissionsManager()) {
				return ok(manager.setRolePermissions(rolesPermisions));
			}
		});
	}

	/**
	 * Service lay tat ca cac lenh de refresh cache
	 * 
	 * @author linhtt2.ho
	 * @param
	 * @return return tat ca cac lenh de refresh cache
	 * @throws Exception
	 */
	@GET
	@Path("/cache-tags")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCacheTags() throws Exception {
		return this.authorize(ServiceName.GET_V1_0_BA_GET_CACHE_TAGS, () -> {
			try (AssignPermissionsManager manager = new AssignPermissionsManager()) {
				return ok(manager.getCacheTags());
			}
		});
	}

	/**
	 * Service thuc hien refreshCache
	 * 
	 * @author linhtt2.ho
	 * @param
	 * @return return ket qua refreshCache
	 * @throws Exception
	 */
	@GET
	@Path("/refresh-cache")
	@Produces(MediaType.APPLICATION_JSON)
	public Response refreshCache(@QueryParam("cache") String cache) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_BA_REFRESH_CACHE, () -> {
			try (AssignPermissionsManager manager = new AssignPermissionsManager()) {
				return ok(manager.refreshCache(cache));
			}
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
	@GET
	@Path("/search-users")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchUsers(@QueryParam("keyword") String keyword, @QueryParam("pageNumber") Integer pageNumber,
			@QueryParam("pageSize") Integer pageSize) throws Exception {
//		return this.authorize(ServiceName.GET_V1_0_MANAGE_PERMISION_SEARCH_USERS, () -> {
			try (AssignPermissionsManager manager = new AssignPermissionsManager()) {
				SearchUserDTO searchDTO = new SearchUserDTO(keyword, pageNumber, pageSize);
				return ok(manager.searchUsers(searchDTO));
			}
//		});
	}

	/**
	 * Service thay doi trang thai user
	 * 
	 * @author hoanx.ho
	 * @param
	 * @return return number 1 = success
	 * @throws Exception
	 */
	@PUT
	@Path("/user-status")
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeUserStatus(ChangeUserStatusDTO changeUserStatusDTO) throws Exception {
		return this.authorize(ServiceName.PUT_V1_0_MANAGE_PERMISION_CHANGE_STATUS, () -> {
			try (AssignPermissionsManager manager = new AssignPermissionsManager()) {
				return ok(manager.changeStatusUser(changeUserStatusDTO.getUserId(), changeUserStatusDTO.getActive()));
			}
		});
	}
	
	/**
	 * Service Update thong tin user
	 * 
	 * @author kienvt.ho
	 * @return gia tri 1 neu thanh cong
	 */
	@PUT
	@Path("/update-user")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(UserSearchDTO userSearchDTO) throws Exception {
//		return this.authorize(ServiceName.PUT_V1_0_MANAGE_PERMISION_UPDATE_USER, () -> {
			try (AssignPermissionsManager manager = new AssignPermissionsManager()) {
				return ok(manager.updateUser(userSearchDTO, this.currentUser.getLoginId()));
			}
//		});
	}
	
	/**
	 * Service Them user
	 * 
	 * @author kienvt.ho
	 * @return gia tri 1 neu thanh cong
	 */
	@POST
	@Path("/add-user")
	@Produces(MediaType.APPLICATION_JSON)
	public Response insertUser(UserSearchDTO userSearchDTO) throws Exception {
//		return this.authorize(ServiceName.PUT_V1_0_MANAGE_PERMISION_ADD_USER, () -> {
			try (AssignPermissionsManager manager = new AssignPermissionsManager()) {
				return ok(manager.insertUser(userSearchDTO, this.currentUser.getLoginId()));
			}
//		});
	}
	
	/**
	 * Service Them list user
	 * 
	 * @author kienvt.ho
	 * @return gia tri 1 neu thanh cong
	 */
	@POST
	@Path("/add-user-list")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response insertUser(List<UserListDTO> userlist) throws Exception {
//		return this.authorize(ServiceName.PUT_V1_0_MANAGE_PERMISION_ADD_USER, () -> {
			try (AssignPermissionsManager manager = new AssignPermissionsManager()) {
				return ok(manager.insertUserList(userlist, this.currentUser.getLoginId()));
			}
//		});
	}
	
	/**
	 * Service Them list external user mapping
	 * 
	 * @author kienvt.ho
	 * @return list
	 */
	@GET
	@Path("/get-external-user-mapping-list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getExternalUserMappingList() throws Exception {
//		return this.authorize(ServiceName.PUT_V1_0_MANAGE_PERMISION_ADD_USER, () -> {
			try (AssignPermissionsManager manager = new AssignPermissionsManager()) {
				return ok(manager.getExternalUserMappingList());
			}
//		});
	}

	@GET
	@Path("/testGetData")
	@Produces("application/json")
	public Response getData(@Context HttpServletRequest request) throws MalformedURLException {
		List<String> hosts = headers.getRequestHeader("Host");
		return ok(hosts);
	}
	
}
