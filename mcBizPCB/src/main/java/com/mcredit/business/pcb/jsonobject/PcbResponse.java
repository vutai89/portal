package com.mcredit.business.pcb.jsonobject;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PcbResponse {
	public PcbResponseCommonInfo pcbResponseCommonInfo;
	public PcbResponseCreditInfo pcbResponseCreditInfo;
	public Integer orderMsg;
	public Date createDate;
	public PcbResponse(PcbResponseCommonInfo pcbResponseCommonInfo, PcbResponseCreditInfo pcbResponseCreditInfo,Integer orderMsg,Date createDate) {
		super();
		this.pcbResponseCommonInfo = pcbResponseCommonInfo;
		this.pcbResponseCreditInfo = pcbResponseCreditInfo;
		this.orderMsg = orderMsg;
		this.createDate=createDate;
	}
	
}
