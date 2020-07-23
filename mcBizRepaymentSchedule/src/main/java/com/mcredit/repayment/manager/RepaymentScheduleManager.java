package com.mcredit.repayment.manager;

import com.mcredit.repayment.aggregate.RepaymentScheduleAggregate;
import com.mcredit.repayment.dto.RepaymentSchedulePayload;
import com.mcredit.repayment.validation.RepaymentScheduleValidation;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.manager.BaseManager;
import com.mcredit.sharedbiz.validation.ValidationException;

public class RepaymentScheduleManager extends BaseManager{
	
	private RepaymentScheduleAggregate _agg;
	private UserDTO _user;
	private RepaymentScheduleValidation _repaymentScheduleValidation;

	public RepaymentScheduleManager() throws ValidationException {
		_agg = new RepaymentScheduleAggregate(this.uok,this._user);
		_repaymentScheduleValidation = new RepaymentScheduleValidation();
	}

	public Object getRepaymentSchedule(RepaymentSchedulePayload payload) throws Exception {
		this._repaymentScheduleValidation.validateRepaymentSchedule(payload);
		return this.tryCatch(() -> {
			return _agg.repaymentScheduleData(payload);
		});
	}
	
}
