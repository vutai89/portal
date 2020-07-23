package com.mcredit.model.object;


public class ProspectManager {

	private Long custProspectId;
	private String custName;
	private String prospectStatus;
	private String gender;
	private String province;
	private String provinceText;
	private String callDate;
	private String callStatus;
	private String callResult;
	private String nextAction;
	private Long uploadCustomerId;
	private String nextActionDate;
	private String loginId;
	private String note;
	private String productName;
	private String preProductName;
	private String udf01; 
	private String otpCode;
	private String tsCode;
	private Integer minScore;
	
	public String getPreProductName() {
		return preProductName;
	}
	public void setPreProductName(String preProductName) {
		this.preProductName = preProductName;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getNextActionDate() {
		return nextActionDate;
	}
	public void setNextActionDate(String nextActionDate) {
		this.nextActionDate = nextActionDate;
	}
	public Long getUploadCustomerId() {
		return uploadCustomerId;
	}
	public void setUploadCustomerId(Long uploadCustomerId) {
		this.uploadCustomerId = uploadCustomerId;
	}
	public String getProspectStatus() {
		return prospectStatus;
	}
	public void setProspectStatus(String prospectStatus) {
		this.prospectStatus = prospectStatus;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public Long getCustProspectId() {
		return custProspectId;
	}
	public void setCustProspectId(Long custProspectId) {
		this.custProspectId = custProspectId;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getCallDate() {
		return callDate;
	}
	public void setCallDate(String callDate) {
		this.callDate = callDate;
	}
	public String getCallStatus() {
		return callStatus;
	}
	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}
	public String getCallResult() {
		return callResult;
	}
	public void setCallResult(String callResult) {
		this.callResult = callResult;
	}
	public String getNextAction() {
		return nextAction;
	}
	public void setNextAction(String nextAction) {
		this.nextAction = nextAction;
	}
	public String getUdf01() {
		return udf01;
	}
	public void setUdf01(String udf01) {
		this.udf01 = udf01;
	}
	public String getOtpCode() {
		return otpCode;
	}   
	public void setOtpCode(String otpCode) {
		this.otpCode = otpCode;
	}
	public String getTsCode() {
		return tsCode;
	}
	public void setTsCode(String tsCode) {
		this.tsCode = tsCode;
	}
	public Integer getMinScore() {
		return minScore;
	}
	public void setMinScore(Integer minScore) {
		this.minScore = minScore;
	}
	public String getProvinceText() {
		return provinceText;
	}
	public void setProvinceText(String provinceText) {
		this.provinceText = provinceText;
	}
	
}