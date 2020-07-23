package com.mcredit.data.debt_home.entity;

import java.sql.Date;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "debt_home_assign")
public class DebtHomeAssign {
	
	@Id
	@GenericGenerator(name = "seq_debt_home"
				    , strategy = "com.mcredit.data.common.entity.SequenceGenerate"
					, parameters = @Parameter(name = "seqName", value = "SEQ_debt_home_assign_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_debt_home")
	private Long id;
	
	@Column(name="contract_number")
	private String contractNumber;
	
	@Column(name="user_assign")
	private String userAssign;
	
	@Column(name="teamlead")
	private String teamlead;
	
	@Column(name="administrator")
	private String administrator;

	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="created_by")
	private String createdBy;
	
	@Column(name = "status")
	private String status;

	public DebtHomeAssign(String contractNumber, String userAssign, String teamlead, String administrator, String createdBy) {
	
		this.contractNumber = contractNumber;
		this.userAssign = userAssign;
		this.teamlead = teamlead;
		this.administrator = administrator;
		this.createdBy = createdBy;
		this.createdDate = new Date(Calendar.getInstance().getTime().getTime());
		this.status = "A";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getUserAssign() {
		return userAssign;
	}

	public void setUserAssign(String userAssign) {
		this.userAssign = userAssign;
	}

	public String getTeamlead() {
		return teamlead;
	}

	public void setTeamlead(String teamlead) {
		this.teamlead = teamlead;
	}

	public String getAdministrator() {
		return administrator;
	}

	public void setAdministrator(String administrator) {
		this.administrator = administrator;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
