package com.mcredit.restcore.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Result")
@XmlAccessorType(XmlAccessType.FIELD)
public class Result {
	@XmlElement
	private String returnCode;
	@XmlElement
	private String returnMes;
	
	public Result() { }
	
	public Result(String returnCode, String returnMes) {
		this.returnCode = returnCode;
		this.returnMes = returnMes;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMes() {
		return returnMes;
	}
	public void setReturnMes(String returnMes) {
		this.returnMes = returnMes;
	}
	
}
