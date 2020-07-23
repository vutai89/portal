package com.mcredit.business.ocr.validation;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class OcrValidation extends AbstractValidation {
	public void validateCheckPcb(Object payload, String appId) throws ValidationException {
		if (StringUtils.isNullOrEmpty(appId) || payload ==  null) {
			throw new ValidationException(Messages.getString("pcb.appId.payload.required"));
		}
	}
	
	public void validateCompareQrCode(String shd, String ngayKyHopDong, String hoTenKhachHang, String soCMT, String ngayCap,
			String noiCap, String soTienVay, String thoiHanVay, String laiSuatVay, String phiBaoHiem,
			String tenCongTyBaoHiem, String soTaiKhoanNguoiNhan, String nganHangNhan, String daiLyChiHo,
			String tenHangHoa, String nhanHieu, String mauMa, String soKhung, String soMay, String pos, String fileType,
			String imageLink) throws ValidationException {
		if (StringUtils.isNullOrEmpty(shd)) {
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.ocr.doctype.shd")));
		}
		if (StringUtils.isNullOrEmpty(ngayKyHopDong)) {
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.ocr.doctype.shd")));
		}
		if (StringUtils.isNullOrEmpty(hoTenKhachHang)) {
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.ocr.doctype.shd")));
		}
		if (StringUtils.isNullOrEmpty(soCMT)) {
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.ocr.doctype.shd")));
		}
		if (StringUtils.isNullOrEmpty(ngayCap)) {
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.ocr.doctype.shd")));
		}
		if (StringUtils.isNullOrEmpty(noiCap)) {
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.ocr.doctype.shd")));
		}
		if (StringUtils.isNullOrEmpty(soTienVay)) {
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.ocr.doctype.shd")));
		}
		if (StringUtils.isNullOrEmpty(thoiHanVay)) {
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.ocr.doctype.shd")));
		}
		if (StringUtils.isNullOrEmpty(laiSuatVay)) {
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.ocr.doctype.shd")));
		}
		if (StringUtils.isNullOrEmpty(fileType)) {
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.ocr.doctype.shd")));
		}
		if (StringUtils.isNullOrEmpty(imageLink)) {
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.ocr.doctype.shd")));
		}
		
	}
}
