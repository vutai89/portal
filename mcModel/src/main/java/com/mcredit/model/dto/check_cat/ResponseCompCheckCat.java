package com.mcredit.model.dto.check_cat;

import java.util.List;

public class ResponseCompCheckCat {
	private Long totalCount;
	private Integer pageNumber;
	private Integer pageSize;
	private List<ApproveCatDTO> data;
	public Long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
	public List<ApproveCatDTO> getData() {
		return data;
	}
	public void setData(List<ApproveCatDTO> data) {
		this.data = data;
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
