
package com.mcredit.los.service.pcb;
import javax.ws.rs.Consumes;
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

import com.mcredit.business.pcb.manager.PCBManager;
import com.mcredit.los.service.BasedService;

@Path("/v1.0/pcb")
public class PcbService extends BasedService {
	
	public PcbService(@Context HttpHeaders headers){
		super(headers);
	}

	/**
	 * @author sonhv.ho
	 * @param payload : thong tin check PCB (BPM gui sang)
	 * @param appId : appId tuong ung voi ho so 
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("/check")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response pcbCheck(Object[] payload, @QueryParam("appId") String appId, @QueryParam("idCard") String idCard,@QueryParam("idCard1") String idCard1,@QueryParam("birthDay") String birthDay
			, @QueryParam("stepBpm") String stepBpm, @QueryParam("type") String type, @QueryParam("s37Result") Integer s37Result, @QueryParam("loanAmount") Double loanAmount) throws Exception {
		try( PCBManager manager = new PCBManager() ) {
			return ok(manager.checkPCB(payload, appId, idCard,idCard1, birthDay, stepBpm, type, s37Result,loanAmount));
		}
	}
	
	
	@POST
	@Path("/getInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response pcbCheck(@QueryParam("id") String id,@QueryParam("channel") String channel) throws Exception {
		try( PCBManager manager = new PCBManager() ) {
			return ok(manager.getPcbInfo(id,channel));
		}
	}
}
