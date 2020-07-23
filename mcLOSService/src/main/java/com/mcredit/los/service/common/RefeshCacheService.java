package com.mcredit.los.service.common;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.los.service.BasedService;
import com.mcredit.sharedbiz.cache.CacheManager;


@Path("/v1.0/refesh-cache")
public class RefeshCacheService extends BasedService {
	
	@GET
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response refeshCache(@QueryParam("cacheName") String cacheName) throws Exception {		
		try( CacheManager manager = new CacheManager() ) {
			return ok(manager.refeshCache(cacheName));
		}
	}
}
