package com.mcredit.model.dto.warehouse;

import java.util.List;

import com.mcredit.model.object.warehouse.WareHouseObject;
import com.mcredit.model.object.warehouse.WareHouseSeachObject;
import com.mcredit.model.object.warehouse.WhDocumentChangeDTO;

public class WareHouseSeachDTO extends WareHouseObject {

	private static final long serialVersionUID = 1L;

	public String approvedDateBPM;/* change from approvedDate */
	public String actualReceiptDate;/* change from dateActualReceipt */
	public String expectedReceiptDate;/* change from dateExpectedReceipt */
	public String estimateDate;
	public String allocateDate;
	public String cavetInfoLastUpDate;
	public String cavetInfoCreatedDate;
	public String cavetAppendixLastUpDate;
	public String cavetAppendixCreatedDate;
	public String toApproveDate;
	public String extensionBorrowDate;
	public String whLastUpDate;

	public String borrowDate;
	public String appoinmentDate;
	public String approvalDate;
	public String processOP2Date;
	public String whLodgeDate;
	public String updateErrorExpectedReceipt;
	public String importDate;
	public String sendThankLetter;
	public String dateErrCavet;
	public String dateErrLoanDoc;

	public String indentityIssueDate;
	public String indentityExpiryDate;

	public String contractdate;
	public String actualcloseddate;

	public String returnDate;

	public String getApprovedDateBPM() {
		return approvedDateBPM;
	}

	public void setApprovedDateBPM(String approvedDateBPM) {
		this.approvedDateBPM = approvedDateBPM;
	}

	public String getActualReceiptDate() {
		return actualReceiptDate;
	}

	public void setActualReceiptDate(String actualReceiptDate) {
		this.actualReceiptDate = actualReceiptDate;
	}

	public String getExpectedReceiptDate() {
		return expectedReceiptDate;
	}

	public void setExpectedReceiptDate(String expectedReceiptDate) {
		this.expectedReceiptDate = expectedReceiptDate;
	}

	public String getEstimateDate() {
		return estimateDate;
	}

	public void setEstimateDate(String estimateDate) {
		this.estimateDate = estimateDate;
	}

	public String getAllocateDate() {
		return allocateDate;
	}

	public void setAllocateDate(String allocateDate) {
		this.allocateDate = allocateDate;
	}

	public String getCavetInfoLastUpDate() {
		return cavetInfoLastUpDate;
	}

	public void setCavetInfoLastUpDate(String cavetInfoLastUpDate) {
		this.cavetInfoLastUpDate = cavetInfoLastUpDate;
	}

	public String getCavetInfoCreatedDate() {
		return cavetInfoCreatedDate;
	}

	public void setCavetInfoCreatedDate(String cavetInfoCreatedDate) {
		this.cavetInfoCreatedDate = cavetInfoCreatedDate;
	}

	public String getCavetAppendixLastUpDate() {
		return cavetAppendixLastUpDate;
	}

	public void setCavetAppendixLastUpDate(String cavetAppendixLastUpDate) {
		this.cavetAppendixLastUpDate = cavetAppendixLastUpDate;
	}

	public String getCavetAppendixCreatedDate() {
		return cavetAppendixCreatedDate;
	}

	public void setCavetAppendixCreatedDate(String cavetAppendixCreatedDate) {
		this.cavetAppendixCreatedDate = cavetAppendixCreatedDate;
	}

	public String getWhLastUpDate() {
		return whLastUpDate;
	}

	public void setWhLastUpDate(String whLastUpDate) {
		this.whLastUpDate = whLastUpDate;
	}

	public String getBorrowDate() {
		return borrowDate;
	}

	public void setBorrowDate(String borrowDate) {
		this.borrowDate = borrowDate;
	}

	public String getAppoinmentDate() {
		return appoinmentDate;
	}

	public void setAppoinmentDate(String appoinmentDate) {
		this.appoinmentDate = appoinmentDate;
	}

	public String getSendThankLetter() {
		return sendThankLetter;
	}

	public void setSendThankLetter(String sendThankLetter) {
		this.sendThankLetter = sendThankLetter;
	}

	public String getToApproveDate() {
		return toApproveDate;
	}

	public void setToApproveDate(String toApproveDate) {
		this.toApproveDate = toApproveDate;
	}

	public String getExtensionBorrowDate() {
		return extensionBorrowDate;
	}

	public void setExtensionBorrowDate(String extensionBorrowDate) {
		this.extensionBorrowDate = extensionBorrowDate;
	}

	public String getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(String approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getProcessOP2Date() {
		return processOP2Date;
	}

	public void setProcessOP2Date(String processOP2Date) {
		this.processOP2Date = processOP2Date;
	}

	public String getWhLodgeDate() {
		return whLodgeDate;
	}

	public void setWhLodgeDate(String whLodgeDate) {
		this.whLodgeDate = whLodgeDate;
	}

	public String getUpdateErrorExpectedReceipt() {
		return updateErrorExpectedReceipt;
	}

	public void setUpdateErrorExpectedReceipt(String updateErrorExpectedReceipt) {
		this.updateErrorExpectedReceipt = updateErrorExpectedReceipt;
	}

	public String getDateErrCavet() {
		return dateErrCavet;
	}

	public void setDateErrCavet(String dateErrCavet) {
		this.dateErrCavet = dateErrCavet;
	}

	public String getDateErrLoanDoc() {
		return dateErrLoanDoc;
	}

	public void setDateErrLoanDoc(String dateErrLoanDoc) {
		this.dateErrLoanDoc = dateErrLoanDoc;
	}

	public Long getWhId() {
		return whId;
	}

	public void setWhId(Long whId) {
		this.whId = whId;
	}

	public String getDocTypeCode() {
		return docTypeCode;
	}

	public void setDocTypeCode(String docTypeCode) {
		this.docTypeCode = docTypeCode;
	}

	public String getDocTypeName() {
		return docTypeName;
	}

	public void setDocTypeName(String docTypeName) {
		this.docTypeName = docTypeName;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public Long getCaseNum() {
		return caseNum;
	}

	public void setCaseNum(Long caseNum) {
		this.caseNum = caseNum;
	}

	public String getCreditAppId() {
		return creditAppId;
	}

	public void setCreditAppId(String creditAppId) {
		this.creditAppId = creditAppId;
	}

	public Long getPostCodeId() {
		return postCodeId;
	}

	public void setPostCodeId(Long postCodeId) {
		this.postCodeId = postCodeId;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getPostCodeName() {
		return postCodeName;
	}

	public void setPostCodeName(String postCodeName) {
		this.postCodeName = postCodeName;
	}

	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}

	public String getSaleName() {
		return saleName;
	}

	public void setSaleName(String saleName) {
		this.saleName = saleName;
	}

	public String getMobileSale() {
		return mobileSale;
	}

	public void setMobileSale(String mobileSale) {
		this.mobileSale = mobileSale;
	}

	public String getSaleEmail() {
		return saleEmail;
	}

	public void setSaleEmail(String saleEmail) {
		this.saleEmail = saleEmail;
	}

	public Long getStatusBPM() {
		return statusBPM;
	}

	public void setStatusBPM(Long statusBPM) {
		this.statusBPM = statusBPM;
	}

	public String getStatusBPMCode() {
		return statusBPMCode;
	}

	public void setStatusBPMCode(String statusBPMCode) {
		this.statusBPMCode = statusBPMCode;
	}

	public String getStatusBPMName() {
		return statusBPMName;
	}

	public void setStatusBPMName(String statusBPMName) {
		this.statusBPMName = statusBPMName;
	}

	public Long getStatusWH() {
		return statusWH;
	}

	public void setStatusWH(Long statusWH) {
		this.statusWH = statusWH;
	}

	public String getStatusWHCode() {
		return statusWHCode;
	}

	public void setStatusWHCode(String statusWHCode) {
		this.statusWHCode = statusWHCode;
	}

	public String getStatusWHName() {
		return statusWHName;
	}

	public void setStatusWHName(String statusWHName) {
		this.statusWHName = statusWHName;
	}

	public String getWhCode() {
		return whCode;
	}

	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}

	public String getWhLastUpDateBy() {
		return whLastUpDateBy;
	}

	public void setWhLastUpDateBy(String whLastUpDateBy) {
		this.whLastUpDateBy = whLastUpDateBy;
	}

	public String getFrameNum() {
		return frameNum;
	}

	public void setFrameNum(String frameNum) {
		this.frameNum = frameNum;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getGoodsDESC() {
		return goodsDESC;
	}

	public void setGoodsDESC(String goodsDESC) {
		this.goodsDESC = goodsDESC;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getModelTypeCode() {
		return modelTypeCode;
	}

	public void setModelTypeCode(String modelTypeCode) {
		this.modelTypeCode = modelTypeCode;
	}

	public String getModelTypeDESC() {
		return modelTypeDESC;
	}

	public void setModelTypeDESC(String modelTypeDESC) {
		this.modelTypeDESC = modelTypeDESC;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getProductGroupId() {
		return productGroupId;
	}

	public void setProductGroupId(Long productGroupId) {
		this.productGroupId = productGroupId;
	}

	public String getProductGroupCode() {
		return productGroupCode;
	}

	public void setProductGroupCode(String productGroupCode) {
		this.productGroupCode = productGroupCode;
	}

	public String getProductGroupName() {
		return productGroupName;
	}

	public void setProductGroupName(String productGroupName) {
		this.productGroupName = productGroupName;
	}

	public String getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(String workFlow) {
		this.workFlow = workFlow;
	}

	public Long getWorkFlowId() {
		return workFlowId;
	}

	public void setWorkFlowId(Long workFlowId) {
		this.workFlowId = workFlowId;
	}

	public String getWorkFlowDESC1() {
		return workFlowDESC1;
	}

	public void setWorkFlowDESC1(String workFlowDESC1) {
		this.workFlowDESC1 = workFlowDESC1;
	}

	public String getWorkFlowDESC2() {
		return workFlowDESC2;
	}

	public void setWorkFlowDESC2(String workFlowDESC2) {
		this.workFlowDESC2 = workFlowDESC2;
	}

	public String getBdsFullName() {
		return bdsFullName;
	}

	public void setBdsFullName(String bdsFullName) {
		this.bdsFullName = bdsFullName;
	}

	public String getBdsEmail() {
		return bdsEmail;
	}

	public void setBdsEmail(String bdsEmail) {
		this.bdsEmail = bdsEmail;
	}

	public String getBdsMobile() {
		return bdsMobile;
	}

	public void setBdsMobile(String bdsMobile) {
		this.bdsMobile = bdsMobile;
	}

	public Long getContractCavetType() {
		return contractCavetType;
	}

	public void setContractCavetType(Long contractCavetType) {
		this.contractCavetType = contractCavetType;
	}

	public String getContractCavetName() {
		return contractCavetName;
	}

	public void setContractCavetName(String contractCavetName) {
		this.contractCavetName = contractCavetName;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Long getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	public Long getCreditDocmentId() {
		return creditDocmentId;
	}

	public void setCreditDocmentId(Long creditDocmentId) {
		this.creditDocmentId = creditDocmentId;
	}

	public Long getWhCodeId() {
		return whCodeId;
	}

	public void setWhCodeId(Long whCodeId) {
		this.whCodeId = whCodeId;
	}

	public String getAllocator() {
		return allocator;
	}

	public void setAllocator(String allocator) {
		this.allocator = allocator;
	}

	public Long getAllocateStatus() {
		return allocateStatus;
	}

	public void setAllocateStatus(Long allocateStatus) {
		this.allocateStatus = allocateStatus;
	}

	public String getAllocateCode() {
		return allocateCode;
	}

	public void setAllocateCode(String allocateCode) {
		this.allocateCode = allocateCode;
	}

	public String getAllocateDESC() {
		return allocateDESC;
	}

	public void setAllocateDESC(String allocateDESC) {
		this.allocateDESC = allocateDESC;
	}

	public String getAllocateNote() {
		return allocateNote;
	}

	public void setAllocateNote(String allocateNote) {
		this.allocateNote = allocateNote;
	}

	public Long getAssigneeId() {
		return assigneeId;
	}

	public void setAssigneeId(Long assigneeId) {
		this.assigneeId = assigneeId;
	}

	public String getAssigneeLogin() {
		return assigneeLogin;
	}

	public void setAssigneeLogin(String assigneeLogin) {
		this.assigneeLogin = assigneeLogin;
	}

	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}

	public String getLodgeUser() {
		return lodgeUser;
	}

	public void setLodgeUser(String lodgeUser) {
		this.lodgeUser = lodgeUser;
	}

	public Long getUserOperator2Id() {
		return userOperator2Id;
	}

	public void setUserOperator2Id(Long userOperator2Id) {
		this.userOperator2Id = userOperator2Id;
	}

	public String getUserOperator2FullName() {
		return userOperator2FullName;
	}

	public void setUserOperator2FullName(String userOperator2FullName) {
		this.userOperator2FullName = userOperator2FullName;
	}

	public Long getBorrowerId() {
		return borrowerId;
	}

	public void setBorrowerId(Long borrowerId) {
		this.borrowerId = borrowerId;
	}

	public String getBorrowerUsername() {
		return borrowerUsername;
	}

	public void setBorrowerUsername(String borrowerUsername) {
		this.borrowerUsername = borrowerUsername;
	}

	public String getBorrower() {
		return borrower;
	}

	public void setBorrower(String borrower) {
		this.borrower = borrower;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public Long getStatusApprove() {
		return statusApprove;
	}

	public void setStatusApprove(Long statusApprove) {
		this.statusApprove = statusApprove;
	}

	public String getStatusApproveCode() {
		return statusApproveCode;
	}

	public void setStatusApproveCode(String statusApproveCode) {
		this.statusApproveCode = statusApproveCode;
	}

	public String getStatusApproveDESC() {
		return statusApproveDESC;
	}

	public void setStatusApproveDESC(String statusApproveDESC) {
		this.statusApproveDESC = statusApproveDESC;
	}

	public Long getCavetInfoId() {
		return cavetInfoId;
	}

	public void setCavetInfoId(Long cavetInfoId) {
		this.cavetInfoId = cavetInfoId;
	}

	public String getCavetInfoCreatedBy() {
		return cavetInfoCreatedBy;
	}

	public void setCavetInfoCreatedBy(String cavetInfoCreatedBy) {
		this.cavetInfoCreatedBy = cavetInfoCreatedBy;
	}

	public String getCavetInfoLastUpDateBy() {
		return cavetInfoLastUpDateBy;
	}

	public void setCavetInfoLastUpDateBy(String cavetInfoLastUpDateBy) {
		this.cavetInfoLastUpDateBy = cavetInfoLastUpDateBy;
	}

	public Long getCavetInfoWhDocId() {
		return cavetInfoWhDocId;
	}

	public void setCavetInfoWhDocId(Long cavetInfoWhDocId) {
		this.cavetInfoWhDocId = cavetInfoWhDocId;
	}

	public String getCavetInfoBrand() {
		return cavetInfoBrand;
	}

	public void setCavetInfoBrand(String cavetInfoBrand) {
		this.cavetInfoBrand = cavetInfoBrand;
	}

	public String getCavetInfoModelCode() {
		return cavetInfoModelCode;
	}

	public void setCavetInfoModelCode(String cavetInfoModelCode) {
		this.cavetInfoModelCode = cavetInfoModelCode;
	}

	public String getCavetInfoColor() {
		return cavetInfoColor;
	}

	public void setCavetInfoColor(String cavetInfoColor) {
		this.cavetInfoColor = cavetInfoColor;
	}

	public String getCavetInfoEngine() {
		return cavetInfoEngine;
	}

	public void setCavetInfoEngine(String cavetInfoEngine) {
		this.cavetInfoEngine = cavetInfoEngine;
	}

	public String getCavetInfoChassis() {
		return cavetInfoChassis;
	}

	public void setCavetInfoChassis(String cavetInfoChassis) {
		this.cavetInfoChassis = cavetInfoChassis;
	}

	public String getCavetInfoNPlate() {
		return cavetInfoNPlate;
	}

	public void setCavetInfoNPlate(String cavetInfoNPlate) {
		this.cavetInfoNPlate = cavetInfoNPlate;
	}

	public String getCavetInfoCavetNum() {
		return cavetInfoCavetNum;
	}

	public void setCavetInfoCavetNum(String cavetInfoCavetNum) {
		this.cavetInfoCavetNum = cavetInfoCavetNum;
	}

	public Long getCavetInfoType() {
		return cavetInfoType;
	}

	public void setCavetInfoType(Long cavetInfoType) {
		this.cavetInfoType = cavetInfoType;
	}

	public Long getCavetInfoVersion() {
		return cavetInfoVersion;
	}

	public void setCavetInfoVersion(Long cavetInfoVersion) {
		this.cavetInfoVersion = cavetInfoVersion;
	}

	public Long getCavetAppendixId() {
		return cavetAppendixId;
	}

	public void setCavetAppendixId(Long cavetAppendixId) {
		this.cavetAppendixId = cavetAppendixId;
	}

	public String getCavetAppendixCreatedBy() {
		return cavetAppendixCreatedBy;
	}

	public void setCavetAppendixCreatedBy(String cavetAppendixCreatedBy) {
		this.cavetAppendixCreatedBy = cavetAppendixCreatedBy;
	}

	public String getCavetAppendixLastUpDateBy() {
		return cavetAppendixLastUpDateBy;
	}

	public void setCavetAppendixLastUpDateBy(String cavetAppendixLastUpDateBy) {
		this.cavetAppendixLastUpDateBy = cavetAppendixLastUpDateBy;
	}

	public Long getCavetAppendixWhDocId() {
		return cavetAppendixWhDocId;
	}

	public void setCavetAppendixWhDocId(Long cavetAppendixWhDocId) {
		this.cavetAppendixWhDocId = cavetAppendixWhDocId;
	}

	public String getCavetAppendixBrand() {
		return cavetAppendixBrand;
	}

	public void setCavetAppendixBrand(String cavetAppendixBrand) {
		this.cavetAppendixBrand = cavetAppendixBrand;
	}

	public String getCavetAppendixModelCode() {
		return cavetAppendixModelCode;
	}

	public void setCavetAppendixModelCode(String cavetAppendixModelCode) {
		this.cavetAppendixModelCode = cavetAppendixModelCode;
	}

	public String getCavetAppendixColor() {
		return cavetAppendixColor;
	}

	public void setCavetAppendixColor(String cavetAppendixColor) {
		this.cavetAppendixColor = cavetAppendixColor;
	}

	public String getCavetAppendixEngine() {
		return cavetAppendixEngine;
	}

	public void setCavetAppendixEngine(String cavetAppendixEngine) {
		this.cavetAppendixEngine = cavetAppendixEngine;
	}

	public String getCavetAppendixChassis() {
		return cavetAppendixChassis;
	}

	public void setCavetAppendixChassis(String cavetAppendixChassis) {
		this.cavetAppendixChassis = cavetAppendixChassis;
	}

	public String getCavetAppendixNPlate() {
		return cavetAppendixNPlate;
	}

	public void setCavetAppendixNPlate(String cavetAppendixNPlate) {
		this.cavetAppendixNPlate = cavetAppendixNPlate;
	}

	public String getCavetAppendixCavetNum() {
		return cavetAppendixCavetNum;
	}

	public void setCavetAppendixCavetNum(String cavetAppendixCavetNum) {
		this.cavetAppendixCavetNum = cavetAppendixCavetNum;
	}

	public Long getCavetAppendixType() {
		return cavetAppendixType;
	}

	public void setCavetAppendixType(Long cavetAppendixType) {
		this.cavetAppendixType = cavetAppendixType;
	}

	public Long getCavetAppendixVersion() {
		return cavetAppendixVersion;
	}

	public void setCavetAppendixVersion(Long cavetAppendixVersion) {
		this.cavetAppendixVersion = cavetAppendixVersion;
	}

	public String getProcessOP2Status() {
		return processOP2Status;
	}

	public void setProcessOP2Status(String processOP2Status) {
		this.processOP2Status = processOP2Status;
	}

	public List<WhDocumentChangeDTO> getLstWHCaseError() {
		return lstWHCaseError;
	}

	public void setLstWHCaseError(List<WhDocumentChangeDTO> lstWHCaseError) {
		this.lstWHCaseError = lstWHCaseError;
	}

	public List<WhDocumentChangeDTO> getLstWHCavetError() {
		return lstWHCavetError;
	}

	public void setLstWHCavetError(List<WhDocumentChangeDTO> lstWHCavetError) {
		this.lstWHCavetError = lstWHCavetError;
	}

	public List<WareHouseSeachObject> getLstWHOriginalLoan() {
		return lstWHOriginalLoan;
	}

	public void setLstWHOriginalLoan(List<WareHouseSeachObject> lstWHOriginalLoan) {
		this.lstWHOriginalLoan = lstWHOriginalLoan;
	}

	public Integer getLostDate() {
		return lostDate;
	}

	public void setLostDate(Integer lostDate) {
		this.lostDate = lostDate;
	}

	public String getImportDate() {
		return importDate;
	}

	public void setImportDate(String importDate) {
		this.importDate = importDate;
	}

	public String getIndentityIssueDate() {
		return indentityIssueDate;
	}

	public void setIndentityIssueDate(String indentityIssueDate) {
		this.indentityIssueDate = indentityIssueDate;
	}

	public String getIndentityExpiryDate() {
		return indentityExpiryDate;
	}

	public void setIndentityExpiryDate(String indentityExpiryDate) {
		this.indentityExpiryDate = indentityExpiryDate;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	public String getContractdate() {
		return contractdate;
	}

	public void setContractdate(String contractdate) {
		this.contractdate = contractdate;
	}

	public String getActualcloseddate() {
		return actualcloseddate;
	}

	public void setActualcloseddate(String actualcloseddate) {
		this.actualcloseddate = actualcloseddate;
	}

}
