
package com.mcredit.model.object;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement

public class Applicant {
	/**
	 * @author oanhlt.ho
	 */

	private String schemeProduct;
	
	private String specialCode;
	
	private String mucDichVay;
	
	private double totalLoanAtCreditCompany;
	
	private String gender;

	private double age;

	private String education;

	private String maritalStatus;

	private String job;

	private double experience;


	private String salaryPayment;

	private String accommodationType;

	private double eiExpenseIncome;

	private String paymentHistory;
	
	private double totalScore;
	/** so tien de nghi vay von **/

	private double loanAmount;
	/** Phan tram tra truoc **/
	
	private double downPayment;

	/** thoi gian song tai dia chi sinh song - don vi thang **/

	private double livingTimeAtTemporaryAddress;

	/** so to chuc tin dung **/
	
	private int totalLoanAtCreditandOtherInstitutions;
	
	/** tong du no va han muc **/
	
	private double totalLoanAmountAtCreditInstitutions;

	 private String typeOfLoan;
	/** hang **/

	private String rating = "";

	/** pass/fail **/

	private String cutOff = "";
	
	/** Quy dinh chinh sach - pass/fail **/
	private String policyRegulation;

	/** gian lan yes/no**/
	private String fraud;
	
	/** goi y cho Call **/
	private String proposalForCall = "";
	/** goi y cho Approve **/
	private String proposalForApprove = "";
	private String proposalForDataChecker= "";
//	private String messageForApprove = "";
	private Set<String> messageForApprove = new HashSet<String>();
	private Integer isNewScore; 
	private int totalLoanAtCreditInstitutionsandOtherInstitutions;
	
	private String appNumber;
	
	// rq887: y kien cua data corrector (lay tai man hinh DC)
	//	1: chuyen tham dinh dien thoai
	//	4: chuyen phe duyet
	private Integer dataCorrectorDecision;
	
	// rq942
	private double dtiApprover;
	private Long tsScore;			// Diem Trusting Social
	private Long cicScore;			// Diem CIC Data
	
	// rq1038
	private String telcoCode;

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public double getAge() {
		return age;
	}

	public void setAge(double age) {
		this.age = age;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public double getExperience() {
		return experience;
	}

	public void setExperience(double experience) {
		this.experience = experience;
	}


	public String getSalaryPayment() {
		return salaryPayment;
	}

	public void setSalaryPayment(String salaryPayment) {
		this.salaryPayment = salaryPayment;
	}

	public String getAccommodationType() {
		return accommodationType;
	}

	public void setAccommodationType(String accommodationType) {
		this.accommodationType = accommodationType;
	}

	public double getEiExpenseIncome() {
		return eiExpenseIncome;
	}

	public void setEiExpenseIncome(double eiExpenseIncome) {
		this.eiExpenseIncome = eiExpenseIncome;
	}

	public String getPaymentHistory() {
		return paymentHistory;
	}

	public void setPaymentHistory(String paymentHistory) {
		this.paymentHistory = paymentHistory;
	}

	public double getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(double totalScore) {
		this.totalScore = totalScore;
	}

	public String getTypeOfLoan() {
		return typeOfLoan;
	}

	public void setTypeOfLoan(String typeOfLoan) {
		this.typeOfLoan = typeOfLoan;
		/*this.schemeProduct =typeOfLoan;*/   
	}

	public double getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(double loanAmount) {
		this.loanAmount = loanAmount;
	}

	public double getDownPayment() {
		return downPayment;
	}

	public void setDownPayment(double downPayment) {
		this.downPayment = downPayment;
	}

	public double getLivingTimeAtTemporaryAddress() {
		return livingTimeAtTemporaryAddress;
	}

	public void setLivingTimeAtTemporaryAddress(double livingTimeAtTemporaryAddress) {
		this.livingTimeAtTemporaryAddress = livingTimeAtTemporaryAddress;
	}

	public int getTotalLoanAtCreditandOtherInstitutions() {
		return totalLoanAtCreditandOtherInstitutions;
	}

	public void setTotalLoanAtCreditandOtherInstitutions(int totalLoanAtCreditandOtherInstitutions) {
		this.totalLoanAtCreditandOtherInstitutions = totalLoanAtCreditandOtherInstitutions;
	}

	public double getTotalLoanAmountAtCreditInstitutions() {
		return totalLoanAmountAtCreditInstitutions;
	}

	public void setTotalLoanAmountAtCreditInstitutions(double totalLoanAmountAtCreditInstitutions) {
		this.totalLoanAmountAtCreditInstitutions = totalLoanAmountAtCreditInstitutions;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public void addScore(double score) {
		this.totalScore += score;
	}

	public String getCutOff() {
		return cutOff;
	}

	public void setCutOff(String cutOff) {
		this.cutOff = cutOff;
	}

	public String getPolicyRegulation() {
		return policyRegulation;
	}

	public void setPolicyRegulation(String policyRegulation) {
		this.policyRegulation = policyRegulation;
	}

	public String getFraud() {
		return fraud;
	}

	public void setFraud(String fraud) {
		this.fraud = fraud;
	}

	public String getProposalForCall() {
		return proposalForCall;
	}

	public void setProposalForCall(String proposalForCall) {
		this.proposalForCall = proposalForCall;
	}

	public String getProposalForApprove() {
		return proposalForApprove;
	}

	public void setProposalForApprove(String proposalForApprove) {
		this.proposalForApprove = proposalForApprove;
	}

	public String getSchemeProduct() {
		return schemeProduct;
	}

	public void setSchemeProduct(String schemeProduct) {
		this.schemeProduct = schemeProduct;
	}

	public double getTotalLoanAtCreditCompany() {
		return totalLoanAtCreditCompany;
	}

	public void setTotalLoanAtCreditCompany(double totalLoanAtCreditCompany) {
		this.totalLoanAtCreditCompany = totalLoanAtCreditCompany;
	}

	public String getProposalForDataChecker() {
		return proposalForDataChecker;
	}

	public void setProposalForDataChecker(String proposalForDataChecker) {
		this.proposalForDataChecker = proposalForDataChecker;
	}
	
//	public String getMessageForApprove() {
//		return messageForApprove;
//	}

	public void setMessageForApprove(String messageForApprove) {
		this.messageForApprove.clear();
		this.messageForApprove.add(messageForApprove);
	}
	
	public void addMessageForApprove(String msg) {
		this.messageForApprove.add(msg);
	}
	
	public void removeMessageForApprove(String msg) {
		this.messageForApprove.remove(msg);
	}
	
	public Set<String> getMessageForApprove() {
		return this.messageForApprove;
	}

	public String getSpecialCode() {
		return specialCode;
	}

	public void setSpecialCode(String specialCode) {
		this.specialCode = specialCode;
	}

	public String getMucDichVay() {
		return mucDichVay;
	}   

	public void setMucDichVay(String mucDichVay) {
		this.mucDichVay = mucDichVay;
	}

	public String getAppNumber() {
		return appNumber;
	}

	public void setAppNumber(String appNumber) {
		this.appNumber = appNumber;
	}

	public Integer getIsNewScore() {
		return isNewScore;
	}  

	public void setIsNewScore(Integer isNewScore) {
		this.isNewScore = isNewScore;
	}

	public int getTotalLoanAtCreditInstitutionsandOtherInstitutions() {
		return this.totalLoanAtCreditandOtherInstitutions;
	}

	public void setTotalLoanAtCreditInstitutionsandOtherInstitutions(
			int totalLoanAtCreditInstitutionsandOtherInstitutions) {
		this.totalLoanAtCreditInstitutionsandOtherInstitutions = this.totalLoanAtCreditandOtherInstitutions;
	}

	public Integer getDataCorrectorDecision() {
		return dataCorrectorDecision;
	}

	public void setDataCorrectorDecision(Integer dataCorrectorDecision) {
		this.dataCorrectorDecision = dataCorrectorDecision;
	}

	public double getDtiApprover() {
		return dtiApprover;
	}

	public void setDtiApprover(double dtiApprover) {
		this.dtiApprover = dtiApprover;
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

	public String getTelcoCode() {
		return telcoCode;
	}

	public void setTelcoCode(String telcoCode) {
		this.telcoCode = telcoCode;
	}

	public static final String MALE = "Nam";
	public static final String FEMALE = "Nu";
	public static final String INSTALLMENT_LOAN = "InstallmentLoan";
	public static final String CASH_LOAN = "CashLoan";

	@Override
	public String toString() {
                return "Applicant [gender=" + this.gender + ", age=" + this.age + ", education=" + this.education + ", maritalStatus=" 
                        + this.maritalStatus + ", job=" + this.job + ", experience=" + this.experience + ", salaryPayment=" + this.salaryPayment 
                        + ", accommodationType=" + this.accommodationType + ", eiExpenseIncome=" + this.eiExpenseIncome + ", paymentHistory=" 
                        + this.paymentHistory + ", totalScore=" + this.totalScore + ", loanAmount=" + this.loanAmount + ", downPayment=" 
                        + this.downPayment + ", livingTimeAtTemporaryAddress=" + this.livingTimeAtTemporaryAddress + ", totalLoanAtCreditInstitutions=" 
                        + this.totalLoanAtCreditandOtherInstitutions + ", typeOfLoan=" + this.typeOfLoan + ", rating=" + this.rating + ", cutOff=" + this.cutOff 
                        + ", policyRegulation=" + this.policyRegulation + ", fraud=" + this.fraud + ", proposalForCall=" + this.proposalForCall + ", proposalForApprove=" 
                        + this.proposalForApprove + ", specialCode=" + this.specialCode + ", mucDichVay=" + this.mucDichVay + " ,schemeProduct = "+ this.schemeProduct
                        + " ,totalLoanAtCreditCompany = "+ this.totalLoanAtCreditCompany+" ,appNumber = "+ this.appNumber+ " ,isNewScore = "+ this.isNewScore 
                        + " ,totalLoanAtCreditInstitutionsandOtherInstitutions = "+ this.totalLoanAtCreditInstitutionsandOtherInstitutions
                        + ", dtiApprover = " + this.dtiApprover + ", tsScore = " + this.tsScore + ", cicScore = " + this.cicScore + ", telcoCode = " + this.telcoCode + "]";
	}
	
}
