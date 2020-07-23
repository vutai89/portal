package com.mcredit.business.mobile.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.object.mobile.UplCreditAppFilesDTO;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.validation.ValidationException;

public class ZipUtils {
	@SuppressWarnings("resource")
	private static String unzipFile(String zipFilePath, String folderUnzip, List<UplCreditAppFilesDTO> list)
			throws IOException, ValidationException {
		ZipFile zipFile = null;
		try {
			File theDir = new File(folderUnzip);
			if (!theDir.exists()) {
				theDir.mkdirs();
			}
			if (!theDir.exists()) {
				throw new ValidationException(Messages.getString("mfs.validation.path.false"));
			}
			zipFile = new ZipFile(zipFilePath);
			Enumeration<?> enu = zipFile.entries();
			int count = 0;
			while (enu.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) enu.nextElement();
				String name = zipEntry.getName();
				// kiem tra file nen chi cho phep giai nen cac file co type nam trong white list
				if (!name.endsWith(File.separator)) {
					if (name.lastIndexOf(".") > 0) {
						boolean res = isNameInDocument(name, list);
						if (!res) {
							throw new ValidationException(Messages.getString("mfs.validation.image.extension"));
						}
						String extension = name.substring(name.lastIndexOf(".") + 1, name.length()).toUpperCase();
						if (!CacheManager.Parameters().findParamValueAsString(ParametersName.MFS_WHITE_LIST)
								.contains(extension)) {
							zipFile.close();
							throw new ValidationException(Messages.getString("mfs.validation.image.extension"));
						}
					} else {
						zipFile.close();
						throw new ValidationException(Messages.getString("validation.field.invalidFormat",
								Labels.getString("label.mfs.post.upload.zip")));
					}
				}
				if (!unzipOneImage(folderUnzip, name, zipEntry, zipFile)) {
					continue;
				}
				count++;
			}
			if (count != list.size()) {
				throw new ValidationException(Messages.getString("mfs.validation.image.false"));
			}
			return folderUnzip;
		} finally {
			if (null != zipFile) {
				zipFile.close();
			}
		}
	}

	private static boolean unzipOneImage(String folderUnzip, String name, ZipEntry zipEntry, ZipFile zipFile)
			throws IOException {
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			File file = new File(folderUnzip + File.separator + name);
			if (name.endsWith(File.separator)) {
				file.mkdirs();
				return false;
			}
			is = zipFile.getInputStream(zipEntry);
			fos = new FileOutputStream(file);
			byte[] bytes = new byte[1024];
			int length;
			while ((length = is.read(bytes)) >= 0) {
				fos.write(bytes, 0, length);
			}
			return true;
		} finally {
			if (null != is) {
				is.close();				
			}
			if (null != fos) {
				fos.close();				
			}
			
		}
	}

	private static boolean isNameInDocument(String name, List<UplCreditAppFilesDTO> list) {
		for (UplCreditAppFilesDTO upl : list) {
			if (upl.getFileName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public static String unzipNewFile(String path, String fileName, List<UplCreditAppFilesDTO> list)
			throws IOException, ValidationException {
		String fileUnzip = fileName.substring(0, fileName.indexOf("."));
		String zipFilePath = path + fileName;
		String folderUnzip = path + fileUnzip;
		return unzipFile(zipFilePath, folderUnzip, list);

	}
}
