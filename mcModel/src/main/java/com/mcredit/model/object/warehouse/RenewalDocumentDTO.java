package com.mcredit.model.object.warehouse;

import java.util.Date;

public class RenewalDocumentDTO {
	String renewalDate;
	Date appointmentDate;
	Long objectTo;
	Long whDocId;
	String departmentName;
	public String getRenewalDate() {
		return renewalDate;
	}
	public void setRenewalDate(String renewalDate) {
		this.renewalDate = renewalDate;
	}
	public Date getAppointmentDate() {
		return appointmentDate;
	}
	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
	}
	public Long getObjectTo() {
		return objectTo;
	}
	public void setObjectTo(Long objectTo) {
		this.objectTo = objectTo;
	}
	public Long getWhDocId() {
		return whDocId;
	}
	public void setWhDocId(Long whDocId) {
		this.whDocId = whDocId;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}


}
