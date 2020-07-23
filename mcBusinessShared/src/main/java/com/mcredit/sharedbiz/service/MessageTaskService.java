package com.mcredit.sharedbiz.service;

import com.mcredit.data.common.UnitOfWorkCommon;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.common.entity.MessageTask;
import com.mcredit.model.enums.MsgChannel;
import com.mcredit.model.enums.MsgStatus;
import com.mcredit.model.enums.MsgType;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.validation.PaymentalidationConstants;
import com.mcredit.sharedbiz.validation.ValidationException;

public class MessageTaskService {
	
	private UnitOfWorkCommon unitOfWorkCommon = null;
	
	public MessageTaskService(UnitOfWorkCommon unitOfWorkCommon) {
		this.unitOfWorkCommon = unitOfWorkCommon;
	}
	
	public MessageTask createMessageTask(String partnerCode, String postingGroup, String partnerRefId) {
		
		MessageTask msTask = new MessageTask();
		
		msTask.setRelationId(partnerCode + "-" + postingGroup + "-" + partnerRefId);
		
		msTask.setTransType(PaymentalidationConstants.SERVICER_NAME_CREATEPAYMENT);
		
		msTask.setStatus(MsgStatus.NEW.value());
		
		unitOfWorkCommon.messageTaskRepo().upsert(msTask);
		
		return msTask;
	}
	
	public void createMessageLog(Integer transactionTypeId, String channel, Integer msgOrder, String msgRequest, Long taskId
			, String relationId , Long transId ,String status) throws Exception {
		
		MessageLog msLog = new MessageLog();
		
		msLog.setFromChannel(PaymentalidationConstants.PAYMENT_FROM_CHANEL);
		
		msLog.setMsgType(MsgType.REQUEST.value());
		
		msLog.setToChannel(channel);
		
		msLog.setMsgOrder(msgOrder);
		
		msLog.setRelationId(relationId);
		
		msLog.setMsgRequest(msgRequest);
		
		msLog.setMsgStatus(MsgStatus.NEW.value());
		if( transactionTypeId==null )
			throw new ValidationException("transactionTypeId is null!");
		
		if(channel.equals(MsgChannel.T24_APPLICATION.value()))
			msLog.setServiceName(PaymentalidationConstants.SERVICER_NAME_MAKE_TRANSFER);
			msLog.setTransType((CacheManager.CodeTable().getCodeById(transactionTypeId)).getCodeValue1());
			
		if(channel.equals(MsgChannel.WAY4_APPLICATION.value()))
			msLog.setServiceName(PaymentalidationConstants.SERVICER_NAME_PAYMENT_CREDIT_CARD);
		msLog.setTransType((CacheManager.CodeTable().getCodeById(transactionTypeId)).getCodeValue2());
		
		msLog.setTransId(transId);
		
		msLog.setTaskId(taskId);
		
		msLog.setTransType((CacheManager.CodeTable().getCodeById(transactionTypeId)).getCodeValue1());
		
		unitOfWorkCommon.messageLogRepo().upsert(msLog);
	}
	
	public void createMessageLogSMS(String tranType, String channel, Integer msgOrder, String msgRequest, Long taskId
			, String relationId , Long transId ,String status) throws Exception {
		
		MessageLog msLog = new MessageLog();
		
		msLog.setFromChannel(PaymentalidationConstants.PAYMENT_FROM_CHANEL);
		
		msLog.setMsgType(MsgType.REQUEST.value());
		
		msLog.setToChannel(channel);
		
		msLog.setMsgOrder(msgOrder);
		
		msLog.setRelationId(relationId);
		
		msLog.setMsgRequest(msgRequest);
		
		if(status.equals("U"))
			msLog.setMsgStatus(MsgStatus.IGNORE.value());
		else
			msLog.setMsgStatus(MsgStatus.NEW.value());
		
		msLog.setServiceName(PaymentalidationConstants.SERVICER_NAME_SMS_QUEUE);
		
		msLog.setTransId(transId);
		
		msLog.setTaskId(taskId);
		
		msLog.setTransType(tranType);
		
		unitOfWorkCommon.messageLogRepo().upsert(msLog);
	}
	
	public MessageTask createMessageTask(MessageTask messageTask) {		
		
		messageTask.setStatus(MsgStatus.NEW.value());
		
		unitOfWorkCommon.messageTaskRepo().upsert(messageTask);
		
		return messageTask;
	}
	
	public MessageLog createMessageLog(MessageLog messageLog) throws Exception {
		
		unitOfWorkCommon.messageLogRepo().upsert(messageLog);
		
		return messageLog;
	}
}
