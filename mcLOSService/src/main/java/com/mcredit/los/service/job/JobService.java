package com.mcredit.los.service.job;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.business.job.cancelCaseBPM.CancelCaseBPMManager;
import com.mcredit.business.job.cardNotify.CardNotifyManager;
import com.mcredit.business.job.cardPayment.CardPaymentManager;
import com.mcredit.business.job.contract.ContractManager;
import com.mcredit.business.job.contract.ContractUpdateCreditManager;
import com.mcredit.business.job.createCard.CreateCardManager;
import com.mcredit.business.job.sms.SMSManager;
import com.mcredit.los.service.BasedService;
import com.sun.jersey.api.client.ClientResponse.Status;


@Path("/v1.0/job")
public class JobService extends BasedService {

	@GET
	@Path("/createCard")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCredit() throws Exception {
		try (CreateCardManager manager = new CreateCardManager()) {
			return Response.status(Status.OK).entity(manager.createCard()).build();
		}
	}

	@GET
	@Path("/checkCard")
	@Produces(MediaType.APPLICATION_JSON)
	public Response CheckCredit() throws Exception {
		try (CreateCardManager manager = new CreateCardManager()) {
			return Response.status(Status.OK).entity(manager.checkCard()).build();
		}
	}

	@GET
	@Path("/cardPayment")
	@Produces(MediaType.APPLICATION_JSON)
	public Response CardPayment() throws Exception {
		try (CardPaymentManager manager = new CardPaymentManager()) {
			return Response.status(Status.OK).entity(manager.runJob()).build();
		}
	}

	@GET
	@Path("/sms")
	@Produces(MediaType.APPLICATION_JSON)
	public Response SMS() throws Exception {
		try (SMSManager manager = new SMSManager()) {
			return Response.status(Status.OK).entity(manager.runJobSingle()).build();
		}
	}

	@GET
	@Path("/cardNotify")
	@Produces(MediaType.APPLICATION_JSON)
	public Response CardNotify() throws Exception {
		try (CardNotifyManager manager = new CardNotifyManager()) {
			return Response.status(Status.OK).entity(manager.runJob()).build();
		}
	}

	@GET
	@Path("/checkUpdateCreditT24")
	public Response checkUpdateCreditT24() throws Exception {
		try (ContractManager manager = new ContractManager()) {
			return returnObject(manager.GetContractCreditUpdateT24());
		}
	}

	@GET
	@Path("/updateCreditContractT24")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCreditContractT24() throws Exception {
		try (ContractUpdateCreditManager manager = new ContractUpdateCreditManager()) {
			return Response.status(Status.OK).entity(manager.runJobSingle()).build();
		}
	}
	
}
