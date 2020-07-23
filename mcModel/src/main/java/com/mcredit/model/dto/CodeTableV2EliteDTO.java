package com.mcredit.model.dto;

import java.io.Serializable;

public class CodeTableV2EliteDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6912260359874558968L;
	
	private Integer i; //id
	private String c; //codeValue1
	private String d; //description1
	private Integer p; //parentId

	public CodeTableV2EliteDTO(){}
	
	public CodeTableV2EliteDTO(Integer id, String codeValue1, String description1, Integer parentId){
		this.i = id; //id
		this.c = codeValue1; //codeValue1
		this.d = description1; //description1
		this.p = parentId; //parentId
	}

	public Integer getI() {
		return i;
	}

	public void setI(Integer i) {
		this.i = i;
	}

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public Integer getP() {
		return p;
	}

	public void setP(Integer p) {
		this.p = p;
	}
}