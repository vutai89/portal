package com.mcredit.model.object.mobile;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadDocumentDTO implements Serializable{
	private static final Long serialVersionUID = 1560387296903450952L;

	private UplCreditAppRequestDTO request;
	private String mobileProductType;
	private String mobileIssueDateCitizen;
	private int appStatus;
	private String md5;
	private String filePath;
	private List<UplCreditAppFilesDTO> info;

	public UplCreditAppRequestDTO getRequest() {
		return request;
	}

	public void setRequest(UplCreditAppRequestDTO request) {
		this.request = request;
	}

	public String getMobileProductType() {
		return mobileProductType;
	}

	public void setMobileProductType(String mobileProductType) {
		this.mobileProductType = mobileProductType;
	}

	public String getMobileIssueDateCitizen() {
		return mobileIssueDateCitizen;
	}

	public void setMobileIssueDateCitizen(String mobileIssueDateCitizen) {
		this.mobileIssueDateCitizen = mobileIssueDateCitizen;
	}

	public int getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(int appStatus) {
		this.appStatus = appStatus;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public List<UplCreditAppFilesDTO> getInfo() {
		return info;
	}

	public void setInfo(List<UplCreditAppFilesDTO> info) {
		this.info = info;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
