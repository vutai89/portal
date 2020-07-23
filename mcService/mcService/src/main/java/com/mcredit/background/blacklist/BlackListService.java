package com.mcredit.background.blacklist;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.business.black_list.manager.BlackListManager;
import com.mcredit.model.dto.black_list.CustMonitorDTO;
import com.mcredit.service.BasedService;
import com.sun.jersey.multipart.FormDataParam;

@Path("/v1.0/black-list")
public class BlackListService extends BasedService{
	
	public BlackListService(@Context HttpHeaders headers) {
		super(headers);
	}
	
	@POST
	@Path("/import")
	@Consumes("multipart/form-data")
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadVehiclePrice(
            @FormDataParam("fileContent") InputStream fileContent
			) throws Exception {
		return this.authorize(() -> {
			try (BlackListManager manager = new BlackListManager())	{
					return accepted(manager.importBlackList(fileContent, this.currentUser.getLoginId()));
			}
		});
	}
	
	@POST
	@Path("/save")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response save(List<CustMonitorDTO> list) throws Exception {
		return this.authorize(() -> {
			try (BlackListManager manager = new BlackListManager()) {
				return ok(manager.save(list, this.currentUser.getLoginId()));
			}
		});
	}
	
	
}
