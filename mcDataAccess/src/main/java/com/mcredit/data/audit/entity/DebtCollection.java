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
@Table(name="DEBT_COLLECTION")
public class DebtCollection implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	private String debtCollectionId;
	
	@Column(name = "PAYMENT_AMOUNT")
	private Long paymentAmount;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "PAYMENT_CHANNEL_CODE")
	private String paymentChannelCode;
	
	@Column(name = "COLLECTION_FEE")
	private Long collectionFee;
	
	@Column(name = "PARTNER_REF_ID")
	private String partnerRefid;
	
	@Column(name = "CONTRACT_NUMBER")
	private String contractNumber;
	
	@Column(name = "CANCEL")
	private Long cancel;

	public String getDebtCollectionId() {
		return debtCollectionId;
	}

	public void setDebtCollectionId(String debtCollectionId) {
		this.debtCollectionId = debtCollectionId;
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

	public Long getCollectionFee() {
		return collectionFee;
	}

	public void setCollectionFee(Long collectionFee) {
		this.collectionFee = collectionFee;
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

	public Long getCancel() {
		return cancel;
	}

	public void setCancel(Long cancel) {
		this.cancel = cancel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
