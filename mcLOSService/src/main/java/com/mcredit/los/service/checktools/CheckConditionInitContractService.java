//
//package com.mcredit.los.service.checktools;
//import javax.ws.rs.Consumes;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.HttpHeaders;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
//import com.mcredit.business.checktools.manager.CheckToolsManager;
//import com.mcredit.los.service.BasedService;
//import com.mcredit.model.dto.checktools.ConditionInitContract;
//
//
//@Path("/v1.0/check-init-contract")
//public class CheckConditionInitContractService extends BasedService {
//	
//	public CheckConditionInitContractService(@Context HttpHeaders headers){
//		super(headers);
//	}
//	
//	/**
//	 * Kiem tra thong tin khoi tao khoan vay (pre-check phase 2)
//	 * @param conditionInitContract
//	 * @return
//	 * @throws Exception
//	 */
//	@POST
//	@Path("/")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response getResult(ConditionInitContract conditionInitContract) throws Exception {
//		try( CheckToolsManager manager = new CheckToolsManager() ) {
//			return ok(manager.checkConditionInitContract(conditionInitContract));
//		}
//	}
//}
