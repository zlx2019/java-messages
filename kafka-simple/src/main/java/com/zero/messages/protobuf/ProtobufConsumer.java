package com.zero.messages.protobuf;

import com.zero.messages.protobuf.codec.ProtobufDeserializer;
import com.zero.messages.protobuf.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

/**
 * Kafka 消费者, 使用 Protobuf 作为消息格式
 *
 * @author Zero.
 * <p> Created on 2025/6/10 14:32 </p>
 */
@Slf4j
public class ProtobufConsumer {
    private static final String TOPIC_NAME = "proto-topic";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String GROUP_ID = "proto-consumer-group";
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // 从最早的消息开始消费
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true); // 自动提交偏移量
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000"); // 自动提交间隔
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000"); // 会话超时
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100"); // 单次拉取最大记录数

        // 创建消费者
        Consumer<String, Message.EasyMessage> consumer = new KafkaConsumer<>(props, new StringDeserializer(), new ProtobufDeserializer<>(Message.EasyMessage.getDefaultInstance()));
        try {
            // 订阅一个或多个主题
            consumer.subscribe(List.of(TOPIC_NAME));
            log.info("Subscribed to topic {}", TOPIC_NAME);
            // 批量拉取消息
            while (true) {
                ConsumerRecords<String, Message.EasyMessage> records = consumer.poll(Duration.ofMillis(300));
                if (records.isEmpty()) {
                    log.debug("No messages received...");
                    continue;
                }
                log.info("Received {} records", records.count());
                for (ConsumerRecord<String, Message.EasyMessage> record : records) {
                    log.info("Received message, Topic={}, Partition={}, Offset={}, Key={}, Value={}",
                            record.topic(), record.partition(), record.offset(), record.key(), record.value());
                }
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
