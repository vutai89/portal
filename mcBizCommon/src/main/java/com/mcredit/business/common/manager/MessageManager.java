package com.mcredit.business.common.manager;

import com.mcredit.business.common.MessageAggregate;
import com.mcredit.business.common.factory.CommonFactory;
import com.mcredit.business.common.factory.MessageFactory;
import com.mcredit.model.dto.IdDTO;
import com.mcredit.model.dto.common.MessageLogDTO;
import com.mcredit.model.dto.common.MessageTaskDTO;
import com.mcredit.model.enums.MsgStatus;
import com.mcredit.sharedbiz.manager.BaseManager;

public class MessageManager extends BaseManager {

	public IdDTO upsertMessageTask(MessageTaskDTO request, String updateId) throws Exception {

		return this.tryCatch(()->{
			
			IdDTO id = new IdDTO();
			
			// Validation check
			MessageFactory.getMessageValidationInstance().validateMessageTask(request, updateId);
			if (updateId != null)
				request.setId(Long.parseLong(updateId));
			MessageAggregate item = MessageFactory.getMessageAggregateInstance(request, this.uok.common);
			item.getMessageTask().setStatus(MsgStatus.NEW.value());
			item.upsertMessageTask();

			if (item.getMessageTask().getId() != null) {
				for (MessageLogDTO messageLogDTO : request.getListMessage()) {
					messageLogDTO.setTaskId(item.getMessageTask().getId());
					CommonFactory.getInstanceMessageLog(messageLogDTO, this.uok.common).upsertMessageLog();
				}
			}
			
			id.setId(item.getMessageTask().getId());
			
			return id;
		});
				
	}

}
