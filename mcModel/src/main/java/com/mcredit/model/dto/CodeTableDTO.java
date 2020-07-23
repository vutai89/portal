package com.mcredit.model.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CodeTableDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String category;
	private String codeGroup;
	private String codeMessageKey;
	private String codeValue1;
	private String codeValue2;
	private String createdBy;
	private Date createdDate;
	private String description1;
	private String description2;
	private String lastUpdatedBy;
	private Date lastUpdatedDate;
	private Integer parentId;
	private Integer productId;
	private Integer productCatId;
	private Integer productGroupId;
	private String recordStatus;
	private String reference;
	private String status;
	private List<CodeTableDTO> childLst;
	private Date startEffDate;
	private Date endEffDate;

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

	public List<CodeTableDTO> getChildLst() {
		return childLst;
	}

	public void setChildLst(List<CodeTableDTO> childLst) {
		this.childLst = childLst;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCodeGroup() {
		return codeGroup;
	}

	public void setCodeGroup(String codeGroup) {
		this.codeGroup = codeGroup;
	}

	public String getCodeMessageKey() {
		return codeMessageKey;
	}

	public void setCodeMessageKey(String codeMessageKey) {
		this.codeMessageKey = codeMessageKey;
	}

	public String getCodeValue1() {
		return codeValue1;
	}

	public void setCodeValue1(String codeValue1) {
		this.codeValue1 = codeValue1;
	}

	public String getCodeValue2() {
		return codeValue2;
	}

	public void setCodeValue2(String codeValue2) {
		this.codeValue2 = codeValue2;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getDescription1() {
		return description1;
	}

	public void setDescription1(String description1) {
		this.description1 = description1;
	}

	public String getDescription2() {
		return description2;
	}

	public void setDescription2(String description2) {
		this.description2 = description2;
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

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}	
	
	public Integer getProductCatId() {
		return productCatId;
	}

	public void setProductCatId(Integer productCatId) {
		this.productCatId = productCatId;
	}

	public Integer getProductGroupId() {
		return productGroupId;
	}

	public void setProductGroupId(Integer productGroupId) {
		this.productGroupId = productGroupId;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
	
}