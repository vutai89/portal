package com.mcredit.model.dto.telesales;

import java.io.InputStream;
import java.io.Serializable;

public class UploadFileXsellDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userFileName;
    private InputStream fileContent;
    private String uplType;
    private String dateFrom;
    private String dateTo;    

    public String getUplType() {
        return uplType;
    }

    public void setUplType(String uplType) {
        this.uplType = uplType;
    }

    public String getUserFileName() {
        return userFileName;
    }

    public void setUserFileName(String userFileName) {
        this.userFileName = userFileName;
    }

    public InputStream getFileContent() {
        return fileContent;
    }

    public void setFileContent(InputStream fileContent) {
        this.fileContent = fileContent;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }
    
}
