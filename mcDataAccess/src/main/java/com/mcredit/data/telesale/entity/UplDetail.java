package com.mcredit.data.telesale.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * The persistent class for the UPL_DETAIL database table.
 *
 */
@Entity
@Table(name = "UPL_DETAIL")
@NamedQuery(name = "UplDetail.findAll", query = "SELECT p FROM UplDetail p")
public class UplDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "seq_UplDetail", strategy = "com.mcredit.data.common.entity.SequenceGenerate", parameters = @Parameter(name = "seqName", value = "SEQ_UPL_DETAIL_ID.NEXTVAL"))
    @GeneratedValue(generator = "seq_UplDetail")
    private Long id;

    @Column(name = "UPL_MASTER_ID")
    private Long uplMasterId;

    @Column(name = "UPL_SEQ")
    private Integer uplSeq;

    @Column(name = "IMPORTED")
    private Integer imported;

    @Column(name = "TOTAL_ALLOCATED")
    private Integer totalAllocated;

    @Column(name = "UPL_FILE_NAME")
    private String uplFileName;

    @Column(name = "SERVER_FILE_NAME")
    private String serverFileName;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "RECORD_STATUS")
    private String recordStatus;

    @CreationTimestamp
    @Column(name = "CREATED_DATE", updatable = false)
    private Date createdDate;

    @UpdateTimestamp
    @Column(name = "LAST_UPDATED_DATE")
    private Date lastUpdatedDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "LAST_UPDATED_BY")
    private String lastUpdatedBy;

    @Lob
    @Column(name = "ERROR_MESSAGE")
    private String errorMessage;

    @Column(name = "STATUS_APP")
    private Long statusApp;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_APP")
    private Date dateApp;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_FROM")
    private Date dateFrom;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_TO")
    private Date dateTo;

    @Column(name = "REJECTION_REASON")
    private String rejectionReason;

    public UplDetail() {
    }

    public UplDetail(String recordStatus, Date createdDate, Date lastUpdatedDate, Integer uplSeq, Integer imported, Integer totalAllocated, String status, Long uplMasterId) {

        this.recordStatus = recordStatus;
        this.createdDate = createdDate;
        this.lastUpdatedDate = lastUpdatedDate;
        this.uplSeq = uplSeq;
        this.imported = imported;
        this.totalAllocated = totalAllocated;
        this.status = status;
        this.uplMasterId = uplMasterId;
    }

    public UplDetail(Long uplMasterId, Integer uplSeq, Integer imported, Integer totalAllocated, String uplFileName, String serverFileName, String status, 
            String recordStatus, Date createdDate, String createdBy, Date dateFrom, Date dateTo, Long statusApp) {
        this.uplMasterId = uplMasterId;
        this.uplSeq = uplSeq;
        this.imported = imported;
        this.totalAllocated = totalAllocated;
        this.uplFileName = uplFileName;
        this.serverFileName = serverFileName;
        this.status = status;
        this.recordStatus = recordStatus;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.statusApp = statusApp;
    }
    

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUplMasterId() {
        return uplMasterId;
    }

    public void setUplMasterId(Long uplMasterId) {
        this.uplMasterId = uplMasterId;
    }

    public Integer getUplSeq() {
        return uplSeq;
    }

    public void setUplSeq(Integer uplSeq) {
        this.uplSeq = uplSeq;
    }

    public Integer getImported() {
        return imported;
    }

    public void setImported(Integer imported) {
        this.imported = imported;
    }

    public Integer getTotalAllocated() {
        return totalAllocated;
    }

    public void setTotalAllocated(Integer totalAllocated) {
        this.totalAllocated = totalAllocated;
    }

    public String getUplFileName() {
        return uplFileName;
    }

    public void setUplFileName(String uplFileName) {
        this.uplFileName = uplFileName;
    }

    public String getServerFileName() {
        return serverFileName;
    }

    public void setServerFileName(String serverFileName) {
        this.serverFileName = serverFileName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Long getStatusApp() {
        return statusApp;
    }

    public void setStatusApp(Long statusApp) {
        this.statusApp = statusApp;
    }

    public Date getDateApp() {
        return dateApp;
    }

    public void setDateApp(Date dateApp) {
        this.dateApp = dateApp;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

}
