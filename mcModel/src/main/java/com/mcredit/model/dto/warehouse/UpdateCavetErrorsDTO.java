package com.mcredit.model.dto.warehouse;
import java.util.List;

public class UpdateCavetErrorsDTO {
	Boolean isCavetHasError;
	List<Integer> errorIds;
	
	public Boolean getIsCavetHasError() {
		return isCavetHasError;
	}
	
	public void setIsCavetHasError(Boolean isCavetHasError) {
		this.isCavetHasError = isCavetHasError;
	}
	
	public List<Integer> getErrorIds() {
		return errorIds;
	}
	
	public void setErrorIds(List<Integer> errorIds) {
		this.errorIds = errorIds;
	}
	
}
