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

import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.service.BasedService;
import com.mcredit.sharedbiz.dto.ClickToCallDTO;
import com.mcredit.sharedbiz.esb.ContactCenterProxy;

@Path("/v1.0/contact_center")
public class ContactCenterService extends BasedService {
	
	BasedHttpClient bs = new BasedHttpClient();

	public ContactCenterService(@Context HttpHeaders headers) {
		super(headers);
	}

	@POST
	@Path("/dial")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNapasBankListMC(ClickToCallDTO request) throws Exception {
		try (ContactCenterProxy manager = new ContactCenterProxy()) {
			return ok(manager.dial(request).getBodyContent());
		}
	}
}
