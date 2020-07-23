package com.mcredit.sharedbiz.aggregate;

import java.util.Arrays;
import java.util.Observable;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.mcredit.model.enums.CacheTag;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

public class CacheEventSourceAggregate extends Observable implements Runnable {
	private String bootstrap_servers = null;
	private String kafka_topic = null;
	private String group_id = null;
	private ParametersCacheManager cache = CacheManager.Parameters();
	private CacheTag[] cacheTags;
	
	public CacheEventSourceAggregate(CacheTag[] cacheTags) {
		this.group_id = cache.findParamValueAsString(ParametersName.KAFKA_CACHE_GROUP);
		this.kafka_topic = cache.findParamValueAsString(ParametersName.KAFKA_CACHE_TOPIC);
		this.bootstrap_servers = cache.findParamValueAsString(ParametersName.KAFKA_BOOTSTRAP_SERVER);
		this.cacheTags = cacheTags;
	}
	
	private Properties init(){
		Properties properties = new Properties();
		properties.put("bootstrap.servers", bootstrap_servers);
		properties.put("kafka.topic", kafka_topic);
		properties.put("compression.type", "gzip");
		properties.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
		
		return properties;
	}

	@Override
	public void run() {
		try{
        	Properties properties = init();
    		properties.put("max.partition.fetch.bytes", "20971522");
    		properties.put("max.poll.records", "500");
    		properties.put("group.id", group_id + "." + StringUtils.getComputerName());
    		
    		// Create Kafka producer
    		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);

    		try {

    			consumer.subscribe(Arrays.asList(properties.getProperty("kafka.topic")));
    			System.out.println("Subscribed to topic "+ properties.getProperty("kafka.topic"));

    			while (true) {
    				ConsumerRecords<String, String> records = null;
					try {
						records = consumer.poll(Long.MAX_VALUE);
					} catch (Throwable e) {
						System.out.println("KAFKA ERROR: " + e.getMessage());
						Thread.sleep(5000);
					}
					
					System.out.println("TOPIC DATA: " + JSONConverter.toJSON(records));
					for (ConsumerRecord<String, String> record : records){
						setChanged();
			            notifyObservers(record.value());
					}
    			}
    		}finally {
    			consumer.close();
    		}
        }catch(Throwable ex){
        	ex.printStackTrace();
        }
	}
	
	public boolean isRefresh(CacheTag cache){
		for (int i = 0; i < cacheTags.length; i++) {
			if(cache == cacheTags[i] || cacheTags[i] == CacheTag.ALL)
				return true;
		}
		return false;
	}
}
