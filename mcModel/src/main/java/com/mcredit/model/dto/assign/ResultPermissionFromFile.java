package com.mcredit.model.dto.assign;

import java.util.ArrayList;
import java.util.List;

public class ResultPermissionFromFile {
	
	private List<String> errorLst;
	private List<UserRolesDTO> successIds;
	
	public ResultPermissionFromFile() {
		this.errorLst = new ArrayList<String>();
		this.successIds = new ArrayList<UserRolesDTO>();
	}

	public List<UserRolesDTO> getSuccessIds() {
		return successIds;
	}

	public void setSuccessIds(List<UserRolesDTO> successIds) {
		this.successIds = successIds;
	}

	public List<String> getErrorLst() {
		return errorLst;
	}

	public void setErrorLst(List<String> errorLst) {
		this.errorLst = errorLst;
	}


}
