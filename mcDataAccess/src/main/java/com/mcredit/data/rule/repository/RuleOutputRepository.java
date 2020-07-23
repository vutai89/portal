package com.mcredit.data.rule.repository;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.DataException;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.data.rule.entity.RuleOutput;

public class RuleOutputRepository extends BaseRepository implements IUpsertRepository<RuleOutput> {

	public RuleOutputRepository(Session session) {
		super(session);
	}

	public void update(RuleOutput item) throws DataException {
		this.session.update("RuleOutput", item);
	}

	public void upsert(RuleOutput item) throws DataException {
		if( item.getId() != null )
			updateEntity(item, "RuleOutput");
		else
			this.session.save("RuleOutput", item);
	}
	
	public void remove(RuleOutput item) throws DataException {
		this.session.delete("RuleOutput", item);
	}
	
	public List<RuleOutput> findOutputByRuleId(Integer ruleId) {
		List<RuleOutput> ruleList = this.session.createNamedQuery("findOutputByRuleId1", RuleOutput.class)
				.setParameter("ruleId", ruleId)
				.list();

		List<RuleOutput> retList = new ArrayList<RuleOutput>();
		if(ruleList != null && ruleList.size() > 0) {
			retList.addAll(ruleList);
		}
		ruleList = null;
		ruleList = this.session.createNamedQuery("findOutputByRuleId2", RuleOutput.class)
				.setParameter("ruleId", ruleId)
				.list();
		if(ruleList != null && ruleList.size() > 0) {
			retList.addAll(ruleList);
		}

		return retList;
	}

	public List<RuleOutput> findOutputByRuleCode(String ruleCode) {
		List<RuleOutput> ruleList = this.session.createNamedQuery("findOutputByRuleCode1", RuleOutput.class)
				.setParameter("ruleCode", ruleCode)
				.list();

		List<RuleOutput> retList = new ArrayList<RuleOutput>();
		if(ruleList != null && ruleList.size() > 0) {
			retList.addAll(ruleList);
		}
		ruleList = null;
		ruleList = this.session.createNamedQuery("findOutputByRuleCode2", RuleOutput.class)
				.setParameter("ruleCode", ruleCode)
				.list();
		if(ruleList != null && ruleList.size() > 0) {
			retList.addAll(ruleList);
		}

		return retList;
	}

}
