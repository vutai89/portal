package com.mcredit.los.service.customer;

import java.io.File;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.business.common.manager.CommonManager;
import com.mcredit.business.customer.CustomerManager;
import com.mcredit.business.customer.dto.CustomerDTO;
import com.mcredit.los.service.BasedService;
import com.mcredit.model.enums.ServiceName;
import com.mcredit.model.telesale.ContractSearch;
import com.mcredit.util.StringUtils;

@Path("/v1.0/customer")
public class CustomerService extends BasedService {

	public CustomerService(@Context HttpHeaders headers) {
		super(headers);
	}

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCustomer(CustomerDTO request) throws Exception {
		try (CustomerManager manager = new CustomerManager()) {
			return ok(manager.upsertCustomer(request, null));
		}
	}

	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeCustomer(@PathParam("id") String id) throws Exception {
		try (CustomerManager manager = new CustomerManager()) {
			return ok(manager.removeCustomer(id));
		}
	}

	@GET
	@Path("/card-info/{cardId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findCardInformationByCardId(@PathParam("cardId") String cardId) throws Exception {
		try (CustomerManager manager = new CustomerManager()) {
			return ok(manager.findCardInformationByCardId(cardId));
		}
	}

	@GET
	@Path("/query-info")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryInfo(@QueryParam("type") String type, @QueryParam("contractNumber") String contractNumbers,
			@QueryParam("identityNumber") String identityNumber, @QueryParam("militaryId") String militaryId,
			@QueryParam("mobilePhone") String mobilePhone) throws Exception {
		try (CustomerManager manager = new CustomerManager()) {
			return ok(manager.queryInfo(StringUtils.nullToEmpty(type), StringUtils.nullToEmpty(contractNumbers),
					StringUtils.nullToEmpty(identityNumber), StringUtils.nullToEmpty(militaryId),
					StringUtils.nullToEmpty(mobilePhone)));
		}
	}

	@GET
	@Path("/contract-state/{contractNumber}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkContractNumber(@PathParam("contractNumber") String contractNumber) throws Exception {
		try (CustomerManager manager = new CustomerManager()) {
			return ok(manager.checkContractNumber(contractNumber));
		}
	}

	@POST
	@Path("/accountLinks")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createAccountLink(CustomerDTO request) throws Exception {
		try (CustomerManager manager = new CustomerManager()) {
			return ok(manager.upsertAccountLink(request, null));
		}
	}

	@POST
	@Path("/contract-info")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findContractInfo(ContractSearch contractSearch) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Telesales_contractInfo, () -> {
			try (CustomerManager manager = new CustomerManager()) {
				return ok(manager.findContractInfo(contractSearch));
			}
		});
	}

	/* For thientu call */
	@POST
	@Path("/info-contract")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findInfoContract(ContractSearch contractSearch) throws Exception {
		try (CustomerManager manager = new CustomerManager()) {
			return ok(manager.findContractInfo(contractSearch));
		}
	}

	@GET
	@Path("/contract-duplicate/{identityNumber}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findContractDuplicate(@PathParam("identityNumber") String identityNumber) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Telesales_contractDuplicate, () -> {
			try (CustomerManager manager = new CustomerManager()) {
				return ok(manager.findContractDuplicate(identityNumber));
			}
		});
	}

	/* For thientu call */
	@GET
	@Path("/duplicate-contract/{identityNumber}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findDuplicateContract(@PathParam("identityNumber") String identityNumber) throws Exception {
		try (CustomerManager manager = new CustomerManager()) {
			return ok(manager.findContractDuplicate(identityNumber));
		}
	}

	/* Service check tá»•ng dÆ° ná»£ KH cho BPM gá»�i */
	@GET
	@Path("/total-debt")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findTotalDebt(@QueryParam("identityNumber") String identityNumber,
			@QueryParam("oldIdentityNumber") String oldIdentityNumber,
			@QueryParam("militaryNumber") String militaryNumber) throws Exception {
		try (CustomerManager manager = new CustomerManager()) {
//			return ok(manager.findTotalDebt(StringUtils.nullToEmpty(identityNumber),
//					StringUtils.nullToEmpty(oldIdentityNumber), StringUtils.nullToEmpty(militaryNumber)));
			return ok(manager.findTotalDebtMC(StringUtils.nullToEmpty(identityNumber), StringUtils.nullToEmpty(oldIdentityNumber)
					, StringUtils.nullToEmpty(militaryNumber)));
		}
	}

	@GET
	@Path("/downloadFile/{appId}/{docType}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadFile(@PathParam("appId") String appId, @PathParam("docType") String docType)
			throws Exception {
		try (CommonManager manager = new CommonManager()) {
			File file = manager.downloadFile(appId, docType);
			return responseFile(file);
		}
	}
}
