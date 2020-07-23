package com.mcredit.service.warehouse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.business.ocr.manager.OcrManager;
import com.mcredit.service.BasedService;

@Path("/v1.0/ai")
public class QRCodeService extends BasedService {

	public QRCodeService(@Context HttpHeaders headers) {
		super(headers);
	}

	@GET
	@Path("/compare-qr-code")
	@Produces(MediaType.APPLICATION_JSON)
	public Response compareQRCode(@QueryParam("shd") String shd, @QueryParam("ngayKyHopDong") String ngayKyHopDong,
			@QueryParam("hoTenKhachHang") String hoTenKhachHang, @QueryParam("soCMT") String soCMT,
			@QueryParam("ngayCap") String ngayCap, @QueryParam("noiCap") String noiCap,
			@QueryParam("soTienVay") String soTienVay, @QueryParam("thoiHanVay") String thoiHanVay,
			@QueryParam("laiSuatVay") String laiSuatVay, @QueryParam("phiBaoHiem") String phiBaoHiem,
			@QueryParam("tenCongTyBaoHiem") String tenCongTyBaoHiem,
			@QueryParam("soTaiKhoanNguoiNhan") String soTaiKhoanNguoiNhan,
			@QueryParam("chiNhanhNganHang") String nganHangNhan, @QueryParam("daiLyChiHo") String daiLyChiHo,
			@QueryParam("tenHangHoa") String tenHangHoa, @QueryParam("nhanHieu") String nhanHieu,
			@QueryParam("mauMa") String mauMa, @QueryParam("soKhung") String soKhung, @QueryParam("soMay") String soMay,
			@QueryParam("pos") String pos, @QueryParam("fileType") String fileType,
			@QueryParam("imageLink") String imageLink) throws Exception {
		try (OcrManager manager = new OcrManager()) {
			return ok(manager.compareQrCode(shd, ngayKyHopDong, hoTenKhachHang, soCMT, ngayCap, noiCap, soTienVay,
					thoiHanVay, laiSuatVay, phiBaoHiem, tenCongTyBaoHiem, soTaiKhoanNguoiNhan, nganHangNhan, daiLyChiHo,
					tenHangHoa, nhanHieu, mauMa, soKhung, soMay, pos, fileType, imageLink));
		}
		// return this.authorize(() -> {
		// });
	}
}
