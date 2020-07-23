package com.mcredit.business.job.send_mail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.mcredit.data.UnitOfWork;
import com.mcredit.model.dto.ResponseSuccess;
import com.mcredit.model.dto.send_mail.DataMailDTO;
import com.mcredit.model.dto.send_mail.MailReasonDTO;
import com.mcredit.model.object.SendEmailDTO;
import com.mcredit.sharedbiz.BusinessLogs;

public class SendMailAggregate {
	private UnitOfWork _uok = null;
	protected String emailAutoAbort = null ;
	protected String emailUserAutoAbort = null ;
	protected String emailPassAutoAbort = null ;
	
	public SendMailAggregate(String emailAutoAbort, String emailUserAutoAbort, String emailPassAutoAbort, UnitOfWork _uok) {
		this.emailAutoAbort = emailAutoAbort;
		this.emailUserAutoAbort = emailUserAutoAbort;
		this.emailPassAutoAbort = emailPassAutoAbort;
		this._uok = _uok;
	}
	
	public Object sendMailWarn(String subject, String body) {
		List<DataMailDTO> data = this._uok.sendMail.createSendMailRepository().getDataMail();
		System.out.println(new BusinessLogs().writeLog("List Data send mail: " + new Gson().toJson(data)));
		if(!CollectionUtils.isEmpty(data)){
			for(DataMailDTO obj : data) {
				if(StringUtils.isNotBlank(obj.getMailSale())) {
					String reqBody = null;
					String reqSubject = null;
					List<MailReasonDTO> reason = this._uok.sendMail.createSendMailRepository().getReasonById(obj.getAppId());
					String strReason = "";
					if(!CollectionUtils.isEmpty(reason)){
						for(MailReasonDTO mr : reason) {
							strReason += (mr.getCode() + " - " + mr.getDescription() + (StringUtils.isNotBlank(mr.getComment()) ? (" - " + mr.getComment() + "</br>") : "</br>"));
						}
					}
					reqBody = body.replace("caseNumber", StringUtils.isNotBlank(obj.getAppNumber()) ? obj.getAppNumber() : "")
							.replace("custName", StringUtils.isNotBlank(obj.getCustName()) ? obj.getCustName() : "")
							.replace("contractCode", StringUtils.isNotBlank(obj.getContractNumber()) ? obj.getContractNumber() : "")
							.replace("contractDate", StringUtils.isNotBlank(obj.getContractDate()) ? obj.getContractDate() : "")
							.replace("status", StringUtils.isNotBlank(obj.getStatus()) ? obj.getStatus() : "")
							.replace("reason", strReason);
					reqSubject = subject.replace("caseNumber", StringUtils.isNotBlank(obj.getAppNumber()) ? obj.getAppNumber() : "")
							.replace("custName", StringUtils.isNotBlank(obj.getCustName()) ? obj.getCustName() : "");
					List<String> mailTo = new ArrayList<>();
					System.out.println("mail CA: " + obj.getMailSale());
					mailTo.add(obj.getMailSale().trim());
//					mailTo.add("hoangnh2.ho@mcredit.com.vn");
					List<String> mailCC = new ArrayList<>();
					if(StringUtils.isNotBlank(obj.getMailLead())) mailCC.add(obj.getMailLead().trim());
					SendEmailDTO send = new SendEmailDTO(emailUserAutoAbort, emailPassAutoAbort, emailAutoAbort, 
							(!CollectionUtils.isEmpty(mailTo) ? new ArrayList<>(mailTo) : null), //mail to
							(!CollectionUtils.isEmpty(mailCC) ? new ArrayList<>(mailCC) : null), //mail cc
							null, reqSubject, reqBody);
					System.out.println(new BusinessLogs().writeLog("Content send mail: " + new Gson().toJson(send)));
					//Send mail
					try {
						new com.mcredit.sharedbiz.service.MessageService(send).send();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		return new ResponseSuccess();
	}

}
