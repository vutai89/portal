package com.mcredit.business.job.createCard;

import java.util.List;

import com.mcredit.business.job.CallBack;
import com.mcredit.business.job.createCard.dto.CreateCardDTO;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.model.enums.MsgOrder;

public class UpdateBPMMessageTransform extends CallBack {
	public void execute(CreateCardDTO createcardDTO, List<MessageLog> messageLogList) {
		
		MessageLog finalMessage = null;
		for (MessageLog message : messageLogList) {
			if (message.getMsgOrder()==MsgOrder.FOUR.value()) {
				finalMessage = message;
				break;
			}
		}
		createcardDTO.setMessageLog(finalMessage);

	}

}
