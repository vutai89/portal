package com.mcredit.alfresco.server;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;

import com.mcredit.alfresco.utils.Constants;

public class AlfrescoECMServerSession {

	static Logger logger = Logger.getLogger(AlfrescoECMServerSession.class.getName());
	private AlfrescoECMSessionPool pool;
	private AtomicInteger count = new AtomicInteger(0);

	private void setUp() {
		try {
			GenericObjectPoolConfig config = new GenericObjectPoolConfig();
			config.setMaxIdle(Constants.session_pool_maxIdle);
			config.setMaxTotal(Constants.session_pool_maxTotal);
			config.setTestOnBorrow(true);
			config.setTestOnReturn(true);
			pool = new AlfrescoECMSessionPool(new AlfrescoECMSessionFactory(), config);
		} catch (Exception e) {
		}

	}

	private static volatile AlfrescoECMServerSession instance = null;

	private AlfrescoECMServerSession() {
		//instance.setUp();
		setUp();
	};

	public static AlfrescoECMServerSession getInstance() {
		if (instance == null) {
			synchronized (AlfrescoECMServerSession.class) {
				if (instance == null) {
					instance = new AlfrescoECMServerSession();
				}
			}
		}
		return instance;
	}

	public Session getSession() {
		Session session = null;
		try {
			logger.info("***** get session *****");
			session = pool.borrowObject();
			session.getDefaultContext().setCacheEnabled(false);
			count.getAndIncrement();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} 
		return session;
	}

	public void returnSession(Session session) {
		logger.info("***** starting return session *****");
		if (session != null) {
			pool.returnObject(session);
		}
	}
}
