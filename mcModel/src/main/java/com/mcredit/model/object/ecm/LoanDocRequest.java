package com.mcredit.model.object.ecm;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoanDocRequest {

	private String contentInBase64;
	private byte[] content;
	private String typeOfLoan;
	private String fileName;
	private String mimeType; //
	private String documentType;

	private String loanTerm;
	private String refNumber; // = appId
	private String contractNumber;
	private Boolean isScan;
	private String productCode;
	private String productName;
	private String idCardNumber;
	private String refName; // = appNumber
	private String customerName;
	private Double loanAmount;

	private String businessStep;

	public LoanDocRequest() {
		super();
	}

	public LoanDocRequest(String contentInBase64, byte[] content, String typeOfLoan, String fileName, String mimeType,
			String documentType, String loanTerm, String refNumber, String contractNumber, Boolean isScan,
			String productCode, String productName, String idCardNumber, String refName, String customerName,
			Double loanAmount, String businessStep) {
		super();
		this.contentInBase64 = contentInBase64;
		this.content = content;
		this.typeOfLoan = typeOfLoan;
		this.fileName = fileName;
		this.mimeType = mimeType;
		this.documentType = documentType;
		this.loanTerm = loanTerm;
		this.refNumber = refNumber;
		this.contractNumber = contractNumber;
		this.isScan = isScan;
		this.productCode = productCode;
		this.productName = productName;
		this.idCardNumber = idCardNumber;
		this.refName = refName;
		this.customerName = customerName;
		this.loanAmount = loanAmount;
		this.businessStep = businessStep;
	}
	
	

	public LoanDocRequest( String refName ) {
		super();	
		this.refName = refName;
	}

	public String getLoanTerm() {
		return loanTerm;
	}

	public void setLoanTerm(String loanTerm) {
		this.loanTerm = loanTerm;
	}

	public String getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public Boolean getIsScan() {
		return isScan;
	}

	public void setIsScan(Boolean isScan) {
		this.isScan = isScan;
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

	public String getIdCardNumber() {
		return idCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}

	public String getRefName() {
		return refName;
	}

	public void setRefName(String refName) {
		this.refName = refName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Double getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(Double loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getTypeOfLoan() {
		return typeOfLoan;
	}

	public void setTypeOfLoan(String typeOfLoan) {
		this.typeOfLoan = typeOfLoan;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getContentInBase64() {
		return contentInBase64;
	}

	public void setContentInBase64(String contentInBase64) {
		this.contentInBase64 = contentInBase64;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getBusinessStep() {
		return businessStep;
	}

	public void setBusinessStep(String businessStep) {
		this.businessStep = businessStep;
	}

	@Override
	public String toString() {
		return "LoanDocRequest [typeOfLoan=" + typeOfLoan + ", fileName=" + fileName + ", mimeType=" + mimeType
				+ ", documentType=" + documentType + ", loanTerm=" + loanTerm + ", refNumber=" + refNumber
				+ ", contractNumber=" + contractNumber + ", isScan=" + isScan + ", productCode=" + productCode
				+ ", productName=" + productName + ", idCardNumber=" + idCardNumber + ", refName=" + refName
				+ ", customerName=" + customerName + ", loanAmount=" + loanAmount + ", businessStep=" + businessStep
				+ "]";
	}

}
