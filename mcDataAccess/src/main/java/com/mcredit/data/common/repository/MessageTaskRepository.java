package com.mcredit.data.common.repository;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.common.entity.MessageTask;
import com.mcredit.data.repository.IUpdateRepository;

public class MessageTaskRepository extends BaseRepository implements IUpdateRepository<MessageTask> {

	public MessageTaskRepository(Session session) {
		super(session);
	}

	public void update(MessageTask item) {
		this.session.update(item);
	}

	public void upsert(MessageTask item) {
		this.session.saveOrUpdate("MessageTask", item);
	}

	public void remove(MessageTask item) {
		this.session.delete("MessageTask", item);
	}

	@SuppressWarnings("unchecked")
	public List<MessageTask> findPostingConfiguration(String relationId) {
		return this.session.getNamedQuery("findMessageTaskByRelationId").setParameter("relationId", relationId).list();
	}

	public List<MessageTask> getMessageTaskBy(String messageType, String[] msgStatus) {
		Query<MessageTask> query = this.session .createQuery("from MessageTask where transType=:messageType and status in :msgStatus", MessageTask.class)
				.setParameter("messageType", messageType)
				.setParameter("msgStatus", Arrays.asList(msgStatus));

		return query.list();
	}
	
	public MessageTask getMessageTaskBy(Long id) {
		Query<MessageTask> query = this.session.createQuery("from MessageTask where id = :id",MessageTask.class)
				.setParameter("id", id);
	
		return query.uniqueResult();
	}
	
	
}


