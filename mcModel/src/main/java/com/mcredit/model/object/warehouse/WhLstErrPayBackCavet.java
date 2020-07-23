package com.mcredit.model.object.warehouse;

import java.util.Date;

public class WhLstErrPayBackCavet {

    public Long id;
    public Long idWhDocument;
    public String errCode;
    public String errNote; 

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getIdWhDocument() {
        return idWhDocument;
    }

    public void setIdWhDocument(Long idWhDocument) {
        this.idWhDocument = idWhDocument;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrNote() {
        return errNote;
    }

    public void setErrNote(String errNote) {
        this.errNote = errNote;
    }

}
