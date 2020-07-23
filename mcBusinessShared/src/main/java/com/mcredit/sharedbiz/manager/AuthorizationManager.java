package com.mcredit.sharedbiz.manager;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.data.mobile.entity.UsersProfiles;
import com.mcredit.model.dto.AuthorizationRequestDTO;
import com.mcredit.model.dto.AuthorizationResponseDTO;
import com.mcredit.model.dto.MenuFunctionDTO;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.object.mobile.dto.LoginMobileRequestDTO;
import com.mcredit.model.object.mobile.dto.LoginMobileResponseDTO;
import com.mcredit.sharedbiz.aggregate.UserAggregate;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.factory.UserFactory;
import com.mcredit.sharedbiz.validation.AuthorizationException;
import com.mcredit.sharedbiz.validation.MobileLoginValidation;
import com.mcredit.sharedbiz.validation.UserValidation;
import com.mcredit.util.StringUtils;

public class AuthorizationManager extends BaseManager {
		
	private UserAggregate _agg = null;
	private int tokenExpiredTimeMobile = CacheManager.Parameters().findParamValueAsInteger(ParametersName.MFS_TOKEN_EXPIRED_TIME);
	private String exMes = "Token is required.";
	
	public AuthorizationManager(){
		_agg = UserFactory.getUserAgg(this.uok);
	}

	
	/**
	 * verify and certify user by passing username and password.
	 * this method is used for Web Portal
	 * 
	 * @author dongtd.ho
	 * @param username,password
	 * @return user information that is wrapped in AuthorizationResponseDTO object 
	 * @throws Exception when username or password is invalid.
	 */
	public AuthorizationResponseDTO authorized(AuthorizationRequestDTO request) throws Exception {

		return this.tryCatch(()->{
			
			UserValidation.validateAuthorization(request);
			
			_agg.login(request.getUsername(), request.getPassword());
			
			UserDTO user = getUserInfo(request.getUsername());
			String token = UUID.randomUUID().toString();
			addTokenToCache(user, token);
			//user = _agg.getUserInfoBy(user.getLoginId());
			
			//user.setPassword(MD5Converter.encrypt(request.getPassword()));
			
			/*add token to cache*/
			/*
			CacheManager.Token().set(token, user);
			user.setRoleCodeLst(CacheManager.UserPermission().findRoleByUser(user.getLoginId()));
			user.setServiceLst(CacheManager.UserPermission().getUserService(user.getLoginId()));
			*/
			
			//khoi tao menu cho nguoi dung vao Cache
			CacheManager.UserPermission().findUserTreeMenuByUser(user.getLoginId());
			user.setFuncLst(CacheManager.UserPermission().getUserFunction(user.getLoginId(), false));

			return new AuthorizationResponseDTO(token
											, user.getId()
											, user.getBdsId()
											, user.getBdsCode()
											, StringUtils.isNullOrEmpty(user.getSuperVisor())?false:true
											//, StringUtils.isNullOrEmpty(user.getAsm())?false:true
											, StringUtils.isNullOrEmpty(user.getTeamLead())?false:true
											, user.getEmpCode()
											, user.getExtNumber()
											, user.getDeviceName()
											, user.getFuncLst()
											, user.getLoginId()
											, user.getUsrFullName()
											, user.getUsrType()
											, user.getRoleCodeLst()
											, user.getServiceLst());

		});
		
	}
	
	/**
	 * verify and certify user by passing username and password. this token will be invalid after first use or 120 seconds if not use
	 * this method is used for integrating with other systems
	 * 
	 * @author dongtd.ho
	 * @param username,password
	 * @return user information that is wrapped in AuthorizationResponseDTO object 
	 * @throws Exception when username or password is invalid.
	 */
	public AuthorizationResponseDTO authorizedOTT(AuthorizationRequestDTO request) throws Exception {

		return this.tryCatch(()->{
			
			UserValidation.validateAuthorization(request);
			_agg.login(request.getUsername(), request.getPassword());
			
			UserDTO user = getUserInfo(request.getUsername());
			String token = UUID.randomUUID().toString();
			user = _agg.getUserInfoBy(user.getLoginId());

			//add one time token to cache
			CacheManager.Token().setOTT(token, user);			
			user.setRoleCodeLst(CacheManager.UserPermission().findRoleByUser(user.getLoginId()));
			user.setServiceLst(CacheManager.UserPermission().getUserService(user.getLoginId()));
			
			//khoi tao menu cho nguoi dung vao Cache
			CacheManager.UserPermission().findUserTreeMenuByUser(user.getLoginId());
			user.setFuncLst(CacheManager.UserPermission().getUserFunction(user.getLoginId(), false));

			return new AuthorizationResponseDTO(token
											, user.getId()
											, user.getBdsId()
											, user.getBdsCode()
											, StringUtils.isNullOrEmpty(user.getSuperVisor())?false:true
											//, StringUtils.isNullOrEmpty(user.getAsm())?false:true
											, StringUtils.isNullOrEmpty(user.getTeamLead())?false:true
											, user.getEmpCode()
											, user.getExtNumber()
											, user.getDeviceName()
											, user.getFuncLst()
											, user.getLoginId()
											, user.getUsrFullName()
											, user.getUsrType()
											, user.getRoleCodeLst()
											, user.getServiceLst());

		});
		
	}
	
	public UserDTO authorized(String username,String password) throws AuthorizationException, Exception {
		return this.tryCatch(()->{		
			_agg.login(username, password);
			
			return _agg.getUserInfoBy(username);			
		});	
	}
	
	public UserDTO checkToken(String token) throws AuthorizationException, Exception {
		
		return this.tryCatch(()->{		
			if (StringUtils.isNullOrEmpty(token))
				throw new AuthorizationException(exMes);
			
			UserDTO user = _agg.checkToken(token);
			
			if(user == null)
				throw new AuthorizationException(exMes);
			
			return user;			
		});	
	}
	
	public UserDTO checkOTT(String token) throws AuthorizationException, Exception {
		
		return this.tryCatch(()->{		
			if (StringUtils.isNullOrEmpty(token))
				throw new AuthorizationException(exMes);
			
			UserDTO user = _agg.checkOTT(token);
			
			if(user == null)
				throw new AuthorizationException(exMes);
			
			return user;			
		});	
	}
	
	/**
	 * verify and certify user by passing username and password.
	 * this method is used for mobile app
	 * 
	 * @author dongtd.ho
	 * @param username,password
	 * @return user information that is wrapped in AuthorizationResponseDTO object 
	 * @throws Exception when username or password is invalid.
	 */
	public LoginMobileResponseDTO authorizedMobile(LoginMobileRequestDTO loginMobileRequestDTO) throws Exception {

		return this.tryCatch(()->{
			
			MobileLoginValidation.validateAuthorization(loginMobileRequestDTO);
			_agg.login(loginMobileRequestDTO.getUsername(), loginMobileRequestDTO.getPassword());
			UserDTO user = getUserInfo(loginMobileRequestDTO.getUsername());
			UsersProfiles userProfiles = _agg.getUsersProfilesByUserId(user.getId());
			
			if(userProfiles == null) 
				throw new AuthorizationException(Messages.getString("validation.field.invalidFormat",Labels.getString("label.post.login.account")));
			
			if(StringUtils.isNullOrEmpty(userProfiles.getMobileImei())) {
				//Lan dau dang nhap luu lai imei
				if(_agg.checkImei(loginMobileRequestDTO.getImei(), userProfiles.getUserId()))
					throw new AuthorizationException(Messages.getString("validation.field.invalidFormat",Labels.getString("label.mfs.post.upload.imei.required")));
				
				else {
					userProfiles.setMobileImei(loginMobileRequestDTO.getImei());
					userProfiles.setOsType(loginMobileRequestDTO.getOsType());
				}
			} else {
				if(!loginMobileRequestDTO.getImei().equals(userProfiles.getMobileImei()))
					throw new AuthorizationException(Messages.getString("validation.field.invalidFormat",Labels.getString("label.mfs.post.upload.imei.required")));
			}
			
			//Luu lai notificationId neu khac null
			if(!StringUtils.isNullOrEmpty(loginMobileRequestDTO.getNotificationId()))
				userProfiles.setNotificationId(loginMobileRequestDTO.getNotificationId());
				
			_agg.updateUserProfile(userProfiles);
			
			String token = UUID.randomUUID().toString();
			addTokenToCacheMobile(user, token);
			CacheManager.UserPermission().findUserTreeMenuByUser(user.getLoginId());
			
			List<MenuFunctionDTO> menuFunctionDTOList =  CacheManager.UserPermission().getUserFunction(user.getLoginId(), false);
			List<MenuFunctionDTO> result = menuFunctionDTOList.stream().filter(item -> "MFS".equals(item.getModule())).collect(Collectors.toList()); 
			user.setFuncLst(result);
			
			LoginMobileResponseDTO loginMobileResponseDTO = new LoginMobileResponseDTO(user.getLoginId(), user.getUsrFullName(), token, user.getRoleCodeLst(),user.getFuncLst());
			
			return loginMobileResponseDTO;
		});
	}
	
	private UserDTO getUserInfo (String userName) throws Exception {
		UserDTO user = new UserDTO();
		user = _agg.isActiveUser(userName.toLowerCase());
		if(user == null)
			throw new AuthorizationException("This account is invalid.");
		
		if(!"A".equalsIgnoreCase(user.getRecordStatus()))
			throw new AuthorizationException("This account is locked.");
		
		user = _agg.getUserInfoBy(user.getLoginId());
		return user;
	}
	
	private void addTokenToCache (UserDTO user, String token) throws Exception {
		CacheManager.Token().set(token, user);
		user.setRoleCodeLst(CacheManager.UserPermission().findRoleByUser(user.getLoginId()));
		user.setServiceLst(CacheManager.UserPermission().getUserService(user.getLoginId()));
	}
	
	private void addTokenToCacheMobile (UserDTO user, String token) throws Exception {
		CacheManager.Token().set(token, user, tokenExpiredTimeMobile);
		user.setRoleCodeLst(CacheManager.UserPermission().findRoleByUser(user.getLoginId()));
		user.setServiceLst(CacheManager.UserPermission().getUserService(user.getLoginId()));
	}
	
}
