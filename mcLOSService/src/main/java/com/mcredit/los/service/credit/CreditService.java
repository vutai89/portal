package com.mcredit.los.service.credit;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
//import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
//import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.business.credit.CreditManager;
import com.mcredit.business.credit.dto.CreditDTO;
import com.mcredit.los.service.BasedService;

@Path("/v1.0/credit")
public class CreditService extends BasedService{

	@POST
	@Path("/request")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCredit(CreditDTO request) throws Exception {
		try( CreditManager manager = new CreditManager() ) {
			return ok(manager.upsertCredit(request, null));
		}
	}
	
	@DELETE
	@Path("/request/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCredit(@PathParam("id") String id) throws Exception {
		try( CreditManager manager = new CreditManager() ) {
			return ok(manager.deleteCredit(id));
		}
	}
}
