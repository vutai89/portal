package com.mcredit.sharedbiz.factory;

import com.mcredit.data.common.UnitOfWorkCommon;
import com.mcredit.data.telesale.UnitOfWorkTelesale;
import com.mcredit.sharedbiz.aggregate.AllocationAggregate;

public class AllocationFactory {

	public static AllocationAggregate getSupervisorAgg(UnitOfWorkTelesale uokTelesale , UnitOfWorkCommon uokCommon) {
		return new AllocationAggregate(uokTelesale, uokCommon);
	}

}
