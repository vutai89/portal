package com.mcredit.business.job.cancelCaseBPM;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpsertTmpCancelCaseDE implements Serializable {

	private static final long serialVersionUID = -751102785355071300L;

	@SerializedName("contractNumber")
	@Expose
	private String contractNumber;
	@SerializedName("appID")
	@Expose
	private String appID;
	@SerializedName("appNumber")
	@Expose
	private String appNumber;
	@SerializedName("status")
	@Expose
	private String status;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public UpsertTmpCancelCaseDE(String contractNumber, String appID, String appNumber, String status) {
		super();
		this.contractNumber = contractNumber;
		this.appID = appID;
		this.appNumber = appNumber;
		this.status = status;
	}
}