package com.zero.messages.simple;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;

/**
 * Kafka 分区消费 + 手动Ack
 *
 * @author Zero.
 * <p> Created on 2025/6/9 23:08 </p>
 */
@Slf4j
public class SimplePartitionConsumer {
    private static final String TOPIC_NAME = "simple-topic";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String GROUP_ID = "advanced-consumer-group";
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false"); // 关闭自动提交

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        try {
            consumer.subscribe(List.of(TOPIC_NAME));
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                // 按分区处理消息
                for (TopicPartition partition : records.partitions()) {
                    // 取出分区中的消息, 进行消费
                    for (ConsumerRecord<String, String> record : records.records(partition)) {
                        log.info("Received message, Topic={}, Partition={}, Offset={}, Key={}, Value={}",
                                record.topic(), record.partition(), record.offset(), record.key(), record.value());

                        // TODO do consume message
                        // 手动提交偏移量
                        Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
                        offsets.put(partition, new OffsetAndMetadata(record.offset() + 1));
                        consumer.commitSync(offsets);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            consumer.close();
        }
    }
}
