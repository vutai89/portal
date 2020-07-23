package com.mcredit.model.dto.pcb.request.input;

public class Instalment {
	
	private Long AmountFinancedCapital;			// so tien vay
	private Integer NumTotalInstalment;			// ky han vay
	private String CodPaymentPeriodicity;		// chu ki thanh toan goc
	
	public Long getAmountFinancedCapital() {
		return AmountFinancedCapital;
	}
	
	public void setAmountFinancedCapital(Long amountFinancedCapital) {
		AmountFinancedCapital = amountFinancedCapital;
	}
	
	public Integer getNumTotalInstalment() {
		return NumTotalInstalment;
	}
	
	public void setNumTotalInstalment(Integer numTotalInstalment) {
		NumTotalInstalment = numTotalInstalment;
	}
	
	public String getCodPaymentPeriodicity() {
		return CodPaymentPeriodicity;
	}
	
	public void setCodPaymentPeriodicity(String codPaymentPeriodicity) {
		CodPaymentPeriodicity = codPaymentPeriodicity;
	}

}
