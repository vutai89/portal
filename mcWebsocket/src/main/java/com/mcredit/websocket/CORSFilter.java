package com.mcredit.websocket;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

public class CORSFilter implements ContainerResponseFilter {

	@Override
    public ContainerResponse filter(ContainerRequest request,ContainerResponse response) {

		
        response.getHttpHeaders().add("Access-Control-Allow-Origin", "*");
        response.getHttpHeaders().add("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept, Authorization, X-CSRF-TOKEN");
        response.getHttpHeaders().add("Access-Control-Allow-Credentials", "false");
        response.getHttpHeaders().add("Access-Control-Allow-Methods","GET, POST, PUT, DELETE");
        response.getHttpHeaders().add("Access-Control-Max-Age","3600");
        
        return response;
    }
}