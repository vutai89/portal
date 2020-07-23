package com.mcredit.data.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity(name="SchedulerInstance")
@Table(name="SCHEDULE_INSTANCE", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
@NamedNativeQuery(name="SchedulerID.nextSeq",query="select SEQ_SCHEDULER_ID.nextval from DUAL")
public class SchedulerInstance implements Serializable {

	@Id
	@Column(name="ID", unique=true)
	private Long id;

	@Column(name="SCHEDULE_ID")
	private Integer schedulerId;

	@Column(name="RECORD_STATUS")
	private String recordStatus;

	@CreationTimestamp
	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE", updatable=false)
	private Date createdDate;

	@UpdateTimestamp
	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
	private Date lastUpdatedDate;

	@CreationTimestamp
	@Temporal(TemporalType.DATE)
	@Column(name="START_TIME", updatable=false)
	private Date startTime;

	@Temporal(TemporalType.DATE)
	@Column(name="END_TIME")
	private Date endTime;
	
	@Column(name="STATUS")
	private String status;

	@Column(name="RESPONSE_CODE")
	private String responseCode;

	@Column(name="MESSAGE")
	private String message;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getSchedulerId() {
		return schedulerId;
	}

	public void setSchedulerId(Integer schedulerId) {
		this.schedulerId = schedulerId;
	}

}
