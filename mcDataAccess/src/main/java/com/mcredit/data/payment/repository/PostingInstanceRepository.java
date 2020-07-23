package com.mcredit.data.payment.repository;

import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.payment.entity.PostingInstance;
import com.mcredit.data.repository.IUpsertRepository;

public class PostingInstanceRepository extends BaseRepository implements IUpsertRepository<PostingInstance> {

	public PostingInstanceRepository(Session session) {
		super(session);
	}

	public void update(PostingInstance item) {
		this.session.update(item);
	}

	public void upsert(PostingInstance item) {
		this.session.saveOrUpdate("PostingInstance", item);
	}

	public void remove(PostingInstance item) {
		this.session.delete("PostingInstance", item);
	}

	public List<PostingInstance> findPostingInstance() {
		return this.session.getNamedQuery("findPostingInstance").list();
	}

	public PostingInstance findPostingInstanceById(Long Id) {
		List<PostingInstance> listPostingInstances = this.session.getNamedQuery("findPostingInstanceById")
				.setParameter("id", Id).list();
		if (listPostingInstances.size() > 0)
			return listPostingInstances.get(0);
		return null;
	}

	public List<PostingInstance> findByPartnerRefId(String partnerRefId) {
		return this.session.getNamedQuery("findPostingInstanceByPartnerRefId").list();
	}
}
