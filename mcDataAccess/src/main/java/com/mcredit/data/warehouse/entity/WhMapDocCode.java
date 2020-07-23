package com.mcredit.data.warehouse.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="WH_MAP_DOC_CODE")
public class WhMapDocCode implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6621985890166777721L;

	@Id
	@Column(name="WH_DOC_ID")
	private Long whDocId;

	@Id
	@Column(name="WH_CODE_ID")
	private Long whCodeId;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE")
	private Date createdDate;

	public WhMapDocCode() {
	}
	
	public WhMapDocCode(Long whDocId, Long whCodeId, String createdBy, Date createdDate) {
		this.whDocId = whDocId;
		this.whCodeId = whCodeId;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
	}
	
	public Long getWhDocId() {
		return whDocId;
	}

	public void setWhDocId(Long whDocId) {
		this.whDocId = whDocId;
	}

	public Long getWhCodeId() {
		return whCodeId;
	}

	public void setWhCodeId(Long whCodeId) {
		this.whCodeId = whCodeId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}