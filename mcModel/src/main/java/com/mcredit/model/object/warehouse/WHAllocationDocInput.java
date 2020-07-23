package com.mcredit.model.object.warehouse;

import java.io.Serializable;
import java.util.List;

public class WHAllocationDocInput implements Serializable {

	private static final long serialVersionUID = 3597507299124578924L;

	public Long userId;

	public List<Long> lstwhDocId;

	public String reason;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<Long> getLstwhDocId() {
		return lstwhDocId;
	}

	public void setLstwhDocId(List<Long> lstwhDocId) {
		this.lstwhDocId = lstwhDocId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
