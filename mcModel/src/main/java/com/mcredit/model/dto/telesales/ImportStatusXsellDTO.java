package com.mcredit.model.dto.telesales;

import java.util.List;

public class ImportStatusXsellDTO {

    List<String> lstIdentityNumberDupFile;
    List<String> lstIdentityNumberDupDB;
    List<String> lstInvalidData;
    

    public List<String> getLstIdentityNumberDupFile() {
        return lstIdentityNumberDupFile;
    }

    public void setLstIdentityNumberDupFile(List<String> lstIdentityNumberDupFile) {
        this.lstIdentityNumberDupFile = lstIdentityNumberDupFile;
    }

    public List<String> getLstIdentityNumberDupDB() {
        return lstIdentityNumberDupDB;
    }

    public void setLstIdentityNumberDupDB(List<String> lstIdentityNumberDupDB) {
        this.lstIdentityNumberDupDB = lstIdentityNumberDupDB;
    }

    public List<String> getLstInvalidData() {
        return lstInvalidData;
    }

    public void setLstInvalidData(List<String> lstInvalidData) {
        this.lstInvalidData = lstInvalidData;
    }
}
