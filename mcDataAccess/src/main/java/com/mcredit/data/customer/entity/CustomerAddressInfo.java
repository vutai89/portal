package com.mcredit.data.customer.entity;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;


/**
 * The persistent class for the CUST_ADDR_INFO database table.
 * 
 */
@Entity
@Table(name="CUST_ADDR_INFO")
@NamedQuery(name="CustomerAddressInfo.nextAddrOrder",query = " select max(addrOrder) from CustomerAddressInfo " +
		 												     " where custId = :custId and addrType = :addrType ")
public class CustomerAddressInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "seq_cadi"
				    , strategy = "com.mcredit.data.common.entity.SequenceGenerate"
					, parameters = @Parameter(name = "seqName", value = "SEQ_CUST_ADDR_INFO_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_cadi")
	private Long id;
	
	@Column(name="CUST_ID")
	private Long custId;
	
	@Column(name="RECORD_STATUS")
	private String recordStatus;
	
	@Column(name="CREATED_BY", updatable=false)
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE", updatable=false)
	@CreationTimestamp
	private Date createdDate;
	
	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
	@UpdateTimestamp
	private Date lastUpdatedDate;
	
	@Column(name="ADDR_TYPE")
	private Integer addrType;

	@Column(name="ADDR_ORDER")
	private Integer addrOrder;

	@Column(name="ADDRESS")
	private String address;

	@Column(name="PROVINCE")
	private Integer province;
	
	@Column(name="DISTRICT")
	private Integer district;
	
	@Column(name="WARD")
	private Integer ward;

	public CustomerAddressInfo() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getCustId() {
		return custId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Integer getAddrType() {
		return addrType;
	}

	public void setAddrType(Integer addrType) {
		this.addrType = addrType;
	}

	public Integer getAddrOrder() {
		return addrOrder;
	}

	public void setAddrOrder(Integer addrOrder) {
		this.addrOrder = addrOrder;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getProvince() {
		return province;
	}

	public void setProvince(Integer province) {
		this.province = province;
	}

	public Integer getDistrict() {
		return district;
	}

	public void setDistrict(Integer district) {
		this.district = district;
	}

	public Integer getWard() {
		return ward;
	}

	public void setWard(Integer ward) {
		this.ward = ward;
	}
}