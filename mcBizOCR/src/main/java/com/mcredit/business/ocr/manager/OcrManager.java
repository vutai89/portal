package com.mcredit.business.ocr.manager;

import java.io.InputStream;

import com.mcredit.business.ocr.aggregate.OcrAggregate;
import com.mcredit.business.ocr.validation.OcrValidation;
import com.mcredit.model.dto.warehouse.ContractCompareResult;
import com.mcredit.sharedbiz.manager.BaseManager;

public class OcrManager extends BaseManager {
	private OcrAggregate _agg;
	private OcrValidation _valid = new OcrValidation();

	public OcrManager() {
		_agg = new OcrAggregate(this.uok);
	}

	public ContractCompareResult compareQrCode(String shd, String ngayKyHopDong, String hoTenKhachHang, String soCMT, String ngayCap,
			String noiCap, String soTienVay, String thoiHanVay, String laiSuatVay, String phiBaoHiem,
			String tenCongTyBaoHiem, String soTaiKhoanNguoiNhan, String nganHangNhan, String daiLyChiHo,
			String tenHangHoa, String nhanHieu, String mauMa, String soKhung, String soMay, String pos, String fileType,
			String imageLink) throws Exception {
		return this.tryCatch(() -> {
//			_valid.validateCompareQrCode(shd, ngayKyHopDong, hoTenKhachHang, soCMT, ngayCap, noiCap, soTienVay, thoiHanVay,
//					laiSuatVay, phiBaoHiem, tenCongTyBaoHiem, soTaiKhoanNguoiNhan, nganHangNhan, daiLyChiHo, tenHangHoa,
//					nhanHieu, mauMa, soKhung, soMay, pos, fileType, imageLink);
			
			return _agg.compareQrCode(shd, ngayKyHopDong, hoTenKhachHang, soCMT, ngayCap, noiCap, soTienVay, thoiHanVay,
					laiSuatVay, phiBaoHiem, tenCongTyBaoHiem, soTaiKhoanNguoiNhan, nganHangNhan, daiLyChiHo, tenHangHoa,
					nhanHieu, mauMa, soKhung, soMay, pos, fileType, imageLink);
		});
	}
}	
