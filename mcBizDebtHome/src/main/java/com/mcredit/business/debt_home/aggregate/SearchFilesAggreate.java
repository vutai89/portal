package com.mcredit.business.debt_home.aggregate;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcredit.business.debt_home.callout.esbAPI;
import com.mcredit.business.debt_home.utils.SearchFilesUtil;
import com.mcredit.data.UnitOfWork;
import com.mcredit.model.dto.debt_home.DebtContractInfo;
import com.mcredit.model.dto.debt_home.DebtHomeAllFile;
import com.mcredit.util.StringUtils;

public class SearchFilesAggreate {
	private UnitOfWork uok = null;
	
	public SearchFilesAggreate(UnitOfWork uok) {
		this.uok = uok;
	}
	
	public List<DebtHomeAllFile> lookup(String contractNumber, String currentUser, boolean isRemoveFilePath) throws Exception {
		
		String appNumber = this.uok.debtHome.debtHomeAssignRepository().getAppNumberByContractNumber(contractNumber);
		if (appNumber == null) {
			throw new Exception("Can not find app number by contract number");
		}
		
		List<DebtHomeAllFile> files = this.lookupFilePathByAppNumber(appNumber);
		
		if (isRemoveFilePath) {
			files = SearchFilesUtil.removeFilePath(files);
		}
		
		return files;
	}
	
	public String getFilePath(Long fileId) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		DebtHomeAllFile file = mapper.readValue(new esbAPI().getFilePath(fileId), new TypeReference<DebtHomeAllFile>() {
		});

		if (file.getFilePath() == null) {
			throw new Exception("Can not get filePath from file ID");
		}

		return file.getFilePath();
	}
	
	public Long getAppNumber(Long fileId) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		DebtHomeAllFile file = mapper.readValue(
				new esbAPI().getFilePath(fileId),
				new TypeReference<DebtHomeAllFile>() {
		});
		
		if (file.getAppNumber() == null) {
			throw new Exception("Can not get app number from file ID");
		}
		
		return file.getAppNumber();
	}
	
	/**
	 * Get list Contract info by input: contractNumber, appNumber, mobilePhone, identityNumber
	 * @param contractNumber
	 * @param appNumber
	 * @param mobilePhone
	 * @param identityNumber
	 * @return list contract info
	 */
	public List<DebtContractInfo> lookupContractInfo(String contractNumber, String appNumber, String mobilePhone, String identityNumber) {
		return this.uok.debtHome.debtHomeAssignRepository().getListContractInfo(contractNumber, appNumber, mobilePhone, identityNumber);
	}
	
	/**
	 * Get file by appNumber
	 * @param appNumber
	 * @return
	 * @throws Exception
	 */
	public List<DebtHomeAllFile> lookupFile(String appNumber) throws Exception {
		return this.lookupFilePathByAppNumber(appNumber);
	}
	
	/**
	 * Function get lst file by appNumber
	 * @param appNumber
	 * @return
	 * @throws Exception
	 */
	private List<DebtHomeAllFile> lookupFilePathByAppNumber(String appNumber) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		
		List<DebtHomeAllFile> files = new ArrayList<DebtHomeAllFile>();
		
		files = mapper.readValue(new esbAPI().lookupFiles(appNumber),
				new TypeReference<List<DebtHomeAllFile>>() {
		});
		
		files = SearchFilesUtil.getDistinctFilePath(files);
		
		return files;
	}
	
}
