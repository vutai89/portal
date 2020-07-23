package com.mcredit.model.dto;

import java.io.Serializable;
import java.sql.Clob;
import java.util.Date;

public class ProdHisDetailsDTO implements Serializable {

	private static final long serialVersionUID = 4629075367866218516L;
	public Integer id;
	public Date lastUpdatedDate;
	public String createdBy;
	public String tableName;   
	public Integer action;
	public Clob content;
	public String sContent;
	public String sAction;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Integer getAction() {
		return action;
	}
	public void setAction(Integer action) {
		this.action = action;
	}
	public Clob getContent() {
		return content;
	}
	public void setContent(Clob content) {
		this.content = content;
	}
	public String getsContent() {
		return sContent;
	}
	public void setsContent(String sContent) {
		this.sContent = sContent;
	}
	public String getsAction() {
		return sAction;
	}
	public void setsAction(String sAction) {
		this.sAction = sAction;
	}
	
	
	
}
