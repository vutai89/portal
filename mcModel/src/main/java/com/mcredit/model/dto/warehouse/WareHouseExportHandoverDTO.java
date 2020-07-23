/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcredit.model.dto.warehouse;

import java.util.List;

public class WareHouseExportHandoverDTO {
    private String executioner;
    private String receiver;
    private String accountExcutioner;
    private String accountReceiver;
    private List<WareHouseDocumentHandover> lstHandover;

    public String getExecutioner() {
        return executioner;
    }

    public void setExecutioner(String executioner) {
        this.executioner = executioner;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getAccountExcutioner() {
        return accountExcutioner;
    }

    public void setAccountExcutioner(String accountExcutioner) {
        this.accountExcutioner = accountExcutioner;
    }

    public String getAccountReceiver() {
        return accountReceiver;
    }

    public void setAccountReceiver(String accountReceiver) {
        this.accountReceiver = accountReceiver;
    }

    public List<WareHouseDocumentHandover> getLstHandover() {
        return lstHandover;
    }

    public void setLstHandover(List<WareHouseDocumentHandover> lstHandover) {
        this.lstHandover = lstHandover;
    }    
    
}
