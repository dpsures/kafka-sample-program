package org.learning.kafka.producer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.google.common.io.Resources;

public class MessageProducer {

	public static void main(String[] args) throws IOException {
		MessageProducer producerApp = new MessageProducer();
		producerApp.sendMessages();
	}

	private void sendMessages() throws IOException{
		KafkaProducer<String, String> messageProducer;
		
		try(InputStream props = Resources.getResource("producer.props").openStream()){
			Properties properties = new Properties();
			properties.load(props);
			messageProducer = new KafkaProducer<>(properties);
		};
		
		try{			
			for(int i = 0; i <= 100000; i++){
				messageProducer.send(new ProducerRecord<String, String>("vacations", 
						String.format("{\"type\":\"vacation\", \"text\":\"Wishing you all a happy new year 2018!\", \"mobile\":"+i+"}", i)
						));
				System.out.println("Sending message :"+i);
			}
			System.out.println("messages are send successfully!");
		}catch(Throwable throwable){
			System.out.println("messages are send failed!");
			System.out.println(throwable.getStackTrace());
		}finally {
			messageProducer.close();
		}
	}
}
