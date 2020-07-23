package com.mcredit.business.audit.ftp;

import java.util.HashMap;

import com.mcredit.model.object.audit.DynamicFile;


public interface IFTP {
	
	void connect() throws Exception;
	void disconnect() throws Exception;
	public HashMap<DynamicFile, Object> getFile(String day) throws Exception;
}
