package com.mcredit.data.payment;

import org.hibernate.Session;

import com.mcredit.data.HibernateBase;
import com.mcredit.data.BaseUnitOfWork;
import com.mcredit.data.payment.repository.PostingConfigurationRepository;
import com.mcredit.data.payment.repository.PostingInstanceRepository;
import com.mcredit.data.repository.IRepository;

public class UnitOfWorkPayment extends BaseUnitOfWork {
	public UnitOfWorkPayment() {
		super();
	}
	
	public UnitOfWorkPayment(HibernateBase hibernateBase,Session session) {
		super(hibernateBase,session);
	}
	
	@SuppressWarnings("rawtypes")
	private IRepository _postingInstanceRepository = null ;
	@SuppressWarnings("rawtypes")	
	private IRepository _postingConfigurationRepository = null;
	
	public PostingConfigurationRepository postingConfigurationRepo() {

		if (_postingConfigurationRepository == null)
			_postingConfigurationRepository = new PostingConfigurationRepository(this.session);

		return (PostingConfigurationRepository) _postingConfigurationRepository;
	}
	
	public PostingInstanceRepository postingInstanceRepo() {

		if (_postingInstanceRepository == null)
			_postingInstanceRepository = new PostingInstanceRepository(this.session);

		return (PostingInstanceRepository) _postingInstanceRepository;
	}
	
	
	
	@Override
	public void reset(){
		super.reset();
		_postingInstanceRepository = null ;
		_postingConfigurationRepository = null;		
		_postingInstanceRepository = new PostingInstanceRepository(this.session);
		_postingConfigurationRepository = new PostingConfigurationRepository(this.session);
	}	
}
