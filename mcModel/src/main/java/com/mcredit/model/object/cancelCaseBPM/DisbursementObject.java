
package com.mcredit.model.object.cancelCaseBPM;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DisbursementObject implements Serializable {
	private static final long serialVersionUID = -5011482858020373535L;

	@SerializedName("result")
	@Expose
	private List<Disbursement> result;

	public List<Disbursement> getResult() {
		return result;
	}

	public void setResult(List<Disbursement> result) {
		this.result = result;
	}

}
