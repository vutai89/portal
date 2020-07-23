package com.mcredit.business.telesales.object;

/**
 * @author manhnt1.ho
 * Class nay tao ra de hung ket qua duoc tra ve tu API
 */
public class ScoringComputeResponse {
	private String returnCode;
	private String returnMes;
	private String dateTime;
	private String apiRequestId;
	
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
	
}
