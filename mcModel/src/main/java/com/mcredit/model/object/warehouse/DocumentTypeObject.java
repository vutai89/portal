package com.mcredit.model.object.warehouse;

import com.mcredit.model.dto.CodeTableDTO;

public class DocumentTypeObject  extends CodeTableDTO{
	private static final long serialVersionUID = 1L;
	public Integer extraDays;



	public Integer getExtraDays() {
		return extraDays;
	}

	public void setExtraDays(Integer extraDays) {
		this.extraDays = extraDays;
	}

}
