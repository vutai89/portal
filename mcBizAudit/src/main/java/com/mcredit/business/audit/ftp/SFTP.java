package com.mcredit.business.audit.ftp;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.mcredit.business.audit.config.Config;
import com.mcredit.business.audit.utils.CommonUtils;
import com.mcredit.model.enums.disaudit.AuditEnum;
import com.mcredit.model.enums.disaudit.FileFormat;
import com.mcredit.model.object.audit.DynamicFile;

public class SFTP extends FTPBase implements IFTP {
	
	public SFTP() {
		super();
	}
	
	public SFTP(Config config) {
		super(config);
	}

	@Override
	public void connect() throws Exception {
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		JSch jsch = new JSch();
		this.session = jsch.getSession(this.config.getUsername(), this.config.getFtpServer(),
				this.config.getPort());
		this.session.setPassword(this.config.getPassword());
		this.session.setConfig(config);
		this.session.connect();
		if (this.session == null) {
			throw new Exception("null");
		} else {
			Channel channel = session.openChannel("sftp");
			channel.connect();
			this.sftpClient = (ChannelSftp) channel;
		}
	}

	@Override
	public void disconnect() throws Exception {
		try {		
			if(this.sftpClient != null && this.sftpClient.isConnected())
				this.sftpClient.disconnect();
			
			if(this.session != null && this.session.isConnected())
				this.session.disconnect();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public HashMap<DynamicFile, Object> getFile(String day) throws Exception {
		HashMap<DynamicFile, Object> res = new HashMap<>();
		
		if (null == this.session || null == this.sftpClient) {
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
