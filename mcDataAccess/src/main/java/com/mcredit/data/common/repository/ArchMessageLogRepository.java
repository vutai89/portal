package com.mcredit.data.common.repository;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.common.entity.ArchMessageLog;
import com.mcredit.data.repository.IRepository;

public class ArchMessageLogRepository extends BaseRepository implements IRepository<ArchMessageLog> {
	public ArchMessageLogRepository(Session session) {
		super(session);
	}

	public void add(ArchMessageLog item) {
		this.session.save(item);
	}

	public void update(ArchMessageLog item) {
		this.session.update(item);
	}

	public void upsert(ArchMessageLog item) {
		this.session.saveOrUpdate("ArchMessageLog", item);
	}

	public void remove(ArchMessageLog item) {
		this.session.delete("ArchMessageLog", item);
	}

//	public long getMessageLogTransId() {
//		Query query = this.session.getNamedNativeQuery("Seq_message_log_Trans_ID");
//		BigDecimal a = (BigDecimal) query.uniqueResult();
//		return a.longValue();
//	}
//
//	public List<MessageLog> getMessageBy(Long taskId) {
//		Query<MessageLog> query = this.session
//				.createQuery("from MessageLog WHERE taskId=:taskId ORDER BY msgOrder", MessageLog.class)
//				.setParameter("taskId", taskId);
//
//		List<MessageLog> messages = query.list();
//		return messages;
//	}
	
	
	public List<ArchMessageLog> getArchMessageLogFirstByTransId(Long transId,String toChannel, String transType) {	
		Query<ArchMessageLog> query = this.session.createQuery(
				"from ArchMessageLog WHERE trans_id = :transId and trans_type =: transType and to_channel =:toChannel order by response_time",
				ArchMessageLog.class).
				setParameter("transId", transId).setParameter("transType", transType).setParameter("toChannel", toChannel);
		List<ArchMessageLog> messages = query.list();
		return messages;		
	}
	
	
}
