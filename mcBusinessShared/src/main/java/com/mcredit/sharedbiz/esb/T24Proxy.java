package com.mcredit.sharedbiz.esb;

import com.mcredit.model.enums.ParametersName;
import com.mcredit.restcore.enums.AcceptType;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.manager.BaseManager;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.JSONConverter;

public class T24Proxy extends BaseManager {
	BasedHttpClient bs = new BasedHttpClient();

	public ApiResult getNapasBankListMC(Object request) throws Exception {
		String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
				+ BusinessConstant.T24_GET_NAPAS_BANK_LIST_MC;
		ApiResult result = bs.doPost(url, JSONConverter.toJSON(request), ContentType.Json, AcceptType.Json);
		return result;
	}

	public ApiResult getNapasAccountNameMC(Object request) throws Exception {
		String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
				+ BusinessConstant.T24_GET_NAPAS_ACCOUNT_NAME_MC;
		ApiResult result = bs.doPost(url, JSONConverter.toJSON(request), ContentType.Json, AcceptType.Json);
		return result;
	}

	public ApiResult getMBAccountNameMC(Object request) throws Exception {
		String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
				+ BusinessConstant.T24_GET_MB_ACCOUNT_NAME_MC;
		ApiResult result = bs.doPost(url, JSONConverter.toJSON(request), ContentType.Json, AcceptType.Json);
		return result;
//			throw new ValidationException(result.getBodyContent());
	}

	public ApiResult getCitadBranchListMC(Object request) throws Exception {
		String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
				+ BusinessConstant.T24_GET_CITAD_BRANCH_LIST_MC;
		ApiResult result = bs.doPost(url, JSONConverter.toJSON(request), ContentType.Json, AcceptType.Json);
		return result;
	}

	public ApiResult getCitadBankListMC(Object request) throws Exception {
		String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
				+ BusinessConstant.T24_GET_CITAD_BANK_LIST_MC;
		ApiResult result = bs.doPost(url, JSONConverter.toJSON(request), ContentType.Json, AcceptType.Json);
		return result;
	}
}
