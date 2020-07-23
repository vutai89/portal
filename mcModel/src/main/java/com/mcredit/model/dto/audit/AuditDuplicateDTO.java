package com.mcredit.model.dto.audit;

public class AuditDuplicateDTO {
	private Long id;

	private String tpPartnerRefId;
	
	private String contractId;
	
	private String type;
	
	private Long tpContractFee;
	
	private String contractDate;
	
	private String tpStatus;
	
	private String result;
	
	private String thirdParty;

	private String workFlow;

	private String timeControl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTpPartnerRefId() {
		return tpPartnerRefId;
	}

	public void setTpPartnerRefId(String tpPartnerRefId) {
		this.tpPartnerRefId = tpPartnerRefId;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getTpContractFee() {
		return tpContractFee;
	}

	public void setTpContractFee(Long tpContractFee) {
		this.tpContractFee = tpContractFee;
	}

	public String getContractDate() {
		return contractDate;
	}

	public void setContractDate(String contractDate) {
		this.contractDate = contractDate;
	}

	public String getTpStatus() {
		return tpStatus;
	}

	public void setTpStatus(String tpStatus) {
		this.tpStatus = tpStatus;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getThirdParty() {
		return thirdParty;
	}

	public void setThirdParty(String thirdParty) {
		this.thirdParty = thirdParty;
	}

	public String getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(String workFlow) {
		this.workFlow = workFlow;
	}

	public String getTimeControl() {
		return timeControl;
	}

	public void setTimeControl(String timeControl) {
		this.timeControl = timeControl;
	}

	public AuditDuplicateDTO(Long id, String tpPartnerRefId, String contractId, String type, Long tpContractFee,
			String contractDate, String tpStatus, String result, String thirdParty, String workFlow,
			String timeControl) {
		super();
		this.id = id;
		this.tpPartnerRefId = tpPartnerRefId;
		this.contractId = contractId;
		this.type = type;
		this.tpContractFee = tpContractFee;
		this.contractDate = contractDate;
		this.tpStatus = tpStatus;
		this.result = result;
		this.thirdParty = thirdParty;
		this.workFlow = workFlow;
		this.timeControl = timeControl;
	}

	public AuditDuplicateDTO() {
		super();
	}
	
	
}
