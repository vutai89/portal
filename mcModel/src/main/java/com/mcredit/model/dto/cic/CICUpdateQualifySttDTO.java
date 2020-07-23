package com.mcredit.model.dto.cic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CICUpdateQualifySttDTO {
	
	private String requestId;
	private String leadId;
	private int qualifyStatus;
	private int qualifyRejectCode;
	private String qualifyTime;
	private String qualifyReason;
}
