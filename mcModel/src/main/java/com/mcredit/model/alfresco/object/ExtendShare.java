package com.mcredit.model.alfresco.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExtendShare {

	@SerializedName("nodeId")
	@Expose
	public String nodeId;
	@SerializedName("expiresAt")
	@Expose
	public String expiresAt;

	public ExtendShare(String nodeId, String expiresAt) {
		super();
		this.nodeId = nodeId;
		this.expiresAt = expiresAt;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(String expiresAt) {
		this.expiresAt = expiresAt;
	}

}
