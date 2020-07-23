package com.mcredit.business.job;

import java.io.Closeable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.mcredit.business.common.MessageAggregate;
import com.mcredit.business.common.factory.MessageFactory;
import com.mcredit.business.common.service.MessageService;
import com.mcredit.business.job.dto.JobDTO;
import com.mcredit.business.job.dto.JobRespondDTO;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.common.entity.MessageTask;
import com.mcredit.data.factory.UnitOfWorkFactory;
import com.mcredit.model.enums.MsgOrder;
import com.mcredit.model.enums.MsgStatus;
import com.mcredit.model.enums.MsgTransType;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.SessionType;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.util.JSONConverter;

public abstract class JobManagerBase implements Closeable {
	protected UnitOfWork uok = UnitOfWorkFactory.getInstance(SessionType.JTA);
	private MessageService _messageService = null;
	private int retryLimit = BusinessConstant.JOB_MAX_RETRY;
	private int limit = Integer.parseInt(CacheManager.Parameters().findParamValueAsString(ParametersName.SMS_RECORDS_LIMIT));
	private JobDTO jobDTO;
	private MessageAggregate messageAggregate = null;
	protected MsgTransType TRANS_TYPE_FILTER = null;
	protected MsgOrder[] msg_order;
	protected CallBack[] stepList;
	protected CallBack stepSingle;
	protected String[] messageTypeFilter = null;
	private JobRespondDTO jobRespond = new JobRespondDTO();

	public JobManagerBase() {
		// uokCommon = new UnitOfWorkCommon();
	}

	private void registerMessage(CallBack callback, JobDTO jobDTO) throws Exception {
		callback.execute(jobDTO);
	}

	private boolean transformMessage(CallBack callback, JobDTO jobDTO, List<MessageLog> messageLogList) {
		if (callback == null)
			return false;
		callback.transformMessage(jobDTO, messageLogList);
		if (jobDTO.getMessageLog() == null)
			return false;
		return true;
	}

	private List<MessageTask> getProcessingTasks() throws Exception {
		List<MessageTask> messageTasks = null;

		try {
			messageTasks = _messageService.findMessageTaskBy(TRANS_TYPE_FILTER,
					new String[] { MsgStatus.NEW.value(), MsgStatus.ERROR.value() });
			if (messageTasks == null || messageTasks.size() == 0) {
				uok.common.commit();
				return null;
			}

			// MARK ALL FETCHED MESSAGE TASK AS PROCESSING
			for (MessageTask messageTask : messageTasks) {
				messageTask.setStatus(MsgStatus.PROCESSING.value());
			}
			_messageService.updateBatchMessageTask(messageTasks);
			uok.common.commit();
		} catch (Exception e) {
			uok.common.rollback();
		}
		return messageTasks;

	}

	private int loadCheckPointStep(List<MessageLog> messages, MsgOrder[] loadOrder) {
		int i = 0;
		int initStep = loadOrder.length;
		for (MessageLog message : messages) {
			MsgStatus _msgStatus = MsgStatus.fromString(message.getMsgStatus());

			if (_msgStatus == MsgStatus.ERROR || _msgStatus == MsgStatus.NEW || _msgStatus == MsgStatus.TIME_OUT) {
				initStep = i;
				break;
			}
			i++;
		}
		return initStep;
	}

	private JobDTO loadCheckPoint(MessageTask messageTask) {
		JobDTO jobDTO01 = JSONConverter.toObject(messageTask.getDtoObject(), JobDTO.class);
		if (jobDTO01 == null)
			jobDTO01 = new JobDTO();
		return jobDTO01;

	}

	private void markRemainMessageAsError(List<MessageLog> messages, int i) {
		int j = i;
		while (messages.size() > j) {
			if (messages.get(j).getMsgOrder() != MsgOrder.NINETY_NINE.value())
				messages.get(j).setMsgStatus(MsgStatus.ERROR.value());
			j++;
		}
	}

	public Object runJob() throws Exception {
		// DEFINE JOBs PARAMETERS
		List<MessageLog> processingMessage;

		// INIT MESSAGES TASK
		uok.common.start();
		_messageService = MessageFactory.getMessageServiceInstance(uok.common);

		List<MessageTask> messageTasks = this.getProcessingTasks();

		if (messageTasks == null)
			return jobRespond;
		jobRespond.setFetch(messageTasks.size());
		for (MessageTask messageTask : messageTasks) {
			try {
				// INITIATE VARIABLES
				uok.common.reset();
				uok.common.start();
				_messageService = MessageFactory.getMessageServiceInstance(uok.common);
				messageAggregate = MessageFactory.getMessageAggregateInstance(uok.common);

				// BYPASS MAX RETRIED MESSAGE TASK AND MARK THEM AS IGNORED
				if (messageTask.getNumRetry() != null && messageTask.getNumRetry() >= retryLimit) {
					messageTask.setStatus(MsgStatus.IGNORE.value());
					uok.common.messageTaskRepo().update(messageTask);
					uok.common.commit();
					jobRespond.setIgnore(jobRespond.getIgnore() + 1);
					continue;
				}

				// LOAD MESSAGE LOGS
//				processingMessage = messageAggregate.findMessageLogBy(messageTask.getId());
				processingMessage = messageAggregate.findMessageLogByTaskIdAndRelationId(messageTask.getId(), messageTask.getRelationId());

				// BY PASS EMPTY MESSAGE TASK AND MARK THEM AS IGNORED
				if (processingMessage == null || processingMessage.size() == 0) {
					jobRespond.setError(jobRespond.getError() + 1);
					messageTask.setStatus(MsgStatus.ERROR.value());
					uok.common.commit();
					continue;
				}

				// LOAD JOB-DTO AT PREVIOUS CHECKPOINT
				jobDTO = loadCheckPoint(messageTask);

				int i = 0;
				try {
					for (i = loadCheckPointStep(processingMessage, msg_order); i < msg_order.length; i++) {
						jobDTO.setMessageTaskStatus(MsgStatus.fromString(messageTask.getStatus()));
						// RUN INIT MESSAGE FOR EXECUTION
						if (!transformMessage(stepList[i], jobDTO, processingMessage))
							throw new Exception("Error transform message");

						registerMessage(stepList[i], jobDTO);

						// CREATE CHECKPOINT
						messageTask.setStatus(jobDTO.getMessageTaskStatus().value());
						messageTask.setDtoObject(new JSONObject(jobDTO).toString());
					}
					jobRespond.setSuccess(jobRespond.getSuccess() + 1);
					messageTask.setStatus(MsgStatus.SUCCESS.value());
				} catch (Exception ex) {
					jobRespond.setError(jobRespond.getError() + 1);
					if (jobDTO.getMessageLog() != null)
						jobDTO.getMessageLog().setResponseErrorDesc(ex.getMessage());
					markRemainMessageAsError(processingMessage, i);

					// CREATE CHECKPOINT
					if (jobDTO != null) {
						JSONObject jsonObj = new JSONObject(jobDTO);
						messageTask.setDtoObject(jsonObj.toString());
					}
					messageTask.setStatus(MsgStatus.ERROR.value());
					messageTask.setNumRetry(messageTask.getNumRetry() != null ? messageTask.getNumRetry() + 1 : 1);

				}
				// SAVE STATE
				messageAggregate.upsertBatchMessageLog();
				messageAggregate.setMessageTask(messageTask);
				messageAggregate.updateMessageTask();

				uok.common.commit();
			} catch (Exception ex) {
				throw new Exception("Run job error: " + ex.getMessage());
			}
		}
		return jobRespond;
	}

	public Object runJobSingle() throws Exception {
		if (messageTypeFilter == null) {
			messageTypeFilter = new String[] { MsgStatus.NEW.value(), MsgStatus.ERROR.value() };
		}

		JobDTO jobDTO = new JobDTO();
		JobRespondDTO result = new JobRespondDTO();
		try {
			uok.common.start();

			_messageService = MessageFactory.getMessageServiceInstance(uok.common);
			List<MessageLog> messageLogs = _messageService.findMessageForSendSMS(TRANS_TYPE_FILTER.toString(), messageTypeFilter, limit);
			result.setFetch(messageLogs.size());
			for (MessageLog message : messageLogs) {
//				message.setMsgStatus(MsgStatus.PROCESSING.value());
				message.setProcessTime(new Timestamp(new Date().getTime()));
				_messageService.updateMessageLog(message);
			}
			uok.common.commit();

			for (MessageLog message : messageLogs) {
				uok.common.start();
				try {
					_messageService = MessageFactory.getMessageServiceInstance(uok.common);

					jobDTO.setMessageLog(message);
					stepSingle.execute(jobDTO);
					_messageService.updateMessageLog(jobDTO.getMessageLog());
					result.setSuccess(result.getSuccess() + 1);
				} catch (Exception e) {
					jobDTO.getMessageLog().setRequestTime(new Timestamp(new Date().getTime()));
					jobDTO.getMessageLog().setMsgStatus(MsgStatus.ERROR.value());

					_messageService.updateMessageLog(jobDTO.getMessageLog());
					result.setError(result.getError() + 1);
				}
				uok.common.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
			uok.common.rollback();
		}

		return result;
	}

}
