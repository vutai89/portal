package com.mcredit.mobile.service.mobile4sale;

import java.io.File;
import java.io.InputStream;

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

import com.mcredit.business.mobile.manager.MobileManager;
import com.mcredit.mobile.service.BasedService;
import com.mcredit.model.dto.ResponseSuccess;
import com.mcredit.model.enums.ServiceName;
import com.mcredit.model.object.mobile.dto.CancelCaseDTO;
import com.mcredit.model.object.mobile.dto.NoteDto;
import com.mcredit.model.object.mobile.dto.SearchCaseDTO;
import com.mcredit.model.object.mobile.dto.SearchNotiDTO;
import com.mcredit.model.object.mobile.dto.UpdateNotificationIdDTO;
import com.mcredit.sharedbiz.validation.PermissionException;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/v1.0/mobile-4sales")
public class Mobile4SaleService extends BasedService {

	public Mobile4SaleService(@Context HttpHeaders headers) throws PermissionException {
		super(headers);
	}
	
	/**
	 * Service dashboard information
	 * 
	 * @author hoanx.ho
	 * @param 
	 * @return case processing number, case abort number 
	 * @throws Exception
	 */
	@GET
	@Path("/dashboard")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDashboardInfo() throws Exception {

		return this.authorize(ServiceName.GET_V1_0_MOBILE_GET_DASHBOARD_INFO, () -> {
			try (MobileManager manager = new MobileManager(this.currentUser)) {
				return ok(manager.getDashboardInfo());
			}
		});
	}

	
	/**
	 * Service get cases
	 * 
	 * @author hoanx.ho
	 * @param status (processing or abort), keyword, page number, page size
	 * @return cases processing or abort
	 * @throws Exception
	 */
	@GET
	@Path("/cases")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCases(@QueryParam("status") String status, @QueryParam("keyword") String keyword,
			@QueryParam("pageNumber") Integer pageNumber, @QueryParam("pageSize") Integer pageSize) throws Exception {

		return this.authorize(ServiceName.GET_V1_0_MOBILE_GET_CASES, () -> {
			try (MobileManager manager = new MobileManager(this.currentUser)) {
				SearchCaseDTO searchCaseDTO = new SearchCaseDTO(keyword, pageNumber, pageSize, status);
				return ok(manager.getCases(searchCaseDTO));
			}
		});
	}

	
	/**
	 * Service get products
	 * 
	 * @author hoanx.ho
	 * @param 
	 * @return list product effective
	 * @throws Exception
	 */
	@GET
	@Path("/products")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProducts() throws Exception {

		return this.authorize(ServiceName.GET_V1_0_MOBILE_GET_PRODUCTS, () -> {
			try (MobileManager manager = new MobileManager(this.currentUser)) {
				return ok(manager.getProducts());
			}
		});

	}

	/**
	 * Service get kiosks
	 * 
	 * @author hoanx.ho
	 * @param 
	 * @return list kiosk
	 * @throws Exception
	 */
	@GET
	@Path("/kiosks")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getShops() throws Exception {

		return this.authorize(ServiceName.GET_V1_0_MOBILE_GET_KIOSKS, () -> {
			try (MobileManager manager = new MobileManager(this.currentUser)) {
				return ok(manager.geKiosks());
			}
		});
	}
	

	/**
	 * Service download file pdf
	 * 
	 * @author hoanx.ho
	 * @param document id
	 * @return file pdf
	 * @throws Exception
	 */
	@GET
	@Path("/downloadPdf/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/pdf")
	public Response downloadFile(@PathParam("id") Long documentId)
			throws Exception {

		return this.authorize(ServiceName.GET_V1_0_MOBILE_DOWNLOAD_PDF, () -> {
			try (MobileManager manager = new MobileManager(this.currentUser)) {

				File file = manager.downloadFile(documentId);
				return responseFile(file);
			}
		});
	}
	
	
	/**
	 * Service get notifications
	 * 
	 * @author hoanx.ho
	 * @param 
	 * @return list notifications
	 * @throws Exception
	 */
	@GET
	@Path("/notifications")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNotifications(@QueryParam("keyword") String keyword,
			@QueryParam("pageNumber") Integer pageNumber, @QueryParam("pageSize") Integer pageSize)
			throws Exception {

		return this.authorize(ServiceName.GET_V1_0_NOTIFICATIONS, () -> {
			try (MobileManager manager = new MobileManager(this.currentUser)) {
				SearchNotiDTO searchDTO = new SearchNotiDTO(pageNumber, pageSize, keyword);
				return ok(manager.getNotifications(searchDTO));
			}
		});
	}

	/**
	 * Service Check category of company
	 * 
	 * @author cuongvt.ho
	 * @param companyTaxNumber ma so thue cong ty
	 * @return company infomation
	 * @throws Exception
	 */
	@GET
	@Path("/check-cat")
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkCategory(@QueryParam("companyTaxNumber") String companyTaxNumber) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_MOBILE_CHECK_CATEGORY, () -> {
			try (MobileManager manager = new MobileManager(this.currentUser)) {
				return ok(manager.checkCategory(companyTaxNumber));
			}
		});
	}

	/**
	 * Service get list case note
	 * 
	 * @author cuongvt.ho
	 * @param appId appID cua ho so duoc sinh ra tu BPM
	 * @return return list case note
	 * @throws Exception
	 */
	@GET
	@Path("/list-case-note/{appNumber}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCaseNote(@PathParam("appNumber") String appNumber) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_MOBILE_LIST_CASE_NOTE, () -> {
			try (MobileManager manager = new MobileManager(this.currentUser)) {
				return ok(manager.getCaseNote(appNumber));
			}
		});
	}

	/**
	 * Service send case note
	 * 
	 * @author cuongvt.ho
	 * @param noteDto noi dung note bao gom noi dung note va appID cua ho so tu BPM
	 * @return return success
	 * @throws Exception
	 */
	@POST
	@Path("/send-case-note")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response sendCaseNote(NoteDto noteDto) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_MOBILE_SEND_CASE_NOTE, () -> {
			try (MobileManager manager = new MobileManager(this.currentUser)) {
				return ok(manager.sendCaseNote(noteDto));
			}
		});
	}

	/**
	 * Service get checklist anh ho so
	 * 
	 * @author linhtt2.ho
	 * @param mobileSchemProductCode: ma san pham
	 *            mobileTempResidence: dia chi song trung ho khau hay khong
	 * @return return danh sach checklist anh ho so tuong ung
	 * @throws Exception
	 */
	@GET
	@Path("/check-list")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response checklist(@QueryParam("mobileSchemaProductCode") String mobileSchemaProductCode,
			@QueryParam("mobileTemResidence") String mobileTemResidence) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_MOBILE_CHECK_LIST, () -> {
			try (MobileManager manager = new MobileManager(null)) {
				return ok(manager.checklist(mobileSchemaProductCode, mobileTemResidence));
			}			
		});
	}

	/**
	 * Service tao moi ho so va upload anh
	 * 
	 * @author linhtt2.ho
	 * @param payload object: thong tin ho so gui len
	 *             file .zip: thong tin anh cua ho so
	 * @return return success
	 * @throws Exception: loi khi tao moi hoac cap nhat anh cho ho so tra lai
	 */
	@POST
	@Path("/upload-document")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail, @FormDataParam("object") String payload)
			throws Exception {
		return this.authorize(ServiceName.GET_V1_0_MOBILE_UPLOAD_DOCUMENT, () -> {
			try (MobileManager manager = new MobileManager(this.currentUser)) {
				System.out.println("payload: " + payload);
				System.out.println("fileDetail: " + fileDetail.getFileName());
				return ok(manager.createCase(uploadedInputStream, payload, fileDetail.getFileName()));
			}
		});
	}

	/**
	 * Service huy ho so tra ve
	 * 
	 * @author linhtt2.ho
	 * @param CancelCaseDTO: ma ho so, ma li do va mo ta li do huy ho so
	 * @return return success
	 * @throws Exception
	 */
	@POST
	@Path("/cancel-case")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response cancelCase(CancelCaseDTO ccDTO) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_MOBILE_CANCEL_CASE, () -> {
			try (MobileManager manager = new MobileManager(this.currentUser)) {
				return ok(manager.cancelCase(ccDTO));
			}
		});
	}
	
	/**
	 * Service get daily report
	 * @author cuongvt.ho
	 * @param dateExport ngay xem report
	 * @return list report daily
	 * @throws Exception
	 */
	@GET
	@Path("/daily")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getReport(@QueryParam("dateExport") String dateExport) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_MOBILE_DAILY_REPORT, () -> {
			try( MobileManager manager = new MobileManager(this.currentUser)) {
				return ok(manager.getReport(dateExport));
			}
		});
	}
	
	/**
	 * Service Get Report approval
	 * @author cuongvt.ho
	 * @param DateExport ngay xem report
	 * @return list report approval
	 * @throws Exception
	 */
	@GET
	@Path("/approval")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getApprovalReport(@QueryParam("dateExport") String dateExport) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_MOBILE_APPROVAL_REPORT, () -> {
			try( MobileManager manager = new MobileManager(this.currentUser)) {
				return ok(manager.getApprovalReport(dateExport));
			}
		});
	}
	
	
	/**
	 * update NotificationId
	 * 
	 * @author sonhv.ho
	 * @param 
	 * @return 1L
	 * @throws Exception
	 */
	@POST
	@Path("/updateNotificationId")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateNotificationId(UpdateNotificationIdDTO updateNotificationIdDTO) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_MOBILE_UPDATE_NOTI_ID, () -> {
			try (MobileManager manager = new MobileManager(this.currentUser)) {
				return ok(manager.updateNotificationId(updateNotificationIdDTO));
			}
		});
	}

}
