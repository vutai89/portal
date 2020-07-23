/**
 * 
 */
package com.mcredit.service.system;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.model.enums.ServiceName;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.service.BasedService;
import com.mcredit.sharedbiz.esb.T24Proxy;

@Path("/v1.0/t24")
public class T24Service extends BasedService {
	BasedHttpClient bs = new BasedHttpClient();

	public T24Service(@Context HttpHeaders headers) {
		super(headers);
	}

	@POST
	@Path("/getNapasBankListMC")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNapasBankListMC(Object request) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_Departments_NapasBankListMC, () -> {
			try (T24Proxy manager = new T24Proxy()) {
				ApiResult result = manager.getNapasBankListMC(request);
				if (result.getCode() == 200)
					return ok(result.getBodyContent());
				else
					return badRequest(result.getBodyContent());
			}
		});

	}

	@POST
	@Path("/getNapasAccountNameMC")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNapasAccountNameMC(Object request) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_Departments_NapasAccountNameMC, () -> {
			try (T24Proxy manager = new T24Proxy()) {
				ApiResult result = manager.getNapasAccountNameMC(request);
				if (result.getCode() == 200)
					return ok(result.getBodyContent());
				else
					return badRequest(result.getBodyContent());
			}
		});

	}

	@POST
	@Path("/getMBAccountNameMC")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMBAccountNameMC(Object request) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_Departments_MBAccountNameMC, () -> {
			try (T24Proxy manager = new T24Proxy()) {
				ApiResult result = manager.getMBAccountNameMC(request);
				if (result.getCode() == 200)
					return ok(result.getBodyContent());
				else
					return badRequest(result.getBodyContent());
			}
		});

	}

	@POST
	@Path("/getCitadBranchListMC")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCitadBranchListMC(Object request) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_Departments_CitadBranchListMC, () -> {
			try (T24Proxy manager = new T24Proxy()) {
				ApiResult result = manager.getCitadBranchListMC(request);
				if (result.getCode() == 200)
					return ok(result.getBodyContent());
				else
					return badRequest(result.getBodyContent());
			}
		});

	}

	@POST
	@Path("/getCitadBankListMC")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCitadBankListMC(Object request) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_Departments_CitadBankListMC, () -> {
			try (T24Proxy manager = new T24Proxy()) {
				ApiResult result = manager.getCitadBankListMC(request);
				if (result.getCode() == 200)
					return ok(result.getBodyContent());
				else
					return badRequest(result.getBodyContent()); // throw Exception in exception fileter validation
																// exception
			}
		});

	}
}
