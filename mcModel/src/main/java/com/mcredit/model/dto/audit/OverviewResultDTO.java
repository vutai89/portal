package com.mcredit.model.dto.audit;

import java.util.Date;

public class OverviewResultDTO {
	private Date date;
	private String result;
	private Long countTp;
	private Long countMc;
	private Long sumTp;
	private Long sumMc;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
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
	public OverviewResultDTO(Date date, String result, Long countTp, Long countMc, Long sumTp, Long sumMc) {
		super();
		this.date = date;
		this.result = result;
		this.countTp = countTp;
		this.countMc = countMc;
		this.sumTp = sumTp;
		this.sumMc = sumMc;
	}
	public OverviewResultDTO() {
		super();
	}
}
