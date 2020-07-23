package com.mcredit.data.mobile.reporsitorytest;

import java.util.HashSet;
import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.repository.IAddRepository;
import com.mcredit.data.repository.IRepository;

public class MessageLogRepositoryTest extends BaseRepository implements IRepository<MessageLog>{
	
	public MessageLogRepositoryTest(Session session) {
		super(session);
	}
	
	public int remove(HashSet<Long> lstRemoveId) {
		return this.session.getNamedNativeQuery("removeMessageLogId").setParameterList("ids", lstRemoveId.toArray(new Long[0])).executeUpdate();
	}

	public MessageLog getMessageLogByTransId(Long idTrue) {
		return (MessageLog) this.session.getNamedNativeQuery("getMessageLog").setParameter("trans_id", idTrue).addEntity(MessageLog.class).uniqueResult();
	}
	
	public MessageLog getMessageLogNotification(Long id) {
		return (MessageLog) this.session.getNamedNativeQuery("getMessageLogNotification").setParameter("id", id).addEntity(MessageLog.class).uniqueResult();
	}
	
	public int removeById(List<String> reId) {
		return this.session.getNamedNativeQuery("removeMessageLogById").setParameterList("ids", reId).executeUpdate();
	}

	public Long addMessageLog(MessageLog msgLog) {
		return (Long) this.session.save("MessageLog", msgLog);
	}
}
