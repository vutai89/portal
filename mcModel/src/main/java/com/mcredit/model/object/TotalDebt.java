package com.mcredit.model.object;

import java.io.Serializable;

public class TotalDebt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3974562933261780259L;
	
	private Long totalDebt;
	
	public TotalDebt() {
	}
	
	public TotalDebt(Long totalDebt) {
		this.totalDebt = totalDebt;
	}

	public Long getTotalDebt() {
		return totalDebt;
	}

	public void setTotalDebt(Long totalDebt) {
		this.totalDebt = totalDebt;
	}
}
