package com.mcredit.model.dto.debt_home;

public class DebtContractPcbDTO {
	private boolean checkPcbResult;
	private Long pcbId;
	private String pcbLink;

	public boolean isCheckPcbResult() {
		return checkPcbResult;
	}

	public void setCheckPcbResult(boolean checkPcbResult) {
		this.checkPcbResult = checkPcbResult;
	}

	public Long getPcbId() {
		return pcbId;
	}

	public void setPcbId(Long pcbId) {
		this.pcbId = pcbId;
	}

	public String getPcbLink() {
		return pcbLink;
	}

	public void setPcbLink(String pcbLink) {
		this.pcbLink = pcbLink;
	}

	public DebtContractPcbDTO(boolean checkPcbResult, Long pcbId, String pcbLink) {
		super();
		this.checkPcbResult = checkPcbResult;
		this.pcbId = pcbId;
		this.pcbLink = pcbLink;
	}

	public DebtContractPcbDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

}
