package com.mcredit.data.common.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


/**
 * The persistent class for the CODE_TABLE database table.
 * 
 */
@Entity(name="AuditDataChange")
@Table(name="AUDIT_DATA_CHANGE")
@NamedQuery(name="AuditDataChange.findAll", query="SELECT c FROM AuditDataChange c")
public class AuditDataChange implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public AuditDataChange() {};
	public AuditDataChange(Long action, String content, String updateBy) {
		this.action = action;
		this.content = content;
		this.lastUpdatedBy = updateBy;
		this.lastUpdatedDate = new Timestamp(System.currentTimeMillis());  
		this.tableName = "TMP";
		this.masterKey = 0;
		this.changeSeq = 0;
	};
	
	@Id
//	@Column(name="ID")
	@GenericGenerator(name = "seq_AuditDataChange", strategy = "com.mcredit.data.common.entity.SequenceGenerate", parameters = @Parameter(name = "seqName", value = "AUDIT_DATA_CHANGE_SEQ.NEXTVAL"))
	@GeneratedValue(generator = "seq_AuditDataChange")
	private Long id;

	@Column(name="LAST_UPDATED_DATE") 
	private Timestamp lastUpdatedDate;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Column(name="TABLE_NAME")
	private String tableName;

	@Column(name="MASTER_KEY")
	private Integer masterKey;

	@Column(name="REFERENCE_KEY")
	private Integer referenceKey;

	@Column(name="CHANGE_SEQ")
	private Integer changeSeq;

	@Column(name="ACTION")
	private Long action;
	
	@Column(name="CONTENT")
	private String content;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(Timestamp lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Integer getMasterKey() {
		return masterKey;
	}

	public void setMasterKey(Integer masterKey) {
		this.masterKey = masterKey;
	}

	public Integer getReferenceKey() {
		return referenceKey;
	}

	public void setReferenceKey(Integer referenceKey) {
		this.referenceKey = referenceKey;
	}

	public Integer getChangeSeq() {
		return changeSeq;
	}

	public void setChangeSeq(Integer changeSeq) {
		this.changeSeq = changeSeq;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	public Long getAction() {
		return action;
	}
	public void setAction(Long action) {
		this.action = action;
	}
	
}
