package com.mcredit.model;

public class Config {
	private String[] heathCheckEndPoint;
	private String restartEndPoint;
	private String name;
	private String buildStatusEndPoint;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getHeathCheckEndPoint() {
		return heathCheckEndPoint;
	}

	public void setHeathCheckEndPoint(String[] heathCheckEndPoint) {
		this.heathCheckEndPoint = heathCheckEndPoint;
	}

	public String getRestartEndPoint() {
		return restartEndPoint;
	}

	public void setRestartEndPoint(String restartEndPoint) {
		this.restartEndPoint = restartEndPoint;
	}

	public String getBuildStatusEndPoint() {
		return buildStatusEndPoint;
	}

	public void setBuildStatusEndPoint(String buildStatusEndPoint) {
		this.buildStatusEndPoint = buildStatusEndPoint;
	}

}
