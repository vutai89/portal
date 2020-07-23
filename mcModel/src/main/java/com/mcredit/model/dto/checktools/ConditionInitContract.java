package com.mcredit.model.dto.checktools;

public class ConditionInitContract {
	
	private String productGroup;
	private String productCode;
	private String customerName;
	private String citizenId;
	private Long loanAmount;
	private Integer loanTenor;
	private Long customerIncome;
	private String dateOfBirth;				// ngay sinh (dd-MM-yyyy)
	private String gender;					// M/F
	private String address;					// dia chi thuong tru
	private Integer hasInsurance;
	private String dateRequestContract;		// ngay yeu cau cap tin dung (dd-MM-yyyy)
	private String appNumber;
	private String saleCode;
	
	public String getProductGroup() {
		return productGroup;
	}
	
	public void setProductGroup(String productGroup) {
		this.productGroup = productGroup;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCitizenId() {
		return citizenId;
	}

	public void setCitizenId(String citizenId) {
		this.citizenId = citizenId;
	}

	public Long getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(Long loanAmount) {
		this.loanAmount = loanAmount;
	}

	public Integer getLoanTenor() {
		return loanTenor;
	}

	public void setLoanTenor(Integer loanTenor) {
		this.loanTenor = loanTenor;
	}

	public Long getCustomerIncome() {
		return customerIncome;
	}

	public void setCustomerIncome(Long customerIncome) {
		this.customerIncome = customerIncome;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public Integer getHasInsurance() {
		return hasInsurance;
	}

	public void setHasInsurance(Integer hasInsurance) {
		this.hasInsurance = hasInsurance;
	}

	public String getDateRequestContract() {
		return dateRequestContract;
	}

	public void setDateRequestContract(String dateRequestContract) {
		this.dateRequestContract = dateRequestContract;
	}

	public String getAppNumber() {
		return appNumber;
	}

	public void setAppNumber(String appNumber) {
		this.appNumber = appNumber;
	}

	public String getSaleCode() {
		return saleCode;
	}

	public void setSaleCode(String saleCode) {
		this.saleCode = saleCode;
	}
	
}
