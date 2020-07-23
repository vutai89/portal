package com.mcredit.data.mobile.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="UPL_CREDIT_APP_FILES", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
//@NamedQuery(name="UplCreditAppFiles.findAll", query="SELECT o FROM UplCreditAppFiles o")
//@NamedNativeQuery(name="UplCreditAppFiles.nextSeq",query="select SEQ_UPL_CREDIT_APP_FILES_ID.NEXTVAL from DUAL")
public class UplCreditAppFiles implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name = "seq_UplCreditAppFiles" , strategy = "com.mcredit.data.common.entity.SequenceGenerate" , parameters = @Parameter(name = "seqName", value = "SEQ_UPL_CREDIT_APP_FILES_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_UplCreditAppFiles")
	private Long id;

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
	
	@Column(name = "UPL_CREDIT_APP_ID")
	private Long uplCreditAppId;
	
	@Column(name = "DOCUMENT_ID")
	private Long documentId;
	
	@Column(name = "DOCUMENT_SEQ")
	private Long documentSeq;
	
	@Column(name = "VERSION")
	private Long version;
	
	@Column(name = "FILE_NAME")
	private String fileName;
	
	@Column(name = "FILE_TYPE")
	private String fileType;
	
	@Column(name = "FILE_PATH_SERVER")
	private String filePathServer;
	
	@Column(name = "MIMETYPE")
	private String mimeType;
	
	@Column(name = "STATUS")
	private String status;

	public Long getId() {
		return id;
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

	public Long getUplCreditAppId() {
		return uplCreditAppId;
	}

	public Long getDocumentId() {
		return documentId;
	}

	public Long getDocumentSeq() {
		return documentSeq;
	}

	public Long getVersion() {
		return version;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public String getFilePathServer() {
		return filePathServer;
	}

	public String getMimeType() {
		return mimeType;
	}

	public String getStatus() {
		return status;
	}

	public void setId(Long id) {
		this.id = id;
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

	public void setUplCreditAppId(Long uplCreditAppId) {
		this.uplCreditAppId = uplCreditAppId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public void setDocumentSeq(Long documentSeq) {
		this.documentSeq = documentSeq;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public void setFilePathServer(String filePathServer) {
		this.filePathServer = filePathServer;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
