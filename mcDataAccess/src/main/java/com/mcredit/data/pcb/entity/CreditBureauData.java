package com.mcredit.data.pcb.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.soap.Text;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "CREDIT_BUREAU_DATA")
public class CreditBureauData implements Serializable {
	@Id
	@GenericGenerator(name = "seq_CreditBureauData", strategy = "com.mcredit.data.common.entity.SequenceGenerate", parameters = @Parameter(name = "seqName", value = "SEQ_CREDIT_BUREAU_DATA_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_CreditBureauData")
	private Long id;
	private static final long serialVersionUID = 1L;

	@Column(name = "RECORD_STATUS")
	private String recordStatus;

//	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_DATE")
	private Date createdDate;

//	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_UPDATED_DATE")
	private Date lastUpdatedDate;

	@Column(name = "CUST_ID")
	private Long custId;

	@Column(name = "CB_SOURCE")
	private String cbSource;

	@Column(name = "CB_TYPE")
	private String cbType;

	@Column(name = "CB_KEY")
	private String cbkey;

	@Column(name = "CB_DATA_DETAIL")
	private String cbDataDetail;

	@Column(name = "CUST_IDENTITY_NUMBER")
	private String custIdentityNumber;
	
	@Column(name = "CUST_IDENTITY_NUMBER_OLD")
	private String custIdentityNumberOld;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "BIRTH_DATE")
	private Date birthDate;
	
	@Column(name = "CUST_MOBILE")
	private String custMobile;
	
	@Column(name = "CB_CODE")
	private String cbCode;

	public String getCbCode() {
		return cbCode;
	}

	public void setCbCode(String cbCode) {
		this.cbCode = cbCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getCustIdentityNumberOld() {
		return custIdentityNumberOld;
	}


	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Long getCustId() {
		return custId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
	}

	public String getCbSource() {
		return cbSource;
	}

	public void setCbSource(String cbSource) {
		this.cbSource = cbSource;
	}

	public String getCbType() {
		return cbType;
	}

	public void setCbType(String cbType) {
		this.cbType = cbType;
	}

	public String getCbkey() {
		return cbkey;
	}

	public void setCbkey(String cbkey) {
		this.cbkey = cbkey;
	}

	public String getCbDataDetail() {
		return cbDataDetail;
	}

	public void setCbDataDetail(String cbDataDetail) {
		this.cbDataDetail = cbDataDetail;
	}

	public String getCustIdentityNumber() {
		return custIdentityNumber;
	}

	public void setCustIdentityNumber(String custIdentityNumber) {
		this.custIdentityNumber = custIdentityNumber;
	}
	
	public void setCustIdentityNumberOld(String custIdentityNumberOld) {
		this.custIdentityNumberOld = custIdentityNumberOld;
	}
	
	public String getCustMobile() {
		return custMobile;
	}

	public void setCustMobile(String custMobile) {
		this.custMobile = custMobile;
	}

	public CreditBureauData(Date createdDate, Date lastUpdatedDate, Long custId, String cbkey, String cbDataDetail, String custIdentityNumber, String custIdentityNumberOld,Date birthDate) {
		super();
		this.createdDate = createdDate;
		this.lastUpdatedDate = lastUpdatedDate;
		this.custId = custId;
		this.cbkey = cbkey;
		this.cbDataDetail = cbDataDetail;
		this.custIdentityNumber = custIdentityNumber;
		this.custIdentityNumberOld = custIdentityNumberOld;
		this.birthDate = birthDate;
	}

	public CreditBureauData() {
	}
}
