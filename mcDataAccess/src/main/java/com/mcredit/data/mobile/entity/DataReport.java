package com.mcredit.data.mobile.entity;

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
@Table(name="UPL_SLM_DATA_REPORT")
public class DataReport implements Serializable{
	  
	  private static final long serialVersionUID = 6621985890166777721L;

//	@Id
//	@GenericGenerator(name = "seq_DataReport" , strategy = "com.mcredit.data.common.entity.SequenceGenerate" , parameters = @Parameter(name = "seqName", value = "SEQ_UPL_SLN_DATA_REPORT_ID.NEXTVAL"))
//	@GeneratedValue(generator = "seq_DataReport") 
//    private Long id;
//	
	
	@Column(name="NUM_OF_USER_REGISTERED")
	private Long numOfUserRegistered;
	
	@Column(name="NUM_OF_USER_USED")
	private Long numOfUserUsed;
	
	@Column(name="NUM_OF_USER_INPUT_DLY")
	private Long numOfUserInputDLY;
	
	@Column(name="NUM_OF_CASE_INPUT_DLY")
	private Long numOfCaseInputDLY;
	
	@Column(name="NUM_OF_CASE_DISBUR_DLY")
	private Long numOfCaseDisurDLY;
	
	@Column(name="NUM_OF_MOBI_CASE_LTD")
	private Long numOfMobiCaseLTD;
	
	@Column(name="DISBUR_AMOUNT_DLY")
	private Long disburAmountDLY;
	
	@Column(name="DISBUR_AMOUNT_LTD")
	private Long disburAmountLTD;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DATE")
	private Date updatedDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Id
	@Temporal(TemporalType.DATE)
	@Column(name="BUSINESS_DATE")
	private Date businessDate;

	public Long getNumOfUserRegistered() {
		return numOfUserRegistered;
	}

	public void setNumOfUserRegistered(Long numOfUserRegistered) {
		this.numOfUserRegistered = numOfUserRegistered;
	}

	public Long getNumOfUserUsed() {
		return numOfUserUsed;
	}

	public void setNumOfUserUsed(Long numOfUserUsed) {
		this.numOfUserUsed = numOfUserUsed;
	}

	public Long getNumOfUserInputDLY() {
		return numOfUserInputDLY;
	}

	public void setNumOfUserInputDLY(Long numOfUserInputDLY) {
		this.numOfUserInputDLY = numOfUserInputDLY;
	}

	public Long getNumOfCaseInputDLY() {
		return numOfCaseInputDLY;
	}

	public void setNumOfCaseInputDLY(Long numOfCaseInputDLY) {
		this.numOfCaseInputDLY = numOfCaseInputDLY;
	}

	public Long getNumOfCaseDisurDLY() {
		return numOfCaseDisurDLY;
	}

	public void setNumOfCaseDisurDLY(Long numOfCaseDisurDLY) {
		this.numOfCaseDisurDLY = numOfCaseDisurDLY;
	}

	public Long getNumOfMobiCaseLTD() {
		return numOfMobiCaseLTD;
	}

	public void setNumOfMobiCaseLTD(Long numOfMobiCaseLTD) {
		this.numOfMobiCaseLTD = numOfMobiCaseLTD;
	}

	public Long getDisburAmountDLY() {
		return disburAmountDLY;
	}

	public void setDisburAmountDLY(Long disburAmountDLY) {
		this.disburAmountDLY = disburAmountDLY;
	}

	public Long getDisburAmountLTD() {
		return disburAmountLTD;
	}

	public void setDisburAmountLTD(Long disburAmountLTD) {
		this.disburAmountLTD = disburAmountLTD;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(Date businessDate) {
		this.businessDate = businessDate;
	}

	
	
}
