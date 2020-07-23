package com.mcredit.los.service.codetable;

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
import com.mcredit.los.service.BasedService;
import com.mcredit.model.enums.ServiceName;
import com.mcredit.model.object.CodeTableQuery;

@Path("/v1.0/code-table")
public class CodeTableService extends BasedService {

	public CodeTableService(@Context HttpHeaders headers){
		super(headers);
	}
	
	@GET
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findCodeTable(@QueryParam("codeGroup") String codeGroup, @QueryParam("category") String category) throws Exception {
		try( CodeTableManager manager = new CodeTableManager() ) {
			return ok(manager.findCodeTable(codeGroup, category));
		}
	}
	
	@GET
	@Path("/call-status")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findCallStatus() throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Code_Table_Call_Status,()->{
			try( CodeTableManager manager = new CodeTableManager() ) {
				return ok(manager.findCallStatus());
			}
		});
	}
	
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCodeListByCriteria( CodeTableQuery input) throws Exception {
		try( CodeTableManager manager = new CodeTableManager() ) {
			return ok(manager.getCodeListByCriteria(input));
		}
	}
	
	@GET
	@Path("/find-cat-prcode")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findCodeTableByCatProCode(@QueryParam("category") String category, @QueryParam("productCode") String productCode) throws Exception {
		try( CodeTableManager manager = new CodeTableManager() ) {
			return ok(manager.findCodeTableByCatProCode(category, productCode));
		}
	}
		
}
