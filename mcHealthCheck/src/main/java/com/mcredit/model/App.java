package com.mcredit.model;

public class App {
	private Config[] apps;
	private String log4jPath;

	public String getLog4jPath() {
		return log4jPath;
	}

	public void setLog4jPath(String log4jPath) {
		this.log4jPath = log4jPath;
	}

	public Config[] getApps() {
		return apps;
	}

	public void setApps(Config[] apps) {
		this.apps = apps;
	}

}
