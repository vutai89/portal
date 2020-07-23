/**
 * 
 */
package com.mcredit.mobile.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.Callable;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.collections.CollectionUtils;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.data.exception.NoRecordFoundException;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.ServiceName;
import com.mcredit.model.object.mobile.dto.XSecurityDTO;
import com.mcredit.model.object.mobile.dto.XSecurityListDTO;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.manager.AuthorizationManager;
import com.mcredit.sharedbiz.manager.UserManager;
import com.mcredit.sharedbiz.validation.AuthorizationException;
import com.mcredit.sharedbiz.validation.PermissionException;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

/**
 * @author anhdv.ho
 *
 */
public class BasedService {

	public BasedService() {
	}

	public BasedService(@Context HttpHeaders headers) throws PermissionException {
		this.headers = headers;
		if(enableXsecurity()) 
			validateClient();
	}

	protected HttpHeaders headers = null;
	private String AUTHORIZATION = "Authorization";
	private String AUTHORIZATION_CHEME = "Bearer ";
	protected UserDTO currentUser = new UserDTO();
	private String X_SECURITY = "x-security";

	
    
    protected Response ok(Object object) {
		return Response.status(Status.OK.getStatusCode()).entity(object)
				.build();
	}
    
	protected Response file(String url,String filename, Boolean isCleanUp) throws ValidationException {		
	    File file = new File(url);
	    	
	    if (!file.exists()) {
	    	throw new ValidationException(Messages.getString("validation.field.mainMessage", Labels.getString("label.xsell.file.download")));
	    }
	    
	    ResponseBuilder response = Response.ok((Object) file);
	    response.header("Content-Disposition", String.format("attachment; filename=\"%s\"",filename));
	    
	    //clean up
	    if (isCleanUp) {
	    	file.deleteOnExit();
	    }
	   
	    return response.build();
	}
	
	protected Response responseFile(File file) throws ValidationException, IOException {
	    if (!file.exists()) {
	    	throw new ValidationException(Messages.getString("validation.field.mainMessage", Labels.getString("label.xsell.file.download")));
	    }
	    
	    InputStream inputStream = new FileInputStream(file);

	    StreamingOutput stream = new StreamingOutput() {
	        @Override
	        public void write(OutputStream outputStream) throws IOException {
	          try {
	            
	        	  byte[] bytesArray = new byte[4096];
	              int bytesRead = -1;
	              while ((bytesRead = inputStream.read(bytesArray)) != -1) {
	           	   	outputStream.write(bytesArray, 0, bytesRead);
	              }
	              
	              inputStream.close();
	              // Delete file after response
	              file.delete();
	        	  
	          } catch (Exception e) {
	             e.printStackTrace();
	          }
	        }
	    };

	      
	    return Response.ok(stream, "application/pdf")
	              .header("content-disposition", "attachment; filename = "+ file.getName())
	              .build();
  }
	

	protected Response created(Object object) {
		return Response.status(Status.CREATED.getStatusCode()).entity(object)
				.build();
	};

	protected Response accepted(Object object) {
		return Response.status(Status.ACCEPTED.getStatusCode()).entity(object)
				.build();
	}

	protected Response noContent(Object object) {
		return Response.status(Status.NO_CONTENT.getStatusCode())
				.entity(object).build();
	}

	protected Response internalServerError(Object object) {
		return Response.status(Status.INTERNAL_SERVER_ERROR.getStatusCode())
				.entity(object).build();
	}

	protected Response unauthorize(Object object) {
		return Response.status(Status.UNAUTHORIZED.getStatusCode())
				.entity(object).build();
	}

	protected Response badRequest(Object object) {
		return Response.status(Status.BAD_REQUEST.getStatusCode())
				.entity(object).build();
	}

	protected Response returnObject(Object object) {
		return Response.ok(object, MediaType.APPLICATION_JSON).build();
	}

	protected <T> T authorize(Callable<T> func) throws Exception {

		validateToken();
		
		return func.call();
	}
	
	protected <T> T authorize(ServiceName serviceName,Callable<T> func) throws Exception {
	
		validateToken();
		
		validatePermission(serviceName);

		return func.call();
	}
	
	protected <T> T anonymous(Callable<T> func) throws Exception {
		return func.call();
	}

	private void validateToken() throws Exception {

		try (AuthorizationManager manager = new AuthorizationManager()) {

			List<String> tempHeaders = headers.getRequestHeader(AUTHORIZATION);
			if (tempHeaders == null || tempHeaders.size() == 0)
				throw new AuthorizationException("Unauthorized.");

			String auth = tempHeaders.get(0);
			if (StringUtils.isNullOrEmpty(auth))
				throw new AuthorizationException("Unauthorized.");

			if (!auth.startsWith(AUTHORIZATION_CHEME))
				throw new AuthorizationException("Unauthorized.");

			String token = auth.replaceFirst(AUTHORIZATION_CHEME, "");
			if (StringUtils.isNullOrEmpty(token))
				throw new AuthorizationException("Unauthorized.");

			UserDTO user = manager.checkToken(token);
			
			this.currentUser = user;

		} catch (NoRecordFoundException ex) {
			throw new AuthorizationException("Unauthorized");
		}
	}


	private void validatePermission(ServiceName serviceName) throws Exception {
		try (UserManager manager = new UserManager()) {
			
			if(!manager.isServiceAllow(this.currentUser.getLoginId().toString(), serviceName))
				throw new PermissionException("Permission denied.");
		}
	}
	
	private void validateClient() throws PermissionException {
		//Check xSecurity
		List<String> xSecurityHeaders = headers.getRequestHeader(X_SECURITY);
		if(CollectionUtils.isEmpty(xSecurityHeaders))
			throw new PermissionException("Permission denied.");
		
		String xSecurity = xSecurityHeaders.get(0);
		if (StringUtils.isNullOrEmpty(xSecurity)) 
			throw new PermissionException("Permission denied.");
		
		String xSecurityListString = CacheManager.Parameters().findParamValueAsString(ParametersName.MFS_XSECURITY_LIST);
		XSecurityListDTO xSecurityListDTO = JSONConverter.toObject(xSecurityListString, XSecurityListDTO.class);
		
		List<XSecurityDTO> xSecurityList = xSecurityListDTO.getxSecurityList();
		
		if(CollectionUtils.isEmpty(xSecurityList)) {
			throw new PermissionException("Permission denied.");
		}
		
		if(!xSecurityList.stream().anyMatch(t -> (t.getxSecurity().equals(xSecurity)&& t.getIsActive().equals("A")))) {
			throw new PermissionException("Permission denied.");
		}
	}
	
	private Boolean enableXsecurity() {
		return "A".equals(CacheManager.Parameters().findParamValueAsString(ParametersName.MFS_ENABLE_XSECURITY)) ? true : false;
	}

}
