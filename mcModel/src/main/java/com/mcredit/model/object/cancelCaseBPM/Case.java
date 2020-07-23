
package com.mcredit.model.object.cancelCaseBPM;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Case implements Serializable{
	private static final long serialVersionUID = -4074704548926177234L;
	
	@SerializedName("APP_UID")
	@Expose
	private String aPPUID;
	@SerializedName("DEL_INDEX")
	@Expose
	private Integer dELINDEX;

	public Case(String aPPUID, Integer dELINDEX) {
		this.aPPUID = aPPUID;
		this.dELINDEX = dELINDEX;
	}

	public String getAPPUID() {
		return aPPUID;
	}

	public void setAPPUID(String aPPUID) {
		this.aPPUID = aPPUID;
	}

	public Integer getDELINDEX() {
		return dELINDEX;
	}

	public void setDELINDEX(Integer dELINDEX) {
		this.dELINDEX = dELINDEX;
	}

}
