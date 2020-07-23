/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcredit.model.object.transaction;

public class TransactionProductObject {

    private Long transactionId;
    private Long productId;
    private String createdDate;
    private String ticketCode;
    private String productCode;
    private String productName;
    private String statusDesire;
    private String statusDesireCode;
    private String createdBy;
    private String effectDate;
    private String statusRequest;
    private Long bpmProductId;

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getStatusDesire() {
        return statusDesire;
    }

    public void setStatusDesire(String statusDesire) {
        this.statusDesire = statusDesire;
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

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStatusRequest() {
        return statusRequest;
    }

    public void setStatusRequest(String statusRequest) {
        this.statusRequest = statusRequest;
    }

    public String getStatusDesireCode() {
        return statusDesireCode;
    }

    public void setStatusDesireCode(String statusDesireCode) {
        this.statusDesireCode = statusDesireCode;
    }

    public Long getBpmProductId() {
        return bpmProductId;
    }

    public void setBpmProductId(Long bpmProductId) {
        this.bpmProductId = bpmProductId;
    }

}
