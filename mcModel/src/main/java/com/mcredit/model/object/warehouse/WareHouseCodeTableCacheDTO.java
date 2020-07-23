package com.mcredit.model.object.warehouse;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.dto.ProductDTO;

public class WareHouseCodeTableCacheDTO implements Serializable {

	private static final long serialVersionUID = -7664486747206425913L;

	private Integer whChanTypeSaveAndMove;
	private List<Integer> changeToLodgeId;
	private Integer whCaveTypeInCavet;
	private Integer whCaveTypeInAppendix;
	private Integer whAppCavetBrType;
	private Integer whAppCavetBrIdCodeTable;
	private Integer whDocTypeLoanDoc;
	private Integer whDocTypeCavet;
	private Integer whUplDetailId;

	private Integer typeReturn;
	private Integer typeBorrow;
	private Integer typeBorrowLoanDoc;
	private Integer typeBorrowCavet;

	private Integer contactType;
	private Integer contactCategory;

	private Integer whStatusBorrowWait;
	private Integer whStatusReturnWait;

	private Integer addType;

	private Integer whStatusBorrowReturnWait;
	private List<Integer> whStatusBorrowedAndRerected;
	private List<Integer> whStatusReturnAndRerected;

	private List<Integer> carStatus;
	private List<Integer> lstIdCodetabeWH_LOGDE;
	private List<Integer> lstEM_POS_TS;
	private List<Integer> whdocTypeForLodgeCavet;
	private List<Integer> whStatusAllocation;
	private List<Integer> statusAllocation;
	private List<Integer> whStatusLodgeContract;
	private List<Integer> whStatusLodgeCavet;
	private List<Integer> whStatusBorrowContract;
	private List<Integer> whcStatusBorrowCavet;
	private List<Integer> whStatusNotBorrowCavet;
	private List<Integer> whcStatusReturnCavet;
	private List<Integer> whcStatusLodge;

	private List<Integer> whApprovalStatusBorrowCavet;
	private List<Integer> whApprovalStatusReturnCavet;

	private List<Integer> dateOperation2ASC;
	private List<Integer> dateStorageDESC;
	private List<Integer> dateBorrowASC;
	private List<Integer> dateReturnDESC;
	private List<Integer> dateReceiveASC;

	private Integer sendApprovalDate;
	private List<Integer> approvalDate;

	HashMap<String, CodeTableDTO> workFlowCodeTable;
	HashMap<Long, CodeTableDTO> hashCodeSeach;
	HashMap<Long, ProductDTO> hashProduct;
	HashMap<Long, CodeTableDTO> hashIndentityIssuePlace;

	public List<Integer> getLstIdCodetabeWH_LOGDE() {
		return lstIdCodetabeWH_LOGDE;
	}

	public void setLstIdCodetabeWH_LOGDE(List<Integer> lstIdCodetabeWH_LOGDE) {
		this.lstIdCodetabeWH_LOGDE = lstIdCodetabeWH_LOGDE;
	}

	public Integer getContactType() {
		return contactType;
	}

	public void setContactType(Integer contactType) {
		this.contactType = contactType;
	}

	public Integer getContactCategory() {
		return contactCategory;
	}

	public void setContactCategory(Integer contactCategory) {
		this.contactCategory = contactCategory;
	}

	public List<Integer> getLstEM_POS_TS() {
		return lstEM_POS_TS;
	}

	public void setLstEM_POS_TS(List<Integer> lstEM_POS_TS) {
		this.lstEM_POS_TS = lstEM_POS_TS;
	}

	public List<Integer> getCarStatus() {
		return carStatus;
	}

	public void setCarStatus(List<Integer> carStatus) {
		this.carStatus = carStatus;
	}

	public Integer getWhChanTypeSaveAndMove() {
		return whChanTypeSaveAndMove;
	}

	public void setWhChanTypeSaveAndMove(Integer whChanTypeSaveAndMove) {
		this.whChanTypeSaveAndMove = whChanTypeSaveAndMove;
	}

	public List<Integer> getChangeToLodgeId() {
		return changeToLodgeId;
	}

	public void setChangeToLodgeId(List<Integer> changeToLodgeId) {
		this.changeToLodgeId = changeToLodgeId;
	}

	public Integer getWhCaveTypeInCavet() {
		return whCaveTypeInCavet;
	}

	public void setWhCaveTypeInCavet(Integer whCaveTypeInCavet) {
		this.whCaveTypeInCavet = whCaveTypeInCavet;
	}

	public Integer getWhCaveTypeInAppendix() {
		return whCaveTypeInAppendix;
	}

	public void setWhCaveTypeInAppendix(Integer whCaveTypeInAppendix) {
		this.whCaveTypeInAppendix = whCaveTypeInAppendix;
	}

	public Integer getWhAppCavetBrType() {
		return whAppCavetBrType;
	}

	public void setWhAppCavetBrType(Integer whAppCavetBrType) {
		this.whAppCavetBrType = whAppCavetBrType;
	}

	public Integer getWhAppCavetBrIdCodeTable() {
		return whAppCavetBrIdCodeTable;
	}

	public void setWhAppCavetBrIdCodeTable(Integer whAppCavetBrIdCodeTable) {
		this.whAppCavetBrIdCodeTable = whAppCavetBrIdCodeTable;
	}

	public List<Integer> getWhStatusAllocation() {
		return whStatusAllocation;
	}

	public void setWhStatusAllocation(List<Integer> whStatusAllocation) {
		this.whStatusAllocation = whStatusAllocation;
	}

	public List<Integer> getStatusAllocation() {
		return statusAllocation;
	}

	public void setStatusAllocation(List<Integer> statusAllocation) {
		this.statusAllocation = statusAllocation;
	}

	public List<Integer> getWhdocTypeForLodgeCavet() {
		return whdocTypeForLodgeCavet;
	}

	public void setWhdocTypeForLodgeCavet(List<Integer> whdocTypeForLodgeCavet) {
		this.whdocTypeForLodgeCavet = whdocTypeForLodgeCavet;
	}

	public List<Integer> getWhStatusLodgeContract() {
		return whStatusLodgeContract;
	}

	public void setWhStatusLodgeContract(List<Integer> whStatusLodgeContract) {
		this.whStatusLodgeContract = whStatusLodgeContract;
	}

	public List<Integer> getWhStatusLodgeCavet() {
		return whStatusLodgeCavet;
	}

	public void setWhStatusLodgeCavet(List<Integer> whStatusLodgeCavet) {
		this.whStatusLodgeCavet = whStatusLodgeCavet;
	}

	public Integer getWhDocTypeLoanDoc() {
		return whDocTypeLoanDoc;
	}

	public void setWhDocTypeLoanDoc(Integer whDocTypeLoanDoc) {
		this.whDocTypeLoanDoc = whDocTypeLoanDoc;
	}

	public List<Integer> getWhStatusBorrowContract() {
		return whStatusBorrowContract;
	}

	public void setWhStatusBorrowContract(List<Integer> whStatusBorrowContract) {
		this.whStatusBorrowContract = whStatusBorrowContract;
	}

	public Integer getWhDocTypeCavet() {
		return whDocTypeCavet;
	}

	public void setWhDocTypeCavet(Integer whDocTypeCavet) {
		this.whDocTypeCavet = whDocTypeCavet;
	}

	public List<Integer> getWhcStatusBorrowCavet() {
		return whcStatusBorrowCavet;
	}

	public void setWhcStatusBorrowCavet(List<Integer> whcStatusBorrowCavet) {
		this.whcStatusBorrowCavet = whcStatusBorrowCavet;
	}

	public List<Integer> getWhStatusNotBorrowCavet() {
		return whStatusNotBorrowCavet;
	}

	public void setWhStatusNotBorrowCavet(List<Integer> whStatusNotBorrowCavet) {
		this.whStatusNotBorrowCavet = whStatusNotBorrowCavet;
	}

	public List<Integer> getWhcStatusReturnCavet() {
		return whcStatusReturnCavet;
	}

	public void setWhcStatusReturnCavet(List<Integer> whcStatusReturnCavet) {
		this.whcStatusReturnCavet = whcStatusReturnCavet;
	}

	public List<Integer> getWhcStatusLodge() {
		return whcStatusLodge;
	}

	public void setWhcStatusLodge(List<Integer> whcStatusLodge) {
		this.whcStatusLodge = whcStatusLodge;
	}

	public HashMap<String, CodeTableDTO> getWorkFlowCodeTable() {
		return workFlowCodeTable;
	}

	public void setWorkFlowCodeTable(HashMap<String, CodeTableDTO> workFlowCodeTable) {
		this.workFlowCodeTable = workFlowCodeTable;
	}

	public HashMap<Long, CodeTableDTO> getHashCodeSeach() {
		return hashCodeSeach;
	}

	public void setHashCodeSeach(HashMap<Long, CodeTableDTO> hashCodeSeach) {
		this.hashCodeSeach = hashCodeSeach;
	}

	public HashMap<Long, ProductDTO> getHashProduct() {
		return hashProduct;
	}

	public void setHashProduct(HashMap<Long, ProductDTO> hashProduct) {
		this.hashProduct = hashProduct;
	}

	public List<Integer> getDateOperation2ASC() {
		return dateOperation2ASC;
	}

	public void setDateOperation2ASC(List<Integer> dateOperation2ASC) {
		this.dateOperation2ASC = dateOperation2ASC;
	}

	public List<Integer> getDateStorageDESC() {
		return dateStorageDESC;
	}

	public void setDateStorageDESC(List<Integer> dateStorageDESC) {
		this.dateStorageDESC = dateStorageDESC;
	}

	public List<Integer> getDateBorrowASC() {
		return dateBorrowASC;
	}

	public void setDateBorrowASC(List<Integer> dateBorrowASC) {
		this.dateBorrowASC = dateBorrowASC;
	}

	public List<Integer> getDateReturnDESC() {
		return dateReturnDESC;
	}

	public void setDateReturnDESC(List<Integer> dateReturnDESC) {
		this.dateReturnDESC = dateReturnDESC;
	}

	public List<Integer> getDateReceiveASC() {
		return dateReceiveASC;
	}

	public void setDateReceiveASC(List<Integer> dateReceiveASC) {
		this.dateReceiveASC = dateReceiveASC;
	}

	public List<Integer> getWhStatusBorrowedAndRerected() {
		return whStatusBorrowedAndRerected;
	}

	public void setWhStatusBorrowedAndRerected(List<Integer> whStatusBorrowedAndRerected) {
		this.whStatusBorrowedAndRerected = whStatusBorrowedAndRerected;
	}

	public Integer getWhStatusBorrowReturnWait() {
		return whStatusBorrowReturnWait;
	}

	public void setWhStatusBorrowReturnWait(Integer whStatusBorrowReturnWait) {
		this.whStatusBorrowReturnWait = whStatusBorrowReturnWait;
	}

	public Integer getWhUplDetailId() {
		return whUplDetailId;
	}

	public void setWhUplDetailId(Integer whUplDetailId) {
		this.whUplDetailId = whUplDetailId;
	}

	public Integer getTypeReturn() {
		return typeReturn;
	}

	public void setTypeReturn(Integer typeReturn) {
		this.typeReturn = typeReturn;
	}

	public Integer getTypeBorrow() {
		return typeBorrow;
	}

	public void setTypeBorrow(Integer typeBorrow) {
		this.typeBorrow = typeBorrow;
	}

	public Integer getTypeBorrowLoanDoc() {
		return typeBorrowLoanDoc;
	}

	public void setTypeBorrowLoanDoc(Integer typeBorrowLoanDoc) {
		this.typeBorrowLoanDoc = typeBorrowLoanDoc;
	}

	public Integer getTypeBorrowCavet() {
		return typeBorrowCavet;
	}

	public void setTypeBorrowCavet(Integer typeBorrowCavet) {
		this.typeBorrowCavet = typeBorrowCavet;
	}

	public Integer getSendApprovalDate() {
		return sendApprovalDate;
	}

	public void setSendApprovalDate(Integer sendApprovalDate) {
		this.sendApprovalDate = sendApprovalDate;
	}

	public List<Integer> getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(List<Integer> approvalDate) {
		this.approvalDate = approvalDate;
	}

	public Integer getAddType() {
		return addType;
	}

	public void setAddType(Integer addType) {
		this.addType = addType;
	}

	public Integer getWhStatusBorrowWait() {
		return whStatusBorrowWait;
	}

	public void setWhStatusBorrowWait(Integer whStatusBorrowWait) {
		this.whStatusBorrowWait = whStatusBorrowWait;
	}

	public Integer getWhStatusReturnWait() {
		return whStatusReturnWait;
	}

	public void setWhStatusReturnWait(Integer whStatusReturnWait) {
		this.whStatusReturnWait = whStatusReturnWait;
	}

	public List<Integer> getWhStatusReturnAndRerected() {
		return whStatusReturnAndRerected;
	}

	public void setWhStatusReturnAndRerected(List<Integer> whStatusReturnAndRerected) {
		this.whStatusReturnAndRerected = whStatusReturnAndRerected;
	}

	public List<Integer> getWhApprovalStatusBorrowCavet() {
		return whApprovalStatusBorrowCavet;
	}

	public void setWhApprovalStatusBorrowCavet(List<Integer> whApprovalStatusBorrowCavet) {
		this.whApprovalStatusBorrowCavet = whApprovalStatusBorrowCavet;
	}

	public List<Integer> getWhApprovalStatusReturnCavet() {
		return whApprovalStatusReturnCavet;
	}

	public void setWhApprovalStatusReturnCavet(List<Integer> whApprovalStatusReturnCavet) {
		this.whApprovalStatusReturnCavet = whApprovalStatusReturnCavet;
	}

	public HashMap<Long, CodeTableDTO> getHashIndentityIssuePlace() {
		return hashIndentityIssuePlace;
	}

	public void setHashIndentityIssuePlace(HashMap<Long, CodeTableDTO> hashIndentityIssuePlace) {
		this.hashIndentityIssuePlace = hashIndentityIssuePlace;
	}

}
