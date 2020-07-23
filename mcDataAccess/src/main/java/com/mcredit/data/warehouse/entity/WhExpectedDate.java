package com.mcredit.data.warehouse.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "WH_EXPECTED_DATE")
public class WhExpectedDate implements Serializable {

    private static final long serialVersionUID = -1583877483053941484L;

    @Id
    @GenericGenerator(name = "seq_WhExpectedDateId", strategy = "com.mcredit.data.common.entity.SequenceGenerate", parameters = @Parameter(name = "seqName", value = "SEQ_WH_EXPECTED_DATE_ID.NEXTVAL"))
    @GeneratedValue(generator = "seq_WhExpectedDateId")
    private Long id;

    @Column(name = "CREDIT_APP_ID")
    private Long creditAppId;

    @Column(name = "DOC_TYPE")
    private Long docType;

    @Column(name = "EXPECTED_DATE")
    private Date expectedDate;

    public WhExpectedDate(Long creditAppId, Long docType, Date expectedDate) {
        this.creditAppId = creditAppId;
        this.docType = docType;
        this.expectedDate = expectedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreditAppId() {
        return creditAppId;
    }

    public void setCreditAppId(Long creditAppId) {
        this.creditAppId = creditAppId;
    }

    public Long getDocType() {
        return docType;
    }

    public void setDocType(Long docType) {
        this.docType = docType;
    }

    public Date getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(Date expectedDate) {
        this.expectedDate = expectedDate;
    }

}
