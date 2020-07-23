package com.mcredit.model.dto.warehouse;

import java.io.Serializable;
import java.util.List;

import com.mcredit.model.object.ecm.LoanDocRespone;
import com.mcredit.model.object.warehouse.CheckRecordsCavetInfo;
import com.mcredit.model.object.warehouse.ReturnDocumentInfo;
import com.mcredit.model.object.warehouse.WhDocumentChangeDTO;

public class CheckRecordsCavetDTO extends CheckRecordsCavetInfo implements Serializable {

	private static final long serialVersionUID = -1144308212760101149L;

	public List<ReturnDocumentInfo> lstReturnDocument;
	public Object lstResultsDocument;
	public List<LoanDocRespone> lstLoanDoc;
	List<WhDocumentChangeDTO> errorList;
	public List<GoodsDTO> lstGoods;
	Boolean checkSaveAndTrans;
	String typeCurrentDocument;

	public String getTypeCurrentDocument() {
		return typeCurrentDocument;
	}

	public void setTypeCurrentDocument(String typeCurrentDocument) {
		this.typeCurrentDocument = typeCurrentDocument;
	}

	public List<ReturnDocumentInfo> getLstReturnDocument() {
		return lstReturnDocument;
	}

	public Boolean getCheckSaveAndTrans() {
		return checkSaveAndTrans;
	}

	public void setCheckSaveAndTrans(Boolean checkSaveAndTrans) {
		this.checkSaveAndTrans = checkSaveAndTrans;
	}

	public void setLstReturnDocument(List<ReturnDocumentInfo> lstReturnDocument) {
		this.lstReturnDocument = lstReturnDocument;
	}

	public Object getLstResultsDocument() {
		return lstResultsDocument;
	}

	public void setLstResultsDocument(Object lstResultsDocument) {
		this.lstResultsDocument = lstResultsDocument;
	}

	public List<LoanDocRespone> getLstLoanDoc() {
		return lstLoanDoc;
	}

	public void setLstLoanDoc(List<LoanDocRespone> lstLoanDoc) {
		this.lstLoanDoc = lstLoanDoc;
	}

	public List<WhDocumentChangeDTO> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<WhDocumentChangeDTO> errorList) {
		this.errorList = errorList;
	}

	public List<GoodsDTO> getLstGoods() {
		return lstGoods;
	}

	public void setLstGoods(List<GoodsDTO> lstGoods) {
		this.lstGoods = lstGoods;
	}

}
