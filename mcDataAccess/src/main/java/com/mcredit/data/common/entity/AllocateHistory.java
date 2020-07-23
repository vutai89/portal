package com.mcredit.data.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "ALLOCATION_HISTORY")
public class AllocateHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5996910149520967127L;

	@Id
	@GenericGenerator(name = "seq_AllocateHistory", strategy = "com.mcredit.data.common.entity.SequenceGenerate", parameters = @Parameter(name = "seqName", value = "SEQ_ALLOCATION_HISTORY_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_AllocateHistory")
	private Long id;

	@Column(name = "ALLOCATION_DETAIL_ID")
	private Long allocationDetailId;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "ASSIGNEE_ID")
	private Long objectId;

	@Column(name = "STATUS")
	private Long status;

	@Column(name = "NOTE")
	private String note;

	public AllocateHistory() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAllocationDetailId() {
		return allocationDetailId;
	}

	public void setAllocationDetailId(Long allocationDetailId) {
		this.allocationDetailId = allocationDetailId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
