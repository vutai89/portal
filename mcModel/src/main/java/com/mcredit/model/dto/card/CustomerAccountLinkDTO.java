package com.mcredit.model.dto.card;

import java.io.Serializable;
import java.util.Date;

public class CustomerAccountLinkDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2782804205251600700L;
	
	private Long id;
	private Long custId;
	private String linkType;
	private Integer linkSeq;
	private String recordStatus;
	private Date createdDate;
	private Date lastUpdatedDate;
	private String createdBy;
	private String lastUpdatedBy;
	private String linkValue;
	private String linkName;
	private String linkSystem;
	private String linkCurrency;
	private String linkProduct;
	private String custName;
	private String accountingCode;
	//private Integer partnerId;

	public CustomerAccountLinkDTO() {}
	
	public CustomerAccountLinkDTO(Long custId, String linkType, String recordStatus, String createdBy, String linkValue, String linkName, String linkSystem, String linkCurrency, String linkProduct) {
		this.custId = custId;
		this.linkType = linkType;
		this.recordStatus = recordStatus;
		this.createdBy = createdBy;
		this.linkValue = linkValue;
		this.linkName = linkName;
		this.linkSystem = linkSystem;
		this.linkCurrency = linkCurrency;
		this.linkProduct = linkProduct;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCustId() {
		return custId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public Integer getLinkSeq() {
		return linkSeq;
	}

	public void setLinkSeq(Integer linkSeq) {
		this.linkSeq = linkSeq;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getLinkValue() {
		return linkValue;
	}

	public void setLinkValue(String linkValue) {
		this.linkValue = linkValue;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getLinkSystem() {
		return linkSystem;
	}

	public void setLinkSystem(String linkSystem) {
		this.linkSystem = linkSystem;
	}

	public String getLinkCurrency() {
		return linkCurrency;
	}

	public void setLinkCurrency(String linkCurrency) {
		this.linkCurrency = linkCurrency;
	}

	public String getLinkProduct() {
		return linkProduct;
	}

	public void setLinkProduct(String linkProduct) {
		this.linkProduct = linkProduct;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getAccountingCode() {
		return accountingCode;
	}

	public void setAccountingCode(String accountingCode) {
		this.accountingCode = accountingCode;
	}

	/*public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}*/
}
