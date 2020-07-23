package com.mcredit.los.service.blacklist;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.business.black_list.manager.BlackListManager;
import com.mcredit.los.service.BasedService;

@Path("/v1.0/blacklist")
public class BlackListService  extends BasedService {
	
	@GET
	@Path("/check")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkBlackList(@QueryParam("CitizenID") String citizenId, @QueryParam("CitizenIDOld") String citizenIdOld, 
			@QueryParam("MilitaryId") String militaryId, @QueryParam("CompanyTaxNumber") String companyTaxNumber) throws Exception {
		try (BlackListManager manager = new BlackListManager()) {
			return ok(manager.checkBlackList(citizenId, citizenIdOld, militaryId, companyTaxNumber));
		}
	}
}
