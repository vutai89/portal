package com.mcredit.data.common.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


/**
 * The persistent class for the CODE_TABLE database table.
 * 
 */
@Entity(name="CodeTable")
@Table(name="CODE_TABLE")
@NamedQuery(name="CodeTable.findAll", query="SELECT c FROM CodeTable c")
public class CodeTable implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
//  @Column(name="ID")
    @GenericGenerator(name = "seq_CodeTable", strategy = "com.mcredit.data.common.entity.SequenceGenerate", parameters = @Parameter(name = "seqName", value = "SEQ_CODE_TABLE_ID.NEXTVAL"))
    @GeneratedValue(generator = "seq_CodeTable")
    private Long id;

	@Column(name="CATEGORY")
	private String category;

	@Column(name="CODE_GROUP")
	private String codeGroup;

	@Column(name="CODE_MESSAGE_KEY")
	private String codeMessageKey;

	@Column(name="CODE_VALUE1")
	private String codeValue1;

	@Column(name="CODE_VALUE2")
	private String codeValue2;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE")
	private Date createdDate;

	@Column(name="DESCRIPTION1")
	private String description1;

	@Column(name="DESCRIPTION2")
	private String description2;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
	private Date lastUpdatedDate;

	@Column(name="PARENT_ID")
	private Integer parentId;

	@Column(name="PRODUCT_CAT_ID")
	private Integer productCatId;

	@Column(name="PRODUCT_GROUP_ID")
	private Integer productGroupId;

	@Column(name="PRODUCT_ID")
	private Integer productId;

	@Column(name="RECORD_STATUS")
	private String recordStatus;

	@Column(name="REFERENCE")
	private String reference;

	@Column(name="STATUS")
	private String status;

	@Transient
	private List<CodeTable> childLst;
	
	@Temporal(TemporalType.DATE)
	@Column(name="START_EFF_DATE", insertable=false)
	private Date startEffDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="END_EFF_DATE", insertable=false)
	private Date endEffDate;
	
	public CodeTable(String codeGroup, String category, String codeValue1, String description1) {
		this.category = category;
		this.codeGroup = codeGroup;
		this.description1 = description1;
		this.codeValue1 = codeValue1;
		this.lastUpdatedDate = new Date();
	}
	
	public Date getStartEffDate() {
		return startEffDate;
	}

	public void setStartEffDate(Date startEffDate) {
		this.startEffDate = startEffDate;
	}

	public Date getEndEffDate() {
		return endEffDate;
	}

	public void setEndEffDate(Date endEffDate) {
		this.endEffDate = endEffDate;
	}

	public List<CodeTable> getChildLst() {
		return childLst;
	}

	public void setChildLst(List<CodeTable> childLst) {
		this.childLst = childLst;
	}

	public CodeTable() {
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCodeGroup() {
		return this.codeGroup;
	}

	public void setCodeGroup(String codeGroup) {
		this.codeGroup = codeGroup;
	}

	public String getCodeMessageKey() {
		return this.codeMessageKey;
	}

	public void setCodeMessageKey(String codeMessageKey) {
		this.codeMessageKey = codeMessageKey;
	}

	public String getCodeValue1() {
		return this.codeValue1;
	}

	public void setCodeValue1(String codeValue1) {
		this.codeValue1 = codeValue1;
	}

	public String getCodeValue2() {
		return this.codeValue2;
	}

	public void setCodeValue2(String codeValue2) {
		this.codeValue2 = codeValue2;
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

	public String getDescription1() {
		return this.description1;
	}

	public void setDescription1(String description1) {
		this.description1 = description1;
	}

	public String getDescription2() {
		return this.description2;
	}

	public void setDescription2(String description2) {
		this.description2 = description2;
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

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getProductCatId() {
		return this.productCatId;
	}

	public void setProductCatId(Integer productCatId) {
		this.productCatId = productCatId;
	}

	public Integer getProductGroupId() {
		return this.productGroupId;
	}

	public void setProductGroupId(Integer productGroupId) {
		this.productGroupId = productGroupId;
	}

	public Integer getProductId() {
		return this.productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getRecordStatus() {
		return this.recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getReference() {
		return this.reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}