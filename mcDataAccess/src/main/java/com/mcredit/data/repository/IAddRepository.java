package com.mcredit.data.repository;


public interface IAddRepository<T> extends IRepository<T>{
	
	void add(T item);
	
}