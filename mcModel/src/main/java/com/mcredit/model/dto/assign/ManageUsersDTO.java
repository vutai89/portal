package com.mcredit.model.dto.assign;

import java.util.ArrayList;
import java.util.List;

public class ManageUsersDTO {
	private Long totalCount;
	private List<UserSearchDTO> data;
	private Boolean availableLDAP;

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount != null ? totalCount : 0L;
	}

	public List<UserSearchDTO> getData() {
		return data;
	}

	public void setData(List<UserSearchDTO> data) {
		this.data = data != null ? data: new ArrayList<UserSearchDTO>();
	}

	public Boolean getAvailableLDAP() {
		return availableLDAP;
	}

	public void setAvailableLDAP(Boolean availableLDAP) {
		this.availableLDAP = availableLDAP;
	}

}
