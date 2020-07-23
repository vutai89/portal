package com.mcredit.data.payment.repository;

import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.payment.entity.PostingConfiguration;
import com.mcredit.data.repository.IUpsertRepository;

public class PostingConfigurationRepository extends BaseRepository implements IUpsertRepository<PostingConfiguration> {

	public PostingConfigurationRepository(Session session) {
		super(session);
	}

	public void upsert(PostingConfiguration item) {		
		this.session.saveOrUpdate("PostingConfiguration", item);
	}

	public void remove(PostingConfiguration item) {
		this.session.delete("PostingConfiguration", item);
	}
	
	@SuppressWarnings("unchecked")
	public List<PostingConfiguration> findPostingConfiguration() {
		return this.session.getNamedQuery("findPostingConfiguration").list();
	}	
}