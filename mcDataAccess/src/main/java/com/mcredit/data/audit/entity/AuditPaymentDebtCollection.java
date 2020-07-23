package com.mcredit.data.audit.entity;

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

@Entity
@Table(name = "CONSOLIDATE_PAYMENT")
public class AuditPaymentDebtCollection implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "seq_AuditPaymentDebtCollection", strategy = "com.mcredit.data.common.entity.SequenceGenerate", parameters = @Parameter(name = "seqName", value = "SEQ_AUDIT_PAYMENT_DEBT_COLLECTION_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_AuditPaymentDebtCollection")
	private Long id;

	@Column(name = "TP_PARTNER_REFID")
	private String tpPartnerRefId;

	@Column(name = "MC_PARTNER_REFID")
	private String mcPartnerRefId;

	@Column(name = "CONTRACT_ID")
	private String contractId;

	@Column(name = "TP_CONTRACT_ID")
	private String tpContractId;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "TP_CONTRACT_FEE")
	private Long tpContractFee;

	@Column(name = "MC_CONTRACT_FEE")
	private Long mcContractFee;

	@Temporal(TemporalType.DATE)
	@Column(name = "CONTRACT_DATE")
	private Date contractDate;

	@Column(name = "TP_STATUS")
	private String tpStatus;

	@Column(name = "MC_STATUS")
	private String mcStatus;

	@Column(name = "RESULT")
	private String result;

	@Column(name = "PARTNER_ID")
	private int partnerId;

	@Column(name = "WORKFLOW")
	private String workFlow;

	@Column(name = "TIME_CONTROL")
	private String timeControl;

	public String getTimeControl() {
		return timeControl;
	}

	public void setTimeControl(String timeControl) {
		this.timeControl = timeControl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTpPartnerRefId() {
		return tpPartnerRefId;
	}

	public void setTpPartnerRefId(String tpPartnerRefId) {
		this.tpPartnerRefId = tpPartnerRefId;
	}

	public String getMcPartnerRefId() {
		return mcPartnerRefId;
	}

	public void setMcPartnerRefId(String mcPartnerRefId) {
		this.mcPartnerRefId = mcPartnerRefId;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getTpContractId() {
		return tpContractId;
	}

	public void setTpContractId(String tpContractId) {
		this.tpContractId = tpContractId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getTpContractFee() {
		return tpContractFee;
	}

	public void setTpContractFee(Long tpContractFee) {
		this.tpContractFee = tpContractFee;
	}

	public Long getMcContractFee() {
		return mcContractFee;
	}

	public void setMcContractFee(Long mcContractFee) {
		this.mcContractFee = mcContractFee;
	}

	public Date getContractDate() {
		return contractDate;
	}

	public void setContractDate(Date contractDate) {
		this.contractDate = contractDate;
	}

	public String getTpStatus() {
		return tpStatus;
	}

	public void setTpStatus(String tpStatus) {
		this.tpStatus = tpStatus;
	}

	public String getMcStatus() {
		return mcStatus;
	}

	public void setMcStatus(String mcStatus) {
		this.mcStatus = mcStatus;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(int partnerId) {
		this.partnerId = partnerId;
	}

	public String getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(String workFlow) {
		this.workFlow = workFlow;
	}

}
