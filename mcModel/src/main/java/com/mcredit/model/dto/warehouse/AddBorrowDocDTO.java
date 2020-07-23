package com.mcredit.model.dto.warehouse;

import java.io.Serializable;
import java.util.List;

public class AddBorrowDocDTO implements Serializable {
	
	
	private static final long serialVersionUID = -1840963094716824225L;
	
	List<WhBorrowedDocumentDTO> listBorrowedDocument;

	public List<WhBorrowedDocumentDTO> getListBorrowedDocument() {
		return listBorrowedDocument;
	}

	public void setListBorrowedDocument(List<WhBorrowedDocumentDTO> listBorrowedDocument) {
		this.listBorrowedDocument = listBorrowedDocument;
	}
}
