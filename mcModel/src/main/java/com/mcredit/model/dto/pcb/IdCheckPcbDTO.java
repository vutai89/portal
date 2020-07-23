package com.mcredit.model.dto.pcb;

public class IdCheckPcbDTO {
	public String id;
	public String idCard;
	public String highestLoan12Month;
	public String highestLoan36Month;
	public String finalcialCompanyAmount;
	public String finalComLoanTotal;
	public String countBank;
	public String loanMainTotal;
	public String returnMes;

	public IdCheckPcbDTO(String id,String idCard, String highestLoan12Month, String highestLoan36Month, String finalcialCompanyAmount, String finalComLoanTotal, String countBank, String loanMainTotal,String returnMes) {
		super();
		this.id = id;
		this.idCard = idCard;
		this.highestLoan12Month = highestLoan12Month;
		this.highestLoan36Month = highestLoan36Month;
		this.finalcialCompanyAmount = finalcialCompanyAmount;
		this.finalComLoanTotal = finalComLoanTotal;
		this.countBank = countBank;
		this.loanMainTotal = loanMainTotal;
		this.returnMes = returnMes;
	}

	public IdCheckPcbDTO() {

	}
	public IdCheckPcbDTO(String idCard,String returnMes) {
		this.idCard = idCard;
		this.returnMes = returnMes;
	}
}
