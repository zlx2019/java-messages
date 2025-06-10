package com.zero.messages.simple;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Scanner;

/**
 * Kafka 生产者
 *
 * @author Zero.
 * <p> Created on 2025/6/9 22:27 </p>
 */
@Slf4j
public class SimpleProducer {
    private static final String TOPIC_NAME = "simple-topic";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    public static void main(String[] args) {
        Properties props = new Properties();
        // Kafka 地址
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "all"); // 等待所有副本确认
        props.put(ProducerConfig.RETRIES_CONFIG, 3); // 重试次数
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384); // 批处理大小
        props.put(ProducerConfig.LINGER_MS_CONFIG, 10); // 等待时间
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432); // 缓冲区大小
        // 创建生产者
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        Scanner scanner = new Scanner(System.in);
        try {
            int count = 0;
            while (true) {
                System.out.print("请输入：");
                String message = scanner.nextLine();
                if ("quit".equals(message)) {
                    break;
                }
                // 创建消息Key
                String messageKey = "key-" + count;
                // 创建消息记录
                ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, messageKey, message);
                // 同步发送消息
                RecordMetadata metadata = producer.send(record).get();
                log.info("消息发送成功! Topic: {}, Partition: {}, Offset: {}", metadata.topic(), metadata.partition(), metadata.offset());
            }
        } catch (Exception e) {
            log.error("发送消息失败", e);
        }finally {
            producer.close();
            scanner.close();
            log.info("close.");
        }
    }
}
