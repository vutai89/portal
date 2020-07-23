package com.mcredit.business.debt_home.callout;

import com.mcredit.model.enums.ParametersName;
import com.mcredit.restcore.enums.AcceptType;
import com.mcredit.restcore.exception.ISRestCoreException;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;

public class esbAPI {
	
	private ParametersCacheManager _cParam = CacheManager.Parameters();
	private String _esbHost;
	
	public esbAPI() {
		this._esbHost = _cParam.findParamValueAsString(ParametersName.ESB_HOST);
	}
	
	public String lookupFiles(String appNumber) throws Exception {
		
		ApiResult result = null;
		try (BasedHttpClient bs = new BasedHttpClient()) {
			result = bs.doGet(this._esbHost + BusinessConstant.DEBT_HOME_LOOKUP_FILES + appNumber, AcceptType.Json);
		}

		if (!result.getStatus())
			throw new Exception(result.getBodyContent());

		return result.getBodyContent();
	}
	
	public String getFilePath(Long fileId) throws Exception {
		ApiResult result = null;
		try (BasedHttpClient bs = new BasedHttpClient()) {
			result = bs.doGet(this._esbHost + BusinessConstant.DEBT_HOME_GET_FILE_PATH + fileId, AcceptType.Json);
		}

		if (!result.getStatus())
			throw new Exception(result.getBodyContent());

		return result.getBodyContent();
	}
}
