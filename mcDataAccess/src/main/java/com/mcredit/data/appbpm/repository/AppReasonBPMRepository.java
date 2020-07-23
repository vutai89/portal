package com.mcredit.data.appbpm.repository;

import java.util.List;

import javax.persistence.ParameterMode;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.repository.IRepository;
import com.mcredit.data.util.JSONFactory;
import com.mcredit.model.dto.appbpm.AppReasonBPMDTO;

public class AppReasonBPMRepository extends BaseRepository implements IRepository<AppReasonBPMDTO>{

	public AppReasonBPMRepository(Session session) {
		super(session);
	}

	/**
	 * Call procedure online_update_reason to update app reason
	 * 
	 * @param appReasonBPMTO
	 */
	public void updateAppReason(List<AppReasonBPMDTO> appReasonBPMDTO) {
		String jsonMsg = JSONFactory.toJson(appReasonBPMDTO);
		this.session.createStoredProcedureCall("online_update_reason")
				.registerStoredProcedureParameter("jsonMsg", String.class, ParameterMode.IN)
				.setParameter("jsonMsg", jsonMsg).execute();
	}

}
