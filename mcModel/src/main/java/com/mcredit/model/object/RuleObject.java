package com.mcredit.model.object;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleObject {
	
	// rq775
	public static final List<String> listFile1 = Arrays.asList("TemporaryResidenceConfirmation", "TemporaryResidenceBook", "TemporaryResidenceCard", 
			"HomeOwnershipCertification", "NotarizedHomeSalesContract", "HomeMortgageContract", "InternetBill", "WaterBill", 
			"CableTelevisionBill", "ElectricBill", "HomePhoneBill", "MobilePhoneBill");
	public static final List<String> listFile2 = Arrays.asList("MarriageCertificate", "excerptOfMarriage");
	public static final List<String> listFile3 = Arrays.asList("photoMessagesSwitchboard1414");
	public static final List<String> listFile4 = Arrays.asList("photoInformationCustomer");
	
	// rq871
	public static final List<String> listFile5 = Arrays.asList("HealthInsuranceCard");
	
	private String productCode;
	private String ruleCode;
	private String categoryCode;
	private Integer loanAmount;
	private int loanTenor;
	private Double interest;
	private Double yearInterest;
	private String loanPurpose;
	private String loanPurposeOther;
	private Double goodsPrice;
	private Double ownedCapital;
	private Double insurance;
	private Integer numberOfGoods;
	private String typeOfGoods;
	private String brand;
	private String brandOther;
	private String model;
	private String sipCode;
	private String birthDate;
	private String gender;
	private String maritalStatus;
	private String education;
	private String professionalStatus;
	private String position;
	private String militaryRank;
	private String department;
	private String compAddrWardCode;
	private String compAddrDistrictCode;
	private String compAddrProvinceCode;
	private String compTaxNumber;
	private Double customerIncome;
	private String typeOfLabourContract;
	private Integer experienceInYear;
	private Integer experienceInMonth;
	private String salaryPaymentType;
	private String permResWardCode;
	private String permResDistrictCode;
	private String permResProvinceCode;
	private String familyBookSeriesType;
	private String tempAddrWardCode;
	private String tempAddrDistrictCode;
	private String tempAddrProvinceCode;
	private Integer livingAtTempAddInYear;
	private Integer livingAtTempAddInMonth;
	private String accomType;
	private String relationApplicant;
	private Integer numberOfDependants;
	private String spouseAddrWardCode;
	private String spouseAddrDistrictCode;
	private String spouseAddrProvinceCode;
	private Double incomeSpouse;
	private String relationBorrower1;
	private String relationBorrower2;
	private Double amountOtherCreditLoan;
	private String appId;
	private Integer appNumber;
	private String createdDate;
	private String lastUpdatedDate;
	private Double loanAmountAfterInsurrance;
	private String typeOfGoodsOther;
	private Integer hasInsurrance;
	private String typeOfLoan;
	private String disbursementChannel;
	private String bankName;
	private String atBank;
	private String saleCode;
	private String spousePosition;
	private Double spouseIncome;
	private Double insurranceFee;
	private String loanStartDate;
	private Double averageElectricBill;
	private String insuranceTerm;
	private Double averageAccountBalance;
	private Float agencyCode;
	private Byte typeOfProduct;
	private Double insuranceRate;
	private String insuranceType;
	private String pilotName;
	private String customerExpense;
	private Double downPaymentRate;
	private Integer temporaryResidence;
	private String custCompanyCat;
	private Integer wageStatement;
	private Integer custAddress;
	private Integer homeCompAdress;
	private Integer selfEmployed;
	private Integer annualFeeInsu;
	private String productGroup;
	private String identityType;
	private String militaryLevel;
	private String cccdExpirationDate;
	private String cmndOrCmqdIssueDate;
	private String dataEntryDate;
	private Integer hasCourier;
	private String temSamePermDist;
	private Integer evnBillName;
	
	// Rule check comptype
	private String taxChapterGroup;
	private String compNameHave;
	private String compNameHave2;
	private String compNameNotHave;
	private String lenghtTaxNumber;
	private String economicTypeHave;
	
	// rule check cat
	private String compType;
	private int topComp;
	private int topBranch;
	private int operationTime;
	private String cicInfo;
	private int recordStatus;
	
	//cic
	private String appStatus;
	private List<String> appStatusList;
	private int hasCicInfo;
	private Integer numberOfRelationOrganize;	// so to chuc tai chinh co quan he
	private Integer numberOfRelationFinanceCompany;	// so cong ty tai chinh co quan he
	private Double totalOutstandingLoanAtCIC;		// tong du no cua khach hang tai CIC
	private Double pti;							// PTI
	private Double dti;							// DTI
	private Double annualFeeLifeInsurance;		// phi BHNT hang nam
	
	private String startDatePreviousLoan;
	private Integer loanTenorOtherCredit;		// months
	private String endDatePreviousLoan;

	private String participants;				// Doi tuong tham gia (rule checkList entry)
	
	// rq_775 rule payment validation
	private Long numberBills;					// so loai hoa don
	private Double avgPayValueOnBills;			// gia tri thanh toan trung binh tren hoa don
	
	// rq_775 rule relationship
	private String personNameBill;				// nguoi dung ten tren hoa don
	private String relationshipWithApplicant;	// moi quan he voi nguoi de nghi vay von
	
	// rq_775 rule checklist validation
	private HashMap<String, String> fileUploadedList;	// danh sach cac ho so da upload
	private String typeBills;					// Loai hoa don
	private String cpeProvince;					// tinh thanh pho giong/khac
	private String cpeDistrict;					// quan huyen giong/khac
	private String currentAddressSpouse;		// cho o hien tai
	
	// rq827 Rule Leadgen_Score 
	private String partner;
	private String telcoCode;
	private Long minScore;
	
	// rq878 add new input rule rm_codefield
	private Long scoreCard;						// diem score card	
	
	// rq874 change rule age
	private String checkAgeDate;				// ngay check tuoi
	
	// rq822 Rule VALIDATION_CICPCB
	private String s37Result;					// ket qua s37 khach hang
	private String s37SpouseState;				// thong tin s37 vo chong KH da chon ket qua va tai file hay chua 
	private String pcbState;					// thong tin pcb KH da chon ket qua va tai file hay chua 
	
	// rq858 rule number of call
	private String scoreStatus;					// mien tham chieu (1)/ khong mien tham chieu (0) khi get scoreCard
	private String taxCustomerStatus;			// trang thai mst khach hang 
	private String cicStatus;					// trang thai cic
	private String otherLookup;					// tra cuu khac
	private Integer totalCustomerCall;			// tong so cuoc goi khach hang
	private Integer totalReferenceCall;			// tong so cuoc goi tham chieu
	
	// rq871 rule insurance card
	private String insuranceCardStatus;			// Co/Khong (1/0) upload the bao hiem y te
	private List<String> listTypeOfGoods;		// danh sach hang hoa
	private List<String> listBrands;			// danh sach nhan hieu
	
	// rq956 rule rm codefield
	private String incomeFromSalary;			// KH co thu nhap tu luong
	
	// rq1039 rule list of products
	private String mobileUserName;				// tai khoan day case tren mobile4sales
	
	// rq1013 rule reject score, vendor valid
	private String policyRegulationAppraisal;	// chinh sach quy dinh QTRR/DK san pham (Pass/Fail)
	private Long tsScore;						// cham diem trusting social
	private Long cicScore;						// cham diem cic data
	// rq1063 rule validate score
	private Long lgTsScore;						// diem leadgen trusting social
	private Long lgCicScore;					// diem leadgen cic data
	private String tsScoreDate;					// ngay cham diem trusting social
	private String cicScoreDate;				// ngay cham diem cic data
	private String lgTsScoreDate;				// ngay nhan diem leadgen trusting social
	private String lgCicScoreDate;				// ngay nhan diem leadgen cic data
	
	// rq1010, rq1015
	private Integer typeScore;					// muc dich sinh san pham LeadGen_score = 1 (sinh sp khi 3rd day lead sang), Get_score = 2 (sinh sp khi goi sang 3rd cham diem)

	// rq1043
	private Integer contractType;				// thoi vu, thu viec, 1-3 nam, khong xac dinh thoi han
	
	//rq1121-CHECKLIST_EXPT
	private String saleChannels;                // kenh ban
	private Integer goodsType;                  // list hang hoa = 1 (khi co tat ca cac loai 'Noi that thiet ke','Phau thuat tham my','Y te','Cham soc syc khoe/lam dep'),list hang hoa = 0 (truong hop con lai) 
	
	public int getHasCicInfo() {
		return hasCicInfo;
	}

	public void setHasCicInfo(int hasCicInfo) {
		this.hasCicInfo = hasCicInfo;
	}

	public String getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}

	public List<String> getAppStatusList() {
		return appStatusList;
	}

	public void setAppStatusList(List<String> appStatusList) {
		this.appStatusList = appStatusList;
	}

	public String getRuleCode() {
		return ruleCode;
	}

	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Integer getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(Integer loanAmount) {
		this.loanAmount = loanAmount;
	}

	public int getLoanTenor() {
		return loanTenor;
	}

	public void setLoanTenor(int loanTenor) {
		this.loanTenor = loanTenor;
	}

	public String getLoanPurpose() {
		return loanPurpose;
	}

	public void setLoanPurpose(String loanPurpose) {
		this.loanPurpose = loanPurpose;
	}

	public String getLoanPurposeOther() {
		return loanPurposeOther;
	}

	public void setLoanPurposeOther(String loanPurposeOther) {
		this.loanPurposeOther = loanPurposeOther;
	}

	public Double getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(Double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public Double getOwnedCapital() {
		return ownedCapital;
	}

	public void setOwnedCapital(Double ownedCapital) {
		this.ownedCapital = ownedCapital;
	}

	public Double getInsurance() {
		return insurance;
	}

	public void setInsurance(Double insurance) {
		this.insurance = insurance;
	}

	public Integer getNumberOfGoods() {
		return numberOfGoods;
	}

	public void setNumberOfGoods(Integer numberOfGoods) {
		this.numberOfGoods = numberOfGoods;
	}

	public String getTypeOfGoods() {
		return typeOfGoods;
	}

	public void setTypeOfGoods(String typeOfGoods) {
		this.typeOfGoods = typeOfGoods;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getBrandOther() {
		return brandOther;
	}

	public void setBrandOther(String brandOther) {
		this.brandOther = brandOther;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSipCode() {
		return sipCode;
	}

	public void setSipCode(String sipCode) {
		this.sipCode = sipCode;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getProfessionalStatus() {
		return professionalStatus;
	}

	public void setProfessionalStatus(String professionalStatus) {
		this.professionalStatus = professionalStatus;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getMilitaryRank() {
		return militaryRank;
	}

	public void setMilitaryRank(String militaryRank) {
		this.militaryRank = militaryRank;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getCompAddrWardCode() {
		return compAddrWardCode;
	}

	public void setCompAddrWardCode(String compAddrWardCode) {
		this.compAddrWardCode = compAddrWardCode;
	}

	public String getCompAddrDistrictCode() {
		return compAddrDistrictCode;
	}

	public void setCompAddrDistrictCode(String compAddrDistrictCode) {
		this.compAddrDistrictCode = compAddrDistrictCode;
	}

	public String getCompAddrProvinceCode() {
		return compAddrProvinceCode;
	}

	public void setCompAddrProvinceCode(String compAddrProvinceCode) {
		this.compAddrProvinceCode = compAddrProvinceCode;
	}

	public String getCompTaxNumber() {
		return compTaxNumber;
	}

	public void setCompTaxNumber(String compTaxNumber) {
		this.compTaxNumber = compTaxNumber;
	}

	public Double getCustomerIncome() {
		return customerIncome;
	}

	public void setCustomerIncome(Double customerIncome) {
		this.customerIncome = customerIncome;
	}

	public String getTypeOfLabourContract() {
		return typeOfLabourContract;
	}

	public void setTypeOfLabourContract(String typeOfLabourContract) {
		this.typeOfLabourContract = typeOfLabourContract;
	}

	public Integer getExperienceInYear() {
		return experienceInYear;
	}

	public void setExperienceInYear(Integer experienceInYear) {
		this.experienceInYear = experienceInYear;
	}

	public Integer getExperienceInMonth() {
		return experienceInMonth;
	}

	public void setExperienceInMonth(Integer experienceInMonth) {
		this.experienceInMonth = experienceInMonth;
	}

	public String getSalaryPaymentType() {
		return salaryPaymentType;
	}

	public void setSalaryPaymentType(String salaryPaymentType) {
		this.salaryPaymentType = salaryPaymentType;
	}

	public String getPermResWardCode() {
		return permResWardCode;
	}

	public void setPermResWardCode(String permResWardCode) {
		this.permResWardCode = permResWardCode;
	}

	public String getPermResDistrictCode() {
		return permResDistrictCode;
	}

	public void setPermResDistrictCode(String permResDistrictCode) {
		this.permResDistrictCode = permResDistrictCode;
	}

	public String getPermResProvinceCode() {
		return permResProvinceCode;
	}

	public void setPermResProvinceCode(String permResProvinceCode) {
		this.permResProvinceCode = permResProvinceCode;
	}

	public String getFamilyBookSeriesType() {
		return familyBookSeriesType;
	}

	public void setFamilyBookSeriesType(String familyBookSeriesType) {
		this.familyBookSeriesType = familyBookSeriesType;
	}

	public String getTempAddrWardCode() {
		return tempAddrWardCode;
	}

	public void setTempAddrWardCode(String tempAddrWardCode) {
		this.tempAddrWardCode = tempAddrWardCode;
	}

	public String getTempAddrDistrictCode() {
		return tempAddrDistrictCode;
	}

	public void setTempAddrDistrictCode(String tempAddrDistrictCode) {
		this.tempAddrDistrictCode = tempAddrDistrictCode;
	}

	public String getTempAddrProvinceCode() {
		return tempAddrProvinceCode;
	}

	public void setTempAddrProvinceCode(String tempAddrProvinceCode) {
		this.tempAddrProvinceCode = tempAddrProvinceCode;
	}

	public Integer getLivingAtTempAddInYear() {
		return livingAtTempAddInYear;
	}

	public void setLivingAtTempAddInYear(Integer livingAtTempAddInYear) {
		this.livingAtTempAddInYear = livingAtTempAddInYear;
	}

	public Integer getLivingAtTempAddInMonth() {
		return livingAtTempAddInMonth;
	}

	public void setLivingAtTempAddInMonth(Integer livingAtTempAddInMonth) {
		this.livingAtTempAddInMonth = livingAtTempAddInMonth;
	}

	public String getAccomType() {
		return accomType;
	}

	public void setAccomType(String accomType) {
		this.accomType = accomType;
	}

	public String getRelationApplicant() {
		return relationApplicant;
	}

	public void setRelationApplicant(String relationApplicant) {
		this.relationApplicant = relationApplicant;
	}

	public Integer getNumberOfDependants() {
		return numberOfDependants;
	}

	public void setNumberOfDependants(Integer numberOfDependants) {
		this.numberOfDependants = numberOfDependants;
	}

	public String getSpouseAddrWardCode() {
		return spouseAddrWardCode;
	}

	public void setSpouseAddrWardCode(String spouseAddrWardCode) {
		this.spouseAddrWardCode = spouseAddrWardCode;
	}

	public String getSpouseAddrDistrictCode() {
		return spouseAddrDistrictCode;
	}

	public void setSpouseAddrDistrictCode(String spouseAddrDistrictCode) {
		this.spouseAddrDistrictCode = spouseAddrDistrictCode;
	}

	public String getSpouseAddrProvinceCode() {
		return spouseAddrProvinceCode;
	}

	public void setSpouseAddrProvinceCode(String spouseAddrProvinceCode) {
		this.spouseAddrProvinceCode = spouseAddrProvinceCode;
	}

	public Double getIncomeSpouse() {
		return incomeSpouse;
	}

	public void setIncomeSpouse(Double incomeSpouse) {
		this.incomeSpouse = incomeSpouse;
	}

	public String getRelationBorrower1() {
		return relationBorrower1;
	}

	public void setRelationBorrower1(String relationBorrower1) {
		this.relationBorrower1 = relationBorrower1;
	}

	public String getRelationBorrower2() {
		return relationBorrower2;
	}

	public void setRelationBorrower2(String relationBorrower2) {
		this.relationBorrower2 = relationBorrower2;
	}

	public Double getAmountOtherCreditLoan() {
		return amountOtherCreditLoan;
	}

	public void setAmountOtherCreditLoan(Double amountOtherCreditLoan) {
		this.amountOtherCreditLoan = amountOtherCreditLoan;
	}

	public Integer getAppNumber() {
		return appNumber;
	}

	public void setAppNumber(Integer appNumber) {
		this.appNumber = appNumber;
	}

	public Double getLoanAmountAfterInsurrance() {
		return loanAmountAfterInsurrance;
	}

	public void setLoanAmountAfterInsurrance(Double loanAmountAfterInsurrance) {
		this.loanAmountAfterInsurrance = loanAmountAfterInsurrance;
	}

	public String getTypeOfGoodsOther() {
		return typeOfGoodsOther;
	}

	public void setTypeOfGoodsOther(String typeOfGoodsOther) {
		this.typeOfGoodsOther = typeOfGoodsOther;
	}

	public Integer getHasInsurrance() {
		return hasInsurrance;
	}

	public void setHasInsurrance(Integer hasInsurrance) {
		this.hasInsurrance = hasInsurrance;
	}

	public String getTypeOfLoan() {
		return typeOfLoan;
	}

	public void setTypeOfLoan(String typeOfLoan) {
		this.typeOfLoan = typeOfLoan;
	}

	public String getDisbursementChannel() {
		return disbursementChannel;
	}

	public void setDisbursementChannel(String disbursementChannel) {
		this.disbursementChannel = disbursementChannel;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAtBank() {
		return atBank;
	}

	public void setAtBank(String atBank) {
		this.atBank = atBank;
	}

	public String getSaleCode() {
		return saleCode;
	}

	public void setSaleCode(String saleCode) {
		this.saleCode = saleCode;
	}

	public String getSpousePosition() {
		return spousePosition;
	}

	public void setSpousePosition(String spousePosition) {
		this.spousePosition = spousePosition;
	}

	public Double getSpouseIncome() {
		return spouseIncome;
	}

	public void setSpouseIncome(Double spouseIncome) {
		this.spouseIncome = spouseIncome;
	}

	public Double getInsurranceFee() {
		return insurranceFee;
	}

	public void setInsurranceFee(Double insurranceFee) {
		this.insurranceFee = insurranceFee;
	}

	public Double getAverageElectricBill() {
		return averageElectricBill;
	}

	public void setAverageElectricBill(Double averageElectricBill) {
		this.averageElectricBill = averageElectricBill;
	}

	public String getInsuranceTerm() {
		return insuranceTerm;
	}

	public void setInsuranceTerm(String insuranceTerm) {
		this.insuranceTerm = insuranceTerm;
	}

	public Double getAverageAccountBalance() {
		return averageAccountBalance;
	}

	public void setAverageAccountBalance(Double averageAccountBalance) {
		this.averageAccountBalance = averageAccountBalance;
	}

	public Float getAgencyCode() {
		return agencyCode;
	}

	public void setAgencyCode(Float agencyCode) {
		this.agencyCode = agencyCode;
	}

	public Byte getTypeOfProduct() {
		return typeOfProduct;
	}

	public void setTypeOfProduct(Byte typeOfProduct) {
		this.typeOfProduct = typeOfProduct;
	}

	public Double getInsuranceRate() {
		return insuranceRate;
	}

	public void setInsuranceRate(Double insuranceRate) {
		this.insuranceRate = insuranceRate;
	}

	public String getInsuranceType() {
		return insuranceType;
	}

	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}

	public String getPilotName() {
		return pilotName;
	}

	public void setPilotName(String pilotName) {
		this.pilotName = pilotName;
	}

	public String getCustomerExpense() {
		return customerExpense;
	}

	public void setCustomerExpense(String customerExpense) {
		this.customerExpense = customerExpense;
	}

	public Double getInterest() {
		return interest;
	}

	public void setInterest(Double interest) {
		this.interest = interest;
	}

	public Double getYearInterest() {
		return yearInterest;
	}

	public void setYearInterest(Double yearInterest) {
		this.yearInterest = yearInterest;
	}

	public Double getDownPaymentRate() {
		return downPaymentRate;
	}

	public void setDownPaymentRate(Double downPaymentRate) {
		this.downPaymentRate = downPaymentRate;
	}

	public Integer getTemporaryResidence() {
		return temporaryResidence;
	}

	public void setTemporaryResidence(Integer temporaryResidence) {
		this.temporaryResidence = temporaryResidence;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getLoanStartDate() {
		return loanStartDate;
	}

	public void setLoanStartDate(String loanStartDate) {
		this.loanStartDate = loanStartDate;
	}

	public String getCustCompanyCat() {
		return custCompanyCat;
	}

	public void setCustCompanyCat(String custCompanyCat) {
		this.custCompanyCat = custCompanyCat;
	}

	public Integer getWageStatement() {
		return wageStatement;
	}

	public void setWageStatement(Integer wageStatement) {
		this.wageStatement = wageStatement;
	}

	public Integer getCustAddress() {
		return custAddress;
	}

	public void setCustAddress(Integer custAddress) {
		this.custAddress = custAddress;
	}

	public Integer getHomeCompAdress() {
		return homeCompAdress;
	}

	public void setHomeCompAdress(Integer homeCompAdress) {
		this.homeCompAdress = homeCompAdress;
	}

	public Integer getSelfEmployed() {
		return selfEmployed;
	}

	public void setSelfEmployed(Integer selfEmployed) {
		this.selfEmployed = selfEmployed;
	}

	public Integer getAnnualFeeInsu() {
		return annualFeeInsu;
	}

	public void setAnnualFeeInsu(Integer annualFeeInsu) {
		this.annualFeeInsu = annualFeeInsu;
	}

	public String getProductGroup() {
		return productGroup;
	}

	public void setProductGroup(String productGroup) {
		this.productGroup = productGroup;
	}

	public String getIdentityType() {
		return identityType;
	}

	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}

	public String getMilitaryLevel() {
		return militaryLevel;
	}

	public void setMilitaryLevel(String militaryLevel) {
		this.militaryLevel = militaryLevel;
	}

	public String getCccdExpirationDate() {
		return cccdExpirationDate;
	}

	public void setCccdExpirationDate(String cccdExpirationDate) {
		this.cccdExpirationDate = cccdExpirationDate;
	}

	public String getCmndOrCmqdIssueDate() {
		return cmndOrCmqdIssueDate;
	}

	public void setCmndOrCmqdIssueDate(String cmndOrCmqdIssueDate) {
		this.cmndOrCmqdIssueDate = cmndOrCmqdIssueDate;
	}

	public String getDataEntryDate() {
		return dataEntryDate;
	}

	public void setDataEntryDate(String dataEntryDate) {
		this.dataEntryDate = dataEntryDate;
	}

	public Integer getHasCourier() {
		return hasCourier;
	}

	public void setHasCourier(Integer hasCourier) {
		this.hasCourier = hasCourier;
	}

	public String getTemSamePermDist() {
		return temSamePermDist;
	}

	public void setTemSamePermDist(String temSamePermDist) {
		this.temSamePermDist = temSamePermDist;
	}

	public Integer getEvnBillName() {
		return evnBillName;
	}

	public void setEvnBillName(Integer evnBillName) {
		this.evnBillName = evnBillName;
	}


	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getTaxChapterGroup() {
		return taxChapterGroup;
	}

	public void setTaxChapterGroup(String taxChapterGroup) {
		this.taxChapterGroup = taxChapterGroup;
	}

	public String getCompNameHave() {
		return compNameHave;
	}

	public void setCompNameHave(String compNameHave) {
		this.compNameHave = compNameHave;
	}

	public String getCompNameHave2() {
		return compNameHave2;
	}

	public void setCompNameHave2(String compNameHave2) {
		this.compNameHave2 = compNameHave2;
	}

	public String getCompNameNotHave() {
		return compNameNotHave;
	}

	public void setCompNameNotHave(String compNameNotHave) {
		this.compNameNotHave = compNameNotHave;
	}

	public String getLenghtTaxNumber() {
		return lenghtTaxNumber;
	}

	public void setLenghtTaxNumber(String lenghtTaxNumber) {
		this.lenghtTaxNumber = lenghtTaxNumber;
	}

	public String getEconomicTypeHave() {
		return economicTypeHave;
	}

	public void setEconomicTypeHave(String economicTypeHave) {
		this.economicTypeHave = economicTypeHave;
	}

	public String getCompType() {
		return compType;
	}

	public int getTopComp() {
		return topComp;
	}

	public int getTopBranch() {
		return topBranch;
	}

	public int getOperationTime() {
		return operationTime;
	}

	public String getCicInfo() {
		return cicInfo;
	}

	public void setCompType(String compType) {
		this.compType = compType;
	}

	public void setTopComp(int topComp) {
		this.topComp = topComp;
	}

	public void setTopBranch(int topBranch) {
		this.topBranch = topBranch;
	}

	public void setOperationTime(int operationTime) {
		this.operationTime = operationTime;
	}

	public void setCicInfo(String cicInfo) {
		this.cicInfo = cicInfo;
	}

	public int getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(int recordStatus) {
		this.recordStatus = recordStatus;
	}
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Integer getNumberOfRelationOrganize() {
		return numberOfRelationOrganize;
	}

	public void setNumberOfRelationOrganize(Integer numberOfRelationOrganize) {
		this.numberOfRelationOrganize = numberOfRelationOrganize;
	}
	
	public Integer getNumberOfRelationFinanceCompany() {
		return numberOfRelationFinanceCompany;
	}

	public void setNumberOfRelationFinanceCompany(Integer numberOfRelationFinanceCompany) {
		this.numberOfRelationFinanceCompany = numberOfRelationFinanceCompany;
	}

	public Double getTotalOutstandingLoanAtCIC() {
		return totalOutstandingLoanAtCIC;
	}

	public void setTotalOutstandingLoanAtCIC(Double totalOutstandingLoanAtCIC) {
		this.totalOutstandingLoanAtCIC = totalOutstandingLoanAtCIC;
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

	public Double getAnnualFeeLifeInsurance() {
		return annualFeeLifeInsurance;
	}

	public void setAnnualFeeLifeInsurance(Double annualFeeLifeInsurance) {
		this.annualFeeLifeInsurance = annualFeeLifeInsurance;
	}

	public String getStartDatePreviousLoan() {
		return startDatePreviousLoan;
	}

	public void setStartDatePreviousLoan(String startDatePreviousLoan) {
		this.startDatePreviousLoan = startDatePreviousLoan;
	}

	public Integer getLoanTenorOtherCredit() {
		return loanTenorOtherCredit;
	}

	public void setLoanTenorOtherCredit(Integer loanTenorOtherCredit) {
		this.loanTenorOtherCredit = loanTenorOtherCredit;
	}
	
	public String getEndDatePreviousLoan() {
		return endDatePreviousLoan;
	}

	public void setEndDatePreviousLoan(String endDatePreviousLoan) {
		this.endDatePreviousLoan = endDatePreviousLoan;
	}

	public String getParticipants() {
		return participants;
	}

	public void setParticipants(String participants) {
		this.participants = participants;
	}
	
	public Long getNumberBills() {
		return numberBills;
	}

	public void setNumberBills(Long numberBills) {
		this.numberBills = numberBills;
	}

	public Double getAvgPayValueOnBills() {
		return avgPayValueOnBills;
	}

	public void setAvgPayValueOnBills(Double avgPayValueOnBills) {
		this.avgPayValueOnBills = avgPayValueOnBills;
	}

	public String getPersonNameBill() {
		return personNameBill;
	}

	public void setPersonNameBill(String personNameBill) {
		this.personNameBill = personNameBill;
	}

	public String getRelationshipWithApplicant() {
		return relationshipWithApplicant;
	}

	public void setRelationshipWithApplicant(String relationshipWithApplicant) {
		this.relationshipWithApplicant = relationshipWithApplicant;
	}

	public HashMap<String, String> getFileUploadedList() {
		return fileUploadedList;
	}

	public void setFileUploadedList(HashMap<String, String> fileUploadedList) {
		this.fileUploadedList = fileUploadedList;
	}

	public String getTypeBills() {
		return typeBills;
	}

	public void setTypeBills(String typeBills) {
		this.typeBills = typeBills;
	}

	public String getCpeProvince() {
		return cpeProvince;
	}

	public void setCpeProvince(String cpeProvince) {
		this.cpeProvince = cpeProvince;
	}

	public String getCpeDistrict() {
		return cpeDistrict;
	}

	public void setCpeDistrict(String cpeDistrict) {
		this.cpeDistrict = cpeDistrict;
	}

	public String getCurrentAddressSpouse() {
		return currentAddressSpouse;
	}

	public void setCurrentAddressSpouse(String currentAddressSpouse) {
		this.currentAddressSpouse = currentAddressSpouse;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getTelcoCode() {
		return telcoCode;
	}

	public void setTelcoCode(String telcoCode) {
		this.telcoCode = telcoCode;
	}

	public Long getMinScore() {
		return minScore;
	}

	public void setMinScore(Long minScore) {
		this.minScore = minScore;
	}
	
	public String getCheckAgeDate() {
		return checkAgeDate;
	}

	public void setCheckAgeDate(String checkAgeDate) {
		this.checkAgeDate = checkAgeDate;
	}

	public Long getScoreCard() {
		return scoreCard;
	}

	public void setScoreCard(Long scoreCard) {
		this.scoreCard = scoreCard;
	}

	public String getS37Result() {
		return s37Result;
	}

	public void setS37Result(String s37Result) {
		this.s37Result = s37Result;
	}

	public String getS37SpouseState() {
		return s37SpouseState;
	}

	public void setS37SpouseState(String s37SpouseState) {
		this.s37SpouseState = s37SpouseState;
	}

	public String getPcbState() {
		return pcbState;
	}

	public void setPcbState(String pcbState) {
		this.pcbState = pcbState;
	}

	public String getScoreStatus() {
		return scoreStatus;
	}

	public void setScoreStatus(String scoreStatus) {
		this.scoreStatus = scoreStatus;
	}

	public String getTaxCustomerStatus() {
		return taxCustomerStatus;
	}

	public void setTaxCustomerStatus(String taxCustomerStatus) {
		this.taxCustomerStatus = taxCustomerStatus;
	}

	public String getCicStatus() {
		return cicStatus;
	}

	public void setCicStatus(String cicStatus) {
		this.cicStatus = cicStatus;
	}

	public String getOtherLookup() {
		return otherLookup;
	}

	public void setOtherLookup(String otherLookup) {
		this.otherLookup = otherLookup;
	}

	public Integer getTotalCustomerCall() {
		return totalCustomerCall;
	}

	public void setTotalCustomerCall(Integer totalCustomerCall) {
		this.totalCustomerCall = totalCustomerCall;
	}

	public Integer getTotalReferenceCall() {
		return totalReferenceCall;
	}

	public void setTotalReferenceCall(Integer totalReferenceCall) {
		this.totalReferenceCall = totalReferenceCall;
	}

	public String getInsuranceCardStatus() {
		return insuranceCardStatus;
	}

	public void setInsuranceCardStatus(String insuranceCardStatus) {
		this.insuranceCardStatus = insuranceCardStatus;
	}

	public List<String> getListTypeOfGoods() {
		return listTypeOfGoods;
	}

	public void setListTypeOfGoods(List<String> listTypeOfGoods) {
		this.listTypeOfGoods = listTypeOfGoods;
	}

	public List<String> getListBrands() {
		return listBrands;
	}

	public void setListBrands(List<String> listBrands) {
		this.listBrands = listBrands;
	}

	public String getIncomeFromSalary() {
		return incomeFromSalary;
	}

	public void setIncomeFromSalary(String incomeFromSalary) {
		this.incomeFromSalary = incomeFromSalary;
	}

	public String getMobileUserName() {
		return mobileUserName;
	}

	public void setMobileUserName(String mobileUserName) {
		this.mobileUserName = mobileUserName;
	}

	public String getPolicyRegulationAppraisal() {
		return policyRegulationAppraisal;
	}

	public void setPolicyRegulationAppraisal(String policyRegulationAppraisal) {
		this.policyRegulationAppraisal = policyRegulationAppraisal;
	}

	public Long getTsScore() {
		return tsScore;
	}

	public void setTsScore(Long tsScore) {
		this.tsScore = tsScore;
	}

	public Long getCicScore() {
		return cicScore;
	}

	public void setCicScore(Long cicScore) {
		this.cicScore = cicScore;
	}
	
	public Long getLgTsScore() {
		return lgTsScore;
	}

	public void setLgTsScore(Long lgTsScore) {
		this.lgTsScore = lgTsScore;
	}

	public Long getLgCicScore() {
		return lgCicScore;
	}

	public void setLgCicScore(Long lgCicScore) {
		this.lgCicScore = lgCicScore;
	}

	public String getTsScoreDate() {
		return tsScoreDate;
	}

	public void setTsScoreDate(String tsScoreDate) {
		this.tsScoreDate = tsScoreDate;
	}

	public String getCicScoreDate() {
		return cicScoreDate;
	}

	public void setCicScoreDate(String cicScoreDate) {
		this.cicScoreDate = cicScoreDate;
	}

	public String getLgTsScoreDate() {
		return lgTsScoreDate;
	}

	public void setLgTsScoreDate(String lgTsScoreDate) {
		this.lgTsScoreDate = lgTsScoreDate;
	}

	public String getLgCicScoreDate() {
		return lgCicScoreDate;
	}

	public void setLgCicScoreDate(String lgCicScoreDate) {
		this.lgCicScoreDate = lgCicScoreDate;
	}

	public Integer getTypeScore() {
		return typeScore;
	}

	public void setTypeScore(Integer typeScore) {
		this.typeScore = typeScore;
	}
	
	public Integer getContractType() {
		return contractType;
	}

	public void setContractType(Integer contractType) {
		this.contractType = contractType;
	}

	public boolean checkListNotContains(HashMap<String, String> input, List<String> listFile) {
		if (input == null || input.isEmpty())
			return true;
		
		for (Map.Entry<String, String> entry : input.entrySet()) {
			if (entry != null && entry.getKey() != null && entry.getKey().length() > 0 &&  entry.getValue() != null && entry.getValue().trim().length() > 0 &&
					listFile.contains(entry.getKey())) {
				return false;
			}
		}
		return true;
	}

	public String getSaleChannels() {
		return saleChannels;
	}

	public void setSaleChannels(String saleChannels) {
		this.saleChannels = saleChannels;
	}

	public Integer getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(Integer goodsType) {
		this.goodsType = goodsType;
	}
				
}
