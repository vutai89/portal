package com.mcredit.model.dto.telesales;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@XmlAccessorType(XmlAccessType.FIELD)
public class AllocationCustomerDTO {

	@SerializedName("uplMasterId")
	@Expose
	public Long uplMasterId;
	
	@SerializedName("allocatedNumber")
	@Expose
	public String allocatedNumber;
	
	@SerializedName("allocatedTo")
	@Expose
	public Long allocatedTo;
	
	@SerializedName("allocatedType")
	@Expose
	public String allocatedType;
	
	@SerializedName("relatedAllocation")
	@Expose
	public Long relatedAllocation;
	
	@SerializedName("objectType")
	@Expose
	public String objectType;
	
	@SerializedName("objectId")
	@Expose
	public List<Long> objectId;
	
	@SerializedName("isOwner")
	@Expose
	public boolean isOwner;
	
	//Phan bo cho team lead ---------------
	@SerializedName("isExceptTeam")
	@Expose
	public boolean isExceptTeam;
	
	@SerializedName("isSpecifyTeam")
	@Expose
	public boolean isSpecifyTeam;
	//-------------------------------------
	
	//Phan bo cho team member -------------
	@SerializedName("isExceptMembers")
	@Expose
	public boolean isExceptMembers;
	
	@SerializedName("isSpecifyMembers")
	@Expose
	public boolean isSpecifyMembers;
	//-------------------------------------
	
	public boolean isExceptTeam() {
		return isExceptTeam;
	}

	public boolean isExceptMembers() {
		return isExceptMembers;
	}

	public void setExceptMembers(boolean isExceptMembers) {
		this.isExceptMembers = isExceptMembers;
	}

	public boolean isSpecifyMembers() {
		return isSpecifyMembers;
	}

	public void setSpecifyMembers(boolean isSpecifyMembers) {
		this.isSpecifyMembers = isSpecifyMembers;
	}

	public void setExceptTeam(boolean isExceptTeam) {
		this.isExceptTeam = isExceptTeam;
	}

	public boolean isSpecifyTeam() {
		return isSpecifyTeam;
	}

	public void setSpecifyTeam(boolean isSpecifyTeam) {
		this.isSpecifyTeam = isSpecifyTeam;
	}

	public Long getUplMasterId() {
		return uplMasterId;
	}

	public void setUplMasterId(Long uplMasterId) {
		this.uplMasterId = uplMasterId;
	}

	public String getAllocatedNumber() {
		return allocatedNumber;
	}

	public void setAllocatedNumber(String allocatedNumber) {
		this.allocatedNumber = allocatedNumber;
	}

	public Long getAllocatedTo() {
		return allocatedTo;
	}

	public void setAllocatedTo(Long allocatedTo) {
		this.allocatedTo = allocatedTo;
	}

	public String getAllocatedType() {
		return allocatedType;
	}

	public void setAllocatedType(String allocatedType) {
		this.allocatedType = allocatedType;
	}

	public Long getRelatedAllocation() {
		return relatedAllocation;
	}

	public void setRelatedAllocation(Long relatedAllocation) {
		this.relatedAllocation = relatedAllocation;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public List<Long> getObjectId() {
		return objectId;
	}

	public void setObjectId(List<Long> objectId) {
		this.objectId = objectId;
	}

	public boolean isOwner() {
		return isOwner;
	}

	public void setOwner(boolean isOwner) {
		this.isOwner = isOwner;
	}

}