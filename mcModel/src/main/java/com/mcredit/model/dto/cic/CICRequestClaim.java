package com.mcredit.model.dto.cic;

import java.io.Serializable;

public class CICRequestClaim implements Serializable{

	private static final long serialVersionUID = -3732789320434044473L;
	
	private Long requestId;
	private Long transId;

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public Long getTransId() {
		return transId;
	}

	public void setTransId(Long transId) {
		this.transId = transId;
	}
	
}
