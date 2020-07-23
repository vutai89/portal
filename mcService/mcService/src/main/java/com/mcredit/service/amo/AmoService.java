package com.mcredit.service.amo;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.business.telesales.manager.AmoManager;
import com.mcredit.model.leadgen.LeadDTO;
import com.mcredit.model.leadgen.LeadResultDTO;
import com.mcredit.service.BasedService;
import com.mcredit.util.StringUtils;

@Path("/v1.0/amo")

public class AmoService extends BasedService {

	public AmoService(@Context HttpHeaders headers) {
		super(headers);
	}

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createLead(LeadDTO input) throws Exception {
		
		try (AmoManager manager = new AmoManager()) {
			
			LeadResultDTO result = manager.createLead(input);
			
			return !StringUtils.isNullOrEmpty(result.getReason()) ? badRequest(result) : ok(result);
		}
	}
}
