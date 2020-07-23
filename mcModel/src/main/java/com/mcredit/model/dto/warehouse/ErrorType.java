package com.mcredit.model.dto.warehouse;

public class ErrorType {

    private String codeValue;
    private String note;
    private Integer updateRequest;

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getUpdateRequest() {
        return updateRequest;
    }

    public void setUpdateRequest(Integer updateRequest) {
        this.updateRequest = updateRequest;
    }

}
