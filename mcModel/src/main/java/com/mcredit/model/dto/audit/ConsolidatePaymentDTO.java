package com.mcredit.model.dto.audit;

public class ConsolidatePaymentDTO {
	private Long id;

	private String tpPartnerRefId;

	private String mcPartnerRefId;

	private String contractId;

	private String tpContractId;

	private String type;

	private Long tpContractFee;

	private Long mcContractFee;

	private String contractDate;

	private String tpStatus;

	private String mcStatus;

	private String result;

	private String thirdParty;

	private String workFlow;

	private String timeControl;

	public String getTimeControl() {
		return timeControl;
	}

	public void setTimeControl(String timeControl) {
		this.timeControl = timeControl;
	}

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

	public String getMcPartnerRefId() {
		return mcPartnerRefId;
	}

	public void setMcPartnerRefId(String mcPartnerRefId) {
		this.mcPartnerRefId = mcPartnerRefId;
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

	public Long getMcContractFee() {
		return mcContractFee;
	}

	public void setMcContractFee(Long mcContractFee) {
		this.mcContractFee = mcContractFee;
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

	public String getMcStatus() {
		return mcStatus;
	}

	public void setMcStatus(String mcStatus) {
		this.mcStatus = mcStatus;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(String workFlow) {
		this.workFlow = workFlow;
	}

	public String getThirdParty() {
		return thirdParty;
	}

	public void setThirdParty(String thirdParty) {
		this.thirdParty = thirdParty;
	}

	public String getTpContractId() {
		return tpContractId;
	}

	public void setTpContractId(String tpContractId) {
		this.tpContractId = tpContractId;
	}

	public ConsolidatePaymentDTO(Long id, String tpPartnerRefId, String mcPartnerRefId, String contractId, String type,
			Long tpContractFee, Long mcContractFee, String contractDate, String tpStatus, String mcStatus,
			String result, String thirdParty, String workFlow, String timeControl) {
		super();
		this.id = id;
		this.tpPartnerRefId = tpPartnerRefId;
		this.mcPartnerRefId = mcPartnerRefId;
		this.contractId = contractId;
		this.type = type;
		this.tpContractFee = tpContractFee;
		this.mcContractFee = mcContractFee;
		this.contractDate = contractDate;
		this.tpStatus = tpStatus;
		this.mcStatus = mcStatus;
		this.result = result;
		this.thirdParty = thirdParty;
		this.workFlow = workFlow;
		this.timeControl = timeControl;
	}

	public ConsolidatePaymentDTO(Long id, String tpPartnerRefId, String mcPartnerRefId, String contractId,
			String tpContractId, String type, Long tpContractFee, Long mcContractFee, String contractDate,
			String tpStatus, String mcStatus, String result, String thirdParty, String workFlow, String timeControl) {
		super();
		this.id = id;
		this.tpPartnerRefId = tpPartnerRefId;
		this.mcPartnerRefId = mcPartnerRefId;
		this.contractId = contractId;
		this.tpContractId = tpContractId;
		this.type = type;
		this.tpContractFee = tpContractFee;
		this.mcContractFee = mcContractFee;
		this.contractDate = contractDate;
		this.tpStatus = tpStatus;
		this.mcStatus = mcStatus;
		this.result = result;
		this.thirdParty = thirdParty;
		this.workFlow = workFlow;
		this.timeControl = timeControl;
	}

	public ConsolidatePaymentDTO() {
		super();
	}

}
