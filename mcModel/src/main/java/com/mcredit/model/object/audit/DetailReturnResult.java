package com.mcredit.model.object.audit;

import java.util.List;

import com.mcredit.model.dto.audit.ConsolidatePaymentDTO;

public class DetailReturnResult {
	private long totalRecord;
	private List<ConsolidatePaymentDTO> detailResult;

	public long getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(long totalRecord) {
		this.totalRecord = totalRecord;
	}

	public List<ConsolidatePaymentDTO> getDetailResult() {
		return detailResult;
	}

	public void setDetailResult(List<ConsolidatePaymentDTO> detailResult) {
		this.detailResult = detailResult;
	}

	public DetailReturnResult(long totalRecord, List<ConsolidatePaymentDTO> detailResult) {
		super();
		this.totalRecord = totalRecord;
		this.detailResult = detailResult;
	}

	public DetailReturnResult() {
		super();
	}

}
