package com.mcredit.data.common.repository;

import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.common.entity.NotificationTemplate;
import com.mcredit.data.repository.IUpsertRepository;

public class NotificationTemplateRepository extends BaseRepository implements IUpsertRepository<NotificationTemplate> {
	public NotificationTemplateRepository(Session session) {
		super(session);
	}
	
	public void remove(NotificationTemplate item) {
		this.session.delete("NotificationTemplate", item);
	}

	public void upsert(NotificationTemplate item) {
		this.session.saveOrUpdate("NotificationTemplate", item);
	}
	
//	public void update(NotificationTemplate item) {
//		this.session.update(item);
//	}
	
	public NotificationTemplate findByCode(String Code)
	{
		return (NotificationTemplate) this.session.createNamedQuery("findNotificationTemplateByCode").setParameter("code", Code).getSingleResult();	
	}
	
	public List<NotificationTemplate> findActiveNotificationTemp(){
		return this.session.createQuery("from NotificationTemplate where status = 'A'", NotificationTemplate.class).getResultList();
	}
}
