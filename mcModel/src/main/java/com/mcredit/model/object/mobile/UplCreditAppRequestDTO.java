package com.mcredit.model.object.mobile;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UplCreditAppRequestDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8158475647276778264L;

	private Long id;

	private String recordStatus;

	private Date createdDate;

	private Date lastUpdatedDate;

	private String createdBy;

	private String lastUpdatedBy;

	private Long creditAppId;

	private Long appNumber;

	private String appId;

	private Long insuRate;

	private String saleCode;

	private Long saleId;
	
	private String appCode;

	private String customerName;

	private String citizenId;

	private Date issueDateCitizen;

	private String issuePlace;

	private Date birthDate;

	private Long productId;

	private String tempResidence;

	private BigDecimal loanTenor;

	private BigDecimal loanAmount;

	private Long hasInsurance;

	private String mobileImei;

	private Long appStatus;

	private Long previousStatus;

	private String shopCode;

	private String companyTaxNumber;

	private String status;

	private String checkList;

	private String abortReason;

	private String abortComment;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getCreditAppId() {
		return creditAppId;
	}

	public void setCreditAppId(Long creditAppId) {
		this.creditAppId = creditAppId;
	}

	public Long getAppNumber() {
		return appNumber;
	}

	public void setAppNumber(Long appNumber) {
		this.appNumber = appNumber;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Long getInsuRate() {
		return insuRate;
	}

	public void setInsuRate(Long insuRate) {
		this.insuRate = insuRate;
	}

	public String getSaleCode() {
		return saleCode;
	}

	public void setSaleCode(String saleCode) {
		this.saleCode = saleCode;
	}

	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
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

	public Date getIssueDateCitizen() {
		return issueDateCitizen;
	}

	public void setIssueDateCitizen(Date issueDateCitizen) {
		this.issueDateCitizen = issueDateCitizen;
	}

	public String getIssuePlace() {
		return issuePlace;
	}

	public void setIssuePlace(String issuePlace) {
		this.issuePlace = issuePlace;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getTempResidence() {
		return tempResidence;
	}

	public void setTempResidence(String tempResidence) {
		this.tempResidence = tempResidence;
	}

	public BigDecimal getLoanTenor() {
		return loanTenor;
	}

	public void setLoanTenor(BigDecimal loanTenor) {
		this.loanTenor = loanTenor;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public Long getHasInsurance() {
		return hasInsurance;
	}

	public void setHasInsurance(Long hasInsurance) {
		this.hasInsurance = hasInsurance;
	}

	public String getMobileImei() {
		return mobileImei;
	}

	public void setMobileImei(String mobileImei) {
		this.mobileImei = mobileImei;
	}

	public Long getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(Long appStatus) {
		this.appStatus = appStatus;
	}

	public Long getPreviousStatus() {
		return previousStatus;
	}

	public void setPreviousStatus(Long previousStatus) {
		this.previousStatus = previousStatus;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getCompanyTaxNumber() {
		return companyTaxNumber;
	}

	public void setCompanyTaxNumber(String companyTaxNumber) {
		this.companyTaxNumber = companyTaxNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCheckList() {
		return checkList;
	}

	public void setCheckList(String checkList) {
		this.checkList = checkList;
	}

	public String getAbortReason() {
		return abortReason;
	}

	public void setAbortReason(String abortReason) {
		this.abortReason = abortReason;
	}

	public String getAbortComment() {
		return abortComment;
	}

	public void setAbortComment(String abortComment) {
		this.abortComment = abortComment;
	}

}
