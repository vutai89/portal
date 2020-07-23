package com.mcredit.sharedbiz.manager;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Callable;

import com.mcredit.common.UnitOfWorkHelper;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.factory.UnitOfWorkFactory;
import com.mcredit.model.enums.SessionType;

public class BaseManager implements Closeable {

	protected UnitOfWork uok = UnitOfWorkFactory.getInstance(SessionType.JTA);

	protected <T> T tryCatch(Callable<T> func) throws Exception {
		return UnitOfWorkHelper.tryCatch(uok, func);
	}

	@Override
	public void close() throws IOException {
		this.uok = null;
	}
}