package com.mcredit.model.dto.assign;

import java.util.ArrayList;
import java.util.List;

public class ManageUsersTsaDTO {
	private Long totalCount;
	private List<UserSearchTsaDTO> data;
	private Boolean availableLDAP;

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount != null ? totalCount : 0L;
	}

	public List<UserSearchTsaDTO> getData() {
		return data;
	}

	public void setData(List<UserSearchTsaDTO> data) {
		this.data = data != null ? data: new ArrayList<UserSearchTsaDTO>();
	}

	public Boolean getAvailableLDAP() {
		return availableLDAP;
	}

	public void setAvailableLDAP(Boolean availableLDAP) {
		this.availableLDAP = availableLDAP;
	}

}
