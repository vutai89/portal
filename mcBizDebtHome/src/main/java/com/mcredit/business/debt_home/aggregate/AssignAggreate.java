package com.mcredit.business.debt_home.aggregate;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcredit.business.debt_home.callout.esbAPI;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.debt_home.entity.DebtHomeAssign;
import com.mcredit.model.dto.debt_home.DebtHomeAllFile;
import com.mcredit.model.dto.debt_home.DebtHomeAssignDTO;

public class AssignAggreate {
	private UnitOfWork uok = null;
	public AssignAggreate(UnitOfWork uok) {
		this.uok = uok;
	}
	
	DebtHomeAssign d = null;
	public Integer assignDebtHome(List<DebtHomeAssignDTO> debtHomeAssigns, String currentUser) {
		
		this.disableExistData();
		
		for (DebtHomeAssignDTO assign: debtHomeAssigns) {
			d = new DebtHomeAssign(assign.getContractNumber(), assign.getUserAssign(), assign.getTeamlead(), assign.getAdministrator(), currentUser);
			this.uok.debtHome.debtHomeAssignRepository().add(d);
		}
		
		return debtHomeAssigns.size();
	}
	
	public void disableExistData() {
		this.uok.debtHome.debtHomeAssignRepository().disableExistData();
	}
}






