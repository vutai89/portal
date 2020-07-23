package com.mcredit.data.customer.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;


/**
 * The persistent class for the CUST_FINANCIAL_INFO database table.
 * 
 */
@Entity
@Table(name="CUST_FINANCIAL_INFO")
public class CustomerFinancialInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "seq_cfi"
				    , strategy = "com.mcredit.data.common.entity.SequenceGenerate"
					, parameters = @Parameter(name = "seqName", value = "SEQ_CUST_FINANCIAL_INFO_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_cfi")
	private Long id;

	@Column(name="ACCOUNT_NUMBER_AT_BANK")
	private String accountNumberAtBank;

	@Column(name="AVG_ACCOUNT_BAL")
	private BigDecimal avgAccountBal;

	@Column(name="AVG_ELECTRIC_BILL")
	private BigDecimal avgElectricBill;

	@Column(name="BANK_BRANCH")
	private String bankBranch;

	@Column(name="BANK_NAME")
	private String bankName;

	@Column(name="CREATED_BY", updatable=false)
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE", updatable=false)
	@CreationTimestamp
	private Date createdDate;

	@Column(name="CREDIT_IN_OTHER_BANK")
	private String creditInOtherBank;

	@Column(name="CUST_ID")
	private Long custId;

	@Column(name="CUST_INCOME")
	private BigDecimal custIncome;

	@Column(name="INSU_TERM")
	private Integer insuTerm;

	@Column(name="INSU_TERM_FEE")
	private BigDecimal insuTermFee;

	@Column(name="INSU_TERM_OTHER")
	private String insuTermOther;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
	@UpdateTimestamp
	private Date lastUpdatedDate;

	@Column(name="LIFE_INSU_COMPANY_ID")
	private Integer lifeInsuCompanyId;

	@Column(name="PAYMENT_AMOUNT_AT_BANK")
	private BigDecimal paymentAmountAtBank;

	@Column(name="RECORD_STATUS")
	private String recordStatus;

	@Column(name="S37_DATA")
	private Integer s37Data;

	@Column(name="SPOUSE_CREDIT_IN_OTHER_BANK")
	private String spouseCreditInOtherBank;

	public CustomerFinancialInfo() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountNumberAtBank() {
		return this.accountNumberAtBank;
	}

	public void setAccountNumberAtBank(String accountNumberAtBank) {
		this.accountNumberAtBank = accountNumberAtBank!=null?accountNumberAtBank.trim():null;
	}

	public BigDecimal getAvgAccountBal() {
		return this.avgAccountBal;
	}

	public void setAvgAccountBal(BigDecimal avgAccountBal) {
		this.avgAccountBal = avgAccountBal;
	}

	public BigDecimal getAvgElectricBill() {
		return this.avgElectricBill;
	}

	public void setAvgElectricBill(BigDecimal avgElectricBill) {
		this.avgElectricBill = avgElectricBill;
	}

	public String getBankBranch() {
		return this.bankBranch;
	}

	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch!=null?bankBranch.trim():null;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName!=null?bankName.trim():null;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy!=null?createdBy.trim():null;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreditInOtherBank() {
		return this.creditInOtherBank;
	}

	public void setCreditInOtherBank(String creditInOtherBank) {
		this.creditInOtherBank = creditInOtherBank!=null?creditInOtherBank.trim():null;
	}

	public Long getCustId() {
		return this.custId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
	}

	public BigDecimal getCustIncome() {
		return this.custIncome;
	}

	public void setCustIncome(BigDecimal custIncome) {
		this.custIncome = custIncome;
	}

	public Integer getInsuTerm() {
		return this.insuTerm;
	}

	public void setInsuTerm(Integer insuTerm) {
		this.insuTerm = insuTerm;
	}

	public BigDecimal getInsuTermFee() {
		return this.insuTermFee;
	}

	public void setInsuTermFee(BigDecimal insuTermFee) {
		this.insuTermFee = insuTermFee;
	}

	public String getInsuTermOther() {
		return this.insuTermOther;
	}

	public void setInsuTermOther(String insuTermOther) {
		this.insuTermOther = insuTermOther!=null?insuTermOther.trim():null;
	}

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy!=null?lastUpdatedBy.trim():null;
	}

	public Date getLastUpdatedDate() {
		return this.lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Integer getLifeInsuCompanyId() {
		return this.lifeInsuCompanyId;
	}

	public void setLifeInsuCompanyId(Integer lifeInsuCompanyId) {
		this.lifeInsuCompanyId = lifeInsuCompanyId;
	}

	public BigDecimal getPaymentAmountAtBank() {
		return this.paymentAmountAtBank;
	}

	public void setPaymentAmountAtBank(BigDecimal paymentAmountAtBank) {
		this.paymentAmountAtBank = paymentAmountAtBank;
	}

	public String getRecordStatus() {
		return this.recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus!=null?recordStatus.trim():null;
	}

	public Integer getS37Data() {
		return this.s37Data;
	}

	public void setS37Data(Integer s37Data) {
		this.s37Data = s37Data;
	}

	public String getSpouseCreditInOtherBank() {
		return this.spouseCreditInOtherBank;
	}

	public void setSpouseCreditInOtherBank(String spouseCreditInOtherBank) {
		this.spouseCreditInOtherBank = spouseCreditInOtherBank!=null?spouseCreditInOtherBank.trim():null;
	}

}