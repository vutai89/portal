package com.mcredit.business.checkcic.callout;

import java.net.URLEncoder;
import java.text.MessageFormat;

import com.mcredit.common.Messages;
import com.mcredit.model.dto.cic.CICUpdateQualifySttDTO;
import com.mcredit.model.dto.cic.LeadDataTSReportDTO;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.restcore.enums.AcceptType;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;
import com.mcredit.util.JSONConverter;

public class EsbApi {
	private ParametersCacheManager _cParam = CacheManager.Parameters();

	private String _esbHost;

	public EsbApi() {
		this._esbHost = _cParam.findParamValueAsString(ParametersName.ESB_HOST);
	}
	
	/**
	 * Send request to esb -> bpm get identity from new contract
	 * @author catld.ho
	 * @param from : start time
	 * @param to : end time
	 * @param processId : id concentrating
	 * @return list new identity
	 * @throws Exception
	 */
	public ApiResult getNewIdentity(String from, String to, String processId) throws Exception {
		ApiResult resultApi = null;
		try (BasedHttpClient bs = new BasedHttpClient()) {
			resultApi = bs.doGet(this._esbHost + MessageFormat.format(BusinessConstant.CIC_CLAIM_NEW_IDENTITY_FROM_BPM, processId, URLEncoder.encode(from, "UTF-8"), URLEncoder.encode(to, "UTF-8")), AcceptType.NotSet, ContentType.NotSet);
		} catch (Exception e) {
			throw new Exception(Messages.getString("esb call service CIC_CLAIM_NEW_IDENTITY_FROM_BPM error") + ": " +  e.getMessage());
		}
		return resultApi;
	}
	
	public ApiResult sendLeadgenReport(LeadDataTSReportDTO report) throws Exception {
		ApiResult resultApi = null;
		try (BasedHttpClient bs = new BasedHttpClient()) {
			System.out.println("Send lead data ts report: " + JSONConverter.toJSON(report));
			resultApi = bs.doPost(this._esbHost + BusinessConstant.ESB_SEND_LEAD_REPORT, report, ContentType.Json, AcceptType.ScoresApi);
		} catch (Exception e) {
			throw new Exception(Messages.getString("esb call service ESB_SEND_LEAD_REPORT error") + ": " +  e.getMessage());
		}
		return resultApi;
	}
	
	public ApiResult updateLeadQualifyStatus(CICUpdateQualifySttDTO report) throws Exception {
		ApiResult resultApi = null;
		try (BasedHttpClient bs = new BasedHttpClient()) {
			System.out.println("Update lead's qualification status " + JSONConverter.toJSON(report));
			resultApi = bs.doPost(this._esbHost + BusinessConstant.ESB_UPDATE_QUALIFY_STATUS, report, ContentType.Json, AcceptType.ScoresApi);
			System.out.println("Update lead's qualification result " + resultApi);
		} catch (Exception e) {
			throw new Exception(Messages.getString("esb call service ESB_UPDATE_QUALIFY_STATUS error") + ": " +  e.getMessage());
		}
		return resultApi;
	}

	
}
