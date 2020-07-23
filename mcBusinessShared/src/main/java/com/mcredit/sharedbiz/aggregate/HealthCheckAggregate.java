package com.mcredit.sharedbiz.aggregate;


import com.mcredit.data.UnitOfWork;
import com.mcredit.model.dto.StatusDTO;

public class HealthCheckAggregate {
	private UnitOfWork _uokHealthCheck = null;
	
	public HealthCheckAggregate(UnitOfWork uok) {
		this._uokHealthCheck = uok;
	}
	
	public StatusDTO getStatusConnection() {
		return this._uokHealthCheck.common.getHealtCheckRepository().getStatus();
	}

}
