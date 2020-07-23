/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcredit.model.object.transaction;

public class InforSendEmailObject {

    private Long id;
    private String productName;
    private String ticketCode;
    private String statusActive;
    private String statusApp;
    private String createdBy;
    private String effectDate;
    private String email;
    private String emailTeamLead;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getStatusActive() {
        return statusActive;
    }

    public void setStatusActive(String statusActive) {
        this.statusActive = statusActive;
    }

    public String getStatusApp() {
        return statusApp;
    }

    public void setStatusApp(String statusApp) {
        this.statusApp = statusApp;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(String effectDate) {
        this.effectDate = effectDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailTeamLead() {
        return emailTeamLead;
    }

    public void setEmailTeamLead(String emailTeamLead) {
        this.emailTeamLead = emailTeamLead;
    }

}
