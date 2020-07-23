/**
 * 
 */
package com.mcredit.data.customer.entity;

/**
 * @author anhdv.ho
 *
 */
public class ResponseCustomer {
	
	private Long mcCustId;
	private String mcCustCode;
	
	public Long getMcCustId() {
		return mcCustId;
	}
	public void setMcCustId(Long mcCustId) {
		this.mcCustId = mcCustId;
	}
	public String getMcCustCode() {
		return mcCustCode;
	}
	public void setMcCustCode(String mcCustCode) {
		this.mcCustCode = mcCustCode;
	}
}
