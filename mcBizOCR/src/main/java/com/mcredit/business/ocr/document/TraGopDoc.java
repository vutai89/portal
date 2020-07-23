package com.mcredit.business.ocr.document;

import com.mcredit.business.ocr.distance.ImageCracker;
import com.mcredit.model.dto.warehouse.ContractInfo;

public class TraGopDoc extends DocBase {
	@Override
	public ContractInfo getInfo(String filePath) throws Exception {
		String array = getArray(filePath);
		ContractInfo item = getBase(array);
		
		item.setLstProduct(ImageCracker.getListProduct(array));
		item.setPos(ImageCracker.getPos(array));

		return item;
	}
}
