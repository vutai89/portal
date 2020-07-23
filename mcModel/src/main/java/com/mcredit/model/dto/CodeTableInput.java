package com.mcredit.model.dto;

import java.io.Serializable;
import java.util.List;

public class CodeTableInput implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4671436204706320483L;
	
	private String date; // Effect date,  dd/MM/yyyy hh:mi:ss
	
	private List<String> category; // ABC,DEF,GHI 
	
	private List<String> swapDescription;	// list category swap description1 and description2

	public CodeTableInput(){}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<String> getCategory() {
		return category;
	}

	public void setCategory(List<String> category) {
		this.category = category;
	}

	public List<String> getSwapDescription() {
		return swapDescription;
	}

	public void setSwapDescription(List<String> swapDescription) {
		this.swapDescription = swapDescription;
	}
	
}