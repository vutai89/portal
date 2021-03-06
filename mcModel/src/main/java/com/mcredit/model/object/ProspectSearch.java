/**
 * 
 */
package com.mcredit.model.object;

import java.io.Serializable;

/**
 * @author anhdv.ho
 *
 */
public class ProspectSearch implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long uplMasterId;
	private String mobile;
	private Long nextAction;
	private Long callResult;
	private String custName;
	private String fromDate;
	private String toDate;
	private Long callStatus;
	private Long prospectStatus;
	private String identityNumber;
	private int page;
	private int rowPerPage;
	private Long userId;
	private boolean isSuperVisor;
	private boolean isTeamLead;
	private String provinceId;
	private String productName;
	private String dataSource;
	private String leadSource;
	private Long tsaReceiveId;
	private Integer isMark;
	private Integer resultOTP;
	private Integer resultTS;
	private String provinceTextList;
	private String newMobile;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getLeadSource() {
		return leadSource;
	}

	public void setLeadSource(String leadSource) {
		this.leadSource = leadSource;
	}

	public Long getTsaReceiveId() {
		return tsaReceiveId;
	}

	public void setTsaReceiveId(Long tsaReceiveId) {
		this.tsaReceiveId = tsaReceiveId;
	}

	public boolean isSuperVisor() {
		return isSuperVisor;
	}

	public void setSuperVisor(boolean isSuperVisor) {
		this.isSuperVisor = isSuperVisor;
	}

	public boolean isTeamLead() {
		return isTeamLead;
	}

	public void setTeamLead(boolean isTeamLead) {
		this.isTeamLead = isTeamLead;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUplMasterId() {
		return uplMasterId;
	}

	public void setUplMasterId(Long uplMasterId) {
		this.uplMasterId = uplMasterId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Long getNextAction() {
		return nextAction;
	}

	public void setNextAction(Long nextAction) {
		this.nextAction = nextAction;
	}

	public Long getCallResult() {
		return callResult;
	}

	public void setCallResult(Long callResult) {
		this.callResult = callResult;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public Long getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(Long callStatus) {
		this.callStatus = callStatus;
	}

	public Long getProspectStatus() {
		return prospectStatus;
	}

	public void setProspectStatus(Long prospectStatus) {
		this.prospectStatus = prospectStatus;
	}

	public String getIdentityNumber() {
		return identityNumber;
	}

	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRowPerPage() {
		return rowPerPage;
	}

	public void setRowPerPage(int rowPerPage) {
		this.rowPerPage = rowPerPage;
	}
	
	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public Integer getIsMark() {
		return isMark;
	}

	public void setIsMark(Integer isMark) {
		this.isMark = isMark;
	}

	public Integer getResultOTP() {
		return resultOTP;
	}

	public void setResultOTP(Integer resultOTP) {
		this.resultOTP = resultOTP;
	}

	public Integer getResultTS() {
		return resultTS;
	}

	public void setResultTS(Integer resultTS) {
		this.resultTS = resultTS;
	}

	public String getProvinceTextList() {
		return provinceTextList;
	}

	public void setProvinceTextList(String provinceTextList) {
		this.provinceTextList = provinceTextList;
	}

	public String getNewMobile() {
		return newMobile;
	}

	public void setNewMobile(String newMobile) {
		this.newMobile = newMobile;
	}
	
}
