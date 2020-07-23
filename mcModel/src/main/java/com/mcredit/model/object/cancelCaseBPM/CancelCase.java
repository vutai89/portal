package com.mcredit.model.object.cancelCaseBPM;

import java.io.Serializable;
import java.util.List;

public class CancelCase implements Serializable {

	public static final long serialVersionUID = 1L;

	public List<CancelCaseObject> caseInstallmentLoan;
	public List<CancelCaseObject> caseConcentratingDataEntry;
	public List<CancelCaseObject> caseConcentratingDataEntryDONE;

	public CancelCase(List<CancelCaseObject> caseInstallmentLoan, List<CancelCaseObject> caseConcentratingDataEntry, List<CancelCaseObject> caseConcentratingDataEntryDONE) {
		super();
		this.caseInstallmentLoan = caseInstallmentLoan;
		this.caseConcentratingDataEntry = caseConcentratingDataEntry;
		this.caseConcentratingDataEntryDONE = caseConcentratingDataEntryDONE;
	}

	public CancelCase() {
	}

	public List<CancelCaseObject> getCaseInstallmentLoan() {
		return caseInstallmentLoan;
	}

	public void setCaseInstallmentLoan(List<CancelCaseObject> caseInstallmentLoan) {
		this.caseInstallmentLoan = caseInstallmentLoan;
	}

	public List<CancelCaseObject> getCaseConcentratingDataEntry() {
		return caseConcentratingDataEntry;
	}

	public void setCaseConcentratingDataEntry(List<CancelCaseObject> caseConcentratingDataEntry) {
		this.caseConcentratingDataEntry = caseConcentratingDataEntry;
	}

	public List<CancelCaseObject> getCaseConcentratingDataEntryDONE() {
		return caseConcentratingDataEntryDONE;
	}

	public void setCaseConcentratingDataEntryDONE(List<CancelCaseObject> caseConcentratingDataEntryDONE) {
		this.caseConcentratingDataEntryDONE = caseConcentratingDataEntryDONE;
	}

}
