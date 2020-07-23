package com.mcredit.business.customer.dto;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import com.mcredit.sharedbiz.validation.DateTimeDeserialize;

public class CustomerIdentityDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8500891632624777123L;
	
	private Long id;
	private String createdBy;
	private Date createdDate;
	private Long custId;
	
	@JsonDeserialize(using=DateTimeDeserialize.class)
	private Date identityExpiryDate;
	
	@JsonDeserialize(using=DateTimeDeserialize.class)
	private Date identityIssueDate;
	
	private Integer identityIssuePlace;
	private String identityIssuePlaceValue;
	private String identityNumber;
	private Integer identityTypeId;
	private String identityTypeIdValue;
	private String lastUpdatedBy;
	private Date lastUpdatedDate;
	private String recordStatus;
	private String codeName;

	public CustomerIdentityDTO() {
	}

	public String getIdentityIssuePlaceValue() {
		return identityIssuePlaceValue;
	}

	public void setIdentityIssuePlaceValue(String identityIssuePlaceValue) {
		this.identityIssuePlaceValue = identityIssuePlaceValue;
	}

	public String getIdentityTypeIdValue() {
		return identityTypeIdValue;
	}

	public void setIdentityTypeIdValue(String identityTypeIdValue) {
		this.identityTypeIdValue = identityTypeIdValue;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getCustId() {
		return this.custId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
	}

	public Date getIdentityExpiryDate() {
		return this.identityExpiryDate;
	}

	public void setIdentityExpiryDate(Date identityExpiryDate) {
		this.identityExpiryDate = identityExpiryDate;
	}

	public Date getIdentityIssueDate() {
		return this.identityIssueDate;
	}

	public void setIdentityIssueDate(Date identityIssueDate) {
		this.identityIssueDate = identityIssueDate;
	}

	public Integer getIdentityIssuePlace() {
		return this.identityIssuePlace;
	}

	public void setIdentityIssuePlace(Integer identityIssuePlace) {
		this.identityIssuePlace = identityIssuePlace;
	}

	public String getIdentityNumber() {
		return this.identityNumber;
	}

	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}

	public Integer getIdentityTypeId() {
		return this.identityTypeId;
	}

	public void setIdentityTypeId(Integer identityTypeId) {
		this.identityTypeId = identityTypeId;
	}

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdatedDate() {
		return this.lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getRecordStatus() {
		return this.recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

}