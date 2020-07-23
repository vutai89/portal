package com.mcredit.model.dto.telesales;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErrorAllocateDTO {

	@SerializedName("errorIds")
	@Expose
	public List<Long> errorIds = null;

	public List<Long> getErrorIds() {
		return errorIds;
	}

	public void setErrorIds(List<Long> errorIds) {
		this.errorIds = errorIds;
	}
	
}
