package com.mcredit.mobile.service.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.sun.jersey.server.impl.model.parameter.multivalued.ExtractorContainerException;
import com.mcredit.common.Messages;
import com.mcredit.model.object.ExceptionModelDTO;
import com.mcredit.model.object.ExceptionModelLeadGenDTO;
import com.mcredit.sharedbiz.validation.AuthorizationException;
import com.mcredit.sharedbiz.validation.PermissionException;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.DateUtil;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

public class ExceptionFilter implements Filter {

	private ServletContext context;

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		this.context = fConfig.getServletContext();
		this.context.log("ExceptionFilter initialized");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		try {
			chain.doFilter(request, response);
		} catch (Throwable ex) {

			response.setContentType("application/json;charset=utf-8");

			// support CORS
			HttpServletResponse httpResponse = ((HttpServletResponse) response);
			httpResponse.setHeader("Access-Control-Allow-Origin", "*");
			httpResponse.setHeader("Access-Control-Allow-Headers","origin, content-type, accept, authorization");
			httpResponse.setHeader("Access-Control-Allow-Credentials","true");
			httpResponse.setHeader("Access-Control-Allow-Methods","GET, POST, PUT, DELETE, OPTIONS, HEAD");
			
			try {
				int status = 0;
				String returnCode = "";
				String message = StringUtils.Empty;
				Throwable th = ex.getCause();
				Exception tempEx = ((Exception)th); 
				
				String statusLeadGen = "";
				String reasonLeadGen = "";
				
				if (tempEx instanceof ValidationException) {
					
					status = ((ValidationException) tempEx).getHttpStatusCode();
					
					if( !StringUtils.isNullOrEmpty(((ValidationException) tempEx).getStatus()) ) {
						statusLeadGen = ((ValidationException) tempEx).getStatus();
						reasonLeadGen = ((ValidationException) tempEx).getReason();
					}else {
						returnCode = ((ValidationException) tempEx).getCode();
						message = ex.getMessage();
					}
				}  
				else if (tempEx instanceof AuthorizationException) {
					status = ((AuthorizationException) tempEx).getHttpStatusCode();
					message = ex.getMessage();
				}
				else if (tempEx instanceof PermissionException) {
					status = ((PermissionException) tempEx).getHttpStatusCode();
					message = ex.getMessage();
				}
				else if (tempEx instanceof ExtractorContainerException) {
					status = 400;
					message = "Value Extract Error: "+ ex.getMessage();
				}
//				else if (ExtractorContainerException)
				
				else if (tempEx instanceof org.codehaus.jackson.map.exc.UnrecognizedPropertyException) {
					status = 400;
					message = "Value Extract Error: "+ ex.getMessage();
				}
				else {
					status = 500;
					if( tempEx==null )
						message = "Internal Server Error";
					else
						message = tempEx.getMessage();
				}
				httpResponse.setStatus(status);
				
				PrintWriter out = response.getWriter();
				if( !StringUtils.isNullOrEmpty(statusLeadGen) ) {
					
					ExceptionModelLeadGenDTO result = new ExceptionModelLeadGenDTO();
					
					result.setStatus(statusLeadGen);
					result.setReason(reasonLeadGen);
					result.setDateTime(DateUtil.today("yyyy-MM-dd"));
					
					messageProgress(result, reasonLeadGen);

					out.print(JSONConverter.toJSON(result));
				}else {
					
					ExceptionModelDTO result = new ExceptionModelDTO();
					if( StringUtils.isNullOrEmpty(returnCode) )
						returnCode = status + "";
					
					result.setReturnCode(returnCode);
					messageProgress(result, message);

					out.print(JSONConverter.toJSON(result));
				}
				out.flush();
				
				System.out.println(tempEx.getMessage());
			} catch (IOException e) {
				System.out.println(e.getMessage());
				System.out.println(e.getStackTrace());
				
				httpResponse.setStatus(500);
				PrintWriter out = response.getWriter();
				ExceptionModelDTO result = new ExceptionModelDTO();
				result.setReturnCode("500");
				messageProgress(result, e.getMessage());

				out.print(JSONConverter.toJSON(result));
				out.flush();
			}
			
		}
	}

	private void messageProgress(ExceptionModelDTO result, String message) {
		message = formatMessage(message);
		result.setReturnMes(replaceMessage(message));
	}
	
	private void messageProgress(ExceptionModelLeadGenDTO result, String message) {
		message = formatMessage(message);
		result.setReason(replaceMessage(message));
	}
	
	private String formatMessage(String message) {
		
		if (message.indexOf("org.codehaus.jackson.map.JsonMappingException: ") != -1 && message.indexOf("->") != -1) {

			String errEntity = message.substring(message.indexOf("->") + 2);

			if (errEntity.indexOf("[\"") != -1 && errEntity.indexOf("\"]") != -1) {

				String errField = errEntity.substring(errEntity.indexOf("[\"") + 2, errEntity.indexOf("\"]"));
				message = message.replace("org.codehaus.jackson.map.JsonMappingException: ", "");
				message = (!StringUtils.isNullOrEmpty(errField) ? "'" + errField + "': " : "") + message.substring(0, message.indexOf(":"));

				String lblInteger = Messages.getString("validation.json.integer");
				String strFmt = "Can not construct instance of {0} from String value";
				message = message.replace(MessageFormat.format(strFmt, "java.lang.Integer"), lblInteger)
								 .replace(MessageFormat.format(strFmt, "java.lang.Long"), lblInteger)
								 .replace(MessageFormat.format(strFmt, "java.math.BigDecimal"), lblInteger)
								 .replace(MessageFormat.format(strFmt, "java.util.Date"), Messages.getString("validation.json.date"));
			}
		}
		
		return message;
	}
	
	private String replaceMessage(String message) {
		
		return message.replace(
				"com.mcredit.sharedbiz.validation.ValidationException: ", "")
				.replace("com.mcredit.sharedbiz.validation.AuthorizationException: ", "")
				.replace("com.mcredit.data.DataException: ", "")
				.replace("com.mcredit.data.DataException: ", "")
				.replace("com.mcredit.validation.AuthorizationException: ", "")
				.replace("com.mcredit.sharedbiz.validation.PermissionException: ", "");
	}

	@Override
	public void destroy() {
	}
}