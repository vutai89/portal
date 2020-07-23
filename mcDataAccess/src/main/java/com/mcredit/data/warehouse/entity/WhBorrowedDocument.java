package com.mcredit.data.warehouse.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "WH_BORROWED_RETURN_DOCUMENT")
public class WhBorrowedDocument implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6621985890166777721L;

    @Id
    @GenericGenerator(name = "seq_WhBorrowedDocument", strategy = "com.mcredit.data.common.entity.SequenceGenerate", parameters = @Parameter(name = "seqName", value = "SEQ_WH_BORROWED_DOCUMENT_ID.NEXTVAL"))
    @GeneratedValue(generator = "seq_WhBorrowedDocument")
    private Long id;

    @Column(name = "WH_DOC_ID")
    private Long whDocId;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "OBJECT_TO")
    private Long objectTo;

    @Temporal(TemporalType.DATE)
    @Column(name = "APPOINTMENT_DATE")
    private Date appointmentDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "EXTENSION_DATE")
    private Date extensionDate;

    @Column(name = "APPROVE_STATUS")
    private Long approveStatus;

    @Temporal(TemporalType.DATE)
    @Column(name = "APPROVE_DATE")
    private Date approveDate;

    @Column(name = "APPROVE_BY")
    private String approveBy;

    @Column(name = "REJECT_REASON")
    private String rejectReason;

    @Column(name = "TYPE")
    private Long type;

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "DEPARTMENT_NAME")
    private String departmentName;

    @Column(name = "RECORD_STATUS")  
    private String recordStatus;

    @Temporal(TemporalType.DATE)
    @Column(name = "BORROWED_DATE")
    private Date borrowedDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "RETURN_DATE")
    private Date returnDate;

    public WhBorrowedDocument(Long whDocId, Long approveStatus, Long type, Date createdDate, String recordStatus) {
        this.whDocId = whDocId;
        this.approveStatus = approveStatus;
        this.type = type;
        this.createdDate = createdDate;
        this.recordStatus = recordStatus;
    }   

    public WhBorrowedDocument() {
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

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Date getExtensionDate() {
        return extensionDate;
    }

    public void setExtensionDate(Date extensionDate) {
        this.extensionDate = extensionDate;
    }

    public Long getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(Long approveStatus) {
        this.approveStatus = approveStatus;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public String getApproveBy() {
        return approveBy;
    }

    public void setApproveBy(String approveBy) {
        this.approveBy = approveBy;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    public Date getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(Date borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

}
