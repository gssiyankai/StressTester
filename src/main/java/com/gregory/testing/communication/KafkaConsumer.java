package com.gregory.testing.communication;

import com.gregory.testing.message.Message;
import com.gregory.testing.message.TimestampedMessage;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

import static kafka.consumer.Consumer.createJavaConsumerConnector;

public final class KafkaConsumer implements OutputChannel {

    private final String zookeeper;
    private final String topic;
    private final KafkaStream<byte[], byte[]> stream;
    private final Queue<TimestampedMessage> queue = new ConcurrentLinkedDeque<>();;

    public KafkaConsumer(String zookeeper, String topic) throws InterruptedException {
        this.zookeeper = zookeeper;
        this.topic = topic;

        Properties properties = new Properties();
        properties.setProperty("group.id", "kafka-consumer");
        properties.setProperty("zookeeper.connect", zookeeper);

        ConsumerConnector connector = createJavaConsumerConnector(new ConsumerConfig(properties));
        Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(topic, 1);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = connector.createMessageStreams(topicCountMap);
        stream = consumerMap.get(topic).get(0);
        new Thread(new Task()).start();
        Thread.sleep(2500);
        this.queue.clear();
    }

    @Override
    public TimestampedMessage getMessage() {
        return queue.poll();
    }

    private class Task implements Runnable {
        @Override
        public void run() {
            ConsumerIterator<byte[], byte[]> iterator = stream.iterator();
            while (iterator.hasNext()) {
                byte[] data = iterator.next().message();
                long timestamp = System.currentTimeMillis();
                queue.add(new TimestampedMessage(timestamp, new Message(data)));
            }
        }
    }

}
