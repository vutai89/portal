package com.mcredit.sharedbiz.manager;
import java.util.List;

import com.mcredit.model.dto.warehouse.EmployeeDTO;
import com.mcredit.sharedbiz.factory.UserFactory;


public class EmployeeManager extends BaseManager{

	public List<EmployeeDTO> listEmployee(String teamType, String teamGroup, String teamCode) throws Exception {
		return this.tryCatch(()-> {
			return UserFactory.getEmployeeAgg(uok).listEmployee(teamType , teamGroup, teamCode);
		});
	}

}
