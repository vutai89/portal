package com.mcredit.model.dto.card;

import java.io.Serializable;
import java.util.List;

public class CustomerContactInfoDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<CustomerCommunicationInfoDTO> communicationInfo;
	private List<CustomerAddressInfoDTO> addressInfo;
	
	public CustomerContactInfoDTO() {
	}

	public List<CustomerCommunicationInfoDTO> getCommunicationInfo() {
		return communicationInfo;
	}

	public void setCommunicationInfo(
			List<CustomerCommunicationInfoDTO> communicationInfo) {
		this.communicationInfo = communicationInfo;
	}

	public List<CustomerAddressInfoDTO> getAddressInfo() {
		return addressInfo;
	}

	public void setAddressInfo(List<CustomerAddressInfoDTO> addressInfo) {
		this.addressInfo = addressInfo;
	}
}