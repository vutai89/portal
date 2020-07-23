package com.mcredit.business.job.cardPayment;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.mcredit.business.job.CallBack;
import com.mcredit.business.job.cardPayment.dto.MakePaymentMCreditCardResultDTO;
import com.mcredit.business.job.createCard.GetPropertiesValues;
import com.mcredit.business.job.dto.JobDTO;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.payment.UnitOfWorkPayment;
import com.mcredit.data.payment.entity.PostingInstance;
import com.mcredit.model.enums.MsgOrder;
import com.mcredit.model.enums.MsgStatus;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.restcore.enums.AcceptType;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.exception.ISRestCoreException;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.util.JSONConverter;

@Transactional(rollbackOn = ISRestCoreException.class)
public class MakePaymentMCreditCardOnWay4 extends CallBack {
	BasedHttpClient bs = new BasedHttpClient();
	GetPropertiesValues prop = new GetPropertiesValues();
	MsgOrder msgOrder = MsgOrder.THREE;

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
				+ BusinessConstant.WAY4_PAYMENT_CREDIT_CARD;

		ApiResult result = bs.doPost(url, log.getMsgRequest(), ContentType.Json, AcceptType.NotSet);

		log.setResponseCode(String.valueOf(result.getCode()));
		log.setMsgResponse(JSONConverter.toJSON(result));
		log.setResponseTime(new Timestamp(new Date().getTime()));

		UnitOfWorkPayment unitOfWorkPayment = new UnitOfWorkPayment();
		PostingInstance postingInstance = unitOfWorkPayment.postingInstanceRepo()
				.findPostingInstanceById(log.getTransId());

		MakePaymentMCreditCardResultDTO cardResultDTO = bs.toObject(MakePaymentMCreditCardResultDTO.class,
				result.getBodyContent(), BusinessConstant.ESB_RESULT_JSON);

		if (!result.getStatus()) {
			log.setMsgStatus(MsgStatus.ERROR.value());
			postingInstance.setStatus(MsgStatus.ERROR.value());
			unitOfWorkPayment.postingInstanceRepo().upsert(postingInstance);
			unitOfWorkPayment.commit();

			throw new Exception(cardResultDTO.getReturnCode() + " - " + cardResultDTO.getReturnMes());
		}

		if (cardResultDTO.getErrorStatus() == null
				|| !cardResultDTO.getErrorStatus().equals(BusinessConstant.RESPONSE_OK + "")) {
			postingInstance.setStatus(MsgStatus.ERROR.value());
			unitOfWorkPayment.postingInstanceRepo().upsert(postingInstance);
			unitOfWorkPayment.commit();

			log.setMsgStatus(MsgStatus.ERROR.value());
			log.setResponseErrorDesc("Payload Malformed!");
			throw new Exception(cardResultDTO.getReturnCode() + " - " + cardResultDTO.getReturnMes());
		}

		postingInstance.setFtRefNumber(cardResultDTO.getFTID());
		postingInstance.setStatus(MsgStatus.SUCCESS.value());
		unitOfWorkPayment.postingInstanceRepo().upsert(postingInstance);
		unitOfWorkPayment.commit();

		log.setMsgStatus(MsgStatus.SUCCESS.value());
		log.setResponsePayloadId(cardResultDTO.getFTID());
		log.setResponseErrorDesc("");
		jobDTO.setMessageLog(log);
	}
}
