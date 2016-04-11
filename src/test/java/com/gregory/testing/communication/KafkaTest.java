package com.gregory.testing.communication;

import com.gregory.testing.message.Message;
import com.gregory.testing.message.TimestampedMessage;
import info.batey.kafka.unit.KafkaUnit;
import kafka.producer.KeyedMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.fest.assertions.Assertions.assertThat;

public class KafkaTest {

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
    public void it_should_read_messages_from_kafka() throws InterruptedException {
        long startTimestamp = System.currentTimeMillis();
        KafkaConsumer consumer = new KafkaConsumer(zookeeper, topic);
        Thread.sleep(1000);
        KeyedMessage<String, String> keyedMessage = new KeyedMessage<>(topic, "key", "value");
        kafka.sendMessages(keyedMessage);
        Thread.sleep(1000);
        TimestampedMessage message = consumer.getMessage();
        assertThat(message.timestamp()).isGreaterThan(startTimestamp);
        assertThat(message.message().data()).isEqualTo("value".getBytes());
    }

    @Test
    public void it_should_write_messages_to_kafka() throws InterruptedException, TimeoutException {
        KafkaProducer producer = new KafkaProducer(broker, topic);
        Thread.sleep(1000);
        producer.sendMessage(new Message("foo".getBytes()));
        List<String> messages = kafka.readMessages(topic, 1);
        assertThat(messages).containsExactly("foo");
    }

}
