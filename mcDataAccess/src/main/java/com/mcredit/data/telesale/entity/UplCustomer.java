package com.mcredit.data.telesale.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * The persistent class for the UPL_CUSTOMER database table.
 * 
 */
@Entity
@Table(name = "UPL_CUSTOMER")
@NamedQuery(name = "UplCustomer.findAll", query = "SELECT p FROM UplCustomer p")
public class UplCustomer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "seq_UplCustomer", strategy = "com.mcredit.data.common.entity.SequenceGenerate", parameters = @Parameter(name = "seqName", value = "SEQ_UPL_CUSTOMER_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_UplCustomer")
	private Long id;

	@Column(name = "UPL_DETAIL_ID")
	private Long uplDetailId;

	@Column(name = "CUSTOMER_NAME")
	private String customerName;

	@Column(name = "BIRTH_DATE")
	private String birthDate;

	@Column(name = "MOBILE")
	private String mobile;

	@Column(name = "IDENTITY_NUMBER")
	private String identityNumber;

	@Column(name = "ADDRESS")
	private String address;

	@Column(name = "INCOME")
	private String income;

	@Column(name = "NOTE")
	private String note;

	@Column(name = "RESPONSE_CODE")
	private String responseCode;

	@Column(name = "MESSAGE")
	private String message;

	@Column(name = "TELCO")
	private String telco;

	@Column(name = "SCORE_RANGE")
	private String scoreRange;

	@Column(name = "SOURCE_SYSTEM")
	private String sourceSystem;

	@Column(name = "PROVINCE")
	private String province;

	@Column(name = "MIN_SCORE")
	private Long minScore;

	@Column(name = "MAX_SCORE")
	private Long maxScore;

	@Column(name = "PRODUCT_ID")
	private Long productId;

	@CreationTimestamp
	@Column(name = "LAST_UPDATED_DATE", updatable = false)
	private Date lastUpdatedDate;

	@Column(name = "REF_ID")
	private String refId;

	// for XSell
 @Column(name = "IDENTITY_NUMBER_ARMY")
    private String identityNumberArmy;

    @Column(name = "APP_PRODUCT_NAME")
    private String appProductName;

    @Column(name = "APP_PRODUCT_CODE")
    private String appProductCode;

    @Column(name = "APP_LOAN_APPROVED_AMT")
    private Long appLoanApprovedAmt;

    @Column(name = "APP_INT_RATE")
    private Double appIntRate;

    @Column(name = "APP_TERM_LOAN")
    private Long appTermLoan;

    @Temporal(TemporalType.DATE)
    @Column(name = "DISBURSEMENT_DATE")
    private Date disbursementDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "MAT_DATE")
    private Date matDate;

    @Column(name = "APP_EMI")
    private Long appEmi;

    @Column(name = "COMMODITIES_CODE")
    private String commoditiesCode;

    @Column(name = "PRE_PRODUCT_NAME")
    private String preProductName;

    @Column(name = "PRE_PRODUCT_CODE")
    private String preProductCode;

    @Column(name = "PRE_MIN_LIMIT")
    private Long preMinLimit;

    @Column(name = "PRE_MAX_LIMIT")
    private Long preMaxLimit;

    @Column(name = "PRE_MIN_TENOR")
    private Long preMinTenor;

    @Column(name = "PRE_MAX_TENOR")
    private Long preMaxTenor;

    @Column(name = "PRE_MIN_EMI")
    private Long preMinEmi;

    @Column(name = "PRE_MAX_EMI")
    private Long preMaxEmi;

    @Column(name = "DATA_SOURCE")
    private String dataSource;

    @Column(name = "LEAD_SOURCE")
    private String leadSource;

    @Column(name = "CUST_ID")
    private Long custId;
	
    @Column(name = "UDF01")
    private String udf01;
    
    @Column(name = "UDF02")
    private String udf02;
    
    @Column(name = "UDF03")
    private String udf03;
    
    @Column(name = "UDF04")
    private String udf04;
    
    @Column(name = "UDF05")
    private String udf05;
    
    @Column(name = "UDF06")
    private String udf06;
    
    @Column(name = "UDF07")
    private String udf07;
    
    @Column(name = "UDF08")
    private String udf08;
    
    @Column(name = "UDF09")
    private String udf09;

    @Column(name = "APP_NUMBER")
    private Long appNumber;
    
    @Column(name = "CAMPAIGN_CODE")
    private String campaignCode;
    
    @Column(name = "GENDER")
    private String gender;
    
	public UplCustomer() {
	}

	/* Lead Gen */
	public UplCustomer(Long uplDetailId, String customerName, String birthDate,
			String mobile, String identityNumber, String address,
			String income, String note, String responseCode, String message,
			String telco, String scoreRange, String sourceSystem,
			String province, Long minScore, Long maxScore, Long productId,
			Date lastUpdatedDate, String refId, String campaignCode, String gender) {

		this.uplDetailId = uplDetailId;
		this.customerName = customerName;
		this.birthDate = birthDate;
		this.mobile = mobile;
		this.identityNumber = identityNumber;
		this.address = address;
		this.income = income;
		this.note = note;
		this.responseCode = responseCode;
		this.message = message;
		this.telco = telco;
		this.scoreRange = scoreRange;
		this.sourceSystem = sourceSystem;
		this.province = province;
		this.minScore = minScore;
		this.maxScore = maxScore;
		this.productId = productId;
		this.lastUpdatedDate = lastUpdatedDate;
		this.refId = refId;
		this.campaignCode = campaignCode;
		this.gender = gender;
	}
	
	/* AMO */
	public UplCustomer(Long uplDetailId, String customerName, String birthDate,
			String mobile, String identityNumber, String income, String responseCode,
			String message, String province, Long minScore, Long maxScore,
			Long productId, Date lastUpdatedDate, String refId) {

		this.uplDetailId = uplDetailId;
		this.customerName = customerName;
		this.birthDate = birthDate;
		this.mobile = mobile;
		this.identityNumber = identityNumber;
		this.income = income;
		this.responseCode = responseCode;
		this.message = message;
		this.province = province;
		this.minScore = minScore;
		this.maxScore = maxScore;
		this.productId = productId;
		this.lastUpdatedDate = lastUpdatedDate;
		this.refId = refId;
		this.gender = gender;
		this.address = address;
	}
	
	public UplCustomer(Long uplDetailId, String birthDate, String identityNumber, String responseCode, String identityNumberArmy,
            String appProductName, String appProductCode, Long appLoanApprovedAmt, Double appIntRate, Long appTermLoan, Date disbursementDate,
            Date matDate, Long appEmi, String commoditiesCode, String preProductName, String preProductCode, Long preMinLimit, Long preMaxLimit,
            Long preMinTenor, Long preMaxTenor, Long preMinEmi, Long preMaxEmi, String dataSource, String leadSource, Long custId, String customerName, Long appNumber) {
        this.uplDetailId = uplDetailId;
        this.birthDate = birthDate;
        this.identityNumber = identityNumber;
        this.responseCode = responseCode;
        this.identityNumberArmy = identityNumberArmy;
        this.appProductName = appProductName;
        this.appProductCode = appProductCode;
        this.appLoanApprovedAmt = appLoanApprovedAmt;
        this.appIntRate = appIntRate;
        this.appTermLoan = appTermLoan;
        this.disbursementDate = disbursementDate;
        this.matDate = matDate;
        this.appEmi = appEmi;
        this.commoditiesCode = commoditiesCode;
        this.preProductName = preProductName;
        this.preProductCode = preProductCode;
        this.preMinLimit = preMinLimit;
        this.preMaxLimit = preMaxLimit;
        this.preMinTenor = preMinTenor;
        this.preMaxTenor = preMaxTenor;
        this.preMinEmi = preMinEmi;
        this.preMaxEmi = preMaxEmi;
        this.dataSource = dataSource;
        this.leadSource = leadSource;
        this.custId = custId;
        this.customerName = customerName;
        this.appNumber = appNumber;
    }

    public Long getAppLoanApprovedAmt() {
        return appLoanApprovedAmt;
    }

    public void setAppLoanApprovedAmt(Long appLoanApprovedAmt) {
        this.appLoanApprovedAmt = appLoanApprovedAmt;
    }


    public Double getAppIntRate() {
        return appIntRate;
    }

    public void setAppIntRate(Double appIntRate) {
        this.appIntRate = appIntRate;
    }

    public Long getAppTermLoan() {
        return appTermLoan;
    }

    public void setAppTermLoan(Long appTermLoan) {
        this.appTermLoan = appTermLoan;
    }

    public Date getDisbursementDate() {
        return disbursementDate;
    }

    public void setDisbursementDate(Date disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    public Date getMatDate() {
        return matDate;
    }

    public void setMatDate(Date matDate) {
        this.matDate = matDate;
    }

    public Long getAppEmi() {
        return appEmi;
    }

    public void setAppEmi(Long appEmi) {
        this.appEmi = appEmi;
    }

    public String getCommoditiesCode() {
        return commoditiesCode;
    }

    public void setCommoditiesCode(String commoditiesCode) {
        this.commoditiesCode = commoditiesCode;
    }

    public Long getPreMinLimit() {
        return preMinLimit;
    }

    public void setPreMinLimit(Long preMinLimit) {
        this.preMinLimit = preMinLimit;
    }

    public Long getPreMaxLimit() {
        return preMaxLimit;
    }

    public void setPreMaxLimit(Long preMaxLimit) {
        this.preMaxLimit = preMaxLimit;
    }

    public Long getPreMinTenor() {
        return preMinTenor;
    }

    public void setPreMinTenor(Long preMinTenor) {
        this.preMinTenor = preMinTenor;
    }

    public Long getPreMaxTenor() {
        return preMaxTenor;
    }

    public void setPreMaxTenor(Long preMaxTenor) {
        this.preMaxTenor = preMaxTenor;
    }

    public Long getPreMinEmi() {
        return preMinEmi;
    }

    public void setPreMinEmi(Long preMinEmi) {
        this.preMinEmi = preMinEmi;
    }

    public Long getPreMaxEmi() {
        return preMaxEmi;
    }

    public void setPreMaxEmi(Long preMaxEmi) {
        this.preMaxEmi = preMaxEmi;
    }
	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public Long getMinScore() {
        return minScore;
    }

    public void setMinScore(Long minScore) {
        this.minScore = minScore;
    }

    public Long getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Long maxScore) {
        this.maxScore = maxScore;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getTelco() {
        return telco;
    }

    public void setTelco(String telco) {
        this.telco = telco;
    }

    public String getScoreRange() {
        return scoreRange;
    }

    public void setScoreRange(String scoreRange) {
        this.scoreRange = scoreRange;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUplDetailId() {
        return uplDetailId;
    }

    public void setUplDetailId(Long uplDetailId) {
        this.uplDetailId = uplDetailId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getIdentityNumberArmy() {
        return identityNumberArmy;
    }

    public void setIdentityNumberArmy(String identityNumberArmy) {
        this.identityNumberArmy = identityNumberArmy;
    }

    public String getPreProductName() {
        return preProductName;
    }

    public void setPreProductName(String preProductName) {
        this.preProductName = preProductName;
    }

    public String getPreProductCode() {
        return preProductCode;
    }

    public void setPreProductCode(String preProductCode) {
        this.preProductCode = preProductCode;
    }

    public String getAppProductName() {
        return appProductName;
    }

    public void setAppProductName(String appProductName) {
        this.appProductName = appProductName;
    }

    public String getAppProductCode() {
        return appProductCode;
    }

    public void setAppProductCode(String appProductCode) {
        this.appProductCode = appProductCode;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getLeadSource() {
        return leadSource;
    }

    public void setLeadSource(String leadSource) {
        this.leadSource = leadSource;
    }

	public String getUdf01() {
		return udf01;
	}

	public void setUdf01(String udf01) {
		this.udf01 = udf01;
	}

	public String getUdf02() {
		return udf02;
	}

	public void setUdf02(String udf02) {
		this.udf02 = udf02;
	}

	public String getUdf03() {
		return udf03;
	}

	public void setUdf03(String udf03) {
		this.udf03 = udf03;
	}

	public String getUdf04() {
		return udf04;
	}

	public void setUdf04(String udf04) {
		this.udf04 = udf04;
	}

	public String getUdf05() {
		return udf05;
	}

	public void setUdf05(String udf05) {
		this.udf05 = udf05;
	}

	public String getUdf06() {
		return udf06;
	}

	public void setUdf06(String udf06) {
		this.udf06 = udf06;
	}

	public String getUdf07() {
		return udf07;
	}

	public void setUdf07(String udf07) {
		this.udf07 = udf07;
	}

	public String getUdf08() {
		return udf08;
	}

	public void setUdf08(String udf08) {
		this.udf08 = udf08;
	}

	public String getUdf09() {
		return udf09;
	}

	public void setUdf09(String udf09) {
		this.udf09 = udf09;
	}

	public Long getAppNumber() {
		return appNumber;
	}

	public void setAppNumber(Long appNumber) {
		this.appNumber = appNumber;
	}

	public String getCampaignCode() {
		return campaignCode;
	}

	public void setCampaignCode(String campaignCode) {
		this.campaignCode = campaignCode;
	}
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
}
