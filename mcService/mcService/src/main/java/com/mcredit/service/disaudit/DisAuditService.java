package com.mcredit.service.disaudit;

import java.io.File;
import java.io.IOException;
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
import javax.ws.rs.core.Response.ResponseBuilder;

import com.mcredit.business.audit.manager.DisAuditManager;
import com.mcredit.service.BasedService;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/v1.0/audit")
public class DisAuditService extends BasedService {

	public DisAuditService(@Context HttpHeaders headers) {
		super(headers);
	}
	
	/**
	 * Service lay va doi soat du lieu hang ngay
	 * 
	 * @author linhtt2.ho
	 * @param date: Ngay lay du lieu
	 *        thirdParty: Doi tac lay du lieu
	 *        override: co ghi de du lieu cu khong
	 * @return return success
	 * @throws Exception: loi khi lay file that bai hoac cau truc file khong hop le
	 */
	@GET
	@Path("/import-data")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public Response importData(@QueryParam("date") String date, @QueryParam("thirdParty") String thirdParty,
			@QueryParam("override") int override) throws Exception {
		try (DisAuditManager manager = new DisAuditManager()) {
			return ok(manager.importData(date, thirdParty, override));
		}
	}

	/**
	 * Service thuc hien lay thong tin bao cao duoi dang excel
	 * 
	 * @author linhtt2.ho
	 * @param fromDate: Ngay bat dau lay du lieu
	 * 		  toDate: Ngay ket thuc lay du lieu
	 *        thirdParty: Doi tac lay du lieu
	 *        reportType: Loai bao cao thu/chi
	 * @return return file bao cao excel
	 * @throws Exception
	 */
	@GET
	@Path("/report")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/vnd.ms-excel")
	public Response getReport(@QueryParam("thirdParty") String thirdParty, @QueryParam("fromDate") String fromDate, 
			@QueryParam("toDate") String toDate, @QueryParam("reportType") String reportType) throws Exception {
		try (DisAuditManager manager = new DisAuditManager()) {
			File file = manager.getReport(thirdParty, fromDate, toDate, reportType);
			return responseExcelFile(file);
		}
	}
	
	// get data overview
	/**
	 * Service lay thong tin bao cao: thong tin tong quan
	 * 
	 * @author linhtt2.ho
	 * @param file: File bao cao cua Viettel (neu thuc hien doi soat Viettel)
	 * 		  payload: Noi dung thong tin day len de lay bao cao
	 * @return return json chua thong tin tong quan
	 * @throws Exception
	 */
	@POST
	@Path("/result")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response auditResult(@FormDataParam("file") InputStream uploadedInputStream, 
			@FormDataParam("file") FormDataContentDisposition fileDetail, @FormDataParam("object") String payload) throws Exception {
		try (DisAuditManager manager = new DisAuditManager()){
			if (null == uploadedInputStream) {
				return ok(manager.auditResult(uploadedInputStream, payload, ""));
			} else {
				return ok(manager.auditResult(uploadedInputStream, payload, fileDetail.getFileName()));
			}
		}
	}
	
	/**
	 * Service lay thong tin bao cao: danh sach ban ghi khop/khong khop chi tiet
	 * 
	 * @author linhtt2.ho
	 * @param thirdParty: ben thu ba thuc hien lay thong tin
	 * 		  fromDate: Ngay bat dau doi soat
	 * 		  toDate: Ngay ket thuc doi soat
	 * 		  reportType: loai bao cao doi soat (thu/chi)
	 * 		  result: ket qua lua chon (khop / khong khop)
	 *        workflow: Loai thanh toan
	 *        time: Thoi gian doi soat (17h/24h)
	 *        pageSize: so ban ghi tra ve 1 page
	 *        pageNum: trang bao nhieu
	 * @return return json chua cac ban ghi khop / khong khop chi tiet
	 * @throws Exception
	 */
	@GET
	@Path("/result/detail")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public Response getDetailResult(@QueryParam("thirdParty") String thirdParty, @QueryParam("fromDate") String fromDate,
			@QueryParam("toDate") String toDate, @QueryParam("reportType") String reportType,@QueryParam("result") String result,
			@QueryParam("workflow") String workflow, @QueryParam("time") String time,
			@QueryParam("pageSize") int pageSize, @QueryParam("pageNum") int pageNum) throws Exception {
		try (DisAuditManager manager = new DisAuditManager()){
			return ok(manager.getResult(thirdParty, fromDate, toDate, reportType,result, workflow, time, pageSize, pageNum));
		}
	}
}
