package com.mcredit.alfresco.server;

import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class AlfrescoECMSessionFactory extends BasePooledObjectFactory<Session>  {

	@Override
	public Session create() throws Exception {
		return AlfrescoECMServerOnPrem.getInstance().getCmisSession();
	}


	@Override
	public PooledObject<Session> wrap(Session arg0) {
		return new DefaultPooledObject<Session>((Session) arg0);
	} 

	 
}
