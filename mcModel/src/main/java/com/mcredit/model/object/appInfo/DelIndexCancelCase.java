
package com.mcredit.model.object.appInfo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "Result" })
public class DelIndexCancelCase implements Serializable {

	private static final long serialVersionUID = -8871036573869442915L;

	@JsonProperty("Result")
	private List<ResultDelIndex> result = null;

	@JsonProperty("Result")
	public List<ResultDelIndex> getResult() {
		return result;
	}

	@JsonProperty("Result")
	public void setResult(List<ResultDelIndex> result) {
		this.result = result;
	}

}
