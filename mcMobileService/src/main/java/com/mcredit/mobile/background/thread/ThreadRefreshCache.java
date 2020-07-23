package com.mcredit.mobile.background.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.mcredit.model.enums.ProjectName;
import com.mcredit.sharedbiz.thread.RefreshCacheThread;

public class ThreadRefreshCache implements ServletContextListener{
	
	private ExecutorService executorService;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		executorService = Executors.newSingleThreadExecutor();
		Runnable runnable = new RefreshCacheThread(ProjectName.mcMobileService.value());
		executorService.submit(runnable);
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		executorService.shutdown();
	}
}
