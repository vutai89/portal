package com.mcredit.data.common.repository;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.common.entity.SendEmail;
import com.mcredit.data.repository.IAddRepository;
import com.mcredit.data.repository.IGetRepository;
import com.mcredit.data.repository.IRepository;
import com.mcredit.data.repository.IUpdateRepository;

public class SendEmailRepository extends BaseRepository implements IRepository<SendEmail>, IAddRepository<SendEmail>, IGetRepository<SendEmail>, IUpdateRepository<SendEmail> {
	
	public SendEmailRepository(Session session) {
		super(session);
	}
	
	@Override
	public SendEmail get(Long id) {
		return this.session.get(SendEmail.class, id);
	}

	@Override
	public void add(SendEmail item) {
		this.session.save(item);
	}
	
	@Override
	public void update(SendEmail item) {
		this.session.update(item);
	}
	
}
