package com.mcredit.model.alfresco.object;

import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ECMShareDTO extends ACMShareObject {
	@SerializedName("modifiedAt")
	@Expose
	private Date modifiedAt;

	@SerializedName("expiresAt")
	@Expose
	private Date expiresAt;

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public Date getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Date expiresAt) {
		this.expiresAt = expiresAt;
	}

}
