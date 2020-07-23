package com.mcredit.sharedbiz.esb;

import java.net.URLEncoder;

import com.mcredit.model.enums.ParametersName;
import com.mcredit.restcore.enums.AcceptType;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.manager.BaseManager;
import com.mcredit.util.JSONConverter;

public class BPMProxy extends BaseManager {
	BasedHttpClient bs = new BasedHttpClient();

	public ApiResult getDepartmentsBatch() throws Exception {
		String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
				+ BusinessConstant.BPM_DEPARTMENT_BATCH;
		ApiResult result = bs.doGet(url, AcceptType.Json, ContentType.Json);
		return result;
	}

	public ApiResult getDepartmentsArea() throws Exception {
		String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
				+ BusinessConstant.BPM_DEPARTMENT_AREA;
		ApiResult result = bs.doGet(url, AcceptType.Json, ContentType.Json);
		return result;
	}

	public ApiResult getDepartmentsRegion() throws Exception {
		String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
				+ BusinessConstant.BPM_DEPARTMENT_REGION;
		ApiResult result = bs.doGet(url, AcceptType.Json, ContentType.Json);
		return result;
	}

	public ApiResult getDepartmentHistory(String departmentId) throws Exception {
		String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
				+ BusinessConstant.BPM_DEPARTMENT_HISTORY + "/" + departmentId;
		ApiResult result = bs.doGet(url, AcceptType.Json, ContentType.Json);
		return result;
	}

	public ApiResult searchDepartments(String isListUpdated, String regionId, String areaId, String batchId,
			String updateStatus, String departmentCode, String departmentName, String sipType, String updateDate,
			String offset, String limit) throws Exception {
		String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
				+ BusinessConstant.BPM_DEPARTMENT_SEARCH + "?isListUpdated=" + isListUpdated + "&RegionId=" + regionId
				+ "&AreaId=" + areaId + "&BatchId=" + batchId + "&UpdateStatus=" + updateStatus + "&DepartmentCode="
				+ URLEncoder.encode(departmentCode, "UTF-8") + "&DepartmentName="
				+ URLEncoder.encode(departmentName, "UTF-8") + "&SipType=" + sipType + "&UpdateDate=" + updateDate
				+ "&offset=" + offset + "&limit=" + limit;
		url = url.replace("null", "");
		ApiResult result = bs.doGet(url.replace("null", ""), AcceptType.Json, ContentType.Json);
		return result;
	}

	public ApiResult updateDepartmnet(Object request) throws Exception {
		String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
				+ BusinessConstant.BPM_DEPARTMENT_PAYMENT_INFO_UPDATE;
		ApiResult result = bs.doPut(url, JSONConverter.toJSON(request), AcceptType.Json, ContentType.Json);
		return result;
	}

	public ApiResult getListFileUpload(String appId) throws Exception {
		String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
				+ BusinessConstant.BPM_CASE_GET_LIST_FILE_UPLOAD + appId;
		System.out.print("Log Download File: " + url);
		ApiResult result = bs.doGet(url, AcceptType.Json, ContentType.Json);
		return result;
	}
}
