package com.mcredit.data.user.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "USERS_SESSION")
@NamedQuery(name = "UsersSession.findAll", query = "SELECT a FROM UsersSession a")
public class UsersSession implements Serializable {

	private static final long serialVersionUID = 1L;

	public UsersSession() {
	}

	@Id
	@Column(name = "LOGIN_ID")
	private String LoginId;

	@Column(name = "SESSION_KEY")
	private String SessionKey;

	@UpdateTimestamp
	@Column(name = "LAST_UPDATE_DATE")
	private Date LastUpdateDate;

	public String getLoginId() {
		return LoginId;
	}

	public void setLoginId(String loginId) {
		LoginId = loginId;
	}

	public String getSessionKey() {
		return SessionKey;
	}

	public void setSessionKey(String sessionKey) {
		SessionKey = sessionKey;
	}

	public Date getLastUpdateDate() {
		return LastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		LastUpdateDate = lastUpdateDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}