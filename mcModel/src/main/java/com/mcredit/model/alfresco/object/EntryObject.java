
package com.mcredit.model.alfresco.object;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EntryObject {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("expiresAt")
    @Expose
    private String expiresAt;
    @SerializedName("nodeId")
    @Expose
    private String nodeId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("modifiedAt")
    @Expose
    private String modifiedAt;
    @SerializedName("modifiedByUser")
    @Expose
    private ModifiedByUser modifiedByUser;
    @SerializedName("sharedByUser")
    @Expose
    private SharedByUser sharedByUser;
    @SerializedName("content")
    @Expose
    private Content content;
    @SerializedName("allowableOperations")
    @Expose
    private List<String> allowableOperations = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public ModifiedByUser getModifiedByUser() {
        return modifiedByUser;
    }

    public void setModifiedByUser(ModifiedByUser modifiedByUser) {
        this.modifiedByUser = modifiedByUser;
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

    public List<String> getAllowableOperations() {
        return allowableOperations;
    }

    public void setAllowableOperations(List<String> allowableOperations) {
        this.allowableOperations = allowableOperations;
    }

}
