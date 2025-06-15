package com.zero.messages.protobuf;

import com.zero.messages.protobuf.codec.ProtobufSerializer;
import com.zero.messages.protobuf.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Kafka 生产者，使用 Protobuf 作为消息格式
 *
 * @author Zero.
 * <p> Created on 2025/6/10 14:31 </p>
 */
@Slf4j
public class ProtobufProducer {
    private static final String TOPIC_NAME = "proto-topic";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ProtobufSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "all"); // 等待所有副本确认
        props.put(ProducerConfig.RETRIES_CONFIG, 3); // 重试次数
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384); // 批处理大小
        props.put(ProducerConfig.LINGER_MS_CONFIG, 10); // 等待时间
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432); // 缓冲区大小

        // 创建生产者
        Producer<String, Message.EasyMessage> producer = new KafkaProducer<>(props);
//        Producer<String, Message.EasyMessage> producer = new KafkaProducer<>(props, new StringSerializer(), new KafkaProtobufSerializer<>());
        // 创建消息
        Message.EasyRequestMessage requestMessage = Message.EasyRequestMessage.newBuilder()
                .setClientId(UUID.randomUUID().toString()).build();
        Message.EasyMessage easyMessage = Message.EasyMessage.newBuilder()
                .setMagic(111)
                .setVersion(Message.MessageVersion.V1)
                .setType(Message.MessageType.ONLINE)
                .setId(1001)
                .setTimestamp(System.currentTimeMillis())
                .setRequest(requestMessage).build();
        ProducerRecord<String, Message.EasyMessage> record =
                new ProducerRecord<>(TOPIC_NAME, "proto-msg-key", easyMessage);
        // 发送消息
        RecordMetadata metadata = producer.send(record).get();
        log.info("消息发送成功! Topic: {}, Partition: {}, Offset: {}", metadata.topic(), metadata.partition(), metadata.offset());
        producer.close();
    }
}
