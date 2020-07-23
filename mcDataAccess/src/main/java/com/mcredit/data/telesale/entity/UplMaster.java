package com.mcredit.data.telesale.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * The persistent class for the UPL_MASTER database table.
 * 
 */
@Entity
@Table(name = "UPL_MASTER")
@NamedQuery(name = "UplMaster.findAll", query = "SELECT u FROM UplMaster u")
public class UplMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "seq_UplMaster", strategy = "com.mcredit.data.common.entity.SequenceGenerate", parameters = @Parameter(name = "seqName", value = "SEQ_UPL_MASTER_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_UplMaster")
	private Long id;

	@Column(name = "RECORD_STATUS")
	private String recordStatus;

	@CreationTimestamp
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@UpdateTimestamp
	@Column(name = "LAST_UPDATED_DATE")
	private Date lastUpdatedDate;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Column(name = "UPL_FORMAT")
	private String uplFormat;

	@Column(name = "FROM_SOURCE")
	private Long fromSource;

	@Column(name = "UPL_CODE")
	private String uplCode;

	@Column(name = "UPL_TYPE")
	private Integer uplType;

	@Column(name = "OWNER_ID")
	private Long ownerId;

	@Column(name = "IMPORTED")
	private Long imported;

	@Column(name = "TOTAL_ALLOCATED")
	private Long totalAllocated;

	@Transient
	@Column(name = "owner_login_id")
	protected String owner_login_id;

	@Transient
	@Column(name = "owner_name")
	protected String ownerName;

	@Transient
	@Column(name = "team_id")
	protected Long teamId;

	@Transient
	@Column(name = "team_name")
	protected String team_name;

	@Transient
	@Column(name = "team_lead_id")
	protected Long teamLeadId;

	@Transient
	@Column(name = "team_lead_login_id")
	protected String teamLeadLoginId;

	@Transient
	@Column(name = "team_lead_name")
	protected String ownerTeamLead;

	@Transient
	@Column(name = "total_UnAllocated")
	protected Integer totalUnallocated;

	@Transient
	@Column(name = "DESCRIPTION1")
	protected String uplTypeDescription;
	
	@Transient
	@Column(name = "allocation_MasterId")
	protected Long allocationMasterId;
	
	@Transient
	@Column(name = "case_received")
	protected Integer case_received ;
	
	@Transient
	@Column(name = "total_allocateds")
	protected Integer total_allocated ;

	public UplMaster() {
	}
	
	public UplMaster(String recordStatus, Date createdDate, Date lastUpdatedDate, String uplFormat, Long fromSource, String uplCode, Integer uplType, Long imported, Long totalAllocated) {
		
		this.recordStatus = recordStatus;
		this.createdDate = createdDate;
		this.lastUpdatedDate = lastUpdatedDate;
		this.uplFormat = uplFormat;
		this.fromSource = fromSource;
		this.uplCode = uplCode;
		this.uplType = uplType;
		this.imported = imported;
		this.totalAllocated = totalAllocated;
	}

    public UplMaster(String recordStatus, Date createdDate,String createdBy, String uplFormat, Long fromSource, String uplCode, Integer uplType, Long ownerId, 
            Long imported, Long totalAllocated) {
        this.recordStatus = recordStatus;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.uplFormat = uplFormat;
        this.fromSource = fromSource;
        this.uplCode = uplCode;
        this.uplType = uplType;
        this.ownerId = ownerId;
        this.imported = imported;
        this.totalAllocated = totalAllocated;
    }
        

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getUplFormat() {
		return uplFormat;
	}

	public void setUplFormat(String uplFormat) {
		this.uplFormat = uplFormat;
	}

	public Long getFromSource() {
		return fromSource;
	}

	public void setFromSource(Long fromSource) {
		this.fromSource = fromSource;
	}

	public String getUplCode() {
		return uplCode;
	}

	public void setUplCode(String uplCode) {
		this.uplCode = uplCode;
	}

	public Integer getUplType() {
		return uplType;
	}

	public void setUplType(Integer uplType) {
		this.uplType = uplType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getImported() {
		return imported;
	}

	public void setImported(Long imported) {
		this.imported = imported;
	}

	public Long getTotalAllocated() {
		return totalAllocated;
	}

	public void setTotalAllocated(Long totalAllocated) {
		this.totalAllocated = totalAllocated;
	}

	public String getOwner_login_id() {
		return owner_login_id;
	}

	public void setOwner_login_id(String owner_login_id) {
		this.owner_login_id = owner_login_id;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	public String getTeam_name() {
		return team_name;
	}

	public void setTeam_name(String team_name) {
		this.team_name = team_name;
	}

	public Long getTeamLeadId() {
		return teamLeadId;
	}

	public void setTeamLeadId(Long teamLeadId) {
		this.teamLeadId = teamLeadId;
	}

	public String getTeamLeadLoginId() {
		return teamLeadLoginId;
	}

	public void setTeamLeadLoginId(String teamLeadLoginId) {
		this.teamLeadLoginId = teamLeadLoginId;
	}

	public String getOwnerTeamLead() {
		return ownerTeamLead;
	}

	public void setOwnerTeamLead(String ownerTeamLead) {
		this.ownerTeamLead = ownerTeamLead;
	}

	public Integer getTotalUnallocated() {
		return totalUnallocated;
	}

	public void setTotalUnallocated(Integer totalUnallocated) {
		this.totalUnallocated = totalUnallocated;
	}
	

	public String getUplTypeDescription() {
		return uplTypeDescription;
	}

	public void setUplTypeDescription(String uplTypeDescription) {
		this.uplTypeDescription = uplTypeDescription;
	}

	public Long getAllocationMasterId() {
		return allocationMasterId;
	}

	public void setAllocationMasterId(Long allocationMasterId) {
		this.allocationMasterId = allocationMasterId;
	}

	public Integer getCase_received() {
		return case_received;
	}

	public void setCase_received(Integer case_received) {
		this.case_received = case_received;
	}

	public Integer getTotal_allocated() {
		return total_allocated;
	}

	public void setTotal_allocated(Integer total_allocated) {
		this.total_allocated = total_allocated;
	}

}