package com.mcredit.data.common.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * The persistent class for the MESSAGE_TASK database table.
 * 
 */
@Entity
@Table(name = "MESSAGE_TASK")
@NamedQuery(name = "MessageTask.findAll", query = "SELECT m FROM MessageTask m")
public class MessageTask implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GenericGenerator(name = "seq_msgTask", strategy = "com.mcredit.data.common.entity.SequenceGenerate", parameters = @Parameter(name = "seqName", value = "SEQ_MESSAGE_TAG_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_msgTask")
	@Column(name = "ID", unique = true)
	private Long id;

	@CreationTimestamp
	@Column(name = "CREATED_DATE", updatable = false)
	private Timestamp createdDate;

	@Lob
	@Column(name = "DTO_OBJECT")
	private String dtoObject;

	@Column(name = "LAST_RETRY_DATE")
	private Timestamp lastRetryDate;

	@Column(name = "LAST_UPDATED_DATE")
	private Timestamp lastUpdatedDate;

	@Column(name = "NUM_RETRY")
	private Integer numRetry;

	@Column(name = "RELATION_ID")
	private String relationId;

	private String status;

	@Column(name = "TASK_TYPE")
	private String taskType;

	@Column(name = "TRANS_TYPE")
	private String transType;

	public MessageTask() {
	}

	public MessageTask(Timestamp createdDate, Timestamp lastUpdatedDate, String relationId, String status,
			String taskType, String transType ,String dtoObject) {
		super();
		this.createdDate = createdDate;
		this.lastUpdatedDate = lastUpdatedDate;
		this.relationId = relationId;
		this.status = status;
		this.taskType = taskType;
		this.transType = transType;
		this.dtoObject = dtoObject;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getDtoObject() {
		return this.dtoObject;
	}

	public void setDtoObject(String dtoObject) {
		this.dtoObject = dtoObject;
	}

	public Timestamp getLastRetryDate() {
		return this.lastRetryDate;
	}

	public void setLastRetryDate(Timestamp lastRetryDate) {
		this.lastRetryDate = lastRetryDate;
	}

	public Timestamp getLastUpdatedDate() {
		return this.lastUpdatedDate;
	}

	public void setLastUpdatedDate(Timestamp lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Integer getNumRetry() {
		return this.numRetry;
	}

	public void setNumRetry(Integer numRetry) {
		this.numRetry = numRetry;
	}

	public String getRelationId() {
		return this.relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTaskType() {
		return this.taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTransType() {
		return this.transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

}