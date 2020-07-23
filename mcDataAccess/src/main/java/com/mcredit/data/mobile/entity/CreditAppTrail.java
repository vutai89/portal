package com.mcredit.data.mobile.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="UPL_CREDIT_APP_REQUEST", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
@NamedQuery(name="CreditAppTrail.findAll", query="SELECT o FROM CreditAppTrail o")
@NamedNativeQuery(name="CreditAppTrail.nextSeq",query="select SEQ_CREDIT_APP_TRAIL_ID.NEXTVAL from DUAL")
public class CreditAppTrail implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private Long id;

	@Column(name = "CREDIT_APP_ID")
	private Long creditAppId;
	
	@Column(name = "TRAIL_SEQ")
	private Long trailSeq;
	
	@Column(name="RECORD_STATUS")
	private String recordStatus;

	@Column(name="CREATED_BY")
	private String createdBy;

	@CreationTimestamp
	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE", updatable=false)
	private Date createdDate;
	
	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@UpdateTimestamp
	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
	private Date lastUpdatedDate;
	
	@Column(name = "STEP")
	private Long step;
	
	@Column(name = "TASK_ID")
	private Long taskId;
	
	@Column(name = "FROM_USER")
	private Long fromUser;
	
	@Column(name = "TO_USER")
	private Long toUser;
	
	@Column(name = "TO_TEAM")
	private Long toTeam;
	
	@Column(name = "ACTION")
	private Long action;
	
	@Column(name = "REASON_ID")
	private Long reasonId;
	
	@Column(name = "USER_COMMENT")
	private String userComment;
	
	@Column(name = "STEP_CODE")
	private String stepCode;
	
	@Column(name = "FROM_USER_CODE")
	private String fromUserCode;
	
	@Column(name = "TO_USER_CODE")
	private String toUserCode;
	
	@Column(name = "TRAIL_ORDER")
	private Long trailOrder;
	
	@Column(name = "CURRENT_TASK")
	private Long currentTask;
	
	public Long getId() {
		return id;
	}

	public Long getCreditAppId() {
		return creditAppId;
	}

	public Long getTrailSeq() {
		return trailSeq;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public Long getStep() {
		return step;
	}

	public Long getTaskId() {
		return taskId;
	}

	public Long getFromUser() {
		return fromUser;
	}

	public Long getToUser() {
		return toUser;
	}

	public Long getToTeam() {
		return toTeam;
	}

	public Long getAction() {
		return action;
	}

	public Long getReasonId() {
		return reasonId;
	}

	public String getUserComment() {
		return userComment;
	}

	public String getStepCode() {
		return stepCode;
	}

	public String getFromUserCode() {
		return fromUserCode;
	}

	public String getToUserCode() {
		return toUserCode;
	}

	public Long getTrailOrder() {
		return trailOrder;
	}

	public Long getCurrentTask() {
		return currentTask;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCreditAppId(Long creditAppId) {
		this.creditAppId = creditAppId;
	}

	public void setTrailSeq(Long trailSeq) {
		this.trailSeq = trailSeq;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public void setStep(Long step) {
		this.step = step;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public void setFromUser(Long fromUser) {
		this.fromUser = fromUser;
	}

	public void setToUser(Long toUser) {
		this.toUser = toUser;
	}

	public void setToTeam(Long toTeam) {
		this.toTeam = toTeam;
	}

	public void setAction(Long action) {
		this.action = action;
	}

	public void setReasonId(Long reasonId) {
		this.reasonId = reasonId;
	}

	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}

	public void setStepCode(String stepCode) {
		this.stepCode = stepCode;
	}

	public void setFromUserCode(String fromUserCode) {
		this.fromUserCode = fromUserCode;
	}

	public void setToUserCode(String toUserCode) {
		this.toUserCode = toUserCode;
	}

	public void setTrailOrder(Long trailOrder) {
		this.trailOrder = trailOrder;
	}

	public void setCurrentTask(Long currentTask) {
		this.currentTask = currentTask;
	}
}
