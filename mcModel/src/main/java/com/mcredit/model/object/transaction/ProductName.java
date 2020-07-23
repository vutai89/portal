/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcredit.model.object.transaction;

public class ProductName {

    private Long productId;
    private String productCode;
    private String productName;
    private String statuspresent;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public String getStatuspresent() {
        return statuspresent;
    }

    public void setStatuspresent(String statuspresent) {
        this.statuspresent = statuspresent;
    }

}
