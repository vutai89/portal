package com.mcredit.model.dto.appraisal;

import java.util.List;

import com.google.gson.Gson;

public class AppraisalDataDetailDTO {
	
	private Integer appNumber;					// bpm app number (bpm M-FIX)
	private Long productId;						// 
	private String productCode;					// (bpm M-FIX)
	private String productName;					// (bpm M-FIX)
	private String productGroup;				// nhom san pham: InstallmentLoan, CashLoan, ConcentratingDataEntry (bpm M-FIX)
	
	private String customerName;				// (bpm M-FIX)
	private String typeOfDocPerson;				// loai giay to tuy than (CMND/CCCD, CMQD) (bpm FIX, (bpm nullable))
	private String citizenID;					// CMND/CCCD (bpm M-FIX)
	private String citizenIDDate;				// ngay cap CMND/CCCD/CMQD (bpm M-FIX)
	private String militaryID;					// CMQD (bpm FIX)
	private String militaryIDDate;				// ngay cap CMQD (bpm FIX)
	
	private Integer loanAmount;					// so tien vay (so tien vay de xuat bao gom ca tien phi bao hiem khi action la CALL (bpm M)
												//				so tien vay phe duyet bao gom ca tien phi bao hiem khi action la APPROVE) (bpm M)
	private Double loanAmountAfterInsurance;	// so tien KH thuc nhan (bpm M)
	private Integer loanTenor;					// ki han vay (thang) (bpm M)
	
	
	private String createdDate;					// ngay khoi tao ho so	(bpm M-FIX)
	private String appraisalDate;				// ngay tham dinh, ngay mo tool gan nhat
	private String birthDate;					// ngay sinh khach hang (bpm M-FIX)
	private String salaryPaymentType;			// hinh thuc nhan luong (chuyen khoan, tien mat, ..): 1, 2, 3 (bpm FIX)
	private String custCompanyCat;				// ket qua phan loai: CAT A, CAT B, CAT C (bpm FIX)
	private Double downPaymentRate;				// ti le tra truoc
	private List<GoodsInformation> goodsInformation;	// Danh sach hang hoa (bpm FIX)
	private Double monthIncome1;				// thu nhap thang 1
	private Double monthIncome2;				// thu nhap thang 2
	private Double monthIncome3;				// thu nhap thang 3
	private Double goodsPrice;					// gia tri hang hoa (GOODS_VALIDATION) (bpm FIX)
	private Integer numberOfRelationFinanceCompany;	// so cong ty tai chinh co quan he (bpm M (bpm nullable))
	private Integer numberOfRelationBank;		// so ngan hang co quan he (bpm M (bpm nullable))
	private Integer numberOfRelationOrganize;	// so TCTC co quan he
	private Double totalOutstandingLoanCIC;		// tong du no cua khach hang tai CIC
	private Double pti;							// PTI
	private Double dti;							// DTI
	private Double electricBillM1;				// hoa don dien thang 1
	private Double electricBillM2;				// hoa don dien thang 2
	private Double electricBillM3;				// hoa don dien thang 3
	private Double averageElectricBill;			// hoa don dien trung binh 
	private Double accountBalanceMinM1;			// so du tai khoan min thang 1
	private Double accountBalanceMaxM1;			// so du tai khoan max thang 1
	private Double accountBalanceMinM2;			// so du tai khoan min thang 2
	private Double accountBalanceMaxM2;			// so du tai khoan max thang 2
	private Double accountBalanceMinM3;			// so du tai khoan min thang 3
	private Double accountBalanceMaxM3;			// so du tai khoan max thang 3
	private Double accountBalanceMinM4;			// so du tai khoan min thang 4
	private Double accountBalanceMaxM4;			// so du tai khoan max thang 4
	private Double accountBalanceMinM5;			// so du tai khoan min thang 5
	private Double accountBalanceMaxM5;			// so du tai khoan max thang 5
	private Double accountBalanceMinM6;			// so du tai khoan min thang 6
	private Double accountBalanceMaxM6;			// so du tai khoan max thang 6
	private Double averageAccountBalance;		// so du tai khoan trung binh
	private Double customerIncome;				// thu nhap khach hang (bpm M, (bpm nullable))
	private Double customerIncomeInterpolate;	// thu nhap khach hang (noi suy)
	private Double customerIncomeAppraisal;		// thu nhap khach hang (tham dinh)
	private String hasInsurance;				// co tham gia bao hiem khong (bpm M-FIX)
	private String insuranceTerm;				// dinh ki dong phi bao hiem: MONTH, YEAR, ... (bpm FIX)
	private Long insuranceTermFee;				// phi bao hiem dinh ki	(bpm FIX (bpm nullable))
	private String insuranceTermOther;			// so thang dinh ki dong phi khi insuranceTerm = OTHER (bpm FIX (bpm nullable))
	private Double annualFeeLifeInsurance;		// phi BHNT hang nam
	private Double insuranceFee;				// so tien phi bao hiem (bpm FIX)
	private Double interestRate;				// lai suat (bpm M-FIX)
	private Double emi;							// EMI so tien khach hang tra hang thang
	private Double totalOutstandingLoan;		// tong du no khach hang
	
	private Double finalEMI;
	
	private Boolean result;
	
	private Double input_1a;
	private Double input_1b;
	private Double input_2;
	private Double input_3a;
	private Double input_3b;
	private Double input_3c;
	private Double input_4a;
	private Double input_4b;
	private Double input_4c;
	private Double input_11a;
	private Double input_11b;
	private Double input_12;
	private Double input_13a;
	private Double input_13b;
	private Double input_13c;
	private Double input_14a;
	private Double input_14b;
	private Double input_16a;
	private Double input_16b;
	private Double input_17;
	private Double input_18a;
	private Double input_18b;
	private Double input_18c;
	private Double input_19a;
	private Double input_19b;
	private Double input_19c;
	
	private Double remaining_term_1a;
	private Double remaining_term_1b;
	private Double remaining_term_2;
	private Double remaining_term_3a;
	private Double remaining_term_3b;
	private Double remaining_term_3c;
	private Double remaining_term_4a;
	private Double remaining_term_4b;
	private Double remaining_term_4c;
	
	public Integer getAppNumber() {
		return appNumber;
	}

	public void setAppNumber(Integer appNumber) {
		this.appNumber = appNumber;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductGroup() {
		return productGroup;
	}

	public void setProductGroup(String productGroup) {
		this.productGroup = productGroup;
	}

	public Integer getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(Integer loanAmount) {
		this.loanAmount = loanAmount;
	}

	public Double getLoanAmountAfterInsurance() {
		return loanAmountAfterInsurance;
	}

	public void setLoanAmountAfterInsurance(Double loanAmountAfterInsurance) {
		this.loanAmountAfterInsurance = loanAmountAfterInsurance;
	}

	public Integer getLoanTenor() {
		return loanTenor;
	}

	public void setLoanTenor(Integer loanTenor) {
		this.loanTenor = loanTenor;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getSalaryPaymentType() {
		return salaryPaymentType;
	}

	public void setSalaryPaymentType(String salaryPaymentType) {
		this.salaryPaymentType = salaryPaymentType;
	}

	public String getCustCompanyCat() {
		return custCompanyCat;
	}

	public void setCustCompanyCat(String custCompanyCat) {
		this.custCompanyCat = custCompanyCat;
	}

	public Double getDownPaymentRate() {
		return downPaymentRate;
	}

	public void setDownPaymentRate(Double downPaymentRate) {
		this.downPaymentRate = downPaymentRate;
	}

	public List<GoodsInformation> getGoodsInformation() {
		return goodsInformation;
	}

	public void setGoodsInformation(List<GoodsInformation> goodsInformation) {
		this.goodsInformation = goodsInformation;
	}

	public Double getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(Double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public Integer getNumberOfRelationOrganize() {
		return numberOfRelationOrganize;
	}

	public void setNumberOfRelationOrganize(Integer numberOfRelationOrganize) {
		this.numberOfRelationOrganize = numberOfRelationOrganize;
	}

	public Double getTotalOutstandingLoanCIC() {
		return totalOutstandingLoanCIC;
	}

	public void setTotalOutstandingLoanCIC(Double totalOutstandingLoanCIC) {
		this.totalOutstandingLoanCIC = totalOutstandingLoanCIC;
	}

	public Double getPti() {
		return pti;
	}

	public void setPti(Double pti) {
		this.pti = pti;
	}

	public Double getDti() {
		return dti;
	}

	public void setDti(Double dti) {
		this.dti = dti;
	}

	public Double getElectricBillM1() {
		return electricBillM1;
	}

	public void setElectricBillM1(Double electricBillM1) {
		this.electricBillM1 = electricBillM1;
	}

	public Double getElectricBillM2() {
		return electricBillM2;
	}

	public void setElectricBillM2(Double electricBillM2) {
		this.electricBillM2 = electricBillM2;
	}

	public Double getElectricBillM3() {
		return electricBillM3;
	}

	public void setElectricBillM3(Double electricBillM3) {
		this.electricBillM3 = electricBillM3;
	}

	public Double getAverageElectricBill() {
		return averageElectricBill;
	}

	public void setAverageElectricBill(Double averageElectricBill) {
		this.averageElectricBill = averageElectricBill;
	}

	public Double getAccountBalanceMinM1() {
		return accountBalanceMinM1;
	}

	public void setAccountBalanceMinM1(Double accountBalanceMinM1) {
		this.accountBalanceMinM1 = accountBalanceMinM1;
	}

	public Double getAccountBalanceMaxM1() {
		return accountBalanceMaxM1;
	}

	public void setAccountBalanceMaxM1(Double accountBalanceMaxM1) {
		this.accountBalanceMaxM1 = accountBalanceMaxM1;
	}

	public Double getAccountBalanceMinM2() {
		return accountBalanceMinM2;
	}

	public void setAccountBalanceMinM2(Double accountBalanceMinM2) {
		this.accountBalanceMinM2 = accountBalanceMinM2;
	}

	public Double getAccountBalanceMaxM2() {
		return accountBalanceMaxM2;
	}

	public void setAccountBalanceMaxM2(Double accountBalanceMaxM2) {
		this.accountBalanceMaxM2 = accountBalanceMaxM2;
	}

	public Double getAccountBalanceMinM3() {
		return accountBalanceMinM3;
	}

	public void setAccountBalanceMinM3(Double accountBalanceMinM3) {
		this.accountBalanceMinM3 = accountBalanceMinM3;
	}

	public Double getAccountBalanceMaxM3() {
		return accountBalanceMaxM3;
	}

	public void setAccountBalanceMaxM3(Double accountBalanceMaxM3) {
		this.accountBalanceMaxM3 = accountBalanceMaxM3;
	}

	public Double getAccountBalanceMinM4() {
		return accountBalanceMinM4;
	}

	public void setAccountBalanceMinM4(Double accountBalanceMinM4) {
		this.accountBalanceMinM4 = accountBalanceMinM4;
	}

	public Double getAccountBalanceMaxM4() {
		return accountBalanceMaxM4;
	}

	public void setAccountBalanceMaxM4(Double accountBalanceMaxM4) {
		this.accountBalanceMaxM4 = accountBalanceMaxM4;
	}

	public Double getAccountBalanceMinM5() {
		return accountBalanceMinM5;
	}

	public void setAccountBalanceMinM5(Double accountBalanceMinM5) {
		this.accountBalanceMinM5 = accountBalanceMinM5;
	}

	public Double getAccountBalanceMaxM5() {
		return accountBalanceMaxM5;
	}

	public void setAccountBalanceMaxM5(Double accountBalanceMaxM5) {
		this.accountBalanceMaxM5 = accountBalanceMaxM5;
	}

	public Double getAccountBalanceMinM6() {
		return accountBalanceMinM6;
	}

	public void setAccountBalanceMinM6(Double accountBalanceMinM6) {
		this.accountBalanceMinM6 = accountBalanceMinM6;
	}

	public Double getAccountBalanceMaxM6() {
		return accountBalanceMaxM6;
	}

	public void setAccountBalanceMaxM6(Double accountBalanceMaxM6) {
		this.accountBalanceMaxM6 = accountBalanceMaxM6;
	}

	public Double getAverageAccountBalance() {
		return averageAccountBalance;
	}

	public void setAverageAccountBalance(Double averageAccountBalance) {
		this.averageAccountBalance = averageAccountBalance;
	}

	public Double getCustomerIncomeInterpolate() {
		return customerIncomeInterpolate;
	}

	public void setCustomerIncomeInterpolate(Double customerIncomeInterpolate) {
		this.customerIncomeInterpolate = customerIncomeInterpolate;
	}

	public Double getCustomerIncomeAppraisal() {
		return customerIncomeAppraisal;
	}

	public void setCustomerIncomeAppraisal(Double customerIncomeAppraisal) {
		this.customerIncomeAppraisal = customerIncomeAppraisal;
	}
	
	public String getInsuranceTerm() {
		return insuranceTerm;
	}

	public void setInsuranceTerm(String insuranceTerm) {
		this.insuranceTerm = insuranceTerm;
	}

	public Long getInsuranceTermFee() {
		return insuranceTermFee;
	}

	public void setInsuranceTermFee(Long insuranceTermFee) {
		this.insuranceTermFee = insuranceTermFee;
	}

	public String getInsuranceTermOther() {
		return insuranceTermOther;
	}

	public void setInsuranceTermOther(String insuranceTermOther) {
		this.insuranceTermOther = insuranceTermOther;
	}

	public Double getAnnualFeeLifeInsurance() {
		return annualFeeLifeInsurance;
	}

	public void setAnnualFeeLifeInsurance(Double annualFeeLifeInsurance) {
		this.annualFeeLifeInsurance = annualFeeLifeInsurance;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getTypeOfDocPerson() {
		return typeOfDocPerson;
	}

	public void setTypeOfDocPerson(String typeOfDocPerson) {
		this.typeOfDocPerson = typeOfDocPerson;
	}

	public String getCitizenID() {
		return citizenID;
	}

	public void setCitizenID(String citizenID) {
		this.citizenID = citizenID;
	}

	public String getCitizenIDDate() {
		return citizenIDDate;
	}

	public void setCitizenIDDate(String citizenIDDate) {
		this.citizenIDDate = citizenIDDate;
	}

	public String getMilitaryID() {
		return militaryID;
	}

	public void setMilitaryID(String militaryID) {
		this.militaryID = militaryID;
	}

	public String getMilitaryIDDate() {
		return militaryIDDate;
	}

	public void setMilitaryIDDate(String militaryIDDate) {
		this.militaryIDDate = militaryIDDate;
	}

	public String getAppraisalDate() {
		return appraisalDate;
	}

	public void setAppraisalDate(String appraisalDate) {
		this.appraisalDate = appraisalDate;
	}

	public Double getMonthIncome1() {
		return monthIncome1;
	}

	public void setMonthIncome1(Double monthIncome1) {
		this.monthIncome1 = monthIncome1;
	}

	public Double getMonthIncome2() {
		return monthIncome2;
	}

	public void setMonthIncome2(Double monthIncome2) {
		this.monthIncome2 = monthIncome2;
	}

	public Double getMonthIncome3() {
		return monthIncome3;
	}

	public void setMonthIncome3(Double monthIncome3) {
		this.monthIncome3 = monthIncome3;
	}

	public Integer getNumberOfRelationFinanceCompany() {
		return numberOfRelationFinanceCompany;
	}

	public void setNumberOfRelationFinanceCompany(Integer numberOfRelationFinanceCompany) {
		this.numberOfRelationFinanceCompany = numberOfRelationFinanceCompany;
	}

	public Integer getNumberOfRelationBank() {
		return numberOfRelationBank;
	}

	public void setNumberOfRelationBank(Integer numberOfRelationBank) {
		this.numberOfRelationBank = numberOfRelationBank;
	}

	public Double getCustomerIncome() {
		return customerIncome;
	}

	public void setCustomerIncome(Double customerIncome) {
		this.customerIncome = customerIncome;
	}

	public String getHasInsurance() {
		return hasInsurance;
	}

	public void setHasInsurance(String hasInsurance) {
		this.hasInsurance = hasInsurance;
	}

	public Double getInsuranceFee() {
		return insuranceFee;
	}

	public void setInsuranceFee(Double insuranceFee) {
		this.insuranceFee = insuranceFee;
	}

	public Double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}

	public Double getEmi() {
		return emi;
	}

	public void setEmi(Double emi) {
		this.emi = emi;
	}

	public Double getTotalOutstandingLoan() {
		return totalOutstandingLoan;
	}

	public void setTotalOutstandingLoan(Double totalOutstandingLoan) {
		this.totalOutstandingLoan = totalOutstandingLoan;
	}

	public Double getFinalEMI() {
		return finalEMI;
	}

	public void setFinalEMI(Double finalEMI) {
		this.finalEMI = finalEMI;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public Double getInput_1a() {
		return input_1a;
	}

	public void setInput_1a(Double input_1a) {
		this.input_1a = input_1a;
	}

	public Double getInput_1b() {
		return input_1b;
	}

	public void setInput_1b(Double input_1b) {
		this.input_1b = input_1b;
	}

	public Double getInput_2() {
		return input_2;
	}

	public void setInput_2(Double input_2) {
		this.input_2 = input_2;
	}

	public Double getInput_3a() {
		return input_3a;
	}

	public void setInput_3a(Double input_3a) {
		this.input_3a = input_3a;
	}

	public Double getInput_3b() {
		return input_3b;
	}

	public void setInput_3b(Double input_3b) {
		this.input_3b = input_3b;
	}

	public Double getInput_3c() {
		return input_3c;
	}

	public void setInput_3c(Double input_3c) {
		this.input_3c = input_3c;
	}

	public Double getInput_4a() {
		return input_4a;
	}

	public void setInput_4a(Double input_4a) {
		this.input_4a = input_4a;
	}

	public Double getInput_4b() {
		return input_4b;
	}

	public void setInput_4b(Double input_4b) {
		this.input_4b = input_4b;
	}

	public Double getInput_4c() {
		return input_4c;
	}

	public void setInput_4c(Double input_4c) {
		this.input_4c = input_4c;
	}

	public Double getInput_11a() {
		return input_11a;
	}

	public void setInput_11a(Double input_11a) {
		this.input_11a = input_11a;
	}

	public Double getInput_11b() {
		return input_11b;
	}

	public void setInput_11b(Double input_11b) {
		this.input_11b = input_11b;
	}

	public Double getInput_12() {
		return input_12;
	}

	public void setInput_12(Double input_12) {
		this.input_12 = input_12;
	}

	public Double getInput_13a() {
		return input_13a;
	}

	public void setInput_13a(Double input_13a) {
		this.input_13a = input_13a;
	}

	public Double getInput_13b() {
		return input_13b;
	}

	public void setInput_13b(Double input_13b) {
		this.input_13b = input_13b;
	}

	public Double getInput_13c() {
		return input_13c;
	}

	public void setInput_13c(Double input_13c) {
		this.input_13c = input_13c;
	}

	public Double getInput_14a() {
		return input_14a;
	}

	public void setInput_14a(Double input_14a) {
		this.input_14a = input_14a;
	}

	public Double getInput_14b() {
		return input_14b;
	}

	public void setInput_14b(Double input_14b) {
		this.input_14b = input_14b;
	}

	public Double getInput_16a() {
		return input_16a;
	}

	public void setInput_16a(Double input_16a) {
		this.input_16a = input_16a;
	}

	public Double getInput_16b() {
		return input_16b;
	}

	public void setInput_16b(Double input_16b) {
		this.input_16b = input_16b;
	}

	public Double getInput_17() {
		return input_17;
	}

	public void setInput_17(Double input_17) {
		this.input_17 = input_17;
	}

	public Double getInput_18a() {
		return input_18a;
	}

	public void setInput_18a(Double input_18a) {
		this.input_18a = input_18a;
	}

	public Double getInput_18b() {
		return input_18b;
	}

	public void setInput_18b(Double input_18b) {
		this.input_18b = input_18b;
	}

	public Double getInput_18c() {
		return input_18c;
	}

	public void setInput_18c(Double input_18c) {
		this.input_18c = input_18c;
	}

	public Double getInput_19a() {
		return input_19a;
	}

	public void setInput_19a(Double input_19a) {
		this.input_19a = input_19a;
	}

	public Double getInput_19b() {
		return input_19b;
	}

	public void setInput_19b(Double input_19b) {
		this.input_19b = input_19b;
	}

	public Double getInput_19c() {
		return input_19c;
	}

	public void setInput_19c(Double input_19c) {
		this.input_19c = input_19c;
	}

	public Double getRemaining_term_1a() {
		return remaining_term_1a;
	}

	public void setRemaining_term_1a(Double remaining_term_1a) {
		this.remaining_term_1a = remaining_term_1a;
	}

	public Double getRemaining_term_1b() {
		return remaining_term_1b;
	}

	public void setRemaining_term_1b(Double remaining_term_1b) {
		this.remaining_term_1b = remaining_term_1b;
	}

	public Double getRemaining_term_2() {
		return remaining_term_2;
	}

	public void setRemaining_term_2(Double remaining_term_2) {
		this.remaining_term_2 = remaining_term_2;
	}

	public Double getRemaining_term_3a() {
		return remaining_term_3a;
	}

	public void setRemaining_term_3a(Double remaining_term_3a) {
		this.remaining_term_3a = remaining_term_3a;
	}

	public Double getRemaining_term_3b() {
		return remaining_term_3b;
	}

	public void setRemaining_term_3b(Double remaining_term_3b) {
		this.remaining_term_3b = remaining_term_3b;
	}

	public Double getRemaining_term_3c() {
		return remaining_term_3c;
	}

	public void setRemaining_term_3c(Double remaining_term_3c) {
		this.remaining_term_3c = remaining_term_3c;
	}

	public Double getRemaining_term_4a() {
		return remaining_term_4a;
	}

	public void setRemaining_term_4a(Double remaining_term_4a) {
		this.remaining_term_4a = remaining_term_4a;
	}

	public Double getRemaining_term_4b() {
		return remaining_term_4b;
	}

	public void setRemaining_term_4b(Double remaining_term_4b) {
		this.remaining_term_4b = remaining_term_4b;
	}

	public Double getRemaining_term_4c() {
		return remaining_term_4c;
	}

	public void setRemaining_term_4c(Double remaining_term_4c) {
		this.remaining_term_4c = remaining_term_4c;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return new Gson().toJson(this);
	}

}
