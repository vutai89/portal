package com.mcredit.alfresco.server;

import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class AlfrescoECMSessionPool extends GenericObjectPool<Session>{

	 public AlfrescoECMSessionPool(PooledObjectFactory<Session> factory) {
	        super(factory);
	    }
	 
	 public AlfrescoECMSessionPool(PooledObjectFactory<Session> factory,
	            GenericObjectPoolConfig config) {
	        super(factory, config);
	    }
}
