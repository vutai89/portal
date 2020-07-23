package com.mcredit.data.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


/**
 * The persistent class for the PRODUCTS database table.
 * 
 */
@Entity(name="Product")
@Table(name="PRODUCTS")
@NamedQuery(name="Product.findAll", query="SELECT p FROM Product p")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "seq_product" , strategy = "com.mcredit.data.common.entity.SequenceGenerate" , parameters = @Parameter(name = "seqName", value = "SEQ_PRODUCTS_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_product")
	private long id;

	@Column(name="CCY")
	private String ccy;

	@Column(name="CREATED_BY")
	private String createdBy;
	
	@CreationTimestamp
	@Column(name = "CREATED_DATE", updatable = false)
	private Date createdDate;

	@Temporal(TemporalType.DATE)
	@Column(name="END_EFF_DATE")
	private Date endEffDate;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
	private Date lastUpdatedDate;

	@Column(name="LATE_PENALTY_FEE")
	private BigDecimal latePenaltyFee;

	@Column(name="LATE_RATE_INDEX")
	private BigDecimal lateRateIndex;

	@Column(name="MAX_LOAN_AMOUNT")
	private BigDecimal maxLoanAmount;

	@Column(name="MAX_QUANTITY_COMMODITIES")
	private BigDecimal maxQuantityCommodities;

	@Column(name="MAX_TENOR")
	private BigDecimal maxTenor;

	@Column(name="MIN_LOAN_AMOUNT")
	private BigDecimal minLoanAmount;

	@Column(name="MIN_TENOR")
	private BigDecimal minTenor;

	@Column(name="PRE_LIQUIDATION_FEE")
	private BigDecimal preLiquidationFee;

	@Column(name="PRODUCT_CATEGORY_ID")
	private Integer productCategoryId;

	@Column(name="PRODUCT_CODE")
	private String productCode;

	@Column(name="PRODUCT_GROUP_ID")
	private BigDecimal productGroupId;

	@Column(name="PRODUCT_NAME")
	private String productName;

	private BigDecimal pti;

	@Column(name="RATE_INDEX")
	private BigDecimal rateIndex;

	@Column(name="RECORD_STATUS")
	private String recordStatus;

	@Temporal(TemporalType.DATE)
	@Column(name="START_EFF_DATE")
	private Date startEffDate;

	@Column(name="STATUS")
	private String status;

	private BigDecimal tenor;
	
	@Column(name="EXP_MIN_VALUE")
	private String expMinValue;
	
	@Column(name="EXP_MAX_VALUE")
	private String expMaxValue;
	
	@Column(name="IS_CHECK_CATEGORY")
	private String isCheckCat;
	
	public Product() {
	}

	public String getIsCheckCat() {
		return isCheckCat;
	}

	public void setIsCheckCat(String isCheckCat) {
		this.isCheckCat = isCheckCat;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCcy() {
		return this.ccy;
	}

	public void setCcy(String ccy) {
		this.ccy = ccy;
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

	public Date getEndEffDate() {
		return this.endEffDate;
	}

	public void setEndEffDate(Date endEffDate) {
		this.endEffDate = endEffDate;
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

	public BigDecimal getLatePenaltyFee() {
		return this.latePenaltyFee;
	}

	public void setLatePenaltyFee(BigDecimal latePenaltyFee) {
		this.latePenaltyFee = latePenaltyFee;
	}

	public BigDecimal getLateRateIndex() {
		return this.lateRateIndex;
	}

	public void setLateRateIndex(BigDecimal lateRateIndex) {
		this.lateRateIndex = lateRateIndex;
	}

	public BigDecimal getMaxLoanAmount() {
		return this.maxLoanAmount;
	}

	public void setMaxLoanAmount(BigDecimal maxLoanAmount) {
		this.maxLoanAmount = maxLoanAmount;
	}

	public BigDecimal getMaxQuantityCommodities() {
		return this.maxQuantityCommodities;
	}

	public void setMaxQuantityCommodities(BigDecimal maxQuantityCommodities) {
		this.maxQuantityCommodities = maxQuantityCommodities;
	}

	public BigDecimal getMaxTenor() {
		return this.maxTenor;
	}

	public void setMaxTenor(BigDecimal maxTenor) {
		this.maxTenor = maxTenor;
	}

	public BigDecimal getMinLoanAmount() {
		return this.minLoanAmount;
	}

	public void setMinLoanAmount(BigDecimal minLoanAmount) {
		this.minLoanAmount = minLoanAmount;
	}

	public BigDecimal getMinTenor() {
		return this.minTenor;
	}

	public void setMinTenor(BigDecimal minTenor) {
		this.minTenor = minTenor;
	}

	public BigDecimal getPreLiquidationFee() {
		return this.preLiquidationFee;
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
		return this.productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public BigDecimal getProductGroupId() {
		return this.productGroupId;
	}

	public void setProductGroupId(BigDecimal productGroupId) {
		this.productGroupId = productGroupId;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getPti() {
		return this.pti;
	}

	public void setPti(BigDecimal pti) {
		this.pti = pti;
	}

	public BigDecimal getRateIndex() {
		return this.rateIndex;
	}

	public void setRateIndex(BigDecimal rateIndex) {
		this.rateIndex = rateIndex;
	}

	public String getRecordStatus() {
		return this.recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Date getStartEffDate() {
		return this.startEffDate;
	}

	public void setStartEffDate(Date startEffDate) {
		this.startEffDate = startEffDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getTenor() {
		return this.tenor;
	}

	public void setTenor(BigDecimal tenor) {
		this.tenor = tenor;
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