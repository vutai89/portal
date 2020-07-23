package com.mcredit.business.pcb.callout;

import com.mcredit.common.Messages;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;

public class PcbEsbApi {
	private ParametersCacheManager _cParam = CacheManager.Parameters();

	private String _esbHost;

	public PcbEsbApi() {
		this._esbHost = _cParam.findParamValueAsString(ParametersName.ESB_HOST);
	}

	/**
	 * Call PCB via ESB
	 * @param payload
	 * @param pcbLink
	 * @return
	 * @throws Exception
	 */
	public ApiResult pcbCheck(Object payload, String pcbLink) throws Exception {
		ApiResult apiResult = null;
		try (BasedHttpClient bs = new BasedHttpClient()) {
			apiResult=  bs.doPost(this._esbHost + pcbLink, payload, ContentType.Json);			
//		apiResult=  bs.doPost("http://dev-esb.mcredit.com.vn:8280" + pcbLink, payload, ContentType.Json);
		}catch(Exception ex) {
			throw new Exception(Messages.getString("pcb.esbService.checkPcb.error") + ": " +  ex.getMessage());
		}
		return apiResult;
	}
}
