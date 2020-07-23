package com.mcredit.model.object.warehouse;

import java.util.List;

public class ResultsComtractDocumentInfo {

    public String scanId;
    public String scanName;
    public String urlDocument;

    List<ResultsDocumentInfo> errorList;

    public ResultsComtractDocumentInfo(List<ResultsDocumentInfo> errorList) {
        super();
        this.errorList = errorList;
    }

    public String getScanId() {
        return scanId;
    }

    public void setScanId(String scanId) {
        this.scanId = scanId;
    }

    public String getScanName() {
        return scanName;
    }

    public void setScanName(String scanName) {
        this.scanName = scanName;
    }

    public String getUrlDocument() {
        return urlDocument;
    }

    public void setUrlDocument(String urlDocument) {
        this.urlDocument = urlDocument;
    }

    public List<ResultsDocumentInfo> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<ResultsDocumentInfo> errorList) {
        this.errorList = errorList;
    }

}
