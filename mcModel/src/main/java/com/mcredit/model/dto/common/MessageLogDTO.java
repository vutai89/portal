package com.mcredit.model.dto.common;

import java.sql.Timestamp;

public class MessageLogDTO {
	private Long id;
	private String fromChannel;
	private Integer msgOrder;
	private String msgRequest;
	private String msgResponse;
	private String msgStatus;
	private String msgType;
	private Timestamp processTime;
	private String relationId;
	private Timestamp requestTime;
	private String responseCode;
	private String responseErrorDesc;
	private Timestamp responseTime;
	private String serviceName;
	private String toChannel;
	private Long transId;
	private String transType;
	private Long taskId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFromChannel() {
		return fromChannel;
	}
	public void setFromChannel(String fromChannel) {
		this.fromChannel = fromChannel;
	}
	public Integer getMsgOrder() {
		return msgOrder;
	}
	public void setMsgOrder(Integer msgOrder) {
		this.msgOrder = msgOrder;
	}
	public String getMsgRequest() {
		return msgRequest;
	}
	public void setMsgRequest(String msgRequest) {
		this.msgRequest = msgRequest;
	}
	public String getMsgResponse() {
		return msgResponse;
	}
	public void setMsgResponse(String msgResponse) {
		this.msgResponse = msgResponse;
	}
	public String getMsgStatus() {
		return msgStatus;
	}
	public void setMsgStatus(String msgStatus) {
		this.msgStatus = msgStatus;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public Timestamp getProcessTime() {
		return processTime;
	}
	public void setProcessTime(Timestamp processTime) {
		this.processTime = processTime;
	}
	public String getRelationId() {
		return relationId;
	}
	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
	public Timestamp getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Timestamp requestTime) {
		this.requestTime = requestTime;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseErrorDesc() {
		return responseErrorDesc;
	}
	public void setResponseErrorDesc(String responseErrorDesc) {
		this.responseErrorDesc = responseErrorDesc;
	}
	public Timestamp getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(Timestamp responseTime) {
		this.responseTime = responseTime;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getToChannel() {
		return toChannel;
	}
	public void setToChannel(String toChannel) {
		this.toChannel = toChannel;
	}
	public Long getTransId() {
		return transId;
	}
	public void setTransId(Long transId) {
		this.transId = transId;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	
}
