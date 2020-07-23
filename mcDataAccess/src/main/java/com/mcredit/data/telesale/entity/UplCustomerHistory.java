package com.mcredit.data.telesale.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * The persistent class for the UPL_CUSTOMER_HIST database table.
 * 
 */
@Entity
@Table(name = "UPL_CUSTOMER_HIST")
public class UplCustomerHistory implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -5082804180872735575L;

	@Id
	@GenericGenerator(name = "seq_UplCustomerHistory", strategy = "com.mcredit.data.common.entity.SequenceGenerate", parameters = @Parameter(name = "seqName", value = "SEQ_UPL_CUSTOMER_HIST_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_UplCustomerHistory")
	private Long id;
	
	@CreationTimestamp
	@Column(name = "LAST_UPDATED_DATE", updatable = false)
	private Date lastUpdatedDate;

	@Column(name = "UPL_CUSTOMER_ID")
	private Long uplCustomerId;

	@Column(name = "UPL_MASTER_ID")
	private Long uplMasterId;
	
	@Column(name = "REF_ID")
	private String refId;

	@Column(name = "RESPONSE_CODE")
	private String responseCode;
	
	@Column(name = "MESSAGE")
	private String message;
	
	public UplCustomerHistory() {
	}
	
	public UplCustomerHistory(Long uplCustomerId, Long uplMasterId, String refId, String responseCode, String message) {
		
		this.uplCustomerId = uplCustomerId;
		this.uplMasterId = uplMasterId;
		this.refId = refId;
		this.responseCode = responseCode;
		this.message = message;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Long getUplCustomerId() {
		return uplCustomerId;
	}

	public void setUplCustomerId(Long uplCustomerId) {
		this.uplCustomerId = uplCustomerId;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public Long getUplMasterId() {
		return uplMasterId;
	}

	public void setUplMasterId(Long uplMasterId) {
		this.uplMasterId = uplMasterId;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}