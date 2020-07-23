package com.mcredit.business.mobile.factory;

import com.mcredit.business.mobile.aggregate.CaseAggregate;
import com.mcredit.business.mobile.aggregate.CommonAggregate;
import com.mcredit.business.mobile.aggregate.MobileAggregate;
import com.mcredit.business.mobile.validation.ReportValidation;
import com.mcredit.data.UnitOfWork;
import com.mcredit.sharedbiz.dto.UserDTO;

public class MobileFactory {
	public static ReportValidation getReportValidation() {
		return new ReportValidation();
	}
	
	public static MobileAggregate getMobileAggregate(UnitOfWork unitOfWork) {
		return new MobileAggregate(unitOfWork);
	}
	
	public static CaseAggregate getCaseAggreagate(UnitOfWork unitOfWork, UserDTO user) {
		return new CaseAggregate(unitOfWork, user);
	}
	
	public static CommonAggregate getCommonAggreagate(UnitOfWork unitOfWork) {
		return new CommonAggregate(unitOfWork);
	}
	
}
