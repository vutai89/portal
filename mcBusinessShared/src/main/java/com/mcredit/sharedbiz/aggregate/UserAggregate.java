package com.mcredit.sharedbiz.aggregate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.mcredit.data.UnitOfWork;
import com.mcredit.data.exception.NoRecordFoundException;
import com.mcredit.data.mobile.entity.UsersProfiles;
import com.mcredit.data.user.entity.EmployeeLink;
import com.mcredit.data.user.entity.Users;
import com.mcredit.model.dto.MenuFunctionDTO;
import com.mcredit.model.dto.ServicePermissionDTO;
import com.mcredit.model.dto.common.ExtNumberDTO;
import com.mcredit.model.dto.telesales.TelesaleUser;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.ServiceName;
import com.mcredit.model.object.ADUser;
import com.mcredit.model.object.Sale;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.UserPermissionCacheManager;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.service.LDAPService;
import com.mcredit.sharedbiz.validation.AuthorizationException;
import com.mcredit.sharedbiz.validation.ValidationException;

public class UserAggregate {
	private UnitOfWork _uok = null;
	private LDAPService service = null;
	private static ModelMapper modelMapper = new ModelMapper();
	private String accountSubFix = "@mcredit.local";
	private EmployeeLink employeeLink;
	
	public UserAggregate(UnitOfWork uok) {
		this._uok = uok;
	}

	public EmployeeLink getEmpBySaleCode(String saleCode) {
		employeeLink = this._uok.user.usersRepo().findEmpBySaleCode(saleCode);
		return employeeLink;
	}
	
	public EmployeeLink getEmployeeLink() {
		return employeeLink;
	}
	
	public void login(String username, String password) throws AuthorizationException {
		try {
			service = new LDAPService();
			service.connect(username + accountSubFix, password);
			service.disconnect();
		} catch (Throwable e) {
			throw new AuthorizationException("Username or Password is invalid.");
		}
	}

	public UserDTO getUserInfoBy(String loginId) throws ValidationException, NoRecordFoundException {	
		Users user = this._uok.user.userSessionRepo().findUserInfoBy(loginId);
		
		return user != null ? modelMapper.map(user, UserDTO.class) : null;
	}
	
	public UserDTO checkToken(String token) throws ValidationException, NoRecordFoundException {
			
		Users user = CacheManager.Token().get(token);
		
		return user != null ? modelMapper.map(user, UserDTO.class) : null;
	}
	
	public UserDTO checkOTT(String token) throws ValidationException, NoRecordFoundException {
		
		Users user = CacheManager.Token().getOTT(token);
		
		return user != null ? modelMapper.map(user, UserDTO.class) : null;
	}
	
	public UserDTO isActiveUser(String loginId) throws ValidationException {
		Users user = this._uok.user.usersRepo().findUserByLoginId(loginId.toLowerCase());
		
		return user != null ? modelMapper.map(user, UserDTO.class) : null;
	}
	
	public Users findUserByUserId(Long userId) throws Exception {
		return this._uok.user.usersRepo().findUserById(userId);
	}
	
/*	public List<RoleDTO> findRoleCodeLst(String loginId) throws ValidationException {
		try {
			return this._uok.user.usersRepo().findRoleCodeLst(loginId);
		} catch (Exception e) {
			throw new ValidationException(e.getMessage());
		}
	}
*/	
	public List<UserDTO> getActiveUsers() throws ValidationException {
		try {
			
			List<UserDTO> list = null;
			//List<Users> users = this._uok.user.usersRepo().findActiveUser();
			List<Users> users = this._uok.user.usersRepo().findActiveEmployee();

			if(users != null && users.size() > 0){
				list = new ArrayList<UserDTO>();
				for (Users u : users) {
					list.add(modelMapper.map(u, UserDTO.class));
				}
			}
	
			return list;
		} catch (Exception e) {
			throw new ValidationException(e.getMessage());
		}
	}
	
	public ExtNumberDTO getExtNumber(String adCode) throws ValidationException {
		try {
			return this._uok.user.usersRepo().getExtNumber(adCode);
		} catch (Exception e) {
			throw new ValidationException(e.getMessage());
		}
	}
	
	public List<Sale> findSaleByManager(Long empId) throws ValidationException {
		try {
			return this._uok.user.usersRepo().findSaleByManager(empId);
		} catch (Exception e) {
			throw new ValidationException(e.getMessage());
		}
	}
	
	public List<TelesaleUser> findTsaList(boolean superVisor, String ntb, String asm, String xsm, boolean teamLead, Long currentUserId) throws ValidationException {
		try {
			return this._uok.user.usersRepo().findTsaList(superVisor, ntb, asm, xsm, teamLead, currentUserId);
		} catch (Exception e) {
			throw new ValidationException(e.getMessage());
		}
	}
	
	public void syncAllUser() throws Exception{
		service = new LDAPService();
		String adAdminUsername = CacheManager.Parameters().findParamValueAsString(ParametersName.AD_ADMIN_USERNAME);
		String adAdminPassword = CacheManager.Parameters().findParamValueAsString(ParametersName.AD_ADMIN_PASSWORD);
		service.connect(adAdminUsername + accountSubFix, adAdminPassword);
		ArrayList<ADUser> users = service.getAllUser();
		
		if(users != null && users.size() > 0){
			
			List<Users> currentUsers = this._uok.user.usersRepo().findAllUsers();
			
			for (ADUser u : users) {
				Users nUser = convertFrom(u);
				nUser.setCreatedBy(adAdminUsername);
				String id = nUser.getLoginId().trim();
				Users exitedUser = currentUsers.stream().filter(item-> item.getLoginId().equals(id)).findAny().orElse(null);
				
				if(exitedUser != null){
					exitedUser.setUsrFullName(nUser.getUsrFullName());
					exitedUser.setLastUpdatedBy(adAdminUsername);
					exitedUser.setLastUpdatedDate(new Date());
					this._uok.user.usersRepo().upsert(exitedUser);
				}else				
					this._uok.user.usersRepo().upsert(nUser);				
			}
		}
	}
	
	
	
	public Users findUserByLoginId(String currentUsername) {
		 return this._uok.user.usersRepo().findUserByLoginId(currentUsername);
	}
	
	private Users convertFrom(ADUser u){
		Users nUser = new Users();
		nUser.setUsrFullName(u.name);
		nUser.setLoginId(u.userId);
		nUser.setRecordStatus("A");
		nUser.setUsrType("1");
		nUser.setStartEffDate(new Date());
		return nUser;
	}
	
	public boolean isFunctionAllow(String loginId, int functionId) throws Exception {
		List<MenuFunctionDTO> list = UserPermissionCacheManager.getInstance().getUserFunction(loginId, false);
		for(MenuFunctionDTO mfd : list) {
			if(mfd.getFunctionId() == functionId) {
				return true;
			}
		}
		return false;
	}

	public boolean isFunctionAllow(String loginId, String functionCode) throws Exception {
		List<MenuFunctionDTO> list = UserPermissionCacheManager.getInstance().getUserFunction(loginId, false);
		for(MenuFunctionDTO mfd : list) {
			if(functionCode.equals(mfd.getFunctionCode())) {
				return true;
			}
		}
		return false;
	}

	public boolean isServiceAllow(String loginId, int serviceId) throws Exception {
		List<ServicePermissionDTO> list = UserPermissionCacheManager.getInstance().getUserService(loginId);
		for(ServicePermissionDTO spd : list) {
			if(spd.getServiceId() == serviceId) {
				return true;
			}
		}
		return false;
	}

	public boolean isServiceAllow(String loginId, ServiceName serviceName) throws Exception {
		List<ServicePermissionDTO> list = UserPermissionCacheManager.getInstance().getUserService(loginId);
		for(ServicePermissionDTO spd : list) {
			if(serviceName.stringValue().equalsIgnoreCase(spd.getServiceName())) {
				return true;
			}
		}
		return false;
	}
	
	public UsersProfiles getUsersProfilesByUserId(Long id) {
		return this._uok.user.usersProfilesRepository().getByUserId(id);
	}
	
	public void updateUserProfile(UsersProfiles usersProfiles) {
		this._uok.user.usersProfilesRepository().update(usersProfiles);
	}
	
	public Boolean checkImei(String imei, Long userId) {
		return this._uok.user.usersProfilesRepository().checkImei(imei, userId);
	}
}
