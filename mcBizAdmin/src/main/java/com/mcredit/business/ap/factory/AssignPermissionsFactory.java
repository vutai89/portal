package com.mcredit.business.ap.factory;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;

import com.mcredit.model.enums.ParametersName;
import com.mcredit.sharedbiz.cache.CacheManager;

public class AssignPermissionsFactory {
	
	public static KafkaProducer<String, String> initKafkaProducer() {
		Properties properties = new Properties();
		
		// list of host/port to use for establishing the initial connection to the Kafka cluster. 
		properties.put("bootstrap.servers", CacheManager.Parameters().findParamValue(ParametersName.KAFKA_BOOTSTRAP_SERVER));
		
		// An id string to pass to the server when making requests. 
		properties.put("client.id", "Assign Permissions");
		// Serializer class for key that implements the org.apache.kafka.common.serialization.Serializer interface.
		properties.put("key.serializer"   , "org.apache.kafka.common.serialization.StringSerializer");
		
		// Serializer class for value that implements the org.apache.kafka.common.serialization.Serializer interface. 
		properties.put("value.serializer" , "org.apache.kafka.common.serialization.StringSerializer");
		
		// 	The number of acknowledgments the producer requires the leader to have received before considering a request complete
		properties.put("acks"             , "1");
		
		properties.put("retries"          , "10");
		properties.put("batch.size"       , "20971520");
		properties.put("linger.ms"        , "33");
		properties.put("max.request.size" , "2097152");
		
		return new KafkaProducer<>(properties);
	}

}
