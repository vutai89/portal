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
@Table(name = "ALLOCATION_DETAIL")
public class AllocationDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6621985890166777721L;

	@Id
	@GenericGenerator(name = "seq_AllocationDetail", strategy = "com.mcredit.data.common.entity.SequenceGenerate", parameters = @Parameter(name = "seqName", value = "SEQ_ALLOCATION_DETAIL_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_AllocationDetail")
	private Long id;

	@Column(name = "ALLOCATED_NUMBER")
	private Integer allocatedNumber;

	@Column(name = "ALLOCATION_MASTER_ID")
	private Long allocationMasterId;

	// @Column(name="OBJECT_ID")
	@Column(name = "ASSIGNEE_ID")
	private Long objectId;

	@Column(name = "OBJECT_TYPE")
	private String objectType;

	@Column(name = "STATUS")
	private Long status;

	// @Column(name="UPL_CUSTOMER_ID")
	@Column(name = "UPL_OBJECT_ID")
	private Long uplCustomerId;

	@Column(name = "UPL_DETAIL_ID")
	private Long uplDetailId;

	@Column(name = "NOTE")
	private String note;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_UPDATED_DATE")
	private Date lastUpdatedDate;

	public AllocationDetail() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAllocatedNumber() {
		return this.allocatedNumber;
	}

	public void setAllocatedNumber(Integer integer) {
		this.allocatedNumber = integer;
	}

	public Long getAllocationMasterId() {
		return this.allocationMasterId;
	}

	public void setAllocationMasterId(Long allocationMasterId) {
		this.allocationMasterId = allocationMasterId;
	}

	public Long getObjectId() {
		return this.objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public String getObjectType() {
		return this.objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Long getUplCustomerId() {
		return this.uplCustomerId;
	}

	public void setUplCustomerId(Long uplCustomerId) {
		this.uplCustomerId = uplCustomerId;
	}

	public Long getUplDetailId() {
		return this.uplDetailId;
	}

	public void setUplDetailId(Long uplDetailId) {
		this.uplDetailId = uplDetailId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

}
