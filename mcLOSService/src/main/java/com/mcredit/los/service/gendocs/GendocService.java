package com.mcredit.los.service.gendocs;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.los.service.BasedService;
import com.mcredit.business.gendoc.manager.GendocManager;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;

@Path("/v1.0/genDocProvider")
public class GendocService extends BasedService {

    @GET
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/pdf")
    public Response genDocProvider(@QueryParam("appId") String appId, @QueryParam("typeOfDocument") String typeOfDocument,
            @QueryParam("typeOfLoans") String typeOfLoans, @QueryParam("userName") String userName, @QueryParam("appNum") String appNum) throws Exception {
        try (GendocManager manager = new GendocManager()) {
            return fileGendoc(manager.getfileGendoc(appId, typeOfDocument, typeOfLoans, userName, appNum), true);
        } 
    }

}
