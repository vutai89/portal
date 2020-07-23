package com.mcredit.data;

public interface IBaseUnitOfWork {
	void start();

	void commit();

	void rollback();
	
	void close();
}
