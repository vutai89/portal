package com.mcredit.data.send_mail;

import org.hibernate.Session;

import com.mcredit.data.BaseUnitOfWork;
import com.mcredit.data.HibernateBase;
import com.mcredit.data.send_mail.repository.SendMailRepository;

public class UnitOfWorkSendMail extends BaseUnitOfWork {
	
	public UnitOfWorkSendMail(){
		super();
	}
	
	public UnitOfWorkSendMail(HibernateBase hibernateBase, Session session) {
		super(hibernateBase, session);
	}
	
	private SendMailRepository _sendMailRepository = null;
	
	public SendMailRepository createSendMailRepository() {
		if (_sendMailRepository == null)
			_sendMailRepository = new SendMailRepository(this.session);
		return (SendMailRepository) _sendMailRepository;
	}

}
