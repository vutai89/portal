package com.mcredit.data.customer.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.UpdateTimestamp;



/**
 * The persistent class for the CUST_COMPANY_INFO database table.
 * 
 */
@Entity
@Table(name="CUST_COMPANY_INFO")
public class CustomerCompanyInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "seq_cpn_i"
				    , strategy = "com.mcredit.data.common.entity.SequenceGenerate"
					, parameters = @Parameter(name = "seqName", value = "SEQ_CUST_COMPANY_INFO_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_cpn_i")
	private Long id;
	
	@Column(name="RECORD_STATUS")
	private String recordStatus;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE", updatable=false)
	@CreationTimestamp
	private Date createdDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
    @UpdateTimestamp
	private Date lastUpdatedDate;
	
	@Column(name="CREATED_BY", updatable=false)
	private String createdBy;
	
	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;
	
	@Column(name="COMP_ADDR_STREET")
	private String compAddrStreet;
	
	@Column(name="COMP_ADDR_PROVINCE")
	private Integer compAddrProvince;
	
	@Column(name="COMP_ADDR_DISTRICT")
	private Integer compAddrDistrict;

	@Column(name="COMP_ADDR_WARD")
	private Integer compAddrWard;

	@Column(name="COMP_NAME")
	private String compName;

	@Column(name="COMP_TAX_NUMBER")
	private String compTaxNumber;

	@Column(name="OFFICE_PHONE_NUMBER")
	private String officePhoneNumber;
	
	@Column(name = "ESTABLISH_DATE")
	private Date establishDate;
	
	@Column(name = "OPERATION_MONTH")
	private Integer operationMonth;

	@Column(name="CAT_TYPE")
	private Integer catType;
	
	@Column(name="COMP_TYPE")
	private Integer compType;
	
	@Column(name = "IS_TOP_500_1000_COM")
	private String isTopComp;
	
	@Column(name = "IS_TOP_500_1000_BRANCH")
	private String isTopBranch;
	
	@Column(name = "TOP_500_1000")
	private String top5001000;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "CIC_CONSULTING_DATE")
	private Date cicConsultingDate;
	
	@Column(name= "PROCESSING")
	private String processing;
	
	@Column(name= "ECONOMIC_TYPE")
	private Integer economicType;
	
	@Column(name = "IS_MULTINATIONAL_COMPANY")
	private String isMultiComp;
	
	@Column(name= "CIC_CODE")
	private String cicCode;
	
	@Column(name= "CIC_INFO")
	private String cicInfo;
	
	@Column(name= "TAX_CHAPTER")
	private String taxChapter;
	
	@Transient
	private String custName;

	public CustomerCompanyInfo() {
	}
	
	public CustomerCompanyInfo(Long id, String compName, String compTaxNumber, String compAddrStreet, String officePhoneNumber, String loginId, String processing) {
		this.id = id;
		this.compName = compName;
		this.compTaxNumber = compTaxNumber;
		this.compAddrStreet = compAddrStreet;
		this.officePhoneNumber  = officePhoneNumber;
		this.createdBy = loginId;
		this.processing = processing;
	}

	public String getTop5001000() {
		return top5001000;
	}

	public void setTop5001000(String top5001000) {
		this.top5001000 = top5001000;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCompAddrDistrict() {
		return this.compAddrDistrict;
	}

	public void setCompAddrDistrict(Integer compAddrDistrict) {
		this.compAddrDistrict = compAddrDistrict;
	}

	public Integer getCompAddrProvince() {
		return this.compAddrProvince;
	}

	public void setCompAddrProvince(Integer compAddrProvince) {
		this.compAddrProvince = compAddrProvince;
	}

	public String getCompAddrStreet() {
		return this.compAddrStreet;
	}

	public void setCompAddrStreet(String compAddrStreet) {
		this.compAddrStreet = compAddrStreet!=null?compAddrStreet.trim():null;
	}

	public Integer getCompAddrWard() {
		return this.compAddrWard;
	}

	public void setCompAddrWard(Integer compAddrWard) {
		this.compAddrWard = compAddrWard;
	}

	public String getCompName() {
		return this.compName;
	}

	public void setCompName(String compName) {
		this.compName = compName!=null?compName.trim():null;
	}

	public String getCompTaxNumber() {
		return this.compTaxNumber;
	}

	public void setCompTaxNumber(String compTaxNumber) {
		this.compTaxNumber = compTaxNumber!=null?compTaxNumber.trim():null;
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

	public String getOfficePhoneNumber() {
		return this.officePhoneNumber;
	}

	public void setOfficePhoneNumber(String officePhoneNumber) {
		this.officePhoneNumber = officePhoneNumber!=null?officePhoneNumber.trim():null;
	}

	public String getRecordStatus() {
		return this.recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus!=null?recordStatus.trim():null;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName!=null?custName.trim():null;
	}

	public Date getEstablishDate() {
		return establishDate;
	}

	public void setEstablishDate(Date establishDate) {
		this.establishDate = establishDate;
	}

	public Integer getCatType() {
		return catType;
	}

	public void setCatType(Integer catType) {
		this.catType = catType;
	}

	public Integer getCompType() {
		return compType;
	}

	public void setCompType(Integer compType) {
		this.compType = compType;
	}

	public String getIsTopComp() {
		return isTopComp;
	}

	public void setIsTopComp(String isTopComp) {
		this.isTopComp = isTopComp;
	}

	public String getIsTopBranch() {
		return isTopBranch;
	}

	public void setIsTopBranch(String isTopBranch) {
		this.isTopBranch = isTopBranch;
	}

	public String getIsMultiComp() {
		return isMultiComp;
	}

	public void setIsMultiComp(String isMultiComp) {
		this.isMultiComp = isMultiComp;
	}

	public Date getCicConsultingDate() {
		return cicConsultingDate;
	}

	public void setCicConsultingDate(Date cicConsultingDate) {
		this.cicConsultingDate = cicConsultingDate;
	}

	public Integer getOperationMonth() {
		return operationMonth;
	}

	public void setOperationMonth(Integer operationMonth) {
		this.operationMonth = operationMonth;
	}

	public String getCicInfo() {
		return cicInfo;
	}

	public void setCicInfo(String cicInfo) {
		this.cicInfo = cicInfo;
	}

	public String getProcessing() {
		return processing;
	}

	public void setProcessing(String processing) {
		this.processing = processing;
	}

	public Integer getEconomicType() {
		return economicType;
	}

	public void setEconomicType(Integer economicType) {
		this.economicType = economicType;
	}

	public String getCicCode() {
		return cicCode;
	}

	public void setCicCode(String cicCode) {
		this.cicCode = cicCode;
	}

	public String getTaxChapter() {
		return taxChapter;
	}

	public void setTaxChapter(String taxChapter) {
		this.taxChapter = taxChapter;
	}
	
	

}