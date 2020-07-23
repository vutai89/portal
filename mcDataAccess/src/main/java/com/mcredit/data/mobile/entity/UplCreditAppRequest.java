package com.mcredit.data.mobile.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name="UPL_CREDIT_APP_REQUEST")
//@NamedQuery(name="UplCreditAppRequest.findAll", query="SELECT o FROM UplCreditAppRequest o")
//@NamedNativeQuery(name="UplCreditAppRequest.nextSeq",query="select SEQ_UPL_CREDIT_APP_REQUEST_ID.NEXTVAL from DUAL")
public class UplCreditAppRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "seq_UplCreditAppRequest" , strategy = "com.mcredit.data.common.entity.SequenceGenerate" , parameters = @Parameter(name = "seqName", value = "SEQ_UPL_CREDIT_APP_REQUEST_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_UplCreditAppRequest") 
	private Long id;
	
	@Column(name="RECORD_STATUS")
	private String recordStatus;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
	private Date lastUpdatedDate;

	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Column(name = "CREDIT_APP_ID")
	private Long creditAppId;
	
	@Column(name = "APP_NUMBER")
	private Long appNumber;
	
	@Column(name = "APP_ID")
	private String appId;
	
	@Column(name = "INSU_RATE")
	private Long insuRate;
	
	@Column(name = "SALE_CODE")
	private String saleCode;
	
	@Column(name = "SALE_ID")
	private Long saleId;
	
	@Column(name = "APP_CODE")
	private String appCode;
	
	@Column(name = "CUSTOMER_NAME")
	private String customerName;
	
	@Column(name = "CITIZEN_ID")
	private String citizenId;
	
	@Column(name = "ISSUE_DATE_CITIZEN")
	private Date issueDateCitizen;
	
	@Column(name = "ISSUE_PLACE")
	private String issuePlace;
	
	@Column(name = "BIRTH_DATE")
	private Date birthDate;
	
	@Column(name = "PRODUCT_ID")
	private Long productId;
	
	@Column(name = "TEMP_RESIDENCE")
	private String tempResidence;
	
	@Column(name = "LOAN_TENOR")
	private BigDecimal loanTenor;
	
	@Column(name = "LOAN_AMOUNT")
	private BigDecimal loanAmount;
	
	@Column(name = "HAS_INSURANCE")
	private Long hasInsurance;
	
	@Column(name = "MOBILE_IMEI")
	private String mobileImei;
	
	@Column(name = "APP_STATUS")
	private Long appStatus;
	
	@Column(name = "PREVIOUS_APP_STATUS")
	private Long previousStatus;
	
	@Column(name = "SHOP_CODE")
	private String shopCode;
	
	@Column(name = "COMPANY_TAX_NUMBER")
	private String companyTaxNumber;

	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "CHECKLIST")
	private String checkList;
	
	@Column(name = "ABORT_REASON")
	private Long abortReason;
	
	@Column(name = "ABORT_COMMENT")
	private String abortComment;
	
	@Column(name = "LAST_UPDATE_FROM_LOS")
	private Date lastUpdateFromLOS;
	
	@Column(name = "UPL_CUSTOMER_ID")
	private Long uplCustomerId;
	
	@Column(name = "MOBILE_TSA")
    private String mobileTSA;
	
	@Column(name = "GENDER")
    private String gender;
	
	@Column(name = "CUSTOMER_INCOME")
	private Long customerIncome;
	
	@Column(name="AFFILIATE_PARTNER_CODE")
	private String affiliatePartnerCode;	

	@Column(name = "HAS_COURIER")
	private Long hasCourier;
	
	public Date getLastUpdateFromLOS() {
		return lastUpdateFromLOS;
	}

	public void setLastUpdateFromLOS(Date lastUpdateFromLOS) {
		this.lastUpdateFromLOS = lastUpdateFromLOS;
	}

	public Long getAbortReason() {
		return abortReason;
	}

	public String getAbortComment() {
		return abortComment;
	}

	public void setAbortReason(Long abortReason) {
		this.abortReason = abortReason;
	}

	public void setAbortComment(String abortComment) {
		this.abortComment = abortComment;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Long getCreditAppId() {
		return creditAppId;
	}

	public void setCreditAppId(Long creditAppId) {
		this.creditAppId = creditAppId;
	}

	public Long getAppNumber() {
		return appNumber;
	}

	public void setAppNumber(Long appNumber) {
		this.appNumber = appNumber;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Long getInsuRate() {
		return insuRate;
	}

	public void setInsuRate(Long insuRate) {
		this.insuRate = insuRate;
	}

	public String getSaleCode() {
		return saleCode;
	}

	public void setSaleCode(String saleCode) {
		this.saleCode = saleCode;
	}

	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCitizenId() {
		return citizenId;
	}

	public void setCitizenId(String citizenId) {
		this.citizenId = citizenId;
	}

	public Date getIssueDateCitizen() {
		return issueDateCitizen;
	}

	public void setIssueDateCitizen(Date issueDateCitizen) {
		this.issueDateCitizen = issueDateCitizen;
	}

	public String getIssuePlace() {
		return issuePlace;
	}

	public void setIssuePlace(String issuePlace) {
		this.issuePlace = issuePlace;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getTempResidence() {
		return tempResidence;
	}

	public void setTempResidence(String tempResidence) {
		this.tempResidence = tempResidence;
	}

	public BigDecimal getLoanTenor() {
		return loanTenor;
	}

	public void setLoanTenor(BigDecimal loanTenor) {
		this.loanTenor = loanTenor;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public Long getHasInsurance() {
		return hasInsurance;
	}

	public void setHasInsurance(Long hasInsurance) {
		this.hasInsurance = hasInsurance;
	}

	public String getMobileImei() {
		return mobileImei;
	}

	public void setMobileImei(String mobileImei) {
		this.mobileImei = mobileImei;
	}

	public Long getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(Long appStatus) {
		this.appStatus = appStatus;
	}

	public Long getPreviousStatus() {
		return previousStatus;
	}

	public void setPreviousStatus(Long previousStatus) {
		this.previousStatus = previousStatus;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getCompanyTaxNumber() {
		return companyTaxNumber;
	}

	public void setCompanyTaxNumber(String companyTaxNumber) {
		this.companyTaxNumber = companyTaxNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCheckList() {
		return checkList;
	}

	public void setCheckList(String checkList) {
		this.checkList = checkList;
	}

	public Long getUplCustomerId() {
		return uplCustomerId;
	}

	public void setUplCustomerId(Long uplCustomerId) {
		this.uplCustomerId = uplCustomerId;
	}

	public String getMobileTSA() {
		return mobileTSA;
	}

	public void setMobileTSA(String mobileTSA) {
		this.mobileTSA = mobileTSA;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Long getCustomerIncome() {
		return customerIncome;
	}

	public void setCustomerIncome(Long customerIncome) {
		this.customerIncome = customerIncome;
	}

	public String getAffiliatePartnerCode() {
		return affiliatePartnerCode;
	}

	public void setAffiliatePartnerCode(String affiliatePartnerCode) {
		this.affiliatePartnerCode = affiliatePartnerCode;
	}

	public Long getHasCourier() {
		return hasCourier;
	}

	public void setHasCourier(Long hasCourier) {
		this.hasCourier = hasCourier;
	}	
}
