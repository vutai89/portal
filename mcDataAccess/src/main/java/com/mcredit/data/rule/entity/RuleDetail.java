package com.mcredit.data.rule.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.mcredit.model.enums.Constant;
import com.mcredit.util.StringUtils;

@Entity
@Table(name="RULE_DETAIL")
public class RuleDetail implements Serializable {

	@Id
	@GenericGenerator(name = "seq_rule_detail"
				    , strategy = "com.mcredit.data.common.entity.SequenceGenerate"
					, parameters = @Parameter(name = "seqName", value = "SEQ_RULE_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_rule_detail")
	@Column(name="ID")
	private Integer id;

	@Column(name="RULE_ID")
	private Integer ruleId;

	@Column(name="RULE_COMBINATION_TYPE")
	private String ruleCombinationType;

	@Column(name="RULE_COMBINATION_DEFINITION")
	private String ruleCombinationDefinition;

	@Column(name="STATUS")
	private String status;

	@Column(name="MAPPING_COLUMN_NAME")
	private String mappingColumnName;

	@Column(name="RULE_CACHE_DEFINITION")
	private String ruleCacheDefinition;

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

	public String getRuleCombinationType() {
		return ruleCombinationType;
	}

	public void setRuleCombinationType(String ruleCombinationType) {
		this.ruleCombinationType = ruleCombinationType;
	}

	public String getRuleCombinationDefinition() {
		return ruleCombinationDefinition;
	}

	public void setRuleCombinationDefinition(String ruleCombinationDefinition) {
		this.ruleCombinationDefinition = ruleCombinationDefinition;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMappingColumnName() {
		return mappingColumnName;
	}

	public void setMappingColumnName(String mappingColumnName) {
		this.mappingColumnName = mappingColumnName;
	}

	public String getRuleCacheDefinition() {
		return ruleCacheDefinition;
	}

	public void setRuleCacheDefinition(String ruleCacheDefinition) {
		this.ruleCacheDefinition = ruleCacheDefinition;
	}

	public String getRuleSQL2InitCache() {
		if(StringUtils.isNullOrEmpty(this.ruleCacheDefinition)) {
			return null;
		}
		String[] sqlQuery = this.ruleCacheDefinition.split(Constant.RULE_SQL_DELIMITER);
		return sqlQuery[0];
	}

	public String getRuleSQL2GetCacheInputKey() {
		if(StringUtils.isNullOrEmpty(this.ruleCacheDefinition)) {
			return null;
		}
		String[] sqlQuery = this.ruleCacheDefinition.split(Constant.RULE_SQL_DELIMITER);
		if(sqlQuery.length > 1) {
			return sqlQuery[1];
		} else {
			return null;
		}
	}
}
