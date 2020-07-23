package com.mcredit.data.audit.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="DISBURSEMENT")
public class Disbursement implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	private String disbursementId;
	
	@Column(name = "PAYMENT_AMOUNT")
	private Long paymentAmount;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "PAYMENT_CHANNEL_CODE")
	private String paymentChannelCode;
	
	@Column(name = "DISBURSEMENT_FEE")
	private Long disbursementFee;
	
	@Column(name = "PARTNER_REF_ID")
	private String partnerRefid;
	
	@Column(name = "CONTRACT_NUMBER")
	private String contractNumber;
	
	@Column(name = "PAYMENT_CODE")
	private String paymentCode;

	@Column(name = "DEBIT_ACCOUNT")
	private String debitAccount;
	
	@Column(name = "DISBURSEMENT_DATE")
	private String disbursementDate;

	public String getDisbursementId() {
		return disbursementId;
	}

	public void setDisbursementId(String disbursementId) {
		this.disbursementId = disbursementId;
	}

	public Long getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(Long paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getPaymentChannelCode() {
		return paymentChannelCode;
	}

	public void setPaymentChannelCode(String paymentChannelCode) {
		this.paymentChannelCode = paymentChannelCode;
	}

	public Long getDisbursementFee() {
		return disbursementFee;
	}

	public void setDisbursementFee(Long disbursementFee) {
		this.disbursementFee = disbursementFee;
	}

	public String getPartnerRefid() {
		return partnerRefid;
	}

	public void setPartnerRefid(String partnerRefid) {
		this.partnerRefid = partnerRefid;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}

	public String getDebitAccount() {
		return debitAccount;
	}

	public void setDebitAccount(String debitAccount) {
		this.debitAccount = debitAccount;
	}

	public String getDisbursementDate() {
		return disbursementDate;
	}

	public void setDisbursementDate(String disbursementDate) {
		this.disbursementDate = disbursementDate;
	}
	
}
