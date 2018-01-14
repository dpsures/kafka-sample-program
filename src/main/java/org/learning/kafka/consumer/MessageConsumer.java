package org.learning.kafka.consumer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;

public class MessageConsumer {

	public static void main(String args[]) throws IOException{
		MessageConsumer messageConsumer = new MessageConsumer();
		messageConsumer.consumeMessages();
	}
	
	private void consumeMessages() throws IOException{
		KafkaConsumer<String, String> consumer;
		ObjectMapper mapper = new ObjectMapper();
        try (InputStream props = Resources.getResource("consumer.props").openStream()) {
            Properties properties = new Properties();
            properties.load(props);
            if (properties.getProperty("group.id") == null) {
                properties.setProperty("group.id", "group-" + new Random().nextInt(100000));
            }
            consumer = new KafkaConsumer<>(properties);
        }
        consumer.subscribe(Arrays.asList("vacations"));
        int timeouts = 0;
        
        while(true){
        	ConsumerRecords<String, String> records = consumer.poll(200);
            if (records.count() == 0) {
                timeouts++;
            } else {
                System.out.printf("Got %d records after %d timeouts\n", records.count(), timeouts);
                timeouts = 0;
            }
            
            for (ConsumerRecord<String, String> record : records) {
            	switch (record.topic()) {
            	case  "vacations":
            		JsonNode msg = mapper.readTree(record.value());
            		switch (msg.get("type").asText()) {
            		case "vacation":
            			System.out.println("mobile no:"+msg.get("mobile")+" message:"+msg.get("text"));
            			break;
            		default:
                        throw new IllegalArgumentException("Illegal message type: " + msg.get("type"));
            		}
            		break;
            	default:
                    throw new IllegalStateException("Shouldn't be possible to get message on topic " + record.topic());
            	}
            }
        }
	}
}
