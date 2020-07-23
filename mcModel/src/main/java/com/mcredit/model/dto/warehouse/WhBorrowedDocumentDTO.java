package com.mcredit.model.dto.warehouse;


public class WhBorrowedDocumentDTO {
	
	private Long id;
	
	private Long whDocId;

	private Long objectTo;

	private String appointmentDate;

	private String extensionDate;

	private Long approveStatus;

	private String approveDate;

	private String approveBy;

	private Long type;

	private String createdBy;

	private String createdDate;

	private String departmentName;

	public WhBorrowedDocumentDTO(){
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getWhDocId() {
		return whDocId;
	}

	public void setWhDocId(Long whDocId) {
		this.whDocId = whDocId;
	}

	public Long getObjectTo() {
		return objectTo;
	}

	public void setObjectTo(Long objectTo) {
		this.objectTo = objectTo;
	}

	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getExtensionDate() {
		return extensionDate;
	}

	public void setExtensionDate(String extensionDate) {
		this.extensionDate = extensionDate;
	}

	public Long getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(Long approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(String approveDate) {
		this.approveDate = approveDate;
	}

	public String getApproveBy() {
		return approveBy;
	}

	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	
}
