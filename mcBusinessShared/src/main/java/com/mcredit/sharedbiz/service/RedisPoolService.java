package com.mcredit.sharedbiz.service;

import java.time.Duration;
import java.util.concurrent.locks.ReentrantLock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.mcredit.model.enums.ParametersName;
import com.mcredit.sharedbiz.cache.CacheManager;

public class RedisPoolService {

	private static RedisPoolService instance;
	private JedisPool pool = null;
	private static ReentrantLock lock = new ReentrantLock();
	private RedisPoolService(){
		jedisPoolConfig();
	}
	
	public static synchronized RedisPoolService getInstance() {
		lock.lock();
		if (instance == null) {
			synchronized (RedisPoolService.class) {
				if (null == instance) {
					instance = new RedisPoolService();
				}
			}
		}
		lock.unlock();
		return instance;		
	}
	
	private void jedisPoolConfig() {
        JedisPoolConfig poolConfig = buildPoolConfig();
        pool = new JedisPool(poolConfig,CacheManager.Parameters().findParamValueAsString(ParametersName.REDIS_HOST));
    }
	
	private JedisPoolConfig buildPoolConfig() {
	    final JedisPoolConfig poolConfig = new JedisPoolConfig();
	    poolConfig.setMaxTotal(128);
	    poolConfig.setMaxIdle(128);
	    poolConfig.setMinIdle(16);
	    poolConfig.setTestOnBorrow(true);
	    poolConfig.setTestOnReturn(true);
	    poolConfig.setTestWhileIdle(true);
	    poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
	    poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
	    poolConfig.setNumTestsPerEvictionRun(10);
	    poolConfig.setBlockWhenExhausted(true);
	    return poolConfig;
	}
	
	
    public Jedis getJedis() {
        return pool.getResource();
    }
}
