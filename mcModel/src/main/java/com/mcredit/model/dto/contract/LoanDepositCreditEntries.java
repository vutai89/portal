package com.mcredit.model.dto.contract;

import java.io.Serializable;
import java.util.ArrayList;

public class LoanDepositCreditEntries implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LoanDepositCreditEntries() {
		this.setItems(new ArrayList<LoanDepositCreditEntry>());
	}
	
	private ArrayList<LoanDepositCreditEntry> LoanDepositCreditEntry;

	public ArrayList<LoanDepositCreditEntry> getItems() {
		return LoanDepositCreditEntry;
	}

	public void setItems(ArrayList<LoanDepositCreditEntry> items) {
		this.LoanDepositCreditEntry = items;
	}
}