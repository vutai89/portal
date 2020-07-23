/**
 * 
 */
package com.mcredit.model.dto;

/**
 * @author anhdv.ho
 *
 */
public class ResponseObject {
	
	private int httpStatusCode;
	private String jsonMessage;
	
	public ResponseObject() {
		this.httpStatusCode = 200; //By default
		//setHttpStatusCode(200);
	}
	public int getHttpStatusCode() {
		return httpStatusCode;
	}
	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}
	public String getJsonMessage() {
		return jsonMessage;
	}
	public void setJsonMessage(String jsonMessage) {
		this.jsonMessage = jsonMessage;
	}
}
