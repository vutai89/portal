package com.mcredit.model.object.mobile.dto;

/**
 * @author sonhv.ho
 * 
 * Route And Create Case to BPM result DTO
 * successCount: So luong xu li thanh cong
 * failCount: So luong xu li that bai
 * total: Tong so luong xu li
 */
public class RACCaseResultDTO {
	
	int successCount;
	int failCount;
	int total;
	public int getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}
	public int getFailCount() {
		return failCount;
	}
	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public RACCaseResultDTO(int successCount, int failCount, int total) {
		super();
		this.successCount = successCount;
		this.failCount = failCount;
		this.total = total;
	}
	
	public RACCaseResultDTO() {
		super();
	}
	
}
