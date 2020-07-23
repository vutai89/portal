package com.mcredit.model.dto.contract;

import java.io.Serializable;

public class LoanDepositCreditEntry implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LoanDepositCreditEntry() {
		this.setContractNumber("");
		this.setLDNumber("");
		this.setApproveDate("");
		this.setLastUpdatedDate("");
		this.setStatus("");
	}
	
	private String contractNumber;
	private String LDNumber;
	private String approveDate;
	private String lastUpdatedDate;
	private String status;

	// Getter Methods

	public String getContractNumber() {
		return contractNumber;
	}

	public String getLDNumber() {
		return LDNumber;
	}

	public String getApproveDate() {
		return approveDate;
	}

	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public void setLDNumber(String LDNumber) {
		this.LDNumber = LDNumber;
	}

	public void setApproveDate(String approveDate) {
		this.approveDate = approveDate;
	}

	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}