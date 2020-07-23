
package com.mcredit.model.object.ecm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mcredit.model.object.ColumnIndex;

public class InputUploadECM {

	@SerializedName("documentCode")
	@Expose
	private String documentCode;
	@SerializedName("documentSource")
	@Expose
	private String documentSource;
	@SerializedName("appNumber")
	@Expose
	@ColumnIndex(index=1)
	private Integer appNumber;
	@SerializedName("appId")
	@Expose
	@ColumnIndex(index=0)
	private String appId;
	@SerializedName("productCode")
	@Expose
	private String productCode;
	@SerializedName("citizenID")
	@Expose
	@ColumnIndex(index=5)
	private String citizenID;
	@SerializedName("ldNumber")
	@Expose
	private String ldNumber;
	@SerializedName("custName")
	@Expose
	@ColumnIndex(index=4)
	private String custName;
	@SerializedName("serverFileName")
	@Expose
	private String serverFileName;
	@SerializedName("userFileName")
	@Expose
	private String userFileName;

	@ColumnIndex(index=2)
	private String folder;
	@ColumnIndex(index=6)
	private String contractNumber;
	@ColumnIndex(index=7)
	private String ldID;
	private String validateStatus;
	private Integer documentId;
	@ColumnIndex(index=3)
	private Integer productId;

	public InputUploadECM() {
		super();
	}

	public InputUploadECM(String documentCode, String documentSource, Integer appNumber, String appId,
			String productCode, String citizenID, String ldNumber, String custName, String serverFileName,
			String userFileName) {
		super();
		this.documentCode = documentCode;
		this.documentSource = documentSource;
		this.appNumber = appNumber;
		this.appId = appId;
		this.productCode = productCode;
		this.citizenID = citizenID;
		this.ldNumber = ldNumber;
		this.custName = custName;
		this.serverFileName = serverFileName;
		this.userFileName = userFileName;
	}

	public String getDocumentCode() {
		return documentCode;
	}

	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}

	public String getDocumentSource() {
		return documentSource;
	}

	public void setDocumentSource(String documentSource) {
		this.documentSource = documentSource;
	}

	public Integer getAppNumber() {
		return appNumber;
	}

	public void setAppNumber(Integer appNumber) {
		this.appNumber = appNumber;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getCitizenID() {
		return citizenID;
	}

	public void setCitizenID(String citizenID) {
		this.citizenID = citizenID;
	}

	public String getLdNumber() {
		return ldNumber;
	}

	public void setLdNumber(String ldNumber) {
		this.ldNumber = ldNumber;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getServerFileName() {
		return serverFileName;
	}

	public void setServerFileName(String serverFileName) {
		this.serverFileName = serverFileName;
	}

	public String getUserFileName() {
		return userFileName;
	}

	public void setUserFileName(String userFileName) {
		this.userFileName = userFileName;
	}

	public String getValidateStatus() {
		return validateStatus;
	}

	public void setValidateStatus(String validateStatus) {
		this.validateStatus = validateStatus;
	}

	public Integer getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Integer documentId) {
		this.documentId = documentId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getLdID() {
		return ldID;
	}

	public void setLdID(String ldID) {
		this.ldID = ldID;
	}

}
