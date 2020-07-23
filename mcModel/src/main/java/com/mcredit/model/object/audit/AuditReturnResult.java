package com.mcredit.model.object.audit;

import java.util.List;

import com.mcredit.model.dto.audit.AuditResultDTO;
import com.mcredit.model.dto.audit.ConsolidatePaymentDTO;

public class AuditReturnResult {
	private List<AuditResultDTO> auditResult;
	private List<ConsolidatePaymentDTO> auditMatchDetail;
	private List<ConsolidatePaymentDTO> auditUnmatchDetail;
	public List<AuditResultDTO> getAuditResult() {
		return auditResult;
	}
	public void setAuditResult(List<AuditResultDTO> auditResult) {
		this.auditResult = auditResult;
	}
	public List<ConsolidatePaymentDTO> getAuditMatchDetail() {
		return auditMatchDetail;
	}
	public void setAuditMatchDetail(List<ConsolidatePaymentDTO> auditMatchDetail) {
		this.auditMatchDetail = auditMatchDetail;
	}
	public List<ConsolidatePaymentDTO> getAuditUnmatchDetail() {
		return auditUnmatchDetail;
	}
	public void setAuditUnmatchDetail(List<ConsolidatePaymentDTO> auditUnmatchDetail) {
		this.auditUnmatchDetail = auditUnmatchDetail;
	}
	public AuditReturnResult(List<AuditResultDTO> auditResult, List<ConsolidatePaymentDTO> auditMatchDetail,
			List<ConsolidatePaymentDTO> auditUnmatchDetail) {
		super();
		this.auditResult = auditResult;
		this.auditMatchDetail = auditMatchDetail;
		this.auditUnmatchDetail = auditUnmatchDetail;
	}
	public AuditReturnResult() {
		super();
	}
	
	
}
