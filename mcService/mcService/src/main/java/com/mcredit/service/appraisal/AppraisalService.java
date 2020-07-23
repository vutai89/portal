package com.mcredit.service.appraisal;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.business.appraisal.manager.AppraisalManager;
import com.mcredit.model.dto.appraisal.AppraisalDataDetailDTO;
import com.mcredit.model.dto.appraisal.AppraisalObjectDTO;
import com.mcredit.service.BasedService;

@Path("/v1.0/appraisal")
public class AppraisalService extends BasedService {
	
	public AppraisalService(@Context HttpHeaders headers) {
		super(headers);
	}

	/**
	 * Validate appraisal info
	 * @author catld.ho
	 * @param dataDetail : appraisal object sent by appraisal tool (portal)
	 * @return list result of conditions
	 * @throws Exception
	 */
	@POST
	@Path("/validate")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response validateCustomer(AppraisalDataDetailDTO dataDetail) throws Exception {
		return this.authorize(() -> {
			try( AppraisalManager manager = new AppraisalManager() ) {
				return ok(manager.validateCustomer(dataDetail));
			}
		});
	}
	
	/**
	 * Get appraisal data by bpmAppId
	 * @author catld.ho
	 * @param bpmAppId : appId correspond customer profile
	 * @param action : actor call get data
	 * @param ottToken : ott token sent by bpm
	 * @return appraisal data in dto model
	 * @throws Exception
	 */
	@GET
	@Path("/{bpmAppId}/{action}/{ottToken}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAppraisalDetail(@PathParam("bpmAppId") String bpmAppId, @PathParam("action") String action, @PathParam("ottToken") String ottToken) throws Exception {
		return this.authorize(() -> {
			try (AppraisalManager manager = new AppraisalManager()) {
				return ok(manager.getAppraisalDataDetail(bpmAppId, action, ottToken));
			}
		});
	}
	
	/**
	 * Update appraisal data from appraisal tool
	 * @author catld.ho
	 * @param appraisalObj : appraisal object sent by appraisal tool (portal)
	 * @return code 200 if success
	 * @throws Exception
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveAppraisal(AppraisalObjectDTO appraisalObj) throws Exception {
		return this.authorize(() -> {
			try( AppraisalManager manager = new AppraisalManager()) {
				return ok(manager.saveAppraisal(appraisalObj));
			}
		});
	}
}
