package com.mcredit.model.dto.assign;

public class SearchUserTsaDTO {
	private String keyword;
	private Integer pageNumber;
	private Integer pageSize;
	
	public SearchUserTsaDTO(String keyword, Integer pageNumber, Integer pageSize) {
		super();
		this.keyword = keyword;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
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
