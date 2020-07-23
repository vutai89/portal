package com.mcredit.business.ocr.document;

import com.mcredit.model.enums.ocr.ContractEnum;

public class DocFactory {
	public static IDoc create(String fileType) throws Exception {
		IDoc doc = null;
		if (fileType.equals(ContractEnum.TIEN_MAT.value())) {
			doc = new TienMatDoc();
		} else if (fileType.equals(ContractEnum.TIEN_MAT_BAO_HIEM.value())) {
			doc = new TienMatBaoHiemDoc();
		} else if (fileType.equals(ContractEnum.TRA_GOP.value())) {
			doc = new TraGopDoc();
		} else if (fileType.equals(ContractEnum.TRA_GOP_BAO_HIEM.value())) {
			doc = new TraGopBaoHiemDoc();
		} else {
			throw new Exception("Invalid Protocol.");
		}
		return doc;
	}
}
