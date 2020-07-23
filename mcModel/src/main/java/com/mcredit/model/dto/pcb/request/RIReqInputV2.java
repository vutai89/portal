package com.mcredit.model.dto.pcb.request;

import com.mcredit.model.dto.pcb.request.input.Contract;
import com.mcredit.model.dto.pcb.request.inputv2.Subject;

public class RIReqInputV2 {
	private Contract Contract;		// thong tin hop dong
	private Subject Subject;		// thong tin khach hang
	private String Role;			// loai lien ket
	
	public Contract getContract() {
		return Contract;
	}
	
	public void setContract(Contract contract) {
		Contract = contract;
	}
	
	public Subject getSubject() {
		return Subject;
	}
	
	public void setSubject(Subject subject) {
		Subject = subject;
	}
	
	public String getRole() {
		return Role;
	}
	
	public void setRole(String role) {
		Role = role;
	}
}
