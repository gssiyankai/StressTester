package com.gregory.testing.communication;

import com.gregory.testing.message.Message;
import com.gregory.testing.message.TimestampedMessage;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Date;
import java.util.Properties;

import static com.gregory.testing.utils.DateUtils.SIMPLE_DATE_FORMAT;

public final class KafkaProducer implements InputChannel {

    private final String broker;
    private final String topic;

    public KafkaProducer(String broker, String topic) {
        this.broker = broker;
        this.topic = topic;
    }

    @Override
    public TimestampedMessage sendMessage(Message message) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", broker);
        org.apache.kafka.clients.producer.KafkaProducer<String, byte[]> producer =
                new org.apache.kafka.clients.producer.KafkaProducer<>(properties, new StringSerializer(), new ByteArraySerializer());
        final ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, message.data());
        long timestamp = System.currentTimeMillis();
        System.out.println("@" + SIMPLE_DATE_FORMAT.format(new Date(timestamp)) + " : sending -> " + new String(message.data()));
        producer.send(record);
        return new TimestampedMessage(timestamp, message);
    }

}
