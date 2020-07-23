package com.mcredit.business.audit.common;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class GetterAndSetter {
	/**
	 * @param obj
	 * @param fieldName
	 * @param value
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws ClassNotFoundException 
	 */
	public static void callSetter(Object obj, String fieldName, Object value) throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		PropertyDescriptor pd;
		try {
			pd = new PropertyDescriptor(fieldName, obj.getClass());
			Field field = obj.getClass().getDeclaredField(fieldName);
			if (!field.getType().isInstance(value)) {
				value = Class.forName(field.getType().toString()).cast(value);
				pd.getWriteMethod().invoke(obj, value);
			} else {
				pd.getWriteMethod().invoke(obj, value);				
			}
		} catch (IntrospectionException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param obj
	 * @param fieldName
	 * @param value
	 */
	public static void callGetter(Object obj, String fieldName) {
		PropertyDescriptor pd;
		try {
			pd = new PropertyDescriptor(fieldName, obj.getClass());
			System.out.println("" + pd.getReadMethod().invoke(obj));
		} catch (IntrospectionException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
