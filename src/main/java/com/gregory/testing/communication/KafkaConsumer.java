package com.gregory.testing.communication;

import com.gregory.testing.message.Message;
import com.gregory.testing.message.TimestampedMessage;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static kafka.consumer.Consumer.createJavaConsumerConnector;

public class KafkaConsumer implements Channel {

    private final String zookeeper;
    private final String topic;

    public KafkaConsumer(String zookeeper, String topic) {
        this.zookeeper = zookeeper;
        this.topic = topic;
    }

    public TimestampedMessage getMessage() {
        Properties properties = new Properties();
        properties.setProperty("group.id", "kafka-consumer");
        properties.setProperty("zookeeper.connect", zookeeper);
        properties.setProperty("zookeeper.session.timeout.ms", "10000");
        properties.setProperty("zookeeper.sync.time.ms", "200");
        properties.setProperty("auto.commit.interval.ms", "1000");
        properties.setProperty("serializer.class", "kafka.serializer.DefaultEncoder");
        properties.setProperty("key.serializer.class", "kafka.serializer.StringEncoder");

        ConsumerConnector connector = createJavaConsumerConnector(new ConsumerConfig(properties));
        Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(topic, 1);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = connector.createMessageStreams(topicCountMap);

        final KafkaStream<byte[], byte[]> stream = consumerMap.get(topic).get(0);
        ConsumerIterator<byte[], byte[]> iterator = stream.iterator();
        if(iterator.hasNext()) {
            byte[] message = iterator.next().message();
            long timestamp = System.currentTimeMillis();
            return new TimestampedMessage(timestamp, new Message(message));
        }
        return null;
    }

}
