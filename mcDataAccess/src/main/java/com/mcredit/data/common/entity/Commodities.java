package com.mcredit.data.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the COMMODITIES database table.
 * 
 */
@Entity
@NamedQuery(name="Commodities.findAll", query="SELECT c FROM Commodities c")
public class Commodities implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="COMMODITIES_ID_GENERATOR", sequenceName="SEQ_COMMODITIES_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COMMODITIES_ID_GENERATOR")
	private Long id;

	@Column(name="BRAND_ID")
	private BigDecimal brandId;

	@Column(name="COMM_CATEGORY_ID")
	private BigDecimal commCategoryId;

	@Column(name="COMM_NAME")
	private String commName;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE")
	private Date createdDate;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
	private Date lastUpdatedDate;

	@Column(name="RECORD_STATUS")
	private String recordStatus;

	private String status;

	public Commodities() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getBrandId() {
		return this.brandId;
	}

	public void setBrandId(BigDecimal brandId) {
		this.brandId = brandId;
	}

	public BigDecimal getCommCategoryId() {
		return this.commCategoryId;
	}

	public void setCommCategoryId(BigDecimal commCategoryId) {
		this.commCategoryId = commCategoryId;
	}

	public String getCommName() {
		return this.commName;
	}

	public void setCommName(String commName) {
		this.commName = commName;
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

	public String getRecordStatus() {
		return this.recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}