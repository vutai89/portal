package com.mcredit.model.dto.telesales;

public class ProspectDTO {
	private UplMasterDTO uplMasterDTO;
	private UplDetailDTO uplDetailDTO;
	private UploadFileDTO uploadFileDTO;

	public UplMasterDTO getUplMasterDTO() {
		return uplMasterDTO;
	}

	public void setUplMasterDTO(UplMasterDTO uplMasterDTO) {
		this.uplMasterDTO = uplMasterDTO;
	}

	public UplDetailDTO getUplDetailDTO() {
		return uplDetailDTO;
	}

	public void setUplDetailDTO(UplDetailDTO uplDetailDTO) {
		this.uplDetailDTO = uplDetailDTO;
	}

	public UploadFileDTO getUploadFileDTO() {
		return uploadFileDTO;
	}

	public void setUploadFileDTO(UploadFileDTO uploadFileDTO) {
		this.uploadFileDTO = uploadFileDTO;
	}
}
