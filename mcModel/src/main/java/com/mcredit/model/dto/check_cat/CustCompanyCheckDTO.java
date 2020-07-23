package com.mcredit.model.dto.check_cat;

public class CustCompanyCheckDTO extends CustCompanyInfoDTO {
	
	private String canReClassifyCat;
	private String resultCheck;
	private String dbInfo;
	
	
	public String getCanReClassifyCat() {
		return canReClassifyCat;
	}

	public void setCanReClassifyCat(String canReClassifyCat) {
		this.canReClassifyCat = canReClassifyCat;
	}

	public String getResultCheck() {
		return resultCheck;
	}

	public void setResultCheck(String resultCheck) {
		this.resultCheck = resultCheck;
	}

	public String getDbInfo() {
		return dbInfo;
	}

	public void setDbInfo(String dbInfo) {
		this.dbInfo = dbInfo;
	}

}
