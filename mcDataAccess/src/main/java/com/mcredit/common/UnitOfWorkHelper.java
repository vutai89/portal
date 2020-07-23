package com.mcredit.common;

import java.util.concurrent.Callable;

import com.mcredit.data.UnitOfWork;

public class UnitOfWorkHelper {

	public static <T> T tryCatch(UnitOfWork uok, Callable<T> func) throws Exception {

		try {
			uok.start();
			T result = func.call();
			uok.commit();
			return result;
		} catch (Throwable e) {
			uok.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			uok.close();
		}
	}

}