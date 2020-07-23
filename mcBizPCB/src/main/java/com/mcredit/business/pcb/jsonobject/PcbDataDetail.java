package com.mcredit.business.pcb.jsonobject;

import com.mcredit.model.dto.pcb.request.RIReqInput;

public class PcbDataDetail {
	RIReqInput Request;
	PcbInfo   Response;
	
	public RIReqInput getRequest() {
		return Request;
	}
	public void setRequest(RIReqInput request) {
		this.Request = request;
	}
	public PcbInfo getResponse() {
		return Response;
	}
	public void setResponse(PcbInfo response) {
		this.Response = response;
	}	
}
