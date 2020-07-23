package com.mcredit.service.checkcic;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.business.checkcic.manager.CheckCICManager;
import com.mcredit.model.enums.ServiceName;
import com.mcredit.model.enums.cic.CICRequestSource;
import com.mcredit.service.BasedService;
import com.sun.jersey.multipart.FormDataParam;

@Path("/v1.0/check-cic")
public class CheckCICService extends BasedService {
	
	public CheckCICService(@Context HttpHeaders headers) {
		super(headers);
	}

	/**
	 * CIC Requester create new request check cic if not exists
	 * @author catld.ho
	 * @param citizenID : cmnd
	 * @param oldCitizenID : cmnd old
	 * @param customerName : customer name
	 * @param militaryID : cmqd
	 * @return list CICDetailDTO
	 * @throws Exception
	 */
	@GET
	@Path("/check")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkCIC(@QueryParam("citizenID") String citizenID, @QueryParam("oldCitizenID") String oldCitizenID, 
			@QueryParam("customerName") String customerName, @QueryParam("militaryID") String militaryID) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_REQUEST_CHECK_CIC, () -> {
			try( CheckCICManager manager = new CheckCICManager() ) {
				return ok(manager.checkCIC(citizenID, oldCitizenID, militaryID, customerName, this.currentUser.getLoginId(), CICRequestSource.WEB_PORTAL.value()));
			}
		});
	}
	
	/**
	 * CIC user search cic result
	 * @author catld.ho
	 * @param citizenID : cmnd/cccd/cmqd
	 * @param customerName
	 * @return list CICDetailDTO
	 * @throws Exception
	 */
	@GET
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchCIC(@QueryParam("citizenID") String citizenID, @QueryParam("customerName") String customerName) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_SEARCH_CIC_RESULT, () -> {
			try( CheckCICManager manager = new CheckCICManager() ) {
				return ok(manager.searchCIC(citizenID, customerName));
			}
		});
	}
	
	/**
	 * Search list cic result (call from bpm, cic extension)
	 * @author catld.ho
	 * @param listIdentity
	 * @return list cic result
	 * @throws Exception
	 */
	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchListCIC(List<String> listIdentity) throws Exception {
//		return this.authorize(ServiceName.GET_V1_0_SEARCH_CIC_RESULT, () -> {
			try( CheckCICManager manager = new CheckCICManager() ) {
				return ok(manager.searchListCIC(listIdentity));
			}
//		});
	}
	
	/**
	 * CIC checker claim request check cic to process
	 * @author catld.ho
	 * @return list CICDetailDTO
	 * @throws Exception
	 */
	@GET
	@Path("/claim-request")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCICRequest() throws Exception {
		return this.authorize(ServiceName.GET_V1_0_CLAIM_REQUEST, () -> {
			try( CheckCICManager manager = new CheckCICManager() ) {
				return ok(manager.claimCICRequest(this.currentUser.getLoginId()));
			}
		});
	}
	
	/**
	 * update cic result
	 * @author catld.ho
	 * @param fileContent : file cic result
	 * @param payload : cic result
	 * @return CICDetailDTO
	 * @throws Exception
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadFile(@FormDataParam("file") InputStream fileContent, @FormDataParam("payload") String payload)
			throws Exception {
		return this.authorize(ServiceName.POST_V1_0_UPDATE_CIC_RESULT, () -> {
			try( CheckCICManager manager = new CheckCICManager()) {
				return ok(manager.updateCIC(fileContent, payload, this.currentUser.getLoginId()));
			}
		});
	}

	/**
	 * Get file cic result
	 * @author catld.ho
	 * @param identity : cmnd/cccd/cmqd
	 * @return File
	 * @throws Exception
	 */
	@GET
	@Path("/download-image")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response downloadFile(@QueryParam("identity") String identity)
			throws Exception {
//		return this.authorize(() -> {
			try( CheckCICManager manager = new CheckCICManager()) {
				return responseFile(manager.getFile(identity), false);
			}
//		});
	}
	
	/**
	 * Execute job auto create check cic request. Job called from Scheduler
	 * @author catld.ho
	 * @return success or fail
	 * @throws Exception
	 */
	@GET
	@Path("/auto-create-request")
	@Produces(MediaType.APPLICATION_JSON)
	public Response autoCreateCICRequest()
			throws Exception {
		try( CheckCICManager manager = new CheckCICManager()) {
			return ok(manager.autoCreateCICRequest());
		}
	}

	/**
	 * Like GET /check but not need authen & don't send email
	 * @author catld.ho
	 * @param citizenID
	 * @param customerName
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCICRequest(@QueryParam("citizenID") String citizenID, @QueryParam("customerName") String customerName, 
			@QueryParam("userName") String userName) throws Exception {
		try( CheckCICManager manager = new CheckCICManager() ) {
			return ok(manager.checkCIC(citizenID, null, null, customerName, userName, null));
		}
	}
	
	/**
	 * Report cic result error or wrong
	 * Re-create request to update new cic result again
	 * @param citizenID
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/report-cic-error")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response reportCICError(@QueryParam("citizenID") String citizenID, @QueryParam("userName") String userName) throws Exception {
		try( CheckCICManager manager = new CheckCICManager() ) {
			return ok(manager.reportCICResult(citizenID, userName));
		}
	}
	
	@GET
	@Path("/syncFileName")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response synFileName(@QueryParam("encode") String encode)
			throws Exception {
//		return this.authorize(() -> {
			try( CheckCICManager manager = new CheckCICManager()) {
				return ok(manager.syncFileName(encode));
			}
//		});
		
	}
}
