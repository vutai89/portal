package com.mcredit.data.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity(name="Scheduler")
@Table(name="SCHEDULER", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class Scheduler implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_SCHEDULER_ID_GEN")
	@SequenceGenerator(name="SEQ_SCHEDULER_ID_GEN", sequenceName = "SEQ_SCHEDULER_ID")
	@Column(name="ID", unique=true)
	private Integer id;

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

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="SCHEDULE_GROUP")
	private String scheduleGroup;

	@Column(name="SCH_NAME")
	private String scheduleName;
	
	@Column(name="FREQUENCY")
	private String frequency;

	@Column(name="INTERVAL")
	private Integer interval;

	@Temporal(TemporalType.DATE)
	@Column(name="START_TIME")
	private Date startTime;
	
	@Temporal(TemporalType.DATE)
	@Column(name="NEXT_SCHEDULE_TIME")
	private Date nextScheduleTime;

	@Column(name="STATUS")
	private String status;

	@Column(name="NUM_OF_RUN")
	private Integer numOfRun;

	@Column(name="REQUEST")
	private String request;

	@Column(name="ALLOW_OVERLAP")
	private String allowOverlap;

	@Column(name="EXECUTE_TARGET")
	private String executeTarget;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getScheduleGroup() {
		return scheduleGroup;
	}

	public void setScheduleGroup(String scheduleGroup) {
		this.scheduleGroup = scheduleGroup;
	}

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getNextScheduleTime() {
		return nextScheduleTime;
	}

	public void setNextScheduleTime(Date nextScheduleTime) {
		this.nextScheduleTime = nextScheduleTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getNumOfRun() {
		return numOfRun;
	}

	public void setNumOfRun(Integer numOfRun) {
		this.numOfRun = numOfRun;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getAllowOverlap() {
		return allowOverlap;
	}

	public void setAllowOverlap(String allowOverlap) {
		this.allowOverlap = allowOverlap;
	}

	public String getExecuteTarget() {
		return executeTarget;
	}

	public void setExecuteTarget(String executeTarget) {
		this.executeTarget = executeTarget;
	}

}
