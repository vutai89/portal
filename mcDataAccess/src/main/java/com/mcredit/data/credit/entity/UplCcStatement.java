package com.mcredit.data.credit.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the UPL_CC_STATEMENT database table.
 * 
 */
@Entity
@Table(name="UPL_CC_STATEMENT", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class UplCcStatement implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@Column(name="CARD_ID")
	private String cardId;

	@Column(name="CARD_STATUS")
	private String cardStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getCardStatus() {
		return cardStatus;
	}

	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}
}