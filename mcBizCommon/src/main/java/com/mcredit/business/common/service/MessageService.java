package com.mcredit.business.common.service;

import java.util.List;

import com.mcredit.data.common.UnitOfWorkCommon;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.common.entity.MessageTask;
import com.mcredit.model.enums.MsgOrder;
import com.mcredit.model.enums.MsgTransType;

public class MessageService {
	private UnitOfWorkCommon unitOfWorkCommon = null;

	public MessageService(UnitOfWorkCommon uok) {
		this.unitOfWorkCommon = uok;
	}
	
	public List<MessageLog> findMessageLogBy(String messageType,String[] msgStatus, MsgOrder msgOrder){
		return unitOfWorkCommon.messageLogRepo().getMessageBy(messageType,msgStatus,msgOrder.value());
	}
	
	public List<MessageLog> findMessageLogBy(String messageType,String[] msgStatus){
		return unitOfWorkCommon.messageLogRepo().getMessageBy(messageType,msgStatus);
	}
	
	public List<MessageLog> findMessageForSendSMS(String messageType, String[] msgStatus, int limit){
		return unitOfWorkCommon.messageLogRepo().getMessageForSendSMS(messageType, msgStatus, limit);
	}
	
	public void updateMessageLog(MessageLog messageLog){
		unitOfWorkCommon.messageLogRepo().update(messageLog);
	}
	
	public List<MessageTask> findMessageTaskBy(MsgTransType messageType,String[] msgStatus) {
		return unitOfWorkCommon.messageTaskRepo().getMessageTaskBy(messageType.value(), msgStatus);
	}
	
	public void updateBatchMessageTask(List<MessageTask> messageTasks) throws Exception{
		if (messageTasks == null || messageTasks.size() == 0)
			throw new Exception("Object messageTasks is null!");

		for (MessageTask messageTask : messageTasks) {
			unitOfWorkCommon.messageTaskRepo().update(messageTask);
		}

	}
	
	
}
