package com.mcredit.product.dto;

import java.util.Date;
import java.util.List;

import com.mcredit.model.dto.product.CommodityDTO;

public class UploadFileComodityDTO {

	private String fileName;
	private String fileDri;
	private String fileType;
	private String updateBy;
	private Date updateDate; 
	private String type;
	private String category;
	private Integer productChannelId;
	private Integer productGroupId;
	
	private List<CommodityDTO> lstCommodity;
	private String fileUpload;
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileDri() {
		return fileDri;
	}
	public void setFileDri(String fileDri) {
		this.fileDri = fileDri;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<CommodityDTO> getLstCommodity() {
		return lstCommodity;
	}
	public void setLstCommodity(List<CommodityDTO> lstCommodity) {
		this.lstCommodity = lstCommodity;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Integer getProductChannelId() {
		return productChannelId;
	}
	public void setProductChannelId(Integer productChannelId) {
		this.productChannelId = productChannelId;
	}
	public Integer getProductGroupId() {
		return productGroupId;
	}
	public void setProductGroupId(Integer productGroupId) {
		this.productGroupId = productGroupId;
	}

	public String getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(String fileUpload) {
		this.fileUpload = fileUpload;
	}
}
