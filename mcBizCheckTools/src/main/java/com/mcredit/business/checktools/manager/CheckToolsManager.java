package com.mcredit.business.checktools.manager;

import com.mcredit.business.checktools.aggregate.CheckToolsAggregate;
import com.mcredit.business.checktools.validation.CheckToolsValidation;
import com.mcredit.model.dto.checktools.ConditionInitContract;
import com.mcredit.sharedbiz.manager.BaseManager;

public class CheckToolsManager extends BaseManager {
	private CheckToolsAggregate _agg;
	private CheckToolsValidation validation = new CheckToolsValidation(); 

	public CheckToolsManager() {
		_agg = new CheckToolsAggregate(this.uok);
	}
	
	/**
	 * Kiem tra thong tin cmnd co hop le khong: blacklist, duplicate hop dong, cic s37, tong du no qua 100M
	 * @param productGroup
	 * @param citizenId
	 * @param loanAmount
	 * @param appNumber
	 * @return
	 * @throws Exception
	 */
	public Object checkCitizenId(String productGroup, String citizenId, Long loanAmount, String appNumber, String saleCode) throws Exception {
		return this.tryCatch( () -> {
			validation.checkCitizenId(productGroup, citizenId);
			return _agg.checkCitizenId(productGroup, citizenId, loanAmount, appNumber, saleCode);
		});
	}
	
	/**
	 * Kiem tra dieu kien truoc khi khoi tao khoan vay (Lich su tin dung (PCB), DTI, PTI)
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	public Object checkConditionInitContract(ConditionInitContract conditionInitContract) throws Exception {
		return this.tryCatch( () -> {
			validation.checkConditionInitContract(conditionInitContract);
			return _agg.checkConditionInitContract(conditionInitContract);
		});
	}
}
