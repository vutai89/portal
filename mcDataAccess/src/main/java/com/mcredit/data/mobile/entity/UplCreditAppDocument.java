package com.mcredit.data.mobile.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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

@Entity
@Table(name="UPL_CREDIT_APP_DOCUMENT", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class UplCreditAppDocument implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name = "seq_UplCreditAppDocument" , strategy = "com.mcredit.data.common.entity.SequenceGenerate" , parameters = @Parameter(name = "seqName", value = "SEQ_UPL_CREDIT_APP_DOCUMENT_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_UplCreditAppDocument") 
	private Long Id;
	
	@CreationTimestamp
	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE", updatable=false)
	private Date createdDate;
	
	@Column(name = "UPL_CREDIT_APP_ID")
	private Long uplCreditAppId;
	
	@Column(name = "DOCUMENT_ID")
	private Long documentId;
	
	@Column(name = "VERSION")
	private Long version;
	
	@Column(name = "LOCAL_PATH_SERVER")
	private String localPathServer;
	
	@Column(name = "REMOTE_PATH_SERVER")
	private String remotePathServer;
	
	@Column(name = "STATUS")
	private String status;

	public Long getId() {
		return Id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public Long getUplCreditAppId() {
		return uplCreditAppId;
	}

	public Long getDocumentId() {
		return documentId;
	}

	public Long getVersion() {
		return version;
	}

	public String getLocalPathServer() {
		return localPathServer;
	}

	public String getRemotePathServer() {
		return remotePathServer;
	}

	public String getStatus() {
		return status;
	}

	public void setId(Long id) {
		Id = id;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setUplCreditAppId(Long uplCreditAppId) {
		this.uplCreditAppId = uplCreditAppId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public void setLocalPathServer(String localPathServer) {
		this.localPathServer = localPathServer;
	}

	public void setRemotePathServer(String remotePathServer) {
		this.remotePathServer = remotePathServer;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
