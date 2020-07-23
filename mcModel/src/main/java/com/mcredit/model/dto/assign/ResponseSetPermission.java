package com.mcredit.model.dto.assign;

import java.util.ArrayList;
import java.util.List;

public class ResponseSetPermission {
	private List<Long> successIds;
	
	private List<Long> errorIds;

	public List<Long> getSuccessIds() {
		return successIds;
	}

	public void setSuccessIds(List<Long> successIds) {
		this.successIds = successIds;
	}

	public List<Long> getErrorIds() {
		return errorIds;
	}

	public void setErrorIds(List<Long> errorIds) {
		this.errorIds = errorIds;
	}
}
