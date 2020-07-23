package com.mcredit.data.product.entity;

import java.io.Serializable;
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


/**
 * The persistent class for the PRODUCTS database table.
 * 
 */
@Entity(name="MappingHierarchy")
@Table(name="MAPPING_HIERARCHY")
public class MappingHierarchy implements Serializable {
	private static final long serialVersionUID = 1L;
	public MappingHierarchy(){};
	public MappingHierarchy(Long mapId1, Long mapId2, Long parentId, String mapType) {
		this.mapId1 = mapId1;
		this.mapId2 = mapId2;
		this.parentId = parentId;
		this.mapType = mapType;
		this.lastUpdatedDate =  new Date();
		this.status = "A";
	}
	
	@Id
	@GenericGenerator(name = "seq_MAPPING_HIERARCHY" , strategy = "com.mcredit.data.common.entity.SequenceGenerate" , parameters = @Parameter(name = "seqName", value = "MAPPING_HIERARCHY_SEQ.NEXTVAL"))
	@GeneratedValue(generator = "seq_MAPPING_HIERARCHY")
	private Long id;

	@Column(name="MAP_ID1")
	private Long mapId1;

	@Column(name="MAP_ID2")
	private Long mapId2;
	
	@Column(name="PARENT_ID")
	private Long parentId;
	
	@Column(name="MAP_TYPE")
	private String mapType;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="RECORD_STATUS")
	private String recordStatus;
	
	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE")
	private Date createdDate;

	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
	private Date lastUpdatedDate;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getMapId1() {
		return mapId1;
	}


	public void setMapId1(Long mapId1) {
		this.mapId1 = mapId1;
	}


	public Long getMapId2() {
		return mapId2;
	}


	public void setMapId2(Long mapId2) {
		this.mapId2 = mapId2;
	}


	public Long getParentId() {
		return parentId;
	}


	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}


	public String getMapType() {
		return mapType;
	}


	public void setMapType(String mapType) {
		this.mapType = mapType;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
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


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}


	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}


	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}


	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}