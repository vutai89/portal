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
@Table(name="RULE_OUTPUT")
public class RuleOutput implements Serializable {

	@Id
	@GenericGenerator(name = "seq_rule_output"
				    , strategy = "com.mcredit.data.common.entity.SequenceGenerate"
					, parameters = @Parameter(name = "seqName", value = "SEQ_RULE_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_rule_output")
	@Column(name="ID")
	private Integer id;

	@Column(name="RULE_ID")
	private Integer ruleId;

	@Column(name="RULE_COMBINATION_KEY")
	private Integer ruleCombinationKey;

	@Column(name="RULE_PARAM_ID")
	private Integer ruleParamId;

	@Column(name="RULE_PARAM_DETAIL_ID")
	private Integer ruleParamDetailId;

	@Column(name="OUTPUT_KEY")
	private String outputKey;

	@Column(name="OUTPUT_DATA_TYPE")
	private String outputDataType;

	@Column(name="STATUS")
	private String status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

	public Integer getRuleCombinationKey() {
		return ruleCombinationKey;
	}

	public void setRuleCombinationKey(Integer ruleCombinationKey) {
		this.ruleCombinationKey = ruleCombinationKey;
	}

	public Integer getRuleParamId() {
		return ruleParamId;
	}

	public void setRuleParamId(Integer ruleParamId) {
		this.ruleParamId = ruleParamId;
	}

	public Integer getRuleParamDetailId() {
		return ruleParamDetailId;
	}

	public void setRuleParamDetailId(Integer ruleParamDetailId) {
		this.ruleParamDetailId = ruleParamDetailId;
	}

	public String getOutputKey() {
		return outputKey;
	}

	public void setOutputKey(String outputKey) {
		this.outputKey = outputKey;
	}

	public String getOutputDataType() {
		return outputDataType;
	}

	public void setOutputDataType(String outputDataType) {
		this.outputDataType = outputDataType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
