package com.mcredit.sharedbiz.aggregate;

import java.util.List;

import com.mcredit.data.UnitOfWork;
import com.mcredit.model.dto.appbpm.AppReasonBPMDTO;
import com.mcredit.model.dto.appbpm.AppStatusBPMDTO;

public class ApplicationBPMAggregate {


	
	private UnitOfWork _uok = null;

	public ApplicationBPMAggregate(UnitOfWork _uok) {
		super();
		this._uok = _uok;
	}
	
	public void updateAppStatus(AppStatusBPMDTO appStatusBPMDTO) {
		this._uok.appBPM.createAppStatusBPMRepo().updateAppStatus(appStatusBPMDTO);
	}
	
	public void updateAppReason(List<AppReasonBPMDTO> appReasonBPMDTO) {
		this._uok.appBPM.createAppReasonBPMRepo().updateAppReason(appReasonBPMDTO);
		
	}
}
