package com.mcredit.model.object.mobile.dto;

import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;

public class CaseDTO {
	private Long id;
	private String createdDate;
	private Long appNumber;
	private Long creditAppId;
	private String customerName;
	private String citizenId;
	private String productName;
	private Long loanAmount;
	private Long loanTenor;
	private String hasInsurrance;
	private String tempResidence;
	private String kioskAddress;
	private String bpmStatus;
	private Clob cLobChecklist;
	private String checklist;
	
	// More info for case return
	private List<ReasonDTO> reasons;
	private List<PdfDTO> pdfFiles;
	
	public CaseDTO() {
		this.reasons = new ArrayList<ReasonDTO>();
		this.pdfFiles = new ArrayList<PdfDTO>();
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Long getAppNumber() {
		return appNumber;
	}
	
	public void setAppNumber(Long appNumber) {
		this.appNumber = appNumber;
	}

	public Long getCreditAppId() {
		return creditAppId;
	}

	public void setCreditAppId(Long creditAppId) {
		this.creditAppId = creditAppId;
	}

	public String getCustomerName() {
		return customerName;
	}
	
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getCitizenId() {
		return citizenId;
	}
	
	public void setCitizenId(String citizenId) {
		this.citizenId = citizenId;
	}
	
	public String getProductName() {
		return productName;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public Long getLoanAmount() {
		return loanAmount;
	}
	
	public void setLoanAmount(Long loanAmount) {
		this.loanAmount = loanAmount;
	}
	
	public Long getLoanTenor() {
		return loanTenor;
	}
	
	public void setLoanTenor(Long loanTenor) {
		this.loanTenor = loanTenor;
	}
	
	public String getHasInsurrance() {
		return hasInsurrance;
	}
	
	public void setHasInsurrance(String hasInsurrance) {
		this.hasInsurrance = hasInsurrance;
	}
	
	public String getTempResidence() {
		return tempResidence;
	}
	
	public void setTempResidence(String tempResidence) {
		this.tempResidence = tempResidence;
	}
	
	public String getKioskAddress() {
		return kioskAddress;
	}
	
	public void setKioskAddress(String kioskAddress) {
		this.kioskAddress = kioskAddress;
	}
	
	public String getBpmStatus() {
		return bpmStatus;
	}
	
	public void setBpmStatus(String bpmStatus) {
		this.bpmStatus = bpmStatus;
	}
	
	public List<ReasonDTO> getReasons() {
		return reasons;
	}

	public void setReasons(List<ReasonDTO> reasons) {
		this.reasons = reasons;
	}

	public Clob getcLobChecklist() {
		return cLobChecklist;
	}

	public void setcLobChecklist(Clob cLobChecklist) {
		this.cLobChecklist = cLobChecklist;
	}

	public String getChecklist() {
		return checklist;
	}

	public void setChecklist(String checklist) {
		this.checklist = checklist;
	}

	public List<PdfDTO> getPdfFiles() {
		return pdfFiles;
	}

	public void setPdfFiles(List<PdfDTO> pdfFiles) {
		this.pdfFiles = pdfFiles;
	}
}
