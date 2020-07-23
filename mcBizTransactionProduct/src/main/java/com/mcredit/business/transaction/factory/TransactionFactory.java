package com.mcredit.business.transaction.factory;

import com.mcredit.business.transaction.aggregate.TransactionAggregate;
import com.mcredit.data.UnitOfWork;

public class TransactionFactory {
	
	public static TransactionAggregate getTransactionAggregate(UnitOfWork unitOfWork) {
		return new TransactionAggregate(unitOfWork);
	}
	
}
