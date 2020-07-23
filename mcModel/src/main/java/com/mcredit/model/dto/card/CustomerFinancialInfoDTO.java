package com.mcredit.model.dto.card;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CustomerFinancialInfoDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5047974134581910548L;
	
	private Long id;
	private String accountNumberAtBank;
	private BigDecimal avgAccountBal;
	private BigDecimal avgElectricBill;
	private String bankBranch;
	private String bankName;
	private String createdBy;
	private Date createdDate;
	private String creditInOtherBank;
	private Long custId;
	private BigDecimal custIncome;
	private Integer insuTerm;
	private String insuTermValue;
	private BigDecimal insuTermFee;
	private String insuTermOther;
	private String lastUpdatedBy;
	private Date lastUpdatedDate;
	private Integer lifeInsuCompanyId;
	private String lifeInsuCompanyIdValue;
	private BigDecimal paymentAmountAtBank;
	private String recordStatus;
	private Integer s37Data;
	private String s37DataValue;
	private String spouseCreditInOtherBank;

	public CustomerFinancialInfoDTO() {
	}

	public String getInsuTermValue() {
		return insuTermValue;
	}

	public void setInsuTermValue(String insuTermValue) {
		this.insuTermValue = insuTermValue;
	}

	public String getLifeInsuCompanyIdValue() {
		return lifeInsuCompanyIdValue;
	}

	public void setLifeInsuCompanyIdValue(String lifeInsuCompanyIdValue) {
		this.lifeInsuCompanyIdValue = lifeInsuCompanyIdValue;
	}

	public String getS37DataValue() {
		return s37DataValue;
	}

	public void setS37DataValue(String s37DataValue) {
		this.s37DataValue = s37DataValue;
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
		this.accountNumberAtBank = accountNumberAtBank;
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
		this.bankBranch = bankBranch;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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
		this.creditInOtherBank = creditInOtherBank;
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
		this.insuTermOther = insuTermOther;
	}

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
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
		this.recordStatus = recordStatus;
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
		this.spouseCreditInOtherBank = spouseCreditInOtherBank;
	}

}