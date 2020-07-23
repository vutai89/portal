package com.mcredit.business.job.sms;

import java.io.IOException;

import com.mcredit.business.job.JobManagerBase;
import com.mcredit.model.enums.MsgOrder;
import com.mcredit.model.enums.MsgTransType;

public class SMSManager extends JobManagerBase {

	public SMSManager() {
		TRANS_TYPE_FILTER = MsgTransType.SMS;
//		msg_order = new MsgOrder[] { MsgOrder.ONE };
		stepSingle = new SendSMS(MsgOrder.ONE);
	}

	public void close() throws IOException {
	}

//	 public static void main(final String[] args) throws Exception {
//		 try (SMSManager manager = new SMSManager()) {
//			 manager.runJobSingle();
//		 }
//	 }
}
