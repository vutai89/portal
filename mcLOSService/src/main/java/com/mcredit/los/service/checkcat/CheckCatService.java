package com.mcredit.los.service.checkcat;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.business.checkcat.manager.CheckCatManager;
import com.mcredit.los.service.BasedService;

@Path("/v1.0/checkcat")
public class CheckCatService extends BasedService{

	@GET
	@Path("/checkComp")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkCat(@QueryParam("compTaxNumber") String compTaxNumber, @QueryParam("productCode") String productCode) throws Exception {
		try( CheckCatManager manager = new CheckCatManager() ) {
			return ok(manager.checkCatForLOSService(compTaxNumber, productCode));
		}
	}

}
