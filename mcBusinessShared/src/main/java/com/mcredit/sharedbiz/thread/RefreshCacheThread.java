package com.mcredit.sharedbiz.thread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.mcredit.model.enums.CacheTag;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;
import com.mcredit.util.StringUtils;

public class RefreshCacheThread implements Runnable {

	private String bootstrap_servers = null;
	private String kafka_topic = null;
	private String group_id = null;
	private String projectName;
	private ParametersCacheManager cache = CacheManager.Parameters();
	private CacheTag[] lstCacheTag;

	public RefreshCacheThread(String projectName, CacheTag[] lstCacheTag) {
		this.bootstrap_servers = cache.findParamValueAsString(ParametersName.KAFKA_BOOTSTRAP_SERVER);
		this.group_id = cache.findParamValueAsString(ParametersName.KAFKA_GROUP_REFRESH_CACHE) + StringUtils.getComputerName() + "_" + projectName ;
		this.kafka_topic = cache.findParamValueAsString(ParametersName.KAFKA_TOPIC_REFRESH_CACHE);
		this.projectName = projectName;
		this.lstCacheTag = lstCacheTag;
		System.out.println("GROUP_ID: " + group_id);
		System.out.println("KAFKA_TOPIC: " + kafka_topic);
		System.out.println("BOOSTRAP: " + bootstrap_servers);
	}
	
	public RefreshCacheThread(String projectName) {
		this.bootstrap_servers = cache.findParamValueAsString(ParametersName.KAFKA_BOOTSTRAP_SERVER);
		this.group_id = cache.findParamValueAsString(ParametersName.KAFKA_GROUP_REFRESH_CACHE) + StringUtils.getComputerName() + "_" + projectName ;
		this.kafka_topic = cache.findParamValueAsString(ParametersName.KAFKA_TOPIC_REFRESH_CACHE);
		this.projectName = projectName;
		this.lstCacheTag = new CacheTag[] {};
		System.out.println("GROUP_ID: " + group_id);
		System.out.println("KAFKA_TOPIC: " + kafka_topic);
		System.out.println("BOOSTRAP: " + bootstrap_servers);
	}

	@SuppressWarnings({ "deprecation", "resource" })
	@Override
	public void run() {
		Properties properties = init();
		// Create Kafka consumer
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
		try {
			consumer.subscribe(Arrays.asList(kafka_topic.split(",")));
			System.out.println("====START REFRESH THREAD====  " + projectName);
			CacheManager cacheManager = new CacheManager();
			while (true) {
				try {
					ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
					if (null != records && !records.isEmpty()) {
						for (ConsumerRecord<String, String> record : records) {
							if (record.key().equals(BusinessConstant.BIZ_ADMIN_REFRESH_KEY) && (lstCacheTag.length == 0 || isContain(record.value()))) {
								System.out.println("PROJECT "+ projectName +" : " + "|KEY: " + record.key() + "|REFRESH CACHE: " + record.value());
								cacheManager.refeshCache(record.value());
								System.out.println("PROJECT " + projectName + " : " + "|REFRESH CACHE: " + record.value() + "  ---DONE----");
							}
						}
					}
					Thread.sleep(200);
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		} finally {
			consumer.close();
		}

	}

	private Properties init() {
		Properties properties = new Properties();
		properties.put("bootstrap.servers", bootstrap_servers);
		properties.put("kafka.topic", kafka_topic);
		properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("max.partition.fetch.bytes", "20971522");
		properties.put("max.poll.records", "500");
		properties.put("group.id", group_id);

		return properties;
	}

	private boolean isContain(String value) {
	    for (int i = 0; i < lstCacheTag.length; i++) {
	    	if (lstCacheTag[i].value().equals(value)) {
	    		return true;
	    	}
	    }
	    return false;

	}
}
