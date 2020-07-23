package com.mcredit.model.dto.check_cat;

import java.util.List;

public class ResponseSearchCompany {
	private Long totalCount;
	private Integer pageNumber;
	private Integer pageSize;
	private List<CustCompanyInfoFullDTO> data;

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
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

	public List<CustCompanyInfoFullDTO> getData() {
		return data;
	}

	public void setData(List<CustCompanyInfoFullDTO> data) {
		this.data = data;
	}

}
