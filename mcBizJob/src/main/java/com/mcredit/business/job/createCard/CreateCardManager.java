package com.mcredit.business.job.createCard;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import org.json.JSONObject;

import com.mcredit.business.common.MessageAggregate;
import com.mcredit.business.common.factory.MessageFactory;
import com.mcredit.business.common.service.MessageService;
import com.mcredit.business.job.CallBack;
import com.mcredit.business.job.createCard.dto.CreateCardDTO;
import com.mcredit.business.job.createCard.dto.CreateCardRespondDTO;
import com.mcredit.data.common.UnitOfWorkCommon;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.common.entity.MessageTask;
import com.mcredit.model.enums.MsgOrder;
import com.mcredit.model.enums.MsgStatus;
import com.mcredit.model.enums.MsgTransType;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.JSONConverter;

public class CreateCardManager implements Closeable {
	private UnitOfWorkCommon uokCommon = null;
	private MessageService _messageService = null;
	private int retryLimit = BusinessConstant.JOB_MAX_RETRY;
	private CreateCardDTO createCardDTO;
	private MessageAggregate messageAggregate = null;

	public CreateCardManager() {
		uokCommon = new UnitOfWorkCommon();
	}

	private void registerMessage(CallBack callback, CreateCardDTO createcardDTO)
			throws Exception {
		callback.execute(createcardDTO);
	}

	private boolean transformMessage(CallBack callback,
			CreateCardDTO createcardDTO, List<MessageLog> messageLogList) {
		if (callback == null)
			return false;
		callback.execute(createcardDTO, messageLogList);
		if (createCardDTO.getMessageLog() == null) return false;
		return true;
	}

	private List<MessageTask> getProcessingTasks() throws Exception {

		List<MessageTask> messageTasks = null;

		try {
			messageTasks = _messageService.findMessageTaskBy(
					MsgTransType.ISSCARD, new String[] { MsgStatus.NEW.value(),
							MsgStatus.ERROR.value() });
			if (messageTasks == null || messageTasks.size() == 0) {
				uokCommon.commit();
				return null;
			}
			// MARK ALL FETCHED MESSAGE TASK AS PROCESSING
			for (MessageTask messageTask : messageTasks) {
				messageTask.setStatus(MsgStatus.PROCESSING.value());
			}
			_messageService.updateBatchMessageTask(messageTasks);
			uokCommon.commit();
		} catch (Exception e) {
			uokCommon.rollback();
		}
		return messageTasks;

	}

	private int loadCheckPointStep(List<MessageLog> messages,
			MsgOrder[] loadOrder) {
		int initStep = loadOrder.length;
		for (MessageLog message : messages) {
			MsgStatus _msgStatus = MsgStatus.fromString(message.getMsgStatus());

			if (_msgStatus == MsgStatus.ERROR || _msgStatus == MsgStatus.NEW
					|| _msgStatus == MsgStatus.TIME_OUT) {
				initStep = message.getMsgOrder() - 1;
				break;
			}
		}
		return initStep;
	}

	private CreateCardDTO loadCheckPoint(MessageTask messageTask){
		CreateCardDTO createCardDTO1 = JSONConverter.toObject(
				messageTask.getDtoObject(), CreateCardDTO.class);
		if (createCardDTO1 == null)
			createCardDTO1 = new CreateCardDTO();
		return createCardDTO1;

	}
	
	private void markRemainMessageAsError(List<MessageLog> messages, int i) {
		int j = i;
		while (messages.size() > j) {
			messages.get(j).setMsgStatus(MsgStatus.ERROR.value());
			j++;
		}
	}

	public Object createCard() throws Exception {
	
		try {
			// DEFINE JOBs PARAMETERS
			MsgOrder[] msg_order = new MsgOrder[] { MsgOrder.ONE, MsgOrder.TWO,
					MsgOrder.FOUR };
			CallBack[] stepList = new CallBack[] { new CreateCustomerOnCore(),
					new CreateIssueCreditCardOnWay4(), new UpdateCaseOnBPM() };
			CallBack[] messageTransformList = new CallBack[] {
					new InitMessageTransform(), new GetCustomerMessageTransform(),
					new UpdateBPMMessageTransform() };
			CreateCardRespondDTO createCardRespond = new CreateCardRespondDTO();
			List<MessageLog> processingMessage;

			// INIT MESSAGES TASK
			uokCommon.start();
			_messageService = MessageFactory.getMessageServiceInstance(uokCommon);

			List<MessageTask> messageTasks = this.getProcessingTasks();

			if (messageTasks == null)
				return createCardRespond;
			createCardRespond.setFetch(messageTasks.size());
			for (MessageTask messageTask : messageTasks) {
				try {
					// INITIATE VARIABLES
					uokCommon.reset();
					uokCommon.start();
					_messageService = MessageFactory
							.getMessageServiceInstance(uokCommon);
					messageAggregate = MessageFactory
							.getMessageAggregateInstance(uokCommon);

					// BYPASS MAX RETRIED MESSAGE TASK AND MARK THEM AS IGNORED
					if (messageTask.getNumRetry() != null
							&& messageTask.getNumRetry() >= retryLimit) {
						messageTask.setStatus(MsgStatus.IGNORE.value());
						uokCommon.messageTaskRepo().update(messageTask);
						uokCommon.commit();
						createCardRespond
								.setIgnore(createCardRespond.getIgnore() + 1);
						continue;
					}

					// LOAD MESSAGE LOGS
					processingMessage = messageAggregate
							.findMessageLogBy(messageTask.getId());

					// BY PASS EMPTY MESSAGE TASK AND MARK THEM AS IGNORED
					if (processingMessage == null || processingMessage.size() == 0) {
						createCardRespond.setError(createCardRespond.getError()+1);
						messageTask.setStatus(MsgStatus.ERROR.value());
						uokCommon.commit();
						continue;
					}

					// LOAD JOB-DTO AT PREVIOUS CHECKPOINT
					createCardDTO = loadCheckPoint(messageTask);

					int i = 0;
					try {
						for (i = loadCheckPointStep(processingMessage, msg_order); i < msg_order.length; i++) {
							createCardDTO.setMessageTaskStatus(MsgStatus
									.fromString(messageTask.getStatus()));
							//RUN INIT MESSAGE FOR EXECUTION
							if (!transformMessage(messageTransformList[i],
									createCardDTO, processingMessage)) break;

							registerMessage(stepList[i], createCardDTO);

							// CREATE CHECKPOINT
							messageTask.setStatus(createCardDTO
									.getMessageTaskStatus().value());
							messageTask.setDtoObject(new JSONObject(createCardDTO)
									.toString());
						}
						createCardRespond
								.setSuccess(createCardRespond.getSuccess() + 1);

					} catch (Exception ex) {
						createCardRespond
								.setError(createCardRespond.getError() + 1);
						createCardDTO.getMessageLog().setResponseErrorDesc(ex.getMessage());
						markRemainMessageAsError(processingMessage, i);

						// CREATE CHECKPOINT
						if (createCardDTO != null) {
							JSONObject jsonObj = new JSONObject(createCardDTO);
							messageTask.setDtoObject(jsonObj.toString());
						}
						messageTask.setStatus(MsgStatus.ERROR.value());
						messageTask
								.setNumRetry(messageTask.getNumRetry() != null ? messageTask
										.getNumRetry() + 1 : 1);

					}
					//SAVE STATE
					messageAggregate.upsertBatchMessageLog();
					messageAggregate.setMessageTask(messageTask);
					messageAggregate.updateMessageTask();

					uokCommon.commit();

				} catch (Exception ex) {
					throw new Exception("Run job error: " + ex.getMessage());
				}
			}
			return createCardRespond;
		} catch (Throwable e) {
			e.printStackTrace();
			uokCommon.rollback();
			throw new ValidationException(e.getMessage());
		}
	}

	public CreateCardRespondDTO checkCard() throws Exception {

		String[] messageTypeFilter = new String[] { MsgStatus.NEW.value(),
				MsgStatus.ERROR.value(), MsgStatus.TIME_OUT.value() };
		MsgOrder msgOrder = MsgOrder.THREE;
		GetIssueCardOnWay4 getCreditCard = new GetIssueCardOnWay4();
		CreateCardDTO createcardDTO = new CreateCardDTO();
		CreateCardRespondDTO result = new CreateCardRespondDTO();
//		result.setStatus(true);
		try {

			uokCommon.start();

			_messageService = MessageFactory
					.getMessageServiceInstance(uokCommon);
			List<MessageLog> messageLogs = _messageService.findMessageLogBy(
					MsgTransType.ISSCARD.toString(), messageTypeFilter,
					msgOrder);
			result.setFetch(messageLogs.size());
			uokCommon.commit();

			for (MessageLog message : messageLogs) {
				try {
					uokCommon.reset();
					uokCommon.start();
					_messageService = MessageFactory
							.getMessageServiceInstance(uokCommon);

					createcardDTO.setMessageLog(message);
					messageAggregate = MessageFactory
							.getMessageAggregateInstance(uokCommon,
									message.getTaskId());
					
					MessageTask msgTask = messageAggregate.getMessageTask();
					getCreditCard.execute(createcardDTO);
					if (msgTask == null) throw new Exception("Message Task not found!");

					_messageService.updateMessageLog(createcardDTO
							.getMessageLog());

					msgTask.setStatus(MsgStatus.SUCCESS.value());
					result.setSuccess(result.getSuccess()+1);

				} catch (Exception e) {

					createcardDTO.getMessageLog().setMsgStatus(MsgStatus.ERROR.value());
					createcardDTO.getMessageLog().setResponseErrorDesc(e.getMessage());
					result.setError(result.getError()+1);
				}
				if (messageAggregate.getMessageTask() != null) messageAggregate.updateMessageTask();
				_messageService.updateMessageLog(createcardDTO
						.getMessageLog());
				uokCommon.commit();

			}
		} catch (Exception e) {
			uokCommon.rollback();
		}

		return result;
	}

	public void close() throws IOException {
	}

	public static void main(final String[] args) throws Exception {
		CreateCardManager manager = new CreateCardManager();
		System.out.println(JSONConverter.toJSON(manager.checkCard()));
		manager.close();
	}

}
