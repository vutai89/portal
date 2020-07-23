package com.mcredit.business.job.createCard.dto;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateCustomerAccountLinkResultDTO {

	@SerializedName("issue_result")
	@Expose
	private ArrayList<CreateCustomerAccountLinkItemsDTO> issue_result;

	@SerializedName("returnCode")
	@Expose
	private String returnCode;

	@SerializedName("returnMes")
	@Expose
	private String returnMes;

	private int httpStatusCode;

	private boolean status;

	public boolean getStatus() {
		return httpStatusCode >= 200 && httpStatusCode < 300;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMes() {
		return returnMes;
	}

	public void setReturnMes(String returnMes) {
		this.returnMes = returnMes;
	}

	public ArrayList<CreateCustomerAccountLinkItemsDTO> getIssue_result() {
		return issue_result;
	}

	public void setIssue_result(ArrayList<CreateCustomerAccountLinkItemsDTO> issue_result) {
		this.issue_result = issue_result;
	}
}
