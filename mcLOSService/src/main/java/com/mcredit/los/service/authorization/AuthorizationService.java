package com.mcredit.los.service.authorization;
 
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.los.service.BasedService;
import com.mcredit.model.dto.AuthorizationRequestDTO;
import com.mcredit.model.dto.ResponseSuccess;
import com.mcredit.sharedbiz.manager.AuthorizationManager;



@Path("/v1.0/authorization")
public class AuthorizationService extends BasedService {
	
	public AuthorizationService(@Context HttpHeaders headers){
		super(headers);
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response authorized(AuthorizationRequestDTO request) throws Exception {		
		try( AuthorizationManager manager = new AuthorizationManager() ) {
			return ok(manager.authorized(request));
		}
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() throws Exception {		
		return this.authorize(()->{
			return ok(new ResponseSuccess());
		});
	}
}
