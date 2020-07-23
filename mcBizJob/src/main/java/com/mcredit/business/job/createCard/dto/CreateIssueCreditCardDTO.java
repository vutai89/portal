package com.mcredit.business.job.createCard.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateIssueCreditCardDTO extends CreateCardDTO {
	@SerializedName("RequestId")
	@Expose
	private String requestId;
	@SerializedName("UserId")
	@Expose
	private String userId;
	@SerializedName("BatchId")
	@Expose
	private String batchId;
	@SerializedName("ClientType")
	@Expose
	private String clientType;
	@SerializedName("ClientNumber")
	@Expose
	private String clientNumber;
	@SerializedName("ClientName")
	@Expose
	private String clientName;
	@SerializedName("ClientRegNumber")
	@Expose
	private String clientRegNumber;
	@SerializedName("ClientRegDate")
	@Expose
	private String clientRegDate;
	@SerializedName("ClientRegPlace")
	@Expose
	private String clientRegPlace;
	@SerializedName("ClientPhoneNumber")
	@Expose
	private String clientPhoneNumber;
	@SerializedName("ClientEmail")
	@Expose
	private String clientEmail;
	@SerializedName("ClientBirthDate")
	@Expose
	private String clientBirthDate;
	@SerializedName("ClientGender")
	@Expose
	private String clientGender;
	@SerializedName("ClientAddress")
	@Expose
	private String clientAddress;
	@SerializedName("CardClass")
	@Expose
	private String cardClass;
	@SerializedName("CardLevel")
	@Expose
	private String cardLevel;
	@SerializedName("CardType")
	@Expose
	private String cardType;
	@SerializedName("CreditLimit")
	@Expose
	private String creditLimit;
	@SerializedName("EmbossedName")
	@Expose
	private String embossedName;
	@SerializedName("IssueFee")
	@Expose
	private String issueFee;
	@SerializedName("AnnualFee")
	@Expose
	private String annualFee;
	@SerializedName("InsureFee")
	@Expose
	private String insureFee;
	@SerializedName("InsureFeeAmount")
	@Expose
	private String insureFeeAmount;
	@SerializedName("RmMain")
	@Expose
	private String rmMain;
	@SerializedName("RmSub")
	@Expose
	private String rmSub;
	@SerializedName("CardReceivedAddress")
	@Expose
	private String cardReceivedAddress;
	@SerializedName("StatementDebitAccNo")
	@Expose
	private String statementDebitAccNo;
	@SerializedName("ChannelData")
	@Expose
	private String channelData;
	@SerializedName("CardId")
	@Expose
	private String cardId;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getClientNumber() {
		return clientNumber;
	}

	public void setClientNumber(String clientNumber) {
		this.clientNumber = clientNumber;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientRegNumber() {
		return clientRegNumber;
	}

	public void setClientRegNumber(String clientRegNumber) {
		this.clientRegNumber = clientRegNumber;
	}

	public String getClientRegDate() {
		return clientRegDate;
	}

	public void setClientRegDate(String clientRegDate) {
		this.clientRegDate = clientRegDate;
	}

	public String getClientRegPlace() {
		return clientRegPlace;
	}

	public void setClientRegPlace(String clientRegPlace) {
		this.clientRegPlace = clientRegPlace;
	}

	public String getClientPhoneNumber() {
		return clientPhoneNumber;
	}

	public void setClientPhoneNumber(String clientPhoneNumber) {
		this.clientPhoneNumber = clientPhoneNumber;
	}

	public String getClientEmail() {
		return clientEmail;
	}

	public void setClientEmail(String clientEmail) {
		this.clientEmail = clientEmail;
	}

	public String getClientBirthDate() {
		return clientBirthDate;
	}

	public void setClientBirthDate(String clientBirthDate) {
		this.clientBirthDate = clientBirthDate;
	}

	public String getClientGender() {
		return clientGender;
	}

	public void setClientGender(String clientGender) {
		this.clientGender = clientGender;
	}

	public String getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	public String getCardClass() {
		return cardClass;
	}

	public void setCardClass(String cardClass) {
		this.cardClass = cardClass;
	}

	public String getCardLevel() {
		return cardLevel;
	}

	public void setCardLevel(String cardLevel) {
		this.cardLevel = cardLevel;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
	}

	public String getEmbossedName() {
		return embossedName;
	}

	public void setEmbossedName(String embossedName) {
		this.embossedName = embossedName;
	}

	public String getIssueFee() {
		return issueFee;
	}

	public void setIssueFee(String issueFee) {
		this.issueFee = issueFee;
	}

	public String getAnnualFee() {
		return annualFee;
	}

	public void setAnnualFee(String annualFee) {
		this.annualFee = annualFee;
	}

	public String getInsureFee() {
		return insureFee;
	}

	public void setInsureFee(String insureFee) {
		this.insureFee = insureFee;
	}

	public String getInsureFeeAmount() {
		return insureFeeAmount;
	}

	public void setInsureFeeAmount(String insureFeeAmount) {
		this.insureFeeAmount = insureFeeAmount;
	}

	public String getRmMain() {
		return rmMain;
	}

	public void setRmMain(String rmMain) {
		this.rmMain = rmMain;
	}

	public String getRmSub() {
		return rmSub;
	}

	public void setRmSub(String rmSub) {
		this.rmSub = rmSub;
	}

	public String getCardReceivedAddress() {
		return cardReceivedAddress;
	}

	public void setCardReceivedAddress(String cardReceivedAddress) {
		this.cardReceivedAddress = cardReceivedAddress;
	}

	public String getStatementDebitAccNo() {
		return statementDebitAccNo;
	}

	public void setStatementDebitAccNo(String statementDebitAccNo) {
		this.statementDebitAccNo = statementDebitAccNo;
	}

	public String getChannelData() {
		return channelData;
	}

	public void setChannelData(String channelData) {
		this.channelData = channelData;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	@Override
	public String toString() {
		return "CreateIssueCard [" + (requestId != null ? "requestId=" + requestId + ", " : "")
				+ (userId != null ? "userId=" + userId + ", " : "")
				+ (batchId != null ? "batchId=" + batchId + ", " : "")
				+ (clientType != null ? "clientType=" + clientType + ", " : "")
				+ (clientNumber != null ? "clientNumber=" + clientNumber + ", " : "")
				+ (clientName != null ? "clientName=" + clientName + ", " : "")
				+ (clientRegNumber != null ? "clientRegNumber=" + clientRegNumber + ", " : "")
				+ (clientRegDate != null ? "clientRegDate=" + clientRegDate + ", " : "")
				+ (clientRegPlace != null ? "clientRegPlace=" + clientRegPlace + ", " : "")
				+ (clientPhoneNumber != null ? "clientPhoneNumber=" + clientPhoneNumber + ", " : "")
				+ (clientEmail != null ? "clientEmail=" + clientEmail + ", " : "")
				+ (clientBirthDate != null ? "clientBirthDate=" + clientBirthDate + ", " : "")
				+ (clientGender != null ? "clientGender=" + clientGender + ", " : "")
				+ (clientAddress != null ? "clientAddress=" + clientAddress + ", " : "")
				+ (cardClass != null ? "cardClass=" + cardClass + ", " : "")
				+ (cardLevel != null ? "cardLevel=" + cardLevel + ", " : "")
				+ (cardType != null ? "cardType=" + cardType + ", " : "")
				+ (creditLimit != null ? "creditLimit=" + creditLimit + ", " : "")
				+ (embossedName != null ? "embossedName=" + embossedName + ", " : "")
				+ (issueFee != null ? "issueFee=" + issueFee + ", " : "")
				+ (annualFee != null ? "annualFee=" + annualFee + ", " : "")
				+ (insureFee != null ? "insureFee=" + insureFee + ", " : "")
				+ (insureFeeAmount != null ? "insureFeeAmount=" + insureFeeAmount + ", " : "")
				+ (rmMain != null ? "rmMain=" + rmMain + ", " : "") + (rmSub != null ? "rmSub=" + rmSub + ", " : "")
				+ (cardReceivedAddress != null ? "cardReceivedAddress=" + cardReceivedAddress : "") + "]";
	}

	public String toJson() {
		return "{" + (requestId != null ? "\"RequestId\":\"" + requestId : "")
				+ (userId != null ? "\", \"UserId\":\"" + userId : "")
				+ (batchId != null ? "\", \"BatchId\":\"" + batchId : "")
				+ (clientType != null ? "\",\"ClientType\":\"" + clientType : "")
				+ (clientNumber != null ? "\", \"ClientNumber\":\"" + clientNumber : "")
				+ (clientName != null ? "\",\"ClientName\":\"" + clientName : "")
				+ (clientRegNumber != null ? "\", \"ClientRegNumber\":\"" + clientRegNumber : "")
				+ (clientRegDate != null ? "\",\"ClientRegDate\":\"" + clientRegDate : "")
				+ (clientRegPlace != null ? "\", \"ClientRegPlace\":\"" + clientRegPlace : "")
				+ (clientPhoneNumber != null ? "\",\"ClientPhoneNumber\":\"" + clientPhoneNumber : "")
				+ (clientEmail != null ? "\", \"ClientEmail\":\"" + clientEmail : "")
				+ (clientBirthDate != null ? "\",\"ClientBirthDate\":\"" + clientBirthDate : "")
				+ (clientGender != null ? "\", \"ClientGender\":\"" + clientGender : "")
				+ (clientAddress != null ? "\",\"ClientAddress\":\"" + clientAddress : "")
				+ (cardClass != null ? "\", \"CardClass\":\"" + cardClass : "")
				+ (cardLevel != null ? "\",\"CardLevel\":\"" + cardLevel : "")
				+ (cardType != null ? "\", \"CardType\":\"" + cardType : "")
				+ (creditLimit != null ? "\",\"CreditLimit\":\"" + creditLimit : "")
				+ (embossedName != null ? "\", \"EmbossedName\":\"" + embossedName : "")
				+ (issueFee != null ? "\",\"IssueFee\":\"" + issueFee : "")
				+ (annualFee != null ? "\", \"AnnualFee\":\"" + annualFee : "")
				+ (insureFee != null ? "\",\"InsureFee\":\"" + insureFee : "")
				+ (insureFeeAmount != null ? "\",\"InsureFeeAmount\":\"" + insureFeeAmount : "")
				+ (rmMain != null ? "\", \"RmMain\":\"" + rmMain : "")
				+ (rmSub != null ? "\",\"RmSub\":\"" + rmSub : "")
				+ (cardReceivedAddress != null ? "\",\"CardReceivedAddress\":\"" + cardReceivedAddress : "")
				+ (statementDebitAccNo != null ? "\",\"StatementDebitAccNo\":\"" + statementDebitAccNo : "")
				+ (channelData != null ? "\",\"ChannelData\":\"" + channelData : "") + "\"}";
	}
}
