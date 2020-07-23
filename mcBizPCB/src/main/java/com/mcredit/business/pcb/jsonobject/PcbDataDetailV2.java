package com.mcredit.business.pcb.jsonobject;

import com.mcredit.model.dto.pcb.request.RIReqInputV2;

public class PcbDataDetailV2 {
	RIReqInputV2 Request;
	PcbInfo   Response;
	
	public RIReqInputV2 getRequest() {
		return Request;
	}
	public void setRequest(RIReqInputV2 request) {
		Request = request;
	}
	public PcbInfo getResponse() {
		return Response;
	}
	public void setResponse(PcbInfo response) {
		Response = response;
	}	
	
}
