package com.mcredit.business.ap.aggregate;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jboss.jandex.ThrowsTypeTarget;
import org.modelmapper.ModelMapper;

import com.mcredit.business.ap.convert.Converter;
import com.mcredit.business.ap.factory.AssignPermissionsFactory;
import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.mobile.entity.ExternalUserMapping;
import com.mcredit.data.mobile.entity.UsersProfiles;
import com.mcredit.data.user.entity.Employee;
import com.mcredit.data.user.entity.Users;
import com.mcredit.model.dto.CodeTableDTO;
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
import com.mcredit.model.dto.assign.UserSearchTsaDTO;
import com.mcredit.model.enums.CacheTag;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.object.ADUser;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.service.LDAPService;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.DateTimeFormat;
import com.mcredit.util.DateUtil;
import com.mcredit.util.StringUtils;


public class AssignPermissionsAggregate {

	private UnitOfWork _uok = null;
	private KafkaProducer<String, String> _producer;
	private LDAPService LDAPservice = null;
	private String adAdminUsername = CacheManager.Parameters().findParamValueAsString(ParametersName.AD_ADMIN_USERNAME);
	private String adAdminPassword = CacheManager.Parameters().findParamValueAsString(ParametersName.AD_ADMIN_PASSWORD);
	private String accountSubFix = "@mcredit.local";

	public AssignPermissionsAggregate(UnitOfWork _uok, UserDTO _user) {
		this._uok = _uok;
		this._producer = AssignPermissionsFactory.initKafkaProducer();
		;
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
		List<UserRolesDTO> lstRes = new ArrayList<>();

		// check user info
		if (StringUtils.isNullOrEmpty(loginId)) {
			throw new ValidationException(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.post.cancelCase.loginId")));
		}

		// get list user
		String[] lstLogin = loginId.split(",");
		List<Users> lstUser = this._uok.user.usersRepo().findUserByLoginId(lstLogin);
		if (null == lstUser) {
			throw new ValidationException(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.post.cancelCase.loginId")));
		}
		for (Users user : lstUser) {
			UserRolesDTO res = new UserRolesDTO();

			// get roles of user
			List<RoleDTO> lst = new ArrayList<>();
			lst = this._uok.user.rolesRepo().findRolesForUser(user.getLoginId());
			res = Converter.convert(user, lst);

			// add to list
			lstRes.add(res);
		}

		return lstRes;
	}

	/**
	 * Service lay tat ca quyen cua he thong
	 * 
	 * @author linhtt2.ho
	 * @param
	 * @return return tat ca nhung quyen cua he thong
	 * @throws Exception
	 */
	public List<RoleDTO> getAllRoles() {
		return this._uok.user.rolesRepo().findAllRoles();
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
	@SuppressWarnings("unused")
	public ResponseSetPermission setPermission(List<UserPermission> userPermissions) throws Exception {
		ResponseSetPermission response = new ResponseSetPermission();
		List<Long> successIds = new ArrayList<>();
		List<Long> errorIds = new ArrayList<>();
		for (UserPermission user : userPermissions) {
			try {
				// get list roles for users
				Set<Long> lstCurRoles = this._uok.user.rolesRepo().findRolesById(user.getUserId());
				if (null == lstCurRoles) { // user dang khong co quyen nao
					// them moi quyen
					if (null != user.getRoleIds() && user.getRoleIds().size() > 0) {
						for (Long id : user.getRoleIds()) {
							this._uok.user.rolesRepo().insertRolesId(user.getUserId(), id);
						}
					}
					successIds.add(user.getUserId());
				} else { // user dang co quyen
					Set<Long> retainRoles = new HashSet<Long>(lstCurRoles);
					if (null != user.getRoleIds() && user.getRoleIds().size() > 0) {
						Set<Long> newRoles = new HashSet<Long>(user.getRoleIds());
						retainRoles.retainAll(newRoles);
						lstCurRoles.removeAll(retainRoles);
						newRoles.removeAll(retainRoles);
						for (Long id : newRoles) { // insert roles moi
							this._uok.user.rolesRepo().insertRolesId(user.getUserId(), id);
						}
					}
					for (Long id : lstCurRoles) { // delete roles cu
						this._uok.user.rolesRepo().deleteRolesId(user.getUserId(), id);
					}
					successIds.add(user.getUserId());
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				errorIds.add(user.getUserId());
			}
		}
		response.setSuccessIds(successIds);
		response.setErrorIds(errorIds);
		return response;
	}

	/**
	 * Service gui lenh refresh cache len kafka
	 * 
	 * @author linhtt2.ho
	 * @param cache: ten cache de refresh
	 * @return return ket qua gui lenh len kafka thanh cong hoac that bai
	 * @throws Exception
	 */
	public ResponseSuccess sendRefreshNoti(String cache) throws ValidationException {
		try {
			_producer.send(new ProducerRecord<String, String>(
					CacheManager.Parameters().findParamValue(ParametersName.KAFKA_TOPIC_REFRESH_CACHE).toString(),
					BusinessConstant.BIZ_ADMIN_REFRESH_KEY, cache));			
		} catch (Exception ex) {
			throw new ValidationException(Messages.getString("refresh.cache.fail"));
		}

		return new ResponseSuccess();
	}

	/**
	 * Service lay tat ca menu hien tai cua he thong
	 * 
	 * @author linhtt2.ho
	 * @param 
	 * @return return danh sach menu hien tai cua he thong
	 * @throws Exception
	 */
	public List<MenuDTO> getAllMenus() {
		return this._uok.common.menuRepo().getLeafMenus();
	}

	/**
	 * Service lay danh sach role va menu tuong ung voi role
	 * 
	 * @author linhtt2.ho
	 * @param roleIds: danh sach cac role can lay
	 * @return return danh sach role va menu tuong ung voi role
	 * @throws Exception
	 */
	public List<RoleMenusDTO> getRoleMenus(String roleIds) throws ValidationException {
		List<RoleMenusDTO> lstRes = new ArrayList<>();

		// get list roles
		List<RoleDTO> lstRoles = this._uok.user.rolesRepo().findRolesByRoleId(convertLst(roleIds));
		if (null == lstRoles) {
			throw new ValidationException(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.post.cancelCase.loginId")));
		}
		for (RoleDTO role : lstRoles) {
			RoleMenusDTO res = new RoleMenusDTO();

			// get roles of user
			List<MenuDTO> lst = new ArrayList<>();
			lst = this._uok.common.menuRepo().findMenuForRole(role.getId());
			res = Converter.convertMenu(role, lst);

			// add to list
			lstRes.add(res);
		}

		return lstRes;
	}
	
	/**
	 */
	public List<FunctionDTO> getAllFunctions() {
		return this._uok.common.menuRepo().getAllFunction();
	}
	
	/**
	 */
	public List<RoleFunctionsDTO> getRoleFunctions(String roleIds) throws ValidationException {
		List<RoleFunctionsDTO> lstRes = new ArrayList<>();

		// get list roles
		List<RoleDTO> lstRoles = this._uok.user.rolesRepo().findRolesByRoleId(convertLst(roleIds));
		if (null == lstRoles) {
			throw new ValidationException(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.post.cancelCase.loginId")));
		}
		for (RoleDTO role : lstRoles) {
			RoleFunctionsDTO res = new RoleFunctionsDTO();

			// get roles of user
			List<FunctionDTO> lst = new ArrayList<>();
			lst = this._uok.common.menuRepo().findFunctionForRole(role.getId());
			res = Converter.convertFunction(role, lst);

			// add to list
			lstRes.add(res);
		}

		return lstRes;
	}

	/**
	 * Function chuyen doi danh sach role tu String thanh Long
	 * 
	 * @author linhtt2.ho
	 * @param roleIds: danh sach cac role can chuyen doi
	 * @return return danh sach role voi dinh dang Long
	 * @throws Exception
	 */
	private Long[] convertLst(String roleIds) {
		String[] roles = roleIds.split(",");
		Long[] lstRoles = new Long[roles.length];
		for (int i = 0; i < roles.length; i++) {
			lstRoles[i] = Long.valueOf(roles[i]);
		}
		return lstRoles;
	}

	/**
	 * Service thay doi quyen cua role - role co quyen thay cac menu moi
	 * 
	 * @author linhtt2.ho
	 * @param List<FunctionRolePermission> danh sach role va cac menu moi cua role day
	 * @return return danh sach thay doi role thanh cong va that bai
	 * @throws Exception
	 */
	public ResponseSetPermission setMenusRole(List<FunctionRolePermission> functionRoles) throws Exception {
		ResponseSetPermission response = new ResponseSetPermission();
		List<Long> successIds = new ArrayList<>();
		List<Long> errorIds = new ArrayList<>();
		for (FunctionRolePermission role : functionRoles) {
			try {
				// get list menu for roles
				Set<Long> lstCurFunction = this._uok.common.menuRepo().getFunctionIdsForRole(role.getRoleId());
				if (null == lstCurFunction || lstCurFunction.size() == 0) { // role dang khong co menu nao
					if (null != role.getFunctionId() && role.getFunctionId().size() > 0) {
						for (Long functionId : role.getFunctionId()) { // ge danh sach menu
							this._uok.common.menuRepo().insertFunctionId(role.getRoleId(), functionId);
						}
					}
				} else {
					Set<Long> retainFunctions = new HashSet<Long>(lstCurFunction);
					if (null != role.getFunctionId() && role.getFunctionId().size() > 0) {
						Set<Long> newFunctions = new HashSet<Long>(role.getFunctionId());
						retainFunctions.retainAll(newFunctions);
						lstCurFunction.removeAll(retainFunctions);
						newFunctions.removeAll(retainFunctions);
						for (Long functionId : newFunctions) {
							this._uok.common.menuRepo().insertFunctionId(role.getRoleId(), functionId);
						}
					}
					for (Long functionId : lstCurFunction) {
						this._uok.common.menuRepo().deleteFunctionId(role.getRoleId(), functionId);
					}
				}
				successIds.add(role.getRoleId());
			} catch (Exception ex) {
				// add roleId fail
				System.out.println(ex.getMessage());
				errorIds.add(role.getRoleId());
			}
		}
		response.setSuccessIds(successIds);
		response.setErrorIds(errorIds);
		return response;
	}

	/**
	 * Service lay danh sach cac lenh refresh
	 * 
	 * @author linhtt2.ho
	 * @param 
	 * @return return danh sach cac lenh refresh tren he thong
	 * @throws Exception
	 */
	public List<String> getCacheTags() {
		List<String> lstCT = new ArrayList<String>();

		EnumSet.allOf(CacheTag.class).forEach(season -> lstCT.add(season.value()));

		return lstCT;
	}
	
	/**
	 * Service lay danh sach role va cac service tuong ung cua role
	 * 
	 * @author hoanx.ho
	 * @param 
	 * @return return danh sach role va cac service tuong ung cua role
	 * @throws Exception
	 */
	// Manage roles services
	public List<RoleServicesDTO> getRoleServices(Long roleId) {
		
		List<RoleServicesDTO> results = new ArrayList<>();
		
		RoleDTO role = _uok.managePermision.serviceRepository().getRoleById(roleId);
		
		RoleServicesDTO roleService = new RoleServicesDTO(role);
		
		List<ServiceDTO> services = _uok.managePermision.serviceRepository().getServicesByRoleId(role.getId());
		roleService.setServices(services);
		results.add(roleService);
	
		
		return results;
	}
	
	/**
	 * Service lay danh sach service hien co trong he thong
	 * 
	 * @author hoanx.ho
	 * @param 
	 * @return return danh sach service hien co trong he thong
	 * @throws Exception
	 */
	public List<ServiceDTO> getServices() {
		return this._uok.managePermision.serviceRepository().getServices();
	}
	
	/**
	 * Service thay doi role voi cac service tuong ung moi
	 * 
	 * @author hoanx.ho
	 * @param 
	 * @return return danh sach role thay doi thanh cong va that bai
	 * @throws Exception
	 */
	public ResponseSetPermission setRolePermissions(List<RoleServiceIdsDTO> rolesPermisions) {
		
		List<Long> successIds = new ArrayList<>();
		List<Long> errorIds = new ArrayList<>();
		

		for (RoleServiceIdsDTO role: rolesPermisions ) {
			try {
				Long roleId = role.getRoleId();
				
				List<Long> addRoleServices = new ArrayList<>(role.getServiceIds());
				
				List<Long> currentRoleServiceIds =  _uok.managePermision.serviceRepository().getServiceIdsByRoleId(roleId);
				ArrayList<Long> removeRoleServices = new ArrayList<Long>(currentRoleServiceIds);
				removeRoleServices.removeAll(addRoleServices);
				
				addRoleServices.removeAll(currentRoleServiceIds);
				
				for (Long serviceId: addRoleServices) {
					this._uok.managePermision.serviceRepository().insertRoleService(roleId, serviceId);
				}
				
				for (Long serviceId: removeRoleServices) {
					this._uok.managePermision.serviceRepository().removeRoleService(roleId, serviceId);
				}
				
				successIds.add(roleId);
			} catch (Exception e) {
				errorIds.add(role.getRoleId());
			}
			
		}
		
		ResponseSetPermission response = new ResponseSetPermission();
		response.setSuccessIds(successIds);
		response.setErrorIds(errorIds);
		return response;
	}
	
	/**
	 * Service tim kiem va hien thi users
	 * 
	 * @author hoanx.ho
	 * @param
	 * @return return users
	 * @throws Exception
	 */
	public ManageUsersDTO searchUsers(SearchUserDTO searchDTO) {
		ManageUsersDTO response = new ManageUsersDTO();
		response.setTotalCount(10L);
		Long totalUser = this._uok.user.usersRepo().countUsers(searchDTO);
		response.setTotalCount(totalUser);
		
		if(totalUser == 0) {
			// Check LDAP
			LDAPservice = new LDAPService();
			ArrayList<ADUser> LDAPusers = null;
			try {
				LDAPservice.connect(adAdminUsername+accountSubFix, adAdminPassword);
				
				LDAPusers = LDAPservice.getUser("&(sAMAccountName="+ searchDTO.getKeyword() +")");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (LDAPusers.size()>0) {
				response.setAvailableLDAP(true);
			} else {
				response.setAvailableLDAP(false);
			}
			
		}
		
		List<UserSearchDTO> users = this._uok.user.usersRepo().searchUsers(searchDTO);
		response.setData(users);
		return response;
	}
	
	/**
	 * Service thay doi trang thai user
	 * 
	 * @author hoanx.ho
	 * @param
	 * @return return number 1 = success
	 * @throws Exception
	 */
	public int changeStatusUser(Long userId, boolean isActive) {
		return this._uok.user.usersRepo().changeStatusUser(userId, isActive);
	}
	
	/**
	 * @throws ParseException 
	 */
	public int updateUser(UserSearchDTO userSearchDTO, String loginId) throws ParseException {
		Users user = this._uok.user.usersRepo().findUserByLoginId(userSearchDTO.getLoginId());
		
		// Update Employees
		if ( user.getEmpId() != null ) {
			this._uok.user.employeeRepo().updateEmployee(Long.parseLong(user.getEmpId()), userSearchDTO.getHrCode(), userSearchDTO.getEmail(), userSearchDTO.getMobilePhone(), userSearchDTO.getExtPhone());
		} else {
			// New Employee Record.
			Employee employee = new Employee();
			employee.setRecordStatus("A");
			employee.setCreatedDate(new Date());
			employee.setLastUpdatedDate(new Date());
			employee.setCreatedBy(null);
			employee.setLastUpdatedBy(loginId);
			employee.setFullName(userSearchDTO.getUserFullName());
			employee.setEmail(userSearchDTO.getEmail());
			employee.setHrCode(userSearchDTO.getHrCode());
			employee.setMobilePhone(userSearchDTO.getMobilePhone());
			employee.setExtPhone(userSearchDTO.getExtPhone());
			employee.setStatus("A");
			employee.setStaffIdInBPM(null);
			
			this._uok.user.employeeRepo().upsert(employee);
			// Pass EmployeeId to user
			user.setEmpId(employee.getId().toString());
		}
		
		// Update Users
		this._uok.user.usersRepo().updateUser(userSearchDTO, user.getEmpId());
		
		// Update UsersProfiles
		UsersProfiles usersProfiles = new UsersProfiles();
		usersProfiles.setUserId(user.getId());
		usersProfiles.setDeviceName(userSearchDTO.getDeviceName());
		usersProfiles.setExternalUserMappingId(userSearchDTO.getExternalId());
		this._uok.user.usersProfilesRepository().upsert(usersProfiles);
		
		return 1;
	}
	
	/**
	 * @throws ValidationException 
	 * @throws ParseException 
	 */
	public int insertUser(UserSearchDTO userSearchDTO, String loginId) throws ValidationException, ParseException {
		if (this._uok.user.usersRepo().findUserByLoginId(userSearchDTO.getLoginId()) != null) {
			throw new ValidationException(Messages.getString("validation.field.duplicate" , userSearchDTO.getLoginId()));
		};
		
		// New Employee Record.
		Employee employee = new Employee();
		employee.setRecordStatus("A");
		employee.setCreatedDate(new Date());
		employee.setLastUpdatedDate(new Date());
		employee.setCreatedBy(loginId);
		employee.setLastUpdatedBy(loginId);
		employee.setFullName(userSearchDTO.getUserFullName());
		employee.setEmail(userSearchDTO.getEmail());
		employee.setHrCode(userSearchDTO.getHrCode());
		employee.setMobilePhone(userSearchDTO.getMobilePhone());
		employee.setExtPhone(userSearchDTO.getExtPhone());
		employee.setStatus("A");
		employee.setStaffIdInBPM(null);
		
		this._uok.user.employeeRepo().upsert(employee);
		
		// New User Record.
		Users user = new Users();
		user.setUsrFullName(userSearchDTO.getUserFullName());
		user.setRecordStatus(userSearchDTO.getUserStatus());
		user.setLoginId(userSearchDTO.getLoginId());
		user.setCreatedDate(new Date());
		user.setStartEffDate(new Date());
		user.setLastUpdatedBy(loginId);
		user.setEmpId(employee.getId().toString());
		user.setUsrType((userSearchDTO.getUserTypeId() == null) ? "" : userSearchDTO.getUserTypeId().toString() );
		user.setStatus(userSearchDTO.getUserStatus());
		user.setStartEffDate(userSearchDTO.getStartEffDate() == null ? new Date() : DateUtil.toDate(userSearchDTO.getStartEffDate(), "dd/MM/yyyy"));
		user.setEndEffDate(userSearchDTO.getEndEffDate() == null ? DateUtil.toDate("31/12/9999", "dd/MM/yyyy") : DateUtil.toDate(userSearchDTO.getEndEffDate(), "dd/MM/yyyy"));
		
		this._uok.user.usersRepo().upsert(user);
		
		// New Users Profiles
		UsersProfiles usersProfiles = new UsersProfiles();
		usersProfiles.setUserId(user.getId());
		usersProfiles.setDeviceName(userSearchDTO.getDeviceName());
		usersProfiles.setExternalUserMappingId(userSearchDTO.getExternalId());
		this._uok.user.usersProfilesRepository().upsert(usersProfiles);
		
		return 1;
	}
	
	public ResultInsertUserList insertUserList(List<UserListDTO> listUser, String loginId) throws ValidationException, ParseException {
		ResultInsertUserList result = new ResultInsertUserList();
		boolean check = true;
		
		try {
			// Set up LDAP
			LDAPservice = new LDAPService();
			ArrayList<ADUser> LDAPusers = null;
			LDAPservice.connect(adAdminUsername+accountSubFix, adAdminPassword);
			List<String> addedLoginIdLst = new ArrayList<String>();
			
			for (int i = 0; i<listUser.size(); i++) {
				if (listUser.get(i).getLoginId().equals("")) {
					result.getLstError().add("Dòng " + (i+1) + ": Thiếu Login ID");
					check = false;
					continue;
				}
				// Check user exist
				Users user = this._uok.user.usersRepo().findUserByLoginId(listUser.get(i).getLoginId());
				if (user != null) {
					result.getLstError().add("Dòng " + (i+1) + ": User đã tồn tại");
					check = false;
					continue;
				}
				// Check LDAP
				LDAPusers = LDAPservice.getUser("&(sAMAccountName="+ listUser.get(i).getLoginId() +")");
				if (!(LDAPusers.size()>0)) {
					result.getLstError().add("Dòng " + (i+1) + ": Login ID chưa tồn tại trên hệ thống LDAP");
					check = false;
					continue;
				}
				// Check Duplicate LoginId
				if (addedLoginIdLst.contains(listUser.get(i).getLoginId())) {
					result.getLstError().add("Dòng " + (i+1) + ": Login ID bị trùng");
					check = false;
					continue;
				} else {
					addedLoginIdLst.add(listUser.get(i).getLoginId());
				}
				// Check external
				if (!listUser.get(i).getExternal().equals("")) {
					// Check external numeric
					if (!listUser.get(i).getExternal().matches("-?\\d+(\\.\\d+)?")) {
						result.getLstError().add("Dòng " + (i+1) + ": External không đúng format");
						check = false;
						continue;
					}
					// Check external exist
					Long external = Long.parseLong(listUser.get(i).getExternal());
					if (!this._uok.mobile.externalUserMappingRepo().checkExist(external)) {
						result.getLstError().add("Dòng " + (i+1) + ": Nhập mã External không tồn tại");
						check = false;
						continue;
					}
				}
				// Check user type
				if (!listUser.get(i).getUserType().equals("")) {
					if ( CacheManager.CodeTable().getIdByCategoryCodeValue("USER_TYPE", listUser.get(i).getUserType()) == null ) {
						result.getLstError().add("Dòng " + (i+1) + ": Nhập mã User Type không tồn tại");
						check = false;
						continue;
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		if (check) {
			for (UserListDTO uld : listUser) {
				UserSearchDTO user = new UserSearchDTO();
				user.setUserFullName(uld.getFullName());
				user.setLoginId(uld.getLoginId());
				user.setExternalId(uld.getExternal().equals("") ? null : Long.parseLong(uld.getExternal()));
				user.setUserTypeId(uld.getUserType().equals("") ? null : Long.valueOf(CacheManager.CodeTable().getIdByCategoryCodeValue("USER_TYPE", uld.getUserType()).getId()));
				System.out.println("Insert: " + user);
				if (insertUser(user, loginId) == 1) {
					result.setNumbInsertedUser(result.getNumbInsertedUser()+1);
				}
			}
		}
		
		return result;
	}
	
	public List<ExternalDTO> getExternalUserMappingList() throws Exception {
		List<ExternalUserMapping> externalUserMappingList = this._uok.mobile.externalUserMappingRepo().getExternalUserMappingList();
		List<ExternalDTO> lstResult = new ArrayList<ExternalDTO>();
		
		for (ExternalUserMapping ee : externalUserMappingList) {
			lstResult.add(new ExternalDTO(ee.getId().toString(), ee.getUserName()));
		}
		
		return lstResult;
	}
	
	public ResultPermissionFromFile setPermissionFromList(List<UserPermissionFromFile> userPermissionsLst) throws Exception {
		ResultPermissionFromFile result = new ResultPermissionFromFile();
		boolean check = true;
		List<RoleDTO> allRolesLst = this._uok.user.rolesRepo().findAllRoles();
		String loginIdList = "";
		
		for (int i = 0; i<userPermissionsLst.size(); i++) {
			// Check loginId
			if (userPermissionsLst.get(i).getLoginId().equals("")) {
				result.getErrorLst().add("Dòng " + (i+1) + ": Thiếu Login ID");
				check = false;
				continue;
			}
			// Check user exist
			Users user = this._uok.user.usersRepo().findUserByLoginId(userPermissionsLst.get(i).getLoginId());
			if (user == null) {
				result.getErrorLst().add("Dòng " + (i+1) + ": User chưa tồn tại trên hệ thống");
				check = false;
				continue;
			}
			loginIdList = loginIdList + userPermissionsLst.get(i).getLoginId() + ",";
			// Check roles
			// Check roles: Empty
			if (userPermissionsLst.get(i).getRoleIds().trim().equals("")) {
				result.getErrorLst().add("Dòng " + (i+1) + ": Mã Roles rỗng");
				check = false;
				continue;
			}
			String[] rolesLst = userPermissionsLst.get(i).getRoleIds().trim().split(",");
			// Check roles: empty element
			boolean checkEmpty = false;
			for (int j = 0; j<rolesLst.length; j++) {
				if(rolesLst[j].trim().equals("")) {
					checkEmpty = true;
					break;
				}
			}
			if (checkEmpty) {
				result.getErrorLst().add("Dòng " + (i+1) + ": Mã Roles chứa Role kí tự rỗng");
				check = false;
				continue;
			}
			// Check roles: exist
			String rolesCheckResult = "";
			for (int j=0; j<rolesLst.length; j++) {
				boolean checker = false;
				for(RoleDTO role: allRolesLst) {
					if(role.getRoleCode().equals(rolesLst[j].trim())) {
						checker = true;
						break;
					}
				}
				if(!checker) {
					rolesCheckResult = rolesCheckResult + rolesLst[j] + ",";
				}
			}
			if (!rolesCheckResult.equals("")) {
				rolesCheckResult = rolesCheckResult.substring(0, rolesCheckResult.length() - 1);
				result.getErrorLst().add("Dòng " + (i+1) + ": Mã Roles không tồn tại ( " + rolesCheckResult + " )");
				check = false;
				continue;
			}
		}
		
		if(check) {
			List<UserRolesDTO> resultByUserRole = getUserRole(loginIdList);
			
			for (UserPermissionFromFile upff: userPermissionsLst) {
				UserRolesDTO userRole = resultByUserRole.stream()
				  .filter(ur -> upff.getLoginId().equals(ur.getLoginId()))
				  .findAny()
				  .orElse(null);
				List<RoleDTO> roles = userRole.getRoles();
				
				String[] rolesLst = upff.getRoleIds().trim().split(",");
				for (int j=0; j<rolesLst.length; j++) {
					for(RoleDTO role: allRolesLst) {
						if(role.getRoleCode().trim().equals(rolesLst[j].trim())) {
							roles.add(role);
							break;
						}
					}
				}
				userRole.setRoles(roles);
			}
			
			result.setSuccessIds(resultByUserRole);
		}
		
		return result;
	} 

	/**
	 * Service tim kiem va hien thi users tsa
	 * 
	 * @author anhbv.ho
	 * @param
	 * @return return users
	 * @throws Exception
	 */
	public ManageUsersTsaDTO searchUsersTsa(SearchUserTsaDTO searchUserTsaDTO) {
		ManageUsersTsaDTO response = new ManageUsersTsaDTO();
		response.setTotalCount(10L);
		Long totalUser = this._uok.user.usersRepo().countUsersTsa(searchUserTsaDTO);
		response.setTotalCount(totalUser);
		List<UserSearchTsaDTO> usersTsa = this._uok.user.usersRepo().searchUsersTsa(searchUserTsaDTO);
		response.setData(usersTsa);
		return response;
	}
}






