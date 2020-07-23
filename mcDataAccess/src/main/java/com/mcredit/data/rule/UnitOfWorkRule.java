package com.mcredit.data.rule;

import org.hibernate.Session;

import com.mcredit.data.BaseUnitOfWork;
import com.mcredit.data.HibernateBase;
import com.mcredit.data.repository.IRepository;
import com.mcredit.data.rule.repository.RuleDetailRepository;
import com.mcredit.data.rule.repository.RuleOutputRepository;

public class UnitOfWorkRule extends BaseUnitOfWork {

	public UnitOfWorkRule() {
		super();
	}
	public UnitOfWorkRule(HibernateBase hibernateBase,Session session) {
		super(hibernateBase,session);
	}

	@SuppressWarnings("rawtypes")
	private IRepository _ruleOutputRepository = null;
	@SuppressWarnings("rawtypes")
	private IRepository _ruleDetailRepository = null;

	public RuleOutputRepository ruleOutputRepo() {

		if (_ruleOutputRepository == null)
			_ruleOutputRepository = new RuleOutputRepository(
					this.session);

		return (RuleOutputRepository) _ruleOutputRepository;
	}

	public RuleDetailRepository ruleDetailRepo() {

		if (_ruleDetailRepository == null)
			_ruleDetailRepository = new RuleDetailRepository(
					this.session);

		return (RuleDetailRepository) _ruleDetailRepository;
	}

}
