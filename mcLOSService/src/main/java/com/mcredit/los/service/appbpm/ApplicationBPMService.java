/**
 * BPM call service whenever change app_status and update return reason.
 */
package com.mcredit.los.service.appbpm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.los.service.BasedService;
import com.mcredit.sharedbiz.manager.AppBPMManager;
import com.mcredit.util.JSONConverter;

@Path("/v1.0/application")
public class ApplicationBPMService extends BasedService {
	public ApplicationBPMService(@Context HttpHeaders headers) {
		super();
	}

	/**
	 * Call procedure 'online_update_app_status' to update app status
	 * @author huyendtt.ho 
	 * @return Response.OK
	 * @throws Exception
	 */
	@POST
	@Path("/update-app-status")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateAppStatus(com.mcredit.model.dto.appbpm.AppStatusBPMDTO payload) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		System.out.println("Update app status " + sdf.format(Calendar.getInstance().getTime()) +"====" + JSONConverter.toJSON(payload));
		try(AppBPMManager manager = new AppBPMManager() ) {
			return ok(manager.updateAppStatus(payload));
		}
	}

	/**
	 * Call procedure 'online_update_reason' to update app reason
	 * @author huyendtt.ho
	 * @return Status.OK or throws Exception
	 * @throws Exception
	 */
	@POST
	@Path("/update-app-reason")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateAppReason(List<com.mcredit.model.dto.appbpm.AppReasonBPMDTO> payload) throws Exception {
		System.out.println("Update app reason====" + JSONConverter.toJSON(payload));
		try(AppBPMManager manager = new AppBPMManager()) {
			return ok(manager.updateAppReason(payload));
		}
	}

}
