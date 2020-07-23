package com.mcredit.sharedbiz.aggregate;

import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.mcredit.model.enums.ParametersName;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;

public class QRCodeKafkaProduceAggregate {
	private String bootstrap_servers = "172.17.201.70:9092";
	private String kafka_topic = "mcportal.service.qrcode";
	private String group_id = "group.mcportal.service.qrcode";
	private ParametersCacheManager cache = CacheManager.Parameters();
	
	public QRCodeKafkaProduceAggregate() {
		this.group_id = cache.findParamValueAsString(ParametersName.KAFKA_QRCODE_GROUP);
		this.kafka_topic = cache.findParamValueAsString(ParametersName.KAFKA_QRCODE_TOPIC);
		this.bootstrap_servers = cache.findParamValueAsString(ParametersName.KAFKA_BOOTSTRAP_SERVER);
	}
	
	private Properties init(){
		Properties properties = new Properties();
		properties.put("bootstrap.servers", bootstrap_servers);
		properties.put("kafka.topic", kafka_topic);
		properties.put("compression.type", "gzip");
		properties.put("key.serializer"   , "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("value.serializer" , "org.apache.kafka.common.serialization.StringSerializer");
		
		return properties;
	}

	public void pruduce(String data) throws Exception {
		
		Properties properties = init();
		properties.put("acks"             , "1");
		properties.put("retries"          , "10");
		properties.put("batch.size"       , "20971520");
		properties.put("linger.ms"        , "33");
		properties.put("max.request.size" , "2097152");
			
		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

		try {
			System.out.println("PRODUCE KAFKA TOPIC: " + properties.getProperty("kafka.topic"));
			System.out.println("PRODUCE KAFKA TOPIC DATA: " + data);
			producer.send(new ProducerRecord<String, String>(properties.getProperty("kafka.topic"), UUID.randomUUID().toString(), data));
		} finally {
			producer.close();	
		}	
	}
	
}
