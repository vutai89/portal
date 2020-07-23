package com.mcredit.model.dto.warehouse;

import java.util.List;

public class CavetDTO {

    private Long whDocId;
    private Long version;
    private boolean save;
    private int appendixContract;
    private CavetInfor cavetInfo;
    private CavetInfor appendixInfo;
    private List<ErrorType> errorTypes;

    public Long getWhDocId() {
        return whDocId;
    }

    public void setWhDocId(Long whDocId) {
        this.whDocId = whDocId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }  

    public CavetInfor getCavetInfo() {
        return cavetInfo;
    }

    public void setCavetInfo(CavetInfor cavetInfo) {
        this.cavetInfo = cavetInfo;
    }

    public CavetInfor getAppendixInfo() {
        return appendixInfo;
    }

    public void setAppendixInfo(CavetInfor appendixInfo) {
        this.appendixInfo = appendixInfo;
    }

    public List<ErrorType> getErrorTypes() {
        return errorTypes;
    }

    public void setErrorTypes(List<ErrorType> errorTypes) {
        this.errorTypes = errorTypes;
    }

    public int getAppendixContract() {
        return appendixContract;
    }

    public void setAppendixContract(int appendixContract) {
        this.appendixContract = appendixContract;
    }


}
