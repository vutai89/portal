package com.mcredit.service.common;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.service.BasedService;
import com.mcredit.sharedbiz.manager.EmployeeManager;


@Path("/v1.0/common")
public class CommonService extends BasedService {
	@GET
	@Path("/employees/{teamType}/{teamGroup}/{teamCode}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEmployees(@PathParam("teamType") String teamType ,@PathParam("teamGroup") String teamGroup,@PathParam("teamCode") String teamCode) throws Exception {
		try(EmployeeManager commonManager = new EmployeeManager()){
			return ok(commonManager.listEmployee(teamType , teamGroup, teamCode));	
		}
	}
}
