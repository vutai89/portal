package com.mcredit.model.object;

import java.io.Serializable;
import java.util.Date;

public class ApprovalReport implements Serializable{
	  
		private String product;
		
		private Long numOfNewApp;
		
		private Long numOfApproveApp;
		
		private Long numOfRejectApp;
		
		private Long ratioApproveApp;
		
		private Long approveAmount;
		
		private String businessDate;
		
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

		public String getBusinessDate() {
			return businessDate;
		}

		public void setBusinessDate(String businessDate) {
			this.businessDate = businessDate;
		}

		public Date getUpdTime() {
			return updTime;
		}

		public void setUpdTime(Date updTime) {
			this.updTime = updTime;
		}
		
		

}
