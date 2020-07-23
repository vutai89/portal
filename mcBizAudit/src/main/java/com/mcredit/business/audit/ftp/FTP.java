package com.mcredit.business.audit.ftp;

import it.sauronsoftware.ftp4j.FTPClient;

import java.io.File;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mcredit.business.audit.config.Config;
import com.mcredit.model.enums.disaudit.AuditEnum;
import com.mcredit.model.enums.disaudit.FileFormat;
import com.mcredit.model.object.audit.DynamicFile;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FTP extends FTPBase implements IFTP {
	
	public FTP() {
		super();
	}
	
	public FTP(Config config) {
		super(config);
	}

	@Override
	public void connect() throws Exception {
		
		this.ftpClient = new FTPClient();
		if (!this.ftpClient.isConnected())
			ftpClient.connect(this.config.getFtpServer(), this.config.getPort());
		
		if (!this.ftpClient.isAuthenticated()) 
			ftpClient.login(this.config.getUsername(), this.config.getPassword());
		
		ftpClient.setType(FTPClient.TYPE_BINARY);
		ftpClient.setCharset("UTF-8");
		ftpClient.setAutoNoopTimeout(10 * 1000);
		ftpClient.setPassive(true);
		
	}

	@Override
	public void disconnect() throws Exception {
		try {			
			if (this.ftpClient != null && this.ftpClient.isConnected()) 
				this.ftpClient.disconnect(true);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public HashMap<DynamicFile, Object> getFile(String day) throws Exception {
		HashMap<DynamicFile, Object> res = new HashMap<>();
		
		if (!this.ftpClient.isConnected() || null == this.ftpClient)
			connect();
		
		for (DynamicFile name : this.config.getFiles()) {
			String fileName = name.getFileName().replace(AuditEnum.DDMMYYYY.value(), day);
			try {
				String saveName = fileName;
				while (saveName.contains(FileFormat.DIRECTORY_SEPARATOR.value())) {
					saveName = saveName.substring(saveName.indexOf(FileFormat.DIRECTORY_SEPARATOR.value()) + 1, saveName.length());
				}
				File file = new File(filePath + saveName);
				this.ftpClient.download(fileName, file);
				res.put(name, (Object) filePath + saveName);
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
		
		return res;
	}
}
