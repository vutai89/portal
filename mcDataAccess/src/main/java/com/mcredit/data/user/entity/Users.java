package com.mcredit.data.user.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * The persistent class for the USERS database table.
 * 
 */
@Entity
@Table(name = "USERS")
@NamedQuery(name = "User.findAll", query = "SELECT u FROM Users u")
public class Users implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "seq_users", strategy = "com.mcredit.data.common.entity.SequenceGenerate", parameters = @Parameter(name = "seqName", value = "SEQ_USERS_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_users")
	private Long id;

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@CreationTimestamp
	@Column(name = "CREATED_DATE", updatable = false)
	private Date createdDate;

	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_UPDATED_DATE")
	private Date lastUpdatedDate;

	@Column(name = "LOGIN_ID")
	private String loginId;

	/*
	 * @Column(name = "MANAGER_ID") private Integer managerId;
	 */

	@Column(name = "EMP_ID")
	private String empId;

	@Column(name = "USR_FULL_NAME")
	private String usrFullName;

	@Column(name = "USR_TYPE")
	private String usrType;

	/*
	 * @Column(name = "USR_GROUP") private String usrGroup;
	 * 
	 * @Column(name = "SALE_CODE") private String saleCode;
	 * 
	 * @Column(name = "MOBILE") private String mobile;
	 * 
	 * @Column(name = "SALE_NETWORK_ID") private BigDecimal saleNetworkId;
	 */

	@Column(name = "STATUS")
	private String status;

	@Column(name = "START_EFF_DATE")
	private Date startEffDate;

	@Column(name = "END_EFF_DATE")
	private Date endEffDate;

	@Column(name = "RECORD_STATUS")
	private String recordStatus;
	
	@Transient
	private Long allocatedFreshNumber;

	@Transient
	private Long allocatedWipNumber;

	@Transient
	private Long allocationMasterId;

	@Transient
	private String superVisor;

	/*@Transient
	private String asm;*/
	
	@Transient
	private Long bdsId;

	@Transient
	private String bdsCode;

	@Transient
	private String teamLead;

	@Transient
	private String empCode;
	
	@Transient
	private String email;
	
	@Transient
	private String departmentName;
	
	@Transient
	private Date userSessionCreatedTime;
	
	@Transient
	private String extNumber;
	
	@Transient
	private String deviceName;
	
	@Transient
	private Long allocatedNumber;
	
	public Users() {
	}
	
	public Long getAllocatedNumber() {
		return allocatedNumber;
	}

	public void setAllocatedNumber(Long allocatedNumber) {
		this.allocatedNumber = allocatedNumber;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getExtNumber() {
		return extNumber;
	}

	public void setExtNumber(String extNumber) {
		this.extNumber = extNumber;
	}

	public Date getUserSessionCreatedTime() {
		return userSessionCreatedTime;
	}

	public void setUserSessionCreatedTime(Date userSessionCreatedTime) {
		this.userSessionCreatedTime = userSessionCreatedTime;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBdsCode() {
		return bdsCode;
	}

	public void setBdsCode(String bdsCode) {
		this.bdsCode = bdsCode;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public Long getBdsId() {
		return bdsId;
	}

	public void setBdsId(Long bdsId) {
		this.bdsId = bdsId;
	}

	public String getTeamLead() {
		return teamLead;
	}

	public void setTeamLead(String teamLead) {
		this.teamLead = teamLead;
	}

	public String getSuperVisor() {
		return superVisor;
	}

	public void setSuperVisor(String superVisor) {
		this.superVisor = superVisor;
	}

	public Long getAllocatedFreshNumber() {
		return allocatedFreshNumber;
	}

	public void setAllocatedFreshNumber(Long allocatedFreshNumber) {
		this.allocatedFreshNumber = allocatedFreshNumber;
	}

	public Long getAllocatedWipNumber() {
		return allocatedWipNumber;
	}

	public void setAllocatedWipNumber(Long allocatedWipNumber) {
		this.allocatedWipNumber = allocatedWipNumber;
	}

	public Long getAllocationMasterId() {
		return allocationMasterId;
	}

	public void setAllocationMasterId(Long allocationMasterId) {
		this.allocationMasterId = allocationMasterId;
	}

	public Long getId() {
		return id;
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

	public Date getEndEffDate() {
		return this.endEffDate;
	}

	public void setEndEffDate(Date endEffDate) {
		this.endEffDate = endEffDate;
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

	public String getLoginId() {
		return this.loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getRecordStatus() {
		return this.recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Date getStartEffDate() {
		return this.startEffDate;
	}

	public void setStartEffDate(Date startEffDate) {
		this.startEffDate = startEffDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUsrFullName() {
		return this.usrFullName;
	}

	public void setUsrFullName(String usrFullName) {
		this.usrFullName = usrFullName;
	}

	public String getUsrType() {
		return this.usrType;
	}

	public void setUsrType(String usrType) {
		this.usrType = usrType;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}