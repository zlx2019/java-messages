package com.zero.messages.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.Duration;

/// @author Zero.
///
///  Created on 2025/6/15 14:34
@Slf4j
@Component
@RequiredArgsConstructor
public class BasicMessageListener {
    @KafkaListener(topics = "string-topic", groupId = "string-topic-group")
    public void listen(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info("Received record: {} offset: {}, partition: {}", record.value(), record.offset(), record.partition());
        boolean consumeFail = record.value().equals("Failed");
        if (consumeFail){
            // 消息消费失败，响应ack失败，3秒后重新消费
            ack.nack(Duration.ofSeconds(3));
            return;
        }
        ack.acknowledge(); // 手动提交ack成功
    }
}
