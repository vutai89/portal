package com.mcredit.data.cancelcasebpm.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="UPL_APP_AUTO_ABORT")
public class UplAppAutoAbort implements Serializable{
	
	private static final long serialVersionUID = -1561336510794582719L;
	
	@Column(name = "APPUID")
	private String appuid;
	
	@Column(name = "DEL_INDEX")
	private Integer delIndex;
	
	@Id
	@Column(name = "APPNUMBER")
	private Long appnumber;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "COB_DT")
	private Date cobDt;
	
	@Column(name = "SRC_SYS_CD")
	private String srcSysCd;
	
	@Column(name = "UPD_BY")
	private String updBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPD_TM")
	private Date updTm;

	@Column(name = "TYPEOFLOAN")
	private String typeOfLoan;

	@Column(name = "CASEGROUP")
	private Integer caseGroup;

	@Column(name = "CURR_STATUS")
	private String currStatus;

	@Column(name = "NEW_STATUS")
	private String newStatus;

	@Column(name = "REASON_ABORT")
	private String reasonAbort;

	@Column(name = "REASON_ABORT_DETAIL")
	private String reasonAbortDetail;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "AUTO_ABORT_DT")
	private Date autoAbortDt;

	@Column(name = "AUTO_ABORT_STS")
	private String autoAbortSts;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "AUTO_ABORT_TM")
	private Date autoAbortTm;

	@Column(name = "AUTO_ABORT_DESC")
	private String autoAbortDesc;

	@Column(name = "AUTO_ABORT_BY")
	private String autoAbortBy;
	
	@Column(name = "CONTRACTNUMBER")
	private String constractnumber;
	
	@Column(name = "ERROR_COUNT")
	private Integer errorCount;



	public Integer getErrorCount() {
		return errorCount;
	}



	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}



	public String getAppuid() {
		return appuid;
	}



	public void setAppuid(String appuid) {
		this.appuid = appuid;
	}



	public Integer getDelIndex() {
		return delIndex;
	}



	public void setDelIndex(Integer delIndex) {
		this.delIndex = delIndex;
	}



	public Long getAppnumber() {
		return appnumber;
	}



	public void setAppnumber(Long appnumber) {
		this.appnumber = appnumber;
	}



	public Date getCobDt() {
		return cobDt;
	}



	public void setCobDt(Date cobDt) {
		this.cobDt = cobDt;
	}



	public String getSrcSysCd() {
		return srcSysCd;
	}



	public void setSrcSysCd(String srcSysCd) {
		this.srcSysCd = srcSysCd;
	}



	public String getUpdBy() {
		return updBy;
	}



	public void setUpdBy(String updBy) {
		this.updBy = updBy;
	}



	public Date getUpdTm() {
		return updTm;
	}



	public void setUpdTm(Date updTm) {
		this.updTm = updTm;
	}



	public String getTypeOfLoan() {
		return typeOfLoan;
	}



	public void setTypeOfLoan(String typeOfLoan) {
		this.typeOfLoan = typeOfLoan;
	}



	public Integer getCaseGroup() {
		return caseGroup;
	}



	public void setCaseGroup(Integer caseGroup) {
		this.caseGroup = caseGroup;
	}



	public String getCurrStatus() {
		return currStatus;
	}



	public void setCurrStatus(String currStatus) {
		this.currStatus = currStatus;
	}



	public String getNewStatus() {
		return newStatus;
	}



	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
	}



	public String getReasonAbort() {
		return reasonAbort;
	}



	public void setReasonAbort(String reasonAbort) {
		this.reasonAbort = reasonAbort;
	}



	public String getReasonAbortDetail() {
		return reasonAbortDetail;
	}



	public void setReasonAbortDetail(String reasonAbortDetail) {
		this.reasonAbortDetail = reasonAbortDetail;
	}



	public Date getAutoAbortDt() {
		return autoAbortDt;
	}



	public void setAutoAbortDt(Date autoAbortDt) {
		this.autoAbortDt = autoAbortDt;
	}



	public String getAutoAbortSts() {
		return autoAbortSts;
	}



	public void setAutoAbortSts(String autoAbortSts) {
		this.autoAbortSts = autoAbortSts;
	}



	public Date getAutoAbortTm() {
		return autoAbortTm;
	}



	public void setAutoAbortTm(Date autoAbortTm) {
		this.autoAbortTm = autoAbortTm;
	}



	public String getAutoAbortDesc() {
		return autoAbortDesc;
	}



	public void setAutoAbortDesc(String autoAbortDesc) {
		this.autoAbortDesc = autoAbortDesc;
	}



	public String getAutoAbortBy() {
		return autoAbortBy;
	}



	public void setAutoAbortBy(String autoAbortBy) {
		this.autoAbortBy = autoAbortBy;
	}



	public String getConstractnumber() {
		return constractnumber;
	}



	public void setConstractnumber(String constractnumber) {
		this.constractnumber = constractnumber;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	@Override
	public String toString() {
		return "UplAppAutoAbort [appnumber=" + appnumber + ", cobDt=" + cobDt + ", srcSysCd=" + srcSysCd + ", updBy="
				+ updBy + ", updTm=" + updTm + ", typeOfLoan=" + typeOfLoan + ", caseGroup=" + caseGroup
				+ ", currStatus=" + currStatus + ", newStatus=" + newStatus + ", reasonAbort=" + reasonAbort
				+ ", reasonAbortDetail=" + reasonAbortDetail + ", autoAbortDt=" + autoAbortDt + ", autoabortSts="
				+ autoAbortSts + ", autoAbortTm=" + autoAbortTm + ", autoAbortDesc=" + autoAbortDesc + ", autoAbortBy="
				+ autoAbortBy + "]";
	}

}
