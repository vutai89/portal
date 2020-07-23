package com.mcredit.service.common;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.business.common.manager.MessageManager;
import com.mcredit.model.dto.common.MessageTaskDTO;
import com.mcredit.service.BasedService;

@Path("/v1.0/message-tsk")
public class MessageTaskService extends BasedService{
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCustomer(MessageTaskDTO request) throws Exception {
		try( MessageManager manager = new MessageManager() ) {
			return ok(manager.upsertMessageTask(request, null));			
		}catch (Exception e) {
			return internalServerError(e);	
		}
	}
}
