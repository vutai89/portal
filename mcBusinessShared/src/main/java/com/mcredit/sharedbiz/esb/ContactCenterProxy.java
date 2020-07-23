package com.mcredit.sharedbiz.esb;

import com.mcredit.model.enums.ParametersName;
import com.mcredit.restcore.enums.AcceptType;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.dto.ClickToCallDTO;
import com.mcredit.sharedbiz.manager.BaseManager;
import com.mcredit.util.JSONConverter;

public class ContactCenterProxy extends BaseManager {
	BasedHttpClient bs = new BasedHttpClient();

	public ApiResult dial(ClickToCallDTO request) throws Exception {
		ClickToCallDTO item = new ClickToCallDTO(request.getAgentID(), request.getExtNumber(), request.getDeviceName(),
				request.getMobilePhone());

		String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
				+ BusinessConstant.CONTACT_CENTER_CALL;

		return bs.doPost(url, JSONConverter.toJSON(item), ContentType.Json, AcceptType.Json);
	}

	public ApiResult getCallOutcome(Object request) throws Exception {
		String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
				+ BusinessConstant.CONTACT_CENTER_GET_CALL_OUTCOME;
		ApiResult result = bs.doPost(url, JSONConverter.toJSON(request), ContentType.Json);
		return result;
	}

	public ApiResult getBusinessOutcome(Object request) throws Exception {
		String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
				+ BusinessConstant.CONTACT_CENTER_GET_BUSINESS_OUTCOME;
		ApiResult result = bs.doPost(url, JSONConverter.toJSON(request), ContentType.Json);
		return result;
	}

	public ApiResult setBusinessOutcome(Object request) throws Exception {
		String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
				+ BusinessConstant.CONTACT_CENTER_SET_BUSINESS_OUTCOME;
		ApiResult result = bs.doPost(url, JSONConverter.toJSON(request), ContentType.Json);
		return result;
	}

	public ApiResult getExtNumber(String username) throws Exception {
		String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
				+ BusinessConstant.CONTACT_CENTER_GET_EXT_NUMBER + username;
		ApiResult result = bs.doGet(url, ContentType.NotSet);
		return result;
	}

	public ApiResult postDial(Object request) throws Exception {
		String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
				+ BusinessConstant.CONTACT_CENTER_POST_DIAL;
		ApiResult result = bs.doPost(url, JSONConverter.toJSON(request), ContentType.Json);
		return result;
	}
}
