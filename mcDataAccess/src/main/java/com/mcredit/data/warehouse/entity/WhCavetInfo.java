package com.mcredit.data.warehouse.entity;

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

@Entity
@Table(name="WH_CAVET_INFO")
public class WhCavetInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6621985890166777721L;

	
	@Id
	@GenericGenerator(name = "seq_WhCavetInfo" , strategy = "com.mcredit.data.common.entity.SequenceGenerate" , parameters = @Parameter(name = "seqName", value = "SEQ_WH_CAVET_INFO_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_WhCavetInfo") 
	private Long id;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
	private Date lastUpdatedDate;
	
	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="WH_DOC_ID")
	private Long whDocId;
	
	@Column(name="BRAND")
	private String brand;
	
	@Column(name="MODEL_CODE")
	private String modelCode;
	
	@Column(name="COLOR")
	private String color;
	
	@Column(name="ENGINE")
	private String engine;
	
	@Column(name="CHASSIS")
	private String chassis;
	
	@Column(name="N_PLATE")
	private String nPlate;
	
	@Column(name="CAVET_NUMBER")
	private String cavetNumber;
	
	@Column(name="TYPE")
	private Long type;
	
	@Column(name="VERSION")
	private Long version;

	public WhCavetInfo() {
	}

	public WhCavetInfo(Long id, Long whDocId, String brand, String modelCode, String nPlate, String cavetNumber, Long type, String color, String engine, String chassis) {
		this.id = id;
		this.whDocId = whDocId;
		this.brand = brand;
		this.modelCode = modelCode;
		this.nPlate = nPlate;
		this.cavetNumber = cavetNumber;
		this.type = type;
		this.color = color;
		this.engine = engine;
		this.chassis = chassis;
	}

    public WhCavetInfo(String createdBy, Date lastUpdatedDate, String lastUpdatedBy, Long whDocId, String brand, String modelCode, String color, String engine, String chassis, String nPlate, String cavetNumber, Long type) {
        this.createdBy = createdBy;
        this.lastUpdatedDate = lastUpdatedDate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.whDocId = whDocId;
        this.brand = brand;
        this.modelCode = modelCode;
        this.color = color;
        this.engine = engine;
        this.chassis = chassis;
        this.nPlate = nPlate;
        this.cavetNumber = cavetNumber;
        this.type = type;
    }
        

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getWhDocId() {
		return whDocId;
	}

	public void setWhDocId(Long whDocId) {
		this.whDocId = whDocId;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getnPlate() {
		return nPlate;
	}

	public void setnPlate(String nPlate) {
		this.nPlate = nPlate;
	}

	public String getCavetNumber() {
		return cavetNumber;
	}

	public void setCavetNumber(String cavetNumber) {
		this.cavetNumber = cavetNumber;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getEngine() {
		return engine;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}

	public String getChassis() {
		return chassis;
	}

	public void setChassis(String chassis) {
		this.chassis = chassis;
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
}