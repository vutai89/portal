package com.mcredit.sharedbiz.cache;

import java.util.List;

import com.mcredit.common.UnitOfWorkHelper;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.common.entity.NotificationTemplate;
import com.mcredit.data.factory.UnitOfWorkFactory;
import com.mcredit.model.enums.SessionType;

public class NotificationTemplateCacheManager implements IDataCache {

	private List<NotificationTemplate> notificationTemplateList;
	private UnitOfWork uok = null;
	
	private static NotificationTemplateCacheManager instance;
	
	private NotificationTemplateCacheManager() {
		initCache();
	}
	
	public static synchronized NotificationTemplateCacheManager getInstance() {
		if (instance == null) {
			synchronized (NotificationTemplateCacheManager.class) {
				if (null == instance) {
					instance = new NotificationTemplateCacheManager();
				}
			}
		}
		return instance;
	}
	
	@Override
	public void initCache()  {
		uok = UnitOfWorkFactory.getInstance(SessionType.JTA);
		try {
			notificationTemplateList = UnitOfWorkHelper.tryCatch(uok, ()->{
				return this.uok.common.notificationTemplateRepo().findActiveNotificationTemp();
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}

	@Override
	public void refresh() {
		this.initCache();
		
	}
	
	public NotificationTemplate findByNotificationCode(String code){
		if(notificationTemplateList == null){
			initCache();
		}
		NotificationTemplate notificationTemplate =  null;
		for (NotificationTemplate noti: notificationTemplateList) {
			if (noti.getNotificationCode().equals(code)){
				notificationTemplate = noti;
				break;
			}
		}
		
		return notificationTemplate;
	}
	
//	public static void main(String[] arg) {
//		NotificationTemplate nt = NotificationTemplateCacheManager.getInstance().findByNotificationCode("NT-CARD-DUE-SMS");
//		System.out.println("--------------->" +  nt.getNotificationTemplate());
//		
//	}

}
