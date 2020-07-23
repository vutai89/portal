package com.mcredit.model.object;

import java.io.Serializable;
import java.util.Date;


public class DataReport implements Serializable{
	  

//	@Id
//	@GenericGenerator(name = "seq_DataReport" , strategy = "com.mcredit.data.common.entity.SequenceGenerate" , parameters = @Parameter(name = "seqName", value = "SEQ_UPL_SLN_DATA_REPORT_ID.NEXTVAL"))
//	@GeneratedValue(generator = "seq_DataReport") 
//    private Long id;
//	
	
	private Long numOfUserRegistered;
	
	private Long numOfUserUsed;
	
	private Long numOfUserInputDLY;
	
	private Long numOfCaseInputDLY;
	
	private Long numOfCaseDisurDLY;
	
	private Long numOfMobiCaseLTD;
	
	private Long disburAmountDLY;
	
	private Long disburAmountLTD;
	
	private Date updatedDate;
	
	private String createdBy;
	
	private String businessDate;

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

	public String getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(String businessDate) {
		this.businessDate = businessDate;
	}

	
	
}
