/**
 * 
 */
package com.mcredit.model.dto;

import java.util.Date;

/**
 * @author manhnt1.ho
 */
public class SendMarkApiDTO {
	
	private String returnCode;
	private Integer score;
	private String lastUpdateDate;
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public String getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	
	
}
