package com.mcredit.sharedbiz.validation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class AbstractValidation {

	private String messageKey;
	private List<String> messageKeyCollection;
	private List<String> messageDescCollection;
	private ResourceBundle resourceBundle = null;

	public ResourceBundle getResourceBundle(Locale locale) {
		if(this.resourceBundle == null) {
			Locale.setDefault(new Locale(locale.getLanguage(), locale.getCountry()));
			this.resourceBundle = ResourceBundle.getBundle("messages");
		}
		return this.resourceBundle;
	}

	public String getMessageKey() {
		return this.messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public List<String> getMessageKeyCollection() {
		if(this.messageKeyCollection == null) {
			this.messageKeyCollection = new ArrayList<String>();
		}
		return messageKeyCollection;
	}

	public void setMessageKeyCollection(List<String> messageKeyCollection) {
		this.messageKeyCollection = messageKeyCollection;
	}

	public List<String> getMessageDes() {
		if(this.messageDescCollection == null) {
			this.messageDescCollection = new ArrayList<String>();
		}
		return messageDescCollection;
	}

	public void setMessageDes(List<String> messageDescCollection) {
		this.messageDescCollection = messageDescCollection;
	}
	
	public String buildValidationMessage() {
		if(this.messageDescCollection == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
        int count = 0;
		for (Iterator<String> iterator = this.messageDescCollection.iterator(); iterator.hasNext();) {
        	String msg = (String) iterator.next();
        	sb.append(msg);
        	sb.append("\r\n");
        	count++;
        }
		String result = sb.toString();
        if( count==1 )
        	result = result.replace("\r\n", "");
		return result;
	}
	
	public boolean isValid() {
		
		return messageDescCollection == null || messageDescCollection.size() == 0;
	}
}
