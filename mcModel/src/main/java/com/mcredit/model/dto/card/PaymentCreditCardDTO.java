package com.mcredit.model.dto.card;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentCreditCardDTO {

	@SerializedName("CreditCardNo")
	@Expose
	private String creditCardNo;
	@SerializedName("Amount")
	@Expose
	private String amount;
	@SerializedName("FeeAmount")
	@Expose
	private String feeAmount;
	@SerializedName("RefId")
	@Expose
	private String refId;

	public String getCreditCardNo() {
		return creditCardNo;
	}

	public void setCreditCardNo(String creditCardNo) {
		this.creditCardNo = creditCardNo;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(String feeAmount) {
		this.feeAmount = feeAmount;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String toJson() {
		return "{\"CreditCardNo\":\"" + (creditCardNo != null ?   creditCardNo  : "")
				+ "\", \"Amount\":\"" + (amount != null ?  amount  : "")
				+ "\", \"RefId\":\"" + (refId != null ?  refId  : "")
				+ "\",\"FeeAmount\":\"" + (feeAmount != null ? feeAmount : "") + "\"}";
	}
}