package com.mcredit.model.dto;

import java.io.Serializable;
import java.util.Date;

public class ProdHisUploadDTO implements Serializable {

	private static final long serialVersionUID = 4629075367866218516L;
	public Integer id;
	public Date lastUpdatedDate;
	public String createdBy;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}
