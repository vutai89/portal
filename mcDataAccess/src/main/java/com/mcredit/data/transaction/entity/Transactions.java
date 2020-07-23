package com.mcredit.data.transaction.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "TRANSACTIONS")
public class Transactions implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6621985890166777721L;

    @Id
    @GenericGenerator(name = "seq_Transactions", strategy = "com.mcredit.data.common.entity.SequenceGenerate", parameters = @Parameter(name = "seqName", value = "SEQ_TRANSACTIONS_ID.NEXTVAL"))
    @GeneratedValue(generator = "seq_Transactions")
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "INIT_TRANSACTION_DATE")
    private Date initTransactionDate;

    @Column(name = "INIT_USER_ID")
    private Long initUserId;

    @Temporal(TemporalType.DATE)
    @Column(name = "LAST_UPDATED_DATE")
    private Date lastUpdateDate;

    @Column(name = "LAST_UPDATED_USER_ID")
    private Long lastUpdatedUserId;

    @Temporal(TemporalType.DATE)
    @Column(name = "COMPLETED_TRANSACTION_DATE")
    private Date completedTransactionDate;

    @Column(name = "COMPLETED_USER_ID")
    private Long completedUserId;

    @Column(name = "FUNCTION_ID")
    private Long functionId;

    @Column(name = "MASTER_RECORD_ID")
    private Long masterRecordId;

    @Column(name = "MASTER_TABLE_NAME")
    private String masterTableName;

    @Column(name = "REFERENCE_RECORD_ID")
    private Long referenceRecordId;

    @Column(name = "OPERATION_CODE")
    private Long opentionCode;

    @Column(name = "TRANSACTION_STATUS")
    private Long transactionStatus;

    @Column(name = "TRANSACTION_COMMENT")
    private String transactioncomment;

    public Transactions() {
    }

    public Transactions(Date initTransactionDate, Long initUserId, Date lastUpdateDate, Long lastUpdatedUserId, Long masterRecordId, String masterTableName, Long referenceRecordId, Long opentionCode, Long transactionStatus, String transactioncomment) {
        this.initTransactionDate = initTransactionDate;
        this.initUserId = initUserId;
        this.lastUpdateDate = lastUpdateDate;
        this.lastUpdatedUserId = lastUpdatedUserId;
        this.masterRecordId = masterRecordId;
        this.masterTableName = masterTableName;
        this.referenceRecordId = referenceRecordId;
        this.opentionCode = opentionCode;
        this.transactionStatus = transactionStatus;
        this.transactioncomment = transactioncomment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getInitTransactionDate() {
        return initTransactionDate;
    }

    public void setInitTransactionDate(Date initTransactionDate) {
        this.initTransactionDate = initTransactionDate;
    }

    public Long getInitUserId() {
        return initUserId;
    }

    public void setInitUserId(Long initUserId) {
        this.initUserId = initUserId;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Long getLastUpdatedUserId() {
        return lastUpdatedUserId;
    }

    public void setLastUpdatedUserId(Long lastUpdatedUserId) {
        this.lastUpdatedUserId = lastUpdatedUserId;
    }

    public Date getCompletedTransactionDate() {
        return completedTransactionDate;
    }

    public void setCompletedTransactionDate(Date completedTransactionDate) {
        this.completedTransactionDate = completedTransactionDate;
    }

    public Long getCompletedUserId() {
        return completedUserId;
    }

    public void setCompletedUserId(Long completedUserId) {
        this.completedUserId = completedUserId;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    public Long getMasterRecordId() {
        return masterRecordId;
    }

    public void setMasterRecordId(Long masterRecordId) {
        this.masterRecordId = masterRecordId;
    }

    public String getMasterTableName() {
        return masterTableName;
    }

    public void setMasterTableName(String masterTableName) {
        this.masterTableName = masterTableName;
    }

    public Long getReferenceRecordId() {
        return referenceRecordId;
    }

    public void setReferenceRecordId(Long referenceRecordId) {
        this.referenceRecordId = referenceRecordId;
    }

    public Long getOpentionCode() {
        return opentionCode;
    }

    public void setOpentionCode(Long opentionCode) {
        this.opentionCode = opentionCode;
    }

    public Long getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(Long transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getTransactioncomment() {
        return transactioncomment;
    }

    public void setTransactioncomment(String transactioncomment) {
        this.transactioncomment = transactioncomment;
    }

}
