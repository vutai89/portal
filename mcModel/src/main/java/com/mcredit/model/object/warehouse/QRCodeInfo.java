package com.mcredit.model.object.warehouse;

public class QRCodeInfo {
	private String contractNumber;
	private String appID;
	private String appNumber;
	private Integer version;
	private String signature;

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getAppID() {
		return appID;
	}

	public void setAppID(String appID) {
		this.appID = appID;
	}

	public String getAppNumber() {
		return appNumber;
	}

	public void setAppNumber(String appNumber) {
		this.appNumber = appNumber;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
	public QRCodeInfo(){
		super();
	}
	public QRCodeInfo(String contractNumber, String appID, String appNumber,
			Integer version, String signature) {
		super();
		this.contractNumber = contractNumber;
		this.appID = appID;
		this.appNumber = appNumber;
		this.version = version;
		this.signature = signature;
	}

}
