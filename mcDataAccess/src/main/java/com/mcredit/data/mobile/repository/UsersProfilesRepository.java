package com.mcredit.data.mobile.repository;

import java.math.BigDecimal;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.mobile.entity.UsersProfiles;
import com.mcredit.data.repository.IRepository;

public class UsersProfilesRepository extends BaseRepository implements IRepository<UsersProfiles> {
	public UsersProfilesRepository(Session session) {
		super(session);
	}
	
	public void update(UsersProfiles item) {
		this.session.update(item);
	}
	
	public void upsert(UsersProfiles item) {
		this.session.saveOrUpdate("UsersProfiles", item);
	}
	
	public UsersProfiles getByUserId(Long userId) {
		return (UsersProfiles) this.session
				.getNamedNativeQuery("getByUserId")
				.setParameter("userId", userId).addEntity(UsersProfiles.class).uniqueResult();
	}
	
	public String getImei(String usrFullName) {
		return (String) this.session.getNamedNativeQuery("getImei").setParameter("login_id", usrFullName).uniqueResult();
	}

	public int updateNotifiCationId(String notificationId, Long userId) {
		return this.session.getNamedNativeQuery("updateNotificationId").setParameter("notificationId", notificationId).setParameter("userId", userId)
				.executeUpdate();
		
	}
	
	public int updateDeviceName(String deviceName, Long userId) {
		String sqlString = "UPDATE users_profiles " + 
				" set " + 
				"    device_name = :deviceName " + 
				" where " + 
				"    user_id = :userId ";
		
		@SuppressWarnings("rawtypes")
		NativeQuery query = this.session.createNativeQuery(sqlString)
		.setParameter("deviceName", deviceName)
		.setParameter("userId", userId);
		
		return query.executeUpdate();
	}
	
	public Boolean checkImei(String imei, Long userId) {
		return  (((BigDecimal) this.session.getNamedNativeQuery("checkImei").setParameter("userId", userId).setParameter("imei", imei).getSingleResult()).intValue()) > 0 ? true:false;
	}

}
