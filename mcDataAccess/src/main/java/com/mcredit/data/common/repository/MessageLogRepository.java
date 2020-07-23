package com.mcredit.data.common.repository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.repository.IRepository;
import com.mcredit.model.enums.pcb.TransType;
import com.mcredit.model.object.mobile.dto.NotificationDTO;
import com.mcredit.model.object.mobile.dto.SearchNotiDTO;
import com.mcredit.util.StringUtils;

public class MessageLogRepository extends BaseRepository implements IRepository<MessageLog> {

	public MessageLogRepository(Session session) {
		super(session);
	}

	public void add(MessageLog item) {
		this.session.save(item);
	}

	public void update(MessageLog item) {
		this.session.update(item);
	}

	public void upsert(MessageLog item) {
		this.session.saveOrUpdate("MessageLog", item);
	}

	public void remove(MessageLog item) {
		this.session.delete("MessageLog", item);
	}

	public long getMessageLogTransId() {
		Query query = this.session.getNamedNativeQuery("Seq_message_log_Trans_ID");
		BigDecimal a = (BigDecimal) query.uniqueResult();
		return a.longValue();
	}

	public List<MessageLog> getMessageBy(Long taskId) {
		Query<MessageLog> query = this.session
				.createQuery("from MessageLog WHERE taskId=:taskId ORDER BY msgOrder", MessageLog.class)
				.setParameter("taskId", taskId);

		List<MessageLog> messages = query.list();
		return messages;
	}

	public List<MessageLog> getMessageBy(String messageType, String[] msgStatus, int msgOrder) {
		Query<MessageLog> query = this.session.createQuery(
				"from MessageLog WHERE transType = :transType AND msgStatus in :msgStatus AND msgOrder = :msgOrder ORDER BY msgOrder",
				MessageLog.class).
				setParameter("transType", messageType).
				setParameter("msgStatus", Arrays.asList(msgStatus))
				.setParameter("msgOrder", msgOrder);
		List<MessageLog> messages = query.list();
		return messages;
	}
	
	public List<MessageLog> getMessageBy(String messageType, String[] msgStatus) {
		Query<MessageLog> query = this.session.createQuery(
				"from MessageLog WHERE transType = :transType AND msgStatus in :msgStatus ORDER BY msgOrder",
				MessageLog.class).
				setParameter("transType", messageType).
				setParameter("msgStatus", Arrays.asList(msgStatus));
		List<MessageLog> messages = query.list();
		return messages;
	}
	
	public List<MessageLog> getMessageBy(String messageType, Long transId, String[] msgStatus) {
		Query<MessageLog> query = this.session.createQuery(
				"from MessageLog WHERE transType = :transType AND msgStatus in :msgStatus AND transId = :transId",
				MessageLog.class).
				setParameter("transType", messageType).
				setParameter("transId", transId).
				setParameter("msgStatus", Arrays.asList(msgStatus));
		List<MessageLog> messages = query.list();
		return messages;
	}
	
	public List<MessageLog> getMessageForSendSMS(String messageType, String[] msgStatus, int limit) {
		Query<MessageLog> query = this.session.createNativeQuery(
				" select * from message_log " + 
				" where trans_type = :transType " + 
				" AND msg_status IN :msgStatus " + 
				" AND (response_code in (200,201) or response_code IS null) " + 
				" AND request_time >= TRUNC(sysdate)-5 " + 
				" ORDER BY msg_order " + 
				" OFFSET 0 ROWS FETCH NEXT :limit ROWS ONLY ",
				MessageLog.class)
				.setParameter("transType", messageType)
				.setParameter("limit", limit)
				.setParameter("msgStatus", Arrays.asList(msgStatus));
		List<MessageLog> messages = query.list();
		return messages;
	}

	public long getMessageLogId() {
		Query query = this.session.getNamedNativeQuery("Seq_message_log_ID");
		BigDecimal a = (BigDecimal) query.uniqueResult();
		return a.longValue();
	}
	
	public MessageLog getMessageByMessId(Long id) {
		return this.session.find(MessageLog.class, id);
	}
	
	public List<NotificationDTO> getNotifications(SearchNotiDTO searchDTO, String saleCode) {
	
		
		Integer pageNumber = searchDTO.getPageNumber();
		Integer pageSize = searchDTO.getPageSize();
		
		Integer maxNumber = (pageNumber * pageSize) + 1;
		Integer minNumber = ((pageNumber - 1) * pageSize) + 1;
		
		
		String sqlString = "SELECT * FROM (" + 
				"" + 
				"	SELECT a.*, rownum rn FROM (" + 
				"		SELECT " + 
				"           msg.id ," +
				"			to_char(msg.REQUEST_TIME, 'dd/mm/yyyy - hh24:mi') AS created_date, " + 
				"			msg.MSG_REQUEST," + 
				"			uc.last_updated_by," + 
				"			uc.app_number," + 
				"			uc.customer_name," + 
				"			uc.citizen_id," + 
				"			ct_status.description1 AS current_status" + 
				"			" + 
				"		FROM MESSAGE_LOG msg " + 
				"		LEFT JOIN UPL_CREDIT_APP_REQUEST UC ON msg.relation_id = uc.app_id" + 
				"		LEFT JOIN code_table ct_status ON ct_status.id = uc.app_status" + 
				
				"		WHERE msg.TRANS_TYPE = 'notifyLOSApplication' and msg.msg_status in('S', 'E', 'T')  AND uc.SALE_CODE = :saleCode ";

		
		boolean isSearch = !StringUtils.isNullOrEmpty(searchDTO.getKeyword().trim());
		if (isSearch) {
			sqlString += "AND ( LOWER(uc.customer_name) LIKE :keyword OR LOWER(uc.app_number) LIKE :keyword OR  LOWER(uc.citizen_id) LIKE :keyword OR LOWER(ct_status.description1) LIKE :keyword)";
		}
		
		sqlString += " ORDER BY msg.REQUEST_TIME DESC" + 
					 " ) a WHERE rownum < :maxNumber" + 
					 " " + 
					 " ) WHERE rn >= :minNumber";

		NativeQuery query = this.session.createNativeQuery(sqlString);
		query.setParameter("saleCode", saleCode);
		
		if (isSearch) {
			String keywordSearch = "%" + searchDTO.getKeyword().toLowerCase() + "%";
			query.setParameter("keyword", keywordSearch);
		}
		
		query.setParameter("maxNumber", maxNumber);
		query.setParameter("minNumber", minNumber);
		
		List lst = query.list();
		List<NotificationDTO> notifications = transformList(lst, NotificationDTO.class);
		return notifications;

	}
	
	/*
	 * author: truongvd.ho
	 * create_date: 10/01/2020
	 * desc: get list messageLog by app_id
	 * 
	 */	
	
	public List<MessageLog> getMessageLogByIdAndStepBmp(Long id,String appId, String stepBpm) {
		Query<MessageLog> query = this.session.createQuery(
						"from MessageLog WHERE trans_id = :transId and service_name =:serviceName and trans_type =: transType and relation_id =:appId order by response_time desc",
						MessageLog.class).
						setParameter("transId", id).setParameter("serviceName", stepBpm).setParameter("transType", TransType.MC_QUERY.value().toString()).setParameter("appId", appId);
				List<MessageLog> messages = query.list();
				return messages;		
	}
	

	/**
	 * Get message_log: create_payment
	 * @param taskId
	 * @return
	 */
	public List<MessageLog> getMessageByTaskIdAndRelationId(Long taskId, String relationId) {
		Query<MessageLog> query = this.session
				.createQuery("from MessageLog WHERE taskId=:taskId and relationId=:relationId ORDER BY msgOrder", MessageLog.class)
				.setParameter("taskId", taskId)
				.setParameter("relationId", relationId);

		List<MessageLog> messages = query.list();
		return messages;
	}
	
	/*
	 * author: truongvd.ho
	 * create_date: 10/01/2020
	 * desc: get list messageLog by app_id
	 * 
	 */	
	
	public List<MessageLog> getMessageLogByIdAndTransType(Long id,String toChannel, String transType) {
		Query<MessageLog> query = this.session.createQuery(
						"from MessageLog WHERE trans_id = :transId and trans_type =: transType and to_channel =:toChannel order by response_time desc",
						MessageLog.class).
						setParameter("transId", id).setParameter("transType", transType).setParameter("toChannel", toChannel);
				List<MessageLog> messages = query.list();
				return messages;		

	}
	
	/*
	 * author: truongvd.ho
	 * create_date: 16/06/2020
	 * desc: get list messageLog by trans_id	 * 
	 */	
	
	public List<MessageLog> getMessageLogFirstByTransId(Long transId,String toChannel, String transType) {
		Query<MessageLog> query = this.session.createQuery(
						"from MessageLog WHERE trans_id = :transId and trans_type =: transType and to_channel =:toChannel and response_code='200' order by response_time",
						MessageLog.class).
						setParameter("transId", transId).setParameter("transType", transType).setParameter("toChannel", toChannel);
				List<MessageLog> messages = query.list();
				return messages;		

	}

}
