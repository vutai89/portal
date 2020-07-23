package com.mcredit.business.job.send_mail;

import com.mcredit.business.job.constant.Constant;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.manager.BaseManager;

public class SendMailManager extends BaseManager {
	protected SendMailAggregate sendMailAggregate = null;
	protected String emailAutoAbort =  CacheManager.Parameters().findParamValueAsString(ParametersName.NOTIFICATION_VHGN_EMAIL) ;
	protected String emailUserAutoAbort =  CacheManager.Parameters().findParamValueAsString(ParametersName.NOTIFICATION_VHGN_SMTP_USER) ;
	protected String emailPassAutoAbort =  CacheManager.Parameters().findParamValueAsString(ParametersName.NOTIFICATION_VHGN_EMAIL_SMTP_PASS) ;
	
	public SendMailManager() {
		this.sendMailAggregate = new SendMailAggregate(emailAutoAbort, emailUserAutoAbort, emailPassAutoAbort, this.uok);
		
	}
	
	public Object sendMailWarn() {
		String subject = Constant.SEND_MAIL.SUBJECT;
		String body = Constant.SEND_MAIL.BODY;
		return this.sendMailAggregate.sendMailWarn(subject, body);
	}

}
