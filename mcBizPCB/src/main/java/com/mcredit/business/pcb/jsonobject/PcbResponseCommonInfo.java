package com.mcredit.business.pcb.jsonobject;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PcbResponseCommonInfo {
	public String cusNameRequest;
	public String identityRequest;
	public String birthDateRequest;
	public String mainAddressRequest;
	public String genderRequest;
	public String cusName;
	public String pcbCode;
	public String identityCode;
	public String identityCode2;
	public String birthDate;
	public String mainAddress;
	public String additionalAddress;
	public List<Document> documentList;
	public String referenceNumber;
	public String countBank;
	public String loanMainTotal;
	public String loanGuaranteeTotal;
	public String finalcialCompanyAmount;
	public String finalComLoanTotal;
	public String creditComRequestAmount;
	public String creditComRejectAmount;
	public String highest12MonthLoan;
	public String highest3YearLoan;
	public String outOfDateMax;
	
	public PcbResponseCommonInfo(String cusNameRequest,String identityRequest,String birthDateRequest,String mainAddressRequest,String genderRequest,String cusName, String pcbCode, String identityCode,String identityCode2, String birthDate, String mainAddress, String additionalAddress, List<Document> documentList, String referenceNumber,
			String countBank, String loanMainTotal, String loanGuaranteeTotal, String finalcialCompanyAmount, String finalComLoanTotal, String creditComRequestAmount, String creditComRejectAmount,
			String highest12MonthLoan, String highest3YearLoan, String outOfDateMax) {
		super();
		this.cusNameRequest = cusNameRequest;
		this.identityRequest =identityRequest;
		this.birthDateRequest = birthDateRequest;
		this.mainAddressRequest = mainAddressRequest;
		this.genderRequest = genderRequest;
		this.cusName = cusName;
		this.pcbCode = pcbCode;
		this.identityCode = identityCode;
		this.identityCode2 = identityCode2;
		this.birthDate = birthDate;
		this.mainAddress = mainAddress;
		this.additionalAddress = additionalAddress;
		this.documentList = documentList;
		this.referenceNumber = referenceNumber;
		this.countBank = countBank;
		this.loanMainTotal = loanMainTotal;
		this.loanGuaranteeTotal = loanGuaranteeTotal;
		this.finalcialCompanyAmount = finalcialCompanyAmount;
		this.finalComLoanTotal = finalComLoanTotal;
		this.creditComRequestAmount = creditComRequestAmount;
		this.creditComRejectAmount = creditComRejectAmount;
		this.highest12MonthLoan = highest12MonthLoan;
		this.highest3YearLoan = highest3YearLoan;
		this.outOfDateMax = outOfDateMax;
	}
	
	

}
