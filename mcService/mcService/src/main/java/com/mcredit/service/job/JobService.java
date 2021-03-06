package com.mcredit.service.job;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.business.checkcic.manager.CheckCICManager;
import com.mcredit.business.job.cancelCaseBPM.CancelCaseBPMManager;
import com.mcredit.business.job.cardNotify.CardNotifyManager;
import com.mcredit.business.job.cardPayment.CardPaymentManager;
import com.mcredit.business.job.contract.ContractManager;
import com.mcredit.business.job.contract.ContractUpdateCreditManager;
import com.mcredit.business.job.createCard.CreateCardManager;
import com.mcredit.business.job.send_mail.SendMailManager;
import com.mcredit.business.job.sms.SMSManager;
import com.mcredit.business.telesales.manager.XsellManager;
import com.mcredit.service.BasedService;
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
	@Path("/xsell/campaign-status/change")
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeCampaignStatus() throws Exception {
		try (XsellManager manager = new XsellManager(this.currentUser)) {
			return ok(manager.changeCampaignStatus());
		}
	}

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

	/**
	 * @author phaonx.ho get case and call cancel to bpm : fixedDelay = 10 minute
	 * @return CancelCaseOutPutDTO CancelCaseOutPutDTO.succsesss: So luong gui appId
	 *         thanh cong. CancelCaseOutPutDTO.errors: So luong gui appId khong
	 *         thanh cong. CancelCaseOutPutDTO.listAppIdSucess: Danh sach appId
	 *         thanh cong. CancelCaseOutPutDTO.listAppIdError: Danh sach khong thanh
	 *         cong.
	 * @throws Exception
	 */

	@GET
	@Path("/cancelCaseBPM")
	@Produces(MediaType.APPLICATION_JSON)
	public Response cancelCaseBPM() throws Exception {
		try (CancelCaseBPMManager manager = new CancelCaseBPMManager()) {
			return Response.status(Status.OK).entity(manager.runJob()).build();
		}
	}
	
	@GET
	@Path("/approve-leadgen-data")
	@Produces(MediaType.APPLICATION_JSON)
	public Response approveLeadgenData() throws Exception {
		try (CheckCICManager manager = new CheckCICManager()) {
			return Response.status(Status.OK).entity(manager.jobApproveLeadgenData()).build();
		}
	}
	
	@GET
	@Path("/sendMailWarning")
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendMailWarning() throws Exception {
		try (SendMailManager manager = new SendMailManager()) {
			return ok(manager.sendMailWarn());
		}
	}
}
