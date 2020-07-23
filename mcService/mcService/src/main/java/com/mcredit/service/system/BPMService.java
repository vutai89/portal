/**
 * 
 */
package com.mcredit.service.system;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.model.enums.ServiceName;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.service.BasedService;
import com.mcredit.sharedbiz.esb.BPMProxy;

@Path("/v1.0/bpm")
public class BPMService extends BasedService {
	BasedHttpClient bs = new BasedHttpClient();

	public BPMService(@Context HttpHeaders headers) {
		super(headers);
	}

	@GET
	@Path("/getDepartmentsBatch")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDepartmentsBatch() throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Departments_Batch, () -> {
			try (BPMProxy manager = new BPMProxy()) {
				return ok(manager.getDepartmentsBatch().getBodyContent());
			}
		});

	}

	@GET
	@Path("/getDepartmentsArea")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDepartmentsArea() throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Departments_Area, () -> {
			try (BPMProxy manager = new BPMProxy()) {
				return ok(manager.getDepartmentsArea().getBodyContent());
			}
		});

	}

	@GET
	@Path("/getDepartmentsRegion")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDepartmentsRegion() throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Departments_Region, () -> {
			try (BPMProxy manager = new BPMProxy()) {
				return ok(manager.getDepartmentsRegion().getBodyContent());
			}
		});

	}

	@GET
	@Path("/getDepartmentHistory")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDepartmentHistory(@QueryParam("departmentId") String departmentId) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Departments_History, () -> {
			try (BPMProxy manager = new BPMProxy()) {
				return ok(manager.getDepartmentHistory(departmentId).getBodyContent());
			}
		});

	}

	@GET
	@Path("/searchDepartments")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchDepartments(@QueryParam("isListUpdated") String isListUpdated,
			@QueryParam("RegionId") String regionId, @QueryParam("AreaId") String areaId,
			@QueryParam("BatchId") String batchId, @QueryParam("UpdateStatus") String updateStatus,
			@QueryParam("DepartmentCode") String departmentCode, @QueryParam("DepartmentName") String departmentName,
			@QueryParam("SipType") String sipType, @QueryParam("UpdateDate") String updateDate,
			@QueryParam("offset") String offset, @QueryParam("limit") String limit) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Departments_searchDepartments, () -> {
			try (BPMProxy manager = new BPMProxy()) {
				return ok(manager.searchDepartments(isListUpdated, regionId, areaId, batchId, updateStatus,
						departmentCode, departmentName, sipType, updateDate, offset, limit).getBodyContent());
			}
		});

	}

	@PUT
	@Path("/updateDepartmnet")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateDepartmnet(Object request) throws Exception {
		return this.authorize(ServiceName.PUT_V1_0_Departments_updateDepartmnet, () -> {
			try (BPMProxy manager = new BPMProxy()) {
				return ok(manager.updateDepartmnet(request).getBodyContent());
			}
		});

	}
}
