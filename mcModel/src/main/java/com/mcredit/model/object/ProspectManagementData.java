package com.mcredit.model.object;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ProspectManagementData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6797050232839170514L;
	
	public BigDecimal totalRows;
	public List<?> dataRows;
	
	public BigDecimal getTotalRows() {
		return totalRows;
	}
	public void setTotalRows(BigDecimal totalRows) {
		this.totalRows = totalRows;
	}
	public List<?> getDataRows() {
		return dataRows;
	}
	public void setDataRows(List<?> dataRows) {
		this.dataRows = dataRows;
	}
}
