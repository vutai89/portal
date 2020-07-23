package com.mcredit.business.credit.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the CREDIT_APP_ADDITIONAL database table.
 * 
 */
public class CreditApplicationAdditionalDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Integer annualFeeColl;
	private Integer annualFeeFrequency;
	private String cardHolderName;
	private String createdBy;
	private Date createdDate;
	private Long creditAppId;
	private BigDecimal differenceAnnualFee;
	private BigDecimal differenceInsuFee;
	private BigDecimal differenceIssueFee;
	private Integer insuFeeFrequency;
	private Integer isIbAllowed;
	private Integer issueFeeColl;
	private String lastUpdatedBy;
	private Date lastUpdatedDate;
	private Integer receiveCardAddr;
	private Integer insuFeeColl;
	private String isIbAllowedValue;
	private String receiveCardAddrValue;
	private String issueFeeCollValue;
	private String annualFeeCollValue;
	private String annualFeeFrequencyValue;
	private String insuFeeCollValue;
	private String insuFeeFrequencyValue;
	private String recordStatus;
	private String cardIntGroup;
	private String cardLevel;

	public Integer getInsuFeeColl() {
		return insuFeeColl;
	}

	public void setInsuFeeColl(Integer integer) {
		this.insuFeeColl = integer;
	}

	public Long getId() {
		return id;
	}

	
	public String getIsIbAllowedValue() {
		return isIbAllowedValue;
	}

	public void setIsIbAllowedValue(String isIbAllowedValue) {
		this.isIbAllowedValue = isIbAllowedValue;
	}

	public String getReceiveCardAddrValue() {
		return receiveCardAddrValue;
	}

	public void setReceiveCardAddrValue(String receiveCardAddrValue) {
		this.receiveCardAddrValue = receiveCardAddrValue;
	}

	public String getIssueFeeCollValue() {
		return issueFeeCollValue;
	}

	public void setIssueFeeCollValue(String issueFeeCollValue) {
		this.issueFeeCollValue = issueFeeCollValue;
	}

	public String getAnnualFeeCollValue() {
		return annualFeeCollValue;
	}

	public void setAnnualFeeCollValue(String annualFeeCollValue) {
		this.annualFeeCollValue = annualFeeCollValue;
	}

	public String getAnnualFeeFrequencyValue() {
		return annualFeeFrequencyValue;
	}

	public void setAnnualFeeFrequencyValue(String annualFeeFrequencyValue) {
		this.annualFeeFrequencyValue = annualFeeFrequencyValue;
	}

	public String getInsuFeeCollValue() {
		return insuFeeCollValue;
	}

	public void setInsuFeeCollValue(String insuFeeCollValue) {
		this.insuFeeCollValue = insuFeeCollValue;
	}

	public String getInsuFeeFrequencyValue() {
		return insuFeeFrequencyValue;
	}

	public void setInsuFeeFrequencyValue(String insuFeeFrequencyValue) {
		this.insuFeeFrequencyValue = insuFeeFrequencyValue;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public Integer getAnnualFeeColl() {
		return annualFeeColl;
	}

	public void setAnnualFeeColl(Integer integer) {
		this.annualFeeColl = integer;
	}

	public Integer getIssueFeeColl() {
		return issueFeeColl;
	}

	public void setIssueFeeColl(Integer integer) {
		this.issueFeeColl = integer;
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

	public CreditApplicationAdditionalDTO() {
	}

}