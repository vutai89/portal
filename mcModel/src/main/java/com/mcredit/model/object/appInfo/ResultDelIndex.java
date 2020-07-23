
package com.mcredit.model.object.appInfo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "APP_INIT_USERNAME", "APP_CUR_USER_UID", "APP_INIT_USER_UID", "APP_CUR_USERNAME",
		"APP_UPDATE_DATE", "DEL_INDEX", "APP_CREATE_DATE", "APP_NUMBER", "APP_STATUS", "APP_UID" })
public class ResultDelIndex implements Serializable {
	private static final long serialVersionUID = -7479562659428695099L;

	@JsonProperty("APP_INIT_USERNAME")
	private String aPPINITUSERNAME;
	@JsonProperty("APP_CUR_USER_UID")
	private String aPPCURUSERUID;
	@JsonProperty("APP_INIT_USER_UID")
	private String aPPINITUSERUID;
	@JsonProperty("APP_CUR_USERNAME")
	private String aPPCURUSERNAME;
	@JsonProperty("APP_UPDATE_DATE")
	private String aPPUPDATEDATE;
	@JsonProperty("DEL_INDEX")
	private int dELINDEX;
	@JsonProperty("APP_CREATE_DATE")
	private String aPPCREATEDATE;
	@JsonProperty("APP_NUMBER")
	private String aPPNUMBER;
	@JsonProperty("APP_STATUS")
	private String aPPSTATUS;
	@JsonProperty("APP_UID")
	private String aPPUID;

	@JsonProperty("APP_INIT_USERNAME")
	public String getAPPINITUSERNAME() {
		return aPPINITUSERNAME;
	}

	@JsonProperty("APP_INIT_USERNAME")
	public void setAPPINITUSERNAME(String aPPINITUSERNAME) {
		this.aPPINITUSERNAME = aPPINITUSERNAME;
	}

	@JsonProperty("APP_CUR_USER_UID")
	public String getAPPCURUSERUID() {
		return aPPCURUSERUID;
	}

	@JsonProperty("APP_CUR_USER_UID")
	public void setAPPCURUSERUID(String aPPCURUSERUID) {
		this.aPPCURUSERUID = aPPCURUSERUID;
	}

	@JsonProperty("APP_INIT_USER_UID")
	public String getAPPINITUSERUID() {
		return aPPINITUSERUID;
	}

	@JsonProperty("APP_INIT_USER_UID")
	public void setAPPINITUSERUID(String aPPINITUSERUID) {
		this.aPPINITUSERUID = aPPINITUSERUID;
	}

	@JsonProperty("APP_CUR_USERNAME")
	public String getAPPCURUSERNAME() {
		return aPPCURUSERNAME;
	}

	@JsonProperty("APP_CUR_USERNAME")
	public void setAPPCURUSERNAME(String aPPCURUSERNAME) {
		this.aPPCURUSERNAME = aPPCURUSERNAME;
	}

	@JsonProperty("APP_UPDATE_DATE")
	public String getAPPUPDATEDATE() {
		return aPPUPDATEDATE;
	}

	@JsonProperty("APP_UPDATE_DATE")
	public void setAPPUPDATEDATE(String aPPUPDATEDATE) {
		this.aPPUPDATEDATE = aPPUPDATEDATE;
	}

	@JsonProperty("DEL_INDEX")
	public int getDELINDEX() {
		return dELINDEX;
	}

	@JsonProperty("DEL_INDEX")
	public void setDELINDEX(int dELINDEX) {
		this.dELINDEX = dELINDEX;
	}

	@JsonProperty("APP_CREATE_DATE")
	public String getAPPCREATEDATE() {
		return aPPCREATEDATE;
	}

	@JsonProperty("APP_CREATE_DATE")
	public void setAPPCREATEDATE(String aPPCREATEDATE) {
		this.aPPCREATEDATE = aPPCREATEDATE;
	}

	@JsonProperty("APP_NUMBER")
	public String getAPPNUMBER() {
		return aPPNUMBER;
	}

	@JsonProperty("APP_NUMBER")
	public void setAPPNUMBER(String aPPNUMBER) {
		this.aPPNUMBER = aPPNUMBER;
	}

	@JsonProperty("APP_STATUS")
	public String getAPPSTATUS() {
		return aPPSTATUS;
	}

	@JsonProperty("APP_STATUS")
	public void setAPPSTATUS(String aPPSTATUS) {
		this.aPPSTATUS = aPPSTATUS;
	}

	@JsonProperty("APP_UID")
	public String getAPPUID() {
		return aPPUID;
	}

	@JsonProperty("APP_UID")
	public void setAPPUID(String aPPUID) {
		this.aPPUID = aPPUID;
	}

}
