package com.zero.messages.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author Zero.
 * <p> Created on 2025/6/15 16:38 </p>
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
    private final KafkaProperties kafkaProperties;

    @PostConstruct
    public void init() {
        Map<String, Object> map = kafkaProperties.getConsumer().buildProperties(null);
        map.forEach((k,v)-> {
            System.out.println(k+":"+v);
        });
    }
}
