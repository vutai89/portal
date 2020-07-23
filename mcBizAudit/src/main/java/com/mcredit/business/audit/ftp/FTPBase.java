package com.mcredit.business.audit.ftp;

import it.sauronsoftware.ftp4j.FTPClient;

import org.apache.commons.net.ftp.FTPSClient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.mcredit.business.audit.config.Config;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.sharedbiz.cache.CacheManager;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FTPBase {

	protected String filePath = CacheManager.Parameters().findParamValueAsString(ParametersName.LOCAL_AUDIT_FILE_ADDRESS).toString();
	protected Config config;
	protected FTPSClient ftpsClient;
	protected FTPClient ftpClient;
	protected Session session;
	protected ChannelSftp sftpClient;

	public FTPBase() {
		super();
	}

	public FTPBase(Config _config) {
		super();

		this.config = _config;
		this.ftpClient = null;
		this.ftpsClient = null;
		this.session = null;
		this.sftpClient = null;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public FTPSClient getFtpsClient() {
		return ftpsClient;
	}

	public void setFtpsClient(FTPSClient ftpsClient) {
		this.ftpsClient = ftpsClient;
	}

	public FTPClient getFtpClient() {
		return ftpClient;
	}

	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public ChannelSftp getSftpClient() {
		return sftpClient;
	}

	public void setSftpClient(ChannelSftp sftpClient) {
		this.sftpClient = sftpClient;
	}

}
