package com.mcredit.model.alfresco.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ACMShareObject {
	@SerializedName("modifiedByUser")
	@Expose
	private ModifiedByUser modifiedByUser;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("id")
	@Expose
	private String id;
	@SerializedName("nodeId")
	@Expose
	private String nodeId;
	@SerializedName("sharedByUser")
	@Expose
	private SharedByUser sharedByUser;
	@SerializedName("content")
	@Expose
	private Content content;

	public ModifiedByUser getModifiedByUser() {
		return modifiedByUser;
	}

	public void setModifiedByUser(ModifiedByUser modifiedByUser) {
		this.modifiedByUser = modifiedByUser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public SharedByUser getSharedByUser() {
		return sharedByUser;
	}

	public void setSharedByUser(SharedByUser sharedByUser) {
		this.sharedByUser = sharedByUser;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}
}
