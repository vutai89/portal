package com.mcredit.model.object.audit;

public class DynamicFile {
	private String fileName;
	private String config;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	public DynamicFile(String fileName, String config) {
		super();
		this.fileName = fileName;
		this.config = config;
	}
	public DynamicFile() {
		super();
	}
	
}
