package com.mcredit.model.dto.card;

import java.math.BigDecimal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentDTO {

	@SerializedName("amount")
	@Expose
	private BigDecimal amount;

	@SerializedName("postingGroup")
	@Expose
	private String postingGroup;

	@SerializedName("partnerRefId")
	@Expose
	private String partnerRefId;

	@SerializedName("partnerCode")
	@Expose
	private String partnerCode;

	@SerializedName("cardId")
	@Expose
	private String cardId;
	
	public String getPostingGroup() {
		return postingGroup;
	}
	public void setPostingGroup(String postingGroup) {
		this.postingGroup = postingGroup;
	}
	public String getPartnerRefId() {
		return partnerRefId;
	}
	public void setPartnerRefId(String partnerRefId) {
		this.partnerRefId = partnerRefId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getPartnerCode() {
		return partnerCode;
	}
	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
}