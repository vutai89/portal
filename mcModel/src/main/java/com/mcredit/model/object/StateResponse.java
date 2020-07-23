package com.mcredit.model.object;

import java.io.Serializable;

public class StateResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3708406064896404584L;
	
	private boolean status;

	public StateResponse(){}
	
	public StateResponse(boolean state){
		this.status = state;
	}
	
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
