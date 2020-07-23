package com.mcredit.data.repository;


public interface IUpdateRepository<T> extends IRepository<T>{
	
	void update(T item);
	
}