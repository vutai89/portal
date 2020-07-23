
package com.mcredit.model.dto.card;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentSendSMSBody {

    @SerializedName("receiverID")
    @Expose
    private String receiverID;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("sendImmidiately")
    @Expose
    private Boolean sendImmidiately;
    @SerializedName("senderSystem")
    @Expose
    private String senderSystem;
    @SerializedName("smsType")
    @Expose
    private String smsType;

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getSendImmidiately() {
        return sendImmidiately;
    }

    public void setSendImmidiately(Boolean sendImmidiately) {
        this.sendImmidiately = sendImmidiately;
    }

    public String getSenderSystem() {
        return senderSystem;
    }

    public void setSenderSystem(String senderSystem) {
        this.senderSystem = senderSystem;
    }

    public String getSmsType() {
        return smsType;
    }

    public void setSmsType(String smsType) {
        this.smsType = smsType;
    }

}
