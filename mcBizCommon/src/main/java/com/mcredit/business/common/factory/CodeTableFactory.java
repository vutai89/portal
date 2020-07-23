package com.mcredit.business.common.factory;

import com.mcredit.business.common.validation.CodeTableValidation;

public class CodeTableFactory {

	public static CodeTableValidation createCodeTableValidation() {

		return new CodeTableValidation();
	}
}
