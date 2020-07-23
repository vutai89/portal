package com.mcredit.model.object.warehouse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class WareHouseSeachObject implements Serializable {

	private static final long serialVersionUID = 6681951446482382241L;
	/*------- Infor WareHouse --------*/
	public Long whId;
	public String importer;
	public Date importDate;
	public Long docTypeId;
	public String docTypeCode;
	public String docTypeName;
	public Long statusWH;
	public String statusWHCode;
	public String statusWHName;
	public String whCode;
	public Date actualReceiptDate;
	public Date whLastUpDate;
	public String whLastUpDateBy;
	public Long batchId;
	public Long version;
	public Long orderBy;
	public Long whCodeId;
	public String whLodgeBy;
	public Date whLodgeDate;
	public Date estimateDate;
	public Long contractCavetType;
	public String contractCavetName;
	public String billCode;
	public String deliveryError;
	public Long isActive;
	public Long isOriginal;
	public String errorNote;
	public Long appendixContract;

	/* EXPECTED DATE */
	public Date expectedDate;
	/*---------Infor BPM--------*/
	public String contractNum;
	public Date contractdate;
	public Date actualcloseddate;

	public String custName;
	public Long caseNum;
	public Long creditAppId;
	public Long postCodeId;
	public String postCode;
	public String postCodeName;

	/* so chung minh thu nhan danh */
	public String indentityNum;
	public Date indentityIssueDate;
	public Date indentityExpiryDate;
	public String indentityIssuePlace;
	public String indentityIssuePlaceText;
	public Long indentityTypeId;
	public String indentityTypeCode;
	public String indentityTypeName;

	public String address;
	public Long wardId;
	public Long districtId;
	public Long provinceId;
	public String addressFull;
	public String custMobile;

	/*-----------Sale-----------*/
	public Long saleId;
	public String saleName;
	public String mobileSale;
	public String saleEmail;
	public Long statusBPM;
	public String statusBPMCode;
	public String statusBPMName;
	public Date approvedDateBPM;
	public String bdsFullName;
	public String bdsEmail;
	public String bdsMobile;
	/*---------Proceduct--------*/
	public Long productId;
	public String productCode;
	public String productName;
	public Long productGroupId;
	public String productGroupCode;
	public String productGroupName;
	public String workFlow;
	public Long workFlowId;
	public String workFlowDESC1;
	public String workFlowDESC2;
	/*-------Allocation---------*/
	public String allocator;
	public Date allocateDate;
	public Long allocateStatus;
	public String allocateCode;
	public String allocateDESC;
	public String allocateNote;
	public Long assigneeId;
	public String assigneeLogin;
	public String assigneeName;
	/*------- van hanh 2 ---------*/
	public String userOperator2Id;
	public String userOperator2FullName;
	public String processOP2Status;
	public Date processOP2Date;
	/* ----- thong tin hang hoa ------ */
	public String frameNum;
	public String serialNum;
	public String numPlate;
	public Long goodsId;
	public String goodsCode;
	public String goodsDESC;
	public Long brandId;
	public String brandCode;
	public String brandName;
	public String modelId;
	public String modelTypeCode;
	public String modelTypeDESC;

	public String commodityId;
	public String commodityCode;
	public String commodityDESC;

	/* ------ cho muon ------- */
	public Long borrowerId;
	public String borrower;
	public String borrowerUsername;
	public Date borrowDate;
	public String departmentName;
	public Date appoinmentDate;
	public Date extensionBorrowDate;
	public String rejectReason;
	public Long statusApprove;
	public Date toApproveDate;
	public Date approvalDate;
	public String statusApproveCode;
	public String statusApproveDESC;
	public Date returnDate;
	/* -------- thong tin cavet tren cavet ------ */
	public Long cavetInfoId;
	public String cavetInfoCreatedBy;
	public Date cavetInfoLastUpDate;
	public String cavetInfoLastUpDateBy;
	public Date cavetInfoCreatedDate;
	public Long cavetInfoWhDocId;
	public String cavetInfoBrand;
	public String cavetInfoModelCode;
	public String cavetInfoColor;
	public String cavetInfoEngine;
	public String cavetInfoChassis;
	public String cavetInfoNPlate;
	public String cavetInfoCavetNum;
	public Long cavetInfoType;
	public Long cavetInfoVersion;
	/* ----------- thong tin cavet tren phuc luc -------------- */
	public Long cavetAppendixId;
	public String cavetAppendixCreatedBy;
	public Date cavetAppendixLastUpDate;
	public String cavetAppendixLastUpDateBy;
	public Date cavetAppendixCreatedDate;
	public Long cavetAppendixWhDocId;
	public String cavetAppendixBrand;
	public String cavetAppendixModelCode;
	public String cavetAppendixColor;
	public String cavetAppendixEngine;
	public String cavetAppendixChassis;
	public String cavetAppendixNPlate;
	public String cavetAppendixCavetNum;
	public Long cavetAppendixType;
	public Long cavetAppendixVersion;

	// public String identityNumber;
	// public String identityIssueDate;
	// public String identityExpiryDate;

	public List<WhDocumentErrDTO> lstWHCaseError;
	public List<WhDocumentErrDTO> lstWHCavetError;
	public List<WhDocumentDTO> lstWHOriginal;
	public Date expectedReceiptDate;
	public Integer lostDate;
	public Long idCheckContractCavet;
	public Date sendThankLetter;
	public Date dateErrCavet;
	public Date dateErrLoanDoc;

	public WareHouseSeachObject() {
	}

	public Long getWhId() {
		return whId;
	}

	public void setWhId(Long whId) {
		this.whId = whId;
	}

	public Long getDocTypeId() {
		return docTypeId;
	}

	public void setDocTypeId(Long docTypeId) {
		this.docTypeId = docTypeId;
	}

	public String getDocTypeCode() {
		return docTypeCode;
	}

	public String getSaleEmail() {
		return saleEmail;
	}

	public void setSaleEmail(String saleEmail) {
		this.saleEmail = saleEmail;
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

	public String getCommodityId() {
		return commodityId;
	}

	public void setCommodityId(String commodityId) {
		this.commodityId = commodityId;
	}

	public String getCommodityCode() {
		return commodityCode;
	}

	public void setCommodityCode(String commodityCode) {
		this.commodityCode = commodityCode;
	}

	public String getCommodityDESC() {
		return commodityDESC;
	}

	public void setCommodityDESC(String commodityDESC) {
		this.commodityDESC = commodityDESC;
	}

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
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

	public Long getCreditAppId() {
		return creditAppId;
	}

	public void setCreditAppId(Long creditAppId) {
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

	public Date getApprovedDateBPM() {
		return approvedDateBPM;
	}

	public void setApprovedDateBPM(Date approvedDateBPM) {
		this.approvedDateBPM = approvedDateBPM;
	}

	public Date getActualReceiptDate() {
		return actualReceiptDate;
	}

	public void setActualReceiptDate(Date actualReceiptDate) {
		this.actualReceiptDate = actualReceiptDate;
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

	public Long getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	public Long getWhCodeId() {
		return whCodeId;
	}

	public void setWhCodeId(Long whCodeId) {
		this.whCodeId = whCodeId;
	}

	public String getWhLodgeBy() {
		return whLodgeBy;
	}

	public void setWhLodgeBy(String whLodgeBy) {
		this.whLodgeBy = whLodgeBy;
	}

	public Date getWhLodgeDate() {
		return whLodgeDate;
	}

	public void setWhLodgeDate(Date whLodgeDate) {
		this.whLodgeDate = whLodgeDate;
	}

	public Date getEstimateDate() {
		return estimateDate;
	}

	public void setEstimateDate(Date estimateDate) {
		this.estimateDate = estimateDate;
	}

	public String getAllocator() {
		return allocator;
	}

	public void setAllocator(String allocator) {
		this.allocator = allocator;
	}

	public Date getAllocateDate() {
		return allocateDate;
	}

	public void setAllocateDate(Date allocateDate) {
		this.allocateDate = allocateDate;
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

	public Date getWhLastUpDate() {
		return whLastUpDate;
	}

	public void setWhLastUpDate(Date whLastUpDate) {
		this.whLastUpDate = whLastUpDate;
	}

	public String getWhLastUpDateBy() {
		return whLastUpDateBy;
	}

	public void setWhLastUpDateBy(String whLastUpDateBy) {
		this.whLastUpDateBy = whLastUpDateBy;
	}

	public String getUserOperator2Id() {
		return userOperator2Id;
	}

	public void setUserOperator2Id(String userOperator2Id) {
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

	public String getBorrower() {
		return borrower;
	}

	public void setBorrower(String borrower) {
		this.borrower = borrower;
	}

	public String getBorrowerUsername() {
		return borrowerUsername;
	}

	public void setBorrowerUsername(String borrowerUsername) {
		this.borrowerUsername = borrowerUsername;
	}

	public Date getBorrowDate() {
		return borrowDate;
	}

	public void setBorrowDate(Date borrowDate) {
		this.borrowDate = borrowDate;
	}

	public Date getAppoinmentDate() {
		return appoinmentDate;
	}

	public void setAppoinmentDate(Date appoinmentDate) {
		this.appoinmentDate = appoinmentDate;
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

	public Date getCavetInfoLastUpDate() {
		return cavetInfoLastUpDate;
	}

	public void setCavetInfoLastUpDate(Date cavetInfoLastUpDate) {
		this.cavetInfoLastUpDate = cavetInfoLastUpDate;
	}

	public String getCavetInfoLastUpDateBy() {
		return cavetInfoLastUpDateBy;
	}

	public void setCavetInfoLastUpDateBy(String cavetInfoLastUpDateBy) {
		this.cavetInfoLastUpDateBy = cavetInfoLastUpDateBy;
	}

	public Date getCavetInfoCreatedDate() {
		return cavetInfoCreatedDate;
	}

	public void setCavetInfoCreatedDate(Date cavetInfoCreatedDate) {
		this.cavetInfoCreatedDate = cavetInfoCreatedDate;
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

	public Date getCavetAppendixLastUpDate() {
		return cavetAppendixLastUpDate;
	}

	public void setCavetAppendixLastUpDate(Date cavetAppendixLastUpDate) {
		this.cavetAppendixLastUpDate = cavetAppendixLastUpDate;
	}

	public String getCavetAppendixLastUpDateBy() {
		return cavetAppendixLastUpDateBy;
	}

	public void setCavetAppendixLastUpDateBy(String cavetAppendixLastUpDateBy) {
		this.cavetAppendixLastUpDateBy = cavetAppendixLastUpDateBy;
	}

	public Date getCavetAppendixCreatedDate() {
		return cavetAppendixCreatedDate;
	}

	public void setCavetAppendixCreatedDate(Date cavetAppendixCreatedDate) {
		this.cavetAppendixCreatedDate = cavetAppendixCreatedDate;
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

	public List<WhDocumentErrDTO> getLstWHCaseError() {
		return lstWHCaseError;
	}

	public void setLstWHCaseError(List<WhDocumentErrDTO> lstWHCaseError) {
		this.lstWHCaseError = lstWHCaseError;
	}

	public List<WhDocumentErrDTO> getLstWHCavetError() {
		return lstWHCavetError;
	}

	public void setLstWHCavetError(List<WhDocumentErrDTO> lstWHCavetError) {
		this.lstWHCavetError = lstWHCavetError;
	}

	public Date getExpectedReceiptDate() {
		return expectedReceiptDate;
	}

	public void setExpectedReceiptDate(Date expectedReceiptDate) {
		this.expectedReceiptDate = expectedReceiptDate;
	}

	public Integer getLostDate() {
		return lostDate;
	}

	public void setLostDate(Integer lostDate) {
		this.lostDate = lostDate;
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

	public Date getToApproveDate() {
		return toApproveDate;
	}

	public void setToApproveDate(Date toApproveDate) {
		this.toApproveDate = toApproveDate;
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

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
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

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
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

	public Date getExtensionBorrowDate() {
		return extensionBorrowDate;
	}

	public void setExtensionBorrowDate(Date extensionBorrowDate) {
		this.extensionBorrowDate = extensionBorrowDate;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public Long getIdCheckContractCavet() {
		return idCheckContractCavet;
	}

	public void setIdCheckContractCavet(Long idCheckContractCavet) {
		this.idCheckContractCavet = idCheckContractCavet;
	}

	public String getProcessOP2Status() {
		return processOP2Status;
	}

	public void setProcessOP2Status(String processOP2Status) {
		this.processOP2Status = processOP2Status;
	}

	public Date getProcessOP2Date() {
		return processOP2Date;
	}

	public void setProcessOP2Date(Date processOP2Date) {
		this.processOP2Date = processOP2Date;
	}

	public Date getSendThankLetter() {
		return sendThankLetter;
	}

	public void setSendThankLetter(Date sendThankLetter) {
		this.sendThankLetter = sendThankLetter;
	}

	public Date getDateErrCavet() {
		return dateErrCavet;
	}

	public void setDateErrCavet(Date dateErrCavet) {
		this.dateErrCavet = dateErrCavet;
	}

	public Date getDateErrLoanDoc() {
		return dateErrLoanDoc;
	}

	public void setDateErrLoanDoc(Date dateErrLoanDoc) {
		this.dateErrLoanDoc = dateErrLoanDoc;
	}

	public String getNumPlate() {
		return numPlate;
	}

	public void setNumPlate(String numPlate) {
		this.numPlate = numPlate;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getImporter() {
		return importer;
	}

	public void setImporter(String importer) {
		this.importer = importer;
	}

	public Date getImportDate() {
		return importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getDeliveryError() {
		return deliveryError;
	}

	public void setDeliveryError(String deliveryError) {
		this.deliveryError = deliveryError;
	}

	public Long getIsActive() {
		return isActive;
	}

	public void setIsActive(Long isActive) {
		this.isActive = isActive;
	}

	public Long getIsOriginal() {
		return isOriginal;
	}

	public void setIsOriginal(Long isOriginal) {
		this.isOriginal = isOriginal;
	}

	public Date getExpectedDate() {
		return expectedDate;
	}

	public void setExpectedDate(Date expectedDate) {
		this.expectedDate = expectedDate;
	}

	public List<WhDocumentDTO> getLstWHOriginal() {
		return lstWHOriginal;
	}

	public void setLstWHOriginal(List<WhDocumentDTO> lstWHOriginal) {
		this.lstWHOriginal = lstWHOriginal;
	}

	public String getIndentityNum() {
		return indentityNum;
	}

	public void setIndentityNum(String indentityNum) {
		this.indentityNum = indentityNum;
	}

	public Date getIndentityIssueDate() {
		return indentityIssueDate;
	}

	public void setIndentityIssueDate(Date indentityIssueDate) {
		this.indentityIssueDate = indentityIssueDate;
	}

	public Date getIndentityExpiryDate() {
		return indentityExpiryDate;
	}

	public void setIndentityExpiryDate(Date indentityExpiryDate) {
		this.indentityExpiryDate = indentityExpiryDate;
	}

	public String getIndentityIssuePlace() {
		return indentityIssuePlace;
	}

	public void setIndentityIssuePlace(String indentityIssuePlace) {
		this.indentityIssuePlace = indentityIssuePlace;
	}

	public String getIndentityIssuePlaceText() {
		return indentityIssuePlaceText;
	}

	public void setIndentityIssuePlaceText(String indentityIssuePlaceText) {
		this.indentityIssuePlaceText = indentityIssuePlaceText;
	}

	public String getErrorNote() {
		return errorNote;
	}

	public void setErrorNote(String errorNote) {
		this.errorNote = errorNote;
	}

	public Long getAppendixContract() {
		return appendixContract;
	}

	public void setAppendixContract(Long appendixContract) {
		this.appendixContract = appendixContract;
	}

	public Date getContractdate() {
		return contractdate;
	}

	public void setContractdate(Date contractdate) {
		this.contractdate = contractdate;
	}

	public Date getActualcloseddate() {
		return actualcloseddate;
	}

	public void setActualcloseddate(Date actualcloseddate) {
		this.actualcloseddate = actualcloseddate;
	}

	public Long getIndentityTypeId() {
		return indentityTypeId;
	}

	public void setIndentityTypeId(Long indentityTypeId) {
		this.indentityTypeId = indentityTypeId;
	}

	public String getIndentityTypeCode() {
		return indentityTypeCode;
	}

	public void setIndentityTypeCode(String indentityTypeCode) {
		this.indentityTypeCode = indentityTypeCode;
	}

	public String getIndentityTypeName() {
		return indentityTypeName;
	}

	public void setIndentityTypeName(String indentityTypeName) {
		this.indentityTypeName = indentityTypeName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getWardId() {
		return wardId;
	}

	public void setWardId(Long wardId) {
		this.wardId = wardId;
	}

	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public String getAddressFull() {
		return addressFull;
	}

	public void setAddressFull(String addressFull) {
		this.addressFull = addressFull;
	}

	public String getCustMobile() {
		return custMobile;
	}

	public void setCustMobile(String custMobile) {
		this.custMobile = custMobile;
	}

	public void setThankAndkErrorDate(WareHouseSeachObject obj, Date sendThankLetter, Date dateErrCavet,
			Date dateErrLoanDoc) {
		obj.setSendThankLetter(sendThankLetter);
		obj.setDateErrCavet(dateErrCavet);
		obj.setDateErrLoanDoc(dateErrLoanDoc);
	}

	public HashMap<String, List<String>> objectMapCodeTabe() {
		HashMap<String, List<String>> objectMapCodeTabe = new HashMap<String, List<String>>();

		objectMapCodeTabe.put("docTypeId", new ArrayList<String>(Arrays.asList("docTypeCode", "docTypeName")));
		objectMapCodeTabe.put("postCodeId", new ArrayList<String>(Arrays.asList("postCode", "postCodeName")));
		objectMapCodeTabe.put("productGroupId", new ArrayList<String>(Arrays.asList("productGroupCode", "productGroupName")));
		
		objectMapCodeTabe.put("workFlowId", new ArrayList<String>(Arrays.asList("workFlowDESC1", "workFlowDESC2")));
		
		objectMapCodeTabe.put("allocateStatus", new ArrayList<String>(Arrays.asList("allocateCode", "allocateDESC")));
		objectMapCodeTabe.put("goodsId", new ArrayList<String>(Arrays.asList("goodsCode", "goodsDESC")));
		objectMapCodeTabe.put("brandId", new ArrayList<String>(Arrays.asList("brandCode", "brandName")));
		objectMapCodeTabe.put("modelId", new ArrayList<String>(Arrays.asList("modelTypeCode", "modelTypeDESC")));
		
		objectMapCodeTabe.put("commodityId", new ArrayList<String>(Arrays.asList("commodityCode", "commodityDESC")));

		objectMapCodeTabe.put("statusBPM", new ArrayList<String>(Arrays.asList("statusBPMCode", "statusBPMName")));
		objectMapCodeTabe.put("statusWH", new ArrayList<String>(Arrays.asList("statusWHCode", "statusWHName")));
		objectMapCodeTabe.put("statusApprove", new ArrayList<String>(Arrays.asList("statusApproveCode", "statusApproveDESC")));

		objectMapCodeTabe.put("addressFull", new ArrayList<String>(Arrays.asList("wardId", "districtId", "provinceId")));
		objectMapCodeTabe.put("indentityIssuePlace", new ArrayList<String>(Arrays.asList("indentityIssuePlaceText")));

		return objectMapCodeTabe;
	}
}
