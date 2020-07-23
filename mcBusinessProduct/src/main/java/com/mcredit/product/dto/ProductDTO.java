package com.mcredit.product.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ProductDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private String ccy;
	private String createdBy;
	private Date createdDate;
	private Date endEffDate;
	private String lastUpdatedBy;
	private Date lastUpdatedDate;
	private BigDecimal latePenaltyFee;
	private BigDecimal lateRateIndex;
	private BigDecimal maxLoanAmount;
	private BigDecimal maxQuantityCommodities;
	private BigDecimal maxTenor;
	private BigDecimal minLoanAmount;
	private BigDecimal minTenor;
	private BigDecimal preLiquidationFee;
	private Integer productCategoryId;
	private String productCode;
	private BigDecimal productGroupId;
	private String productName;
	private BigDecimal pti;
	private BigDecimal rateIndex;
	private String recordStatus;
	private Date startEffDate;
	private String status;
	private BigDecimal tenor;
	private String isCheckCat;
	private Long schemeGroupId;
	private String schemeGroupName;
	private String expMinValue;
	private String expMaxValue;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCcy() {
		return ccy;
	}
	public void setCcy(String ccy) {
		this.ccy = ccy;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getEndEffDate() {
		return endEffDate;
	}
	public void setEndEffDate(Date endEffDate) {
		this.endEffDate = endEffDate;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public BigDecimal getLatePenaltyFee() {
		return latePenaltyFee;
	}
	public void setLatePenaltyFee(BigDecimal latePenaltyFee) {
		this.latePenaltyFee = latePenaltyFee;
	}
	public BigDecimal getLateRateIndex() {
		return lateRateIndex;
	}
	public void setLateRateIndex(BigDecimal lateRateIndex) {
		this.lateRateIndex = lateRateIndex;
	}
	public BigDecimal getMaxLoanAmount() {
		return maxLoanAmount;
	}
	public void setMaxLoanAmount(BigDecimal maxLoanAmount) {
		this.maxLoanAmount = maxLoanAmount;
	}
	public BigDecimal getMaxQuantityCommodities() {
		return maxQuantityCommodities;
	}
	public void setMaxQuantityCommodities(BigDecimal maxQuantityCommodities) {
		this.maxQuantityCommodities = maxQuantityCommodities;
	}
	public BigDecimal getMaxTenor() {
		return maxTenor;
	}
	public void setMaxTenor(BigDecimal maxTenor) {
		this.maxTenor = maxTenor;
	}
	public BigDecimal getMinLoanAmount() {
		return minLoanAmount;
	}
	public void setMinLoanAmount(BigDecimal minLoanAmount) {
		this.minLoanAmount = minLoanAmount;
	}
	public BigDecimal getMinTenor() {
		return minTenor;
	}
	public void setMinTenor(BigDecimal minTenor) {
		this.minTenor = minTenor;
	}
	public BigDecimal getPreLiquidationFee() {
		return preLiquidationFee;
	}
	public void setPreLiquidationFee(BigDecimal preLiquidationFee) {
		this.preLiquidationFee = preLiquidationFee;
	}
	public Integer getProductCategoryId() {
		return productCategoryId;
	}
	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public BigDecimal getProductGroupId() {
		return productGroupId;
	}
	public void setProductGroupId(BigDecimal productGroupId) {
		this.productGroupId = productGroupId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public BigDecimal getPti() {
		return pti;
	}
	public void setPti(BigDecimal pti) {
		this.pti = pti;
	}
	public BigDecimal getRateIndex() {
		return rateIndex;
	}
	public void setRateIndex(BigDecimal rateIndex) {
		this.rateIndex = rateIndex;
	}
	public String getRecordStatus() {
		return recordStatus;
	}
	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}
	public Date getStartEffDate() {
		return startEffDate;
	}
	public void setStartEffDate(Date startEffDate) {
		this.startEffDate = startEffDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public BigDecimal getTenor() {
		return tenor;
	}
	public void setTenor(BigDecimal tenor) {
		this.tenor = tenor;
	}
	public String getIsCheckCat() {
		return isCheckCat;
	}
	public void setIsCheckCat(String isCheckCat) {
		this.isCheckCat = isCheckCat;
	}
	
	public Long getSchemeGroupId() {
		return schemeGroupId;
	}
	public void setSchemeGroupId(Long schemeGroupId) {
		this.schemeGroupId = schemeGroupId;
	}
	
	public String getSchemeGroupName() {
		return schemeGroupName;
	}
	public void setSchemeGroupName(String schemeGroupName) {
		this.schemeGroupName = schemeGroupName;
	}
	
	public String getExpMinValue() {
		return expMinValue;
	}
	public void setExpMinValue(String expMinValue) {
		this.expMinValue = expMinValue;
	}
	public String getExpMaxValue() {
		return expMaxValue;
	}
	public void setExpMaxValue(String expMaxValue) {
		this.expMaxValue = expMaxValue;
	}
}
