/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcredit.model.dto.transaction;

public class ProductCompactDTO {

    private Long transactionId;
    private Long productId;
    private String effectDate;
    private String statusDesire;
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

    public String getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(String effectDate) {
        this.effectDate = effectDate;
    }

    public String getStatusDesire() {
        return statusDesire;
    }

    public void setStatusDesire(String statusDesire) {
        this.statusDesire = statusDesire;
    }

    public Long getBpmProductId() {
        return bpmProductId;
    }

    public void setBpmProductId(Long bpmProductId) {
        this.bpmProductId = bpmProductId;
    }

}
