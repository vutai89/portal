package com.mcredit.business.job.createCard;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import javax.transaction.Transactional;

import org.json.JSONObject;

import com.mcredit.business.common.MessageAggregate;
import com.mcredit.business.credit.CreditAggregate;
import com.mcredit.business.customer.CustomerManager;
import com.mcredit.business.customer.dto.CustomerDTO;
import com.mcredit.business.job.CallBack;
import com.mcredit.business.job.createCard.dto.CreateCardDTO;
import com.mcredit.business.job.createCard.dto.CreateCustomerAccountLinkItemsDTO;
import com.mcredit.business.job.createCard.dto.CreateCustomerAccountLinkResultDTO;
import com.mcredit.data.common.UnitOfWorkCommon;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.common.entity.MessageTask;
import com.mcredit.data.credit.UnitOfWorkCredit;
import com.mcredit.model.dto.IdDTO;
import com.mcredit.model.dto.card.CustomerAccountLinkDTO;
import com.mcredit.model.enums.AccountLinkType;
import com.mcredit.model.enums.MsgChannel;
import com.mcredit.model.enums.MsgConcurency;
import com.mcredit.model.enums.MsgOrder;
import com.mcredit.model.enums.MsgStatus;
import com.mcredit.model.enums.MsgType;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.RecordStatus;
import com.mcredit.restcore.enums.AcceptType;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.exception.ISRestCoreException;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.util.StringUtils;

/**
 * @author anhdv.ho
 *
 */
@Transactional(rollbackOn = ISRestCoreException.class)
public class GetIssueCardOnWay4 extends CallBack {

	BasedHttpClient bs = new BasedHttpClient();
	GetPropertiesValues prop = new GetPropertiesValues();

	@Override
	public void execute(CreateCardDTO createcardDTO) throws Exception {

		createcardDTO.getMessageLog().setProcessTime(new Timestamp(new Date().getTime()));

		if (createcardDTO == null || createcardDTO.getMessageLog() == null
				|| StringUtils.isNullOrEmpty(createcardDTO.getMessageLog().getMsgRequest())
				|| StringUtils.isNullOrEmpty(createcardDTO.getMessageLog().getRelationId()))
			return;

		String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
				+ BusinessConstant.WAY4_GET_CARD;

		String translateMessage = createcardDTO.getMessageLog().getMsgRequest();

		ApiResult result = bs.doPost(url, translateMessage, ContentType.Json, AcceptType.Json);
		/*
		 * ApiResult result = new ApiResult(); result.
		 * setBodyContent("{\"Result\":{\"error_status\":\"0\",\"issue_result\":[{\"CardNumber\": \"956725______6019\",\"CardId\": 8274447}]}}"
		 * ); result.setCode(200);
		 */
		CreateCustomerAccountLinkResultDTO createCustomerAccountLinkOutput = bs.toObject(
				CreateCustomerAccountLinkResultDTO.class, result.getBodyContent(),
				BusinessConstant.ESB_RESULT_JSON);

		createcardDTO.getMessageLog().setMsgResponse(result.getBodyContent());
		createcardDTO.getMessageLog().setResponseCode(String.valueOf(result.getCode()));
		createcardDTO.getMessageLog().setResponseTime(new Timestamp(new Date().getTime()));

		if (!result.getStatus()) {
			createcardDTO.getMessageLog().setMsgStatus(MsgStatus.ERROR.value());
			throw new Exception(createCustomerAccountLinkOutput.getReturnCode() + " - "
					+ createCustomerAccountLinkOutput.getReturnMes());
		}

		if (createCustomerAccountLinkOutput == null || (createCustomerAccountLinkOutput.getIssue_result() == null
				|| createCustomerAccountLinkOutput.getIssue_result().size() <= 0
				|| createCustomerAccountLinkOutput.getIssue_result().get(0).getCardId() == null || StringUtils
						.isNullOrEmpty(createCustomerAccountLinkOutput.getIssue_result().get(0).getCardNumber()))) {

			createcardDTO.getMessageLog().setMsgStatus(MsgStatus.ERROR.value());
			throw new Exception(createCustomerAccountLinkOutput.getReturnCode() + " - "
					+ createCustomerAccountLinkOutput.getReturnMes());
		}

		Long custId = null;
		// Long custId = new Long("1259");
		Long creditAppId = null;
		CustomerDTO request = null;

		for (CreateCustomerAccountLinkItemsDTO accountLink : createCustomerAccountLinkOutput.getIssue_result()) {

			if (accountLink == null) {
				createcardDTO.getMessageLog().setResponseErrorDesc("Card Not Issued!");
				continue;
			}

			if (!accountLink.isIssueStatus()) {
				createcardDTO.getMessageLog().setResponseErrorDesc(accountLink.getIssueDesc());
				continue;
			}

			custId = findCustIdByRelationIdMessageLog(createcardDTO.getMessageLog().getRelationId());

			if (custId == null || custId <= 0) {
				createcardDTO.getMessageLog().setResponseErrorDesc("GetIssueCardOnWay4 ==> custId not found!");
				continue;
			}

			request = new CustomerDTO();
			request.setAccountLink(new CustomerAccountLinkDTO(custId, AccountLinkType.CARD_ID.value(),
					RecordStatus.ACTIVE.value(), "", accountLink.getCardId() + "", "",
					MsgChannel.WAY4_APPLICATION.value(), MsgConcurency.VND.value(), "")); // Card ID

			if (createAccountLinkWithCardId(request)) {

				request = new CustomerDTO();
				request.setAccountLink(new CustomerAccountLinkDTO(custId, AccountLinkType.CARD_NUMBER.value(),
						RecordStatus.ACTIVE.value(), "", accountLink.getCardNumber(), "",
						MsgChannel.WAY4_APPLICATION.value(), MsgConcurency.VND.value(), "")); // Card number

				if (createAccountLinkWithCardId(request)) {

					// createMessgeLog(createcardDTO.getCoreCustCode(),
					// createcardDTO.getMessageLog(), accountLink.getIssueId(),
					// accountLink.getCardId() + "");

					// TODO updateCreditAppLmsWithCardId
					creditAppId = getAppIdByRelationIdMessageLog(createcardDTO.getMessageLog().getRelationId());
					if (creditAppId == null || creditAppId <= 0) {
						createcardDTO.getMessageLog()
								.setResponseErrorDesc("GetIssueCardOnWay4 ==> creditAppId not found!");
						continue;
					}

					updateCreditAppLmsWithCardId(accountLink.getCardId() + "", creditAppId);
				}

				createcardDTO.getMessageLog().setResponsePayloadId(accountLink.getCardId() + "");
				createcardDTO.getMessageLog().setResponseErrorDesc("SUCCESS!");
				createcardDTO.getMessageLog().setMsgStatus(MsgStatus.SUCCESS.value());
				createcardDTO.setMessageTaskStatus(MsgStatus.SUCCESS);
			}
		}
	}

	public void updateCreditAppLmsWithCardId(String cardId, Long creditAppId) throws Exception {

		UnitOfWorkCredit uok = new UnitOfWorkCredit();

		try {
			if (StringUtils.isNullOrEmpty(cardId))
				throw new Exception("GetIssueCardOnWay4.updateCreditAppLmsWithCardId() ==> cardNumber is null!");

			if (creditAppId == null || creditAppId <= 0)
				throw new Exception("GetIssueCardOnWay4.updateCreditAppLmsWithCardId() ==> creditAppId is null!");

			uok.start();

			new CreditAggregate(uok).updateCreditAppLmsWithCardId(cardId, creditAppId);

			uok.commit();

		} catch (Exception ex) {

			System.out.println(
					"[Cron Job] GetIssueCardOnWay4.updateCreditAppLmsWithCardNumber() ==> ex: " + ex.toString());

			uok.rollback();

			throw new Exception(ex.getMessage());
		}
	}

	public void createMessgeLog(String coreCustCode, MessageLog oldMsg, String issueId, String cardId)
			throws Exception {

		MessageLog msg = new MessageLog();

		UnitOfWorkCommon uok = new UnitOfWorkCommon();
		uok.start();

		MessageAggregate ma = new MessageAggregate(uok);

		JSONObject request = new JSONObject();
		request.append("coreCustCode", coreCustCode);
		request.append("cardIdNumber", cardId);
		request.append("issueId", issueId);

		msg.setRelationId(oldMsg.getRelationId());
		msg.setTaskId(oldMsg.getTaskId());
		msg.setFromChannel(MsgChannel.MC_PORTAL_APPLICATION.value());
		msg.setMsgOrder(MsgOrder.FOUR.value());
		msg.setMsgType(MsgType.REQUEST.value());
		msg.setServiceName(BusinessConstant.SERVICE_CREATE_ACCOUNT_LINK);
		msg.setToChannel(MsgChannel.BPM.value());
		msg.setMsgType(oldMsg.getMsgType());
		msg.setTransType(oldMsg.getTransType());
		msg.setMsgStatus(MsgStatus.NEW.value());

		try {

			MessageTask msgTask = ma.getUokCommom().messageTaskRepo().getMessageTaskBy(oldMsg.getTaskId());
			if (msgTask != null) {
				msgTask.setStatus(MsgStatus.NEW.value());
				ma.getUokCommom().messageTaskRepo().update(msgTask);
			}
			ma.getUokCommom().messageLogRepo().upsert(msg);

			uok.commit();
		} catch (Exception ex) {
			System.out.println("[Cron Job] GetIssueCardOnWay4.createMessgeLog() ==> ex: " + ex.toString());

			uok.rollback();

			throw new Exception(ex.getMessage());
		} finally {
			if (ma != null)
				ma.close();
		}
	}

	/*
	 * public void createMessgeLog(String coreCustCode, MessageLog oldMsg, String
	 * issueId, String cardId) { MessageLog msg = new MessageLog(); UnitOfWorkCommon
	 * uok = new UnitOfWorkCommon();
	 * 
	 * JSONObject request = new JSONObject(); request.append("coreCustCode",
	 * coreCustCode); request.append("cardIdNumber", cardId);
	 * request.append("issueId", issueId);
	 * 
	 * msg.setRelationId(oldMsg.getRelationId()); msg.setTaskId(oldMsg.getTaskId());
	 * msg.setFromChannel(MsgChannel.MC_PORTAL_APPLICATION.value());
	 * msg.setMsgOrder(MsgOrder.FOUR.value());
	 * msg.setMsgType(MsgType.REQUEST.value());
	 * msg.setServiceName(BusinessConstant.SERVICE_CREATE_ACCOUNT_LINK);
	 * msg.setToChannel(MsgChannel.BPM.value());
	 * msg.setMsgType(oldMsg.getMsgType()); msg.setTransType(oldMsg.getTransType());
	 * msg.setMsgStatus(MsgStatus.NEW.value());
	 * 
	 * MessageTask msgTask =
	 * uok.messageTaskRepository().getMessageTaskBy(oldMsg.getTaskId());
	 * msgTask.setStatus(MsgStatus.NEW.value());
	 * 
	 * try { uok.start(); uok.messageTaskRepository().update(msgTask);
	 * uok.messageLogRepository().upsert(msg); uok.commit(); } catch (Exception e) {
	 * uok.rollback(); } }
	 */

	public boolean createAccountLinkWithCardId(CustomerDTO request) throws IOException {
		boolean status = false;
		CustomerManager manager = null;
		try {

			manager = new CustomerManager();

			manager.upsertAccountLink(request, null);

			status = true;

			if (manager != null)
				manager.close();
		} catch (Exception ex) {
			System.out.println("[Cron Job] GetIssueCardOnWay4.createAccountLinkWithCardId.Ex: " + ex.toString());
			if (manager != null)
				manager.close();
		}
		return status;
	}

	public Long getAppIdByRelationIdMessageLog(String relationId) throws IOException {
		Long appId = null;
		CustomerManager manager = null;
		try {

			manager = new CustomerManager();

			IdDTO idDto = manager.getAppIdByRelationIdMessageLog(relationId);
			if (idDto != null && idDto.getId() != null && idDto.getId() > 0)
				appId = idDto.getId();

			if (manager != null)
				manager.close();
		} catch (Exception ex) {
			System.out.println("[Cron Job] GetIssueCardOnWay4.getAppIdByRelationIdMessageLog.Ex: " + ex.toString());
			if (manager != null)
				manager.close();
		}
		return appId;
	}

	public Long findCustIdByRelationIdMessageLog(String relationId) throws IOException {
		Long custId = null;
		CustomerManager manager = null;
		try {

			manager = new CustomerManager();

			IdDTO idDto = manager.getCustIdByRelationIdMessageLog(relationId);
			if (idDto != null && idDto.getId() != null && idDto.getId() > 0)
				custId = idDto.getId();

			if (manager != null)
				manager.close();
		} catch (Exception ex) {
			System.out.println("[Cron Job] GetIssueCardOnWay4.findCustIdByRelationIdMessageLog.Ex: " + ex.toString());
			if (manager != null)
				manager.close();
		}
		return custId;
	}

	/*
	 * public static void main(String[] args) throws Exception {
	 * 
	 * CreateCardDTO createcardDTO = new CreateCardDTO(); MessageLog mslg = new
	 * MessageLog(); mslg.setRelationId("546546"); mslg.setMsgRequest(
	 * "{\"RequestId\":\"\",\"BatchId\":\"\",\"IssueId\":\"64448\",\"DateInput\":\"\"}"
	 * ); createcardDTO.setMessageLog(mslg);
	 * 
	 * GetIssueCardOnWay4 cs = new GetIssueCardOnWay4(); cs.execute(createcardDTO);
	 * System.exit(0); }
	 */
}
