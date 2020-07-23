package com.mcredit.model.dto.vehicle_price;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class MotorPriceDataDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6797050232839170514L;
	
	public BigDecimal totalRows;
	public List<MotorPriceDataDetailDTO> dataRows;
	
	public BigDecimal getTotalRows() {
		return totalRows;
	}
	public void setTotalRows(BigDecimal totalRows) {
		this.totalRows = totalRows;
	}
	public List<MotorPriceDataDetailDTO> getDataRows() {
		return dataRows;
	}
	public void setDataRows(List<MotorPriceDataDetailDTO> dataRows) {
		this.dataRows = dataRows;
	}
}
