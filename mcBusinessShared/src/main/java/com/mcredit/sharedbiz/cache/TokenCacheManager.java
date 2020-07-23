package com.mcredit.sharedbiz.cache;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.mcredit.common.Messages;
import com.mcredit.data.user.entity.Users;
import com.mcredit.model.dto.AuthorizationRequestDTO;
import com.mcredit.model.dto.AuthorizationResponseDTO;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.aggregate.RedisAggregate;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

public class TokenCacheManager implements IDataCache {
	private static Logger log = Logger.getLogger(TokenCacheManager.class.getName());
	private ReentrantLock lock = new ReentrantLock();
	private RedisAggregate redisAgg = new RedisAggregate();
	private static TokenCacheManager instance;
	private ParametersCacheManager _cache = CacheManager.Parameters();
	
	private String TOKEN_USER = "TOKEN_USER.";
	private String USER_TOKENS = "USER_TOKENS.";
	private String ONE_TIME_TOKEN = "ONE_TIME_TOKEN.";
	
	private int _keepTrackNumber = 10;
	private int _optExpiredInSeconds = 120;//in seconds
	private int _expiredInSeconds = 43200;
	
	private TokenCacheManager() {
		initCache();
		_expiredInSeconds = _cache.findParamValueAsInteger(ParametersName.TOKEN_EXPIRED_TIME);
		int optExpiredInSeconds = _cache.findParamValueAsInteger(ParametersName.OTP_TOKEN_EXPIRED_TIME);
		if(optExpiredInSeconds > 0)
			_optExpiredInSeconds = optExpiredInSeconds;
	}
	
	public static synchronized TokenCacheManager getInstance() {
		if (instance == null) {
			synchronized (TokenCacheManager.class) {
				if (null == instance) {
					instance = new TokenCacheManager();
				}
			}
		}
		return instance;
	}

	public Users get(String token){
		String value = redisAgg.get(this.TOKEN_USER + token);
		return StringUtils.isNullOrEmpty(value)? null : JSONConverter.toObject(value, Users.class);
	}
	
	public Users getOTT(String token){
		try {
			String value = redisAgg.get(this.ONE_TIME_TOKEN + token);
			return StringUtils.isNullOrEmpty(value)? null : JSONConverter.toObject(value, Users.class);
		} catch (Throwable e) {
			log.error("TokenCacheManager.getOTT: " + e.getMessage());
		}finally{
			redisAgg.delete(this.ONE_TIME_TOKEN + token);
		}
		return null;
	}
	
	public void set(String token,Users value){
		this.set(token, value, this._expiredInSeconds);
	}
	
	public void setOTT(String token,Users value){
		redisAgg.set(this.ONE_TIME_TOKEN + token, JSONConverter.toJSON(value),this._optExpiredInSeconds);
	}
	
	public void set(String token,Users value, int expiredInSecondsTime){
		lock.lock();
		try {
			
			//clear previous token of this user
			//this logic support for multiple request at the same time
			int index = 0;
			int total = 0;
			Map<String,String> map = redisAgg.hgetAll(this.USER_TOKENS + value.getLoginId());
			if(map != null && !map.isEmpty()){
				total = map.size();
				for (Map.Entry<String, String> entry : map.entrySet()){
					redisAgg.delete(entry.getKey());
					
					//keep 10 latest items and delete the others
					if(total - index > _keepTrackNumber)
						redisAgg.hdelete(this.USER_TOKENS + value.getLoginId(), entry.getKey());
					
					index++;
				}
			}
			
			//set new token for this user	
			if(expiredInSecondsTime > 0)
				redisAgg.set(this.TOKEN_USER + token, JSONConverter.toJSON(value),expiredInSecondsTime);
			else
				redisAgg.set(this.TOKEN_USER + token, JSONConverter.toJSON(value));//never expire.
			
			//store this token in another key
			//this logic support for multiple request at the same time
			redisAgg.hset(this.USER_TOKENS + value.getLoginId(),this.TOKEN_USER + token, token);
			
		} catch (Throwable e) {
			log.info("TokenCacheManager.set: " + e.getMessage());
		}finally{
			lock.unlock();
		}
	}

	/**
	 * Get bearer token from ott token
	 * @author catld.ho
	 * @param ottToken : ott token
	 * @return bearer token
	 * @throws Exception
	 */
	public synchronized String getBearerToken(String ottToken) throws Exception {
		Users users = getOTT(ottToken);
		if (users == null)
			throw new Exception(Messages.getString("authorize.token.ott.notAvailable"));
		
		// get bearer token
		Map<String, String> tokenMap = redisAgg.hgetAll(this.USER_TOKENS + users.getLoginId());
		if(tokenMap != null && !tokenMap.isEmpty()) {
			for (Map.Entry<String, String> entry : tokenMap.entrySet()){
				if (get(entry.getValue()) != null) {
					return entry.getValue();
				}
			}
		}

		/* If can't find bearer token in cache then set new bearer token for this user */
		String token = UUID.randomUUID().toString();
		set(token, users);

		return token;
	}
	
	public void clear(String token){
		redisAgg.delete(token);
	}
	
	@Override
	public void initCache() {}
	
	@Override
	public void refresh() {
		this.initCache();
	}
}
