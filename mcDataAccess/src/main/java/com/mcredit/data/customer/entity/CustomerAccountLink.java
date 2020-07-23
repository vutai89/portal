package com.mcredit.data.customer.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.UpdateTimestamp;

@Entity(name="CustomerAccountLink")
@Table(name="CUST_ACCOUNT_LINK", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
@NamedQuery(name="CustomerAccountLink.nextLinkSeq",query="select max(linkSeq) from CustomerAccountLink where custId = :custId and linkType = :linkType")
public class CustomerAccountLink implements Serializable {

	private static final long serialVersionUID = -9098368116078679901L;

	@Id
	@GenericGenerator(name = "seq_cal"
				    , strategy = "com.mcredit.data.common.entity.SequenceGenerate"
					, parameters = @Parameter(name = "seqName", value = "SEQ_CUST_ACCOUNT_LINK_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_cal")
	@Column(name = "ID", unique = true)
	
	private Long id;
	
	@Column(name="cust_id", nullable=true)
	private Long custId;

	@Column(name="link_type", nullable=true, length=1)
	private String linkType;

	@Column(name="link_seq", nullable=true)
	private Integer linkSeq;

	@Column(name="record_status", nullable=true, length=1)
	private String recordStatus;

	@CreationTimestamp
	@Column(columnDefinition="Date", name="created_date", nullable=true, updatable=false)
	private Date createdDate;

	@UpdateTimestamp
	@Column(columnDefinition="Date", name="last_updated_date", nullable=true)
	private Date lastUpdatedDate;

	@Column(name="created_by", updatable=false, nullable=true, length=30)
	private String createdBy;

	@Column(name="last_updated_by", nullable=true, length=30)
	private String lastUpdatedBy;

	@Column(name="link_value", nullable=false, length=30)
	private String linkValue;

	@Column(name="link_name", nullable=true, length=100)
	private String linkName;

	@Column(name="link_system", nullable=false, length=1)
	private String linkSystem;

	@Column(name="link_currency", nullable=true, length=3)
	private String linkCurrency;

	@Column(name="link_product", nullable=true, length=10)
	private String linkProduct;
	
	/*@Column(name="partner_id", nullable=false)
	private Integer partnerId;*/
	
	@Transient
	private String custName;

	public CustomerAccountLink() {}
	
	public CustomerAccountLink(Long custId, String linkType, String recordStatus, String createdBy, String linkValue, String linkName, String linkSystem, String linkCurrency, String linkProduct) {
		this.custId = custId;
		this.linkType = linkType;
		this.recordStatus = recordStatus;
		this.createdBy = createdBy;
		this.linkValue = linkValue;
		this.linkName = linkName;
		this.linkSystem = linkSystem;
		this.linkCurrency = linkCurrency;
		this.linkProduct = linkProduct;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCustId() {
		return custId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType!=null?linkType.trim():null;
	}

	public Integer getLinkSeq() {
		return linkSeq;
	}

	public void setLinkSeq(Integer linkSeq) {
		this.linkSeq = linkSeq;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus!=null?recordStatus.trim():null;
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
		this.createdBy = createdBy!=null?createdBy.trim():null;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy!=null?lastUpdatedBy.trim():null;
	}

	public String getLinkValue() {
		return linkValue;
	}

	public void setLinkValue(String linkValue) {
		this.linkValue = linkValue!=null?linkValue.trim():null;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName!=null?linkName.trim():null;
	}

	public String getLinkSystem() {
		return linkSystem;
	}

	public void setLinkSystem(String linkSystem) {
		this.linkSystem = linkSystem!=null?linkSystem.trim():null;
	}

	public String getLinkCurrency() {
		return linkCurrency;
	}

	public void setLinkCurrency(String linkCurrency) {
		this.linkCurrency = linkCurrency!=null?linkCurrency.trim():null;
	}

	public String getLinkProduct() {
		return linkProduct;
	}

	public void setLinkProduct(String linkProduct) {
		this.linkProduct = linkProduct!=null?linkProduct.trim():null;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName!=null?custName.trim():null;
	}

	/*public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}*/
}
