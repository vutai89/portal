package com.mcredit.model.telesale;

import java.io.Serializable;

public class ContractSearch implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5178178943025147686L;

	private String fromDate;
	
	private String toDate;
	
	private String identityNumber;
	
	private Long status;
	
	private String dsaTsaCode;
	
	private String caseId;

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getIdentityNumber() {
		return identityNumber;
	}

	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getDsaTsaCode() {
		return dsaTsaCode;
	}

	public void setDsaTsaCode(String dsaTsaCode) {
		this.dsaTsaCode = dsaTsaCode;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
}