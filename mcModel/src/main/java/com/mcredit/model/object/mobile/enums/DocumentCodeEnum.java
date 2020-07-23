package com.mcredit.model.object.mobile.enums;

public enum DocumentCodeEnum {
	AppointDecision("AppointDecision"),
	BankAccountStatement("BankAccountStatement"),
	BirthCertificate("BirthCertificate"),
	BusinessConfirmation("BusinessConfirmation"),
	BusinessFeeInvoice("BusinessFeeInvoice"),
	BusinessLicense("BusinessLicense"),
	BusinessLocationPhoto("BusinessLocationPhoto"),
	BusinessPlaceProof("BusinessPlaceProof"),
	BusinessTaxReceipt("BusinessTaxReceipt"),
	CableTelevisionBill("CableTelevisionBill"),
	CivicIdentity("CivicIdentity"),
	CustomerInformationSheet("CustomerInformationSheet"),
	DrivingLicense("DrivingLicense"),
	ElectricBill("ElectricBill"),
	EmployeConfirmation("EmployeConfirmation"),
	FacePhoto("FacePhoto"),
	FamilyBook("FamilyBook"),
	HealthInsuranceCard("HealthInsuranceCard"),
	InsuranceCard("InsuranceCard"),
	HomeMortgageContract("HomeMortgageContract"),
	HomeOwnershipCertification("HomeOwnershipCertification"),
	HomePhoneBill("HomePhoneBill"),
	IdentityCard("IdentityCard"),
	IncreaseSalaryDecision("IncreaseSalaryDecision"),
	InsuranceFeeInvoice("InsuranceFeeInvoice"),
	InsuranceFeeOtherDocument("InsuranceFeeOtherDocument"),
	InsuranceFeePaymentConfirmation("InsuranceFeePaymentConfirmation"),
	InsuranceFeeReceipt("InsuranceFeeReceipt"),
	InternetBill("InternetBill"),
	JobCofirmationInfo("JobCofirmationInfo"),
	LabourContract("LabourContract"),
	LifeInsuranceContract("LifeInsuranceContract"),
	MarriageCertificate("MarriageCertificate"),
	MilitaryIdentity("MilitaryIdentity"),
	MobilePhoneBill("MobilePhoneBill"),
	NotarizedHomeSalesContract("NotarizedHomeSalesContract"),
	Other("Other"),
	OtherProofOfIncome("OtherProofOfIncome"),
	OtherProofOfWork("OtherProofOfWork"),
	Passport("Passport"),
	PromotionDecision("PromotionDecision"),
	ResidenceConfirmationOfHeadUnit("ResidenceConfirmationOfHeadUnit"),
	SalaryConfirmation("SalaryConfirmation"),
	SalarySlip("SalarySlip"),
	SalaryStatement("SalaryStatement"),
	SalaryStatementWithPayeeName("SalaryStatementWithPayeeName"),
	StatementPaymentAccount("StatementPaymentAccount"),
	TaxRegistrationCertificate("TaxRegistrationCertificate"),
	TemporaryResidenceBook("TemporaryResidenceBook"),
	TemporaryResidenceCard("TemporaryResidenceCard"),
	TemporaryResidenceConfirmation("TemporaryResidenceConfirmation"),
	WaterBill("WaterBill"),
	WorkTransferDecision("WorkTransferDecision")
	;

	private final String value;

	DocumentCodeEnum(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
	
	public static DocumentCodeEnum from(String text) {
        for (DocumentCodeEnum b : DocumentCodeEnum.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
