package com.mcredit.data.factory;

import org.hibernate.Session;

import com.mcredit.data.HibernateBase;
import com.mcredit.data.UnitOfWork;
import com.mcredit.model.enums.SessionType;


public class UnitOfWorkFactory {
	
	public static UnitOfWork getInstance(SessionType type){
		UnitOfWork uok = null;
		
		if(type == SessionType.THREAD)
			uok = new UnitOfWork();//init Thread nhibernate session by using call getCurrentSession
		else {
			HibernateBase hibernateBase = new HibernateBase(type);//init nhibernate session depending on SessionType
			Session session = hibernateBase.getSession();
			uok = new UnitOfWork(hibernateBase,session);
		}
		return uok;
	}
}
