package com.mcredit.service.mobile;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.business.mobile.manager.ReportManager;
import com.mcredit.service.BasedService;

@Path("/v1.0/mobile/report")
public class ReportService extends BasedService {
	public ReportService(@Context HttpHeaders headers){
		super(headers);
	}

	@GET
	@Path("/daily")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getReport(@QueryParam("dateExport") String dateExport) throws Exception {
		
		try( ReportManager manager = new ReportManager(this.currentUser)) {
			return ok(manager.getReport(dateExport));
		}
	}
	
	@GET
	@Path("/approval")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getApprovalReport(@QueryParam("dateExport") String dateExport) throws Exception {
		
		try( ReportManager manager = new ReportManager(this.currentUser)) {
			return ok(manager.getApprovalReport(dateExport));
		}
	}
}
