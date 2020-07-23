package com.mcredit.business.job.contract;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import com.mcredit.business.job.dto.JobRespondDTO;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.common.entity.MessageTask;
import com.mcredit.model.dto.contract.LoanDepositCreditEntries;
import com.mcredit.model.dto.contract.LoanDepositCreditEntry;
import com.mcredit.model.dto.contract.UpdateCreditContractMCDTO;
import com.mcredit.model.enums.MsgChannel;
import com.mcredit.model.enums.MsgOrder;
import com.mcredit.model.enums.MsgStatus;
import com.mcredit.model.enums.MsgTransType;
import com.mcredit.model.enums.MsgType;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.ServiceName;
import com.mcredit.restcore.enums.AcceptType;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.manager.BaseManager;
import com.mcredit.sharedbiz.service.MessageTaskService;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.DateTimeFormat;
import com.mcredit.util.DateUtil;
import com.mcredit.util.JSONConverter;

public class ContractManager extends BaseManager {
	private BasedHttpClient bs = new BasedHttpClient();

	public JobRespondDTO GetContractCreditUpdateT24() throws Exception {
		return this.tryCatch(() -> {
			JobRespondDTO jobRespondDTO = new JobRespondDTO();
			jobRespondDTO.setFetch(0);
			jobRespondDTO.setIgnore(0);
			jobRespondDTO.setSuccess(0);
			jobRespondDTO.setError(0);

			String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
					+ BusinessConstant.T24_GET_CREATE_LOAN_DEPOSIT_CREDIT;

			ApiResult result = bs.doGet(url, AcceptType.Json);
			if (result.getCode() == 400) {
				return jobRespondDTO;
			}

			if (!result.getStatus()) {
				throw new ValidationException("Error: " + result.getBodyContent());
			}

			LoanDepositCreditEntries creditUpdateDTO = new LoanDepositCreditEntries();
			try {
				creditUpdateDTO = bs.toObject(LoanDepositCreditEntries.class, result.getBodyContent());
			} catch (Exception e) {
				creditUpdateDTO = new LoanDepositCreditEntries();
				creditUpdateDTO.setItems(new ArrayList<LoanDepositCreditEntry>());
			}

			if (creditUpdateDTO.getItems().size() <= 0)
				return jobRespondDTO;

			jobRespondDTO.setFetch(creditUpdateDTO.getItems().size());

			MessageTaskService messageTaskService = new MessageTaskService(this.uok.common);

			MessageTask messageTask = new MessageTask();

			messageTask.setRelationId(UUID.randomUUID().toString());

			messageTask.setTransType(MsgTransType.UPDATE_CREDIT.value());

			messageTask.setStatus(MsgStatus.NEW.value());

			messageTask = messageTaskService.createMessageTask(messageTask);

			for (LoanDepositCreditEntry item : creditUpdateDTO.getItems()) {
				MessageLog msLog = new MessageLog();

				msLog.setFromChannel(MsgChannel.MC_PORTAL_APPLICATION.value());

				msLog.setMsgType(MsgType.REQUEST.value());

				msLog.setToChannel(MsgChannel.T24_APPLICATION.value());

				msLog.setMsgOrder(MsgOrder.ONE.value());

				msLog.setRelationId(item.getContractNumber());

				msLog.setMsgRequest(JSONConverter.toJSON(new UpdateCreditContractMCDTO(item.getContractNumber(),
						DateUtil.changeFormat(item.getApproveDate(),
								DateTimeFormat.yyyyMMdd_T_HHmmssSSSXXX.getDescription(),
								DateTimeFormat.yyyyMMdd.getDescription()).toString())));

				msLog.setMsgStatus(MsgStatus.NEW.value());

				msLog.setServiceName(ServiceName.POST_T24_V1_UpdateCreditContractMC.stringValue());

				msLog.setTransId(Long.valueOf("0"));

				msLog.setTaskId(messageTask.getId());

				msLog.setTransType(MsgTransType.UPDATE_CREDIT.value());

				messageTaskService.createMessageLog(msLog);

				item.setLastUpdatedDate(
						DateUtil.toString(new Date(), DateTimeFormat.yyyyMMdd.getDescription()).toString());

				item.setStatus("1");

				String urlUpsert = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
						+ BusinessConstant.T24_GET_CREATE_LOAN_DEPOSIT_CREDIT_UPSERT;

				ApiResult updateStatusContract = bs.doPost(urlUpsert, JSONConverter.toJSON(item), ContentType.Json);

				if (!updateStatusContract.getStatus()) {
					jobRespondDTO.setError(jobRespondDTO.getError() + 1);
				} else {
					jobRespondDTO.setError(jobRespondDTO.getSuccess() + 1);
				}
			}
			return jobRespondDTO;
		});

	}
}
