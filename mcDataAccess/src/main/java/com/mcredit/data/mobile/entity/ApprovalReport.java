package com.mcredit.data.mobile.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="UPL_APPROVAL_APP_SUMY")
public class ApprovalReport implements Serializable{
	  
	  private static final long serialVersionUID = 6621985890166777721L;
	  
	  	@Id
	  	@Column(name="PRODUCT")
		private String product;
		
		@Column(name="NUM_OF_NEW_APP")
		private Long numOfNewApp;
		
		@Column(name="NUM_OF_APPROVE_APP")
		private Long numOfApproveApp;
		
		@Column(name="NUM_OF_REJECT_APP")
		private Long numOfRejectApp;
		
		@Column(name="RATIO_APPROVE_APP")
		private Long ratioApproveApp;
		
		@Column(name="APPROVE_AMOUNT")
		private Long approveAmount;
		
		@Id
		@Temporal(TemporalType.DATE)
		@Column(name="BUSINESS_DATE")
		private Date businessDate;
		
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name="UPD_TIME")
		private Date updTime;

		public String getProduct() {
			return product;
		}

		public void setProduct(String product) {
			this.product = product;
		}

		public Long getNumOfNewApp() {
			return numOfNewApp;
		}

		public void setNumOfNewApp(Long numOfNewApp) {
			this.numOfNewApp = numOfNewApp;
		}

		public Long getNumOfApproveApp() {
			return numOfApproveApp;
		}

		public void setNumOfApproveApp(Long numOfApproveApp) {
			this.numOfApproveApp = numOfApproveApp;
		}

		public Long getNumOfRejectApp() {
			return numOfRejectApp;
		}

		public void setNumOfRejectApp(Long numOfRejectApp) {
			this.numOfRejectApp = numOfRejectApp;
		}

		public Long getRatioApproveApp() {
			return ratioApproveApp;
		}

		public void setRatioApproveApp(Long ratioApproveApp) {
			this.ratioApproveApp = ratioApproveApp;
		}

		public Long getApproveAmount() {
			return approveAmount;
		}

		public void setApproveAmount(Long approveAmount) {
			this.approveAmount = approveAmount;
		}

		public Date getBusinessDate() {
			return businessDate;
		}

		public void setBusinessDate(Date businessDate) {
			this.businessDate = businessDate;
		}

		public Date getUpdTime() {
			return updTime;
		}

		public void setUpdTime(Date updTime) {
			this.updTime = updTime;
		}
		
		

}
