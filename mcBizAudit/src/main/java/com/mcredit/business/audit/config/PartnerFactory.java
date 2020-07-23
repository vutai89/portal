package com.mcredit.business.audit.config;


import java.text.SimpleDateFormat;

import com.mcredit.model.enums.disaudit.ThirdParty;

public class PartnerFactory {

	private final static String dateFormat = "ddMMyyyy";
	private final static SimpleDateFormat dfVNPOST = new SimpleDateFormat("yyyyMMdd");
	private final static SimpleDateFormat df = new SimpleDateFormat(dateFormat);
	
	
	public static IPartner create(String thirdParty) throws Exception {
	
		IPartner partner = null;
		
		if (thirdParty.equals(ThirdParty.MB.value())) {
			partner = new MBPartner();
			return partner;
		} else if (thirdParty.equals(ThirdParty.MOMO.value())) {
			partner = new MomoPartner();
			return partner;
		} else if (thirdParty.equals(ThirdParty.PAYOO.value())) {
			partner = new PayooPartner();
			return partner;
		} else if (thirdParty.equals(ThirdParty.VNPOST.value())) {
			partner = new VNPostPartner();
			return partner;
		} else if (thirdParty.equals(ThirdParty.VNPTEPAY.value())) {
			partner = new VnptEpayPartner();
			return partner;
		}else if (thirdParty.equals(ThirdParty.VNPOST17.value())) {
			partner = new VNPOST17Partner();
			return partner;
		}
		
		
		throw new Exception("Invalid Protocol.");
	}

}
