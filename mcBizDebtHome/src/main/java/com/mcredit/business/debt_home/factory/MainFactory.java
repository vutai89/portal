package com.mcredit.business.debt_home.factory;

import com.mcredit.business.debt_home.aggregate.AssignAggreate;
import com.mcredit.business.debt_home.aggregate.SearchFilesAggreate;
import com.mcredit.business.debt_home.validation.DebtHomeValidation;
import com.mcredit.data.UnitOfWork;

public class MainFactory {
	
	public static DebtHomeValidation validation(UnitOfWork uok) {
		return new DebtHomeValidation(uok);
	}
	
	public static AssignAggreate assignAggreate(UnitOfWork uok) {
		return new AssignAggreate(uok);
	}
	
	public static SearchFilesAggreate searchFilesAggreate(UnitOfWork uok) {
		return new SearchFilesAggreate(uok);
	}
}
