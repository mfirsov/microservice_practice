package com.mfirsov.kafkaconsumerredis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaConsumerRedisStarter {
    public static void main(String[] args) {
        SpringApplication.run(KafkaConsumerRedisStarter.class, args);
    }
}
