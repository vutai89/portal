package com.mcredit.model.object.gendoc;

public class GendocDataAppendixView {
	private String dataAppendix;
	private Integer totalTypeOfGood;

	public String getDataAppendix() {
		return dataAppendix;
	}

	public void setDataAppendix(String dataAppendix) {
		this.dataAppendix = dataAppendix;
	}

	public Integer getTotalTypeOfGood() {
		return totalTypeOfGood;
	}

	public void setTotalTypeOfGood(Integer totalTypeOfGood) {
		this.totalTypeOfGood = totalTypeOfGood;
	}

	public GendocDataAppendixView(String dataAppendix, Integer totalTypeOfGood) {
		super();
		this.dataAppendix = dataAppendix;
		this.totalTypeOfGood = totalTypeOfGood;
	}

	public GendocDataAppendixView() {
		super();
		// TODO Auto-generated constructor stub
	}

}
