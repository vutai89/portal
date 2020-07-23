/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcredit.model.object.transaction;

public class ScheduleActiveInactiveObject {

    private String ticketCode;
    private String effectDate;
    private String note;

    public ScheduleActiveInactiveObject(String ticketCode, String effectDate, String note) {
        this.ticketCode = ticketCode;
        this.effectDate = effectDate;
        this.note = note;
    }

    public ScheduleActiveInactiveObject() {
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(String effectDate) {
        this.effectDate = effectDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
