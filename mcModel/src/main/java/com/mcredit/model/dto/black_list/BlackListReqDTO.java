package com.mcredit.model.dto.black_list;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



// To ignore any unknown properties in JSON input without exception:
@JsonIgnoreProperties({ "internalId", "secretKey" })
public class BlackListReqDTO {
	private String CitizenId;

	public String getCitizenId() {
		return CitizenId;
	}

	public void setCitizenId(String citizenId) {
		CitizenId = citizenId;
	}

	
	
	
//	private String CitizenIDOld;
//	private String MilitaryId;
//	private String CompanyTaxNumber;
//	
//	
//	public String getCitizenIDOld() {
//		return CitizenIDOld;
//	}
//	public void setCitizenIDOld(String citizenIDOld) {
//		CitizenIDOld = citizenIDOld;
//	}
//	public String getMilitaryId() {
//		return MilitaryId;
//	}
//	public void setMilitaryId(String militaryId) {
//		MilitaryId = militaryId;
//	}
//	public String getCompanyTaxNumber() {
//		return CompanyTaxNumber;
//	}
//	public void setCompanyTaxNumber(String companyTaxNumber) {
//		CompanyTaxNumber = companyTaxNumber;
//	}
//	
	
}
