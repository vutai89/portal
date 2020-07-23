package com.mcredit.business.job.cardPayment;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mcredit.business.job.CallBack;
import com.mcredit.business.job.createCard.GetPropertiesValues;
import com.mcredit.business.job.dto.JobDTO;
import com.mcredit.business.job.sms.dto.SendSMSResultDTO;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.model.enums.MsgOrder;
import com.mcredit.model.enums.MsgStatus;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.restcore.enums.AcceptType;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;

public class CreateSendSMS extends CallBack {
	BasedHttpClient bs = new BasedHttpClient();
	GetPropertiesValues prop = new GetPropertiesValues();

	MsgOrder msgOrder = MsgOrder.NINETY_NINE;

	public void transformMessage(JobDTO jobDTO, List<MessageLog> messageLogList) {
		for (MessageLog message : messageLogList) {
			if (message.getMsgOrder() == this.msgOrder.value()) {
				jobDTO.setMessageLog(message);
				break;
			}
		}
	}

	@Override
	public void execute(JobDTO jobDTO) throws Exception {
		if (null == jobDTO || jobDTO.getMessageLog().getRelationId().trim().isEmpty())
			throw new Exception("Input message malformed!");

		MessageLog log = jobDTO.getMessageLog();
		log.setProcessTime(new Timestamp(new Date().getTime()));

		String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
				+ BusinessConstant.ESB_SEND_SMS;

		ApiResult result = bs.doPost(url, log.getMsgRequest(), ContentType.Json, AcceptType.NotSet);

		log.setResponseCode(String.valueOf(result.getCode()));
		log.setMsgResponse(result.getBodyContent());
		log.setResponseTime(new Timestamp(new Date().getTime()));

		if (!result.getStatus()) {
			log.setMsgStatus(MsgStatus.ERROR.value());
			
			SendSMSResultDTO smsResultDTO = bs.toObject(SendSMSResultDTO.class, result.getBodyContent(),
					BusinessConstant.ESB_RESULT_JSON);
			
			throw new Exception(smsResultDTO.getReturnCode() + " - " + smsResultDTO.getReturnMes());
		}
		
		SendSMSResultDTO smsResultDTO = bs.toObject(SendSMSResultDTO.class, result.getBodyContent(),
				BusinessConstant.ESB_RESULT_JSON);

		// if (!smsResultDTO.getSendSMSResultCollection().getItems().getStatus()) {
		// log.setMsgStatus(MsgStatus.ERROR.value());
		// log.setResponseErrorDesc("SMS sending failure!");
		// throw new Exception("SMS sending failure!");
		// }

		log.setResponseErrorDesc("");
		log.setMsgStatus(MsgStatus.SUCCESS.value());
		log.setResponsePayloadId(smsResultDTO.getSendSMSResultCollection().getItems().getReceiverID().toString());
		jobDTO.setMessageLog(log);
	}
}
