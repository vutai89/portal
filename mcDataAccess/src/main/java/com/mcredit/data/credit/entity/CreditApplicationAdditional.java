package com.mcredit.data.credit.entity;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the CREDIT_APP_ADDITIONAL database table.
 * 
 */
@Entity
@Table(name="CREDIT_APP_ADDITIONAL")
@NamedNativeQuery(name="CreditAppAdditional.nextSeq",query="select SEQ_CREDIT_APP_ADDITIONAL_ID.NEXTVAL from DUAL")
public class CreditApplicationAdditional implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@Column(name="ANNUAL_FEE_COLL")
	private BigDecimal annualFeeColl;

	@Column(name="ANNUAL_FEE_FREQUENCY")
	private Integer annualFeeFrequency;

	@Column(name="CARD_HOLDER_NAME")
	private String cardHolderName;

	@Column(name="CREATED_BY")
	private String createdBy;

	@CreationTimestamp
	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE", updatable=false)
	private Date createdDate;

	@Column(name="CREDIT_APP_ID")
	private Long creditAppId;

	@Column(name="DIFFERENCE_ANNUAL_FEE")
	private BigDecimal differenceAnnualFee;

	@Column(name="DIFFERENCE_INSU_FEE")
	private BigDecimal differenceInsuFee;

	@Column(name="DIFFERENCE_ISSUE_FEE")
	private BigDecimal differenceIssueFee;

	@Column(name="INSU_FEE_FREQUENCY")
	private Integer insuFeeFrequency;

	@Column(name="IS_IB_ALLOWED")
	private Integer isIbAllowed;

	@Column(name="ISSUE_FEE_COLL")
	private BigDecimal issueFeeColl;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Column(name="RECEIVE_CARD_ADDR")
	private Integer receiveCardAddr;

	@Column(name="RECORD_STATUS")
	private String recordStatus;
	
	@Column(name="INSU_FEE_COLL")
	private BigDecimal insuFeeColl;
	
	@UpdateTimestamp
	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
	private Date lastUpdatedDate;
	
	@Column(name="CARD_STATUS_ID")
	private Integer cardStatusId;
	
	@Column(name="CARD_INT_GROUP")
	private String cardIntGroup;
	
	@Column(name="CARD_LEVEL")
	private String cardLevel;

	public Integer getCardStatusId() {
		return cardStatusId;
	}

	public void setCardStatusId(Integer cardStatusId) {
		this.cardStatusId = cardStatusId;
	}

	public Long getId() {
		return id;
	}

	public BigDecimal getInsuFeeColl() {
		return insuFeeColl;
	}

	public void setInsuFeeColl(BigDecimal insuFeeColl) {
		this.insuFeeColl = insuFeeColl;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAnnualFeeFrequency() {
		return annualFeeFrequency;
	}

	public void setAnnualFeeFrequency(Integer annualFeeFrequency) {
		this.annualFeeFrequency = annualFeeFrequency;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
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

	public Long getCreditAppId() {
		return creditAppId;
	}

	public void setCreditAppId(Long creditAppId) {
		this.creditAppId = creditAppId;
	}

	public BigDecimal getDifferenceAnnualFee() {
		return differenceAnnualFee;
	}

	public void setDifferenceAnnualFee(BigDecimal differenceAnnualFee) {
		this.differenceAnnualFee = differenceAnnualFee;
	}

	public BigDecimal getDifferenceInsuFee() {
		return differenceInsuFee;
	}

	public void setDifferenceInsuFee(BigDecimal differenceInsuFee) {
		this.differenceInsuFee = differenceInsuFee;
	}

	public BigDecimal getDifferenceIssueFee() {
		return differenceIssueFee;
	}

	public void setDifferenceIssueFee(BigDecimal differenceIssueFee) {
		this.differenceIssueFee = differenceIssueFee;
	}

	public Integer getInsuFeeFrequency() {
		return insuFeeFrequency;
	}

	public void setInsuFeeFrequency(Integer insuFeeFrequency) {
		this.insuFeeFrequency = insuFeeFrequency;
	}

	public Integer getIsIbAllowed() {
		return isIbAllowed;
	}

	public void setIsIbAllowed(Integer isIbAllowed) {
		this.isIbAllowed = isIbAllowed;
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

	public Integer getReceiveCardAddr() {
		return receiveCardAddr;
	}

	public void setReceiveCardAddr(Integer receiveCardAddr) {
		this.receiveCardAddr = receiveCardAddr;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}


	public BigDecimal getAnnualFeeColl() {
		return annualFeeColl;
	}

	public void setAnnualFeeColl(BigDecimal annualFeeColl) {
		this.annualFeeColl = annualFeeColl;
	}

	public BigDecimal getIssueFeeColl() {
		return issueFeeColl;
	}

	public void setIssueFeeColl(BigDecimal issueFeeColl) {
		this.issueFeeColl = issueFeeColl;
	}
	
	public String getCardIntGroup() {
		return cardIntGroup;
	}

	public void setCardIntGroup(String cardIntGroup) {
		this.cardIntGroup = cardIntGroup;
	}

	public String getCardLevel() {
		return cardLevel;
	}

	public void setCardLevel(String cardLevel) {
		this.cardLevel = cardLevel;
	}

	public CreditApplicationAdditional() {
	}

}