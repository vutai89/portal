package com.mcredit.data.common.entity;

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

@Entity(name="UDFValues")
@Table(name="UDF_VALUES")
public class UDFValues implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "seq_UDFValues" , strategy = "com.mcredit.data.common.entity.SequenceGenerate" , parameters = @Parameter(name = "seqName", value = "SEQ_UDF_VALUE_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_UDFValues") 
	@Column(name="ID")
	private Long id;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
	private Date lastUpdatedDate;

	@Column(name="RECORD_STATUS")
	private String recordStatus;

	@Column(name="UDF_ID")
	private Integer udfId;

	@Column(name="UDF_MASTER_ID")
	private Integer udfMasterId;

	@Column(name="UDF_MASTER_TYPE")
	private String udfMasterType;

	@Column(name="CODE_TABLE_ID")
	private Integer codeTableId;

	@Column(name="UDF_VALUE")
	private String udfValue;

	@Column(name="UDF_KEY")
	private String udfKey;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Integer getUdfId() {
		return udfId;
	}

	public void setUdfId(Integer udfId) {
		this.udfId = udfId;
	}

	public Integer getUdfMasterId() {
		return udfMasterId;
	}

	public void setUdfMasterId(Integer udfMasterId) {
		this.udfMasterId = udfMasterId;
	}

	public String getUdfMasterType() {
		return udfMasterType;
	}

	public void setUdfMasterType(String udfMasterType) {
		this.udfMasterType = udfMasterType;
	}

	public Integer getCodeTableId() {
		return codeTableId;
	}

	public void setCodeTableId(Integer codeTableId) {
		this.codeTableId = codeTableId;
	}

	public String getUdfValue() {
		return udfValue;
	}

	public void setUdfValue(String udfValue) {
		this.udfValue = udfValue;
	}

	public String getUdfKey() {
		return udfKey;
	}

	public void setUdfKey(String udfKey) {
		this.udfKey = udfKey;
	}

}
