package com.mcredit.data.credit.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.UpdateTimestamp;


/**
 * The persistent class for the CREDIT_APP_OUTSTANDING database table.
 * 
 */
@Entity
@Table(name="CREDIT_APP_OUTSTANDING", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class CreditApplicationOutstanding implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@Column(name="CREDIT_APP_ID")
	private Long creditAppId;

	@Column(name="RECORD_STATUS")
	private String recordStatus;
	
	@UpdateTimestamp
	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
	private Date lastUpdatedDate;
	
	@Column(name="REMAIN_LIMIT")
	private BigDecimal remainLimit;
	
	@Column(name="CURRENT_BALANCE")
	private BigDecimal currentBalance;
	
	@Column(name="DUE_BALANCE")
	private BigDecimal dueBalance;
	
	@Column(name="NEXT_DUE_BALANCE")
	private BigDecimal nextDueBalance;
	
	@Column(name="MIN_PAYMENT_AMOUNT")
	private BigDecimal minPaymentAmount;
	
	@Column(name="REMAIN_MIN_AMOUNT")
	private BigDecimal ramainMinAmount;
	
	@Column(name="PRINCIPAL_AMOUNT")
	private BigDecimal principalAmount;
	
	@Column(name="DUE_INT_AMOUNT")
	private BigDecimal dueIntAmount;
	
	@Column(name="PENALTY_INT_AMOUNT")
	private BigDecimal penaltyIntAmount;
	
	@Column(name="FEE_AMOUNT")
	private BigDecimal feeAmount;
	
	@Column(name="OVERDUE_PRINCIPAL_AMOUNT")
	private BigDecimal overDuePrincipalAmount;
	
	@Column(name="DUE_FEE_AMOUNT")
	private BigDecimal dueFeeAmount;
	
	@Column(name="LOYALTY_AMOUNT")
	private Integer loyaltyAmount;
	
	@Column(name="PAYMENT_AMOUNT")
	private BigDecimal paymentAmount;
	
	/*@Column(name="SMS_STATUS")
	private String smsStatus;
	
	@Column(name="EMAIL_STATUS")
	private String emailStatus;*/

	public Long getId() {
		return id;
	}

	public Long getCreditAppId() {
		return creditAppId;
	}

	public void setCreditAppId(Long creditAppId) {
		this.creditAppId = creditAppId;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public BigDecimal getRemainLimit() {
		return remainLimit;
	}

	public void setRemainLimit(BigDecimal remainLimit) {
		this.remainLimit = remainLimit;
	}

	public BigDecimal getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}

	public BigDecimal getDueBalance() {
		return dueBalance;
	}

	public void setDueBalance(BigDecimal dueBalance) {
		this.dueBalance = dueBalance;
	}

	public BigDecimal getNextDueBalance() {
		return nextDueBalance;
	}

	public void setNextDueBalance(BigDecimal nextDueBalance) {
		this.nextDueBalance = nextDueBalance;
	}

	public BigDecimal getMinPaymentAmount() {
		return minPaymentAmount;
	}

	public void setMinPaymentAmount(BigDecimal minPaymentAmount) {
		this.minPaymentAmount = minPaymentAmount;
	}

	public BigDecimal getRamainMinAmount() {
		return ramainMinAmount;
	}

	public void setRamainMinAmount(BigDecimal ramainMinAmount) {
		this.ramainMinAmount = ramainMinAmount;
	}

	public BigDecimal getPrincipalAmount() {
		return principalAmount;
	}

	public void setPrincipalAmount(BigDecimal principalAmount) {
		this.principalAmount = principalAmount;
	}

	public BigDecimal getDueIntAmount() {
		return dueIntAmount;
	}

	public void setDueIntAmount(BigDecimal dueIntAmount) {
		this.dueIntAmount = dueIntAmount;
	}

	public BigDecimal getPenaltyIntAmount() {
		return penaltyIntAmount;
	}

	public void setPenaltyIntAmount(BigDecimal penaltyIntAmount) {
		this.penaltyIntAmount = penaltyIntAmount;
	}

	public BigDecimal getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}

	public BigDecimal getOverDuePrincipalAmount() {
		return overDuePrincipalAmount;
	}

	public void setOverDuePrincipalAmount(BigDecimal overDuePrincipalAmount) {
		this.overDuePrincipalAmount = overDuePrincipalAmount;
	}

	public BigDecimal getDueFeeAmount() {
		return dueFeeAmount;
	}

	public void setDueFeeAmount(BigDecimal dueFeeAmount) {
		this.dueFeeAmount = dueFeeAmount;
	}

	public Integer getLoyaltyAmount() {
		return loyaltyAmount;
	}

	public void setLoyaltyAmount(Integer loyaltyAmount) {
		this.loyaltyAmount = loyaltyAmount;
	}

	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	/*public String getSmsStatus() {
		return smsStatus;
	}

	public void setSmsStatus(String smsStatus) {
		this.smsStatus = smsStatus;
	}

	public String getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
	}*/

	public void setId(Long id) {
		this.id = id;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
}