package com.mcredit.service.authorization;
 
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.model.dto.AuthorizationRequestDTO;
import com.mcredit.model.dto.ResponseSuccess;
import com.mcredit.service.BasedService;
import com.mcredit.sharedbiz.manager.AuthorizationManager;



@Path("/v1.0/authorization")
public class AuthorizationService extends BasedService {
	
	public AuthorizationService(@Context HttpHeaders headers){
		super(headers);
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
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response authorized(AuthorizationRequestDTO request) throws Exception {		
		try( AuthorizationManager manager = new AuthorizationManager() ) {
			return ok(manager.authorized(request));
		}
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
	@POST
	@Path("/ott")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response authorizedOTT(AuthorizationRequestDTO request) throws Exception {		
		try( AuthorizationManager manager = new AuthorizationManager() ) {
			return ok(manager.authorizedOTT(request));
		}
	}
	
	/**
	 * verify token whether still valid or not.
	 * 
	 * @author dongtd.ho
	 * @param none
	 * @return 200 or 401
	 */
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() throws Exception {		
		return this.authorize(()->{
			return ok(new ResponseSuccess());
		});
	}
}
