package com.mcredit.model.dto.common;

import java.io.Serializable;
import java.util.List;

public class MessageTaskDTO implements Serializable {
	private static final long serialVersionUID = -2474205601791521665L;

	private Long id;	
	private String relationId;	
	private String transType;
	List<MessageLogDTO> listMessage;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}	

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
	
	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public List<MessageLogDTO> getListMessage() {
		return listMessage;
	}

	public void setListMessage(List<MessageLogDTO> listMessage) {
		this.listMessage = listMessage;
	}

}
