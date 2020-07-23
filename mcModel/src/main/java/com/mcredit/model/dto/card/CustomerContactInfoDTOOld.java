package com.mcredit.model.dto.card;

import java.io.Serializable;
import java.util.Date;

public class CustomerContactInfoDTOOld implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String createdBy;
	private Date createdDate;
	private Integer currentAddrSpouse;
	private String currentAddrSpouseValue;
	private Long custId;
	private String email;
	private String lastUpdatedBy;
	private Date lastUpdatedDate;
	private String mobile;
	private String permanentAddr;
	private Integer permanentDistrict;
	private String permanentDistrictValue;
	private Integer permanentProvince;
	private String permanentProvinceValue;
	private Integer permanentWard;
	private String permanentWardValue;
	private String recordStatus;
	private String spouseAddr;
	private Integer spouseDistrict;
	private String spouseDistrictValue;
	private Integer spouseProvince;
	private String spouseProvinceValue;
	private Integer spouseWard;
	private String spouseWardValue;
	private String tempAddr;
	private Integer tempDistrict;
	private String tempDistrictValue;
	private Integer tempProvince;
	private String tempProvinceValue;
	private Integer tempWard;
	private String tempWardValue;

	public CustomerContactInfoDTOOld() {
	}

	public String getCurrentAddrSpouseValue() {
		return currentAddrSpouseValue;
	}

	public void setCurrentAddrSpouseValue(String currentAddrSpouseValue) {
		this.currentAddrSpouseValue = currentAddrSpouseValue;
	}

	public String getPermanentDistrictValue() {
		return permanentDistrictValue;
	}

	public void setPermanentDistrictValue(String permanentDistrictValue) {
		this.permanentDistrictValue = permanentDistrictValue;
	}

	public String getPermanentProvinceValue() {
		return permanentProvinceValue;
	}

	public void setPermanentProvinceValue(String permanentProvinceValue) {
		this.permanentProvinceValue = permanentProvinceValue;
	}

	public String getPermanentWardValue() {
		return permanentWardValue;
	}

	public void setPermanentWardValue(String permanentWardValue) {
		this.permanentWardValue = permanentWardValue;
	}

	public String getSpouseDistrictValue() {
		return spouseDistrictValue;
	}

	public void setSpouseDistrictValue(String spouseDistrictValue) {
		this.spouseDistrictValue = spouseDistrictValue;
	}

	public String getSpouseProvinceValue() {
		return spouseProvinceValue;
	}

	public void setSpouseProvinceValue(String spouseProvinceValue) {
		this.spouseProvinceValue = spouseProvinceValue;
	}

	public String getSpouseWardValue() {
		return spouseWardValue;
	}

	public void setSpouseWardValue(String spouseWardValue) {
		this.spouseWardValue = spouseWardValue;
	}

	public String getTempDistrictValue() {
		return tempDistrictValue;
	}

	public void setTempDistrictValue(String tempDistrictValue) {
		this.tempDistrictValue = tempDistrictValue;
	}

	public String getTempProvinceValue() {
		return tempProvinceValue;
	}

	public void setTempProvinceValue(String tempProvinceValue) {
		this.tempProvinceValue = tempProvinceValue;
	}

	public String getTempWardValue() {
		return tempWardValue;
	}

	public void setTempWardValue(String tempWardValue) {
		this.tempWardValue = tempWardValue;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getCurrentAddrSpouse() {
		return this.currentAddrSpouse;
	}

	public void setCurrentAddrSpouse(Integer currentAddrSpouse) {
		this.currentAddrSpouse = currentAddrSpouse;
	}

	public Long getCustId() {
		return this.custId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPermanentAddr() {
		return this.permanentAddr;
	}

	public void setPermanentAddr(String permanentAddr) {
		this.permanentAddr = permanentAddr;
	}

	public Integer getPermanentDistrict() {
		return this.permanentDistrict;
	}

	public void setPermanentDistrict(Integer permanentDistrict) {
		this.permanentDistrict = permanentDistrict;
	}

	public Integer getPermanentProvince() {
		return this.permanentProvince;
	}

	public void setPermanentProvince(Integer permanentProvince) {
		this.permanentProvince = permanentProvince;
	}

	public Integer getPermanentWard() {
		return this.permanentWard;
	}

	public void setPermanentWard(Integer permanentWard) {
		this.permanentWard = permanentWard;
	}

	public String getRecordStatus() {
		return this.recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getSpouseAddr() {
		return this.spouseAddr;
	}

	public void setSpouseAddr(String spouseAddr) {
		this.spouseAddr = spouseAddr;
	}

	public Integer getSpouseDistrict() {
		return this.spouseDistrict;
	}

	public void setSpouseDistrict(Integer spouseDistrict) {
		this.spouseDistrict = spouseDistrict;
	}

	public Integer getSpouseProvince() {
		return this.spouseProvince;
	}

	public void setSpouseProvince(Integer spouseProvince) {
		this.spouseProvince = spouseProvince;
	}

	public Integer getSpouseWard() {
		return this.spouseWard;
	}

	public void setSpouseWard(Integer spouseWard) {
		this.spouseWard = spouseWard;
	}

	public String getTempAddr() {
		return this.tempAddr;
	}

	public void setTempAddr(String tempAddr) {
		this.tempAddr = tempAddr;
	}

	public Integer getTempDistrict() {
		return this.tempDistrict;
	}

	public void setTempDistrict(Integer tempDistrict) {
		this.tempDistrict = tempDistrict;
	}

	public Integer getTempProvince() {
		return this.tempProvince;
	}

	public void setTempProvince(Integer tempProvince) {
		this.tempProvince = tempProvince;
	}

	public Integer getTempWard() {
		return this.tempWard;
	}

	public void setTempWard(Integer tempWard) {
		this.tempWard = tempWard;
	}

}