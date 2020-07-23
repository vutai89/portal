package com.mcredit.business.credit.dto;

import java.math.BigDecimal;
import java.util.Date;

public class CreditApplicationRequestDTO {
	private Long id;
	private Integer applyToObject;
	private Date contractDate;
	private String createdBy;
	private Long custId;
	private Integer disbursementChannel;
	private Integer disbursementMethod;
	private String duplicatedNote;
	private Integer hasInsurance;
	private Long insuAmount;
	private Integer insuCompany;
	private Long insuFee;
	private Long insuRate;
	private Integer insuStaffid;
	private Integer insuType;
	private Long intRate;
	private Integer isDuplicated;
	private String lastUpdatedBy;
	private Long lnAmount;
	private BigDecimal lnAmountMinusInsu;
	private Date lnEndDate;
	private String lnOtherPurpose;
	private Integer lnPurpose;
	private Date lnStartDate;
	private Integer lnTenor;
	private String mcContractNumber;
	private Date paymentDate;
	private Integer productId;
	private Integer rateIndex;
	private String recordStatus;
	private Long saleId;
	private Integer scope;
	private String status;
	private String lnTenorValue;
	private String transOfficeIdValue;
	private String hasInsuranceValue;
	private String insuCompanyValue;
	private String insuStaffIdValue;
	private String insuTypeValue;
	private String disbursementChannelValue;
	private String disbursementMethodValue;
	private String isDuplicatedValue;
	private String lnPurposeValue;
	private String applyToObjectValue;
	private String productCode;
	private String saleCode;

	public String getSaleCode() {
		return saleCode;
	}


	public void setSaleCode(String saleCode) {
		this.saleCode = saleCode;
	}


	public CreditApplicationRequestDTO(){
		
	}
	
	
	public String getProductCode() {
		return productCode;
	}


	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}


	public String getLnTenorValue() {
		return lnTenorValue;
	}


	public void setLnTenorValue(String lnTenorValue) {
		this.lnTenorValue = lnTenorValue;
	}


	public String getTransOfficeIdValue() {
		return transOfficeIdValue;
	}


	public void setTransOfficeIdValue(String transOfficeIdValue) {
		this.transOfficeIdValue = transOfficeIdValue;
	}


	public String getHasInsuranceValue() {
		return hasInsuranceValue;
	}


	public void setHasInsuranceValue(String hasInsuranceValue) {
		this.hasInsuranceValue = hasInsuranceValue;
	}


	public String getInsuCompanyValue() {
		return insuCompanyValue;
	}


	public void setInsuCompanyValue(String insuCompanyValue) {
		this.insuCompanyValue = insuCompanyValue;
	}


	public String getInsuStaffIdValue() {
		return insuStaffIdValue;
	}


	public void setInsuStaffIdValue(String insuStaffIdValue) {
		this.insuStaffIdValue = insuStaffIdValue;
	}


	public String getInsuTypeValue() {
		return insuTypeValue;
	}


	public void setInsuTypeValue(String insuTypeValue) {
		this.insuTypeValue = insuTypeValue;
	}


	public String getDisbursementChannelValue() {
		return disbursementChannelValue;
	}


	public void setDisbursementChannelValue(String disbursementChannelValue) {
		this.disbursementChannelValue = disbursementChannelValue;
	}


	public String getDisbursementMethodValue() {
		return disbursementMethodValue;
	}


	public void setDisbursementMethodValue(String disbursementMethodValue) {
		this.disbursementMethodValue = disbursementMethodValue;
	}


	public String getIsDuplicatedValue() {
		return isDuplicatedValue;
	}


	public void setIsDuplicatedValue(String isDuplicatedValue) {
		this.isDuplicatedValue = isDuplicatedValue;
	}


	public String getLnPurposeValue() {
		return lnPurposeValue;
	}


	public void setLnPurposeValue(String lnPurposeValue) {
		this.lnPurposeValue = lnPurposeValue;
	}


	public String getApplyToObjectValue() {
		return applyToObjectValue;
	}


	public void setApplyToObjectValue(String applyToObjectValue) {
		this.applyToObjectValue = applyToObjectValue;
	}


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getApplyToObject() {
		return applyToObject;
	}
	public void setApplyToObject(Integer applyToObject) {
		this.applyToObject = applyToObject;
	}
	public Date getContractDate() {
		return contractDate;
	}
	public void setContractDate(Date contractDate) {
		this.contractDate = contractDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Long getCustId() {
		return custId;
	}
	public void setCustId(Long custId) {
		this.custId = custId;
	}
	public Integer getDisbursementChannel() {
		return disbursementChannel;
	}
	public void setDisbursementChannel(Integer disbursementChannel) {
		this.disbursementChannel = disbursementChannel;
	}
	public Integer getDisbursementMethod() {
		return disbursementMethod;
	}
	public void setDisbursementMethod(Integer integer) {
		this.disbursementMethod = integer;
	}
	public String getDuplicatedNote() {
		return duplicatedNote;
	}
	public void setDuplicatedNote(String duplicatedNote) {
		this.duplicatedNote = duplicatedNote;
	}
	public Long getInsuAmount() {
		return insuAmount;
	}
	public void setInsuAmount(Long insuAmount) {
		this.insuAmount = insuAmount;
	}
	public Long getInsuFee() {
		return insuFee;
	}
	public void setInsuFee(Long insuFee) {
		this.insuFee = insuFee;
	}
	public Long getInsuRate() {
		return insuRate;
	}
	public void setInsuRate(Long insuRate) {
		this.insuRate = insuRate;
	}
	public Integer getInsuType() {
		return insuType;
	}
	public void setInsuType(Integer insuType) {
		this.insuType = insuType;
	}
	public Long getIntRate() {
		return intRate;
	}
	public void setIntRate(Long intRate) {
		this.intRate = intRate;
	}
	public Integer getHasInsurance() {
		return hasInsurance;
	}
	public void setHasInsurance(Integer hasInsurance) {
		this.hasInsurance = hasInsurance;
	}
	public Integer getInsuCompany() {
		return insuCompany;
	}
	public void setInsuCompany(Integer insuCompany) {
		this.insuCompany = insuCompany;
	}
	public Integer getInsuStaffid() {
		return insuStaffid;
	}
	public void setInsuStaffid(Integer insuStaffid) {
		this.insuStaffid = insuStaffid;
	}
	public Integer getIsDuplicated() {
		return isDuplicated;
	}
	public void setIsDuplicated(Integer isDuplicated) {
		this.isDuplicated = isDuplicated;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public Long getLnAmount() {
		return lnAmount;
	}
	public void setLnAmount(Long lnAmount) {
		this.lnAmount = lnAmount;
	}
	public BigDecimal getLnAmountMinusInsu() {
		return lnAmountMinusInsu;
	}
	public void setLnAmountMinusInsu(BigDecimal lnAmountMinusInsu) {
		this.lnAmountMinusInsu = lnAmountMinusInsu;
	}
	public Date getLnEndDate() {
		return lnEndDate;
	}
	public void setLnEndDate(Date lnEndDate) {
		this.lnEndDate = lnEndDate;
	}
	public String getLnOtherPurpose() {
		return lnOtherPurpose;
	}
	public void setLnOtherPurpose(String lnOtherPurpose) {
		this.lnOtherPurpose = lnOtherPurpose;
	}
	public Integer getLnPurpose() {
		return lnPurpose;
	}
	public void setLnPurpose(Integer lnPurpose) {
		this.lnPurpose = lnPurpose;
	}
	public Date getLnStartDate() {
		return lnStartDate;
	}
	public void setLnStartDate(Date lnStartDate) {
		this.lnStartDate = lnStartDate;
	}
	public Integer getLnTenor() {
		return lnTenor;
	}
	public void setLnTenor(Integer lnTenor) {
		this.lnTenor = lnTenor;
	}
	public String getMcContractNumber() {
		return mcContractNumber;
	}
	public void setMcContractNumber(String mcContractNumber) {
		this.mcContractNumber = mcContractNumber;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getRateIndex() {
		return rateIndex;
	}
	public void setRateIndex(Integer rateIndex) {
		this.rateIndex = rateIndex;
	}
	public String getRecordStatus() {
		return recordStatus;
	}
	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}
	public Long getSaleId() {
		return saleId;
	}
	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}
	public Integer getScope() {
		return scope;
	}
	public void setScope(Integer scope) {
		this.scope = scope;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getTransOfficeId() {
		return transOfficeId;
	}
	public void setTransOfficeId(Integer transOfficeId) {
		this.transOfficeId = transOfficeId;
	}
	private Integer transOfficeId;
}