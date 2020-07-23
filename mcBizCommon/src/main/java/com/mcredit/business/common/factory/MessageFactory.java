package com.mcredit.business.common.factory;

import com.mcredit.business.common.MessageAggregate;
import com.mcredit.business.common.service.MessageService;
import com.mcredit.business.common.validation.MessageValidation;
import com.mcredit.data.common.UnitOfWorkCommon;
import com.mcredit.data.common.entity.MessageTask;
import com.mcredit.model.dto.common.MessageTaskDTO;

public class MessageFactory {

	public static MessageAggregate getMessageAggregateInstance(
			UnitOfWorkCommon uokCommon, MessageTask messageTask) {
		return new MessageAggregate(uokCommon, messageTask);
	}

	public static MessageAggregate getMessageAggregateInstance(
			UnitOfWorkCommon uokCommon) {
		return new MessageAggregate(uokCommon);
	}

	public static MessageAggregate getMessageAggregateInstance(
			UnitOfWorkCommon uokCommon, Long taskId) {
		return new MessageAggregate(uokCommon, taskId);
	}

	public static MessageService getMessageServiceInstance(
			UnitOfWorkCommon uokCommon) {
		return new MessageService(uokCommon);
	}

	public static MessageAggregate getMessageAggregateInstance(
			MessageTaskDTO request, UnitOfWorkCommon uokCommon) {
		MessageAggregate item = new MessageAggregate(uokCommon);

		MessageTask mTask = new MessageTask();
		mTask.setId(request.getId());
		mTask.setRelationId(request.getRelationId());
		mTask.setTransType(request.getTransType());
		item.setMessageTask(mTask);
		return item;
	}
	
	public static MessageValidation getMessageValidationInstance() {
		return new MessageValidation();
	}
	
}