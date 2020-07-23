package com.mcredit.los.service.heathcheck;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.mcredit.los.service.BasedService;

@Path("/v1.0/heath-check")
public class HeathCheckService extends BasedService {

	public HeathCheckService(@Context HttpHeaders headers) {
		super(headers);
	}

	@GET
	@Path("/ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() throws Exception {
		return Response.status(Status.OK.getStatusCode()).entity("{\"status\":\"Pong!!!\"}").build();
	}

}
