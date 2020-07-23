package com.mcredit.business.common;

import java.io.Closeable;
import java.util.List;

import com.mcredit.data.common.UnitOfWorkCommon;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.common.entity.MessageTask;

public class MessageAggregate implements Closeable {
	private UnitOfWorkCommon uokCommon = null;

	private List<MessageLog> messageLogs;
	private MessageTask messageTask;

	public UnitOfWorkCommon getUokCommom() {
		return uokCommon;
	}

	public void setUokCommom(UnitOfWorkCommon uokCommon) {
		this.uokCommon = uokCommon;
	}

	public List<MessageLog> getMessageLog() {
		return messageLogs;
	}

	public void setMessageLog(List<MessageLog> messageLogs) {
		this.messageLogs = messageLogs;
	}

	public MessageTask getMessageTask() {
		return messageTask;
	}

	public void setMessageTask(MessageTask messageTask) {
		this.messageTask = messageTask;
	}

	public MessageAggregate(UnitOfWorkCommon uok) {
		this.uokCommon = uok;
	}

	public MessageAggregate(UnitOfWorkCommon uok, Long taskId) {
		this.uokCommon = uok;
		this.messageTask = uokCommon.messageTaskRepo().getMessageTaskBy(taskId);
	}
	
	public MessageAggregate(UnitOfWorkCommon uok, MessageTask messageTask) {
		this.uokCommon = uok;
		this.messageTask = messageTask;
	}

	public void updateMessageTask() {
		this.uokCommon.messageTaskRepo().upsert(this.messageTask);
	}

	public List<MessageLog> findMessageLogBy(Long taskId) {
		this.messageLogs = this.uokCommon.messageLogRepo().getMessageBy(
				taskId);
		return this.messageLogs;
	}

	public void upsertBatchMessageLog()
			throws Exception {
		if (messageLogs == null || messageLogs.size() == 0)
			throw new Exception("Object messageLogs is null!");

		for (MessageLog messageLog : messageLogs) {
			uokCommon.messageLogRepo().upsert(messageLog);
		}
	}
	
	public void upsertMessageTask() throws Exception {
		if (this.messageTask == null)
			throw new Exception("Object messageTask is null!");

		/*Long result = uokCommon.messageTaskRepository().findByRelationId(this.messageTask.getRelationId());
		if (result == null || result < 1) {
			if (this.messageTask.getId() == null || this.messageTask.getId() == 0)
				this.messageTask.setId(uokCommon.messageTaskRepository().getMessageTaskId());*/
			
			uokCommon.messageTaskRepo().upsert(this.messageTask);
		/*}*/

	}

	public void close() {
		if (uokCommon != null)
			uokCommon = null;
	}
	
	/**
	 * Get messageLog: create_payment
	 * @param taskId
	 * @param relationId
	 * @return
	 */
	public List<MessageLog> findMessageLogByTaskIdAndRelationId(Long taskId, String relationId) {
		this.messageLogs = this.uokCommon.messageLogRepo().getMessageByTaskIdAndRelationId(taskId, relationId);
		return this.messageLogs;
	}
}
