package com.zero.messages.admin;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author Zero.
 * <p> Created on 2025/6/11 16:04 </p>
 */
@Slf4j
public class TopicCreate {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:19092");
        AdminClient client = AdminClient.create(props);

        // 查询所有 TOPIC
        ListTopicsResult topicsResult = client.listTopics();
        List<TopicListing> topics = topicsResult.listings().get().stream().toList();
        for (TopicListing topic : topics) {
            log.info("Topic: {} - {} - {}", topic.name(), topic.topicId(), topic.isInternal());
        }

        // 创建Topic，3个分区 1个副本
//        NewTopic topic = new NewTopic("new-topic", 3, (short) 1);
//        client.createTopics(Collections.singleton(topic)).all().get();
        client.close();
    }
}
