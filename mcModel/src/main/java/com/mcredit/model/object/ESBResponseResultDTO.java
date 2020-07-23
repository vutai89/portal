package com.mcredit.model.object;

import java.io.Serializable;

public class ESBResponseResultDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ESBResponseDTO Result;

	public ESBResponseDTO getResult() {
		return Result;
	}

	public void setResult(ESBResponseDTO result) {
		Result = result;
	}
}