package com.gregory.testing.communication;

import com.gregory.testing.message.Message;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaProducer implements Channel {

    private final String broker;
    private final String topic;

    public KafkaProducer(String broker, String topic) {
        this.broker = broker;
        this.topic = topic;
    }

    public long sendMessaage(Message message) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", broker);
        properties.setProperty("acks", "1");
        org.apache.kafka.clients.producer.KafkaProducer<String, byte[]> producer =
                new org.apache.kafka.clients.producer.KafkaProducer<>(properties, new StringSerializer(), new ByteArraySerializer());
        final ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, message.data());
        long timestamp = System.currentTimeMillis();
        producer.send(record);
        return timestamp;
    }

}
