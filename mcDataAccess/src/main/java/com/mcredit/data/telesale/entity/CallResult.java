package com.mcredit.data.telesale.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * The persistent class for the UPL_MASTER database table.
 * 
 */
@Entity
@Table(name = "CALL_RESULT")
public class CallResult implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "seqCallResult", strategy = "com.mcredit.data.common.entity.SequenceGenerate", parameters = @Parameter(name = "seqName", value = "SEQ_CALL_RESULT_ID.NEXTVAL"))
	@GeneratedValue(generator = "seqCallResult")
	@Column(name = "ID")
	private Long id;

	@Column(name = "ALLOCATION_DETAIL_ID")
	private Long allocationDetailId;

	@Column(name = "CUST_PROSPECT_ID")
	private Long custProspectId;

	@Column(name = "CALL_TIMES")
	private Integer callTimes;

	@Column(name = "CALL_STATUS")
	private Integer callStatus;

	@Column(name = "CALL_RESULT")
	private Integer callResult;

	@Column(name = "NEXT_ACTION")
	private Integer nextAction;

	@Temporal(TemporalType.DATE)
	@Column(name = "NEXT_ACTION_DATE")
	private Date nextActionDate;

	@Temporal(TemporalType.DATE)
	@CreationTimestamp
	@Column(name = "CALL_DATE")
	private Date callDate;

	@Column(name = "NOTE")
	private String note;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public CallResult() {
	}

	public Date getCallDate() {
		return callDate;
	}

	public void setCallDate(Date callDate) {
		this.callDate = callDate;
	}

	public Long getAllocationDetailId() {
		return allocationDetailId;
	}

	public void setAllocationDetailId(Long allocationDetailId) {
		this.allocationDetailId = allocationDetailId;
	}

	public Long getCustProspectId() {
		return custProspectId;
	}

	public void setCustProspectId(Long custProspectId) {
		this.custProspectId = custProspectId;
	}

	public Integer getCallTimes() {
		return callTimes;
	}

	public void setCallTimes(Integer callTimes) {
		this.callTimes = callTimes;
	}

	public Integer getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(Integer callStatus) {
		this.callStatus = callStatus;
	}

	public Integer getCallResult() {
		return callResult;
	}

	public void setCallResult(Integer callResult) {
		this.callResult = callResult;
	}

	public Integer getNextAction() {
		return nextAction;
	}

	public void setNextAction(Integer nextAction) {
		this.nextAction = nextAction;
	}

	public Date getNextActionDate() {
		return nextActionDate;
	}

	public void setNextActionDate(Date nextActionDate) {
		this.nextActionDate = nextActionDate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}