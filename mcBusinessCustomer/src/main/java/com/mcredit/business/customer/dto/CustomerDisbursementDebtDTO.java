package com.mcredit.business.customer.dto;

import com.mcredit.util.StringUtils;

public class CustomerDisbursementDebtDTO {
	private String totalPaymentAmount;
	private String totalOSBalance;

	public Long getTotalPaymentAmount() {
		if (StringUtils.isNullOrEmpty(this.totalPaymentAmount) || this.totalPaymentAmount.equals("null")) {
			return 0L;
		}
		return Long.valueOf(this.totalPaymentAmount);
	}

	public void setTotalPaymentAmount(String totalPaymentAmount) {
		this.totalPaymentAmount = totalPaymentAmount;
	}

	public CustomerDisbursementDebtDTO(String totalPaymentAmount) {
		super();
		this.totalPaymentAmount = totalPaymentAmount;
	}

	public String getTotalOSBalance() {
		return totalOSBalance;
	}

	public void setTotalOSBalance(String totalOSBalance) {
		this.totalOSBalance = totalOSBalance;
	}

	public CustomerDisbursementDebtDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

}
