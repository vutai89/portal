package com.mcredit.sharedbiz.cache.parameters;

public interface IDataTypeStrategy<T> {

	/**
	 * @author catld.ho
	 * @param value : raw value in string type
	 * @return value in specific type T
	 * @throws Exception
	 */
	public T getValue(String value) throws Exception;
	
	/**
	 * @author catld.ho
	 * @return default value in specific type T
	 */
	public T getDefaultValue();
}
