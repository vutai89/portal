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

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "WH_DOCUMENT_ERR")
public class WhDocumentErr implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6621985890166777721L;

    @Id
    @GenericGenerator(name = "seq_WhDocumentErr", strategy = "com.mcredit.data.common.entity.SequenceGenerate", parameters = @Parameter(name = "seqName", value = "SEQ_WH_DOCUMENT_ERR_ID.NEXTVAL"))
    @GeneratedValue(generator = "seq_WhDocumentErr")
    private Long id;

    @Column(name = "WH_DOC_ID")
    private Long whDocId;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "ERR_TYPE")
    private Long errType;
    
    @Column(name = "UPDATE_REQUEST")
    private Integer updateRequest;

    public WhDocumentErr() {
    }

    public WhDocumentErr(Long whDocId, String createdBy, Date createdDate, Long errType, Integer updateRequest) {
        this.whDocId = whDocId;        
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.errType = errType;
        this.updateRequest = updateRequest;
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

    public Long getErrType() {
        return errType;
    }

    public void setErrType(Long errType) {
        this.errType = errType;
    }

    public Integer getUpdateRequest() {
        return updateRequest;
    }

    public void setUpdateRequest(Integer updateRequest) {
        this.updateRequest = updateRequest;
    }


}
