package com.mcredit.model.object.audit;

import java.util.List;

import com.mcredit.model.dto.audit.AuditDuplicateDTO;
import com.mcredit.model.dto.audit.AuditResultDTO;

public class GeneralReturnResult {
	private long totalRecord;
	private List<AuditResultDTO> generalResult;
	private List<AuditDuplicateDTO> auditDuplicate;

	public long getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(long totalRecord) {
		this.totalRecord = totalRecord;
	}

	public List<AuditResultDTO> getGeneralResult() {
		return generalResult;
	}

	public void setGeneralResult(List<AuditResultDTO> generalResult) {
		this.generalResult = generalResult;
	}

	public List<AuditDuplicateDTO> getAuditDuplicate() {
		return auditDuplicate;
	}

	public void setAuditDuplicate(List<AuditDuplicateDTO> auditDuplicate) {
		this.auditDuplicate = auditDuplicate;
	}

	public GeneralReturnResult(long totalRecord, List<AuditResultDTO> generalResult,
			List<AuditDuplicateDTO> auditDuplicate) {
		super();
		this.totalRecord = totalRecord;
		this.generalResult = generalResult;
		this.auditDuplicate = auditDuplicate;
	}

	public GeneralReturnResult() {
		super();
	}

}
