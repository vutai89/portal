package com.mcredit.sharedbiz.aggregate;

import java.util.Map;
import java.util.UUID;

import redis.clients.jedis.Jedis;

import com.mcredit.sharedbiz.service.RedisPoolService;

public class RedisAggregate {
	private RedisPoolService redisService = null;
	
	public RedisAggregate(){
		redisService = RedisPoolService.getInstance();
	}
	
	public static void main(String[] args) {
		RedisAggregate agg = new RedisAggregate();
        //Connecting to Redis on with poll config	
		Thread t = new Thread();
		
		for (int i =0; i<= 1000; i++) {
			
			Thread thread = new Thread(){
			    public void run(){
					agg.hset("23232222222", UUID.randomUUID().toString(),UUID.randomUUID().toString());
					agg.delete("23232222222");
			    }
			};
			thread.start();
			
			 thread = new Thread(){
			    public void run(){
					agg.hset("33333333333", UUID.randomUUID().toString(),UUID.randomUUID().toString());
					agg.delete("33333333333");
			    }
			};
			thread.start();
			
			 thread = new Thread(){
			    public void run(){
					agg.hset("44444444444444", UUID.randomUUID().toString(),UUID.randomUUID().toString());
					agg.delete("44444444444444");
			    }
			};
			thread.start();
			System.out.println(i);
		}
		
		Map<String, String> map =agg.hgetAll("111");
		if(map != null && !map.isEmpty()){
			for (Map.Entry<String, String> entry : map.entrySet()){
			    System.out.println(entry.getKey() + "/" + entry.getValue());
			}
		}
    }
	
	public void hset(String key,String field,String value){
		try (Jedis jedis = redisService.getJedis()) {
			jedis.connect();
			jedis.hset(key,field ,value);
		}catch(Throwable th){
			th.printStackTrace();
			System.out.println("ERROR REDIS: " + th.getMessage());
		}
	}
	
	public void set(String key,String value, int expiredInSeconds){
		try (Jedis jedis = redisService.getJedis()) {
			jedis.connect();
			jedis.set(key, value);
			jedis.expire(key, expiredInSeconds);
		}catch(Throwable th){
			th.printStackTrace();
			System.out.println("ERROR REDIS: " + th.getMessage());
		}
	}
	
	public void set(String key,String value){
		try (Jedis jedis = redisService.getJedis()) {
			jedis.connect();
			jedis.set(key, value);
		}catch(Throwable th){
			th.printStackTrace();
			System.out.println("ERROR REDIS: " + th.getMessage());
		}
	}
	
	public Map<String,String> hgetAll(String key){
		try (Jedis jedis = redisService.getJedis()) {
			jedis.connect();
			return jedis.hgetAll(key);
		}catch(Throwable th){
			th.printStackTrace();
			System.out.println("ERROR REDIS: " + th.getMessage());
			return null;
		}
	}
	
	public String get(String key){
		try (Jedis jedis = redisService.getJedis()) {
			jedis.connect();
			return jedis.get(key);
		}catch(Throwable th){
			th.printStackTrace();
			System.out.println("ERROR REDIS: " + th.getMessage());
			return null;
		}
	}
	
	public void delete(String key){
		try (Jedis jedis = redisService.getJedis()) {
			jedis.connect();
			jedis.del(key);
		}catch(Throwable th){
			th.printStackTrace();
			System.out.println("ERROR REDIS: " + th.getMessage());
		}
	}
	
	public void hdelete(String key,String field){
		try (Jedis jedis = redisService.getJedis()) {
			jedis.connect();
			jedis.hdel(key, field);
		}catch(Throwable th){
			th.printStackTrace();
			System.out.println("ERROR REDIS: " + th.getMessage());
		}
	}
}
