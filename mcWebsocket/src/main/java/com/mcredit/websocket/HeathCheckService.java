package com.mcredit.websocket;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/v1.0/heath-check")
public class HeathCheckService {

	@GET
	@Path("/ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() throws Exception {
		return Response.status(Status.OK.getStatusCode()).entity("{\"status\":\"Pong!!!\"}").build();
	}

}
