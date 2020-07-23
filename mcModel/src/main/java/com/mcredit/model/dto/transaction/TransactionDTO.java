package com.mcredit.model.dto.transaction;

public class TransactionDTO {

    private Long productId;
    private String ticketCode;
    private String statusDesire;
    private String effectDate;
    private String effectDateFrom;
    private String effectDateTo;
    private String note;
    private String statusRequest;

    public TransactionDTO() {
    }

    public TransactionDTO(Long productId, String ticketCode, String effectDateFrom, String effectDateTo, String statusRequest) {
        this.productId = productId;
        this.ticketCode = ticketCode;
        this.effectDateFrom = effectDateFrom;
        this.effectDateTo = effectDateTo;
        this.statusRequest = statusRequest;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public String getEffectDateFrom() {
        return effectDateFrom;
    }

    public void setEffectDateFrom(String effectDateFrom) {
        this.effectDateFrom = effectDateFrom;
    }

    public String getEffectDateTo() {
        return effectDateTo;
    }

    public void setEffectDateTo(String effectDateTo) {
        this.effectDateTo = effectDateTo;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatusRequest() {
        return statusRequest;
    }

    public void setStatusRequest(String statusRequest) {
        this.statusRequest = statusRequest;
    }

    public String getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(String effectDate) {
        this.effectDate = effectDate;
    }

}
