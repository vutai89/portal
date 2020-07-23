package com.mcredit.data.repository;


public interface IRemoveRepository<T> extends IRepository<T> {
	
	void remove(T item);
	
}