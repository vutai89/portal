package com.mcredit.data.common.entity;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.sql.Timestamp;

/**
 * The persistent class for the MESSAGE_LOG database table.
 * 
 */
@Entity(name = "MessageLog")
@Table(name = "MESSAGE_LOG", uniqueConstraints = { @UniqueConstraint(columnNames = { "ID" }) })
@NamedQueries({ @NamedQuery(name = "MessageLog.findAll", query = "SELECT m FROM MessageLog m"), })
public class MessageLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GenericGenerator(name = "seq_msgLog", strategy = "com.mcredit.data.common.entity.SequenceGenerate", parameters = @Parameter(name = "seqName", value = "SEQ_MESSAGE_LOG_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_msgLog")
	@Column(name = "ID", unique = true)

	private Long id;

	@Column(name = "FROM_CHANNEL")
	private String fromChannel;

	@Column(name = "MSG_ORDER")
	private Integer msgOrder;

	@Lob
	@Column(name = "MSG_REQUEST")
	private String msgRequest;

	@Lob
	@Column(name = "MSG_RESPONSE")
	private String msgResponse;

	@Column(name = "MSG_STATUS")
	private String msgStatus;

	@Column(name = "MSG_TYPE")
	private String msgType;

	@Column(name = "PROCESS_TIME")
	private Timestamp processTime;

	@Column(name = "RELATION_ID")
	private String relationId;

	@CreationTimestamp
	@Column(name = "REQUEST_TIME", updatable = false)
	private Timestamp requestTime;

	@Column(name = "RESPONSE_CODE")
	private String responseCode;

	@Column(name = "RESPONSE_ERROR_DESC")
	private String responseErrorDesc;

	@Column(name = "RESPONSE_TIME")
	private Timestamp responseTime;

	@Column(name = "RESPONSE_PAYLOAD_ID")
	private String responsePayloadId;

	@Column(name = "SERVICE_NAME")
	private String serviceName;

	@Column(name = "TO_CHANNEL")
	private String toChannel;

	@Column(name = "TRANS_ID")
	private Long transId;

	@Column(name = "TRANS_TYPE")
	private String transType;

	@Column(name = "TASK_ID")
	private Long taskId;

	// @Column(name = "DRANK", insertable = false, updatable = false)
	// private int drank;

	// public int getDrank() {
	// return drank;
	// }
	//
	// public void setDrank(int drank) {
	// this.drank = drank;
	// }

	public MessageLog() {
	}

	public MessageLog(String fromChannel, Integer msgOrder, String msgRequest, String msgStatus, String msgType,
			Timestamp requestTime, String relationId, String serviceName, String toChannel, Long transId,
			String transType, Long taskId) {
		super();
		this.fromChannel = fromChannel;
		this.msgOrder = msgOrder;
		this.msgRequest = msgRequest;
		this.msgStatus = msgStatus;
		this.msgType = msgType;
		this.relationId = relationId;
		this.requestTime = requestTime;
		this.serviceName = serviceName;
		this.toChannel = toChannel;
		this.transId = transId;
		this.transType = transType;
		this.taskId = taskId;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFromChannel() {
		return this.fromChannel;
	}

	public void setFromChannel(String fromChannel) {
		this.fromChannel = fromChannel;
	}

	public Integer getMsgOrder() {
		return this.msgOrder;
	}

	public void setMsgOrder(Integer msgOrder) {
		this.msgOrder = msgOrder;
	}

	public String getMsgRequest() {
		return this.msgRequest;
	}

	public void setMsgRequest(String msgRequest) {
		this.msgRequest = msgRequest;
	}

	public String getMsgResponse() {
		return this.msgResponse;
	}

	public void setMsgResponse(String msgResponse) {
		this.msgResponse = msgResponse;
	}

	public String getMsgStatus() {
		return this.msgStatus;
	}

	public void setMsgStatus(String msgStatus) {
		this.msgStatus = msgStatus;
	}

	public String getMsgType() {
		return this.msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public Timestamp getProcessTime() {
		return this.processTime;
	}

	public void setProcessTime(Timestamp processTime) {
		this.processTime = processTime;
	}

	public String getRelationId() {
		return this.relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public Timestamp getRequestTime() {
		return this.requestTime;
	}

	public void setRequestTime(Timestamp requestTime) {
		this.requestTime = requestTime;
	}

	public String getResponseCode() {
		return this.responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseErrorDesc() {
		return this.responseErrorDesc;
	}

	public void setResponseErrorDesc(String responseErrorDesc) {
		this.responseErrorDesc = responseErrorDesc;
	}

	public Timestamp getResponseTime() {
		return this.responseTime;
	}

	public void setResponseTime(Timestamp responseTime) {
		this.responseTime = responseTime;
	}

	public String getResponsePayloadId() {
		return this.responsePayloadId;
	}

	public void setResponsePayloadId(String responsePayloadId) {
		this.responsePayloadId = responsePayloadId;
	}

	public String getServiceName() {
		return this.serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getToChannel() {
		return this.toChannel;
	}

	public void setToChannel(String toChannel) {
		this.toChannel = toChannel;
	}

	public Long getTransId() {
		return this.transId;
	}

	public void setTransId(Long transId) {
		this.transId = transId;
	}

	public String getTransType() {
		return this.transType;
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

	public MessageLog(String fromChannel, String msgRequest, String msgResponse, Timestamp processTime, String relationId, String responseCode, Timestamp responseTime, String toChannel, Long transId,
			String transType) {
		super();
		this.fromChannel = fromChannel;
		this.msgRequest = msgRequest;
		this.msgResponse = msgResponse;
		this.processTime = processTime;
		this.relationId = relationId;
		this.responseCode = responseCode;
		this.responseTime = responseTime;
		this.toChannel = toChannel;
		this.transId = transId;
		this.transType = transType;
	}
	
	/*
	 * @author: truongvd.ho
	 * 
	 */
	
	public MessageLog(String fromChannel, String msgRequest, String msgResponse, Timestamp processTime, String relationId, String responseCode, Timestamp responseTime, String toChannel, Long transId,
			String transType, String responsePayloadId ,String msgStatus,String stepBpm, String msgType, Integer msgOrder) {
		super();
		this.fromChannel = fromChannel;
		this.msgRequest = msgRequest;
		this.msgResponse = msgResponse;
		this.processTime = processTime;
		this.relationId = relationId;
		this.responseCode = responseCode;
		this.responseTime = responseTime;
		this.toChannel = toChannel;
		this.transId = transId;
		this.transType = transType;
		this.responsePayloadId = responsePayloadId;
		this.msgStatus = msgStatus;
		this.serviceName = stepBpm;
		this.msgType = msgType;
		this.msgOrder = msgOrder;
	}
}