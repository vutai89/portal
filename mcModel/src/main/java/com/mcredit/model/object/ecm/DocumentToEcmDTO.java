package com.mcredit.model.object.ecm;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;

import com.mcredit.model.enums.Constant;
import com.mcredit.model.object.ColumnIndex;

public class DocumentToEcmDTO {

	@ColumnIndex(index=0)
	private Long id;

	private Date createdDate;

	private String lastUpdatedBy;

	private Date lastUpdatedDate;
	
	@ColumnIndex(index=1)
	private Integer documentId;
	
	@ColumnIndex(index=2)
	private String documentSource;

	private Long versionNumber;

	@ColumnIndex(index=4)
	private String bpmAppId;

	@ColumnIndex(index=3)
	private Integer bpmAppNumber;

	@ColumnIndex(index=5)
	private String ldNumber;

	@ColumnIndex(index=6)
	private Integer productId;

	@ColumnIndex(index=7)
	private String idNumber;

	@ColumnIndex(index=8)
	private String custName;

	@ColumnIndex(index=9)
	private String userFileName;

	@ColumnIndex(index=10)
	private String serverFileName;

	@ColumnIndex(index=11)
	private String remark;

	private String ecmObjectId;

	private Date uploadTime;
	
	private String deleteFlag;

	@ColumnIndex(index=12)
	private String status;

	private String errorMessage;
	
	@ColumnIndex(index=13)
	private String productCode;
	
	@ColumnIndex(index=14)
	private String productName;
	
	@ColumnIndex(index=23)
	private String typeOfLoan;
	
	private String contractNumber;
	
	@ColumnIndex(index=21)
	private String loanTerm;
	
	@ColumnIndex(index=20)
	private BigDecimal loanAmount;
	// File properties
	@ColumnIndex(index=17)
	private String documentName;
	
	@ColumnIndex(index=19)
	private String folderName;

	@ColumnIndex(index=24)
	private String folder;

	private String year;

	private String month;

	private String day;

	private String fileName;

	private String extention;

	@ColumnIndex(index=9)
	private String filePath;

	private String path2File;
	
	private File localFile;
	
	private String basePath;

	private String mimeType;

	@ColumnIndex(index=18)
	private String documentType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Integer getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Integer documentId) {
		this.documentId = documentId;
	}

	public String getDocumentSource() {
		return documentSource;
	}

	public void setDocumentSource(String documentSource) {
		this.documentSource = documentSource;
	}

	public Long getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Long versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getBpmAppId() {
		return bpmAppId;
	}

	public void setBpmAppId(String bpmAppId) {
		this.bpmAppId = bpmAppId;
	}

	public Integer getBpmAppNumber() {
		return bpmAppNumber;
	}

	public void setBpmAppNumber(Integer bpmAppNumber) {
		this.bpmAppNumber = bpmAppNumber;
	}

	public String getLdNumber() {
		return ldNumber;
	}

	public void setLdNumber(String ldNumber) {
		this.ldNumber = ldNumber;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getUserFileName() {
		return userFileName;
	}

	public void setUserFileName(String userFileName) {
		this.userFileName = userFileName;
	}

	public String getServerFileName() {
		return serverFileName;
	}

	public void setServerFileName(String serverFileName) {
		this.serverFileName = serverFileName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getEcmObjectId() {
		return ecmObjectId;
	}

	public void setEcmObjectId(String ecmObjectId) {
		this.ecmObjectId = ecmObjectId;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
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

	public String getTypeOfLoan() {
		return typeOfLoan;
	}

	public void setTypeOfLoan(String typeOfLoan) {
		this.typeOfLoan = typeOfLoan;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getLoanTerm() {
		return loanTerm;
	}

	public void setLoanTerm(String loanTerm) {
		this.loanTerm = loanTerm;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getExtention() {
		return extention;
	}

	public void setExtention(String extention) {
		this.extention = extention;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getPath2File() {
		return path2File;
	}

	public void setPath2File(String path2File) {
		this.path2File = path2File;
	}

	public File getLocalFile() {
		return localFile;
	}

	public void setLocalFile(File localFile) {
		this.localFile = localFile;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	
	public String getFileFullName() {
		return fileName + "." + extention;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

}
