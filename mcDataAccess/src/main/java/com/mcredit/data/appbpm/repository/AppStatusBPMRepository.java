package com.mcredit.data.appbpm.repository;

import javax.persistence.ParameterMode;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.repository.IRepository;
import com.mcredit.data.util.JSONFactory;
import com.mcredit.model.dto.appbpm.AppStatusBPMDTO;
//import com.mcredit.product.utils.JSONFactory;

public class AppStatusBPMRepository extends BaseRepository implements IRepository<AppStatusBPMDTO> {

	public AppStatusBPMRepository(Session session) {
		super(session);
	}

	/**
	 * Call procedure online_update_app_status to update app status
	 * 
	 * @param appStatusBPMDTO
	 */
	public void updateAppStatus(AppStatusBPMDTO appStatusBPMDTO) {
		String jsonMsg = JSONFactory.toJson(appStatusBPMDTO);
		this.session.createStoredProcedureCall("online_update_app_status")
				.registerStoredProcedureParameter("jsonMsg", String.class, ParameterMode.IN)
				.setParameter("jsonMsg", jsonMsg).execute();
	}

}
