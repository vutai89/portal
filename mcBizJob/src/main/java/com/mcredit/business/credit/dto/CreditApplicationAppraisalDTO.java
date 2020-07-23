package com.mcredit.business.credit.dto;

import java.math.BigDecimal;
import java.util.Date;


public class CreditApplicationAppraisalDTO {

	private Long id;
	private BigDecimal approvedAmount;
	private Date approvedDate;
	private Integer approvedUser;
	private String createdBy;
	private Long creditAppId;
	private String lastUpdatedBy;
	private String recordStatus;
	private Integer industry;
	private String industryValue;

	public CreditApplicationAppraisalDTO() {
	}

	public Long getId() {
		return this.id;
	}

	public String getIndustryValue() {
		return industryValue;
	}

	public void setIndustryValue(String industryValue) {
		this.industryValue = industryValue;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getApprovedAmount() {
		return this.approvedAmount;
	}

	public void setApprovedAmount(BigDecimal approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

	public Date getApprovedDate() {
		return this.approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public Integer getApprovedUser() {
		return this.approvedUser;
	}

	public void setApprovedUser(Integer approvedUser) {
		this.approvedUser = approvedUser;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Long getCreditAppId() {
		return this.creditAppId;
	}

	public void setCreditAppId(Long creditAppId) {
		this.creditAppId = creditAppId;
	}

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getRecordStatus() {
		return this.recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Integer getIndustry() {
		return industry;
	}

	public void setIndustry(Integer industry) {
		this.industry = industry;
	}

}