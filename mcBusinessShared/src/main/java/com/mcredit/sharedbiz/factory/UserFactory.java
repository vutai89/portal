package com.mcredit.sharedbiz.factory;

import com.mcredit.data.UnitOfWork;
import com.mcredit.sharedbiz.aggregate.EmployeeAggreate;
import com.mcredit.sharedbiz.aggregate.UserAggregate;

public class UserFactory {

	public static UserAggregate getUserAgg(UnitOfWork uok) {

		return new UserAggregate(uok);
	}
	
	public static EmployeeAggreate getEmployeeAgg(UnitOfWork uok) {

		return new EmployeeAggreate(uok);
	}
}
