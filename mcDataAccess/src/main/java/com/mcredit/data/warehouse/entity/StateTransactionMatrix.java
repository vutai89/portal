package com.mcredit.data.warehouse.entity;

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
@Table(name="STATE_TRANSACTION_MATRIX")
public class StateTransactionMatrix implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6621985890166777721L;

	@Id
	@GenericGenerator(name = "seq_StateTransactionMatrix" , strategy = "com.mcredit.data.common.entity.SequenceGenerate" , parameters = @Parameter(name = "seqName", value = "SEQ_STATE_TRANSACTION_MATRIX_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_StateTransactionMatrix")
	private Long id;
	
	@Column(name="MODULE")
	private String module;
	
	@Column(name="WORK_FLOW")
	private Long workFlow;
	
	@Column(name="RULE_ID")
	private Long ruleId;
	
	@Column(name="EXPECTED_RULE_OUTPUT")
	private String expectedRuleOutput;
	
	@Column(name="OBJECT_TYPE")
	private String objectType;
	
	@Column(name="FUNCTION_ID")
	private Long functionId;
	
	@Column(name="STEP")
	private String step;
	
	@Column(name="ACTION_TYPE")
	private String actionType;
	
	@Column(name="ACTION")
	private String action;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Long getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(Long workFlow) {
		this.workFlow = workFlow;
	}

	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

	public String getExpectedRuleOutput() {
		return expectedRuleOutput;
	}

	public void setExpectedRuleOutput(String expectedRuleOutput) {
		this.expectedRuleOutput = expectedRuleOutput;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public Long getFunctionId() {
		return functionId;
	}

	public void setFunctionId(Long functionId) {
		this.functionId = functionId;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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