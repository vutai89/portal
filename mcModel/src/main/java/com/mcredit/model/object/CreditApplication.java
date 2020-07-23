package com.mcredit.model.object;

import java.io.Serializable;

public class CreditApplication implements Serializable {

	// Lai suat khoan vay
	private float monIntRate;
	private float yearIntRate;
	// Ky han
	private int term;
	// Ma SP
	private String productCode;
	// Han muc vay de nghi
	private float creditLimitRequest;
	// PTI
	private float PTI;
	// Phi bao hiem
	private float insuranceRate;
	// Tra gop
	private Installment installment;
	// Khach hang
	private Customer customer;

	public float getMonIntRate() {
		return monIntRate;
	}
	public void setMonIntRate(float monIntRate) {
		this.monIntRate = monIntRate;
	}
	public float getYearIntRate() {
		return yearIntRate;
	}
	public void setYearIntRate(float yearIntRate) {
		this.yearIntRate = yearIntRate;
	}
	public int getTerm() {
		return term;
	}
	public void setTerm(int term) {
		this.term = term;
	}
	public Installment getInstallment() {
		return installment;
	}
	public void setInstallment(Installment installment) {
		this.installment = installment;
	}
	public String getStringTerm() {
		if(this.term < 10) {
			return "0" + Integer.toString(this.term);
		} else {
			return Integer.toString(this.term);
		}
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public float getCreditLimitRequest() {
		return creditLimitRequest;
	}
	public void setCreditLimitRequest(float creditLimitRequest) {
		this.creditLimitRequest = creditLimitRequest;
	}
	public float getPTI() {
		return PTI;
	}
	public void setPTI(float pTI) {
		PTI = pTI;
	}
	public float getInsuranceRate() {
		return insuranceRate;
	}
	public void setInsuranceRate(float insuranceRate) {
		this.insuranceRate = insuranceRate;
	}
	
}
