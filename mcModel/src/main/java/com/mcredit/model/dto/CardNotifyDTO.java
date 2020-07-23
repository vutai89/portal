package com.mcredit.model.dto;

public class CardNotifyDTO {
	Long dueBalance;
	Long remainMinBalance;
	String cardId;
	String mobileNumber;
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public Long getDueBalance() {
		return dueBalance;
	}
	public void setDueBalance(Long dueBalance) {
		this.dueBalance = dueBalance;
	}
	public Long getRemainMinBalance() {
		return remainMinBalance;
	}
	public void setRemainMinBalance(Long remainMinBalance) {
		this.remainMinBalance = remainMinBalance;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	} 
}
