package com.mcredit.data.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * The persistent class for the ALLOCATION_MASTER database table.
 * 
 */
@Entity
@Table(name = "ALLOCATION_MASTER")
@NamedQuery(name = "AllocationMaster.findAll", query = "SELECT a FROM AllocationMaster a")
public class AllocationMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "seq_AllocationMaster", strategy = "com.mcredit.data.common.entity.SequenceGenerate", parameters = @Parameter(name = "seqName", value = "SEQ_ALLOCATION_MASTER_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_AllocationMaster")
	private Long id;

	@Column(name = "ALLOCATED_NUMBER")
	private Integer allocatedNumber;

	@Column(name = "ALLOCATED_SEQ")
	private Integer allocatedSeq;

	@Column(name = "ALLOCATED_TO")
	private Long allocatedTo;

	@Column(name = "ALLOCATED_TYPE")
	private String allocatedType;

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

	@Column(name = "RECORD_STATUS")
	private String recordStatus;

	@Column(name = "RELATED_ALLOCATION")
	private Integer relatedAllocation;

	@Column(name = "UPL_MASTER_ID")
	private Long uplMasterId;

	@Column(name = "ASSIGNER_ID")
	private Long assigerId;

	public AllocationMaster() {
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

	public void setAllocatedNumber(Integer allocatedNumber) {
		this.allocatedNumber = allocatedNumber;
	}

	public Integer getAllocatedSeq() {
		return this.allocatedSeq;
	}

	public void setAllocatedSeq(Integer allocatedSeq) {
		this.allocatedSeq = allocatedSeq;
	}

	public Long getAllocatedTo() {
		return this.allocatedTo;
	}

	public void setAllocatedTo(Long allocatedTo) {
		this.allocatedTo = allocatedTo;
	}

	public String getAllocatedType() {
		return this.allocatedType;
	}

	public void setAllocatedType(String allocatedType) {
		this.allocatedType = allocatedType;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdatedDate() {
		return this.lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getRecordStatus() {
		return this.recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Integer getRelatedAllocation() {
		return this.relatedAllocation;
	}

	public void setRelatedAllocation(Integer relatedAllocation) {
		this.relatedAllocation = relatedAllocation;
	}

	public Long getUplMasterId() {
		return this.uplMasterId;
	}

	public void setUplMasterId(Long uplMasterId) {
		this.uplMasterId = uplMasterId;
	}

	public Long getAssigerId() {
		return assigerId;
	}

	public void setAssigerId(Long assigerId) {
		this.assigerId = assigerId;
	}

}