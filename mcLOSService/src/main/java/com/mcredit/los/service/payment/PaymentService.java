package com.mcredit.los.service.payment;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.business.payment.PaymentManager;
import com.mcredit.los.service.BasedService;
import com.mcredit.model.dto.card.PaymentDTO;

@Path("/v1.0/payment")
public class PaymentService extends BasedService {
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCustomer(PaymentDTO request) throws Exception {
		try( PaymentManager manager = new PaymentManager() ) {
			return returnObject(manager.createPayment(request));
		}
	}
}
