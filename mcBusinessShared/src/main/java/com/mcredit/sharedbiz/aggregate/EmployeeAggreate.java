package com.mcredit.sharedbiz.aggregate;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.mcredit.data.UnitOfWork;
import com.mcredit.data.user.entity.Employee;
import com.mcredit.model.dto.warehouse.EmployeeDTO;

public class EmployeeAggreate {
	
	private UnitOfWork _uok = null;
	private static ModelMapper modelMapper = new ModelMapper();
	
	public EmployeeAggreate(UnitOfWork unitOfWorkWareHouse) {
		this._uok = unitOfWorkWareHouse;
	}
	
	public List<EmployeeDTO> listEmployee(String teamType, String teamGroup, String teamCode) throws Exception {
		
		List<EmployeeDTO> employeeDTOs = new ArrayList<EmployeeDTO>();
		
		List<Employee> employees =  this._uok.user.employeeRepo().listEmployee(teamType, teamGroup, teamCode);
		if(employees != null && !employees.isEmpty())
			for (Employee employee : employees) {
				employeeDTOs.add(modelMapper.map(employee,EmployeeDTO.class));
			}
			//modelMapper.map(employees, employeeDTOs);
			
		return employeeDTOs;
	}
}
