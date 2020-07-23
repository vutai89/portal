package com.mcredit.model.dto.assign;

public class ServiceDTO {
	private Long id;
	private String createdDate;
	private String serviceName;
	private String servicePath;
	private String description;
	private String module;
	private String serviceAccessRight;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServicePath() {
		return servicePath;
	}

	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getServiceAccessRight() {
		return serviceAccessRight;
	}

	public void setServiceAccessRight(String serviceAccessRight) {
		this.serviceAccessRight = serviceAccessRight;
	}

}
