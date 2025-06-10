package com.zero.messages.simple;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

/**
 * Kafka 消费者
 *
 * @author Zero.
 * <p> Created on 2025/6/9 22:40 </p>
 */
@Slf4j
public class SimpleConsumer {
    private static final String TOPIC_NAME = "simple-topic";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String GROUP_ID = "simple-consumer-group";

    public static void main(String[] args) {
        // 1. 创建消费者配置
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // 重要配置
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // 从最早的消息开始消费
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true"); // 自动提交偏移量
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000"); // 自动提交间隔
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000"); // 会话超时
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100"); // 单次拉取最大记录数

        // 创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        try {
            // 订阅一个或多个主题
            consumer.subscribe(List.of(TOPIC_NAME));
            log.info("Subscribed to topic {}", TOPIC_NAME);
            // 批量拉取消息
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(300));
                if (records.isEmpty()) {
                    log.debug("No messages received...");
                    continue;
                }
                for (ConsumerRecord<String, String> record : records) {
                    log.info("Received message, Topic={}, Partition={}, Offset={}, Key={}, Value={}",
                            record.topic(), record.partition(), record.offset(), record.key(), record.value());
                }
                // 消息手动ack
                // consumer.commitAsync();
            }
        }catch (Exception e) {
            log.error("Consumer exception", e);
            System.exit(1);
        }finally {
            consumer.close();
            log.info("Closing consumer");
        }
    }
}
