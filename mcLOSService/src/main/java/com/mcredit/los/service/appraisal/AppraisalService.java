
package com.mcredit.los.service.appraisal;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.business.appraisal.manager.AppraisalManager;
import com.mcredit.los.service.BasedService;
import com.mcredit.model.dto.appraisal.AppraisalObjectDTO;

@Path("/v1.0/appraisal")
public class AppraisalService extends BasedService {
	
	public AppraisalService(@Context HttpHeaders headers){
		super(headers);
	}

	/**
	 * update appraisal data from bpm
	 * @author catld.ho
	 * @param appraisalObj : appraisal object sent by bpm
	 * @return code 200 if save success
	 * @throws Exception
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveAppraisal(AppraisalObjectDTO appraisalObj) throws Exception {
		try( AppraisalManager manager = new AppraisalManager() ) {
			return ok(manager.losSaveAppraisal(appraisalObj));
		}
	}
	
	/**
	 * Get appraisal result by transactionId
	 * @author catld.ho
	 * @param appraisalObj : appraisal object include transactionId and action sent by bpm
	 * @return code 200 if appraisal pass
	 * @throws Exception
	 */
	@POST
	@Path("/getResult")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getResult(AppraisalObjectDTO appraisalObj) throws Exception {
		try( AppraisalManager manager = new AppraisalManager() ) {
			return ok(manager.getAppraisalResult(appraisalObj));
		}
	}
}
