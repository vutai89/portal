package com.mcredit.business.checktools.callout;

import java.text.MessageFormat;

import com.mcredit.common.Messages;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.restcore.enums.AcceptType;
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
	 * Lay thong tin duplicate hop dong
	 * @param citizenId
	 * @param appNumber
	 * @return
	 * @throws Exception
	 */
	public ApiResult getDuplicateContract(String citizenId, String appNumber) throws Exception {
		ApiResult resultApi = null;
		try (BasedHttpClient bs = new BasedHttpClient()) {
			resultApi = bs.doGet(this._esbHost + MessageFormat.format(BusinessConstant.ESB_DUPLICATE_CONTRACT, appNumber, citizenId), AcceptType.NotSet, ContentType.NotSet);
		} catch (Exception e) {
			throw new Exception("esb call service ESB_DUPLICATE_CONTRACT appNumber=" + appNumber + ", error: " +  e.getMessage());
		}
		return resultApi;
	}
	
	/**
	 * Lay thong tin so tien phai tra hang thang cua cac khoan vay active/backdate
	 * @param citizenId
	 * @return
	 * @throws Exception
	 */
	public ApiResult getEMIContractApproved(String citizenId) throws Exception {
		ApiResult resultApi = null;
		try (BasedHttpClient bs = new BasedHttpClient()) {
			resultApi = bs.doGet(this._esbHost + MessageFormat.format(BusinessConstant.ESB_GET_EMI_CONTRACT_APPROVED, citizenId), AcceptType.NotSet, ContentType.NotSet);
		} catch (Exception e) {
			throw new Exception("esb call service ESB_GET_EMI_CONTRACT_APPROVED citizenId=" + citizenId + ", error: " +  e.getMessage());
		}
		return resultApi;
	}
	
	/**
	 * Goi service check rules
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	public ApiResult checkRule(Object payload) throws Exception {
		ApiResult resultApi = null;
		try (BasedHttpClient bs = new BasedHttpClient()) {
				resultApi = bs.doPost(this._esbHost + BusinessConstant.ESB_CHECK_RULE, payload, ContentType.Json);
		} catch (Exception e) {
			throw new Exception(Messages.getString("esbService.checkRules.error1") + ": " +  e.getMessage());
		}
		return resultApi;
	}
	
}
