
package com.mcredit.model.object.cancelCaseBPM;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "Result" })
public class ResultCancelCaseBPM implements Serializable {
	private static final long serialVersionUID = 20427570388082076L;

	@JsonProperty("Result")
	private List<CancelCaseObject> cancalCaseObject = null;

	@JsonProperty("Result")
	public List<CancelCaseObject> getResult() {
		return cancalCaseObject;
	}

	@JsonProperty("Result")
	public void setResult(List<CancelCaseObject> cancalCaseObject) {
		this.cancalCaseObject = cancalCaseObject;
	}

}
