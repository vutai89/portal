package com.mcredit.background.thread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.mcredit.model.enums.CacheTag;
import com.mcredit.model.enums.ProjectName;
import com.mcredit.sharedbiz.thread.RefreshCacheThread;

public class ThreadRefreshCache implements ServletContextListener{
	
	private ExecutorService executorService;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		executorService = Executors.newSingleThreadExecutor();
		CacheTag[] lstCacheTag = new CacheTag[] {
				CacheTag.CODE_TABLE,
				CacheTag.PARAMETERS };
		Runnable runnable = new RefreshCacheThread(ProjectName.mcWebSocket.value(),lstCacheTag);
		executorService.submit(runnable);
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		executorService.shutdown();
	}
}
