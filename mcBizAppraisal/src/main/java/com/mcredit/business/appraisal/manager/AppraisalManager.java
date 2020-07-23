package com.mcredit.business.appraisal.manager;

import com.mcredit.business.appraisal.aggregate.AppraisalAggregate;
import com.mcredit.business.appraisal.validation.AppraisalValidation;
import com.mcredit.model.dto.appraisal.AppraisalDataDetailDTO;
import com.mcredit.model.dto.appraisal.AppraisalObjectDTO;
import com.mcredit.sharedbiz.manager.BaseManager;

public class AppraisalManager extends BaseManager {
	private AppraisalAggregate _agg;
	private AppraisalValidation validation = new AppraisalValidation(); 

	public AppraisalManager() {
		_agg = new AppraisalAggregate(this.uok);
	}
	
	/**
	 * Validate appraisal info
	 * @author catld.ho
	 * @param dataDetail : appraisal object sent by appraisal tool (portal)
	 * @return list result
	 * @throws Exception
	 */
	public Object validateCustomer(AppraisalDataDetailDTO dataDetail) throws Exception {
		return this.tryCatch( () -> {
			validation.checkCustomer(dataDetail);
			return _agg.validCustomer(dataDetail);
		});
	}
	
	/**
	 * Update appraisal data from appraisal tool
	 * @author catld.ho
	 * @param appraisalObj : appraisal object sent by appraisal tool (portal)
	 * @return code 200 if success
	 * @throws Exception
	 */
	public Object saveAppraisal(AppraisalObjectDTO appraisalObj) throws Exception {
		return this.tryCatch( () -> {
			validation.saveAppraisalData(appraisalObj);
			return _agg.saveAppraisal(appraisalObj);
		});
	}
	
	/**
	 * Update appraisal data from bpm
	 * @author catld.ho
	 * @param appraisalObj : appraisal object sent by bpm
	 * @return code 200 if success
	 * @throws Exception
	 */
	public Object losSaveAppraisal(AppraisalObjectDTO appraisalObj) throws Exception {
		return this.tryCatch( () -> {
			validation.losSaveAppraisalData(appraisalObj);
			return _agg.losSaveAppraisal(appraisalObj);
		});
	}
	
	/**
	 * Get appraisal data
	 * @author catld.ho
	 * @param bpmAppId : appId correspond customer profile
	 * @param action : actor request data
	 * @param ottToken : ott token sent by bpm
	 * @return appraisal data in dto model
	 * @throws Exception
	 */
	public AppraisalObjectDTO getAppraisalDataDetail(String bpmAppId, String action, String ottToken) throws Exception {
		return this.tryCatch( () -> {
			validation.getAppraisalData(bpmAppId);
			return _agg.getAppraisalData(bpmAppId, action, ottToken);
		});
	}
	
	/**
	 * Get appraisal result
	 * @author catld.ho
	 * @param appraisalObj : appraisal object include transactionId and action sent by bpm
	 * @return code 200 if appraisal pass
	 * @throws Exception
	 */
	public Object getAppraisalResult(AppraisalObjectDTO appraisalObj) throws Exception {
		return this.tryCatch( () -> {
			validation.getAppraisalResult(appraisalObj);
			return _agg.getAppraisalResult(appraisalObj);
		});
	}
}
