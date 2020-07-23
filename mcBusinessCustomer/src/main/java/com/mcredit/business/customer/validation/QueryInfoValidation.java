package com.mcredit.business.customer.validation;

import java.util.Arrays;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.enums.QueyInfo;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class QueryInfoValidation extends AbstractValidation {

	public void validate(String type, String contractNumbers, String identityNumber, String militaryId,
			String mobilePhone) throws ValidationException {

		if ("".equals(type))
			throw new ValidationException("400", Messages.getString("validation.field.madatory", Labels.getString("label.customer.queryInfo.typeMissing")));

		if (!Arrays.asList(new String[] { QueyInfo.CREDIT_CONTRACT.value(), QueyInfo.PAYMENT_ON_WEB.value() })
				.contains(type))
			throw new ValidationException("400", Messages.getString("validation.field.invalidFormat", Labels.getString("label.customer.queryInfo.typeMissing")));

		if ("".equals(contractNumbers) && "".equals(identityNumber) && "".equals(militaryId))
			throw new ValidationException("400.01", Messages.getString("validation.field.madatory", Labels.getString("label.customer.queryInfo.conditionMissing")));

		if (!"".equals(contractNumbers) && (contractNumbers.length() != 16 || !StringUtils.isNumberic(contractNumbers)))
			throw new ValidationException("400.02", Messages.getString("validation.field.invalidFormat", Labels.getString("label.customer.queryInfo.contractNumbersInvalid")));

		if (!"".equals(identityNumber)) {
			if (!Arrays.asList(new String[] { "9", "12" }).contains(identityNumber.length() + "")
					|| !StringUtils.isNumberic(identityNumber))
				throw new ValidationException("400.03",
						Messages.getString("validation.field.invalidFormat", Labels.getString("label.customer.queryInfo.identityNumberInvalid")));
		}

		if (!"".equals(militaryId) && militaryId.length() > 15)
			throw new ValidationException("400.07", Messages.getString("validation.field.invalidFormat", Labels.getString("label.customer.queryInfo.militaryIdInvalid")));
	}
}
