/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcredit.model.object.transaction;

import java.util.List;

public class TransactionProductResult {

    private int countRecord;
    private List<TransactionProductObject> lstProductObjects;

    public int getCountRecord() {
        return countRecord;
    }

    public void setCountRecord(int countRecord) {
        this.countRecord = countRecord;
    }

    public List<TransactionProductObject> getLstProductObjects() {
        return lstProductObjects;
    }

    public void setLstProductObjects(List<TransactionProductObject> lstProductObjects) {
        this.lstProductObjects = lstProductObjects;
    }

}
