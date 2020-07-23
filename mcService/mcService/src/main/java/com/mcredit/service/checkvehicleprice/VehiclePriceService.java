package com.mcredit.service.checkvehicleprice;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.business.check_vehicle_price.manager.CheckVehiclePriceManager;
import com.mcredit.service.BasedService;
import com.sun.jersey.multipart.FormDataParam;


@Path("/v1.0/vehicle-price")
public class VehiclePriceService extends BasedService {
	
	public VehiclePriceService(@Context HttpHeaders headers) {
		super(headers);
	}
	
	@GET
	@Path("/droplistSearch")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response droplistSearch() throws Exception {
		return this.authorize(() -> {
			try( CheckVehiclePriceManager manager = new CheckVehiclePriceManager() ) {
				return ok(manager.droplistSearch());
			}
		});
	}
	
	@GET
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findMotorPrice(@QueryParam("brand") String brand, @QueryParam("motorCode") String motorCode, @QueryParam("motorType") String motorType, 
			@QueryParam("page") Integer page, @QueryParam("rowPerPage") Integer rowPerPage)
					throws Exception {
		return this.authorize(() -> {
			try( CheckVehiclePriceManager manager = new CheckVehiclePriceManager() ) {
				return ok(manager.findMotorPrice(brand, motorCode, motorType, page, rowPerPage));
			}
		});
	}

	@POST
	@Path("/import")
	@Consumes("multipart/form-data")
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadVehiclePrice(
            @FormDataParam("fileContent") InputStream fileContent
			) throws Exception {
		return this.authorize(() -> {
			try (CheckVehiclePriceManager manager = new CheckVehiclePriceManager())	{
					return accepted(manager.uploadVehiclePrice(fileContent, this.currentUser.getLoginId()));
			}
		});
	}
}
