package com.mcredit.business.telesales.util;

import java.text.MessageFormat;

import org.modelmapper.ModelMapper;

import com.mcredit.business.telesales.object.ScoringResponse;
import com.mcredit.business.telesales.object.TSResponseDTO;
import com.mcredit.business.telesales.object.TokenResponse;
import com.mcredit.business.telesales.payload.ScoringCRM;
import com.mcredit.business.telesales.payload.ScoringPayload;
import com.mcredit.business.telesales.payload.ScoringTSPayload;
import com.mcredit.business.telesales.payload.SendMarkESB;
import com.mcredit.business.telesales.payload.SendMarkTS;
import com.mcredit.business.telesales.payload.SendOTP;
import com.mcredit.business.telesales.payload.VerifyTSPayload;
import com.mcredit.common.Messages;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.restcore.enums.AcceptType;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.aggregate.RedisAggregate;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;

public class EsbApi {
	private ParametersCacheManager _cParam = CacheManager.Parameters();
	private final String BEARER_TOKEN_TYPE = "Bearer";
	private final String TOKEN_FOR_TSAPI_REDIS_KEY = "TOKEN_FOR_TSAPI_REDIS_KEY";
	private final ModelMapper modelMapper = new ModelMapper();
	
	private String _esbHost;
	
	private RedisAggregate redisAgg = new RedisAggregate();

	public EsbApi() {
		this._esbHost = _cParam.findParamValueAsString(ParametersName.ESB_HOST);
	}
	
	/**
	 * @deprecated
	 */
	public Object sendOTP(SendOTP payload) throws Exception {
		ApiResult resultApi = null;
		try (BasedHttpClient bs = new BasedHttpClient()) {
				resultApi = bs.doPost(this._esbHost + BusinessConstant.ESB_SEND_OTP, payload, ContentType.Json);
		} catch (Exception e) {
			throw new Exception(Messages.getString("appraisal.esbService.checkRules.error1") + ": " +  e.getMessage());     
		}
		return resultApi;
	}   
	
	public Object sendMarkEsb(SendMarkESB payload) throws Exception {
		ApiResult resultApi = null;
		try (BasedHttpClient bs = new BasedHttpClient()) {
				resultApi = bs.doPost(this._esbHost + BusinessConstant.ESB_MARK_ESB, payload, ContentType.Json, AcceptType.ScoresApi);
		} catch (Exception e) {
			throw new Exception(Messages.getString("appraisal.esbService.checkRules.error1") + ": " +  e.getMessage());     
		}
		return resultApi;
	}
	
	public Object sendMarkTs(SendMarkTS payload) throws Exception {
		ApiResult resultApi = null;
		try (BasedHttpClient bs = new BasedHttpClient()) {
				resultApi = bs.doPost(this._esbHost + BusinessConstant.ESB_MARK_TS, payload, ContentType.Json);
		} catch (Exception e) {
			throw new Exception(Messages.getString("appraisal.esbService.checkRules.error1") + ": " +  e.getMessage());     
		}
		return resultApi;
	}
	
	public Object insertCusHis(SendOTP payload) throws Exception {
		ApiResult resultApi = null;
		try (BasedHttpClient bs = new BasedHttpClient()) {
				resultApi = bs.doPost(this._esbHost + BusinessConstant.ESB_MARK_TS, payload, ContentType.Json);
		} catch (Exception e) {
			throw new Exception(Messages.getString("appraisal.esbService.checkRules.error1") + ": " +  e.getMessage());     
		}
		return resultApi;
	}
	
	public ApiResult checkRule(Object payload) throws Exception {
		ApiResult resultApi = null;
		try (BasedHttpClient bs = new BasedHttpClient()) {
				resultApi = bs.doPost(this._esbHost + BusinessConstant.ESB_CHECK_RULE, payload, ContentType.Json);
		} catch (Exception e) {
			throw new Exception(Messages.getString("esbService.checkRules.error1") + ": " +  e.getMessage());
		}
		return resultApi;
	}
	
	public ApiResult checkRules(Object payload) throws Exception {
		ApiResult resultApi = null;
		try (BasedHttpClient bs = new BasedHttpClient()) {
				resultApi = bs.doPost(this._esbHost + BusinessConstant.ESB_CHECK_RULES, payload, ContentType.Json);
		} catch (Exception e) {
			throw new Exception(Messages.getString("esbService.checkRules.error1") + ": " +  e.getMessage());
		}
		return resultApi;
	}
	
	public ApiResult checkCitizenId(String citizenId, String productGroup, String loanAmount, String appNumber) throws Exception {
		try (BasedHttpClient bs = new BasedHttpClient()) {
			return bs.doGet(this._esbHost + MessageFormat.format(BusinessConstant.ESB_API_CHECK_IDENTIFIER, citizenId, productGroup, loanAmount, appNumber), AcceptType.Json);
		}
	}
	
	// RQ1011
	/**
	 * @author kienvt.ho
	 * @return get token from Trusting social
	 * @throws Exception 
	 */	
	private String getTokenTS() throws Exception {
		ApiResult resultApi = null;
		TokenResponse tokenResponseDTO = null;
		
		try (BasedHttpClient bs = new BasedHttpClient()) {
			resultApi = bs.doPost(this._esbHost + BusinessConstant.ESB_GET_TOKEN_TS, null, ContentType.Json);
			
			System.out.println("[RQ1011 LOG] [GET_TOKEN_TS]");
			System.out.println(com.mcredit.data.util.JSONFactory.toJson(resultApi));
		} catch (Exception e) {
			throw new Exception("Kết nối ESB không khả dụng: " +  e.getMessage());
		}
		
		if (resultApi.getStatus() == true) {
			ScoringResponse scoringDTO = (ScoringResponse) com.mcredit.data.util.JSONFactory.fromJSON(resultApi.getBodyContent().replace("\\", ""), ScoringResponse.class);
			tokenResponseDTO = (TokenResponse) com.mcredit.data.util.JSONFactory.fromJSON(com.mcredit.data.util.JSONFactory.toJson(scoringDTO.getResult()), TokenResponse.class);
		} else {
			throw new Exception(resultApi.getBodyContent());
		}
		
		return tokenResponseDTO.getAccess_token();
	}
	
	/**
	 * @return get token from redis, If it is't exist get from ts
	 * @throws Exception
	 */
	private String getTokenForTSAPI() throws Exception {
		String token = this.redisAgg.get(this.TOKEN_FOR_TSAPI_REDIS_KEY);
		if (token != null) {
			return token;
		} else {
			token = this.getTokenTS();
		}
		return token;
	}
	
	/**
	 * @author kienvt.ho
	 * @return Send OTP to Trusting social
	 */
	public Object sendOTPV2(SendOTP payload) throws Exception {
		ApiResult resultApi = null;
		TSResponseDTO tsResponseDTO = null;
		String token = this.getTokenForTSAPI();
		
		// Call to TS
		try (BasedHttpClient bs = new BasedHttpClient(this.BEARER_TOKEN_TYPE, token)) {
			resultApi = bs.doPost(this._esbHost + BusinessConstant.ESB_SEND_OTP_TS, payload, ContentType.Json);
			
			System.out.println("[RQ1011 LOG] [SEND_OTP_TS]");
			System.out.println(com.mcredit.data.util.JSONFactory.toJson(resultApi));
		} catch (Exception e) {
			throw new Exception("Kết nối ESB không khả dụng: " +  e.getMessage());
		}
		
		// Parse Result to Object
		ScoringResponse scoringDTO = (ScoringResponse) com.mcredit.data.util.JSONFactory.fromJSON(resultApi.getBodyContent().replace("\\", ""), ScoringResponse.class);
		tsResponseDTO = this.modelMapper.map(scoringDTO.getResult(), TSResponseDTO.class);
		
		if (!(resultApi.getStatus() == true || resultApi.getCode() == 400)) {
			throw new Exception(resultApi.getBodyContent());
		}
		
		return tsResponseDTO;
	}
	
	/**
	 * @author kienvt.ho
	 * @return Verify request_id + OTP
	 */
	public boolean verify(VerifyTSPayload verifyTSPayload) throws Exception {
		ApiResult resultApi = null;
		TSResponseDTO tsResponseDTO = null;
		String token = this.getTokenForTSAPI();
		
		// Call to TS
		try (BasedHttpClient bs = new BasedHttpClient(this.BEARER_TOKEN_TYPE, token)) {
			resultApi = bs.doPost(this._esbHost + BusinessConstant.ESB_VERIFY_TS, verifyTSPayload, ContentType.Json);
			
			System.out.println("[RQ1011 LOG] [ESB_VERIFY_TS]");
			System.out.println(com.mcredit.data.util.JSONFactory.toJson(resultApi));
		} catch (Exception e) {
			throw new Exception("Kết nối ESB không khả dụng: " +  e.getMessage());
		}
		
		// Parse Result to Object
		ScoringResponse scoringDTO = (ScoringResponse) com.mcredit.data.util.JSONFactory.fromJSON(resultApi.getBodyContent().replace("\\", ""), ScoringResponse.class);
		tsResponseDTO = this.modelMapper.map(scoringDTO.getResult(), TSResponseDTO.class);
		
		if(!("success".equals(tsResponseDTO.getReturnCode()))) {
			throw new Exception(resultApi.getBodyContent());
		}
		
		return true; 
	}
	
	/**
	 * @author kienvt.ho
	 * @return Call Scoring TS
	 * @throws Exception 
	 */
	public Object scoring(ScoringTSPayload scoringPayload) throws Exception {
		ApiResult resultApi = null;
		TSResponseDTO tsResponseDTO = null;
		String token = this.getTokenForTSAPI();
		
		// Call to TS
		try (BasedHttpClient bs = new BasedHttpClient(this.BEARER_TOKEN_TYPE, token)) {
			resultApi = bs.doPost(this._esbHost + BusinessConstant.ESB_SCORING_TS, scoringPayload, ContentType.Json);
			
			System.out.println("[RQ1011 LOG] [ESB_SCORING_TS]");
			System.out.println(com.mcredit.data.util.JSONFactory.toJson(resultApi));
		} catch (Exception e) {
			throw new Exception("Kết nối ESB không khả dụng: " +  e.getMessage());
		}
		
		// Parse Result to Object
		ScoringResponse scoringDTO = (ScoringResponse) com.mcredit.data.util.JSONFactory.fromJSON(resultApi.getBodyContent().replace("\\", ""), ScoringResponse.class);
		tsResponseDTO = this.modelMapper.map(scoringDTO.getResult(), TSResponseDTO.class);
		
		if (!(resultApi.getStatus() == true || resultApi.getCode() == 400)) {
			throw new Exception(resultApi.getBodyContent());
		}
		
		return tsResponseDTO;
	}
}
