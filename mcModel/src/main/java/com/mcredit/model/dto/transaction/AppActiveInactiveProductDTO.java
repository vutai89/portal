/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcredit.model.dto.transaction;

import java.util.List;

public class AppActiveInactiveProductDTO {

    private List<ProductCompactDTO> compactDTOs;
    private String statusRequest;

    public List<ProductCompactDTO> getCompactDTOs() {
        return compactDTOs;
    }

    public void setCompactDTOs(List<ProductCompactDTO> compactDTOs) {
        this.compactDTOs = compactDTOs;
    }

    public String getStatusRequest() {
        return statusRequest;
    }

    public void setStatusRequest(String statusRequest) {
        this.statusRequest = statusRequest;
    }

}
