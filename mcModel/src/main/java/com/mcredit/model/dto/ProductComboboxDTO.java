package com.mcredit.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ProductComboboxDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String productCode;
	private String productName;
	private String status;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}


}
