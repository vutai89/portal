package com.mcredit.data.credit.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the CREDIT_APP_REQUEST database table.
 * 
 */
@Entity
@Table(name="CREDIT_APP_REQUEST", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
@NamedQuery(name="CreditApplicationRequest.findAll", query="SELECT o FROM CreditApplicationRequest o")
@NamedNativeQuery(name="CreditAppRequest.nextSeq",query="select SEQ_CREDIT_APP_REQUEST_ID.NEXTVAL from DUAL")
public class CreditApplicationRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@Column(name="APPLY_TO_OBJECT")
	private Integer applyToObject;

	@Temporal(TemporalType.DATE)
	@Column(name="CONTRACT_DATE")
	private Date contractDate;

	@Column(name="CREATED_BY")
	private String createdBy;

	@CreationTimestamp
	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE", updatable=false)
	private Date createdDate;

	@Column(name="CUST_ID")
	private Long custId;

	@Column(name="DISBURSEMENT_CHANNEL")
	private Integer disbursementChannel;

	@Column(name="DISBURSEMENT_METHOD")
	private BigDecimal disbursementMethod;

	@Column(name="DUPLICATED_NOTE")
	private String duplicatedNote;

	@Column(name="HAS_INSURANCE")
	private Integer hasInsurance;

	@Column(name="INSU_AMOUNT")
	private BigDecimal insuAmount;

	@Column(name="INSU_COMPANY")
	private Integer insuCompany;

	@Column(name="INSU_FEE")
	private BigDecimal insuFee;

	@Column(name="INSU_RATE")
	private BigDecimal insuRate;

	@Column(name="INSU_STAFFID")
	private Integer insuStaffid;

	@Column(name="INSU_TYPE")
	private Integer insuType;

	@Column(name="INT_RATE")
	private BigDecimal intRate;

	@Column(name="IS_DUPLICATED")
	private Integer isDuplicated;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@UpdateTimestamp
	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
	private Date lastUpdatedDate;

	@Column(name="LN_AMOUNT")
	private BigDecimal lnAmount;

	@Column(name="LN_AMOUNT_MINUS_INSU")
	private BigDecimal lnAmountMinusInsu;

	@Temporal(TemporalType.DATE)
	@Column(name="LN_END_DATE")
	private Date lnEndDate;

	@Column(name="LN_OTHER_PURPOSE")
	private String lnOtherPurpose;

	@Column(name="LN_PURPOSE")
	private Integer lnPurpose;

	@Temporal(TemporalType.DATE)
	@Column(name="LN_START_DATE")
	private Date lnStartDate;

	@Column(name="LN_TENOR")
	private Integer lnTenor;

	@Column(name="MC_CONTRACT_NUMBER")
	private String mcContractNumber;

	@Temporal(TemporalType.DATE)
	@Column(name="PAYMENT_DATE")
	private Date paymentDate;

	@Column(name="PRODUCT_ID")
	private Integer productId;

	@Column(name="RATE_INDEX")
	private Integer rateIndex;

	@Column(name="RECORD_STATUS")
	private String recordStatus;

	@Column(name="SALE_ID")
	private Long saleId;

	@Column(name="\"SCOPE\"")
	private Integer scope;

	private String status;

	@Column(name="TRANS_OFFICE_ID")
	private Integer transOfficeId;

	public CreditApplicationRequest() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getApplyToObject() {
		return applyToObject;
	}

	public void setApplyToObject(Integer applyToObject) {
		this.applyToObject = applyToObject;
	}

	public Date getContractDate() {
		return contractDate;
	}

	public void setContractDate(Date contractDate) {
		this.contractDate = contractDate;
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

	public Long getCustId() {
		return custId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
	}

	public Integer getDisbursementChannel() {
		return disbursementChannel;
	}

	public void setDisbursementChannel(Integer disbursementChannel) {
		this.disbursementChannel = disbursementChannel;
	}

	public BigDecimal getDisbursementMethod() {
		return disbursementMethod;
	}

	public void setDisbursementMethod(BigDecimal disbursementMethod) {
		this.disbursementMethod = disbursementMethod;
	}

	public String getDuplicatedNote() {
		return duplicatedNote;
	}

	public void setDuplicatedNote(String duplicatedNote) {
		this.duplicatedNote = duplicatedNote;
	}

	public BigDecimal getInsuAmount() {
		return insuAmount;
	}

	public void setInsuAmount(BigDecimal insuAmount) {
		this.insuAmount = insuAmount;
	}

	public BigDecimal getInsuFee() {
		return insuFee;
	}

	public void setInsuFee(BigDecimal insuFee) {
		this.insuFee = insuFee;
	}

	public BigDecimal getInsuRate() {
		return insuRate;
	}

	public void setInsuRate(BigDecimal insuRate) {
		this.insuRate = insuRate;
	}

	public Integer getInsuStaffid() {
		return insuStaffid;
	}

	public void setInsuStaffid(Integer insuStaffid) {
		this.insuStaffid = insuStaffid;
	}

	public Integer getInsuType() {
		return insuType;
	}

	public void setInsuType(Integer insuType) {
		this.insuType = insuType;
	}

	public BigDecimal getIntRate() {
		return intRate;
	}

	public void setIntRate(BigDecimal intRate) {
		this.intRate = intRate;
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

	public BigDecimal getLnAmount() {
		return lnAmount;
	}

	public void setLnAmount(BigDecimal lnAmount) {
		this.lnAmount = lnAmount;
	}

	public BigDecimal getLnAmountMinusInsu() {
		return lnAmountMinusInsu;
	}

	public void setLnAmountMinusInsu(BigDecimal lnAmountMinusInsu) {
		this.lnAmountMinusInsu = lnAmountMinusInsu;
	}

	public Date getLnEndDate() {
		return lnEndDate;
	}

	public void setLnEndDate(Date lnEndDate) {
		this.lnEndDate = lnEndDate;
	}

	public String getLnOtherPurpose() {
		return lnOtherPurpose;
	}

	public void setLnOtherPurpose(String lnOtherPurpose) {
		this.lnOtherPurpose = lnOtherPurpose;
	}

	public Integer getLnPurpose() {
		return lnPurpose;
	}

	public void setLnPurpose(Integer lnPurpose) {
		this.lnPurpose = lnPurpose;
	}

	public Date getLnStartDate() {
		return lnStartDate;
	}

	public void setLnStartDate(Date lnStartDate) {
		this.lnStartDate = lnStartDate;
	}

	public Integer getLnTenor() {
		return lnTenor;
	}

	public void setLnTenor(Integer lnTenor) {
		this.lnTenor = lnTenor;
	}

	public String getMcContractNumber() {
		return mcContractNumber;
	}

	public void setMcContractNumber(String mcContractNumber) {
		this.mcContractNumber = mcContractNumber;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getRateIndex() {
		return rateIndex;
	}

	public void setRateIndex(Integer rateIndex) {
		this.rateIndex = rateIndex;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}

	public Integer getScope() {
		return scope;
	}

	public void setScope(Integer scope) {
		this.scope = scope;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getTransOfficeId() {
		return transOfficeId;
	}

	public void setTransOfficeId(Integer transOfficeId) {
		this.transOfficeId = transOfficeId;
	}

	public Integer getHasInsurance() {
		return hasInsurance;
	}

	public void setHasInsurance(Integer hasInsurance) {
		this.hasInsurance = hasInsurance;
	}

	public Integer getInsuCompany() {
		return insuCompany;
	}

	public void setInsuCompany(Integer insuCompany) {
		this.insuCompany = insuCompany;
	}

	public Integer getIsDuplicated() {
		return isDuplicated;
	}

	public void setIsDuplicated(Integer isDuplicated) {
		this.isDuplicated = isDuplicated;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}