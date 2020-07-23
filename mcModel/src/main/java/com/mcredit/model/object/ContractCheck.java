package com.mcredit.model.object;

import java.io.Serializable;

public class ContractCheck implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7654723166807733016L;

	private Long custId;
	private Long soNgayQuaHan;
	
	public ContractCheck() {
	}
	
	public ContractCheck(Long custId, Long soNgayQuaHan) {
		this.custId = custId;
		this.soNgayQuaHan = soNgayQuaHan;
	}
	
	public Long getCustId() {
		return custId;
	}
	public void setCustId(Long custId) {
		this.custId = custId;
	}
	public Long getSoNgayQuaHan() {
		return soNgayQuaHan;
	}
	public void setSoNgayQuaHan(Long soNgayQuaHan) {
		this.soNgayQuaHan = soNgayQuaHan;
	}
}
