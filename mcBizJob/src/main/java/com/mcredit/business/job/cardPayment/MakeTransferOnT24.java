package com.mcredit.business.job.cardPayment;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.mcredit.business.job.CallBack;
import com.mcredit.business.job.cardPayment.dto.MakeTransferResultDTO;
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

@Transactional(rollbackOn = ISRestCoreException.class)
public class MakeTransferOnT24 extends CallBack {
	BasedHttpClient bs = new BasedHttpClient();
	GetPropertiesValues prop = new GetPropertiesValues();

	private MsgOrder msgOrder;

	public MakeTransferOnT24(MsgOrder msgOrder) {
		this.msgOrder = msgOrder;
	}

	@Override
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
				+ BusinessConstant.T24_MAKE_TRANSFER;

		ApiResult result = bs.doPost(url, log.getMsgRequest(), ContentType.Json, AcceptType.NotSet);

		UnitOfWorkPayment unitOfWorkPayment = new UnitOfWorkPayment();
		PostingInstance postingInstance = unitOfWorkPayment.postingInstanceRepo()
				.findPostingInstanceById(log.getTransId());

		log.setResponseCode(String.valueOf(result.getCode()));
		log.setMsgResponse(result.getBodyContent());
		log.setResponseTime(new Timestamp(new Date().getTime()));

		MakeTransferResultDTO transferResultDTO = bs.toObject(MakeTransferResultDTO.class, result.getBodyContent(),
				BusinessConstant.ESB_RESULT_JSON);

		if (!result.getStatus()) {
			postingInstance.setStatus(MsgStatus.ERROR.value());
			unitOfWorkPayment.postingInstanceRepo().upsert(postingInstance);
			unitOfWorkPayment.commit();

			log.setMsgStatus(MsgStatus.ERROR.value());
			throw new Exception(transferResultDTO.getReturnCode() + " - " + transferResultDTO.getReturnMes());
		}

		if (transferResultDTO.getErrorStatus() == null
				|| !transferResultDTO.getErrorStatus().equals(BusinessConstant.RESPONSE_OK + "")) {
			postingInstance.setStatus(MsgStatus.ERROR.value());
			unitOfWorkPayment.postingInstanceRepo().upsert(postingInstance);
			unitOfWorkPayment.commit();

			log.setMsgStatus(MsgStatus.ERROR.value());
			throw new Exception(transferResultDTO.getReturnCode() + " - " + transferResultDTO.getReturnMes());
		}

		postingInstance.setFtRefNumber(transferResultDTO.getReferenceNumber());
		postingInstance.setStatus(MsgStatus.SUCCESS.value());
		unitOfWorkPayment.postingInstanceRepo().upsert(postingInstance);
		unitOfWorkPayment.commit();

		log.setResponseErrorDesc("");
		log.setMsgStatus(MsgStatus.SUCCESS.value());
		log.setResponsePayloadId(transferResultDTO.getReferenceNumber());
		jobDTO.setMessageLog(log);
	}
}
