package com.mcredit.model.dto.audit;

public class AuditCommandDTO {
	private String thirdParty;
	private String fromDate;
	private String toDate;
	private String type;
	private String time;
	private String workflow;
	private int pageNum;
	private int pageSize;

	public String getThirdParty() {
		return thirdParty;
	}

	public void setThirdParty(String thirdParty) {
		this.thirdParty = thirdParty;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getWorkflow() {
		return workflow;
	}

	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public AuditCommandDTO(String thirdParty, String fromDate, String toDate, String type, String time, String workflow,
			int pageNum, int pageSize) {
		super();
		this.thirdParty = thirdParty;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.type = type;
		this.time = time;
		this.workflow = workflow;
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	public AuditCommandDTO() {
		super();
	}

}
