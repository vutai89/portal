package com.mcredit.business.telesales.object;

/**
 * @author manhnt1.ho
 * Class nay tao ra de hung ket qua duoc tra ve tu API
 */
public class ScoringTsResponse {
	private String returnCode;
	private String returnMes;
	private String dateTime;
	private String apiRequestId;
	private Long scores;
	private String verifyInfo;
	private String type;
	private Integer status;
	
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
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getApiRequestId() {
		return apiRequestId;
	}
	public void setApiRequestId(String apiRequestId) {
		this.apiRequestId = apiRequestId;
	}
	public Long getScores() {
		return scores;
	}
	public void setScores(Long scores) {
		this.scores = scores;
	}
	public String getVerifyInfo() {
		return verifyInfo;
	}
	public void setVerifyInfo(String verifyInfo) {
		this.verifyInfo = verifyInfo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
