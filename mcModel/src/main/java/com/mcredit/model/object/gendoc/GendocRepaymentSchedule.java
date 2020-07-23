package com.mcredit.model.object.gendoc;

public class GendocRepaymentSchedule {
	public String valueDate;
	public Double intRate;
	public String currentOutstanding;
	public Integer tenor;

	public String getValueDate() {
		return valueDate;
	}

	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}

	public Double getIntRate() {
		return intRate;
	}

	public void setIntRate(Double intRate) {
		this.intRate = intRate;
	}

	public String getCurrentOutstanding() {
		return currentOutstanding;
	}

	public void setCurrentOutstanding(String currentOutstanding) {
		this.currentOutstanding = currentOutstanding;
	}

	public Integer getTenor() {
		return tenor;
	}

	public void setTenor(Integer tenor) {
		this.tenor = tenor;
	}

	public GendocRepaymentSchedule() {
		super();
	}
}
