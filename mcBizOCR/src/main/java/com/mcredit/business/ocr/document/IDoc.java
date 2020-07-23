package com.mcredit.business.ocr.document;

import com.mcredit.model.dto.warehouse.ContractInfo;

public interface IDoc {
	ContractInfo getInfo(String filePath) throws Exception;
	String getArray(String path) throws Exception;
}
