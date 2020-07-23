package com.mcredit.unit.test.mobile.factory;

import com.mcredit.data.mobile.UnitOfWorkMobileTest;
import com.mcredit.unit.test.mobile.aggregate.MobileAggregateTest;

public class MobileFactoryTest {

	public static MobileAggregateTest getMobileAggregateTest(UnitOfWorkMobileTest uokMobileTest) {
		return  new  MobileAggregateTest(uokMobileTest) ;
	}
}
