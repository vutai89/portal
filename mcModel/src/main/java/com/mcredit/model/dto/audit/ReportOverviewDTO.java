package com.mcredit.model.dto.audit;

public class ReportOverviewDTO {
	private String day;
	private String result;
	private String type;
	private Long countTp;
	private Long countMc;
	private Long sumTp;
	private Long sumMc;

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getCountTp() {
		return countTp;
	}

	public void setCountTp(Long countTp) {
		this.countTp = countTp;
	}

	public Long getCountMc() {
		return countMc;
	}

	public void setCountMc(Long countMc) {
		this.countMc = countMc;
	}

	public Long getSumTp() {
		return sumTp;
	}

	public void setSumTp(Long sumTp) {
		this.sumTp = sumTp;
	}

	public Long getSumMc() {
		return sumMc;
	}

	public void setSumMc(Long sumMc) {
		this.sumMc = sumMc;
	}

	public ReportOverviewDTO(String day, String result, String type, Long countTp, Long countMc, Long sumTp,
			Long sumMc) {
		super();
		this.day = day;
		this.result = result;
		this.type = type;
		this.countTp = countTp;
		this.countMc = countMc;
		this.sumTp = sumTp;
		this.sumMc = sumMc;
	}

	public ReportOverviewDTO() {
		super();
	}

}
