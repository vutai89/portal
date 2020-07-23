package com.mcredit.data.common.repository;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.repository.IRepository;
import com.mcredit.model.enums.StatusConnection;
import com.mcredit.model.dto.StatusDTO;
import com.mcredit.util.StringUtils;

public class HealthCheckRepository extends BaseRepository implements IRepository<StatusDTO>{
	
	public HealthCheckRepository(Session session) {
		super(session);
		// TODO Auto-generated constructor stub
	}

	public StatusDTO getStatus() {
		StatusDTO status = new StatusDTO();
		String mess = this.session.createNativeQuery("SELECT 1 FROM DUAL").getSingleResult().toString();
	    if (!mess.isEmpty()) {
	    	status.setServerName(StringUtils.getComputerName());
	    	status.setStatusDB(StatusConnection.CONNECTION_OK.value());
	    	status.setMessage(StatusConnection.CONNECTION_MESSEAGE_SUCCESS.value());
	    } else {
	    	status.setServerName(StringUtils.getComputerName());
	    	status.setStatusDB(StatusConnection.CONNECTION_ERROR.value());
	    	status.setMessage(StatusConnection.CONNECTION_MESSAGE_ERROR.value());
	    }
		return status;
	}
}
