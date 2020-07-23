package com.mcredit.data.warehouse.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name="WH_CODE")
public class WhCode implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6621985890166777721L;

	@Id
	@GenericGenerator(name = "seq_WhCode" , strategy = "com.mcredit.data.common.entity.SequenceGenerate" , parameters = @Parameter(name = "seqName", value = "SEQ_WH_CODE_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_WhCode") 
	private Long id;
	
	@Column(name="STATUS")
	private Long status;
	
	@Column(name="CODE")
	private String code;
	
	@Column(name="DOC_TYPE")
	private Long docType;
	
	@Column(name="MATERIAL")
	private Long material;

	public WhCode() {
	}
	
	public WhCode(Long status, String code, Long docType, Long material) {
		this.status = status;
		this.code = code;
		this.docType = docType;
		this.material = material;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getDocType() {
		return docType;
	}

	public void setDocType(Long docType) {
		this.docType = docType;
	}

	public Long getMaterial() {
		return material;
	}

	public void setMaterial(Long material) {
		this.material = material;
	}
}