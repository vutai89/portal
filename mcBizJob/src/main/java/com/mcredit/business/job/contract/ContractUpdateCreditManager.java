package com.mcredit.business.job.contract;

import java.io.IOException;

import com.mcredit.business.job.JobManagerBase;
import com.mcredit.model.enums.MsgOrder;
import com.mcredit.model.enums.MsgTransType;

public class ContractUpdateCreditManager extends JobManagerBase {

	public ContractUpdateCreditManager() {
		TRANS_TYPE_FILTER = MsgTransType.UPDATE_CREDIT;
		stepSingle = new ContractUpdateCreditT24(MsgOrder.ONE);
	}

	public void close() throws IOException {
	}
}
