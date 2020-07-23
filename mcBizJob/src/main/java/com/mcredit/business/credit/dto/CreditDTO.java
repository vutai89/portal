package com.mcredit.business.credit.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditDTO {

	private CreditApplicationAppraisalDTO appAppraisal;
	private CreditApplicationBPMDTO appBPM;
	private CreditApplicationLoanManagementDTO appLMN;
	private CreditApplicationRequestDTO appRequest;
	private CreditApplicationAdditionalDTO appAdditional;
	
	public CreditApplicationAdditionalDTO getAppAdditional() {
		return appAdditional;
	}
	public void setAppAdditional(CreditApplicationAdditionalDTO appAdditional) {
		this.appAdditional = appAdditional;
	}
	public CreditApplicationAppraisalDTO getAppAppraisal() {
		return appAppraisal;
	}
	public void setAppAppraisal(CreditApplicationAppraisalDTO appAppraisal) {
		this.appAppraisal = appAppraisal;
	}
	public CreditApplicationBPMDTO getAppBPM() {
		return appBPM;
	}
	public void setAppBPM(CreditApplicationBPMDTO appBPM) {
		this.appBPM = appBPM;
	}
	public CreditApplicationLoanManagementDTO getAppLMN() {
		return appLMN;
	}
	public void setAppLMN(CreditApplicationLoanManagementDTO appLMN) {
		this.appLMN = appLMN;
	}
	public CreditApplicationRequestDTO getAppRequest() {
		return appRequest;
	}
	public void setAppRequest(CreditApplicationRequestDTO appRequest) {
		this.appRequest = appRequest;
	}
}