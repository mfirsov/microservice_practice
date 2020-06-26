package com.mfirsov.kafkaconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaConsumerStarter {
    public static void main(String[] args) {
        SpringApplication.run(KafkaConsumerStarter.class, args);
    }
}
