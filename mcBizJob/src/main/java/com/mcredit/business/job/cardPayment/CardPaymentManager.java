package com.mcredit.business.job.cardPayment;

import java.io.IOException;

import com.mcredit.business.job.CallBack;
import com.mcredit.business.job.JobManagerBase;
import com.mcredit.model.enums.MsgOrder;
import com.mcredit.model.enums.MsgTransType;

public class CardPaymentManager extends JobManagerBase {

	public CardPaymentManager() {
		TRANS_TYPE_FILTER = MsgTransType.create_payment;
		msg_order = new MsgOrder[] { MsgOrder.ONE, MsgOrder.TWO, MsgOrder.THREE };
		stepList = new CallBack[] { new MakeTransferOnT24(MsgOrder.ONE), new MakeTransferOnT24(MsgOrder.TWO),
				new MakePaymentMCreditCardOnWay4() };
	}

	public void close() throws IOException {
	}

	// public static void main(final String[] args) throws Exception {
	// try (CardPaymentManager manager = new CardPaymentManager()) {
	// manager.runJob();
	// }
	// }
}
