package com.mcredit.data;

import org.hibernate.HibernateException;
import org.hibernate.Session;

public class BaseRepository {
	protected Session session;

	public BaseRepository(Session session) {
		this.session = session;
	}
	
	public final Object get(Class<?> entityClass, long id) {
		try {
			return this.session.get(entityClass,new Long(id));
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}
}
