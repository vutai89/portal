package com.mcredit.business.mobile.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mcredit.data.mobile.entity.UplCreditAppFiles;
import com.mcredit.sharedbiz.cache.CacheManager;

public class PDFUtils {
	
	@SuppressWarnings("unlikely-arg-type")
	public static Map<String, List<UplCreditAppFiles>> groupCreditAppFiles(List<UplCreditAppFiles> lstCreditAppFile) {
		if (lstCreditAppFile == null || lstCreditAppFile.size() < 1)
			return null;
		Map<String, List<UplCreditAppFiles>> lstCreditAppFileGrouped = new HashMap<String, List<UplCreditAppFiles>>();
		List<String> docTypes = new ArrayList<String>();
		for (UplCreditAppFiles creditAppFile : lstCreditAppFile) {
			String docType = CacheManager.DocumentCache().getDocumentCodeById(creditAppFile.getDocumentId());
			if (docTypes.size() < 1) {
				if (creditAppFile.getDocumentId() != null)
					docTypes.add(docType);
				continue;
			} else {
				boolean check = false;
				if (creditAppFile.getDocumentId() == null)
					continue;
				for (String key : docTypes) {
					if (key.equals(docType)) {
						check = true;
						break;
					}
				}
				if (!check)
					docTypes.add(docType);
			}
		}
		for (String key : docTypes) {
			List<UplCreditAppFiles> lstgroup = new ArrayList<UplCreditAppFiles>();
			for (UplCreditAppFiles creditAppFile : lstCreditAppFile) {
				if (key.equals(CacheManager.DocumentCache().getDocumentCodeById(creditAppFile.getDocumentId())))
					lstgroup.add(creditAppFile);
			}
			lstCreditAppFileGrouped.put(key, lstgroup);
		}
		return lstCreditAppFileGrouped;
	}

}
