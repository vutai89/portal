package com.mcredit.model.dto.black_list;

public class BlackListResponseDTO {
	private String Name;
	private String NumberID;
	private String TypeOfDoc;
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getNumberID() {
		return NumberID;
	}
	public void setNumberID(String numberID) {
		NumberID = numberID;
	}
	public String getTypeOfDoc() {
		return TypeOfDoc;
	}
	public void setTypeOfDoc(String typeOfDoc) {
		TypeOfDoc = typeOfDoc;
	}
	
	
}
