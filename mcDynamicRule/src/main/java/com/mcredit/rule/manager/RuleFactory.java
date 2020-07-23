package com.mcredit.rule.manager;

import com.mcredit.data.UnitOfWork;

public class RuleFactory {

	public static RuleAggregate getInstance(UnitOfWork uok) {
		return new RuleAggregate(uok.rule);
	}
}
