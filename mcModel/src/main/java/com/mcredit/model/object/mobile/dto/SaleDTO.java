package com.mcredit.model.object.mobile.dto;

import java.math.BigDecimal;

public class SaleDTO {
	private BigDecimal saleId;
	private String appId;

	public BigDecimal getSaleId() {
		return saleId;
	}
	public void setSaleId(BigDecimal saleId) {
		this.saleId = saleId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}

}
