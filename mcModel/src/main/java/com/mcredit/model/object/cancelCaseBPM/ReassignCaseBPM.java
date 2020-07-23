
package com.mcredit.model.object.cancelCaseBPM;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReassignCaseBPM implements Serializable {
	private static final long serialVersionUID = -8396771475787771654L;
	
	@SerializedName("cases")
	@Expose
	private List<Case> cases = null;
	@SerializedName("usr_uid_target")
	@Expose
	private String usrUidTarget;

	public ReassignCaseBPM(List<Case> cases, String usrUidTarget) {
		super();
		this.cases = cases;
		this.usrUidTarget = usrUidTarget;
	}

	public List<Case> getCases() {
		return cases;
	}

	public void setCases(List<Case> cases) {
		this.cases = cases;
	}

	public String getUsrUidTarget() {
		return usrUidTarget;
	}

	public void setUsrUidTarget(String usrUidTarget) {
		this.usrUidTarget = usrUidTarget;
	}

}
