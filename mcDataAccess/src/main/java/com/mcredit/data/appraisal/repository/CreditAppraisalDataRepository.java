package com.mcredit.data.appraisal.repository;

import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.appraisal.entity.CreditAppraisalData;
import com.mcredit.data.repository.IAddRepository;
import com.mcredit.data.repository.IUpdateRepository;
import com.mcredit.data.repository.IUpsertRepository;

public class CreditAppraisalDataRepository extends BaseRepository implements IUpsertRepository<CreditAppraisalData>,IUpdateRepository<CreditAppraisalData>,IAddRepository<CreditAppraisalData> {

	public CreditAppraisalDataRepository(Session session) {
		super(session);
	}

	@Override
	public void add(CreditAppraisalData item) {
		this.session.save(item);
	}

	@Override
	public void update(CreditAppraisalData item) {
		this.session.update(item);
	}

	@Override
	public void upsert(CreditAppraisalData item) {
		this.session.saveOrUpdate("CreditAppraisalData", item);
	}

	/**
	 * Get appraisal data by bpmAppId
	 * @author catld.ho
	 * @param bpmAppId : appId correspond customer profile
	 * @return appraisal data
	 */
	public CreditAppraisalData getAppraisalInfo(String bpmAppId) {
		List<CreditAppraisalData> result = this.session.createNamedQuery("getAppraisalInfo", CreditAppraisalData.class)
				.setParameter("bpmAppId", bpmAppId)
				.getResultList();
		return !result.isEmpty() ? result.get(0) : null;
	}

	/**
	 * Get appraisal data by bpmAppId and action
	 * @author catld.ho
	 * @param bpmAppId : appId correspond customer profile
	 * @param action : actor call service
	 * @return appraisal data
	 */
	public CreditAppraisalData getAppraisalInfoByAction(String bpmAppId, List<String> action) {
		List<CreditAppraisalData> result = this.session.createNamedQuery("getAppraisalInfoByAction", CreditAppraisalData.class)
				.setParameter("bpmAppId", bpmAppId)
				.setParameter("action", action)
				.getResultList();
		return !result.isEmpty() ? result.get(0) : null;
	}

	/**
	 * Get appraisal result
	 * @author catld.ho
	 * @param transactionId : session id
	 * @return appraisal data include appraisal result
	 */
	public CreditAppraisalData getAppraisalResult(String transactionId) {
		List<CreditAppraisalData> result = this.session.createNamedQuery("getAppraisalResult", CreditAppraisalData.class)
				.setParameter("transactionId", transactionId)
				.getResultList();
		return !result.isEmpty() ? result.get(0) : null;
	}
	
}
