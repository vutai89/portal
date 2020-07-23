package com.mcredit.model.dto.card;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentSMSDTO {

	@SerializedName("SendSMSRequest")
	@Expose
	private PaymentSendSMSBody paymentSendSMSBody;

	public PaymentSendSMSBody getPaymentSendSMSBody() {
		return paymentSendSMSBody;
	}

	public void setPaymentSendSMSBody(PaymentSendSMSBody paymentSendSMSBody) {
		this.paymentSendSMSBody = paymentSendSMSBody;
	}

}
