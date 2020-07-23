package com.mcredit.repayment.validation;

import com.mcredit.common.Messages;
import com.mcredit.repayment.dto.RepaymentSchedulePayload;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;

public class RepaymentScheduleValidation extends AbstractValidation {
	public void validateRepaymentSchedule(RepaymentSchedulePayload payload) throws ValidationException {
		String mesage = "\u0110\u1EC1 ngh\u1ECB nh\u1EADp ";
		if (payload.getValueDate() == null) throw new ValidationException(Messages.getString(mesage+" ValueDate"));
		else if (payload.getValueDate().getYear() < 80) throw new ValidationException(Messages.getString("Value Date kh\u00F4ng \u0111\u00FAng \u0111\u1ECBnh d\u1EA1ng"));
		else if (payload.getTenor() == null) throw new ValidationException(Messages.getString(mesage+" tenor"));
		else if (payload.getIntRate() == null) throw new ValidationException(Messages.getString(mesage+" intRate"));
		else if (payload.getCurrentOutstanding() == null) throw new ValidationException(Messages.getString(mesage+" currentOutstanding"));
	}
	
}
