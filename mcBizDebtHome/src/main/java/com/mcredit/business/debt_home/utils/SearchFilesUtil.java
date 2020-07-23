package com.mcredit.business.debt_home.utils;

import java.util.ArrayList;
import java.util.List;

import com.mcredit.model.dto.debt_home.DebtHomeAllFile;

public class SearchFilesUtil {

	public static List<DebtHomeAllFile> getDistinctFilePath(List<DebtHomeAllFile> files) {

		List<DebtHomeAllFile> newFiles = new ArrayList<DebtHomeAllFile>();

		for (DebtHomeAllFile file : files) {
			if (!isFilePathExist(file.getFilePath(), newFiles)) {
				newFiles.add(file);
			}
		}
		return newFiles;
	}

	public static boolean isFilePathExist(String filePath, List<DebtHomeAllFile> files) {
		for (DebtHomeAllFile file : files) {
			if (file.getFilePath().equals(filePath)) {
				return true;
			}
		}
		return false;
	}

	public static List<DebtHomeAllFile> removeFilePath(List<DebtHomeAllFile> files) {

		List<DebtHomeAllFile> newFiles = files;

		for (DebtHomeAllFile file : newFiles) {
			file.setFilePath("");
		}
		return newFiles;
	}
}
