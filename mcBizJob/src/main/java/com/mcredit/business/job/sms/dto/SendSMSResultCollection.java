package com.mcredit.business.job.sms.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendSMSResultCollection {
	@SerializedName("items")
	@Expose
	private ItemSendSMSResult items;

	public ItemSendSMSResult getItems() {
		return items;
	}

	public void setItems(ItemSendSMSResult items) {
		this.items = items;
	}
}
