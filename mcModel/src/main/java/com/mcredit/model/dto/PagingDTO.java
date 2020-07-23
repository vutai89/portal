package com.mcredit.model.dto;

import java.io.Serializable;
import java.util.List;

public class PagingDTO implements Serializable {

	private static final long serialVersionUID = 4629075367866218516L;

	public Integer totalCount;

	public Integer pageSize;
	public Integer pageNum;

	public List<?> data;

	public PagingDTO(Integer totalCount, Integer start, Integer end, List<?> data) {
		super();
		this.totalCount = totalCount;
		this.pageSize = start;
		this.pageNum = end;
		this.data = data;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

	

}
