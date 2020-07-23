package com.mcredit.business.audit.config;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mcredit.model.object.audit.DynamicFile;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {
	private String FtpServer;
	private int Port;
	private String Protocol;
	private String Username;
	private String Password;
	private String StoringFolder;
	private List<DynamicFile> files;
	public String getFtpServer() {
		return FtpServer;
	}
	public void setFtpServer(String ftpServer) {
		FtpServer = ftpServer;
	}
	public int getPort() {
		return Port;
	}
	public void setPort(int port) {
		Port = port;
	}
	public String getProtocol() {
		return Protocol;
	}
	public void setProtocol(String protocol) {
		Protocol = protocol;
	}
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getStoringFolder() {
		return StoringFolder;
	}
	public void setStoringFolder(String storingFolder) {
		StoringFolder = storingFolder;
	}

	public List<DynamicFile> getFiles() {
		return files;
	}
	public void setFiles(List<DynamicFile> files) {
		this.files = files;
	}
	public Config(String ftpServer, int port, String protocol, String username, String password, String storingFolder,
			List<DynamicFile> files) {
		super();
		FtpServer = ftpServer;
		Port = port;
		Protocol = protocol;
		Username = username;
		Password = password;
		StoringFolder = storingFolder;
		this.files = files;
	}
	public Config() {
		super();
	}
	
	
}
