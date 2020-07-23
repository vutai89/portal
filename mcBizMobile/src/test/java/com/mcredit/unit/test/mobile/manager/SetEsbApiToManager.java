package com.mcredit.unit.test.mobile.manager;

import java.lang.reflect.Field;

import com.mcredit.business.mobile.aggregate.MobileAggregate;
import com.mcredit.business.mobile.callout.EsbApi;
import com.mcredit.business.mobile.factory.MobileFactory;
import com.mcredit.business.mobile.manager.MobileManager;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.factory.UnitOfWorkFactory;
import com.mcredit.model.enums.SessionType;

public class SetEsbApiToManager {
	
	public static void setEsbApiToManager(MobileManager manager, EsbApi esbApiMock) throws Exception {
		
		// Set aggregate in manager
		UnitOfWork uok = UnitOfWorkFactory.getInstance(SessionType.JTA);
		MobileAggregate aggreate = MobileFactory.getMobileAggregate(uok);
		Field list1 = manager.getClass().getDeclaredField("_mobileAgg");
		list1.setAccessible(true);
		list1.set(manager, aggreate);

		// Set esbapi in aggregate
		Field list2 = aggreate.getClass().getDeclaredField("_esbApi");
		list2.setAccessible(true);
		list2.set(aggreate, esbApiMock);
	}
}
