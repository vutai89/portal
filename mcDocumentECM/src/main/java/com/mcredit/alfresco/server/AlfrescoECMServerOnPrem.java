package com.mcredit.alfresco.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.log4j.Logger;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.mcredit.alfresco.utils.Config;
import com.mcredit.alfresco.utils.Constants;

public class AlfrescoECMServerOnPrem extends AlfrescoECMServerAPI {

	static Logger logger = Logger.getLogger(AlfrescoECMServerOnPrem.class.getName());
	/**
	 * Change these to match your environment
	 */
	public static final String CMIS_URL = "/public/cmis/versions/1.1/atom";

	public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	public static final JsonFactory JSON_FACTORY = new JacksonFactory();

	private HttpRequestFactory requestFactory;

	public String getAtomPubURL(HttpRequestFactory requestFactory) {
		String alfrescoAPIUrl = getAlfrescoAPIUrl();
		String atomPubURL = null;

		try {
			atomPubURL = alfrescoAPIUrl + getHomeNetwork() + CMIS_URL;
		} catch (IOException ioe) {
			logger.error("Warning: Couldn't determine home network, defaulting to -default-");
			atomPubURL = alfrescoAPIUrl + "-default-" + CMIS_URL;
		}

		return atomPubURL;
	}

	/**
	 * Gets a CMIS Session by connecting to the local Alfresco server.
	 *
	 * @return Session
	 * limit verion => vi chua co co che pool session  
	 * => chi gioi han goi. Cac Object khac dung session cua MCreditServerSession.
	 */
	 Session getCmisSession() {
		Session cmisSession = null;
		if (cmisSession == null) {
			// default factory implementation
			SessionFactory factory = SessionFactoryImpl.newInstance();
			Map<String, String> parameter = new HashMap<String, String>();

			// connection settings
			parameter.put(SessionParameter.ATOMPUB_URL, getAtomPubURL(getRequestFactory()));
			parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
			parameter.put(SessionParameter.AUTH_HTTP_BASIC, "true");
			parameter.put(SessionParameter.USER, getUsername());
			parameter.put(SessionParameter.PASSWORD, getPassword());

			List<Repository> repositories = factory.getRepositories(parameter);

			cmisSession = repositories.get(0).createSession();
		}
		return cmisSession;
	}

	/**
	 * Uses basic authentication to create an HTTP request factory.
	 *
	 * @return HttpRequestFactory
	 */
	public HttpRequestFactory getRequestFactory() {
		if (this.requestFactory == null) {
			this.requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
				public void initialize(HttpRequest request) throws IOException {
					request.setParser(new JsonObjectParser(new JacksonFactory()));
					request.getHeaders().setBasicAuthentication(getUsername(), getPassword());
				}
			});
		}
		return this.requestFactory;
	}

	public String getAlfrescoAPIUrl() {
		return Constants.ecmHost + "/api/";
	}

	public String getUsername() {
		return Constants.username;
	}

	public String getPassword() {
		return Constants.password;
	}

	private static volatile AlfrescoECMServerOnPrem instance = null;

	private AlfrescoECMServerOnPrem() {
	};

	public static AlfrescoECMServerOnPrem getInstance() {
		if (instance == null) {
			synchronized (AlfrescoECMServerOnPrem.class) {
				if (instance == null) {
					instance = new AlfrescoECMServerOnPrem();
				}
			}
		}
		return instance;
	}

}
