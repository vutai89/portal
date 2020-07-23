package com.mcredit.model.object.mobile.dto;

public class CategoryResult {
	private String message;
	private CategoryDTO data;
	
	public CategoryResult(){
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public CategoryDTO getCategory() {
		return data;
	}
	public void setCategory(CategoryDTO category) {
		this.data = category;
	}

}
