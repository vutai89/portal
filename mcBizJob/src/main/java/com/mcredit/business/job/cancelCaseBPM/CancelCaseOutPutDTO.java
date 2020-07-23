package com.mcredit.business.job.cancelCaseBPM;

import java.io.Serializable;
import java.util.List;

public class CancelCaseOutPutDTO implements Serializable {
	private static final long serialVersionUID = 3983627927868456381L;
	
	int succsesss;
	List<String> listAppIdSucess;
	int errors ;
	List<String> listAppIdError;
	int rejects ;
	List<String> listAppIdRejects;
	int exceptions ;
	List<String> listAppIdExceptions;
	
	public CancelCaseOutPutDTO() {
		super();
	}
	
	public CancelCaseOutPutDTO(List<String> listAppIdSucess, List<String> listAppIdError, List<String> listAppIdRejects, List<String> listAppIdExceptions) {
		super();
		this.succsesss = listAppIdSucess.size();
		this.listAppIdSucess = listAppIdSucess;
		this.errors = listAppIdError.size();
		this.listAppIdError = listAppIdError;
		this.rejects = listAppIdRejects.size();
		this.listAppIdRejects = listAppIdRejects;
		this.exceptions = listAppIdExceptions.size();
		this.listAppIdExceptions = listAppIdExceptions;
	}
	
	public int getRejects() {
		return rejects;
	}
	public void setRejects(int rejects) {
		this.rejects = rejects;
	}
	public List<String> getListAppIdRejects() {
		return listAppIdRejects;
	}
	public void setListAppIdRejects(List<String> listAppIdRejects) {
		this.listAppIdRejects = listAppIdRejects;
	}
	public int getSuccsesss() {
		return succsesss;
	}
	public void setSuccsesss(int succsesss) {
		this.succsesss = succsesss;
	}
	public List<String> getListAppIdSucess() {
		return listAppIdSucess;
	}
	public void setListAppIdSucess(List<String> listAppIdSucess) {
		this.listAppIdSucess = listAppIdSucess;
	}
	public int getErrors() {
		return errors;
	}
	public void setErrors(int errors) {
		this.errors = errors;
	}
	public List<String> getListAppIdError() {
		return listAppIdError;
	}
	public void setListAppIdError(List<String> listAppIdError) {
		this.listAppIdError = listAppIdError;
	}
	public int getExceptions() {
		return exceptions;
	}
	public void setExceptions(int exceptions) {
		this.exceptions = exceptions;
	}
	public List<String> getListAppIdExceptions() {
		return listAppIdExceptions;
	}
	public void setListAppIdExceptions(List<String> listAppIdExceptions) {
		this.listAppIdExceptions = listAppIdExceptions;
	}
	
}
