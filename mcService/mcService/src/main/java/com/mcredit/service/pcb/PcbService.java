package com.mcredit.service.pcb;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.business.pcb.manager.PCBManager;
import com.mcredit.model.enums.ServiceName;
import com.mcredit.service.BasedService;

@Path("/v1.0/pcb/show")
public class PcbService extends BasedService {
	public PcbService(@Context HttpHeaders headers) {
		super(headers);
	}

	/**
	 * @author sonhv.ho
	 * @param id : id cua bang CREDIT_BUREAU_DATA
	 * @return PcbResponse : Thong tin check PCB
	 * @throws Exception
	 */
	@GET
	@Path("/{id}/{channel}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getPcbInfo(@PathParam("id") String id,@PathParam("channel") String channel) throws Exception {	
		return this.authorize(() -> {
			try (PCBManager manager = new PCBManager()) {
				return ok(manager.getPcbInfo(id,channel));
			}
		});
	}
}
