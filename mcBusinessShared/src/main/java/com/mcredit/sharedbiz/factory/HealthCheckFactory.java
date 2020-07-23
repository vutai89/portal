package com.mcredit.sharedbiz.factory;

import com.mcredit.data.UnitOfWork;
import com.mcredit.sharedbiz.aggregate.HealthCheckAggregate;

public class HealthCheckFactory {
	
	public static HealthCheckAggregate getHealthCheckAgg(UnitOfWork uok) {

		return new HealthCheckAggregate(uok);
	}

}
