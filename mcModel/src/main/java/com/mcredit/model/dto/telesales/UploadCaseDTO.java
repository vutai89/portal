package com.mcredit.model.dto.telesales;

import java.math.BigDecimal;
import java.util.Date;

public class UploadCaseDTO {
	
	private Long id;
	private String customerName;
	private String productCode;
	private String citizenId;
	private String issueDateCitizen;
	private String tempResidence;
	private BigDecimal loanTenor;
	private BigDecimal loanAmount;
	private Long hasInsurance;
	private String shopCode;
	private String mobileTSA;
	private String customerMobile;
	private Long uplCustomerId;
	private Long custIncome;
	private String custGender;
	private String custBirthday;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getCitizenId() {
		return citizenId;
	}

	public void setCitizenId(String citizenId) {
		this.citizenId = citizenId;
	}

	public String getIssueDateCitizen() {
		return issueDateCitizen;
	}

	public void setIssueDateCitizen(String issueDateCitizen) {
		this.issueDateCitizen = issueDateCitizen;
	}

	public String getTempResidence() {
		return tempResidence;
	}

	public void setTempResidence(String tempResidence) {
		this.tempResidence = tempResidence;
	}

	public BigDecimal getLoanTenor() {
		return loanTenor;
	}

	public void setLoanTenor(BigDecimal loanTenor) {
		this.loanTenor = loanTenor;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public Long getHasInsurance() {
		return hasInsurance;
	}

	public void setHasInsurance(Long hasInsurance) {
		this.hasInsurance = hasInsurance;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getMobileTSA() {
		return mobileTSA;
	}

	public void setMobileTSA(String mobileTSA) {
		this.mobileTSA = mobileTSA;
	}

	public String getCustomerMobile() {
		return customerMobile;
	}

	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}

	public Long getUplCustomerId() {
		return uplCustomerId;
	}

	public void setUplCustomerId(Long uplCustomerId) {
		this.uplCustomerId = uplCustomerId;
	}

	public Long getCustIncome() {
		return custIncome;
	}

	public void setCustIncome(Long custIncome) {
		this.custIncome = custIncome;
	}

	public String getCustGender() {
		return custGender;
	}

	public void setCustGender(String custGender) {
		this.custGender = custGender;
	}

	public String getCustBirthday() {
		return custBirthday;
	}

	public void setCustBirthday(String custBirthday) {
		this.custBirthday = custBirthday;
	}
	
}
