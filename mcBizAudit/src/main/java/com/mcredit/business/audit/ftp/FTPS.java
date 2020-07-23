package com.mcredit.business.audit.ftp;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPSClient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mcredit.business.audit.config.Config;
import com.mcredit.business.audit.utils.CommonUtils;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.disaudit.AuditEnum;
import com.mcredit.model.enums.disaudit.FileFormat;
import com.mcredit.model.object.audit.DynamicFile;
import com.mcredit.sharedbiz.cache.CacheManager;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FTPS extends FTPBase implements IFTP {
	
	public FTPS() {
		super();
	}
	
	public FTPS(Config config) {
		super(config);
	}

	@Override
	public void connect() throws Exception {
		this.ftpsClient = new FTPSClient();
		this.ftpsClient = new FTPSClient(true);
//			ftpsClient.setAuthValue("SSL");
		this.ftpsClient.connect(this.config.getFtpServer(), this.config.getPort());
		this.ftpsClient.login(this.config.getUsername(), this.config.getPassword());
		this.ftpsClient.execPBSZ(0);
		this.ftpsClient.execPROT("P");
		this.ftpsClient.enterLocalPassiveMode();
		this.ftpsClient.setFileType(FTP.BINARY_FILE_TYPE);

	}

	@Override
	public void disconnect() throws Exception {
		try {			
			if (this.ftpsClient != null && this.ftpsClient.isConnected()) {
				this.ftpsClient.disconnect();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public HashMap<DynamicFile, Object> getFile(String day) throws Exception {
		HashMap<DynamicFile, Object> res = new HashMap<>();
		
		if (!this.ftpsClient.isConnected() || null == this.ftpsClient) {
			connect();
		}
		
		for (DynamicFile name : this.config.getFiles()) {
			try {
				String fileName = name.getFileName().replace(AuditEnum.DDMMYYYY.value(), day);
				InputStream input = this.sftpClient.get(fileName);
				while (fileName.contains(FileFormat.DIRECTORY_SEPARATOR.value())) {
					fileName = fileName.substring(fileName.indexOf(FileFormat.DIRECTORY_SEPARATOR.value()) + 1, fileName.length());
				}
				CommonUtils.convertInputStream(input, filePath + fileName);
				IOUtils.closeQuietly(input);
				res.put(name, (Object) filePath + File.separator + fileName);
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
		
		return res;
	}
}
