
package com.mcredit.model.alfresco.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Content {

    @SerializedName("mimeType")
    @Expose
    private String mimeType;
    @SerializedName("mimeTypeName")
    @Expose
    private String mimeTypeName;
    @SerializedName("sizeInBytes")
    @Expose
    private Integer sizeInBytes;
    @SerializedName("encoding")
    @Expose
    private String encoding;

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeTypeName() {
        return mimeTypeName;
    }

    public void setMimeTypeName(String mimeTypeName) {
        this.mimeTypeName = mimeTypeName;
    }

    public Integer getSizeInBytes() {
        return sizeInBytes;
    }

    public void setSizeInBytes(Integer sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

}
