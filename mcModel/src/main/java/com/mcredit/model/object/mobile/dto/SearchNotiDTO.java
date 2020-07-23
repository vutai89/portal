package com.mcredit.model.object.mobile.dto;

public class SearchNotiDTO {
	private String keyword;
	private Integer pageNumber;
	private Integer pageSize;

	public SearchNotiDTO() {}
	
	public SearchNotiDTO(Integer pageNumber, Integer pageSize, String keword) {
		this.pageSize = pageSize;
		this.pageNumber = pageNumber;
		this.keyword = keword;
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
}
