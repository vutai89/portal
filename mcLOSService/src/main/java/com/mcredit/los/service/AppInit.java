package com.mcredit.los.service;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.mcredit.rule.RuleCacheManager;
import com.mcredit.rule.manager.RuleDataCacheManager;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;

public class AppInit implements Filter {

	private ServletContext context;

	@SuppressWarnings("resource")
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		try {

			new Thread(new CacheManager()).start();
			
			/*INIT RULE CACHE MANAGER*/
			Thread thread = new Thread(){
			    public void run(){
			      try{
			    	  ParametersCacheManager.getInstance().refresh();
			    	  RuleDataCacheManager.getInstance().refresh();
			    	  RuleCacheManager.getInstance();
			    	  RuleDataCacheManager.getInstance().refresh();
			      }catch(Throwable th){
			    	  System.out.println(th.getMessage());
			      }
			    }
			  };
			  thread.start();

			this.context = fConfig.getServletContext();
			this.context.log("CacheInit initialized!");
		} catch (Exception e) {
			this.context.log("CacheInit.Ex: " + e.toString());
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}
}
