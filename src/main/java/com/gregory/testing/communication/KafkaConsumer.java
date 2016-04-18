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
import java.util.concurrent.*;

import static kafka.consumer.Consumer.createJavaConsumerConnector;

public final class KafkaConsumer implements OutputChannel {

    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    private final String zookeeper;
    private final String topic;
    private final BlockingQueue<TimestampedMessage> queue = new LinkedBlockingQueue<>();

    public KafkaConsumer(String zookeeper, String topic) throws InterruptedException {
        this.zookeeper = zookeeper;
        this.topic = topic;

        executorService.submit(new Task());
    }

    @Override
    public TimestampedMessage getMessage() {
        try {
            return queue.poll(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private class Task implements Runnable {
        @Override
        public void run() {
            while(true) {
                Properties properties = new Properties();
                properties.setProperty("group.id", "kafka-consumer" + System.currentTimeMillis());
                properties.setProperty("autooffset.reset", "largest");
                properties.setProperty("zookeeper.connect", zookeeper);

                ConsumerConnector connector = createJavaConsumerConnector(new ConsumerConfig(properties));
                Map<String, Integer> topicCountMap = new HashMap<>();
                topicCountMap.put(topic, 1);
                Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = connector.createMessageStreams(topicCountMap);
                KafkaStream<byte[], byte[]> stream = consumerMap.get(topic).get(0);

                ConsumerIterator<byte[], byte[]> iterator = stream.iterator();
                while (iterator.hasNext()) {
                    byte[] data = iterator.next().message();
                    long timestamp = System.currentTimeMillis();
                    queue.add(new TimestampedMessage(timestamp, new Message(data)));
                }
            }
        }
    }

}
