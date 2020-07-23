/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcredit.model.dto.warehouse;

import java.util.List;

public class  WareHouseExportHandoverBorrowedDTO{
    private String accountBorrowed;
    private String appointmentDate;
    private String extensionDate;    
    private String docType;    
    private List<WareHouseDocumentHandoverBorrowed> lstHandoverBrr;

    public String getAccountBorrowed() {
        return accountBorrowed;
    }

    public void setAccountBorrowed(String accountBorrowed) {
        this.accountBorrowed = accountBorrowed;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getExtensionDate() {
        return extensionDate;
    }

    public void setExtensionDate(String extensionDate) {
        this.extensionDate = extensionDate;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public List<WareHouseDocumentHandoverBorrowed> getLstHandoverBrr() {
        return lstHandoverBrr;
    }

    public void setLstHandoverBrr(List<WareHouseDocumentHandoverBorrowed> lstHandoverBrr) {
        this.lstHandoverBrr = lstHandoverBrr;
    }

    
}
