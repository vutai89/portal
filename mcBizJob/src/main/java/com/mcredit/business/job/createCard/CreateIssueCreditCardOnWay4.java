package com.mcredit.business.job.createCard;

import java.sql.Timestamp;
import java.util.Date;

import javax.transaction.Transactional;

import org.json.JSONObject;

import com.mcredit.business.common.CommonAggregate;
import com.mcredit.business.job.CallBack;
import com.mcredit.business.job.createCard.dto.CreateCardDTO;
import com.mcredit.business.job.createCard.dto.CreateIssuecardResultDTO;
import com.mcredit.data.common.UnitOfWorkCommon;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.model.enums.MsgChannel;
import com.mcredit.model.enums.MsgOrder;
import com.mcredit.model.enums.MsgStatus;
import com.mcredit.model.enums.MsgType;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.restcore.enums.AcceptType;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.exception.ISRestCoreException;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.util.MessageTranslator;

@Transactional(rollbackOn = ISRestCoreException.class)
public class CreateIssueCreditCardOnWay4 extends CallBack {
	BasedHttpClient bs = new BasedHttpClient();
	GetPropertiesValues prop = new GetPropertiesValues();

	@Override
	public void execute(CreateCardDTO createcardDTO) throws Exception {
		if (null == createcardDTO || createcardDTO.getMessageLog().getRelationId().trim().isEmpty()) {
			throw new Exception("Input message malformed!");
		}

		String[] path = { "ClientRegPlace", "IssueFee", "AnnualFee", "InsureFee" };
		String[] categories = { "IDPLACE", "BOOLEAN", "BOOLEAN", "BOOLEAN" };
		String[] addressFields = { "CardReceivedAddress", "ClientAddress" };
		String[] removeAccentFields = { "ClientRegPlace", "EmbossedName" };
		String[] removeSpaceFields = { "ClientRegPlace" };
		String[] setUpperFields = { "EmbossedName" };

		String[] codeTableValue_systemDefineFields = { "ClientGender" };
		String[] categories_systemDefineFields = { "GENDER" };
		String[] systemCode_systemDefineFields = { "W4" };

		String translatedRequest = createcardDTO.getMessageLog().getMsgRequest();
		translatedRequest = MessageTranslator.translateCodeValue(translatedRequest, path, categories);
		translatedRequest = MessageTranslator.translateAddressW4(translatedRequest, addressFields);
		translatedRequest = MessageTranslator.translateAccent(translatedRequest, removeAccentFields);
		translatedRequest = MessageTranslator.RemoveSpace(translatedRequest, removeSpaceFields);
		translatedRequest = MessageTranslator.SetUpper(translatedRequest, setUpperFields);
		translatedRequest = MessageTranslator.translateSystemDefineFields(translatedRequest,
				codeTableValue_systemDefineFields, categories_systemDefineFields, systemCode_systemDefineFields);
		createcardDTO.getMessageLog().setProcessTime(new Timestamp(new Date().getTime()));
		
		System.out.println("issuecreditcard:" + createcardDTO.getCoreCustCode() + "-payload" + translatedRequest);
		String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
				+ BusinessConstant.WAY4_CREATE_CARD;
		ApiResult result = bs.doPost(url, translatedRequest, ContentType.Json, AcceptType.Json);
		createcardDTO.getMessageLog().setMsgResponse(result.getBodyContent());
		createcardDTO.getMessageLog().setResponseCode(String.valueOf(result.getCode()));
		createcardDTO.getMessageLog().setResponseTime(new Timestamp(new Date().getTime()));

		CreateIssuecardResultDTO createCreditOutput = bs.toObject(CreateIssuecardResultDTO.class,
				result.getBodyContent(), BusinessConstant.ESB_RESULT_JSON);

		if (!result.getStatus() || createCreditOutput.getIssueId().isEmpty()) { // status error or IssueId = null
			createcardDTO.getMessageLog().setMsgStatus(MsgStatus.ERROR.value());
			throw new Exception(
					createCreditOutput.getReturnCode() + " - " + createCreditOutput.getReturnMes());
		}

		createcardDTO.getMessageLog().setResponseErrorDesc("");
		createcardDTO.setCardId(createCreditOutput.getIssueId());
		createcardDTO.getMessageLog().setResponsePayloadId(createCreditOutput.getIssueId());
		createcardDTO.getMessageLog().setMsgStatus(MsgStatus.SUCCESS.value());
		createcardDTO.setMessageTaskStatus(MsgStatus.SUCCESS);

		createMessgeLog(createcardDTO.getMessageLog(), createCreditOutput.getIssueId());
	}

	public void createMessgeLog(MessageLog oldMsg, String issueId) {
		MessageLog msg = new MessageLog();
		UnitOfWorkCommon uok = new UnitOfWorkCommon();
		JSONObject request = new JSONObject();
		request.append("IssueId", issueId);
		msg.setRelationId(oldMsg.getRelationId());
		msg.setTaskId(oldMsg.getTaskId());
		msg.setMsgRequest(request.toString());
		msg.setFromChannel(MsgChannel.MC_PORTAL_APPLICATION.value());
		msg.setMsgOrder(MsgOrder.THREE.value());
		msg.setMsgType(MsgType.REQUEST.value());
		msg.setServiceName(BusinessConstant.SERVICE_GET_ISSUED_CARD);
		msg.setToChannel(MsgChannel.WAY4_APPLICATION.value());
		msg.setMsgType(oldMsg.getMsgType());
		msg.setTransType(oldMsg.getTransType());
		msg.setMsgStatus(MsgStatus.NEW.value());
		try {
//			msg.setTransId(Long.parseLong(issueId));
			msg.setTransId(oldMsg.getTransId());
		} catch (Exception ex) {
			msg.setTransId(null);
		}

		try {
			uok.start();
			CommonAggregate mglogAgg = new CommonAggregate(uok);
			mglogAgg.setMessageLog(msg);
			mglogAgg.upsertMessageLog();
		} catch (Exception e) {
			uok.rollback();
		}
	}
}
