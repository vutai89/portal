package com.mcredit.model.object.mobile.dto;


public class SearchCaseDTO {
	private String keyword;
	private Integer pageNumber;
	private Integer pageSize;
	private String status;
	
	public SearchCaseDTO() {}
	
	public SearchCaseDTO(String keyword, Integer pageNumber, Integer pageSize, String status) {
		this.keyword = keyword;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.status = status;
	}
	
	public String getKeyword() {
		return keyword;
	}
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public Integer getPageNumber() {
		return pageNumber;
	}
	
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	
	public Integer getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
}
