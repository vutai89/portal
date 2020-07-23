package com.mcredit.service.telesales;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.business.telesales.manager.XsellManager;
import com.mcredit.data.telesale.entity.UplDetail;
import com.mcredit.model.dto.telesales.UploadFileXsellDTO;
import com.mcredit.model.enums.ServiceName;
import com.mcredit.service.BasedService;
import com.sun.jersey.multipart.FormDataParam;
import java.io.InputStream;
import javax.ws.rs.POST;
import com.mcredit.model.dto.telesales.UplDetailDTO;

@Path("/v1.0/telesales/xsell")
public class XsellService extends BasedService {

    public XsellService(@Context HttpHeaders headers) {
        super(headers);
    }

    @GET
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchFiles(@QueryParam("status") String statusCodeValue1,
            @QueryParam("dateFrom") String fromDateUpload, @QueryParam("dateTo") String toDateUpload) throws Exception {
    	return this.authorize(ServiceName.GET_V1_0_XSell_SearchFiles, () -> {
	        try (XsellManager manager = new XsellManager(this.currentUser)) {
	            return ok(manager.searchXSellFiles(statusCodeValue1, fromDateUpload, toDateUpload));
	        }
		});
    }



    @GET
    @Path("/download")
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getFile(
            @QueryParam("fileId") Long fileId
    ) throws Exception {
    	return this.authorize(ServiceName.GET_V1_0_XSell_Download_file,() -> {
	        try (XsellManager manager = new XsellManager(this.currentUser)) {
	            UplDetail uplDetail = manager.getUplDetailById(fileId);
	            return file(uplDetail.getServerFileName(), uplDetail.getUplFileName(), false);
	        }
		});
    }

    @PUT
    @Path("/changeStatusUpl/{action}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeStatusUpl(UplDetailDTO uplDTO, @PathParam("action") int action) throws Exception {
    	return this.authorize(ServiceName.PUT_V1_0_XSell_ApprovalUpl, () -> {
	        try (XsellManager manager = new XsellManager(this.currentUser)) {
	            return ok(manager.changeStatusUpl(uplDTO, action));
	        }
		});
    }
    @DELETE
    @Path("/deleteFile/{action}/{idUpl}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFile(@PathParam("action") int action,@PathParam("idUpl") int idUpl) throws Exception {
    	return this.authorize(ServiceName.DELETE_V1_0_XSell_DeleteUpl, () -> {
	        try (XsellManager manager = new XsellManager(this.currentUser)) {
	            return ok(manager.changeStatusUpl(new UplDetailDTO(idUpl), action));
	        }
    	});
    }
    @POST
    @Path("/import")
    @Consumes("multipart/form-data")
    @Produces(MediaType.APPLICATION_JSON)
    public Response scanImportXsell(
            @FormDataParam("userFileName") String userFileName,
            @FormDataParam("uplType") String uplType,
            @FormDataParam("dateFrom") String dateFrom,
            @FormDataParam("dateTo") String dateTo,
            @FormDataParam("fileContent") InputStream fileContent
    //			BufferedInMultiPart bimp
    ) throws Exception {

		return this.authorize(ServiceName.POST_V1_0_Xsell_Import,()->{
        try (XsellManager manager = new XsellManager(this.currentUser)) {
            UploadFileXsellDTO xsellDTO = new UploadFileXsellDTO();
            xsellDTO.setUserFileName(userFileName);
            xsellDTO.setUplType(uplType);
            xsellDTO.setFileContent(fileContent);
            xsellDTO.setDateFrom(dateFrom);
            xsellDTO.setDateTo(dateTo);
            return accepted(manager.importXsellUplCusomter(xsellDTO));
        }
		});
    }

    @PUT
    @Path("/changeStatusImport/{uplDetailId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeStatusImport(@PathParam("uplDetailId") Long uplDetailId) throws Exception {
        return this.authorize(ServiceName.PUT_V1_0_XSell_ChangeStatus, () -> {
            try (XsellManager manager = new XsellManager(this.currentUser)) {
                return ok(manager.changeStatusImport(uplDetailId));
            }
        });
    }

}
