//
//package com.mcredit.los.service.checktools;
//import javax.ws.rs.Consumes;
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.QueryParam;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.HttpHeaders;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
//import com.mcredit.business.checktools.manager.CheckToolsManager;
//import com.mcredit.los.service.BasedService;
//
//@Path("/v1.0/check-identifier")
//public class CheckIdentifierService extends BasedService {
//	
//	public CheckIdentifierService(@Context HttpHeaders headers){
//		super(headers);
//	}
//	
//	/**
//	 * Kiem tra thong tin cmnd khach hang (pre-check phase 1)
//	 * @param productGroup
//	 * @param citizenId
//	 * @param loanAmount
//	 * @param appNumber
//	 * @return
//	 * @throws Exception
//	 */
//	@GET
//	@Path("/")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response getResult(@QueryParam("productGroup") String productGroup, @QueryParam("citizenId") String citizenId, 
//			@QueryParam("loanAmount") Long loanAmount, @QueryParam("appNumber") String appNumber) throws Exception {
//		try( CheckToolsManager manager = new CheckToolsManager() ) {
//			return ok(manager.checkCitizenId(productGroup, citizenId, loanAmount, appNumber));
//		}
//	}
//}
