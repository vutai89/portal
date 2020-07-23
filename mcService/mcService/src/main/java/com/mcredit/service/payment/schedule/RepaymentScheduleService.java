package com.mcredit.service.payment.schedule;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.repayment.dto.RepaymentSchedulePayload;
import com.mcredit.repayment.manager.RepaymentScheduleManager;
import com.mcredit.service.BasedService;

/** 
 * @author manhnt1.ho
 * @since 10/2019
 */
@Path("/v1.0/repaymentSchedule")
public class RepaymentScheduleService extends BasedService {
	public RepaymentScheduleService(@Context HttpHeaders headers) {
		super(headers);
	}

	
	/** 
	 * @author manhnt1.ho
	 * @param id 
	 * @return Lay thong tin lich tra no
	 * @throws Exception
	 */
	@POST
	@Path("/repaymentSchedule")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRepaymentSchedule(RepaymentSchedulePayload payload) throws Exception {
		try (RepaymentScheduleManager mng = new RepaymentScheduleManager()) {
			return ok(mng.getRepaymentSchedule(payload));
		}
	}
	
}
