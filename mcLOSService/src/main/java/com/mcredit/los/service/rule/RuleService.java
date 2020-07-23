package com.mcredit.los.service.rule;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.los.service.BasedService;
import com.mcredit.model.object.Applicant;
import com.mcredit.model.object.RuleObject;
import com.mcredit.rule.RuleScoreCardManager;
import com.mcredit.rule.manager.RuleManager;
import com.mcredit.util.JSONConverter;

@Path("/v1.0/rule")
public class RuleService  extends BasedService {
	
	@POST
	@Path("/checkRule")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response rule(RuleObject input) throws Exception {
		try( RuleManager manager = new RuleManager() ) {
			return ok(manager.checkRule(input));
		}
	}
	
	/**
	 * Check multiple rules
	 * @param input : map rule object
	 * @return list rules result
	 * @throws Exception
	 */
	@POST
	@Path("/checkRules")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response rules(Map<String, RuleObject> input) throws Exception {
		try( RuleManager manager = new RuleManager() ) {
			return ok(manager.checkRules(input));
		}
	}

	@POST
	@Path("/score")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getScore(Applicant a) throws Exception {
		try( RuleScoreCardManager manager = new RuleScoreCardManager() ) {
			return ok(JSONConverter.toJSON(manager.getScoreCardResult(a)));
		}
	}

	
}
