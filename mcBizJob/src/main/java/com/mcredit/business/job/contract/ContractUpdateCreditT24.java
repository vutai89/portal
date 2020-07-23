package com.mcredit.business.job.contract;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.mcredit.business.job.CallBack;
import com.mcredit.business.job.createCard.GetPropertiesValues;
import com.mcredit.business.job.dto.JobDTO;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.model.enums.MsgOrder;
import com.mcredit.model.enums.MsgStatus;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.object.ESBResponseResultDTO;
import com.mcredit.restcore.enums.AcceptType;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.exception.ISRestCoreException;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.util.JSONConverter;

@Transactional(rollbackOn = ISRestCoreException.class)
public class ContractUpdateCreditT24 extends CallBack {
	BasedHttpClient bs = new BasedHttpClient();
	GetPropertiesValues prop = new GetPropertiesValues();

	private MsgOrder msgOrder;

	public ContractUpdateCreditT24(MsgOrder msgOrder) {
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
				+ BusinessConstant.T24_POST_UPDATE_CREDIT_CONTRACT_MC;

		ApiResult result = bs.doPost(url, log.getMsgRequest(), ContentType.Json, AcceptType.NotSet);

		log.setResponseCode(String.valueOf(result.getCode()));
		log.setMsgResponse(result.getBodyContent());
		log.setResponseTime(new Timestamp(new Date().getTime()));
		log.setResponseErrorDesc(String.valueOf(""));

		if (result.getStatus()) {
			log.setMsgStatus(MsgStatus.SUCCESS.value());
		} else {
			log.setMsgStatus(MsgStatus.ERROR.value());
			if (result.getBodyContent().contains("So hop dong nay da duoc thuc hien")) {
				log.setMsgStatus(MsgStatus.SUCCESS.value());
			} else if (result.getBodyContent().contains("Khong phai hop dong tra gop")) {
				log.setMsgStatus(MsgStatus.IGNORE.value());
			}

			ESBResponseResultDTO response = JSONConverter.toObject(result.getBodyContent(), ESBResponseResultDTO.class);
			jobDTO.getMessageLog().setResponseErrorDesc(
					response.getResult().getReturnMes().length() < 255 ? response.getResult().getReturnMes()
							: response.getResult().getReturnMes().substring(0, 254));
		}
		jobDTO.setMessageLog(log);
	}
}
