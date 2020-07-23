package com.mcredit.data.credit.entity;

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

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name="CREDIT_APP_SIGNATURE")
@NamedNativeQuery(name="CreditAppSignature.nextSeq",query="select SEQ_CREDIT_APP_SIGNATURE_ID.nextval from dual")
public class CreditApplicationSignature implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "seq_CreditApplicationSignature", strategy = "com.mcredit.data.common.entity.SequenceGenerate", parameters = @Parameter(name = "seqName", value = "SEQ_CREDIT_APP_SIGNATURE_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_CreditApplicationSignature")
	private Long id;

	@Column(name="CREATED_BY")
	private String createdBy;

	@CreationTimestamp
	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE", updatable=false)
	private Date createdDate;

	@Column(name="CREDIT_APP_ID")
	private Long creditAppId;

	@Column(name="RECORD_STATUS")
	private String recordStatus;

	@Column(name="VERSION")
	private Long version;

	@Column(name="MC_CONTRACT_NUMBER")
	private String mcContractNumber;

	@Column(name="SIGNATURE")
	private String signature;

	@Column(name="SIGNATURE_CONTENT")
	private String signatureContent;

	public CreditApplicationSignature(){
		super();
	}
	
	public CreditApplicationSignature(String createdBy,
			Date createdDate, Long creditAppId, String recordStatus,
			Long version, String mcContractNumber, String signature,
			String signatureContent) {
		super();
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.creditAppId = creditAppId;
		this.recordStatus = recordStatus;
		this.version = version;
		this.mcContractNumber = mcContractNumber;
		this.signature = signature;
		this.signatureContent = signatureContent;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getCreditAppId() {
		return creditAppId;
	}

	public void setCreditAppId(Long creditAppId) {
		this.creditAppId = creditAppId;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getMcContractNumber() {
		return mcContractNumber;
	}

	public void setMcContractNumber(String mcContractNumber) {
		this.mcContractNumber = mcContractNumber;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSignatureContent() {
		return signatureContent;
	}

	public void setSignatureContent(String signatureContent) {
		this.signatureContent = signatureContent;
	}

}
