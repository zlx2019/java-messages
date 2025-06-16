package com.zero.messages.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Zero.
 * <p> Created on 2025/6/15 14:40 </p>
 */
@RestController
@RequestMapping("/send")
@RequiredArgsConstructor
public class SendController {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping
    public String send(@RequestParam("topic") String topic, @RequestParam("message") String message, @RequestParam("count") Integer count) {
        for (int i = 0; i < count; i++) {
            String key = UUID.randomUUID().toString();
            kafkaTemplate.send(topic, key, message);
        }
        return "Success";
    }
}
