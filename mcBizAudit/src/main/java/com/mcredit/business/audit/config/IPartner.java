package com.mcredit.business.audit.config;

import java.util.HashMap;
import java.util.List;

import com.mcredit.model.object.audit.Audit;
import com.mcredit.model.object.audit.DynamicFile;

public interface IPartner {
	
	HashMap<DynamicFile, Object> getFile(String day) throws Exception;
	public HashMap<String,Audit> readDetailFile(String day, List<Audit> duplicateRecords) throws Exception;

}
