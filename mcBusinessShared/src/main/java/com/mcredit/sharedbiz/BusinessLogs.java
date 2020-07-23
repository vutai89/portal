package com.mcredit.sharedbiz;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BusinessLogs {
	
	public String writeLog(String message) {
		String log = "";
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
		log = "LOG_REQUEST_" + format.format(new Date()) + ": " + message;
		return log;
	}
	
	public String writeURN(String url, String loginId) {
		String log = "";
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
		log = "LOG_URL_" + format.format(new Date()) + ": " + url + " LoginId: " + loginId;
		return log;
	}
}
