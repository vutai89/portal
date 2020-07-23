package com.mcredit.data.common.repository;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.common.entity.Parameters;
import com.mcredit.data.repository.IMergeRepository;

public class ParametersRepository extends BaseRepository implements IMergeRepository<Parameters> {

	public ParametersRepository(Session session) {
		super(session);
	}

	public void upsert(Parameters item) {
		this.session.saveOrUpdate("Parameters", item);
	}
	
	public void merge(Parameters item) {
		this.session.merge("Parameters", item);
	}

	public void remove(Parameters item) {
		this.session.delete("Partner", item);
	}
	
	@SuppressWarnings("unchecked")
	public List<Parameters> findParameters() {
		return this.session.getNamedQuery("findParameters").list();
	}	
	public Parameters findParameter(String paramName) {
		Query query = this.session.getNamedQuery("findParameter");
		query.setParameter("paramName", paramName);
		return (Parameters) query.getSingleResult();
	}	

}