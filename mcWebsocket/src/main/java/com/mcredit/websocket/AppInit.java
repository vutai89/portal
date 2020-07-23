package com.mcredit.websocket;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.mcredit.model.enums.CacheTag;
import com.mcredit.sharedbiz.cache.CacheManager;

public class AppInit implements Filter {

	private ServletContext context;

	@SuppressWarnings("resource")
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		try {

			new Thread(new CacheManager(new CacheTag[] {
					CacheTag.CODE_TABLE,
					CacheTag.PARAMETERS })).start();

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