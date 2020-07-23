package com.mcredit.data.user.repository;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.exception.NoRecordFoundException;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.data.common.entity.AllocationDetail;
import com.mcredit.data.user.entity.Users;
import com.mcredit.data.user.entity.UsersSession;

public class UserSessionRepository extends BaseRepository implements
		IUpsertRepository<UsersSession> {

	public UserSessionRepository(Session session) {
		super(session);
	}

	public void upsert(UsersSession item) {
		 StoredProcedureQuery query = this.session.createStoredProcedureCall("USERS_SESSION_UPSERT")
				 .registerStoredProcedureParameter(1, String.class,ParameterMode.IN).setParameter(1, item.getLoginId())
				 .registerStoredProcedureParameter(2, String.class,ParameterMode.IN).setParameter(2, item.getSessionKey());
	        query.execute();
	}

	@SuppressWarnings({ "rawtypes" })
	public Users findUserInfoBy(String loginId) throws NoRecordFoundException {
	
		NativeQuery query = this.session.getNamedNativeQuery("findUserByLoginID").setParameter("loginid", loginId);

		Object obj = null;
		try {
			obj = query.getSingleResult();
		}
		catch (NoResultException ex) {
			throw new NoRecordFoundException("Session Key not found!");
		}
	
		return obj != null ? transformObject(obj, Users.class) : null;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public Users findUserSessionBySessionKey(String token) throws NoRecordFoundException {
	
		NativeQuery query = this.session.getNamedNativeQuery("findUserBySessionKey").setParameter("SESSION_KEY", token);

		Object obj = null;
		try {
			obj = query.getSingleResult();
		}
		catch (NoResultException ex) {
			throw new NoRecordFoundException("Session Key not found!");
		}
	
		return obj != null ? transformObject(obj, Users.class) : null;
	}
	
	public UsersSession findBy(String loginId) throws NoRecordFoundException {
			
		UsersSession us = this.session.createQuery("Select * from USERS_SESSION where LOGIN_ID = :loginId", UsersSession.class).setHibernateFlushMode(FlushMode.ALWAYS).setParameter("loginId", loginId).getSingleResult();
		
		return us;
	}

	@SuppressWarnings("unchecked")
	public List<AllocationDetail> findAllocationDetail() {
		return this.session.getNamedQuery("findAllocationDetail").list();
	}

}
