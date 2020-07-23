
package com.mcredit.model.object.cancelCaseBPM;

public class CancelCaseObject {

	private String caseStatus;
	private String caseNumber;
	private String caseId;
	private String contractNumber;
	private Integer delIndex;
	private String typeOfLoan;
	private Integer casegroup;

	public String saleEmail;
	public String SubjectEmail;
	public String contenEmail;
	
	public Integer errorCount;

	/**
	 * 
	 */
	public CancelCaseObject() {
		this.caseStatus = "";
		this.caseNumber = "";
		this.caseId = "";
		this.contractNumber = "";
		this.delIndex = 0;
		this.typeOfLoan = "";
		this.casegroup = 1;
		this.errorCount = 0;
	}

	public Integer getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}

	public String getCaseStatus() {
		return caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public Integer getDelIndex() {
		return delIndex;
	}

	public void setDelIndex(Integer delIndex) {
		this.delIndex = delIndex;
	}

	public String getTypeOfLoan() {
		return typeOfLoan;
	}

	public void setTypeOfLoan(String typeOfLoan) {
		this.typeOfLoan = typeOfLoan;
	}

	public String getSaleEmail() {
		return saleEmail;
	}

	public void setSaleEmail(String saleEmail) {
		this.saleEmail = saleEmail;
	}

	public String getSubjectEmail() {
		return SubjectEmail;
	}

	public void setSubjectEmail(String subjectEmail) {
		SubjectEmail = subjectEmail;
	}

	public String getContenEmail() {
		return contenEmail;
	}

	public void setContenEmail(String contenEmail) {
		this.contenEmail = contenEmail;
	}

	public Integer getCasegroup() {
		return casegroup;
	}

	public void setCasegroup(Integer casegroup) {
		this.casegroup = casegroup;
	}

}
