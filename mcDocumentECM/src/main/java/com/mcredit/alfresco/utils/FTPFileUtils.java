package com.mcredit.alfresco.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class FTPFileUtils {

	private static final Logger logger = LogManager.getLogger(FTPFileUtils.class);
	private static Session session;
	private static ChannelSftp sftpChannel;
	//private static FTPSClient ftpClient;
	private static FTPClient ftpClient;
	private static Session fasSession;
	private static ChannelSftp fasSftpChannel;
	private static Session ilSession;
	private static ChannelSftp ilSftpChannel;
//	private static StandardFileSystemManager manager;
	private static Session dcSession;
	private static ChannelSftp dcSftpChannel;

	public static File getRemoteFile(String path, String fileName, String basePath, String localName) {
		String remoteFileName = null;
		String localFileName = null;
		try {
			 //remoteFileName = FilenameUtils.separatorsToUnix(getRemoteRootFolder() + basePath + File.separator +  fileName);
			remoteFileName = path;
			 localFileName = getLocalRootFolder() + basePath + File.separator + localName;
			Path pathToLocalFile = Paths.get(localFileName);
			Files.createDirectories(pathToLocalFile.getParent());
			File downloadFile = new File(localFileName);
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
            boolean success = getFtpsSession().retrieveFile(remoteFileName, outputStream);
            outputStream.close();
 
            if (success) {
            	logger.info("File " + remoteFileName + " has been downloaded successfully.");
            } else {
            	remoteFileName = path + getServerFileName(path, fileName);
    			downloadFile = new File(localFileName);
                outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
                success = getFtpsSession().retrieveFile(remoteFileName, outputStream);
                outputStream.close();
                if(success) {
                	logger.info("File " + remoteFileName + " has been downloaded successfully.");
                }
            }
 			//getSftpChannel().get(remoteFileName, localFileName);
			//logger.info("get file success");
			return downloadFile;
		}  catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getCause() + " remoteFileName:=" + remoteFileName + " localFileName:= " + localFileName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getCause() + " remoteFileName:=" + remoteFileName + " localFileName:= " + localFileName);
		}
		return null;
	}
	
	private static String getServerFileName(String serverPath, String localFile) {
		FTPFile[] files = null;
		int curDiff = -1;
		int curIndex = 0;
		int temp = 0;
		try {
			files = getFtpsSession().listFiles(serverPath);
			for(int i=0;i<files.length;i++) {
				temp = compareFileName(files[i].getName(), localFile);
				if(temp == -1) {
					continue;
				}
				if((curDiff == -1) || (curDiff > temp)) {
					curDiff = temp;
					curIndex = i;
				}
			}
		} catch (Exception e) {
			logger.error("getServerFileName error="+e.getMessage());
		}
		return files[curIndex].getName();
	}

	private static int compareFileName(String fn1, String fn2) {
		if(fn1.length() != fn2.length()) {
			return -1;
		}
		int diff = 0;
		char[] char1 = fn1.toCharArray();
		char[] char2 = fn2.toCharArray();
		for(int i=0;i<fn1.length();i++) {
			if(char1[i] != char2[i]) {
				diff++;
			}
		}
		return diff;
	}

	public static void deleteFTPFile(String path, String fileName) {
		String remoteFileName = null;
		String localFileName = null;
		try {
			remoteFileName = path + fileName;
            boolean success = getFtpsSession().deleteFile(remoteFileName);
 
            if (success) {
            	logger.info("File " + remoteFileName + " has been deleted successfully.");
            }
 			//getSftpChannel().get(remoteFileName, localFileName);
			//logger.info("get file success");
			return;
		}  catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getCause() + " Cannot delete remoteFileName:=" + remoteFileName);
			closeFtpsSession();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getCause() + " Cannot delete remoteFileName:=" + remoteFileName);
			closeFtpsSession();
		}
		return;
	}

	public static FTPClient getFtpsSession() {
		if (ftpClient == null) {
			try {
				ftpClient = new FTPClient();
				ftpClient.setControlEncoding("UTF-8");
				ftpClient.setAutodetectUTF8(true);
				ftpClient.connect(Constants.deFtpRemoteServer, Constants.deFtpRemotePort);
				boolean success = ftpClient.login(Constants.deFtpRemoteUser, Constants.deFtpRemotePassword);
				if(success) {
					ftpClient.enterLocalPassiveMode();
					ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
					int reply = ftpClient.sendCommand("OPTS", "UTF8 ON");
					logger.info("Connected to FTPS server!");
				} else {
					throw new IOException("Cannot connect to FTP server="+Constants.deFtpRemoteServer+" on port "+Constants.deFtpRemotePort);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ftpClient;
	}
	
	public static void closeFtpsSession() {
		if(ftpClient != null) {
			try {
				ftpClient.disconnect();
				ftpClient = null;
			} catch (Exception e) {
				logger.error("FTP session close error. " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	public static File getRemoteFile(String fileName, String basePath, boolean isDE) {
		String remoteFileName = null;
		String localFileName = null;
		try {
			 remoteFileName = FilenameUtils.separatorsToUnix(Constants.ilRemoteRootFolder + basePath + File.separator +  fileName);
			 localFileName = getLocalRootFolder() + (isDE ? Constants.localDEBasePath : Constants.localILBasePath) + File.separator + basePath + File.separator + fileName;
			Path pathToLocalFile = Paths.get(localFileName);
			Files.createDirectories(pathToLocalFile.getParent());
			if(isDE) {
				getSftpChannel().get(remoteFileName, localFileName);
			} else {
				getILSftpChannel().get(remoteFileName, localFileName);
			}
			logger.info("get file success");
			return new File(localFileName);
		}  catch (SftpException e) {
			e.printStackTrace();
			logger.error(e.getCause() + " remoteFileName:=" + remoteFileName + " localFileName:= " + localFileName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getCause() + " remoteFileName:=" + remoteFileName + " localFileName:= " + localFileName);
		}
		return null;
	}

	public static File getFasRemoteFile(String filePathName) {
		String remoteFileName = null;
		String localFileName = null;
		try {
			 remoteFileName = FilenameUtils.separatorsToUnix(Constants.fasRemoteRootFolder + filePathName);
			 localFileName = getLocalRootFolder() + Constants.localFASBasePath + File.separator + filePathName;
			Path pathToLocalFile = Paths.get(localFileName);
			Files.createDirectories(pathToLocalFile.getParent());
			try {
				getFasSftpChannel().get(Constants.fasFileTestConnection, getLocalRootFolder() + "TESTFILE");
			} catch (Exception e) {
				try {
					if(fasSession != null) {
						fasSession.disconnect();
					}
				} catch (Exception ex) { }
				fasSession = null;
				fasSftpChannel = null;
			}
			getFasSftpChannel().get(remoteFileName, localFileName);
			logger.info("get file success");
			return new File(localFileName);
		}  catch (SftpException e) {
			e.printStackTrace();
			logger.error(e.getCause() + " remoteFileName:=" + remoteFileName + " localFileName:= " + localFileName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getCause() + " remoteFileName:=" + remoteFileName + " localFileName:= " + localFileName);
		}
		return null;
	}

	
	public static File getDcRemoteFile(String filePathName) {
		String remoteFileName = null;
		String localFileName = null;
		try {
			 remoteFileName = FilenameUtils.separatorsToUnix(Constants.dcRemoteRootFolder + filePathName);
			 localFileName = getLocalRootFolder() + Constants.localDCBasePath + File.separator + filePathName;
			Path pathToLocalFile = Paths.get(localFileName);
			Files.createDirectories(pathToLocalFile.getParent());
//			try {
//				getDcSftpChannel().get(Constants.fasFileTestConnection, getLocalRootFolder() + "TESTFILE");
//			} catch (Exception e) {
//				try {
//					if(dcSession != null) {
//						dcSession.disconnect();
//					}
//				} catch (Exception ex) { }
//				dcSession = null;
//				dcSftpChannel = null;
//			}
			getDcSftpChannel().get(remoteFileName, localFileName);
			logger.info("get file success");
			return new File(localFileName);
		}  catch (SftpException e) {
			e.printStackTrace();
			logger.error(e.getCause() + " remoteFileName:=" + remoteFileName + " localFileName:= " + localFileName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getCause() + " remoteFileName:=" + remoteFileName + " localFileName:= " + localFileName);
		}
		return null;
	}

	
	public static Session getSession() {
		if (session == null) {
			try {
				JSch jsch = new JSch();
				session = jsch.getSession(Constants.deBPMRemoteUser, Constants.deBPMRemoteServer, Constants.deBPMRemotePort);
				session.setConfig("StrictHostKeyChecking", "no");
				session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
				session.setPassword(Constants.deBPMRemotePassword);
				session.connect();
			} catch (JSchException e) {
				e.printStackTrace();
			}
		}
		return session;
	}

	public static Session getILSession() {
		if (ilSession == null) {
			try {
				JSch jsch = new JSch();
				ilSession = jsch.getSession(Constants.ilRemoteUser, Constants.ilRemoteServer, Constants.ilRemotePort);
				ilSession.setConfig("StrictHostKeyChecking", "no");
				ilSession.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
				ilSession.setPassword(Constants.ilRemotePassword);
				ilSession.connect();
			} catch (JSchException e) {
				e.printStackTrace();
			}
		}
		return ilSession;
	}

	
	public static ChannelSftp getSftpChannel() {
		if(sftpChannel == null){
			try{
				Channel channel = getSession().openChannel("sftp");
				channel.connect();
				sftpChannel = (ChannelSftp) channel;
			}catch (JSchException e) {
				e.printStackTrace();
				logger.error(e.getCause());
			}
			
		}
		return sftpChannel;
	}

	public static ChannelSftp getILSftpChannel() {
		if(ilSftpChannel == null){
			try{
				Channel channel = getILSession().openChannel("sftp");
				channel.connect();
				ilSftpChannel = (ChannelSftp) channel;
			}catch (JSchException e) {
				e.printStackTrace();
				logger.error(e.getCause());
			}
			
		}
		return ilSftpChannel;
	}

	public static Session getFasSession() {
		if (fasSession == null) {
			try {
				JSch jsch = new JSch();
				fasSession = jsch.getSession(Constants.fasRemoteUser, Constants.fasRemoteServer, Constants.fasRemotePort);
				fasSession.setConfig("StrictHostKeyChecking", "no");
				fasSession.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
				fasSession.setPassword(Constants.fasRemotePassword);
				fasSession.connect();
			} catch (JSchException e) {
				e.printStackTrace();
			}
		}
		return fasSession;
	}

	
	public static ChannelSftp getFasSftpChannel() {
		if(fasSftpChannel == null){
			try{
				Channel channel = getFasSession().openChannel("sftp");
				channel.connect();
				fasSftpChannel = (ChannelSftp) channel;
			}catch (JSchException e) {
				e.printStackTrace();
				logger.error(e.getCause());
			}
			
		}
		return fasSftpChannel;
	}

	public static Session getDcSession() {
		if (dcSession == null) {
			try {
				JSch jsch = new JSch();
				dcSession = jsch.getSession(Constants.dcRemoteUser, Constants.dcRemoteServer, Constants.dcRemotePort);
				dcSession.setConfig("StrictHostKeyChecking", "no");
				dcSession.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
				dcSession.setPassword(Constants.dcRemotePassword);
				dcSession.connect();
			} catch (JSchException e) {
				e.printStackTrace();
			}
		}
		return dcSession;
	}

	
	public static ChannelSftp getDcSftpChannel() {
		if(dcSftpChannel == null){
			try{
				Channel channel = getDcSession().openChannel("sftp");
				channel.connect();
				dcSftpChannel = (ChannelSftp) channel;
			}catch (JSchException e) {
				e.printStackTrace();
				logger.error(e.getCause());
			}
			
		}
		return dcSftpChannel;
	}

	public static void closeSession() {
		if(session != null) {
			try {
				session.disconnect();
				session = null;
			} catch (Exception e) {
				logger.error("SFTP session close error. " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public static void closeFasSession() {
		if(fasSession != null) {
			try {
				fasSession.disconnect();
				fasSession = null;
			} catch (Exception e) {
				logger.error("SFTP session close error. " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public static void closeDcSession() {
		if(dcSession != null) {
			try {
				dcSession.disconnect();
				dcSession = null;
			} catch (Exception e) {
				logger.error("SFTP session close error. " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public static void closeILSession() {
		if(ilSession != null) {
			try {
				ilSession.disconnect();
				ilSession = null;
			} catch (Exception e) {
				logger.error("SFTP session close error. " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public static void closeAllSession() {
		closeSession();
		closeFtpsSession();
		closeFasSession();
		closeILSession();
		closeDcSession();
	}

	public static void main(String[] args) {
		String fileName = "566254660592fe6f0a284e0055394736_1.jpg";

		String OS = System.getProperty("os.name").toLowerCase();
//		String basePath = "166" + File.separator +  "713" + File.separator + "986" + File.separator + "592fe0d3ee2123007941484"
//				+ File.separator;
//		getRemoteFile(fileName, basePath);
		//FTPSClient ftp = getFtpsSession();
		String filename = "";
		try {
			//String pwd = getFtpsSession().printWorkingDirectory();
			FTPFile[] files = getFtpsSession().listDirectories();
			files = getFtpsSession().listDirectories("/3651ebd02b25dcdf424af542a7265979/2018/10/29/001163015503/CustomerIdentification");
			files = getFtpsSession().listFiles();
			files = getFtpsSession().listFiles("/3651ebd02b25dcdf424af542a7265979/2018/10/29/001163015503/CustomerIdentification");
			//InputStream inStream = getFtpsSession().retrieveFileStream("/3651ebd02b25dcdf424af542a7265979/2018/07/23/082234537/OtherDocuments/38464-Ã�Ã�KKHÃ�PHTTDLKMBâ€“M-1-TENKH.png");
			//filename = files[19].getName();
//			getILSftpChannel().get("/opt/processmaker/shared/sites/workflow/files/119/156/302/59dc6ebd7517c5015140178/16425654859dc70a01837c1085287441_1.JPG", 
//					"d:\\Temp\\bpmfile\\IL\\119\\156\\302\\59dc6ebd7517c5015140178\\16425654859dc70a01837c1085287441_1.JPG");
			System.out.println(OS);
		} catch (Exception e) {
			
		}

		//fileName = "Bâ€“M-1-TENKH.png";
		System.out.println(filename);
	      for (char c : filename.toCharArray()) {
	          System.out.printf("\\u%04x \n", (int) c); 
	       }
	      File f = getRemoteFile("/3651ebd02b25dcdf424af542a7265979/2018/07/23/082234537/OtherDocuments/", "38464-Ã�Ã�KKHÃ�PHTTDLKMBâ€“M-1-TENKH.png", "4946594085b55a74132c121020210258\\OtherDocuments", "38464-Ã�Ã�KKHÃ�PHTTDLKMBâ€“M-1-TENKH.png");
	      f = getRemoteFile("/3651ebd02b25dcdf424af542a7265979/2018/10/29/001163015503/CustomerIdentification/", "39677-CMQÄ�-1-NGUYENVANNLTT4.png", "4946594085b55a74132c121020210258\\OtherDocuments", "39677-CMQÄ�-1-NGUYENVANNLTT4.png");

		System.out.println(OS);

	}

	private static String getLocalRootFolder() {
		return Constants.localRootFolder;
	}
}
