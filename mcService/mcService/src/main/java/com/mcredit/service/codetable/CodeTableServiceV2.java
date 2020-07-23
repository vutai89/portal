package com.mcredit.service.codetable;

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

import com.mcredit.business.common.manager.CodeTableManager;
import com.mcredit.model.dto.CodeTableInput;
import com.mcredit.service.BasedService;

@Path("/v2.0/code-table")
public class CodeTableServiceV2 extends BasedService {

	public CodeTableServiceV2(@Context HttpHeaders headers){
		super(headers);
	}
	
	@GET
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findCodeTable(@QueryParam("codeGroup") String codeGroup, @QueryParam("category") String category) throws Exception {
		try( CodeTableManager manager = new CodeTableManager() ) {
			return ok(manager.findCodeTableV2(codeGroup, category));
		}
	}
	
	@GET
	@Path("/category")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findCodeTableByCategory(@QueryParam("category") String category) throws Exception {
		try( CodeTableManager manager = new CodeTableManager() ) {
			return ok(manager.findCodeTableByCategory(category));
		}
	}
	
	@POST
	@Path("/category")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findCodeTableByCategory(CodeTableInput category) throws Exception {
		try( CodeTableManager manager = new CodeTableManager() ) {
			return ok(manager.findCodeTableByCategory(category));
		}
	}
}
