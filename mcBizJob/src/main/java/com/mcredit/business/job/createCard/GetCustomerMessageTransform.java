package com.mcredit.business.job.createCard;

import java.util.List;

import org.json.JSONObject;

import com.mcredit.business.job.CallBack;
import com.mcredit.business.job.createCard.dto.CreateCardDTO;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.model.enums.MsgOrder;

public class GetCustomerMessageTransform extends CallBack {

	public void execute(CreateCardDTO createcardDTO, List<MessageLog> messageLogList) {
		String customerCoreCodeString = "ClientNumber";

		JSONObject msgReq;
		MessageLog finalMessage = null;
		for (MessageLog message : messageLogList) {
			if (message.getMsgOrder() == MsgOrder.TWO.value()) {
				msgReq = new JSONObject(message.getMsgRequest());
				if (createcardDTO.getCoreCustCode() != null) {
					msgReq.put(customerCoreCodeString, "M" + createcardDTO.getCoreCustCode());
					message.setMsgRequest(msgReq.toString());
					finalMessage = message;

					// uokCommon.start();
					// uokCommon.messageLogRepository().update(finalMessage);
					// uokCommon.commit();
					break;
				}
				if (!msgReq.has(customerCoreCodeString)) {
					finalMessage = null;
					break;
				} else {
					finalMessage = message;
					break;
				}
			}
		}
		createcardDTO.setMessageLog(finalMessage);

	}
}