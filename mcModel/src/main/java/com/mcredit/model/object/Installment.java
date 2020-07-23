package com.mcredit.model.object;

import java.io.Serializable;
import java.math.BigDecimal;

public class Installment implements Serializable {

	// Ty le tra truoc
	private float downPaymentRate;
	// Lai suat ho tro tu dai ly
	private float merchantIntRate;
	// Ma tran lai suat MIR, PRODUCT_SCOPE_ADDITIONAL.rule_matrix
	private String matrixMIR;
	// Gia hang hoa
	private int commodityPrice;
	// Gia tri hang hoa toi thieu
	private BigDecimal minAmount;
	// Gia tri hang hoa toi da
	private BigDecimal maxAmount;
	// Gia tri hang hoa toi da
	private BigDecimal minDownPaymentRate;
	// Ma tran dieu kien ap dung san pham
	private String matrixPDRule0Percent;
	private String sipCode;
	private String comType;
	private String comModel;

	public float getDownPaymentRate() {
		return downPaymentRate;
	}
	public void setDownPaymentRate(float downPaymentRate) {
		this.downPaymentRate = downPaymentRate;
	}
	public float getMerchantIntRate() {
		return merchantIntRate;
	}
	public void setMerchantIntRate(float merchantIntRate) {
		this.merchantIntRate = merchantIntRate;
	}
	public String getMatrixMIR() {
		return matrixMIR;
	}
	public void setMatrixMIR(String matrixMIR) {
		this.matrixMIR = matrixMIR;
	}
	public int getCommodityPrice() {
		return commodityPrice;
	}
	public void setCommodityPrice(int commodityPrice) {
		this.commodityPrice = commodityPrice;
	}
	
	public void addMatrixMIR(String item) {
		this.matrixMIR = this.matrixMIR + item;
	}
	public BigDecimal getMinAmount() {
		return minAmount;
	}
	public void setMinAmount(BigDecimal minAmount) {
		this.minAmount = minAmount;
	}
	public BigDecimal getMaxAmount() {
		return maxAmount;
	}
	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
	}
	public BigDecimal getMinDownPaymentRate() {
		return minDownPaymentRate;
	}
	public void setMinDownPaymentRate(BigDecimal minDownPaymentRate) {
		this.minDownPaymentRate = minDownPaymentRate;
	}
	public String getMatrixPDRule0Percent() {
		return matrixPDRule0Percent;
	}
	public void setMatrixPDRule0Percent(String matrixPDRule0Percent) {
		this.matrixPDRule0Percent = matrixPDRule0Percent;
	}
	public void addMatrixPDRule0Percent(String item) {
		this.matrixPDRule0Percent = this.matrixPDRule0Percent + item;
	}
	public String getSipCode() {
		return sipCode;
	}
	public void setSipCode(String sipCode) {
		this.sipCode = sipCode;
	}
	public String getComType() {
		return comType;
	}
	public void setComType(String comType) {
		this.comType = comType;
	}
	public String getComModel() {
		return comModel;
	}
	public void setComModel(String comModel) {
		this.comModel = comModel;
	}

}
