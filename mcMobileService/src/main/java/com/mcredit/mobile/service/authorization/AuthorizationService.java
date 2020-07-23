package com.mcredit.mobile.service.authorization;
 
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.mobile.service.BasedService;
import com.mcredit.model.object.mobile.dto.LoginMobileRequestDTO;
import com.mcredit.sharedbiz.manager.AuthorizationManager;
import com.mcredit.sharedbiz.validation.PermissionException;



@Path("/v1.0/authorization")
public class AuthorizationService extends BasedService {
	
	public AuthorizationService(@Context HttpHeaders headers) throws PermissionException{
		super(headers);
	}
	
	/**
	 * @param loginMobileRequestDTO
	 * loginMobileRequestDTO.username
	 * loginMobileRequestDTO.password
	 * loginMobileRequestDTO.notificationId: notificationId de ben thu 3 push notification ve dien thoai
	 * loginMobileRequestDTO.imei : imei cua dien thoai
	 * loginMobileRequestDTO.osType : IOS or ANDROID
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginMobile4Sale(LoginMobileRequestDTO loginMobileRequestDTO) throws Exception {		
		try( AuthorizationManager manager = new AuthorizationManager() ) {
			return ok(manager.authorizedMobile(loginMobileRequestDTO));
		}
	}
}
