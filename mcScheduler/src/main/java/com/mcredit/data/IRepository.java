package com.mcredit.data;

public interface IRepository<T> {
	void add(T item);

	void update(T item);
	
	void upsert(T item);

	void remove(T item);
	
}