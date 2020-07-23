package com.mcredit.service.common;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.business.common.manager.CommonManager;
import com.mcredit.model.dto.common.MessageLogDTO;
import com.mcredit.service.BasedService;

@Path("/v1.0/message-log")
public class MessageLogService extends BasedService {
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCustomer(MessageLogDTO request) throws Exception {
		try( CommonManager manager = new CommonManager() ) {
			return ok(manager.upsertMessageLog(request, null));
		}
	}
	
/*	@PUT
	@Path("/{recordId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCustomer(MessageLogDTO request, @PathParam("recordId") String id) throws Exception {
		try( CommonManager manager = new CommonManager() ) {
			return ok(manager.upsertMessageLog(request, id));
		}
	}*/
}
