
package com.mcredit.model.alfresco.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Error {

    @SerializedName("errorKey")
    @Expose
    private String errorKey;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("briefSummary")
    @Expose
    private String briefSummary;
    @SerializedName("stackTrace")
    @Expose
    private String stackTrace;
    @SerializedName("descriptionURL")
    @Expose
    private String descriptionURL;

    public String getErrorKey() {
        return errorKey;
    }

    public void setErrorKey(String errorKey) {
        this.errorKey = errorKey;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getBriefSummary() {
        return briefSummary;
    }

    public void setBriefSummary(String briefSummary) {
        this.briefSummary = briefSummary;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getDescriptionURL() {
        return descriptionURL;
    }

    public void setDescriptionURL(String descriptionURL) {
        this.descriptionURL = descriptionURL;
    }

}
