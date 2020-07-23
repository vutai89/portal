package com.mcredit.model.dto.warehouse;

import java.io.Serializable;
import java.util.List;

public class ChangeDocumentDTO implements Serializable {

	private static final long serialVersionUID = -1946356006786189657L;

	public List<Long> lstData;
	public Long contractCavetType;
	public String billCode;
	public String deliveryError;

	public List<Long> getLstData() {
		return lstData;
	}

	public void setLstData(List<Long> lstData) {
		this.lstData = lstData;
	}

	public Long getContractCavetType() {
		return contractCavetType;
	}

	public void setContractCavetType(Long contractCavetType) {
		this.contractCavetType = contractCavetType;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getDeliveryError() {
		return deliveryError;
	}

	public void setDeliveryError(String deliveryError) {
		this.deliveryError = deliveryError;
	}

}
