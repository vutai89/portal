package com.mcredit.model.dto.assign;

import java.util.ArrayList;
import java.util.List;

public class ResultInsertUserList {
	
	private int numbInsertedUser;
	private List<String> lstError;
	
	public ResultInsertUserList() {
		this.numbInsertedUser = 0;
		this.lstError = new ArrayList<String>();
	} 
	

	public ResultInsertUserList(int numbInsertedUser, List<String> lstError) {
		this.numbInsertedUser = numbInsertedUser;
		this.lstError = lstError;
	}

	public int getNumbInsertedUser() {
		return numbInsertedUser;
	}

	public void setNumbInsertedUser(int numbInsertedUser) {
		this.numbInsertedUser = numbInsertedUser;
	}

	public List<String> getLstError() {
		return lstError;
	}

	public void setLstError(List<String> lstError) {
		this.lstError = lstError;
	}

}
