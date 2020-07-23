package com.mcredit.service.leadgen;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.business.telesales.manager.LeadGenManager;
import com.mcredit.model.leadgen.LeadDTO;
import com.mcredit.model.leadgen.LeadResultDTO;
import com.mcredit.service.BasedService;
import com.mcredit.util.StringUtils;

@Path("/v1.0/leadgen")

public class LeadGenService extends BasedService {

	public LeadGenService(@Context HttpHeaders headers) {
		super(headers);
	}

	@POST
	@Path("/check")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkLead(LeadDTO input) throws Exception {
		try (LeadGenManager manager = new LeadGenManager()) {
			LeadResultDTO result = manager.checkLead(input);
			if (!StringUtils.isNullOrEmpty(result.getReason())) {
				return badRequest(result);
			}
			return ok(result);
		}
	}

	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createLead(LeadDTO input) throws Exception {
		try (LeadGenManager manager = new LeadGenManager()) {
			LeadResultDTO result = manager.createLead(input);
			if (!StringUtils.isNullOrEmpty(result.getReason())) {
				return badRequest(result);
			}
			return ok(result);
		}
	}
}
