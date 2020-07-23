package com.mcredit.data.ecm.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="DOCUMENT_TO_ECM", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
@NamedNativeQuery(name="DocumentToECM.nextSeq",query="select SEQ_DOCUMENT_TO_ECM_ID.NEXTVAL from DUAL")
public class DocumentToECM implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "seq_DocumentToECM", strategy = "com.mcredit.data.common.entity.SequenceGenerate", parameters = @Parameter(name = "seqName", value = "SEQ_DOCUMENT_TO_ECM_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_DocumentToECM")
	private Long id;

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
	
	@Column(name="DOCUMENT_ID")
	private Integer documentId;
	
	@Column(name="DOCUMENT_SOURCE")
	private String documentSource;

	@Column(name="VERSION_NUMBER")
	private Long versionNumber;

	@Column(name="BPM_APP_ID")
	private String bpmAppId;

	@Column(name="BPM_APP_NUMBER")
	private Integer bpmAppNumber;

	@Column(name="LD_NUMBER")
	private String ldNumber;

	@Column(name="PRODUCT_ID")
	private Integer productId;

	@Column(name="ID_NUMBER")
	private String idNumber;

	@Column(name="CUST_NAME")
	private String custName;

	@Column(name="USER_FILE_NAME")
	private String userFileName;

	@Column(name="SERVER_FILE_NAME")
	private String serverFileName;

	@Column(name="REMARK")
	private String remark;

	@Column(name="ECM_OBJECT_ID")
	private String ecmObjectId;

	@Temporal(TemporalType.DATE)
	@Column(name="UPLOAD_TIME")
	private Date uploadTime;
	
	@Column(name="STATUS")
	private String status;

	@Column(name="ERROR_MESSAGE")
	private String errorMessage;

	@Column(name="FOLDER")
	private String folder;

	@Column(name="DELETE_FLAG")
	private String deleteFlag;

	public DocumentToECM() {
		
	}
	
	public DocumentToECM( Integer documentId, String documentSource, Long versionNumber,
			String bpmAppId, Integer bpmAppNumber, String ldNumber, Integer productId, String idNumber, String custName,
			String userFileName, String serverFileName,String status, String folder ) {
		super();		
		this.documentId = documentId;
		this.documentSource = documentSource;
		this.versionNumber = versionNumber;
		this.bpmAppId = bpmAppId;
		this.bpmAppNumber = bpmAppNumber;
		this.ldNumber = ldNumber;
		this.productId = productId;
		this.idNumber = idNumber;
		this.custName = custName;
		this.userFileName = userFileName;
		this.serverFileName = serverFileName;		
		this.status = status;
		this.folder = folder;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Integer documentId) {
		this.documentId = documentId;
	}

	public String getDocumentSource() {
		return documentSource;
	}

	public void setDocumentSource(String documentSource) {
		this.documentSource = documentSource;
	}

	public Long getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Long versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getBpmAppId() {
		return bpmAppId;
	}

	public void setBpmAppId(String bpmAppId) {
		this.bpmAppId = bpmAppId;
	}

	public Integer getBpmAppNumber() {
		return bpmAppNumber;
	}

	public void setBpmAppNumber(Integer bpmAppNumber) {
		this.bpmAppNumber = bpmAppNumber;
	}

	public String getLdNumber() {
		return ldNumber;
	}

	public void setLdNumber(String ldNumber) {
		this.ldNumber = ldNumber;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getUserFileName() {
		return userFileName;
	}

	public void setUserFileName(String userFileName) {
		this.userFileName = userFileName;
	}

	public String getServerFileName() {
		return serverFileName;
	}

	public void setServerFileName(String serverFileName) {
		this.serverFileName = serverFileName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getEcmObjectId() {
		return ecmObjectId;
	}

	public void setEcmObjectId(String ecmObjectId) {
		this.ecmObjectId = ecmObjectId;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

}
