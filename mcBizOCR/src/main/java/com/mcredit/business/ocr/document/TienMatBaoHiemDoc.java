package com.mcredit.business.ocr.document;

import com.mcredit.business.ocr.distance.ImageCracker;
import com.mcredit.model.dto.warehouse.ContractInfo;

public class TienMatBaoHiemDoc extends TienMatDoc {

	@Override
	public ContractInfo getInfo(String filePath) throws Exception {
		String array = getArray(filePath);
		ContractInfo item = getBase(array);

		item.setSoTaiKhoanNguoiNhan(ImageCracker.getSoTaiKHoanNguoiNhan(array));
		item.setNganHangNhan(ImageCracker.getNganHangNhan(array));
		item.setDaiLyChiHo(ImageCracker.getDaiLyChiHo(array));

		item.setPhiBaoHiem(ImageCracker.getPhiBaoHiem(array));
		item.setTenCongTyBaoHiem(ImageCracker.getTenCongTyBaoHiem(array));

		return item;
	}

}
