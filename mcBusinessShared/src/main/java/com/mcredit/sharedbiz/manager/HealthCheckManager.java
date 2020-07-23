package com.mcredit.sharedbiz.manager;

import com.mcredit.model.dto.StatusDTO;
import com.mcredit.sharedbiz.factory.HealthCheckFactory;

public class HealthCheckManager extends BaseManager {
	
	public StatusDTO getStatus() throws Exception {
		return this.tryCatch(()->{
			return HealthCheckFactory.getHealthCheckAgg(this.uok).getStatusConnection();
		});
	}
	

}
