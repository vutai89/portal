package com.mcredit.scheduler;

import java.util.Date;

import com.mcredit.data.entity.ColumnIndex;

public class SchedulerDTO {

	@ColumnIndex(index=0)
	private Integer id;

	@ColumnIndex(index=1)
	private String recordStatus;

	@ColumnIndex(index=2)
	private String createdDate;

	@ColumnIndex(index=3)
	private String lastUpdatedDate;

	@ColumnIndex(index=4)
	private String createdBy;

	@ColumnIndex(index=5)
	private String lastUpdatedBy;

	@ColumnIndex(index=6)
	private String scheduleGroup;

	@ColumnIndex(index=7)
	private String scheduleName;
	
	@ColumnIndex(index=8)
	private String frequency;

	@ColumnIndex(index=9)
	private Integer interval;

	@ColumnIndex(index=10)
	private String startTime;
	
	@ColumnIndex(index=11)
	private String nextScheduleTime;

	@ColumnIndex(index=12)
	private String status;

	@ColumnIndex(index=13)
	private Integer numOfRun;

	@ColumnIndex(index=14)
	private String request;

	@ColumnIndex(index=15)
	private String allowOverlap;

	@ColumnIndex(index=16)
	private String executeTarget;

	@ColumnIndex(index=17)
	private String timeRange;

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

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(String lastUpdatedDate) {
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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getNextScheduleTime() {
		return nextScheduleTime;
	}

	public void setNextScheduleTime(String nextScheduleTime) {
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

	public String getTimeRange() {
		return timeRange;
	}

	public void setTimeRange(String timeRange) {
		this.timeRange = timeRange;
	}
	
}
