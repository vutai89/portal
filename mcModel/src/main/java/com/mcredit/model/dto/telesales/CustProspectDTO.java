package com.mcredit.model.dto.telesales;

public class CustProspectDTO {
	private Long id;

	private String recordStatus;

	private String accommodationType;

	private Long allocationDetailId;

	private String birthDate;

	private String compAddrDistrict;

	private String compAddrProvince;

	private String compAddrStreet;

	private String compAddrWard;

	private String createdBy;

	private Long custIncome;

	private String custName;

	private String gender;

	private String identityNumber;

	private String mobile;

	private String note;

	private String permanentAddr;

	private String permanentDistrict;

	private String permanentProvince;

	private String permanentWard;

	private String professional;

	private Long uplCustomerId;

	private String uplCode;

	private String position;

	// Lead Gen
	private String income;

	private String scoreRange;

	private String sourceSystem;

	private Long productId;

	private String productName;

	private String productCode;

	private String minLoanAmount;

	private String maxLoanAmount;

	private Integer minTenor;

	private Integer maxTenor;

	private String yearlyRate;

	private String monthlyRate;

	private String createdDate;
	
	private String isMark;

	// xsell
	private String province;
	private String minScore;
	private String maxScore;
	private String lastUpdatedDate;
	private String refId;
	private String preProductName;
	private String preProductCode;
	private String preMinLimit;
	private String preMaxLimit;
	private String preMinTenor;
	private String preMaxTenor;
	private String preMinEmi;
	private String appProductName;
	private String appProductCode;
	private String appLoanApprovedAmt;
	private String appIntRate;
	private String appTermLoan;
	private String disbursementDate;
	private String matDate;
	private String commoditiesCode;
	private String dataSource;
	private String leadSource;
	private String custId;
	private String preMaxEmi;
	private String appEmi;

	// --------------------
	private String companyName;
	private String preTenor;
	private String settlementDate;
	private String preEMI;
	private String newProductName;
	private String newProductCode;
	private String periodMin;
	private String periodMax;
	private String refFullName1;
	private String relationRefPerson1;
	private String refPerson1Mobile;
	private String refFullName2;
	private String refPerson2Mobile;
	private String relationRefPerson2;
	private String xsellValidatedFromDate;
	private String xsellValidatedToDate;

	private String preMinAmount;
	private String preMaxAmount;
	private String periodMinTenor;
	private String periodMaxTenor;
	private String permanentAddress;
	
	// More data for xsell
	private String xsMobile;
	private String identityNumberArmy;
	private String cmndIssueDate;
	private String cmndIssuePlace;
	private String cmqdIssueDate;
	private String cmqdIssuePlace;
	
	private String xsGender;
	private String xsProfessionnal;
	private String xsHouseNumber;
	private String xsWard;
	private String xsDistrict;
	private String xsProvince;
	private String xsAccommodation;
	
	private String xsPerHouseNumber;
	private String xsPerWard;
	private String xsPerDistrict;
	private String xsPerProvince;
	
	private String xsCompanyName;
	private String xsCompanyAddress;
	private String xsPositionInComp;
	private String xsCustIncome;
	private String xsCompWard;
	private String xsCompDistrict;
	private String xsCompProvince;
	
	private String fromsource;
	
	// AMO
	private String originIncome;
	
	private String newMobile;
	
	public String getOriginIncome() {
		return originIncome;
	}

	public void setOriginIncome(String originIncome) {
		this.originIncome = originIncome;
	}

	public String getFromsource() {
		return fromsource;
	}

	public void setFromsource(String fromsource) {
		this.fromsource = fromsource;
	}

	public String getPermanentAddress() {
		return permanentAddress;
	}

	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	public String getPreMinAmount() {
		return preMinAmount;
	}

	public void setPreMinAmount(String preMinAmount) {
		this.preMinAmount = preMinAmount;
	}

	public String getPreMaxAmount() {
		return preMaxAmount;
	}

	public void setPreMaxAmount(String preMaxAmount) {
		this.preMaxAmount = preMaxAmount;
	}

	public String getPeriodMinTenor() {
		return periodMinTenor;
	}

	public void setPeriodMinTenor(String periodMinTenor) {
		this.periodMinTenor = periodMinTenor;
	}

	public String getPeriodMaxTenor() {
		return periodMaxTenor;
	}

	public void setPeriodMaxTenor(String periodMaxTenor) {
		this.periodMaxTenor = periodMaxTenor;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getRefFullName1() {
		return refFullName1;
	}

	public void setRefFullName1(String refFullName1) {
		this.refFullName1 = refFullName1;
	}

	public String getRelationRefPerson1() {
		return relationRefPerson1;
	}

	public void setRelationRefPerson1(String relationRefPerson1) {
		this.relationRefPerson1 = relationRefPerson1;
	}

	public String getRefPerson1Mobile() {
		return refPerson1Mobile;
	}

	public void setRefPerson1Mobile(String refPerson1Mobile) {
		this.refPerson1Mobile = refPerson1Mobile;
	}

	public String getRefFullName2() {
		return refFullName2;
	}

	public void setRefFullName2(String refFullName2) {
		this.refFullName2 = refFullName2;
	}

	public String getRefPerson2Mobile() {
		return refPerson2Mobile;
	}

	public void setRefPerson2Mobile(String refPerson2Mobile) {
		this.refPerson2Mobile = refPerson2Mobile;
	}

	public String getRelationRefPerson2() {
		return relationRefPerson2;
	}

	public void setRelationRefPerson2(String relationRefPerson2) {
		this.relationRefPerson2 = relationRefPerson2;
	}

	public String getXsellValidatedFromDate() {
		return xsellValidatedFromDate;
	}

	public void setXsellValidatedFromDate(String xsellValidatedFromDate) {
		this.xsellValidatedFromDate = xsellValidatedFromDate;
	}

	public String getXsellValidatedToDate() {
		return xsellValidatedToDate;
	}

	public void setXsellValidatedToDate(String xsellValidatedToDate) {
		this.xsellValidatedToDate = xsellValidatedToDate;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getMinScore() {
		return minScore;
	}

	public void setMinScore(String minScore) {
		this.minScore = minScore;
	}

	public String getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(String maxScore) {
		this.maxScore = maxScore;
	}

	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getXsMobile() {
		return xsMobile;
	}

	public void setXsMobile(String xsMobile) {
		this.xsMobile = xsMobile;
	}

	public String getIdentityNumberArmy() {
		return identityNumberArmy;
	}

	public void setIdentityNumberArmy(String identityNumberArmy) {
		this.identityNumberArmy = identityNumberArmy;
	}

	public String getPreProductName() {
		return preProductName;
	}

	public void setPreProductName(String preProductName) {
		this.preProductName = preProductName;
	}

	public String getPreProductCode() {
		return preProductCode;
	}

	public void setPreProductCode(String preProductCode) {
		this.preProductCode = preProductCode;
	}

	public String getPreMinLimit() {
		return preMinLimit;
	}

	public void setPreMinLimit(String preMinLimit) {
		this.preMinLimit = preMinLimit;
	}

	public String getPreMaxLimit() {
		return preMaxLimit;
	}

	public void setPreMaxLimit(String preMaxLimit) {
		this.preMaxLimit = preMaxLimit;
	}

	public String getPreMinTenor() {
		return preMinTenor;
	}

	public void setPreMinTenor(String preMinTenor) {
		this.preMinTenor = preMinTenor;
	}

	public String getPreMaxTenor() {
		return preMaxTenor;
	}

	public void setPreMaxTenor(String preMaxTenor) {
		this.preMaxTenor = preMaxTenor;
	}

	public String getPreMinEmi() {
		return preMinEmi;
	}

	public void setPreMinEmi(String preMinEmi) {
		this.preMinEmi = preMinEmi;
	}

	public String getAppProductName() {
		return appProductName;
	}

	public void setAppProductName(String appProductName) {
		this.appProductName = appProductName;
	}

	public String getAppProductCode() {
		return appProductCode;
	}

	public void setAppProductCode(String appProductCode) {
		this.appProductCode = appProductCode;
	}

	public String getAppLoanApprovedAmt() {
		return appLoanApprovedAmt;
	}

	public void setAppLoanApprovedAmt(String appLoanApprovedAmt) {
		this.appLoanApprovedAmt = appLoanApprovedAmt;
	}

	public String getAppIntRate() {
		return appIntRate;
	}

	public void setAppIntRate(String appIntRate) {
		this.appIntRate = appIntRate;
	}

	public String getAppTermLoan() {
		return appTermLoan;
	}

	public void setAppTermLoan(String appTermLoan) {
		this.appTermLoan = appTermLoan;
	}

	public String getDisbursementDate() {
		return disbursementDate;
	}

	public void setDisbursementDate(String disbursementDate) {
		this.disbursementDate = disbursementDate;
	}

	public String getMatDate() {
		return matDate;
	}

	public void setMatDate(String matDate) {
		this.matDate = matDate;
	}

	public String getCommoditiesCode() {
		return commoditiesCode;
	}

	public void setCommoditiesCode(String commoditiesCode) {
		this.commoditiesCode = commoditiesCode;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getLeadSource() {
		return leadSource;
	}

	public void setLeadSource(String leadSource) {
		this.leadSource = leadSource;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getPreMaxEmi() {
		return preMaxEmi;
	}

	public void setPreMaxEmi(String preMaxEmi) {
		this.preMaxEmi = preMaxEmi;
	}

	public String getAppEmi() {
		return appEmi;
	}

	public void setAppEmi(String appEmi) {
		this.appEmi = appEmi;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getUplCode() {
		return uplCode;
	}

	public void setUplCode(String uplCode) {
		this.uplCode = uplCode;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccommodationType() {
		return accommodationType;
	}

	public void setAccommodationType(String accommodationType) {
		this.accommodationType = accommodationType;
	}

	public Long getAllocationDetailId() {
		return allocationDetailId;
	}

	public void setAllocationDetailId(Long allocationDetailId) {
		this.allocationDetailId = allocationDetailId;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getCompAddrDistrict() {
		return compAddrDistrict;
	}

	public void setCompAddrDistrict(String compAddrDistrict) {
		this.compAddrDistrict = compAddrDistrict;
	}

	public String getCompAddrProvince() {
		return compAddrProvince;
	}

	public void setCompAddrProvince(String compAddrProvince) {
		this.compAddrProvince = compAddrProvince;
	}

	public String getCompAddrStreet() {
		return compAddrStreet;
	}

	public void setCompAddrStreet(String compAddrStreet) {
		this.compAddrStreet = compAddrStreet;
	}

	public String getCompAddrWard() {
		return compAddrWard;
	}

	public void setCompAddrWard(String compAddrWard) {
		this.compAddrWard = compAddrWard;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Long getCustIncome() {
		return custIncome;
	}

	public void setCustIncome(Long custIncome) {
		this.custIncome = custIncome;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIdentityNumber() {
		return identityNumber;
	}

	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPermanentAddr() {
		return permanentAddr;
	}

	public void setPermanentAddr(String permanentAddr) {
		this.permanentAddr = permanentAddr;
	}

	public String getPermanentDistrict() {
		return permanentDistrict;
	}

	public void setPermanentDistrict(String permanentDistrict) {
		this.permanentDistrict = permanentDistrict;
	}

	public String getPermanentProvince() {
		return permanentProvince;
	}

	public void setPermanentProvince(String permanentProvince) {
		this.permanentProvince = permanentProvince;
	}

	public String getPermanentWard() {
		return permanentWard;
	}

	public void setPermanentWard(String permanentWard) {
		this.permanentWard = permanentWard;
	}

	public String getProfessional() {
		return professional;
	}

	public void setProfessional(String professional) {
		this.professional = professional;
	}

	public Long getUplCustomerId() {
		return uplCustomerId;
	}

	public void setUplCustomerId(Long uplCustomerId) {
		this.uplCustomerId = uplCustomerId;
	}

	// Lead gen
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getScoreRange() {
		return scoreRange;
	}

	public void setScoreRange(String scoreRange) {
		this.scoreRange = scoreRange;
	}

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getMinLoanAmount() {
		return minLoanAmount;
	}

	public void setMinLoanAmount(String minLoanAmount) {
		this.minLoanAmount = minLoanAmount;
	}

	public String getMaxLoanAmount() {
		return maxLoanAmount;
	}

	public void setMaxLoanAmount(String maxLoanAmount) {
		this.maxLoanAmount = maxLoanAmount;
	}

	public Integer getMinTenor() {
		return minTenor;
	}

	public void setMinTenor(Integer minTenor) {
		this.minTenor = minTenor;
	}

	public Integer getMaxTenor() {
		return maxTenor;
	}

	public void setMaxTenor(Integer maxTenor) {
		this.maxTenor = maxTenor;
	}

	public String getYearlyRate() {
		return yearlyRate;
	}

	public void setYearlyRate(String yearlyRate) {
		this.yearlyRate = yearlyRate;
	}

	public String getMonthlyRate() {
		return monthlyRate;
	}

	public void setMonthlyRate(String monthlyRate) {
		this.monthlyRate = monthlyRate;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getCmndIssueDate() {
		return cmndIssueDate;
	}

	public void setCmndIssueDate(String cmndIssueDate) {
		this.cmndIssueDate = cmndIssueDate;
	}

	public String getCmndIssuePlace() {
		return cmndIssuePlace;
	}

	public void setCmndIssuePlace(String cmndIssuePlace) {
		this.cmndIssuePlace = cmndIssuePlace;
	}

	public String getCmqdIssueDate() {
		return cmqdIssueDate;
	}

	public void setCmqdIssueDate(String cmqdIssueDate) {
		this.cmqdIssueDate = cmqdIssueDate;
	}

	public String getCmqdIssuePlace() {
		return cmqdIssuePlace;
	}

	public void setCmqdIssuePlace(String cmqdIssuePlace) {
		this.cmqdIssuePlace = cmqdIssuePlace;
	}

	public String getPreTenor() {
		return preTenor;
	}

	public void setPreTenor(String preTenor) {
		this.preTenor = preTenor;
	}

	public String getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(String settlementDate) {
		this.settlementDate = settlementDate;
	}

	public String getPreEMI() {
		return preEMI;
	}

	public void setPreEMI(String preEMI) {
		this.preEMI = preEMI;
	}

	public String getNewProductName() {
		return newProductName;
	}

	public void setNewProductName(String newProductName) {
		this.newProductName = newProductName;
	}

	public String getNewProductCode() {
		return newProductCode;
	}

	public void setNewProductCode(String newProductCode) {
		this.newProductCode = newProductCode;
	}

	public String getPeriodMin() {
		return periodMin;
	}

	public void setPeriodMin(String periodMin) {
		this.periodMin = periodMin;
	}

	public String getPeriodMax() {
		return periodMax;
	}

	public void setPeriodMax(String periodMax) {
		this.periodMax = periodMax;
	}

	public String getXsCompanyName() {
		return xsCompanyName;
	}

	public void setXsCompanyName(String xsCompanyName) {
		this.xsCompanyName = xsCompanyName;
	}

	public String getXsCompanyAddress() {
		return xsCompanyAddress;
	}

	public void setXsCompanyAddress(String xsCompanyAddress) {
		this.xsCompanyAddress = xsCompanyAddress;
	}

	public String getXsCustIncome() {
		return xsCustIncome;
	}

	public void setXsCustIncome(String xsCustIncome) {
		this.xsCustIncome = xsCustIncome;
	}

	public String getXsGender() {
		return xsGender;
	}

	public void setXsGender(String xsGender) {
		this.xsGender = xsGender;
	}

	public String getXsProfessionnal() {
		return xsProfessionnal;
	}

	public void setXsProfessionnal(String xsProfessionnal) {
		this.xsProfessionnal = xsProfessionnal;
	}
	
	public String getXsHouseNumber() {
		return xsHouseNumber;
	}

	public void setXsHouseNumber(String xsHouseNumber) {
		this.xsHouseNumber = xsHouseNumber;
	}

	public String getXsWard() {
		return xsWard;
	}

	public void setXsWard(String xsWard) {
		this.xsWard = xsWard;
	}

	public String getXsDistrict() {
		return xsDistrict;
	}

	public void setXsDistrict(String xsDistrict) {
		this.xsDistrict = xsDistrict;
	}

	public String getXsProvince() {
		return xsProvince;
	}

	public void setXsProvince(String xsProvince) {
		this.xsProvince = xsProvince;
	}

	public String getXsAccommodation() {
		return xsAccommodation;
	}

	public void setXsAccommodation(String xsAccommodation) {
		this.xsAccommodation = xsAccommodation;
	}

	public String getXsPerHouseNumber() {
		return xsPerHouseNumber;
	}

	public void setXsPerHouseNumber(String xsPerHouseNumber) {
		this.xsPerHouseNumber = xsPerHouseNumber;
	}

	public String getXsPerWard() {
		return xsPerWard;
	}

	public void setXsPerWard(String xsPerWard) {
		this.xsPerWard = xsPerWard;
	}

	public String getXsPerDistrict() {
		return xsPerDistrict;
	}

	public void setXsPerDistrict(String xsPerDistrict) {
		this.xsPerDistrict = xsPerDistrict;
	}

	public String getXsPerProvince() {
		return xsPerProvince;
	}

	public void setXsPerProvince(String xsPerProvince) {
		this.xsPerProvince = xsPerProvince;
	}

	public String getXsPositionInComp() {
		return xsPositionInComp;
	}

	public void setXsPositionInComp(String xsPositionInComp) {
		this.xsPositionInComp = xsPositionInComp;
	}

	public String getXsCompWard() {
		return xsCompWard;
	}

	public void setXsCompWard(String xsCompWard) {
		this.xsCompWard = xsCompWard;
	}

	public String getXsCompDistrict() {
		return xsCompDistrict;
	}

	public void setXsCompDistrict(String xsCompDistrict) {
		this.xsCompDistrict = xsCompDistrict;
	}

	public String getXsCompProvince() {
		return xsCompProvince;
	}

	public void setXsCompProvince(String xsCompProvince) {
		this.xsCompProvince = xsCompProvince;
	}

	public String getIsMark() {
		return isMark;
	}

	public void setIsMark(String isMark) {
		this.isMark = isMark;
	}

	public String getNewMobile() {
		return newMobile;
	}

	public void setNewMobile(String newMobile) {
		this.newMobile = newMobile;
	}
	
	
}
