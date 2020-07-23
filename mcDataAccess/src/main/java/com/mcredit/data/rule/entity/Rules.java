package com.mcredit.data.rule.entity;

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
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="RULES")
public class Rules implements Serializable {

	@Id
	@GenericGenerator(name = "seq_rules"
				    , strategy = "com.mcredit.data.common.entity.SequenceGenerate"
					, parameters = @Parameter(name = "seqName", value = "SEQ_RULE_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_rules")
	@Column(name="ID")
	private Integer id;

	@Column(name="RECORD_STATUS")
	private String recordStatus;

	@Column(name="CREATED_BY", updatable=false)
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE", updatable=false)
	@CreationTimestamp
	private Date createdDate;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
	@UpdateTimestamp
	private Date lastUpdatedDate;

	@Column(name="RULE_FROM")
	private String ruleFrom;

	@Column(name="RULE_TYPE")
	private String ruleType;

	@Column(name="RULE_CODE")
	private String ruleCode;

	@Column(name="RULE_NAME")
	private String ruleName;

	@Temporal(TemporalType.DATE)
	@Column(name="START_EFF_DATE")
	private Date startEffDate;

	@Temporal(TemporalType.DATE)
	@Column(name="END_EFF_DATE")
	private Date endEffDate;

	@Column(name="STATUS")
	private String status;

	@Column(name="PARENT_RULE")
	private Integer parentRule;

	@Column(name="NUM_OF_RESULT")
	private Integer numOfResult;

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

	public String getRuleFrom() {
		return ruleFrom;
	}

	public void setRuleFrom(String ruleFrom) {
		this.ruleFrom = ruleFrom;
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String getRuleCode() {
		return ruleCode;
	}

	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public Date getStartEffDate() {
		return startEffDate;
	}

	public void setStartEffDate(Date startEffDate) {
		this.startEffDate = startEffDate;
	}

	public Date getEndEffDate() {
		return endEffDate;
	}

	public void setEndEffDate(Date endEffDate) {
		this.endEffDate = endEffDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getParentRule() {
		return parentRule;
	}

	public void setParentRule(Integer parentRule) {
		this.parentRule = parentRule;
	}

	public Integer getNumOfResult() {
		return numOfResult;
	}

	public void setNumOfResult(Integer numOfResult) {
		this.numOfResult = numOfResult;
	}

}
