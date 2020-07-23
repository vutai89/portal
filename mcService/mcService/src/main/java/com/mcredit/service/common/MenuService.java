/**
 * 
 */
package com.mcredit.service.common;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.service.BasedService;
import com.mcredit.sharedbiz.manager.MenuManager;

@Path("/v1.0/menu")
public class MenuService extends BasedService {

	public MenuService(@Context HttpHeaders headers) {
		super(headers);
	}

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserTreeMenu() throws Exception {
		return this.authorize(() -> {
			try (MenuManager manager = new MenuManager()) {
				return ok(manager.getUserTreeMenu(this.currentUser.getLoginId()));
			}
		});
	}
}
