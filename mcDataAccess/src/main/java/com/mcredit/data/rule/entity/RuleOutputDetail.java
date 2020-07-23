package com.mcredit.data.rule.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name="RULE_OUTPUT_DETAIL")
public class RuleOutputDetail implements Serializable {

	@Id
	@GenericGenerator(name = "seq_rule_output_detail"
				    , strategy = "com.mcredit.data.common.entity.SequenceGenerate"
					, parameters = @Parameter(name = "seqName", value = "SEQ_RULE_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_rule_output_detail")
	@Column(name="ID")
	private Integer id;

	@Column(name="RULE_OUTPUT_ID")
	private Integer ruleOutputId;

	@Column(name="OUTPUT_VALUE")
	private String outputValue;

	@Column(name="OUTPUT_ORDER")
	private Integer outputOrder;

	@Column(name="STATUS")
	private String status;

	@Column(name="START_EFF_DATE")
	private String startEffDate;

	@Column(name="END_EFF_DATE")
	private String endEffDate;

	@Column(name="OUTPUT_TYPE")
	private String outputType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRuleOutputId() {
		return ruleOutputId;
	}

	public void setRuleOutputId(Integer ruleOutputId) {
		this.ruleOutputId = ruleOutputId;
	}

	public String getOutputValue() {
		return outputValue;
	}

	public void setOutputValue(String outputValue) {
		this.outputValue = outputValue;
	}

	public Integer getOutputOrder() {
		return outputOrder;
	}

	public void setOutputOrder(Integer outputOrder) {
		this.outputOrder = outputOrder;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStartEffDate() {
		return startEffDate;
	}

	public void setStartEffDate(String startEffDate) {
		this.startEffDate = startEffDate;
	}

	public String getEndEffDate() {
		return endEffDate;
	}

	public void setEndEffDate(String endEffDate) {
		this.endEffDate = endEffDate;
	}

	public String getOutputType() {
		return outputType;
	}

	public void setOutputType(String outputType) {
		this.outputType = outputType;
	}

}
