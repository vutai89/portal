package com.mcredit.business.mobile.utils;

import it.sauronsoftware.ftp4j.FTPClient;

import com.mcredit.model.enums.ParametersName;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;

public class FTPUtils {

	public static FTPClient createFTPConnection(){
		try {
			FTPClient client = new FTPClient();
			ParametersCacheManager _cache = CacheManager.Parameters();
			String UrlFTP = _cache.findParamValueAsString(ParametersName.MFS_FTP_HOST);
			String ftpUser = _cache.findParamValueAsString(ParametersName.MFS_FTP_USER);
			String ftpPass = _cache.findParamValueAsString(ParametersName.MFS_FTP_PASSWORD);
			String port = _cache.findParamValueAsString(ParametersName.MFS_FTP_PORT);
			
			if(!client.isConnected()) {
				client.connect(UrlFTP, Integer.parseInt(port));
			}
	        if(!client.isAuthenticated()) {
	            client.login(ftpUser, ftpPass);
	        }
	        client.setType(FTPClient.TYPE_BINARY);
	        client.setCharset("UTF-8");
	        client.setAutoNoopTimeout(10*1000);
	        return client;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
