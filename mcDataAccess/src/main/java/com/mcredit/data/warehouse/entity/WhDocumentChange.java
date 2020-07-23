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
@Table(name = "WH_DOCUMENT_CHANGE")
public class WhDocumentChange implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6621985890166777721L;

    @Id
    @GenericGenerator(name = "seq_WhDocumentChange", strategy = "com.mcredit.data.common.entity.SequenceGenerate", parameters = @Parameter(name = "seqName", value = "SEQ_WH_DOCUMENT_CHANGE_ID.NEXTVAL"))
    @GeneratedValue(generator = "seq_WhDocumentChange")
    private Long id;

    @Column(name = "WH_DOC_ID")
    private Long whDocId;

    @Column(name = "TYPE")
    private Long type;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "ID_CODE_TABLE")
    private Long idCodeTable;

    @Column(name = "CREDIT_APP_ID")
    private Long creditAppId;

    public WhDocumentChange(Long whDocId, Long type, String note, String createdBy, Date createdDate, Long idCodeTable) {
        this.whDocId = whDocId;
        this.type = type;
        this.note = note;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.idCodeTable = idCodeTable;
    }
    
    public WhDocumentChange(Long whDocId, Long type, String note, String createdBy, Date createdDate, Long idCodeTable, Long creditAppId) {
        this.whDocId = whDocId;
        this.type = type;
        this.note = note;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.idCodeTable = idCodeTable;
        this.creditAppId = creditAppId;
    }

    public WhDocumentChange(Long whDocId, Long type, Long idCodeTable, String createdBy, Date createdDate,
            String scanId) {
        this.whDocId = whDocId;
        this.type = type;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.idCodeTable = idCodeTable;
    }

    public WhDocumentChange(Long whDocId, Long type, Long idCodeTable, String createdBy, Date createdDate) {
        this.whDocId = whDocId;
        this.type = type;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.idCodeTable = idCodeTable;
    }

    public WhDocumentChange(Long creditAppId, Long type, Date createdDate, String createdBy) {
		this.creditAppId = creditAppId;
		this.type = type;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
	}


    public WhDocumentChange() {
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

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getIdCodeTable() {
        return idCodeTable;
    }

    public void setIdCodeTable(Long idCodeTable) {
        this.idCodeTable = idCodeTable;
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

    public Long getCreditAppId() {
        return creditAppId;
    }

    public void setCreditAppId(Long creditAppId) {
        this.creditAppId = creditAppId;
    }

}
