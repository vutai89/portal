package com.mcredit.service.allfreco;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.xml.bind.ValidationException;

import com.mcredit.alfresco.manager.ECMManager;
import com.mcredit.business.debt_home.manager.DebtHomeManager;
import com.mcredit.common.Messages;
import com.mcredit.model.dto.DowloadZipDTO;
import com.mcredit.model.object.ecm.InputUploadECM;
import com.mcredit.service.BasedService;
import com.mcredit.sharedbiz.manager.AlfrecoManager;

@Path("/v1.0/alfreco-service")
public class AlfrecoService extends BasedService {

	@GET
	@Path("/getCacheSharelink")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSharelink(@QueryParam("objectId") String objectId) throws Exception {
		try (AlfrecoManager manager = new AlfrecoManager()) {
			return ok(manager.getSharelink(objectId));
		}
	}

	@GET
	@Path("/getSharelink")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sharelink(@QueryParam("objectId") String objectId) throws Exception {
		try (AlfrecoManager manager = new AlfrecoManager()) {
			return ok(manager.sharelink(objectId));
		}
	}

	@GET
	@Path("/download-zip-file")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadFile(@QueryParam("contractNumber") String contractNumber, @QueryParam("type") String type)
			throws Exception {
		try (DebtHomeManager manager = new DebtHomeManager()) {
			ResponseBuilder response = Response
					.ok(manager.zipDocument(contractNumber, this.currentUser.getLoginId(), false))
					.type("application/zip");
			response.header("Content-Disposition", "attachment; filename=\"" + contractNumber + ".zip" + "\"");
			return response.build();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ValidationException(Messages.getString("validation.notExistDocumentContractNum"));
		}
	}

	@POST
	@Path("/upload-meta-ecm")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadMetaDataECM(List<InputUploadECM> inputUploadECM) throws Exception {
		try (ECMManager manager = new ECMManager()) {
			return ok(manager.uploadMetaDataECM(inputUploadECM));
		}
	}

}
