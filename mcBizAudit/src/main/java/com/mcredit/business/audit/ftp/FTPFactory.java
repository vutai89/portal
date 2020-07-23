package com.mcredit.business.audit.ftp;


import com.mcredit.business.audit.config.Config;
import com.mcredit.model.enums.disaudit.AuditEnum;
import com.mcredit.util.JSONConverter;

public class FTPFactory {

	public static IFTP create(String config) throws Exception {
		
		Config _config = JSONConverter.toObject(config, Config.class);
		
		if (_config.getProtocol().equals(AuditEnum.FTPS.value()))
			return new FTPS(_config);
		
		if (_config.getProtocol().equals(AuditEnum.FTP.value()))
			return new FTP(_config);
		
		if (_config.getProtocol().equals(AuditEnum.SFTP.value()))
			return new SFTP(_config);
		
		throw new Exception("Invalid Protocol.");
	}

}
