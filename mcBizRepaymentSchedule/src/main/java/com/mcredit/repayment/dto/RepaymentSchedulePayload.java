package com.mcredit.repayment.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.mcredit.util.DateUtil;

public class RepaymentSchedulePayload {
	public String contractNo;
	public String customerNo;
	public Date valueDate;
	public Date maturityDate;
	public Double intRate;
	public String type;
	public String status;
	public Double currentOutstanding;
	public Integer tenor;
	
	public RepaymentSchedulePayload() {}
	
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public Date getValueDate() {
		return valueDate;
	}
	public void setValueDate(Date valueDate) {
		this.valueDate = valueDate;
	}
	public Date getMaturityDate() {
		return maturityDate;
	}
	public void setMaturityDate(Date maturityDate) {
		this.maturityDate = maturityDate;
	}
	public Double getIntRate() {
		return intRate;
	}

	public void setIntRate(Double intRate) {
		this.intRate = intRate;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Double getCurrentOutstanding() {
		return currentOutstanding;
	}

	public void setCurrentOutstanding(Double currentOutstanding) {
		this.currentOutstanding = currentOutstanding;
	}

	public Integer getTenor() {
		return tenor;
	}
	public void setTenor(Integer tenor) {
		this.tenor = tenor;
	}
	
	
}
