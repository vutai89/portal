package com.mcredit.business.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.object.mobile.dto.PdfDTO;
import com.mcredit.sharedbiz.cache.CacheManager;

public class CaseUtils {

	public static String getRealFilePath(PdfDTO file) throws ParseException {

		String dateMigratedData = CacheManager.Parameters()
				.findParamValueAsString(ParametersName.MFS_DATE_MIGRATED_DATA);
		String pathMigratedData = CacheManager.Parameters()
				.findParamValueAsString(ParametersName.MFS_PATH_MIGRATED_DATA);

		String path = "";

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date dateMirgated = dateFormat.parse(dateMigratedData);

		boolean isAfterMigratedData = file.getCreatedDate().compareTo(dateMirgated) > -1;

		if (isAfterMigratedData) {
			path = file.getRemotePathServer();
		} else {
			path = pathMigratedData + file.getRemotePathServer();
		}

		return path;
	}

	public static String getFileNameByPath(String filePath) {

		if (filePath == null) {
			return "";
		}

		String fileName = "";
		String[] arr = filePath.split("/");
		fileName = arr[arr.length - 1];

		return fileName;
	}
}
