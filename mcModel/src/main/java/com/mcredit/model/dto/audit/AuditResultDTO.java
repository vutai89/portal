package com.mcredit.model.dto.audit;

public class AuditResultDTO {
	private String date;
	private Long thirdPartyTotalDeal;
	private Long thirdPartyTotalMoney;
	private Long McreditTotalDeal;
	private Long McreditTotalMoney;
	private Long MatchTotalDeal;
	private Long MatchTotalMoney;
	private Long UnMatchTotalDeal;
	private Long UnMatchTotalMoney;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Long getThirdPartyTotalDeal() {
		return thirdPartyTotalDeal;
	}

	public void setThirdPartyTotalDeal(Long thirdPartyTotalDeal) {
		this.thirdPartyTotalDeal = thirdPartyTotalDeal;
	}

	public Long getThirdPartyTotalMoney() {
		return thirdPartyTotalMoney;
	}

	public void setThirdPartyTotalMoney(Long thirdPartyTotalMoney) {
		this.thirdPartyTotalMoney = thirdPartyTotalMoney;
	}

	public Long getMcreditTotalDeal() {
		return McreditTotalDeal;
	}

	public void setMcreditTotalDeal(Long mcreditTotalDeal) {
		McreditTotalDeal = mcreditTotalDeal;
	}

	public Long getMcreditTotalMoney() {
		return McreditTotalMoney;
	}

	public void setMcreditTotalMoney(Long mcreditTotalMoney) {
		McreditTotalMoney = mcreditTotalMoney;
	}

	public Long getMatchTotalDeal() {
		return MatchTotalDeal;
	}

	public void setMatchTotalDeal(Long matchTotalDeal) {
		MatchTotalDeal = matchTotalDeal;
	}

	public Long getMatchTotalMoney() {
		return MatchTotalMoney;
	}

	public void setMatchTotalMoney(Long matchTotalMoney) {
		MatchTotalMoney = matchTotalMoney;
	}

	public Long getUnMatchTotalDeal() {
		return UnMatchTotalDeal;
	}

	public void setUnMatchTotalDeal(Long unMatchTotalDeal) {
		UnMatchTotalDeal = unMatchTotalDeal;
	}

	public Long getUnMatchTotalMoney() {
		return UnMatchTotalMoney;
	}

	public void setUnMatchTotalMoney(Long unMatchTotalMoney) {
		UnMatchTotalMoney = unMatchTotalMoney;
	}

	public AuditResultDTO(String date, Long thirdPartyTotalDeal, Long thirdPartyTotalMoney, Long mcreditTotalDeal,
			Long mcreditTotalMoney, Long matchTotalDeal, Long matchTotalMoney, Long unMatchTotalDeal,
			Long unMatchTotalMoney) {
		super();
		this.date = date;
		this.thirdPartyTotalDeal = thirdPartyTotalDeal;
		this.thirdPartyTotalMoney = thirdPartyTotalMoney;
		McreditTotalDeal = mcreditTotalDeal;
		McreditTotalMoney = mcreditTotalMoney;
		MatchTotalDeal = matchTotalDeal;
		MatchTotalMoney = matchTotalMoney;
		UnMatchTotalDeal = unMatchTotalDeal;
		UnMatchTotalMoney = unMatchTotalMoney;
	}

	public AuditResultDTO() {
		this.thirdPartyTotalDeal = 0L;
		this.thirdPartyTotalMoney = 0L;
		McreditTotalDeal = 0L;
		McreditTotalMoney = 0L;
		MatchTotalDeal = 0L;
		MatchTotalMoney = 0L;
		UnMatchTotalDeal = 0L;
		UnMatchTotalMoney = 0L;
	}

}
