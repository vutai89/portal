package com.mcredit.business.ocr.document;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.mcredit.business.ocr.distance.ImageCracker;
import com.mcredit.model.dto.warehouse.ContractInfo;

public class DocBase implements IDoc {
	public DocBase() {
		super();
	}

	@Override
	public String getArray(String filePath) throws Exception {
		Path path = Paths.get(filePath);
		// byte[] bytes = Files.readAllBytes(path);
		List<String> array = Files.readAllLines(path, StandardCharsets.UTF_8);
		StringBuffer outPut = new StringBuffer();
		for (String str : array) {
			outPut.append(str);
		}
		return outPut.toString();
	}


	@Override
	public ContractInfo getInfo(String filePath) throws Exception {
		throw new Exception("not implement.");
	}
	
	protected ContractInfo getBase(String array) {
		ContractInfo item = new ContractInfo();
		item.setShd(ImageCracker.getSHD(array));
		
		item.setNgayKyHopDong(ImageCracker.getngayKyHopDong(array));
		item.setHoTenKhachHang(ImageCracker.getHoTenKhachHang(array));
		item.setSoCMT(ImageCracker.getSoCMT(array));
		item.setNgayCap(ImageCracker.getNgayCap(array));
		item.setNoiCap(ImageCracker.getNoiCap(array));
		item.setSoTienVay(ImageCracker.getSoTienVay(array));
		item.setThoiHanVay(ImageCracker.getThoiHanVay(array));
		item.setLaiSuatVay(ImageCracker.getLaiSuatVayBH(array));
		
		return item;
	}
}
