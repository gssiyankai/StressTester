package com.gregory.testing.communication;

import com.gregory.testing.message.TimestampedMessage;
import info.batey.kafka.unit.KafkaUnit;
import kafka.producer.KeyedMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public final class KafkaConsumerTest {

    private static final String zookeeper = "localhost:5000";
    private static final String broker = "localhost:5001";
    private static final String topic = "test";
    private KafkaUnit kafka;

    @Before
    public void setUp() {
        kafka = new KafkaUnit(zookeeper, broker);
        kafka.startup();
        kafka.createTopic(topic);
    }

    @After
    public void tearDown() {
        kafka.shutdown();
    }

    @Test
    public void it_should_read_messages_from_kafka() {
        long startTimestamp = System.currentTimeMillis();
        KafkaConsumer consumer = new KafkaConsumer(zookeeper, topic);
        KeyedMessage<String, String> keyedMessage = new KeyedMessage<>(topic, "key", "value");
        kafka.sendMessages(keyedMessage);
        TimestampedMessage message = consumer.getMessage();
        assertThat(message.timestamp()).isGreaterThan(startTimestamp);
        assertThat(message.message().data()).isEqualTo("value".getBytes());
    }

}
