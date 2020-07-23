package com.mcredit.business.common.factory;

import com.mcredit.business.common.MessageAggregate;
import com.mcredit.data.common.UnitOfWorkCommon;
import com.mcredit.data.common.entity.MessageTask;

public class ProductFactory {
	public static MessageAggregate getMessageAggregateInstance(
			UnitOfWorkCommon uokCommon, MessageTask messageTask) {
		return new MessageAggregate(uokCommon, messageTask);
	}

}
