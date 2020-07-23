package com.mcredit.business.credit.dto;

import java.util.Date;


public class CreditApplicationLoanManagementDTO {
	private Long id;
	private String coreLnAppId;
	private String createdBy;
	private Long creditAppId;
	private Long disbursementAmount;
	private Date disbursementDate;
	private Integer disbursementStatus;
	private Date firstPaymentDate;
	private Date lastPaymentDate;
	private String lastUpdatedBy;
	private String recordStatus;
	private String disbursementStatusValue;
	
	public CreditApplicationLoanManagementDTO() {
	}
	
	public Long getId() {
		return id;
	}
	
	public String getDisbursementStatusValue() {
		return disbursementStatusValue;
	}

	public void setDisbursementStatusValue(String disbursementStatusValue) {
		this.disbursementStatusValue = disbursementStatusValue;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getCoreLnAppId() {
		return coreLnAppId;
	}
	public void setCoreLnAppId(String coreLnAppId) {
		this.coreLnAppId = coreLnAppId;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Long getCreditAppId() {
		return creditAppId;
	}
	public void setCreditAppId(Long creditAppId) {
		this.creditAppId = creditAppId;
	}
	public Long getDisbursementAmount() {
		return disbursementAmount;
	}
	public void setDisbursementAmount(Long disbursementAmount) {
		this.disbursementAmount = disbursementAmount;
	}
	public Date getDisbursementDate() {
		return disbursementDate;
	}
	public void setDisbursementDate(Date disbursementDate) {
		this.disbursementDate = disbursementDate;
	}
	public Integer getDisbursementStatus() {
		return disbursementStatus;
	}
	public void setDisbursementStatus(Integer disbursementStatus) {
		this.disbursementStatus = disbursementStatus;
	}
	public Date getFirstPaymentDate() {
		return firstPaymentDate;
	}
	public void setFirstPaymentDate(Date firstPaymentDate) {
		this.firstPaymentDate = firstPaymentDate;
	}
	public Date getLastPaymentDate() {
		return lastPaymentDate;
	}
	public void setLastPaymentDate(Date lastPaymentDate) {
		this.lastPaymentDate = lastPaymentDate;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public String getRecordStatus() {
		return recordStatus;
	}
	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}
}