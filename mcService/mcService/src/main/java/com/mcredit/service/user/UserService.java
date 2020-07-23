package com.mcredit.service.user;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.service.BasedService;
import com.mcredit.sharedbiz.manager.UserManager;

@Path("/v1.0/user")
public class UserService extends BasedService {

	public UserService(@Context HttpHeaders headers) {
		super(headers);
	}

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() throws Exception {
		return this.authorize(() -> {
			try (UserManager manager = new UserManager()) {
				return ok(manager.getActiveUsers());
			}
		});
	}

	@GET
	@Path("/sale/{empId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findSaleByManager(@PathParam("empId") Long empId) throws Exception {
		return this.authorize(() -> {
			try (UserManager manager = new UserManager()) {
				return ok(manager.findSaleByManager(empId));
			}
		});
	}
	
	@GET
	@Path("/tsa-list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findSaleByManager(@QueryParam("sup") boolean superVisor, @QueryParam("ntb") String ntb
			, @QueryParam("asm") String asm, @QueryParam("xsm") String xsm, @QueryParam("teamLead") boolean teamLead) throws Exception {
		return this.authorize(() -> {
			try (UserManager manager = new UserManager()) {
				return ok(manager.findTsaList(superVisor, ntb, asm, xsm, teamLead, this.currentUser.getId()));
			}
		});
	}
}
