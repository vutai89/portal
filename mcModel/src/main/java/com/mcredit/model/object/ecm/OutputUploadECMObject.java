package com.mcredit.model.object.ecm;

import java.io.Serializable;
import java.util.ArrayList;

public class OutputUploadECMObject implements Serializable {

	private static final long serialVersionUID = 1450404425261204667L;

	public ArrayList<OutputUploadECM> success;
	public ArrayList<OutputUploadECM> error;
	
	public OutputUploadECMObject(){
	}

	public OutputUploadECMObject(ArrayList<OutputUploadECM> success, ArrayList<OutputUploadECM> error) {
		super();
		this.success = success;
		this.error = error;
	}

	public ArrayList<OutputUploadECM> getSuccess() {
		return success;
	}

	public void setSuccess(ArrayList<OutputUploadECM> success) {
		this.success = success;
	}

	public ArrayList<OutputUploadECM> getError() {
		return error;
	}

	public void setError(ArrayList<OutputUploadECM> error) {
		this.error = error;
	}

}
