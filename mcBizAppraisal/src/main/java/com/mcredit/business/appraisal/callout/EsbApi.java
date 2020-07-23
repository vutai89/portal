package com.mcredit.business.appraisal.callout;

import com.mcredit.common.Messages;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;

public class EsbApi {
	private ParametersCacheManager _cParam = CacheManager.Parameters();

	private String _esbHost;

	public EsbApi() {
		this._esbHost = _cParam.findParamValueAsString(ParametersName.ESB_HOST);
	}
	
	/**
	 * Send request check multiple rule to Rule Service
	 * @author catld.ho
	 * @param payload : list rule object need check
	 * @return list result
	 * @throws Exception
	 */
	public ApiResult checkRules(Object payload) throws Exception {
		ApiResult resultApi = null;
		try (BasedHttpClient bs = new BasedHttpClient()) {
				resultApi = bs.doPost(this._esbHost + BusinessConstant.ESB_CHECK_RULES, payload, ContentType.Json);
		} catch (Exception e) {
			throw new Exception(Messages.getString("appraisal.esbService.checkRules.error1") + ": " +  e.getMessage());
		}
		return resultApi;
	}
}
