package com.mcredit.business.black_list.manager;

import java.io.InputStream;
import java.util.List;

import com.mcredit.business.black_list.aggregate.BlackListAggregate;
import com.mcredit.business.black_list.validation.BlackListValidation;
import com.mcredit.model.dto.black_list.CustMonitorDTO;
import com.mcredit.sharedbiz.manager.BaseManager;

public class BlackListManager extends BaseManager{
	
	private BlackListAggregate _agg;
	private BlackListValidation _validation;
	
	public BlackListManager() {
		_agg = new BlackListAggregate(this.uok);
		_validation = new BlackListValidation();
	}
	
	
	public Object importBlackList(InputStream fileContent, String userName) throws Exception {
		return this.tryCatch(() -> {
			_validation.importBlackList(fileContent);
			return _agg.importBlackList(fileContent, userName);
		});
	}
	
	public Object save(List<CustMonitorDTO> list, String userName) throws Exception {
		return this.tryCatch(() -> {
			_validation.validateSave(list);
			return _agg.save(list, userName);
		});
	}
	
	public List<?> checkBlackList(String citizenId, String citizenIdOld, String militaryId, String companyTaxNumber) {
		return _agg.checkBlackList(citizenId, citizenIdOld, militaryId, companyTaxNumber);
	}
}
