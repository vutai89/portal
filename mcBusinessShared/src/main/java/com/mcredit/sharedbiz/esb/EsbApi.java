package com.mcredit.sharedbiz.esb;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.List;

import com.mcredit.model.enums.ParametersName;
import com.mcredit.restcore.enums.AcceptType;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;
import com.mcredit.sharedbiz.validation.ValidationException;

public class EsbApi {
	private ParametersCacheManager _cParam = CacheManager.Parameters();

	private String _esbHost;

	public EsbApi() {
		this._esbHost = _cParam.findParamValueAsString(ParametersName.ESB_HOST);
	}
	
	public ApiResult createCIC(String citizenId, String customerName, String userName) throws Exception {
		ApiResult resultApi = null;
		try (BasedHttpClient bs = new BasedHttpClient()) {
				resultApi = bs.doGet(this._esbHost + MessageFormat.format(BusinessConstant.ESB_CREATE_CIC, citizenId, URLEncoder.encode(customerName, "UTF-8"), URLEncoder.encode(userName, "UTF-8")), AcceptType.NotSet, ContentType.NotSet);
		} catch (Exception e) {
			throw new Exception("Service create cic error : " +  e.getMessage());     
		}
		return resultApi;
	}
	
	/**
	 * Get total debt in MC: du lieu lay ben DWH
	 * @param identityId
	 * @param oldIdentityId
	 * @param militaryId
	 * @return
	 * @throws ValidationException
	 */
	public ApiResult findTotalDebtMC(String identityId, String oldIdentityId, String militaryId) throws ValidationException {
		ApiResult result = null;
		try(BasedHttpClient bs = new BasedHttpClient()) {
			result = bs.doGet(this._esbHost + MessageFormat.format(BusinessConstant.ESB_TOTAL_DEBT_MC, identityId, militaryId, oldIdentityId));
		} catch(Exception e) {
			throw new ValidationException("Disbursement/totalOSBalance error: " + e.getMessage());
		}
		return result;
	}
	
}
