package com.mcredit.sharedbiz.service;

import com.mcredit.model.object.SendEmailDTO;
import com.mcredit.sharedbiz.dto.UserDTO;

public class ThreadSendEmail {
	private SendEmailDTO emailInfo = null;
	private UserDTO currentUser = null;
	private Long idEmailType = null;
	
	public ThreadSendEmail(SendEmailDTO emailInfo, Long idEmailType, UserDTO currentUser) {
		this.emailInfo = emailInfo;
		this.currentUser = currentUser;
		this.idEmailType = idEmailType;
	}
	
	public void send() {	
		TaskSendEmail taskSendEmail = new TaskSendEmail(emailInfo, idEmailType, currentUser);
		new Thread(taskSendEmail).start();
	}
}
